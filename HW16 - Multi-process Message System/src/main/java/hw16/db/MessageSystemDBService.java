package hw16.db;

import cache.Cache;
import db_service.CachedUserDBService;
import hw10.dataset.UserDataSet;
import hw16.message_system.Address;
import hw16.message_system.Addressee;
import hw16.message_system.MessageSystemContext;
import hw16.socket.SocketMessageServer;

import java.util.UUID;

public class MessageSystemDBService implements CachedUserDBService, Addressee {
    private final MessageSystemContext context;
    private final CachedUserDBService cachedUserDBService;
    private final Address address = new Address(SocketMessageServer.DB_ADDRESS.getId() + "@" + UUID.randomUUID().toString());

    public MessageSystemDBService(MessageSystemContext context, CachedUserDBService cachedUserDBService) {
        this.context = context;
        this.cachedUserDBService = cachedUserDBService;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystemContext getMessageSystemContext() {
        return context;
    }

    @Override
    public Cache<Long, UserDataSet> getCache() {
        return cachedUserDBService.getCache();
    }

    @Override
    public void save(UserDataSet userDataSet) {
        cachedUserDBService.save(userDataSet);
    }

    @Override
    public UserDataSet get(long l) {
        return cachedUserDBService.get(l);
    }

    @Override
    public UserDataSet get(String s) {
        return cachedUserDBService.get(s);
    }
}
