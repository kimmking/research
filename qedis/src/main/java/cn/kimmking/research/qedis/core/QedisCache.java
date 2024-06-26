package cn.kimmking.research.qedis.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.lang.Math.min;

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

    @Getter
    // TODO  modify HashMap to guava cache to add eviction and ttl functions
    private Map<String, CacheEntry<?>> map = new ConcurrentHashMap<>(1024);

//    private Map<String, CacheEntry<Map<String,String>>> hmap = new ConcurrentHashMap<>(128);

    public void set(String key, String value) {
        this.map.put(key, new CacheEntry(value, System.currentTimeMillis(), -1000));
    }


    public String get(String key) {
        if(checkInvalid(key)) return null;
        CacheEntry<String> entry = (CacheEntry<String>) this.map.get(key);
        return entry.getValue();
    }

    public boolean checkInvalid(String key) {
        CacheEntry<String> entry = (CacheEntry<String>) this.map.get(key);
        if (entry == null || entry.getValue() == null) return true;
        if(entry.getTtl() > 0 && CURRENT - entry.getTs() > entry.getTtl()) {
            System.out.println(String.format("KEY[%s] expire cause CURRENT[%d]-TS[%d] > TTL[%d] ms",
                    key, CURRENT, entry.getTs(), entry.getTtl()));
            this.map.remove(key);
            return true;
        }
        return false;
    }

    public int exists(String...keys) {
        return keys == null ? 0 : (int) Arrays.stream(keys).map(this.map::containsKey)
                .filter(x -> x).count();
    }
    public int del(String...keys) {
        return keys == null ? 0 : (int) Arrays.stream(keys).map(this.map::remove)
                .filter(Objects::nonNull).count();
    }

    public boolean expire(String key, long ttl) {
        CacheEntry<?> entry = this.map.get(key);
        if (entry == null) return false;
        entry.setTtl(ttl * 1000L);
        entry.setTs(System.currentTimeMillis());
        return true;
    }

    public long ttl(String key) {
        CacheEntry<?> entry = this.map.get(key);
        if (entry == null) return -2;
        if (entry.getTtl() == -1000L) return -1;
        long ret = (entry.getTs()+entry.getTtl() - CURRENT)/1000;
        if(ret > 0) return ret;
        return -1;
    }

    public Integer strlen(String key) {
        return this.get(key) == null ? null : this.get(key).length();
    }


    // ================  list start ======================
    public int lpush(String key, String... values) {
        CacheEntry<?> entry = this.map.get(key);
        if(entry == null) {
            synchronized (this.map) {
                if((entry = this.map.get(key)) == null) {
                    entry = new CacheEntry<>(new LinkedList<String>(), System.currentTimeMillis(), -1000);
                    this.map.put(key, entry);
                }
            }
        }
        LinkedList<String> exist = (LinkedList<String>) entry.getValue();
        Arrays.stream(values).forEach(exist::addFirst);
        return values.length;
    }

    public int rpush(String key, String... values) {
        CacheEntry<?> entry = this.map.get(key);
        if(entry == null) {
            synchronized (this.map) {
                if((entry = this.map.get(key)) == null) {
                    entry = new CacheEntry<>(new LinkedList<String>(), System.currentTimeMillis(), -1000);
                    this.map.put(key, entry);
                }
            }
        }
        LinkedList<String> exist = (LinkedList<String>) entry.getValue();
        exist.addAll(List.of(values));
        return values.length;
    }

    public String[] lpop(String key, int count) {
        if(checkInvalid(key)) return null;
        LinkedList<String> v = (LinkedList<String>) this.map.get(key).getValue();
        int len = min(count, v.size());
        String[] ret = new String[len];
        int index = 0;
        while (index < len) {
            ret[index ++] = v.removeFirst();
        }
        return ret;
    }

    public String[] rpop(String key, int count) {
        if(checkInvalid(key)) return null;
        LinkedList<String> v = (LinkedList<String>) this.map.get(key).getValue();
        int len = min(count, v.size());
        String[] ret = new String[len];
        int index = 0;
        while (index < len) {
            ret[index ++] = v.removeLast();
        }
        return ret;
    }

    public int llen(String key) {
        if(checkInvalid(key)) return 0;
        LinkedList<String> v = (LinkedList<String>) this.map.get(key).getValue();
        return v.size();
    }

    public String[] lrange(String key, int start, int stop) {
        if(checkInvalid(key)) return new String[0];
        LinkedList<String> v = (LinkedList<String>) this.map.get(key).getValue();
        int size = v.size();
        int len = min(size-start, stop-start+1);
        String[] ret = new String[len];
        for(int i = 0; i < len; i ++) {
            ret[i] = v.get(start + i);
        }
        return ret;
    }

    public String lindex(String key, int index) {
        if(checkInvalid(key)) return null;
        LinkedList<String> v = (LinkedList<String>) this.map.get(key).getValue();
        if(index >= v.size()) return null;
        return v.get(index);
    }
    // ================  list end ======================

    // ================  hash start ======================

    public int hset(String key, String[] fields, String[] values) {
        CacheEntry<?> entry = this.map.get(key);
        if(fields == null || fields.length == 0) return 0;
        if(entry == null) {
            synchronized (this.map) {
                if((entry = this.map.get(key)) == null) {
                    entry = new CacheEntry<>(new HashMap<String,String>(16), System.currentTimeMillis(), -1000);
                    this.map.put(key, entry);
                }
            }
        }
        HashMap<String,String> exist = (HashMap<String, String>) entry.getValue();
        for(int i = 0; i < fields.length; i ++) {
            exist.put(fields[i], values[i]);
        }
        return fields.length;
    }

    public String hget(String key, String field) {
        if(checkInvalid(key)) return null;
        Map<String,String> v = (Map<String,String>)this.map.get(key).getValue();
        return v.get(field);
    }

    public String[] hgetall(String key) {
        if(checkInvalid(key)) return null;
        Map<String,String> v = (Map<String,String>)this.map.get(key).getValue();
        return map.entrySet().stream()
                .flatMap(e -> Stream.of(e.getKey(),e.getValue())).toArray(String[]::new);
    }

    public String[] hmget(String key, String... fields) {
        if(checkInvalid(key)) return null;
        Map<String,String> v = (Map<String,String>)this.map.get(key).getValue();
        return Arrays.stream(fields).flatMap(f -> Stream.of(f, v.get(f))).toArray(String[]::new);
    }

    public int hlen(String key) {
        if(checkInvalid(key)) return 0;
        Map<String,String> v = (Map<String,String>)this.map.get(key).getValue();
        return v.size();
    }

    public int hexists(String key, String field) {
        if(checkInvalid(key)) return 0;
        Map<String,String> v = (Map<String,String>)this.map.get(key).getValue();
        return v.containsKey(field) ? 1 : 0;
    }

    public int hdel(String key, String... fields) {
        if(checkInvalid(key)) return 0;
        Map<String,String> v = (Map<String,String>)this.map.get(key).getValue();
        return (int) Arrays.stream(fields).map(v::remove).filter(Objects::nonNull).count();
    }

    // ================  hash end ======================


    // ================  set start ======================

    public int sadd(String key, String... values) {
        CacheEntry<?> entry = this.map.get(key);
        if(entry == null) {
            synchronized (this.map) {
                if((entry = this.map.get(key)) == null) {
                    entry = new CacheEntry<>(new LinkedHashSet<>(), System.currentTimeMillis(), -1000);
                    this.map.put(key, entry);
                }
            }
        }
        LinkedHashSet<String> exist = (LinkedHashSet<String>) entry.getValue();
        exist.addAll(List.of(values));
        return values.length;
    }

    public int srem(String key, String... values) {
        if(checkInvalid(key) || values == null || values.length == 0) return 0;
        LinkedHashSet<String> exist = (LinkedHashSet<String>) this.map.get(key).getValue();
        long count = Arrays.stream(values).filter(exist::remove).count();
        return (int)count;
    }

    public String spop(String key) {
        if(checkInvalid(key)) return null;
        LinkedHashSet<String> exist = (LinkedHashSet<String>) this.map.get(key).getValue();
        Object[] array = exist.toArray();
        if(array.length > 0) {
            Object rnd = array[new Random().nextInt(array.length)];
            if(exist.remove(rnd)) {
                return (String) rnd;
            }
        }
        return null;
    }

    public String[] smembers(String key) {
        if(checkInvalid(key)) return new String[0];
        LinkedHashSet<String> exist = (LinkedHashSet<String>) this.map.get(key).getValue();
        return exist.toArray(new String[exist.size()]);
    }

    public int sismember(String key, String value) {
        if(checkInvalid(key)) return 0;
        LinkedHashSet<String> exist = (LinkedHashSet<String>) this.map.get(key).getValue();
        return exist.contains(value) ? 1 : 0;
    }

    public int scard(String key) {
        if(checkInvalid(key)) return 0;
        LinkedHashSet<String> exist = (LinkedHashSet<String>) this.map.get(key).getValue();
        return exist.size();
    }

    // ================  set end ======================

    // ================  zset start ===================

    public int zadd(String key, String[] values, double[] scores) {
        CacheEntry<?> entry = this.map.get(key);
        if(entry == null) {
            synchronized (this.map) {
                if((entry = this.map.get(key)) == null) {
                    entry = new CacheEntry<>(new LinkedHashSet<ZsetEntry>(), System.currentTimeMillis(), -1000);
                    this.map.put(key, entry);
                }
            }
        }
        LinkedHashSet<ZsetEntry> exist = (LinkedHashSet<ZsetEntry>) entry.getValue();
        for(int i = 0; i < values.length; i ++) {
            exist.add(new ZsetEntry(values[i], scores[i]));
        }
        return values.length;
    }

    public int zcard(String key) {
        if(checkInvalid(key)) return 0;
        LinkedHashSet<ZsetEntry> exist = (LinkedHashSet<ZsetEntry>) this.map.get(key).getValue();
        return exist.size();
    }

    public int zcount(String key, double min, double max) {
        if(checkInvalid(key)) return 0;
        LinkedHashSet<ZsetEntry> exist = (LinkedHashSet<ZsetEntry>) this.map.get(key).getValue();
        return (int) exist.stream().filter(e -> e.getScore() >= min && e.getScore() <= max).count();
    }

    public Double zscore(String key, String value) {
        if(checkInvalid(key)) return null;
        LinkedHashSet<ZsetEntry> exist = (LinkedHashSet<ZsetEntry>) this.map.get(key).getValue();
        return exist.stream().filter(e -> e.getValue().equals(value))
                .map(ZsetEntry::getScore).findFirst().orElse(null);
    }

    public Integer zrank(String key, String value) {
        if(checkInvalid(key)) return null;
        LinkedHashSet<ZsetEntry> exist = (LinkedHashSet<ZsetEntry>) this.map.get(key).getValue();
        Double zscore = zscore(key, value);
        if(zscore == null) return null;
        return (int) exist.stream().filter(x -> x.getScore() < zscore).count();
    }

    public int zrem(String key, String... values) {
        if(checkInvalid(key)) return 0;
        LinkedHashSet<ZsetEntry> exist = (LinkedHashSet<ZsetEntry>) this.map.get(key).getValue();
        return (int) Arrays.stream(values)
                .map(x->exist.removeIf(y->y.getValue().equals(x)))
                .filter(Objects::nonNull).count();
    }

    // ================  zset end =====================

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CacheEntry<T> {
        private T value;
        private long ts; // created timestamp
        private long ttl; // alive ttl
    }

    @Data
    @AllArgsConstructor
    public static class ZsetEntry {
        private String value;
        private Double score;
    }

}
