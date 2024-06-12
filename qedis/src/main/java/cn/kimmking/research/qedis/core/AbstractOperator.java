package cn.kimmking.research.qedis.core;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/6/11 下午11:36
 */
public abstract class AbstractOperator {
    QedisCache cache;
    public AbstractOperator(QedisCache cache) {
        this.cache = cache;
    }



}
