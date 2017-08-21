import cache.Cache;
import cache.CacheImpl;
import db_service.CachedUserDBService;
import hw10.dataset.AddressDataSet;
import hw10.dataset.PhoneDataSet;
import hw10.dataset.UserDataSet;
import hw10.db_service.DBService;
import hw10.db_service.DBServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class ServiceTest {
    private Cache<Long, UserDataSet> cache;
    private DBService dbService;

    @Before
    public void setup() {
        cache = new CacheImpl<>(1000, 0, 0, true);
        dbService = new CachedUserDBService(new DBServiceImpl(), cache);
    }

    @Test
    public void test() {
        UserDataSet user = new UserDataSet("Name1", 1, new AddressDataSet("Street1"),
                Arrays.asList(new PhoneDataSet("+70001112201"), new PhoneDataSet("+70001112202")));
        dbService.save(user);

        UserDataSet actual = dbService.get(1);

        Assert.assertEquals(user.getId(), actual.getId());
        Assert.assertEquals(user.getName(), actual.getName());

        Assert.assertEquals(1, cache.getHitCount());
    }
}
