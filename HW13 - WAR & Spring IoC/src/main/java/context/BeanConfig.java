package context;

import cache.CacheImpl;
import db_service.CachedUserDBService;
import hw10.dataset.UserDataSet;
import hw10.db_service.DBService;
import hw10.db_service.DBServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public DBService dbService() {
        return new DBServiceImpl();
    }

    @Bean
    public CachedUserDBService cachedUserDBService() {
        return new CachedUserDBService(dbService(), new CacheImpl<Long, UserDataSet>(1, 11, 1, true));
    }
}
