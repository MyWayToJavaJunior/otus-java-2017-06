import cache.CacheImpl;
import db_service.CachedUserDBService;
import db_service.CachedUserDBServiceImpl;
import hw10.dataset.AddressDataSet;
import hw10.dataset.PhoneDataSet;
import hw10.dataset.UserDataSet;
import hw10.db_service.DBServiceImpl;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlet.AdminServlet;
import servlet.LoginServlet;
import servlet.TemplateProcessor;

import java.util.Arrays;

public class Main {
    private final static int PORT = 7070;

    public static void main(String[] args) throws Exception {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(TemplateProcessor.STATIC_DIR);

        CachedUserDBService dbService = new CachedUserDBServiceImpl(new DBServiceImpl(), new CacheImpl<>(10, 1000, 5, true));

        UserDataSet admin = new UserDataSet("Admin", "secret", 1, new AddressDataSet("Street"),
                Arrays.asList(new PhoneDataSet("+70001112200")));
        admin.setRole("ADMIN");
        dbService.save(admin);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(new LoginServlet(dbService)), LoginServlet.URL);
        context.addServlet(new ServletHolder(new AdminServlet(dbService)), AdminServlet.URL);

        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, context));

        server.start();
        server.join();
    }
}
