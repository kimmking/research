package cn.kimmking.research.qedis.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/26 22:09
 */
public class QedisChannelHolder extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //ctx.fireChannelActive();
        AttributeKey<String> key = AttributeKey.valueOf("name");
        ctx.channel().attr(key).set("kimmking");
    }

}
