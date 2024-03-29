package cn.kimmking.research.qedis.server;

import cn.kimmking.research.qedis.QedisCache;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

import java.nio.charset.StandardCharsets;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/12/4 00:03
 */
public class QedisStringHandler extends SimpleChannelInboundHandler<String> {

    public static final String CRLF = "\r\n";

    private static final String QEDIS_SERVER_VERSION = "0.0.1";
    private static final String QEDIS_SERVER_INFO = "Qedis Server Mock Redis version" + QEDIS_SERVER_VERSION + " by kimmking@apache.org";
    private QedisCache cache;
    public QedisStringHandler(QedisCache cache) {
        this.cache = cache;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String obj) throws Exception {

        System.out.println("ctx.attribute name => " + ctx.channel().attr(AttributeKey.valueOf("name")).get());

        String msg = (String) obj;
        System.out.println("handler:" + CRLF + msg);

        if(msg.trim().endsWith("COMMAND")) { // for redis-cli only
            System.out.println("Process COMMAND:" + CRLF);
            replyArray(ctx, null);
            return;
        }

        String[] args = msg.split(CRLF);
        System.out.println("command len: "+args.length);
        String cmdStr = args[2];
        if(cmdStr.equalsIgnoreCase("set")) {
            setReply(ctx, args[4], args[6]);
            //replyString(ctx, "X1" + CRLF); // 验证test_case_00
        } else if(cmdStr.equalsIgnoreCase("get")) {
            getReply(ctx, args[4]);
            //replyString(ctx, "X2" + CRLF); // 验证test_case_00
        } else if(cmdStr.equalsIgnoreCase("info")) {
            infoReply(ctx);
        } else if(cmdStr.equalsIgnoreCase("del")) {
            delReply(ctx, args[4]);
        } else if(cmdStr.equalsIgnoreCase("strlen")) {
            strlenReply(ctx, args[4]);
        } else if(cmdStr.equalsIgnoreCase("exists")) {
            existsReply(ctx, args[4]);
        } else if(cmdStr.equalsIgnoreCase("incr")) {
            incrOrDecrReply(ctx, args[4], true);
        } else if(cmdStr.equalsIgnoreCase("decr")) {
            incrOrDecrReply(ctx, args[4], false);
        } else if(cmdStr.equalsIgnoreCase("expire")) {
            expireReply(ctx, args[4], args[6]);
        } else if(cmdStr.equalsIgnoreCase("ttl")) {
            ttlReply(ctx, args[4]);
        } else if(cmdStr.equalsIgnoreCase("hget")) {
            hgetReply(ctx, args[4], args[6]);
        } else if(cmdStr.equalsIgnoreCase("hset")) {
            hsetReply(ctx, args[4], args[6], args[8]);
        } else { // default
            replyString(ctx, "OK");
        }
    }



    private void infoReply(ChannelHandlerContext ctx) {
        replyString(ctx, QEDIS_SERVER_INFO); // TODO make it return an array for more info.
    }

    private void getReply(ChannelHandlerContext ctx, String key) {
        String value = this.cache.get(key);
        replyString(ctx,  value);
    }

    private void setReply(ChannelHandlerContext ctx, String key, String value) {
        this.cache.set(key, value);
        replyString(ctx,  "OK");
    }

    private void hsetReply(ChannelHandlerContext ctx, String key, String field, String value) {
        this.cache.hset(key, field, value);
        replyString(ctx,  "OK");
    }

    private void hgetReply(ChannelHandlerContext ctx, String key, String field) {
        String value = this.cache.hget(key, field);
        replyString(ctx,  value);
    }

    private void expireReply(ChannelHandlerContext ctx, String key, String sttl) {
        long ttl = Long.valueOf(sttl);
        boolean expire = this.cache.expire(key, ttl);
        replyInteger(ctx, expire ? 1 : 0);
    }

    private void ttlReply(ChannelHandlerContext ctx, String key) {
        long ttl = this.cache.ttl(key);
        replyInteger(ctx, (int) ttl);
    }

    private void delReply(ChannelHandlerContext ctx, String key) {
        this.cache.getMap().remove(key);
        replyInteger(ctx, 1);
    }

    private void existsReply(ChannelHandlerContext ctx, String key) {
        int len = this.cache.getMap().containsKey(key) ? 1 : 0;
        replyInteger(ctx, len);
    }

    private void strlenReply(ChannelHandlerContext ctx, String key) {
        String value = this.cache.get(key);
        int len = value == null ? -1 : value.length();
        replyInteger(ctx, len);
    }

    private void incrOrDecrReply(ChannelHandlerContext ctx, String key, boolean incr) {
        String value = this.cache.get(key);

        try{
            int intValue = Integer.parseInt(value);
            if(incr)
                intValue++;
            else
                intValue--;
            this.cache.set(key, Integer.valueOf(intValue).toString());
            replyInteger(ctx, intValue);
        } catch (NumberFormatException nfe) {
            replyError(ctx, "NFE", value + " can't convert to int value");
        }

    }
    private void replyArray(ChannelHandlerContext ctx, Object[] array) {
        if(array == null) {
            writeByteBuf(ctx, "*-1" + CRLF);
        }
        if(array.length==0) {
            writeByteBuf(ctx, "*0" + CRLF);
        }
        replyString(ctx, array[0].toString()); // TODO process an array type.
    }

    private void replyString(ChannelHandlerContext ctx, String str) {
        writeByteBuf(ctx, simpleString(str));
    }

    private void writeByteBuf(ChannelHandlerContext ctx, String str) {
        System.out.println("ByteBuf and reply:" + CRLF + str);
        ByteBuf buf = Unpooled.buffer(128);
        buf.writeBytes(str.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(buf);
    }

    private String simpleString(String str) {
        if(str == null) {
            str = "$-1";
        }else if(str.isEmpty()) {
            str = "$0";
        }else {
            str = "+" + str;
        }
        return str + CRLF;
    }

    private void replyInteger(ChannelHandlerContext ctx, int i) {
        writeByteBuf(ctx, simpleInteger(i));
    }

    private String simpleInteger(int i) {
        return ":" + i + CRLF;
    }

    private void replyError(ChannelHandlerContext ctx, String errType, String errMsg) {
        writeByteBuf(ctx, simpleError(errType, errMsg));
    }

    private String simpleError(String errType, String errMsg) {
        if(errType == null || errType.isEmpty()) {
            return "-" + errMsg + CRLF;
        } else {
            return "-" + errType + " " + errMsg + CRLF;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        System.out.println("Exception caught: " + cause.getMessage());
    }
}
