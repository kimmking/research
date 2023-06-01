package cn.kimmking.research.redisbench;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2023/2/19 14:14
 */
public class RedisBenchUtils {

    public static ThreadPoolExecutor createExecutor(int core, int max, int queue) {
        return new ThreadPoolExecutor(core, max,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(queue),
                createThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    private static ThreadFactory createThreadFactory() {
        return new ThreadFactory() {
            private AtomicInteger id = new AtomicInteger();
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "RedisBench-" + id.getAndIncrement());
                thread.setDaemon(true);
                return thread;
            }
        };
    }



}
