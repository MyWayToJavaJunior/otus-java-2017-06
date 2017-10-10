package hw16.socket;

import com.google.gson.Gson;
import hw16.message_system.Address;
import hw16.message_system.Message;
import hw16.messages.AddressMessage;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketMessageServer {
    private static final Logger logger = Logger.getLogger(SocketMessageServer.class.getName());

    private static final int THREADS_NUMBER = 1;
    public static final int PORT = 8080;
    private static final int ECHO_DELAY = 100;
    private static final int CAPACITY = 10;
    private static final String MESSAGES_SEPARATOR = "\n\n";

    public static final Address DB_ADDRESS = new Address("db");

    private final ExecutorService executor = Executors.newFixedThreadPool(THREADS_NUMBER);
    private final Map<String, ChannelMessages> channelMessages = new ConcurrentHashMap<>();
    private final Map<Address, ChannelMessages> addresses = new ConcurrentHashMap<>();
    private final Queue<Address> dbAddresses = new ConcurrentLinkedQueue<>();

    private StringBuilder readBuilder = new StringBuilder();


    @SuppressWarnings("InfiniteLoopStatement")
    public void start() throws Exception {
        executor.submit(this::write);

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress("localhost", PORT));

            serverSocketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, null);

            logger.info("Started on port: " + PORT);

            listen(selector, serverSocketChannel);
        }
    }

    private void listen(Selector selector, ServerSocketChannel serverChannel) throws IOException {
        while (true) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                try {
                    if (key.isAcceptable())
                        accept(selector, serverChannel);
                    else if (key.isReadable())
                        read(key);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, e.getMessage());
                } finally {
                    iterator.remove();
                }
            }
        }
    }

    private void accept(Selector selector, ServerSocketChannel serverChannel) throws IOException {
        SocketChannel channel = serverChannel.accept();
        String remoteAddress = channel.getRemoteAddress().toString();
        logger.info("Connection Accepted: " + remoteAddress);

        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);

        channelMessages.put(remoteAddress, new ChannelMessages(channel));
    }

    private void read(SelectionKey key) throws IOException, ParseException, ClassNotFoundException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);

        int read = channel.read(buffer);
        if (read != -1) {
            String part = new String(buffer.array()).trim();
            readBuilder.append(part);

            if (part.length() != read) {
                readMessage(channel);
                return;
            }
        } else {
            key.cancel();
            String remoteAddress = channel.getRemoteAddress().toString();
            channelMessages.remove(remoteAddress);
            logger.info("Connection closed, key canceled");
        }
    }

    private void readMessage(SocketChannel channel) throws ParseException, ClassNotFoundException, IOException {
        Message msg = Message.getMsgFromJSON(readBuilder.toString());
        readBuilder = new StringBuilder();

        logger.info("Message received: " + msg + " from: " + channel.getRemoteAddress());

        if (msg instanceof AddressMessage) {
            addresses.put(msg.getFrom(), channelMessages.get(channel.getRemoteAddress().toString()));
            logger.info("Accept " + msg.getFrom());

            if ((msg.getFrom().getId().split("@")[0]).equals(DB_ADDRESS.getId())) {
                dbAddresses.add(msg.getFrom());
            }
        }
        else channelMessages.get(channel.getRemoteAddress().toString()).messages.add(msg);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private Object write() throws InterruptedException {
        while (true) {
            for (Map.Entry<String, ChannelMessages> entry : channelMessages.entrySet()) {
                ChannelMessages channelMessages = entry.getValue();
                if (channelMessages.channel.isConnected()) {
                    channelMessages.messages.forEach(message -> {
                        try {
                            writeMessage(message);
                        } catch (IOException e) {
                            logger.log(Level.SEVERE, e.getMessage());
                        }
                    });
                    channelMessages.messages.clear();
                }
            }
            Thread.sleep(ECHO_DELAY);
        }
    }

    private void writeMessage(Message message) throws IOException {
        Address to = message.getTo().equals(DB_ADDRESS) ? pickDbAddress() : message.getTo();

        String json = new Gson().toJson(message) + MESSAGES_SEPARATOR;
        ByteBuffer buffer = ByteBuffer.wrap(json.getBytes());

        logger.info("Send message" + message + ", to " + to + " (" + addresses.get(to).channel.getRemoteAddress() + ")");

        while (buffer.hasRemaining()) {
            addresses.get(to).channel.write(buffer);
        }
    }

    private class ChannelMessages {
        private final SocketChannel channel;
        private final List<Message> messages = new ArrayList<>();

        private ChannelMessages(SocketChannel channel) {
            this.channel = channel;
        }
    }

    private Address pickDbAddress() {
        Address result = dbAddresses.poll();
        dbAddresses.add(result);

        return result;
    }
}
