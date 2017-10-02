package hw16.message_system;

import lombok.Data;

@Data
public abstract class Message {
    public static final String CLASS_NAME_VARIABLE = "className";

    private final String className;

    private final Address from;
    private final Address to;

    public Message(Address from, Address to) {
        this.className = this.getClass().getName();
        this.from = from;
        this.to = to;
    }

    public abstract void exec(Addressee addressee);
}