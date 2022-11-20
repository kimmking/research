package cn.kimmking.research.timewindow;

import lombok.ToString;

/**
 * SlidingTimeWindow implement based on RingBuffer and TS(timestamp).
 * Use TS/1000->SecondNumber to mapping an index slot in a RingBuffer.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022-11-20 19:39:27
 */
@ToString
public class SlidingTimeWindow {

    public static final int DEFAULT_SIZE = 30;

    private final int size;
    private final RingBuffer ringBuffer;
    private int sum = 0;

//    private int _start_mark = -1;
//    private int _prev_mark  = -1;
    private int _curr_mark  = -1;

    private long _start_ts = -1L;
 //   private long _prev_ts  = -1L;
    private long _curr_ts  = -1L;

    public SlidingTimeWindow() {
        this(DEFAULT_SIZE);
    }

    public SlidingTimeWindow(int _size) {
        this.size = _size;
        this.ringBuffer = new RingBuffer(this.size);
    }

    /**
     * record current ts millis.
     *
     * @param millis
     */
    public synchronized void record(long millis) {
        System.out.println("window before: " + this.toString());
        System.out.println("window.record(" + millis + ")");
        long ts = millis / 1000;
        if (_start_ts == -1L) {
            initRing(ts);
        } else {   // TODO  Prev 是否需要考虑
            if(ts == _curr_ts) {
                System.out.println("window ts:" + ts + ", curr_ts:" + _curr_ts + ", size:" + size);
                this.ringBuffer.incr(_curr_mark, 1);
            } else if(ts > _curr_ts && ts < _curr_ts + size) {
                int offset = (int)(ts - _curr_ts);
                System.out.println("window ts:" + ts + ", curr_ts:" + _curr_ts + ", size:" + size + ", offset:" + offset);
                this.ringBuffer.reset(_curr_mark + 1, offset);
                this.ringBuffer.incr(_curr_mark + offset, 1);
                _curr_ts = ts;
                _curr_mark = (_curr_mark + offset) % size;
            } else if(ts >= _curr_ts + size) {
                System.out.println("window ts:" + ts + ", curr_ts:" + _curr_ts + ", size:" + size);
                this.ringBuffer.reset();
                initRing(ts);
            }
        }
        this.sum = this.ringBuffer.sum();
        System.out.println("window after: " + this.toString());
    }

    private void initRing(long ts) {
        System.out.println("window initRing ts:" + ts);
        this._start_ts  = ts;
        this._curr_ts   = ts;
        this._curr_mark = 0;
        this.ringBuffer.incr(0, 1);
    }

    public int getSize() {
        return size;
    }

    public int getSum() {
        return sum;
    }

    public RingBuffer getRingBuffer() {
        return ringBuffer;
    }

    public int get_curr_mark() {
        return _curr_mark;
    }

    public long get_start_ts() {
        return _start_ts;
    }

    public long get_curr_ts() {
        return _curr_ts;
    }

}
