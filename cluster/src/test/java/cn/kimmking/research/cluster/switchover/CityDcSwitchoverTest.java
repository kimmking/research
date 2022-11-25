package cn.kimmking.research.cluster.switchover;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/25 22:01
 */
public class CityDcSwitchoverTest {

    static final int N = 20000;

    @Test
    public void testCityDcSwitchover_case1() {

        List<Integer> originItems = generate(0, 3);
        int originSize = 5;
        List<Integer> backupItems = generate(100, 5);
        int backupSize = 5;
        double threshold = 0.8;

        CityDcSwitchover<Integer> switchover = new CityDcSwitchover();
        List<Integer> result = switchover.filter(originSize, originItems, backupSize, backupItems, threshold);
        System.out.println(Arrays.toString(result.toArray()));
        int originRatio = 0;
        int backupRatio = 0;
        for (int i = 0; i < N; i++) {
            result = switchover.filter(originSize, originItems, backupSize, backupItems, threshold);
            if(result.get(0) == 0) originRatio++;
            if(result.get(0) == 100) backupRatio++;
        }

        System.out.println("originRatio: " + originRatio);
        System.out.println("backupRatio: " + backupRatio);

        double ratio = (double) originRatio/backupRatio;
        System.out.println("ratio = originRatio/backupRatio = " + ratio);
        Assert.assertTrue(ratio > 3d*0.9);
        Assert.assertTrue(ratio < 3d*1.1);

    }

    @Test
    public void testCityDcSwitchover_case2() {

        List<Integer> originItems = generate(0, 5);
        int originSize = 15;
        List<Integer> backupItems = generate(100, 20);
        int backupSize = 20;
        double threshold = 0.7;

        CityDcSwitchover<Integer> switchover = new CityDcSwitchover();
        List<Integer> result = switchover.filter(originSize, originItems, backupSize, backupItems, threshold);
        System.out.println(Arrays.toString(result.toArray()));
        int originRatio = 0;
        int backupRatio = 0;
        for (int i = 0; i < N; i++) {
            result = switchover.filter(originSize, originItems, backupSize, backupItems, threshold);
            if(result.get(0) == 0) originRatio++;
            if(result.get(0) == 100) backupRatio++;
        }

        System.out.println("originRatio: " + originRatio);
        System.out.println("backupRatio: " + backupRatio);

        double ratio = (double) originRatio/backupRatio;
        double expected = 0.476d/0.524d;
        System.out.println("ratio = originRatio/backupRatio = " + ratio + ", expected = " + expected);

        Assert.assertTrue(ratio > expected*0.9);
        Assert.assertTrue(ratio < expected*1.1);

    }

    @Test
    public void testCityDcSwitchover_case3() {

        List<Integer> originItems = generate(0, 35);
        int originSize = 50;
        List<Integer> backupItems = generate(100, 45);
        int backupSize = 50;
        double threshold = 0.6;

        CityDcSwitchover<Integer> switchover = new CityDcSwitchover();
        List<Integer> result = switchover.filter(originSize, originItems, backupSize, backupItems, threshold);
        System.out.println(Arrays.toString(result.toArray()));
        int originRatio = 0;
        int backupRatio = 0;
        for (int i = 0; i < N; i++) {
            result = switchover.filter(originSize, originItems, backupSize, backupItems, threshold);
            if(result.get(0) == 0) originRatio++;
            if(result.get(0) == 100) backupRatio++;
        }

        System.out.println("originRatio: " + originRatio);
        System.out.println("backupRatio: " + backupRatio);

        Assert.assertTrue(originRatio == N);
        Assert.assertTrue(backupRatio == 0);

    }


    @Test
    public void testCityDcSwitchover_case4() {

        List<Integer> originItems = generate(0, 25);
        int originSize = 50;
        List<Integer> backupItems = generate(100, 45);
        int backupSize = 50;
        double threshold = 0.6;

        CityDcSwitchover<Integer> switchover = new CityDcSwitchover();
        List<Integer> result = switchover.filter(originSize, originItems, backupSize, backupItems, threshold);
        System.out.println(Arrays.toString(result.toArray()));
        int originRatio = 0;
        int backupRatio = 0;
        for (int i = 0; i < 20000; i++) {
            result = switchover.filter(originSize, originItems, backupSize, backupItems, threshold);
            if(result.get(0) == 0) originRatio++;
            if(result.get(0) == 100) backupRatio++;
        }

        System.out.println("originRatio: " + originRatio);
        System.out.println("backupRatio: " + backupRatio);

        double ratio = (double) originRatio/backupRatio;
        System.out.println("ratio = originRatio/backupRatio = " + ratio);
        Assert.assertTrue(ratio > 5d*0.9);
        Assert.assertTrue(ratio < 5d*1.1);

    }


    private List<Integer> generate(int s, int n) {
        List list = new ArrayList(n);
        for (int j = 0; j < n; j++) {
            list.add(s + j);
        }
        return list;
    }


}