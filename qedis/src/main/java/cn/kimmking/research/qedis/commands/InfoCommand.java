package cn.kimmking.research.qedis.commands;

import cn.kimmking.research.qedis.core.Command;
import cn.kimmking.research.qedis.core.QedisCache;
import cn.kimmking.research.qedis.core.Reply;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/6/6 上午12:56
 */
public class InfoCommand implements Command {

    private static final String QEDIS_SERVER_VERSION = "0.0.1";
    private static final String QEDIS_SERVER_INFO = "Qedis Server, Mock Redis, version[" + QEDIS_SERVER_VERSION + "]";

    @Override
    public String name() {
        return "INFO";
    }

    @Override
    public Reply<?> exec(QedisCache cache, String[] args) {
        return Reply.bulkString(QEDIS_SERVER_INFO + CRLF
                + "Created by KimmKing(kimmking@apache.org)" + CRLF
                + "At 2024-06-06 03:26:42 in Beijing, China" + CRLF);
    }
}
