package cn.kimmking.research.cluster.loadbalance;

import java.util.List;

/**
 * Abstract interface for all load balance implements.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/25 21:36
 */
public interface Loadbalance<T> {

    /**
     * @param items all items for choice
     * @return the picked one item
     */
    T choice(List<T> items);

}
