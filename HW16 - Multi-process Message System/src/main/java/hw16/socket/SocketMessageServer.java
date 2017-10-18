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

    public static final int PORT = 8080;
    private static final int CAPACITY = 10;
    private static final String MESSAGES_SEPARATOR = "\n\n";

    public static final Address DB_ADDRESS = new Address("db");

    private final Map<String, SocketChannel> channels = new HashMap<>();
    private final Map<Address, SocketChannel> addresses = new HashMap<>();
    private final Queue<Address> dbAddresses = new LinkedList<>();

    private StringBuilder readBuilder = new StringBuilder();


    @SuppressWarnings("InfiniteLoopStatement")
    public void start() throws Exception {

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

        channels.put(remoteAddress, channel);
    }

    private void read(SelectionKey key) throws IOException, ParseException, ClassNotFoundException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);

        int read = channel.read(buffer);
        if (read != -1) {
            String part = new String(buffer.array()).trim();
            readBuilder.append(part);

            if (part.length() != read) {
                Message msg = readMessage(channel);
                if (msg != null) writeMessage(msg);
            }
        } else {
            key.cancel();
            String remoteAddress = channel.getRemoteAddress().toString();
            channels.remove(remoteAddress);
            logger.info("Connection closed, key canceled");
        }
    }

    private Message readMessage(SocketChannel channel) throws ParseException, ClassNotFoundException, IOException {
        Message msg = Message.getMsgFromJSON(readBuilder.toString());
        readBuilder = new StringBuilder();

        logger.info("Message received: " + msg + " from: " + channel.getRemoteAddress());

        if (msg instanceof AddressMessage) {
            addresses.put(msg.getFrom(), channels.get(channel.getRemoteAddress().toString()));
            logger.info("Accept " + msg.getFrom());

            if ((msg.getFrom().getId().split("@")[0]).equals(DB_ADDRESS.getId())) {
                dbAddresses.add(msg.getFrom());
            }

            return null;
        }
        else return msg;
    }

    private void writeMessage(Message message) throws IOException {
        Address to = message.getTo().equals(DB_ADDRESS) ? pickDbAddress() : message.getTo();

        String json = new Gson().toJson(message) + MESSAGES_SEPARATOR;
        ByteBuffer buffer = ByteBuffer.wrap(json.getBytes());

        logger.info("Send message" + message + ", to " + to + " (" + addresses.get(to).getRemoteAddress() + ")");

        while (buffer.hasRemaining()) {
            addresses.get(to).write(buffer);
        }
    }

    private Address pickDbAddress() {
        Address result = dbAddresses.poll();
        dbAddresses.add(result);

        return result;
    }
}
