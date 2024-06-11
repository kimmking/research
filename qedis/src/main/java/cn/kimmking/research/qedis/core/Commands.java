package cn.kimmking.research.qedis.core;

import cn.kimmking.research.qedis.commands.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/6/6 上午12:58
 */
public class Commands {

    public static final Map<String, Command> ALL = new LinkedHashMap<>();

    static {
        initCommand();
    }

    private static void initCommand() {
        registerCommand(new CommandCommand());
        registerCommand(new InfoCommand());
        registerCommand(new PingCommand());

        registerCommand(new SetCommand());
        registerCommand(new GetCommand());
        registerCommand(new MsetCommand());
        registerCommand(new MgetCommand());
        registerCommand(new ExistsCommand());
        registerCommand(new DelCommand());
        registerCommand(new StrlenCommand());
        registerCommand(new IncrCommand());
        registerCommand(new DecrCommand());

        registerCommand(new LpushCommand());
        registerCommand(new LpopCommand());
        registerCommand(new LlenCommand());
        registerCommand(new LindexCommand());
        registerCommand(new LrangeCommand());
        registerCommand(new RpushCommand());
        registerCommand(new RpopCommand());

        registerCommand(new HsetCommand());
        registerCommand(new HgetCommand());
        registerCommand(new HgetallCommand());
        registerCommand(new HdelCommand());
        registerCommand(new HlenCommand());
        registerCommand(new HexistsCommand());

    }

    private static void registerCommand(Command cmd) {
        ALL.put(cmd.name(), cmd);
    }

    public static Command getCommand(String name) {
        return ALL.get(name);
    }

    public static String[] getCommandNames() {
        return ALL.keySet().toArray(new String[0]);
    }

}
