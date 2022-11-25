package cn.kimmking.research.cluster.switchover;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static cn.kimmking.research.cluster.TestUtil.generate;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/25 22:01
 */
public class CityDcSwitchoverTest {

    static final int N = 20000;

    @Test
    public void testCityDcSwitchover_case0() {
        System.out.println("\r\n ==> flow switchover to backup test case0");
        List<Integer> originItems = generate(0, 3);
        int originSize = 5;
        List<Integer> backupItems = generate(100, 5);
        int backupSize = 5;
        double threshold = 0.8;
        printArgs(originSize, originItems, backupSize, backupItems, threshold);

        CityDcSwitchover<Integer> switchover = new CityDcSwitchover();
        List<Integer> result = switchover.filter(originSize, originItems, backupSize, backupItems, threshold);
        //System.out.println(Arrays.toString(result.toArray()));
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
        System.out.println(" ==> flow switchover to backup = " + String.format("%.2f%%",backupRatio*100.0/N) + "%");
        Assert.assertTrue(ratio > 3d*0.9);
        Assert.assertTrue(ratio < 3d*1.1);

    }


    @Test
    public void testCityDcSwitchover_case1() {
        System.out.println("\r\n ==> flow switchover to backup test case1");
        List<Integer> originItems = generate(0, 2);
        int originSize = 5;
        List<Integer> backupItems = generate(100, 5);
        int backupSize = 5;
        double threshold = 0.8;
        printArgs(originSize, originItems, backupSize, backupItems, threshold);

        CityDcSwitchover<Integer> switchover = new CityDcSwitchover();
        List<Integer> result = switchover.filter(originSize, originItems, backupSize, backupItems, threshold);
        //System.out.println(Arrays.toString(result.toArray()));
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
        System.out.println(" ==> flow switchover to backup = " + String.format("%.2f%%",backupRatio*100.0/N) + "%");
        Assert.assertTrue(ratio > 1d*0.9);
        Assert.assertTrue(ratio < 1d*1.1);

    }

    @Test
    public void testCityDcSwitchover_case2() {
        System.out.println("\r\n ==> flow switchover to backup test case2");
        List<Integer> originItems = generate(0, 5);
        int originSize = 15;
        List<Integer> backupItems = generate(100, 20);
        int backupSize = 20;
        double threshold = 0.7;
        printArgs(originSize, originItems, backupSize, backupItems, threshold);

        CityDcSwitchover<Integer> switchover = new CityDcSwitchover();
        List<Integer> result = switchover.filter(originSize, originItems, backupSize, backupItems, threshold);
        //System.out.println(Arrays.toString(result.toArray()));
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
        System.out.println(" ==> flow switchover to backup = " + String.format("%.2f%%",backupRatio*100.0/N) + "%");

        Assert.assertTrue(ratio > expected*0.9);
        Assert.assertTrue(ratio < expected*1.1);

    }

    @Test
    public void testCityDcSwitchover_case3() {
        System.out.println("\r\n ==> flow switchover to backup test case3");
        List<Integer> originItems = generate(0, 35);
        int originSize = 50;
        List<Integer> backupItems = generate(100, 45);
        int backupSize = 50;
        double threshold = 0.6;
        printArgs(originSize, originItems, backupSize, backupItems, threshold);

        CityDcSwitchover<Integer> switchover = new CityDcSwitchover();
        List<Integer> result = switchover.filter(originSize, originItems, backupSize, backupItems, threshold);
        //System.out.println(Arrays.toString(result.toArray()));
        int originRatio = 0;
        int backupRatio = 0;
        for (int i = 0; i < N; i++) {
            result = switchover.filter(originSize, originItems, backupSize, backupItems, threshold);
            if(result.get(0) == 0) originRatio++;
            if(result.get(0) == 100) backupRatio++;
        }

        System.out.println("originRatio: " + originRatio);
        System.out.println("backupRatio: " + backupRatio);
        System.out.println(" ==> flow switchover to backup = " + String.format("%.2f%%",backupRatio*100.0/N) + "%");

        Assert.assertTrue(originRatio == N);
        Assert.assertTrue(backupRatio == 0);

    }


    @Test
    public void testCityDcSwitchover_case4() {
        System.out.println("\r\n ==> flow switchover to backup test case4");
        List<Integer> originItems = generate(0, 25);
        int originSize = 50;
        List<Integer> backupItems = generate(100, 45);
        int backupSize = 50;
        double threshold = 0.6;
        printArgs(originSize, originItems, backupSize, backupItems, threshold);

        CityDcSwitchover<Integer> switchover = new CityDcSwitchover();
        List<Integer> result = switchover.filter(originSize, originItems, backupSize, backupItems, threshold);
        //System.out.println(Arrays.toString(result.toArray()));
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
        double expected = 5d;
        System.out.println("ratio = originRatio/backupRatio = " + ratio + ", expected = " + expected);
        System.out.println(" ==> flow switchover to backup = " + String.format("%.2f%%",backupRatio*100.0/N) + "%");
        Assert.assertTrue(ratio > expected*0.9);
        Assert.assertTrue(ratio < expected*1.1);

    }

    @Test
    public void testCityDcSwitchover_case5() {
        System.out.println("\r\n ==> flow switchover to backup test case5");
        List<Integer> originItems = generate(0, 25);
        int originSize = 50;
        List<Integer> backupItems = generate(100, 45);
        int backupSize = 50;
        double threshold = 0.8;
        printArgs(originSize, originItems, backupSize, backupItems, threshold);

        CityDcSwitchover<Integer> switchover = new CityDcSwitchover();
        List<Integer> result = switchover.filter(originSize, originItems, backupSize, backupItems, threshold);
        //System.out.println(Arrays.toString(result.toArray()));
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
        double expected = 5d/3;
        System.out.println("ratio = originRatio/backupRatio = " + ratio + ", expected = " + expected);
        System.out.println(" ==> flow switchover to backup = " + String.format("%.2f%%",backupRatio*100.0/N) + "%");
        Assert.assertTrue(ratio > expected*0.9);
        Assert.assertTrue(ratio < expected*1.1);

    }

    @Test
    public void testCityDcSwitchover_case6() {
        System.out.println("\r\n ==> flow switchover to backup test case6");
        List<Integer> originItems = generate(0, 25);
        int originSize = 50;
        List<Integer> backupItems = generate(100, 45);
        int backupSize = 50;
        double threshold = 0.7;
        printArgs(originSize, originItems, backupSize, backupItems, threshold);

        CityDcSwitchover<Integer> switchover = new CityDcSwitchover();
        List<Integer> result = switchover.filter(originSize, originItems, backupSize, backupItems, threshold);
        //System.out.println(Arrays.toString(result.toArray()));
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
        double expected = 2.5d;
        System.out.println("ratio = originRatio/backupRatio = " + ratio + ", expected = " + expected);
        System.out.println(" ==> flow switchover to backup = " + String.format("%.2f%%",backupRatio*100.0/N) + "%");
        Assert.assertTrue(ratio > expected*0.9);
        Assert.assertTrue(ratio < expected*1.1);

    }

    @Test
    public void testCityDcSwitchover_case7() {
        System.out.println("\r\n ==> flow switchover to backup test case7");
        List<Integer> originItems = generate(0, 25);
        int originSize = 50;
        List<Integer> backupItems = generate(100, 45);
        int backupSize = 50;
        double threshold = 0.6;
        printArgs(originSize, originItems, backupSize, backupItems, threshold);

        CityDcSwitchover<Integer> switchover = new CityDcSwitchover();
        List<Integer> result = switchover.filter(originSize, originItems, backupSize, backupItems, threshold);
        //System.out.println(Arrays.toString(result.toArray()));
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
        double expected = 5d;
        System.out.println("ratio = originRatio/backupRatio = " + ratio + ", expected = " + expected);
        System.out.println(" ==> flow switchover to backup = " + String.format("%.2f%%",backupRatio*100.0/N) + "%");
        Assert.assertTrue(ratio > expected*0.9);
        Assert.assertTrue(ratio < expected*1.1);

    }

    @Test
    public void testCityDcSwitchover_case8() {
        System.out.println("\r\n ==> flow switchover to backup test case8");
        List<Integer> originItems = generate(0, 20);
        int originSize = 50;
        List<Integer> backupItems = generate(100, 45);
        int backupSize = 50;
        double threshold = 0.6;
        printArgs(originSize, originItems, backupSize, backupItems, threshold);

        CityDcSwitchover<Integer> switchover = new CityDcSwitchover();
        List<Integer> result = switchover.filter(originSize, originItems, backupSize, backupItems, threshold);
        //System.out.println(Arrays.toString(result.toArray()));
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
        double expected = 2d;
        System.out.println("ratio = originRatio/backupRatio = " + ratio + ", expected = " + expected);
        System.out.println(" ==> flow switchover to backup = " + String.format("%.2f%%",backupRatio*100.0/N) + "%");
        Assert.assertTrue(ratio > expected*0.9);
        Assert.assertTrue(ratio < expected*1.1);

    }

    @Test
    public void testCityDcSwitchover_case9() {
        System.out.println("\r\n ==> flow switchover to backup test case9");
        List<Integer> originItems = generate(0, 15);
        int originSize = 50;
        List<Integer> backupItems = generate(100, 45);
        int backupSize = 50;
        double threshold = 0.6;
        printArgs(originSize, originItems, backupSize, backupItems, threshold);

        CityDcSwitchover<Integer> switchover = new CityDcSwitchover();
        List<Integer> result = switchover.filter(originSize, originItems, backupSize, backupItems, threshold);
        //System.out.println(Arrays.toString(result.toArray()));
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
        double expected = 1d;
        System.out.println("ratio = originRatio/backupRatio = " + ratio + ", expected = " + expected);
        System.out.println(" ==> flow switchover to backup = " + String.format("%.2f%%",backupRatio*100.0/N) + "%");
        Assert.assertTrue(ratio > expected*0.9);
        Assert.assertTrue(ratio < expected*1.1);

    }

    @Test
    public void testCityDcSwitchover_case10() {
        System.out.println("\r\n ==> flow switchover to backup test case10");
        List<Integer> originItems = generate(0, 10);
        int originSize = 50;
        List<Integer> backupItems = generate(100, 45);
        int backupSize = 50;
        double threshold = 0.6;
        printArgs(originSize, originItems, backupSize, backupItems, threshold);

        CityDcSwitchover<Integer> switchover = new CityDcSwitchover();
        List<Integer> result = switchover.filter(originSize, originItems, backupSize, backupItems, threshold);
        //System.out.println(Arrays.toString(result.toArray()));
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
        double expected = 0.5d;
        System.out.println("ratio = originRatio/backupRatio = " + ratio + ", expected = " + expected);
        System.out.println(" ==> flow switchover to backup = " + String.format("%.2f%%",backupRatio*100.0/N) + "%");
        Assert.assertTrue(ratio > expected*0.9);
        Assert.assertTrue(ratio < expected*1.1);

    }

    private void printArgs(int originSize, List<Integer> originItems, int backupSize, List<Integer> backupItems, double threshold) {
        System.out.println(String.format("originSize,originItems.size(),backupSize,backupItems.size(),threshold=[%d,%d,%d,%d,%1.2f]",originSize,originItems.size(),backupSize,backupItems.size(),threshold));
    }

}