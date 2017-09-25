package message_system.addressee;

import cache.Cache;
import db_service.CachedUserDBService;
import hw10.dataset.UserDataSet;
import message_system.Address;
import message_system.Addressee;

public class MessageSystemDBService implements CachedUserDBService, Addressee {
    private final CachedUserDBService cachedUserDBService;
    private final Address address = new Address("db");

    public MessageSystemDBService(CachedUserDBService cachedUserDBService) {
        this.cachedUserDBService = cachedUserDBService;
    }

    @Override
    public Address getAddress() {
        return address;
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
