package message_system.message;

import message_system.Address;
import message_system.Addressee;
import message_system.Message;
import message_system.addressee.FrontendService;

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
