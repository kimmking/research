package cn.kimmking.research.cluster.retry;

import java.util.function.BooleanSupplier;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/28 14:46
 */
public interface Retryer {

    int getMaxAttempt();

    int getLeftAttempt();

    boolean execute(BooleanSupplier supplier);

    boolean withException(BooleanSupplier supplier);

}
