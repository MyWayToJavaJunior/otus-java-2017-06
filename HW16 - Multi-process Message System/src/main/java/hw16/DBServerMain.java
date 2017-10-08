package hw16;

import cache.CacheImpl;
import db_service.CachedUserDBService;
import db_service.CachedUserDBServiceImpl;
import hw10.dataset.AddressDataSet;
import hw10.dataset.PhoneDataSet;
import hw10.dataset.UserDataSet;
import hw10.db_service.DBService;
import hw10.db_service.DBServiceImpl;
import hw16.db.MessageSystemDBService;
import hw16.message_system.MessageSystem;
import hw16.message_system.MessageSystemContext;
import hw16.message_system.ProcessMessageSystem;
import hw16.socket.SocketMessageClient;
import hw16.socket.SocketMessageServer;

import java.net.Socket;
import java.util.Arrays;

public class DBServerMain {
    private static String host = "localhost";

    public static void main(String[] args) throws Exception {
        MessageSystem messageSystem = new ProcessMessageSystem(new SocketMessageClient(new Socket(host, SocketMessageServer.PORT)));
        MessageSystemContext context = new MessageSystemContext(messageSystem);

        CachedUserDBService dbService = new CachedUserDBServiceImpl(new DBServiceImpl(),
                new CacheImpl<>(1, 11, 1, true));
        MessageSystemDBService messageSystemDBService = new MessageSystemDBService(context, dbService);
        messageSystem.addAddressee(messageSystemDBService);

        seedDB(messageSystemDBService);

        context.setDbAddress(messageSystemDBService.getAddress());

        messageSystem.start();
    }

    private static void seedDB(DBService dbService) {
        UserDataSet admin = new UserDataSet("Admin", "secret", 1, new AddressDataSet("Street"),
                Arrays.asList(new PhoneDataSet("+70001112200")));
        admin.setRole("ADMIN");
        dbService.save(admin);
    }
}
