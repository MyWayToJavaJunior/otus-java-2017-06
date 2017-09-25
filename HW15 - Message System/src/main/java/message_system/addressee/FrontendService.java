package message_system.addressee;

import cache.CacheImpl;
import message_system.Address;
import message_system.Addressee;

import javax.websocket.Session;

public class FrontendService implements Addressee {
    private final Address address = new Address("websocket");

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

    @Override
    public Address getAddress() {
        return address;
    }
}
