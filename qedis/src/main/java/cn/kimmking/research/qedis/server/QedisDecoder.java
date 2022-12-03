package cn.kimmking.research.qedis.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/12/3 13:44
 */
public class QedisDecoder extends ByteToMessageDecoder {

    private AtomicInteger decoder_count = new AtomicInteger();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        System.out.println("decode start: " + decoder_count.get());

        if(in.readableBytes() <= 0) return;

//        decode start: 0
//        count:17,index:0
//        ret: *1
//        $7
//        COMMAND

        int count = in.readableBytes();
        int index = in.readerIndex();
        System.out.println(String.format("count:%d,index:%d", count, index));
//        byte header = in.readByte();
//        System.out.println("header: " + (char) header);

        byte[] buffer = new byte[count];
        // StringBuffer sb = new StringBuffer(128);

        in.readBytes(buffer);

        String ret = new String(buffer);

        System.out.println("ret: " + ret);

        out.add(ret);

        System.out.println("decode end: " + decoder_count.getAndIncrement());

    }
}
