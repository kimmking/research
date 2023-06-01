package cn.kimmking.research.qonfig;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedExample {
    public static void main(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile("data.bin", "rw");
        FileChannel channel = file.getChannel();
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, channel.size());

        // Create a slice of the MappedByteBuffer
        MappedByteBuffer slice = (MappedByteBuffer) buffer.slice();

        // Print the position and limit of the original buffer and the slice
        System.out.println("Buffer position: " + buffer.position());
        System.out.println("Buffer limit: " + buffer.limit());
        System.out.println("Slice position: " + slice.position());
        System.out.println("Slice limit: " + slice.limit());

        // Change the position and limit of the slice
        slice.position(5);
        slice.limit(10);


        slice.put(0, (byte) 'H');
        slice.put(1, (byte) 'e');
        slice.put(2, (byte) 'l');
        slice.put(3, (byte) 'l');
        slice.put(4, (byte) 'o');
        slice.force();

//        // Print the position and limit of the original buffer and the slice
//        System.out.println("Buffer position: " + buffer.position());
//        System.out.println("Buffer limit: " + buffer.limit());
//        System.out.println("Slice position: " + slice.position());
//        System.out.println("Slice limit: " + slice.limit());

        channel.close();
        file.close();



    }
}
