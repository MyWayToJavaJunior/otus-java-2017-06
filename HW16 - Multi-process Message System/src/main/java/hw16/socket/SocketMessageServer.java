package hw16.socket;

import com.google.gson.Gson;
import hw16.message_system.Address;
import hw16.message_system.Message;
import hw16.messages.AddressMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketMessageServer {
    private static final Logger logger = Logger.getLogger(SocketMessageServer.class.getName());

    private static final int THREADS_NUMBER = 1;
    public static final int PORT = 8080;
    private static final int ECHO_DELAY = 100;
    private static final int CAPACITY = 1024;
    private static final String MESSAGES_SEPARATOR = "\n\n";

    private final ExecutorService executor;
    private final Map<String, ChannelMessages> channelMessages;
    private final Map<Address, ChannelMessages> addresses = new HashMap<>();
    private final Queue<Address> dbAddresses = new LinkedList<>();

    public static final Address DB_ADDRESS = new Address("db");

    public SocketMessageServer() {
        executor = Executors.newFixedThreadPool(THREADS_NUMBER);
        channelMessages = new ConcurrentHashMap<>();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void start() throws Exception {
        executor.submit(this::echo);

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress("localhost", PORT));

            serverSocketChannel.configureBlocking(false); //non blocking mode
            int ops = SelectionKey.OP_ACCEPT;
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, ops, null);

            logger.info("Started on port: " + PORT);

            while (true) {
                selector.select();//blocks
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    try {
                        if (key.isAcceptable()) {
                            SocketChannel channel = serverSocketChannel.accept(); //non blocking accept
                            String remoteAddress = channel.getRemoteAddress().toString();
                            System.out.println("Connection Accepted: " + remoteAddress);

                            channel.configureBlocking(false);
                            channel.register(selector, SelectionKey.OP_READ);

                            channelMessages.put(remoteAddress, new ChannelMessages(channel));

                        } else if (key.isReadable()) {
                            SocketChannel channel = (SocketChannel) key.channel();

                            ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);
                            int read = channel.read(buffer);
                            if (read != -1) {
                                String json = new String(buffer.array()).trim();
                                if (json.equals("")) continue;

                                Message msg = Message.getMsgFromJSON(json);
                                System.out.println("Message received: " + msg + " from: " + channel.getRemoteAddress());

                                if (msg instanceof AddressMessage) {
                                    addresses.put(msg.getFrom(), channelMessages.get(channel.getRemoteAddress().toString()));
                                    logger.info("Accept " + msg.getFrom());

                                    if ((msg.getFrom().getId().split("@")[0]).equals(DB_ADDRESS.getId())) {
                                        dbAddresses.add(msg.getFrom());
                                    }
                                }
                                else channelMessages.get(channel.getRemoteAddress().toString()).messages.add(msg);

                            } else {
                                key.cancel();
                                String remoteAddress = channel.getRemoteAddress().toString();
                                channelMessages.remove(remoteAddress);
                                System.out.println("Connection closed, key canceled");
                            }
                        }
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, e.getMessage());
                    } finally {
                        iterator.remove();
                    }
                }
            }
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private Object echo() throws InterruptedException {
        while (true) {
            for (Map.Entry<String, ChannelMessages> entry : channelMessages.entrySet()) {
                ChannelMessages channelMessages = entry.getValue();
                if (channelMessages.channel.isConnected()) {
                    channelMessages.messages.forEach(message -> {
                        try {
                            Address to = message.getTo().equals(DB_ADDRESS) ? pickDbAddress() : message.getTo();

                            ByteBuffer buffer = ByteBuffer.allocate(CAPACITY);
                            buffer.put(new Gson().toJson(message).getBytes());
                            buffer.put(MESSAGES_SEPARATOR.getBytes());
                            buffer.flip();

                            System.out.println("Send message" + message + ", to " + to +
                                    " (" + addresses.get(to).channel.getRemoteAddress() + ")");
                            while (buffer.hasRemaining()) {
                                addresses.get(to).channel.write(buffer);
                            }
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
