package cache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

public class CacheImpl<K, V> implements Cache<K, V> {
    private static final int TIME_THRESHOLD_MS = 1;

    private final int maxElements;
    private final long lifeTimeMs;
    private final long idleTimeMs;
    private final boolean isEternal;

    private final Map<K, SoftReference<CacheElement<V>>> elements = new LinkedHashMap<>();
    private final Timer timer = new Timer();

    private int hit = 0;
    private int miss = 0;

    public CacheImpl(int maxElements, long lifeTimeMs, long idleTimeMs, boolean isEternal) {
        this.maxElements = maxElements;
        this.lifeTimeMs = lifeTimeMs > 0 ? lifeTimeMs : 0;
        this.idleTimeMs = idleTimeMs > 0 ? idleTimeMs : 0;
        this.isEternal = lifeTimeMs == 0 && idleTimeMs == 0 || isEternal;
    }

    @Override
    public void put(K key, CacheElement<V> element) {
        if (elements.size() == maxElements) {
            K firstKey = elements.keySet().iterator().next();
            elements.remove(firstKey);
        }

        elements.put(key, new SoftReference<>(element));

        if (!isEternal) {
            if (lifeTimeMs != 0) {
                TimerTask lifeTimerTask = getTimerTask(key, lifeElement -> lifeElement.getCreationTime() + lifeTimeMs);
                timer.schedule(lifeTimerTask, lifeTimeMs);
            }
            if (idleTimeMs != 0) {
                TimerTask idleTimerTask = getTimerTask(key, idleElement -> idleElement.getCreationTime() + idleTimeMs);
                timer.schedule(idleTimerTask, idleTimeMs);
            }
        }
    }

    @Override
    public CacheElement<V> get(K key) {
        SoftReference<CacheElement<V>> ref = elements.get(key);
        CacheElement<V> element = ref == null ? null : ref.get();
        if (element != null) {
            hit++;
            element.setLastAccessTime();
        } else {
            miss++;
        }
        return element;
    }

    @Override
    public int getHitCount() {
        return hit;
    }

    @Override
    public int getMissCount() {
        return miss;
    }

    @Override
    public void dispose() {
        timer.cancel();
    }

    private TimerTask getTimerTask(final K key, Function<CacheElement<V>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                SoftReference<CacheElement<V>> ref = elements.get(key);
                CacheElement<V> checkedElement = ref == null ? null : ref.get();
                if (checkedElement == null ||
                        isT1BeforeT2(timeFunction.apply(checkedElement), System.currentTimeMillis())) {
                    elements.remove(key);
                }
            }
        };
    }

    public int getMaxElements() {
        return maxElements;
    }

    public long getLifeTimeMs() {
        return lifeTimeMs;
    }

    public long getIdleTimeMs() {
        return idleTimeMs;
    }

    public boolean isEternal() {
        return isEternal;
    }

    public int getCurCacheSize() {
        return elements.size();
    }

    private boolean isT1BeforeT2(long t1, long t2) {
        return t1 < t2 + TIME_THRESHOLD_MS;
    }
}
