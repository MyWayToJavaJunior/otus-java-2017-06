package db;

import cache.CacheImpl;
import db_service.CachedUserDBService;
import message_system.Address;
import message_system.MessageSystem;
import message_system.message.MsgToDB;

import javax.websocket.Session;

public class MsgGetCacheImpl extends MsgToDB {
    private final MessageSystem messageSystem;
    private final Session session;

    public MsgGetCacheImpl(MessageSystem messageSystem, Address from, Address to, Session session) {
        super(from, to);
        this.messageSystem = messageSystem;
        this.session = session;
    }

    @Override
    public void exec(CachedUserDBService dbService) {
        messageSystem.sendMessage(new MsgGetCacheImplAnswer(getTo(), getFrom(), session, (CacheImpl) dbService.getCache()));
    }
}
