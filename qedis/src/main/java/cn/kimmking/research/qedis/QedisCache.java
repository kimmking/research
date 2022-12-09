package cn.kimmking.research.qedis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/12/4 00:54
 */
@Component
@Data
public class QedisCache {

    ScheduledExecutorService executor;

    public void start() {
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(()->CURRENT = System.currentTimeMillis(), 10, 10, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        executor.shutdown();
        if(!executor.isTerminated()) {
            try {
                if(!executor.awaitTermination(3, TimeUnit.SECONDS)){
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public static long CURRENT = System.currentTimeMillis();

    // TODO  modify HashMap to guava cache to add eviction and ttl functions
    private Map<String, CacheEntry<String>> map = new ConcurrentHashMap<>(1024);

    public void set(String key, String value) {
        this.map.put(key, new CacheEntry(value, System.currentTimeMillis(), -1000));
    }

    public String get(String key) {
        CacheEntry<String> entry = this.map.get(key);
        if (entry == null) return null;
        if (entry.getTtl() < 0) return entry.getValue();

        if(CURRENT - entry.getTs() > entry.getTtl()) {
            System.out.println(String.format("KEY[%s] expire cause CURRENT[%d]-TS[%d] > TTL[%d] ms",
                    key, CURRENT, entry.getTs(), entry.getTtl()));
            this.map.remove(key);
            return null;
        }
        return entry.getValue();
    }

    public boolean expire(String key, long ttl) {
        CacheEntry<String> entry = this.map.get(key);
        if (entry == null) return false;
        entry.setTtl(ttl * 1000L);
        entry.setTs(System.currentTimeMillis());
        return true;
    }

    public long ttl(String key) {
        CacheEntry<String> entry = this.map.get(key);
        if (entry == null) return -2;
        if (entry.getTtl() == -1000L) return -1;
        long ret = (entry.getTs()+entry.getTtl() - CURRENT)/1000;
        if(ret > 0) return ret;
        return -1;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CacheEntry<T> {

        private T value;
        private long ts; // created timestamp
        private long ttl; // alive ttl

    }

}
