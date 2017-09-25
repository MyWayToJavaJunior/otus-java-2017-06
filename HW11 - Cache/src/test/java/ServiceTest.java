import cache.Cache;
import cache.CacheImpl;
import db_service.CachedUserDBServiceImpl;
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
    private DBService vanilaDbService;
    private DBService dbService;

    @Before
    public void setup() {
        cache = new CacheImpl<>(1000, 0, 0, true);
        vanilaDbService = new DBServiceImpl();
        dbService = new CachedUserDBServiceImpl(vanilaDbService, cache);
    }

    @Test
    public void test() throws NoSuchFieldException, IllegalAccessException {
        UserDataSet user = new UserDataSet("Name1", "pass", 1, new AddressDataSet("Street1"),
                Arrays.asList(new PhoneDataSet("+70001112201"), new PhoneDataSet("+70001112202")));
        dbService.save(user);

        UserDataSet actual = dbService.get(1);

        Assert.assertEquals(user.getId(), actual.getId());
        Assert.assertEquals(user.getName(), actual.getName());
        Assert.assertEquals(1, cache.getHitCount());

        user = new UserDataSet("Name2", "pass",20, new AddressDataSet("Street2"),
                Arrays.asList(new PhoneDataSet("+70001112203")));
        vanilaDbService.save(user);
        dbService.get(2);
        Assert.assertEquals(1, cache.getHitCount());
        dbService.get(2);
        Assert.assertEquals(2, cache.getHitCount());
    }
}
