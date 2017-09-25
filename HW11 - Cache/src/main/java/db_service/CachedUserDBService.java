package db_service;

import cache.Cache;
import hw10.dataset.UserDataSet;
import hw10.db_service.DBService;

public interface CachedUserDBService extends DBService {
    Cache<Long, UserDataSet> getCache();
}
