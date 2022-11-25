package cn.kimmking.research.cluster;

import java.util.ArrayList;
import java.util.List;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/26 01:50
 */
public class TestUtil {

    public static List<Integer> generate(int s, int n) {
        List list = new ArrayList(n);
        for (int j = 0; j < n; j++) {
            list.add(s + j);
        }
        return list;
    }

}
