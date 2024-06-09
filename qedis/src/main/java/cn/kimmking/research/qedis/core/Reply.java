package cn.kimmking.research.qedis.core;

import lombok.Data;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/6/6 上午2:28
 */

@Data
public class Reply<T> {
    ReplyType type;
    T value;

    public static Reply<String> string(String value)
    {
        Reply<String> reply = new Reply<>();
        reply.type = ReplyType.SIMPLE_STRING;
        reply.value = value;
        return reply;
    }

    public static Reply<String> bulkString(String value)
    {
        Reply<String> reply = new Reply<>();
        reply.type = ReplyType.BULK_STRING;
        reply.value = value;
        return reply;
    }

    public static Reply<Integer> integer(Integer value)
    {
        Reply<Integer> reply = new Reply<>();
        reply.type = ReplyType.INT;
        reply.value = value;
        return reply;
    }

    public static Reply<String> error(String value)
    {
        Reply<String> reply = new Reply<>();
        reply.type = ReplyType.ERROR;
        reply.value = value;
        return reply;
    }

    public static Reply<String[]> array(String[] value)
    {
        Reply<String[]> reply = new Reply<>();
        reply.type = ReplyType.ARRAY;
        reply.value = value;
        return reply;
    }

    public static final Reply<String> OK = string("OK");

}
