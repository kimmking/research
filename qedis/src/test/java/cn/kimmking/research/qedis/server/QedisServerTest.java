package cn.kimmking.research.qedis.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/12/4 09:36
 */

public class QedisServerTest {

    QedisServer server;

    @Before
    public void before() throws Exception {
        server = new QedisServer();
        server.startup();
    }

//    @Test
//    public void testcase_00() {
//        Jedis jedis = new Jedis("localhost", 6379);
//        String ret = jedis.set("a1", "1"); // 默认正常报文外多一个报文X1
//        assertEquals("OK", ret);
//        String value = jedis.get("a1");    // 默认正常报文外多一个报文X2
//        assertEquals("X1", value);  // 这里拿到的是X1
//        jedis.close();
//    }

    @Test
    public void testcase_01() {
        Jedis jedis = new Jedis("localhost", 6379);
        String ret = jedis.set("a1", "1");
        assertEquals("OK", ret);
        String value = jedis.get("a1");
        assertEquals("1", value);

        ret = jedis.set("a1", "aaa1");
        assertEquals("OK", ret);
        value = jedis.get("a1");
        assertEquals("aaa1", value);

        long len = jedis.strlen("a1");
        assertEquals(4, len);

        value = jedis.get("a2");
        assertNull(value);
        jedis.close();
    }

    @Test
    public void testcase_02() {
        Jedis jedis = new Jedis("localhost", 6379);
        String ret = jedis.set("a1", "1");
        assertEquals("OK", ret);
        long value = jedis.incr("a1");
        assertEquals(2, value);
        value = jedis.incr("a1");
        assertEquals(3, value);

        value = jedis.decr("a1");
        assertEquals(2, value);

        jedis.close();
    }

    @Test
    public void testcase_03() {
        Jedis jedis = new Jedis("localhost", 6379);
        String ret = jedis.set("a1", "1");
        assertEquals("OK", ret);

        boolean value = jedis.exists("a1");
        assertEquals(true, value);

        value = jedis.exists("a10");
        assertEquals(false, value);

        long count = jedis.del("a1");
        assertEquals(1, count);

        ret = jedis.get("a1");
        assertNull(ret);

        jedis.close();
    }

    @Test
    public void testcase_04_ttl() {
        Jedis jedis = new Jedis("localhost", 6379);
        String ret = jedis.set("a1", "a111");
        assertEquals("OK", ret);

        ret = jedis.get("a1");
        assertEquals("a111", ret);

        long lret = jedis.expire("a1", 1L);
        System.out.println("expire a1:" + lret);

        try {
            Thread.sleep(1100L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ret = jedis.get("a1");
        System.out.println("after 1.1s, test a1->" + ret);
        assertNull(ret);

        jedis.close();
    }


    @Test
    public void testcase_05_auth() {

        DefaultJedisClientConfig config = DefaultJedisClientConfig.builder().clientName("App1_Y_X1_Client01").user("user1").password("passwd1").build();
        Jedis jedis = new Jedis( new HostAndPort("localhost", 6379), config);

        jedis.ping();

        String ret = jedis.set("a1", "a111");
        assertEquals("OK", ret);

        ret = jedis.get("a1");
        assertEquals("a111", ret);

        jedis.close();
    }

    @After
    public void after() throws Exception {
        server.getCache().getMap().clear();
        server.shutdown();
    }
}