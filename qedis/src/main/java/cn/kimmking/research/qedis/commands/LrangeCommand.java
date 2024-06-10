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
public class LrangeCommand implements Command {
    @Override
    public String name() {
        return "LRANGE";
    }

    @Override
    public Reply<?> exec(QedisCache cache, String[] args) {
        String key = getKey(args);
        String[] paramsNoKey = getParamsNoKey(args);
        int start = Integer.parseInt(paramsNoKey[0]);
        int stop = Integer.parseInt(paramsNoKey[1]);
        return Reply.array(cache.lrange(key, start, stop));
    }
}
