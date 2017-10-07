package hw16.frontend.websocket;


import hw16.frontend.FrontendService;
import hw16.message_system.Message;
import hw16.message_system.MessageSystemContext;
import hw16.messages.db.MsgGetCacheImpl;
import hw16.messages.db.MsgGetUser;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.UUID;

@ServerEndpoint(value = "/admin")
public class AdminWS {
    private final MessageSystemContext context;

    public AdminWS(MessageSystemContext context) {
        this.context = context;
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        String sessionId = FrontendService.addSession(session);

        context.getMessageSystem().sendMessage(new MsgGetUser(context.getFrontAddress(),
                context.getDbAddress(), "Admin", sessionId));
    }
}
