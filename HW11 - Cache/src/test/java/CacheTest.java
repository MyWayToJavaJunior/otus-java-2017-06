import cache.Cache;
import cache.CacheElement;
import cache.CacheImpl;
import org.junit.Before;
import org.junit.Test;

public class CacheTest {
    private Cache<Integer, BigObject> cache;

    @Before
    public void setup() {
        cache = new CacheImpl<>(1000, 0, 0, true);
    }

    // VM options: -Xmx256m -Xms256m
    @Test
    public void smokeTest() {
        for (int i = 0; i < 1000; i++) cache.put(i, new CacheElement<>(new BigObject()));
    }

    class BigObject {
        private byte[] object = new byte[1024 * 1024];
    }
}
