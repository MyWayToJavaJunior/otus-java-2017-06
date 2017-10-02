package hw16.messages;

import hw16.message_system.Address;
import hw16.message_system.Addressee;
import hw16.message_system.Message;

public class AddressMessage extends Message {
    public AddressMessage(Address from) {
        super(from, null);
    }

    @Override
    public void exec(Addressee addressee) {

    }
}
