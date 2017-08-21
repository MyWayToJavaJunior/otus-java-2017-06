package db_service;

import cache.Cache;
import cache.CacheElement;
import hw10.dataset.UserDataSet;
import hw10.db_service.DBService;

public class CachedUserDBService implements DBService {
    private final DBService dbService;
    private final Cache<Long, UserDataSet> cache;

    public CachedUserDBService(DBService dbService, Cache<Long, UserDataSet> cache) {
        this.dbService = dbService;
        this.cache = cache;
    }

    @Override
    public void save(UserDataSet userDataSet) {
        dbService.save(userDataSet);
        cache.put(userDataSet.getId(), new CacheElement<>(userDataSet));
    }

    @Override
    public UserDataSet get(long id) {
        CacheElement<UserDataSet> cacheElement = cache.get(id);
        return cacheElement != null ? cacheElement.getValue() : dbService.get(id);
    }
}