package hw16.messages.frontend;

import hw10.dataset.UserDataSet;
import hw16.frontend.FrontendService;
import hw16.message_system.Address;

public class MsgGetUserAnswer extends MsgToFrontend {
    private final String sessionId;
    private final UserDataSet userDataSet;

    public MsgGetUserAnswer(Address from, Address to, UserDataSet userDataSet, String sessionId) {
        super(from, to);
        this.sessionId = sessionId;
        userDataSet.setAddress(null);
        userDataSet.setPhones(null);
        this.userDataSet = userDataSet;
    }

    @Override
    public void exec(FrontendService frontendService) {
        frontendService.sendUserAnswer(sessionId, userDataSet);
    }
}
