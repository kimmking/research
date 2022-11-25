package cn.kimmking.research.cluster.switchover;

import java.util.List;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/25 21:38
 */
public interface Switchover<T> {

    List<T> filter(int originSize, List<T> originItems,int backupSize, List<T> backupItems, double threshold);

}
