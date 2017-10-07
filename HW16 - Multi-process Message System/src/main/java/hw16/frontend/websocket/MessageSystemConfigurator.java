package hw16.frontend.websocket;

import hw16.message_system.MessageSystemContext;

import javax.websocket.server.ServerEndpointConfig;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageSystemConfigurator extends ServerEndpointConfig.Configurator {
    private static final Logger logger = Logger.getLogger(MessageSystemConfigurator.class.getName());

    private MessageSystemContext context;

    public MessageSystemConfigurator(MessageSystemContext context) {
        this.context = context;
    }

    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        return (T) new AdminWS(context);
    }
}