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
public class SpopCommand implements Command {
    @Override
    public String name() {
        return "SPOP";
    }

    @Override
    public Reply<?> exec(QedisCache cache, String[] args) {
        String key = getKey(args);
        return Reply.bulkString(cache.spop(key));
    }
}
