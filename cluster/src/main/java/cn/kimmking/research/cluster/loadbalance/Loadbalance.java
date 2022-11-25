package cn.kimmking.research.cluster.loadbalance;

import java.util.List;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/25 21:36
 */
public interface Loadbalance<T> {

    T choice(List<T> items);

}
