package cn.kimmking.research.qedis.server;

import cn.kimmking.research.qedis.core.Command;
import cn.kimmking.research.qedis.core.Commands;
import cn.kimmking.research.qedis.core.QedisCache;
import cn.kimmking.research.qedis.core.Reply;
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
public class QedisCommandHandler extends SimpleChannelInboundHandler<String> {

    public static final String CRLF = "\r\n";

    private QedisCache cache;
    public QedisCommandHandler(QedisCache cache) {
        this.cache = cache;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String obj) throws Exception {

        System.out.println("ctx.attribute name => " + ctx.channel().attr(AttributeKey.valueOf("name")).get());

        String msg = obj;
        System.out.println("handler:" + CRLF + msg);

//        if(msg.trim().endsWith("COMMAND")) { // for redis-cli only
//            System.out.println("Process COMMAND:" + CRLF);
//            replyArray(ctx, null);
//            return;
//        }

        String[] args = msg.split(CRLF);
        System.out.println("command len: "+args.length);
        String cmdStr = args[2].toUpperCase();
        Command command = Commands.getCommand(cmdStr);

        if(command != null) {
            processCommand(ctx, command, args);
            return;
        }


//        if(cmdStr.equalsIgnoreCase("set")) {
//            setReply(ctx, args[4], args[6]);
//            //replyString(ctx, "X1" + CRLF); // 验证test_case_00
//        } else if(cmdStr.equalsIgnoreCase("get")) {
//            getReply(ctx, args[4]);
            //replyString(ctx, "X2" + CRLF); // 验证test_case_00
//        } else if(cmdStr.equalsIgnoreCase("info")) {
//            infoReply(ctx);
//        } else if(cmdStr.equalsIgnoreCase("del")) {
//            delReply(ctx, args[4]);
//        } else if(cmdStr.equalsIgnoreCase("strlen")) {
//            strlenReply(ctx, args[4]);
//        } else if(cmdStr.equalsIgnoreCase("exists")) {
//            existsReply(ctx, args[4]);
//        } else
//            if(cmdStr.equalsIgnoreCase("incr")) {
//            incrOrDecrReply(ctx, args[4], true);
//        } else if(cmdStr.equalsIgnoreCase("decr")) {
//            incrOrDecrReply(ctx, args[4], false);
//        } else
            if(cmdStr.equalsIgnoreCase("expire")) {
            expireReply(ctx, args[4], args[6]);
        } else if(cmdStr.equalsIgnoreCase("ttl")) {
            ttlReply(ctx, args[4]);
        } else if(cmdStr.equalsIgnoreCase("hget")) {
            hgetReply(ctx, args[4], args[6]);
        } else if(cmdStr.equalsIgnoreCase("hset")) {
            int hlen = args.length / 4 - 1;
            if(hlen == 0) {
                replyString(ctx, "OK");
                return;
            }
            String fields[] = new String[hlen];
            String values[] = new String[hlen];
            for(int i = 0; i < hlen; i++) {
                fields[i] = args[i * 4 + 6];
                values[i] = args[i * 4 + 8];
            }
            hsetReply(ctx, args[4], fields, values);
        } else { // default
            replyString(ctx, "OK");
        }
    }

    private void processCommand(ChannelHandlerContext ctx, Command command, String[] args) {
        System.out.println(" ===>> command[" + command.name() + "], " +
                "args = " + String.join(",", args));
        reply(ctx, command.exec(cache, args));
    }

    private void getReply(ChannelHandlerContext ctx, String key) {
        String value = this.cache.get(key);
        replyString(ctx,  value);
    }

    private void setReply(ChannelHandlerContext ctx, String key, String value) {
        this.cache.set(key, value);
        replyString(ctx,  "OK");
    }

    private void hsetReply(ChannelHandlerContext ctx, String key, String[] fields, String[] values) {
        for(int i = 0; i < fields.length; i++) {
            String field = fields[i];
            String value = values[i];
            this.cache.hset(key, field, value);
        }
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

    private void reply(ChannelHandlerContext ctx, Reply<?> reply) {
        switch (reply.getType()) {
            case SIMPLE_STRING:
                replyString(ctx, (String) reply.getValue());
                break;
            case ARRAY:
                replyArray(ctx, (Object[]) reply.getValue());
                break;
            case BULK_STRING:
                replyBulkString(ctx, (String) reply.getValue());
                break;
            case ERROR:
                replyError(ctx, "KK", reply.getValue().toString());
                break;
            case INT:
                replyInteger(ctx, (Integer) reply.getValue());
        }
    }


    private void replyArray(ChannelHandlerContext ctx, Object[] array) {
        writeByteBuf(ctx, decodeArray(array));
    }

    private static String decodeArray(Object[] array) {
        StringBuilder sb = new StringBuilder();
        if(array == null) {
            sb.append("*-1" + CRLF);
        } else if(array.length==0) {
            sb.append("*0" + CRLF);
        } else {
            sb.append("*").append(array.length).append(CRLF);
            for (Object obj : array) {
                if (obj instanceof String) {
                    sb.append("$").append(((String) obj).length()).append(CRLF);
                    sb.append(obj).append(CRLF);
                } else if (obj instanceof Integer) {
                    sb.append(":").append(obj).append(CRLF);
                } else if (obj instanceof Object[]) {
                    sb.append(decodeArray((Object[]) obj));
                }
            }
        }
        return sb.toString();
    }

    private void replyString(ChannelHandlerContext ctx, String str) {
        writeByteBuf(ctx, simpleString(str));
    }

    private void replyBulkString(ChannelHandlerContext ctx, String str) {
        writeByteBuf(ctx, bulkString(str));
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

    private String bulkString(String str) {
        if(str == null) {
            str = "$-1";
        }else if(str.isEmpty()) {
            str = "$0";
        }else {
            str = "$" + str.length() + CRLF + str;
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
        replyError(ctx, "EXC", cause.getMessage());
    }
}
