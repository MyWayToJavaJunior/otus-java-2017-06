package hw16.messages.db;

import cache.CacheImpl;
import db_service.CachedUserDBService;
import hw16.db.MessageSystemDBService;
import hw16.message_system.Address;
import hw16.message_system.MessageSystemContext;
import hw16.messages.frontend.MsgGetCacheImplAnswer;

import javax.websocket.Session;

public class MsgGetCacheImpl extends MsgToDB {
    private final Session session;

    public MsgGetCacheImpl(Address from, Address to, Session session) {
        super(from, to);
        this.session = session;
    }

    @Override
    public void exec(MessageSystemDBService dbService) {
        dbService.getMessageSystemContext().getMessageSystem().sendMessage(new MsgGetCacheImplAnswer(getTo(),
                getFrom(), session, (CacheImpl) dbService.getCache()));
    }
}
