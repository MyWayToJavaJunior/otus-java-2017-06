package hw16;

import hw16.frontend.FrontendService;
import hw16.frontend.websocket.AdminWS;
import hw16.frontend.websocket.MessageSystemConfigurator;
import hw16.message_system.Address;
import hw16.message_system.MessageSystem;
import hw16.message_system.MessageSystemContext;
import hw16.message_system.ProcessMessageSystem;
import hw16.socket.SocketMessageClient;
import hw16.socket.SocketMessageServer;
import lombok.Data;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.ServerContainer;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import javax.websocket.server.ServerEndpointConfig;
import java.net.Socket;

@Data
public class FrontendMain {
    private static String host = "localhost";
    private static int port;

    private MessageSystemContext context;

    public static void main(String[] args) throws Exception {
        if (args.length != 2) port = 7070;
        else port = Integer.parseInt(args[1]);

        FrontendMain main = new FrontendMain();
        main.runMessageSystem();
        main.runJettyServer();
    }

    private void runMessageSystem() throws Exception {
        MessageSystem messageSystem = new ProcessMessageSystem(new SocketMessageClient(new Socket(host, SocketMessageServer.PORT)));
        context = new MessageSystemContext(messageSystem);

        FrontendService frontendService = new FrontendService(new Address(FrontendService.FRONTEND_ADDRESS_PREFIX + port),
                context);
        messageSystem.addAddressee(frontendService);

        context.setFrontAddress(frontendService.getAddress());
        context.setDbAddress(SocketMessageServer.DB_ADDRESS);

        messageSystem.start();
    }

    private void runJettyServer() throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        Server server = new Server(port);
        server.setHandler(context);

        ServerContainer wsContainer = WebSocketServerContainerInitializer.configureContext(context);
        wsContainer.addEndpoint(ServerEndpointConfig.Builder
                .create(AdminWS.class, "/admin")
                .configurator(new MessageSystemConfigurator(this.context))
                .build());

        server.start();
        server.join();
    }
}
