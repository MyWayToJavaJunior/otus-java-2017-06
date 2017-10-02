package hw16.socket;

import hw16.message_system.Addressee;
import hw16.message_system.Message;
import hw16.messages.AddressMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketMessageClient {
    private static final Logger logger = Logger.getLogger(SocketMessageClient.class.getName());

    private static final String HOST = "localhost";
    private static final int PAUSE_MS = 5000;
    private static final int MAX_MESSAGES_COUNT = 10;

    private final Addressee addressee;
    private SocketMessageHandler client;

    public SocketMessageClient(Addressee addressee) {
        this.addressee = addressee;
    }

    public void run() throws InterruptedException, IOException {
        client = new SocketMessageHandler(new Socket(HOST, SocketMessageServer.PORT));
        client.init();

        client.send(new AddressMessage(addressee.getAddress()));

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(() -> {
            try {
                while (true) {
                    Message msg = client.take();

                    msg.exec(addressee);
                }
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        });
    }

    public void sendMessage(Message msg) {
        client.send(msg);
    }
}
