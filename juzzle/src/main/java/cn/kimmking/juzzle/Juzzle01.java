package cn.kimmking.juzzle;

/**
 *  magic HotSpotIntrinsicCandidate annotation
 *  See:
 *  1.https://zhuanlan.zhihu.com/p/165189170
 *  2.https://www.baeldung.com/jvm-intrinsics
 *
 * @Author : kimmking(kimmking@apache.org)
 */
public class Juzzle01 {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        long start = System.currentTimeMillis();
        testSqrt1();
        System.out.println("test1 take: " + (System.currentTimeMillis()-start) + "ms");

        start = System.currentTimeMillis();
        testSqrt2();
        System.out.println("test2 take: " + (System.currentTimeMillis()-start) + "ms");
    }

    private static void testSqrt1() {
        for (int a = 0; a < 1000_000; ++a) {
            double result = Math.sqrt(a);
        }
    }
    private static void testSqrt2() {
        for (int a = 0; a < 1000_000; ++a) {
            double result = StrictMath.sqrt(a);
        }
    }
}