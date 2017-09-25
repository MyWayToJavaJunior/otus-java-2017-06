package websocket;

import db.MsgGetCacheImpl;
import message_system.Message;
import message_system.MessageSystemContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/admin", configurator = SpringConfigurator.class)
public class AdminWS {
    @Autowired private MessageSystemContext context;

    @OnMessage
    public void handleMessage(String message, Session session) {
        Message msg = new MsgGetCacheImpl(context.getMessageSystem(), context.getFrontAddress(), context.getDbAddress(), session);
        context.getMessageSystem().sendMessage(msg);
    }
}
