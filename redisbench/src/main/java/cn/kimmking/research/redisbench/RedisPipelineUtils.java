package cn.kimmking.research.redisbench;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.*;
import redis.clients.jedis.util.IOUtils;

import java.util.List;
import java.util.Set;

import static cn.kimmking.research.redisbench.RedisUtils.createPoolConfig;
import static cn.kimmking.research.redisbench.RedisUtils.getRandomString;
import static redis.clients.jedis.Protocol.Command.HKEYS;
import static redis.clients.jedis.Protocol.Command.HSET;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2023/5/25 17:31
 */
public class RedisPipelineUtils {

    private final static HostAndPort HAP = new HostAndPort("192.168.64.2", 6379);

    private final static String KPREFIX = "KKey";
    private final static String VPREFIX = getRandomString(90);

    final static int SIZE = 100000;
    final static int CORE = 20;
    final static int POOLSIZE = 20;
    static GenericObjectPoolConfig poolConfig = createPoolConfig(POOLSIZE, POOLSIZE, POOLSIZE, 2);

    static JedisPool jedisPool = null;

    public static void main(String[] args) {
        initJedis();
        exec();
        closeJedis();
    }

    private static void exec() {

        try (Jedis jedis = jedisPool.getResource()){

            Pipeline pipelined = jedis.pipelined();
            pipelined.set("pipe1", "ppp1123123");
            pipelined.set("p1", "3");
            pipelined.incr("p1");
            pipelined.get("p1");
            pipelined.get("pipe1");
            pipelined.hset("ph1", "f1", "1");
            pipelined.hset("ph1", "f2", "1");
            pipelined.hkeys("ph1");
            List<Object> objects = pipelined.syncAndReturnAll();

            for (Object o : objects) {
                System.out.println(o);
            }

            CommandObject<String> commandObject1 = new CommandObject<>(new CommandArguments
                    (Protocol.Command.SET).key("K01").add("Value01"), BuilderFactory.STRING);
            CommandObject<Long> commandObject2 = new CommandObject<>(new CommandArguments
                    (HSET).key("HK1").add("f1").add("v1"), BuilderFactory.LONG);
            CommandObject<Long> commandObject3 = new CommandObject<>(new CommandArguments
                    (HSET).key("HK1").add("f2").add("v2"), BuilderFactory.LONG);
            CommandObject<Set<String>> commandObject4 = new CommandObject<>(new CommandArguments
                    (HKEYS).key("HK1"), BuilderFactory.STRING_SET);

            pipelined = jedis.pipelined();
            pipelined.appendCommand(commandObject1);
            pipelined.appendCommand(commandObject2);
            pipelined.appendCommand(commandObject3);
            pipelined.appendCommand(commandObject4);
            objects = pipelined.syncAndReturnAll();

            for (Object o : objects) {
                System.out.println(o);
            }





            System.out.println(" ===> exec Redis finished.");

        }

    }

    private static void initJedis() {
        JedisClientConfig config = DefaultJedisClientConfig.builder()
                .connectionTimeoutMillis(200).socketTimeoutMillis(500).build(); //.timeoutMillis(500);
        jedisPool = new JedisPool(poolConfig, HAP, config);
        try {
            jedisPool.preparePool();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void closeJedis() {
        if(jedisPool != null && !jedisPool.isClosed()) {
            IOUtils.closeQuietly(jedisPool);
        }
    }

}
