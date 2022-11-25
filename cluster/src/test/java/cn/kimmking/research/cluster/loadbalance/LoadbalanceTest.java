package cn.kimmking.research.cluster.loadbalance;


import cn.kimmking.research.cluster.TestUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * test this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/26 01:48
 */
public class LoadbalanceTest {

    static final int N = 20000;


    @Test
    public void testAbstractLoadbalance() {
        int size = 5;
        List<Integer> items = TestUtil.generate(0, 1);
        int[] resutls = new int[size];
        AbstractLoadbalance<Integer> loadbalance = new AbstractLoadbalance<Integer>() {
            @Override
            public Integer doChoice(List<Integer> items) {
                return null;
            }
        };
        for (int i = 0; i < size; i++) {
            resutls[i] = loadbalance.choice(items).intValue();
        }
        System.out.println(String.format("results=[%s]", Arrays.toString(resutls)));
        for (int i = 0; i < size; i++) {
            Assert.assertEquals(0, resutls[i]);
        }
    }

    @Test
    public void testRandomLoadbalance() {
        int size = 5;
        List<Integer> items = TestUtil.generate(0, size);
        int[] resutls = new int[size];
        Loadbalance<Integer> loadbalance = new RandomLoadbalance<>();
        for (int i = 0; i < size * N; i++) {
            Integer item = loadbalance.choice(items);
            resutls[item.intValue()] ++;
        }
        System.out.println(String.format("results=[%s]", Arrays.toString(resutls)));
        for (int i = 0; i < size; i++) {
            Assert.assertTrue(resutls[i] > 0.9*N);
            Assert.assertTrue(resutls[i] < 1.1*N);
        }
    }

    @Test
    public void testRoundRobbinLoadbalance() {
        int size = 5;
        List<Integer> items = TestUtil.generate(0, size);
        int[] resutls = new int[size];
        Loadbalance<Integer> loadbalance = new RoundRobbinLoadbalance<>();
        for (int i = 0; i < size * N; i++) {
            Integer item = loadbalance.choice(items);
            resutls[item.intValue()] ++;
        }
        System.out.println(String.format("results=[%s]", Arrays.toString(resutls)));
        for (int i = 0; i < size; i++) {
            Assert.assertEquals(N, resutls[i]);
        }
    }

}