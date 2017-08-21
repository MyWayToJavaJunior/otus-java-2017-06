package cache;

import lombok.Data;

import java.lang.ref.SoftReference;

@Data
public class CacheElement<V> {
    private final V value;
    private final long creationTime;
    private long lastAccessTime;


    public CacheElement(V value) {
        this.value = value;
        this.creationTime = getCurrentTime();
        this.lastAccessTime = getCurrentTime();
    }

    protected long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public void setLastAccessTime() {
        lastAccessTime = getCurrentTime();
    }
}
