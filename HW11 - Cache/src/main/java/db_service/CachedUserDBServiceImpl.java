package db_service;

import cache.Cache;
import cache.CacheElement;
import hw10.dataset.UserDataSet;
import hw10.db_service.DBService;

public class CachedUserDBServiceImpl implements CachedUserDBService {
    private final DBService dbService;
    private final Cache<Long, UserDataSet> cache;

    public CachedUserDBServiceImpl(DBService dbService, Cache<Long, UserDataSet> cache) {
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
        if (cacheElement != null) return cacheElement.getValue();
        
        UserDataSet dataSet = dbService.get(id);
        cache.put(dataSet.getId(), new CacheElement<>(dataSet));
        return dataSet;
    }

    @Override
    public UserDataSet get(String s) {
        return dbService.get(s);
    }

    @Override
    public Cache<Long, UserDataSet> getCache() {
        return cache;
    }
}
