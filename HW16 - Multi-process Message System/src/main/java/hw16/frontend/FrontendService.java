package hw16.frontend;

import cache.CacheImpl;
import hw10.dataset.UserDataSet;
import hw16.message_system.Address;
import hw16.message_system.Addressee;
import hw16.message_system.MessageSystemContext;

import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FrontendService implements Addressee {
    public static final String FRONTEND_ADDRESS_PREFIX = "frontend-";

    private final Address address;
    private final MessageSystemContext context;
    private static final Map<String, Session> sessions = new HashMap<>();

    public FrontendService(Address address, MessageSystemContext context) {
        this.address = address;
        this.context = context;
    }

    public void sendAnswer(Session session, CacheImpl cache) {
        String data = "hit: " + cache.getHitCount() + "\n"
                + "miss: " + cache.getMissCount() + "\n"
                + "max_size: " + cache.getMaxElements() + "\n"
                + "cur_size: " + cache.getCurCacheSize() + "\n"
                + "life_time: " + cache.getLifeTimeMs() + "\n"
                + "idle_time: " + cache.getIdleTimeMs() + "\n"
                + "eternal: " + cache.isEternal();

        try {
            session.getBasicRemote().sendText(data);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void sendUserAnswer(String sessionId, UserDataSet userDataSet) {
        Session session = sessions.get(sessionId);
        try {
            session.getBasicRemote().sendText(userDataSet.toString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public static String addSession(Session session) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, session);

        return sessionId;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystemContext getMessageSystemContext() {
        return context;
    }
}
