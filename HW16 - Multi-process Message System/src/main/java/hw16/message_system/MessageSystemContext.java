package hw16.message_system;

import hw16.socket.SocketMessageClient;
import lombok.Data;

public class MessageSystemContext {
    private static SocketMessageClient client;
    private static Address frontAddress;
    private static Address dbAddress;

    public static void setClient(SocketMessageClient client) {
        MessageSystemContext.client = client;
    }

    public static void sendMessage(Message message) {
        client.sendMessage(message);
    }

    public static Address getFrontAddress() {
        return frontAddress;
    }

    public static void setFrontAddress(Address frontAddress) {
        MessageSystemContext.frontAddress = frontAddress;
    }

    public static Address getDbAddress() {
        return dbAddress;
    }

    public static void setDbAddress(Address dbAddress) {
        MessageSystemContext.dbAddress = dbAddress;
    }
}