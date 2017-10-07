package hw16.message_system;

import hw16.messages.AddressMessage;
import hw16.socket.SocketMessageClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ProcessMessageSystem implements MessageSystem {
    private static final Logger logger = Logger.getLogger(MessageSystem.class.getName());

    private static final String HOST = "localhost";

    private final List<Addressee> addressees = new ArrayList<>();
    private SocketMessageClient client;

    public ProcessMessageSystem(SocketMessageClient client) {
        this.client = client;
    }

    @Override
    public void addAddressee(Addressee addressee) {
        addressees.add(addressee);
    }

    @Override
    public void start() throws InterruptedException, IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(addressees.size());

        for (Addressee addressee : addressees) {
            client.send(new AddressMessage(addressee.getAddress()));
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
    }

    @Override
    public void sendMessage(Message message) {
        client.send(message);
    }
}
