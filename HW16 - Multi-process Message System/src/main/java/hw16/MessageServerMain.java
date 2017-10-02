package hw16;

import hw16.socket.SocketMessageServer;

public class MessageServerMain {

    public static void main(String[] args) throws Exception {
        new SocketMessageServer().start();
    }
}
