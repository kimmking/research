package cn.kimmking.research.qedis.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.redis.ArrayHeaderRedisMessage;
import io.netty.handler.codec.redis.BulkStringHeaderRedisMessage;
import io.netty.handler.codec.redis.DefaultLastBulkStringRedisContent;
import io.netty.handler.codec.redis.RedisMessage;

import java.util.List;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/26 22:09
 */
public class QedisCommandHandler extends SimpleChannelInboundHandler<RedisMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RedisMessage msg) throws Exception {

        if(msg instanceof ArrayHeaderRedisMessage) {
            ArrayHeaderRedisMessage m = (ArrayHeaderRedisMessage) msg;
            System.out.println("  ==> " + m.toString());
        }
        if(msg instanceof BulkStringHeaderRedisMessage) {
            BulkStringHeaderRedisMessage m = (BulkStringHeaderRedisMessage) msg;
            System.out.println("  ==> " + m.bulkStringLength());
        }
        if(msg instanceof DefaultLastBulkStringRedisContent) {
            DefaultLastBulkStringRedisContent m = (DefaultLastBulkStringRedisContent) msg;
            System.out.println("  ====>> " + m.toString());
            ctx.writeAndFlush("+OK\r\n");
        }


        System.out.println();


    }
}
