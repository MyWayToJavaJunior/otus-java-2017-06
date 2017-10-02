package hw16.messages.frontend;

import cache.CacheImpl;
import hw16.message_system.Address;
import hw16.frontend.FrontendService;

import javax.websocket.Session;

public class MsgGetCacheImplAnswer extends MsgToFrontend {
    private final CacheImpl cache;
    private final Session session;

    public MsgGetCacheImplAnswer(Address from, Address to, Session session, CacheImpl cache) {
        super(from, to);
        this.cache = null;
        this.session = session;
    }

    @Override
    public void exec(FrontendService frontendService) {
        frontendService.sendAnswer(session, cache);
    }
}
