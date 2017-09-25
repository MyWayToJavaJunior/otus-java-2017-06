package context;

import cache.CacheImpl;
import db_service.CachedUserDBService;
import db_service.CachedUserDBServiceImpl;
import hw10.dataset.UserDataSet;
import hw10.db_service.DBService;
import hw10.db_service.DBServiceImpl;
import message_system.MessageSystem;
import message_system.MessageSystemContext;
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
        return new CachedUserDBServiceImpl(dbService(), new CacheImpl<Long, UserDataSet>(1, 11, 1, true));
    }

    @Bean
    public MessageSystem messageSystem() {
        return new MessageSystem();
    }

    @Bean
    public MessageSystemContext messageSystemContext() {
        return new MessageSystemContext(messageSystem());
    }
}
