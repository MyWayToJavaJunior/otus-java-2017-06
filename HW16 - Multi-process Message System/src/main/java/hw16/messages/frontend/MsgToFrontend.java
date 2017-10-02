package hw16.messages.frontend;

import hw16.message_system.Address;
import hw16.message_system.Addressee;
import hw16.message_system.Message;
import hw16.frontend.FrontendService;

/**
 * Created by tully.
 */
public abstract class MsgToFrontend extends Message {
    public MsgToFrontend(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof FrontendService) {
            exec((FrontendService) addressee);
        } else throw new RuntimeException();
    }

    public abstract void exec(FrontendService frontendService);
}
