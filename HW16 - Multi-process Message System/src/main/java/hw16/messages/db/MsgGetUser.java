package hw16.messages.db;

import db_service.CachedUserDBService;
import hw16.db.MessageSystemDBService;
import hw16.message_system.Address;
import hw16.message_system.MessageSystemContext;
import hw16.messages.frontend.MsgGetUserAnswer;

public class MsgGetUser extends MsgToDB {
    private String username;
    private String sessionId;

    public MsgGetUser(Address from, Address to, String username, String sessionId) {
        super(from, to);
        this.username = username;
        this.sessionId = sessionId;
    }

    @Override
    public void exec(MessageSystemDBService dbService) {
        dbService.getMessageSystemContext().getMessageSystem().sendMessage(new MsgGetUserAnswer(getTo(), getFrom(),
                dbService.get(username), sessionId));
    }
}
