package hw16;

import hw16.frontend.FrontendService;
import hw16.frontend.websocket.AdminWS;
import hw16.message_system.Address;
import hw16.message_system.MessageSystemContext;
import hw16.socket.SocketMessageClient;
import hw16.socket.SocketMessageServer;
import lombok.Data;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.ServerContainer;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import java.io.IOException;

@Data
public class FrontendMain {
    private static int port;

    public static void main(String[] args) throws Exception {
        if (args.length != 2) port = 7070;
        else port = Integer.parseInt(args[1]);

        runMessageSyste();
        runJettyServer();
    }

    public static void runMessageSyste() throws IOException, InterruptedException {
        FrontendService frontendService = new FrontendService(new Address(FrontendService.FRONTEND_ADDRESS_PREFIX + port));

        SocketMessageClient socketMessageClient = new SocketMessageClient(frontendService);
        socketMessageClient.run();

        MessageSystemContext.setClient(socketMessageClient);
        MessageSystemContext.setFrontAddress(frontendService.getAddress());
        MessageSystemContext.setDbAddress(SocketMessageServer.DB_ADDRESS);
    }

    public static void runJettyServer() throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        Server server = new Server(port);
        server.setHandler(context);

        ServerContainer wsContainer = WebSocketServerContainerInitializer.configureContext(context);
        wsContainer.addEndpoint(AdminWS.class);

        server.start();
        server.join();
    }
}
