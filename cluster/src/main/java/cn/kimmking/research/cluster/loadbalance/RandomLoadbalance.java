package cn.kimmking.research.cluster.loadbalance;

import java.util.List;
import java.util.Random;

/**
 * Use random utility to choice one item.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/26 01:27
 */
public class RandomLoadbalance<T> extends AbstractLoadbalance<T> {

    final Random random = new Random();

    @Override
    public T doChoice(List<T> items) {
        return items.get(random.nextInt(items.size()));
    }
}
