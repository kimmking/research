package cn.kimmking.research.qedis.commands;

import cn.kimmking.research.qedis.core.Command;
import cn.kimmking.research.qedis.core.QedisCache;
import cn.kimmking.research.qedis.core.Reply;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/6/6 下午6:51
 */
public class IncrCommand implements Command {
    @Override
    public String name() {
        return "INCR";
    }

    @Override
    public Reply<?> exec(QedisCache cache, String[] args) {
        String key = getKey(args);
        String value = cache.get(key);
        try{
            int intValue = Integer.parseInt(value);
            cache.set(key, "" + ++intValue);
            return Reply.integer(intValue);
        } catch (NumberFormatException nfe) {
            return Reply.error("NFE " + value + " can't convert to int value");
        }
    }
}
