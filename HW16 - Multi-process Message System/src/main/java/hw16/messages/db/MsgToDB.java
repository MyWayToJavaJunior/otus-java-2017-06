package hw16.messages.db;

import db_service.CachedUserDBService;
import hw10.db_service.DBService;
import hw16.db.MessageSystemDBService;
import hw16.message_system.Address;
import hw16.message_system.Addressee;
import hw16.message_system.Message;

/**
 * Created by tully.
 */
public abstract class MsgToDB extends Message {
    public MsgToDB(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof DBService) {
            exec((MessageSystemDBService) addressee);
        } else throw new RuntimeException();
    }

    public abstract void exec(MessageSystemDBService dbService);
}
