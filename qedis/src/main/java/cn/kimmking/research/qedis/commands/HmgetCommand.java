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
public class HmgetCommand implements Command {
    @Override
    public String name() {
        return "HMGET";
    }

    @Override
    public Reply<?> exec(QedisCache cache, String[] args) {
        String key = getKey(args);
        String[] fields = getParamsNoKey(args);
        return Reply.array(cache.hmget(key, fields));
    }

}
