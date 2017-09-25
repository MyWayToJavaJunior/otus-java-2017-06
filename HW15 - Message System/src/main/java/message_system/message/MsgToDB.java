package message_system.message;

import db_service.CachedUserDBService;
import hw10.db_service.DBService;
import message_system.Address;
import message_system.Addressee;
import message_system.Message;

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
            exec((CachedUserDBService) addressee);
        } else throw new RuntimeException();
    }

    public abstract void exec(CachedUserDBService dbService);
}
