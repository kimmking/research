package cn.kimmking.research.cluster.loadbalance;

import java.util.List;

/**
 * A decorated abstract class for Loadbalance.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/26 01:34
 */
public abstract class AbstractLoadbalance<T> implements Loadbalance<T> {
    @Override
    public T choice(List<T> items) {
        if(null == items || items.isEmpty()) {
            return null;
        }
        return items.size() == 1 ? items.get(0) : doChoice(items);
    }

    public abstract T doChoice(List<T> items);
}
