package cn.kimmking.research.timewindow;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * SlidingTimeWindow Unit Test class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022-11-20 19:39:27
 */
public class SlidingTimeWindowTest {

    SlidingTimeWindow window = new SlidingTimeWindow(5); // default size 30

    @Test
    public void test_record() {
        // mock 10 seconds to eliminate window

        // Round 1 add 4
        long start_ts = System.currentTimeMillis();
        window.record(start_ts);
        window.record(start_ts);
        window.record(start_ts);
        window.record(start_ts);
        assertEquals(4, window.getSum());
        assertEquals(start_ts/1000, window.get_curr_ts());
        assertEquals(0, window.get_curr_mark());

        // Round 2 add 2 to 6
        long curr_ts = start_ts + 1000;
        window.record(curr_ts);
        window.record(curr_ts);
        assertEquals(6, window.getSum());
        assertEquals(curr_ts/1000, window.get_curr_ts());
        assertEquals(1, window.get_curr_mark());

        // Round 3
        // do nothing
        curr_ts = curr_ts + 1000;

        // Round 4 add 5 to 11
        curr_ts = curr_ts + 1000;
        window.record(curr_ts);
        window.record(curr_ts);
        window.record(curr_ts);
        window.record(curr_ts);
        window.record(curr_ts);
        assertEquals(11, window.getSum());
        assertEquals(curr_ts/1000, window.get_curr_ts());
        assertEquals(3, window.get_curr_mark());

        // Round 5 add 1 to 12
        // do nothing
        curr_ts = curr_ts + 1000;
        window.record(curr_ts);
        assertEquals(12, window.getSum());
        assertEquals(curr_ts/1000, window.get_curr_ts());
        assertEquals(4, window.get_curr_mark());

        // Round 6 == go back to index 0 and add 1/sub 4 to 9
        // do nothing
        curr_ts = curr_ts + 1000;
        window.record(curr_ts);
        assertEquals(9, window.getSum());
        assertEquals(curr_ts/1000, window.get_curr_ts());
        assertEquals(0, window.get_curr_mark());

        // Round 7
        // do nothing
        curr_ts = curr_ts + 1000;

        // Round 8
        // do nothing
        curr_ts = curr_ts + 1000;

        // Round 9
        // do nothing
        curr_ts = curr_ts + 1000;

        // Round 10
        // do nothing
        curr_ts = curr_ts + 1000;

        // Round 11
        // do nothing
        curr_ts = curr_ts + 1000;

        // Round 12 reset and add 3 to 3
        curr_ts = curr_ts + 1000;
        window.record(curr_ts);
        window.record(curr_ts);
        window.record(curr_ts);
        assertEquals(3, window.getSum());
        assertEquals(curr_ts/1000, window.get_curr_ts());
        assertEquals(0, window.get_curr_mark());

        // Round 13 add 5 to 8
        curr_ts = curr_ts + 1000;
        window.record(curr_ts);
        window.record(curr_ts);
        window.record(curr_ts);
        window.record(curr_ts);
        window.record(curr_ts);
        assertEquals(8, window.getSum());
        assertEquals(curr_ts/1000, window.get_curr_ts());
        assertEquals(1, window.get_curr_mark());
    }
}