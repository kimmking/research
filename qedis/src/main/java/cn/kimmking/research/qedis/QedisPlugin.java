package cn.kimmking.research.qedis;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/26 16:08
 */
public interface QedisPlugin {

    void init();
    void startup();
    void shutdown();

}
