package hw16;

import cache.CacheImpl;
import db_service.CachedUserDBServiceImpl;
import hw10.dataset.AddressDataSet;
import hw10.dataset.PhoneDataSet;
import hw10.dataset.UserDataSet;
import hw10.db_service.DBService;
import hw10.db_service.DBServiceImpl;
import hw16.db.MessageSystemDBService;
import hw16.message_system.MessageSystemContext;
import hw16.socket.SocketMessageClient;

import java.io.IOException;
import java.util.Arrays;

public class DBServerMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        MessageSystemDBService messageSystemDBService = new MessageSystemDBService(new CachedUserDBServiceImpl(
                new DBServiceImpl(), new CacheImpl<>(1, 11, 1, true)));

        seedDB(messageSystemDBService);

        SocketMessageClient socketMessageClient = new SocketMessageClient(messageSystemDBService);
        socketMessageClient.run();

        MessageSystemContext.setClient(socketMessageClient);
        MessageSystemContext.setDbAddress(messageSystemDBService.getAddress());
    }

    private static void seedDB(DBService dbService) {
        UserDataSet admin = new UserDataSet("Admin", "secret", 1, new AddressDataSet("Street"),
                Arrays.asList(new PhoneDataSet("+70001112200")));
        admin.setRole("ADMIN");
        dbService.save(admin);
    }
}
