package cn.kimmking.research.redisbench;

import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static cn.kimmking.research.redisbench.RedisUtils.createPoolConfig;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2023/5/26 15:04
 */
public class RedisLettuceUtils {

    final static int CORE = 20;
    final static int POOLSIZE = 20;

    private final static HostAndPort HAP = new HostAndPort("192.168.64.2", 6379);

    public static void main(String[] args) {
        RedisClient lettuce;
        GenericObjectPool<StatefulRedisConnection<String, String>> lettucePool;
        GenericObjectPoolConfig poolConfig = createPoolConfig(POOLSIZE, POOLSIZE, POOLSIZE, 2);
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

        // ========================

            StatefulRedisConnection<String, String> connection = null;
            try {
                connection = lettucePool.borrowObject();
                RedisAsyncCommands<String, String> commands = connection.async();
                commands.setAutoFlushCommands(false);
                List<RedisFuture<?>> futures = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    futures.add(commands.set("key-" + i, "value-" + i));
                    futures.add(commands.expire("key-" + i, 360000));
                    futures.add(commands.get("key-" + i));
                }

                commands.flushCommands();
                boolean result = LettuceFutures.awaitAll(5, TimeUnit.SECONDS,
                        futures.toArray(new RedisFuture[futures.size()]));

                for (RedisFuture<?> f : futures) {
                    System.out.println(f.get());
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                connection.close();
            }





        // ========================

        if(lettucePool!=null && !lettucePool.isClosed()) {
            lettucePool.close();
        }
        if(lettuce != null)
            lettuce.shutdown();

    }

}
