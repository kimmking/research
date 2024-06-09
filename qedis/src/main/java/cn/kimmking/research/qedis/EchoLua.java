package cn.kimmking.research.qedis;

import java.io.File;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2023/4/4 15:00
 */
public class EchoLua {
    public static void main(String[] args) {
        File file = new File("qedis/src/main/resources/lua");
        if(!file.exists()) {
            file = new File("src/main/resources/lua");
        }
        System.out.println("lua dir => " + file.getAbsolutePath());
        if(file.isDirectory()) {
            for (File lua : file.listFiles()) {
                System.out.println("lua script => " + lua.getName());
            }
        }
    }
}
