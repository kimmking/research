package cn.kimmking.research.timewindow;

import lombok.ToString;

/**
 * Ring Buffer implement based on an int array.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022-11-20 19:39:27
 */
@ToString
public class RingBuffer {

    final int size;
    final int[] ring;

    public RingBuffer(int _size) {
        // check size > 0
        this.size = _size;
        this.ring = new int[this.size];
    }

    public int sum() {
        int _sum = 0;
        for (int i = 0; i < this.size; i++) {
            _sum += ring[i];
        }
        return _sum;
    }

    public void reset() {
        for (int i = 0; i < this.size; i++) {
            ring[i] = 0;
        }
    }

    public void reset(int index, int step) {
        for (int i = index; i < index + step; i++) {
            ring[i % this.size] = 0;
        }
    }

    public void incr(int index, int delta) {
        ring[index % this.size] += delta;
    }
}
