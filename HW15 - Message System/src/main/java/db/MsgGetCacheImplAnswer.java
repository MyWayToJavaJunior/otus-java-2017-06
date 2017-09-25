package db;

import cache.CacheImpl;
import db_service.CachedUserDBService;
import message_system.Address;
import message_system.MessageSystem;
import message_system.addressee.FrontendService;
import message_system.message.MsgToDB;
import message_system.message.MsgToFrontend;

import javax.websocket.Session;

public class MsgGetCacheImplAnswer extends MsgToFrontend {
    private final CacheImpl cache;
    private final Session session;

    public MsgGetCacheImplAnswer(Address from, Address to, Session session, CacheImpl cache) {
        super(from, to);
        this.cache = cache;
        this.session = session;
    }

    @Override
    public void exec(FrontendService frontendService) {
        frontendService.sendAnswer(session, cache);
    }
}
