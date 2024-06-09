package cn.kimmking.research.qedis.commands;

import cn.kimmking.research.qedis.core.Command;
import cn.kimmking.research.qedis.core.QedisCache;
import cn.kimmking.research.qedis.core.Reply;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/6/6 上午1:03
 */
public class PingCommand implements Command {

    @Override
    public String name() {
        return "PING";
    }

    @Override
    public Reply exec(QedisCache cache, String[] args) {
        return Reply.string("PONG");
    }
}
