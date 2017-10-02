package hw16.socket;

import hw16.message_system.Address;
import hw16.message_system.Message;
import hw16.messages.AddressMessage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class SocketMessageServer {
    private static final Logger logger = Logger.getLogger(SocketMessageServer.class.getName());

    public static final int PORT = 8080;

    public static final Address DB_ADDRESS = new Address("db");

    private static final int THREADS_NUMBER = 1;
    private static final int DELAY = 10;

    private final ExecutorService executor = Executors.newFixedThreadPool(THREADS_NUMBER);
    private final List<MessageHandler> clients = new ArrayList<>();
    private final Map<Address, MessageHandler> addressMessageHandlerMap = new HashMap<>();
    private final Queue<Address> dbAddresses = new LinkedList<>();

    @SuppressWarnings("InfiniteLoopStatement")
    public void start() throws Exception {
        executor.submit(this::handle);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("Server started on port: " + serverSocket.getLocalPort());
            while(true) {
                Socket socket = serverSocket.accept();
                SocketMessageHandler client = new SocketMessageHandler(socket);
                client.init();
                clients.add(client);

                Message msg = client.take();
                if (msg instanceof AddressMessage) {
                    addressMessageHandlerMap.put(msg.getFrom(), client);
                    logger.info("Accept " + msg.getFrom());

                    if ((msg.getFrom().getId().split("@")[0]).equals(DB_ADDRESS.getId())) {
                        dbAddresses.add(msg.getFrom());
                    }
                }
                else throw new RuntimeException("AddressMessage as first message is required");
            }
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private Object handle() throws InterruptedException {

        while (true) {
            for (MessageHandler client : clients) {
                Message msg = client.poll();

                while (msg != null) {
                    Address to = msg.getTo().equals(DB_ADDRESS) ? pickDbAddress() : msg.getTo();
                    addressMessageHandlerMap.get(to).send(msg);
                    logger.info(msg.getClass().getName() + ":" + msg.getFrom() + " -> " + to);
                    msg = client.poll();
                }
            }
            Thread.sleep(DELAY);
        }
    }

    private Address pickDbAddress() {
        Address result = dbAddresses.poll();
        dbAddresses.add(result);

        return result;
    }
}
