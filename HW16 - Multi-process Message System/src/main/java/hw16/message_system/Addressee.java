package hw16.message_system;

/**
 * @author tully
 */
public interface Addressee {
    Address getAddress();
    MessageSystemContext getMessageSystemContext();
}