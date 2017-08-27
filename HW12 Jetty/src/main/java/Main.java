import cache.CacheImpl;
import db_service.CachedUserDBService;
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

public class Main {
    private final static int PORT = 7070;

    public static void main(String[] args) throws Exception {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(TemplateProcessor.STATIC_DIR);

        CachedUserDBService dbService = new CachedUserDBService(new DBServiceImpl(), new CacheImpl<Long, UserDataSet>(10, 1000, 5, true));

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(new LoginServlet("secret")), LoginServlet.URL);
        context.addServlet(new ServletHolder(new AdminServlet(dbService)), AdminServlet.URL);

        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, context));

        server.start();
        server.join();
    }
}
