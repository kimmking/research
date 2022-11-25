package cn.kimmking.research.cluster.loadbalance;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Use round robbin utility to choice one item.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/26 01:27
 */
public class RoundRobbinLoadbalance<T> extends AbstractLoadbalance<T> {

    final AtomicInteger index = new AtomicInteger(0);

    @Override
    public T doChoice(List<T> items) {
        return items.get(index.getAndIncrement()%items.size());
    }
}
