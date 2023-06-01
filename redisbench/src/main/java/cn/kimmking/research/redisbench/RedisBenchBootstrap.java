package cn.kimmking.research.redisbench;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.*;
import redis.clients.jedis.util.IOUtils;

import java.util.concurrent.*;

import static cn.kimmking.research.redisbench.RedisUtils.createPoolConfig;
import static cn.kimmking.research.redisbench.RedisUtils.getRandomString;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/26 16:23
 */

@SpringBootApplication
public class RedisBenchBootstrap {

    private final static HostAndPort HAP = new HostAndPort("127.0.0.1", 6379);

    private final static String KPREFIX = "KKey";
    private final static String VPREFIX = getRandomString(90);

    public static void main(String[] args) {
        SpringApplication.run(RedisBenchBootstrap.class);
    }

    final static int SIZE = 100000;
    final static int CORE = 20;
    final static int POOLSIZE = 20;
    GenericObjectPoolConfig poolConfig = createPoolConfig(POOLSIZE, POOLSIZE, POOLSIZE, 2);

    @Bean
    public ApplicationRunner createRunner(@Autowired ApplicationContext context) {
        return x -> {
            //System.out.println("ApplicationArguments: Options" + x.getOptionNames() + ",NonOptions" + x.getNonOptionArgs());

            System.out.println("==== Performance Benchmark for Redis Client ====");
            System.out.println("==== Jedis/BIO-Socket vs Lettuce/NIO-Netty ====");
            System.out.println(String.format("Test case for[data=%d,threads=%d,poolSize=%d]", SIZE, CORE, POOLSIZE));

           // 1. init jedis
            initJedis();
            // 2.fill redis server
            initRedisData();
            // 3.perf jedis
            JedisBench();
            // 4. close
            closeJedis();

            // lettuce bench
            initLettuce();
            LettuceBench();
            closeLettuce();

        };
    }

    RedisClient lettuce;
    GenericObjectPool<StatefulRedisConnection<String, String>> lettucePool;

    // =======================  Lettuce Bench ======================
    private void LettuceBench() {

        long start = System.currentTimeMillis();
        System.out.println(" ===> lettuce bench "+SIZE+" starting ...");
        for (int i = 0; i < SIZE; i++) {
            final String key = KPREFIX + i;
            StatefulRedisConnection<String, String> connection = null;
            try {
                connection = lettucePool.borrowObject();
                if(i<SIZE-2) {
                    RedisAsyncCommands<String, String> async = connection.async();
                    async.get(key);
                } else {
                    RedisCommands<String, String> sync = connection.sync();
                    sync.get(key);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                connection.close();
            }
        }
        long cost =System.currentTimeMillis()-start;
        System.out.println(" ===> lettuce bench finish in " + cost + " ms");
        System.out.println(" *** lettuce " + (SIZE*1000d/cost) + " qps");
    }

    private void initLettuce() {
        ClientResources resources = ClientResources.builder().ioThreadPoolSize(CORE).computationThreadPoolSize(CORE).build();
        lettuce = RedisClient.create(resources, "redis://" + HAP.toString());

        lettucePool = ConnectionPoolSupport.createGenericObjectPool(
                () -> lettuce.connect(), poolConfig);
        try {
            lettucePool.preparePool();
            //System.out.println("preparePool.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void closeLettuce() {
        if(lettucePool!=null && !lettucePool.isClosed()) {
            lettucePool.close();
        }
        if(lettuce != null)
            lettuce.shutdown();
    }


    // =======================  Jedis Bench ======================
    JedisPool jedisPool = null;

    private void JedisBench() {
        ThreadPoolExecutor executor = RedisBenchUtils.createExecutor(CORE, CORE, 100);
        long start = System.currentTimeMillis();
        System.out.println(" ===> jedis bench "+SIZE+" starting ...");
        for (int i = 0; i < SIZE; i++) {
            final String key = KPREFIX+i;
            Future<String> f = executor.submit(() -> {
                try (Jedis jedis = jedisPool.getResource()) {
                    return jedis.get(key);
                }
            });
            if(i<SIZE-2) continue;
            try {
                String value = f.get(500, TimeUnit.MILLISECONDS);
                if (value == null) {
                    throw new RuntimeException(key + " is no exists.");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            }
        }
        long cost =System.currentTimeMillis()-start;
        System.out.println(" ===> Jedis bench finish in " + cost + " ms");
        System.out.println(" *** jedis " + (SIZE*1000d/cost) + " qps");
        executor.shutdown();
    }

    private void initRedisData() {

        try (Jedis jedis = jedisPool.getResource()){
            long dbSize = jedis.dbSize();
            System.out.println("dbsize:"+dbSize);
            if(dbSize == SIZE) {
                System.out.println("db has been initialized, this init be ignore.");
                return;
            }
            long start = System.currentTimeMillis();
            System.out.println("flushall:"+jedis.flushAll());
            System.out.println("flashall finish in " + (System.currentTimeMillis()-start) + " ms");
            System.out.println(" ===> init "+SIZE+" with 90 bytes ...");
            start = System.currentTimeMillis();
            for (int i = 0; i < SIZE; i++) { // 10w -> 18.8M
                jedis.set("KKey"+i, VPREFIX + i);
            }
            System.out.println(" ===> init Redis data finish in " + (System.currentTimeMillis()-start) + " ms");

        }

    }

    private void initJedis() {
        JedisClientConfig config = DefaultJedisClientConfig.builder()
                .connectionTimeoutMillis(200).socketTimeoutMillis(500).build(); //.timeoutMillis(500);
        jedisPool = new JedisPool(poolConfig, HAP, config);
        try {
            jedisPool.preparePool();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void closeJedis() {
        if(jedisPool != null && !jedisPool.isClosed()) {
            IOUtils.closeQuietly(jedisPool);
        }
    }


}
