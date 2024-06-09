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
public class MsetCommand implements Command {
    @Override
    public String name() {
        return "MSET";
    }

    @Override
    public Reply<?> exec(QedisCache cache, String[] args) {
        String[] keys = getKeys(args);
        String[] vals = getVals(args);
        for (int i = 0; i < keys.length; i++) {
            cache.set(keys[i], vals[i]);
        }
        return Reply.integer(keys.length);
    }
}
