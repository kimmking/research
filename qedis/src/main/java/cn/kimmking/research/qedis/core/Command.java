package cn.kimmking.research.qedis.core;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/6/5 下午9:04
 */
public interface Command {

    String CRLF = "\r\n";

    String name();
    Reply<?> exec(QedisCache cache, String[] args);

    default String getCmd(String[] args) {
        return args[2].toUpperCase();
    }

    default String getKey(String[] args) {
        return args[4];
    }

    default String getVal(String[] args) {
        return args[6];
    }

    default String[] getKeys(String[] args) {
        int len = (args.length-3)/4;
        String[] keys = new String[len];
        for (int i = 0; i < len; i++) {
            keys[i] = args[4 + i*4];
        }
        return keys;
    }

    default String[] getVals(String[] args) {
        int len = (args.length-3)/4;
        String[] vals = new String[len];
        for (int i = 0; i < len; i++) {
            vals[i] = args[6 + i*4];
        }
        return vals;
    }

    default String[] getParams(String[] args) {
        int len = (args.length-3)/2;
        String[] vals = new String[len];
        for (int i = 0; i < len; i++) {
            vals[i] = args[4 + i*2];
        }
        return vals;
    }
}
