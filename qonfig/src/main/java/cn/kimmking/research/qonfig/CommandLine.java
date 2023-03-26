package cn.kimmking.research.qonfig;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2023/3/24 13:56
 */
public class CommandLine {

    public static void main(String[] args) {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        System.out.println("PID：" + runtimeMXBean.getPid());
        System.out.println("启动时间：" + new Date(runtimeMXBean.getStartTime()).toString());
        System.out.println("JDK版本：" + runtimeMXBean.getVmName() + ","
                + runtimeMXBean.getVmVendor() + "," + runtimeMXBean.getVmVersion());
        System.out.println("启动参数：");
        runtimeMXBean.getInputArguments().forEach(System.out::println);
    }


}
