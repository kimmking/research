package cn.kimmking.research.redisbench;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.time.Duration;
import java.util.Random;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2023/5/25 17:32
 */
public class RedisUtils {

    final static String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static String getRandomString(int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }


    public static GenericObjectPoolConfig createPoolConfig(int min, int max, int total, int maxwait) {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMinIdle(min);
        poolConfig.setMaxIdle(max);
        poolConfig.setMaxTotal(total);
        poolConfig.setMaxWait(Duration.ofSeconds(maxwait));
        return poolConfig;
    }

}
