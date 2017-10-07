package hw16.message_system;

public interface MessageSystem {
    void addAddressee(Addressee addressee);
    void sendMessage(Message msg);
    void start() throws Exception;
}
