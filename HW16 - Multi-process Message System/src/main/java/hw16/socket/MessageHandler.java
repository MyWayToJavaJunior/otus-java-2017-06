package hw16.socket;

import hw16.message_system.Message;

public interface MessageHandler {
    void send(Message msg);
    Message poll();
    Message take() throws InterruptedException;
}