package cn.kimmking.research.agentx;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;


/**
 * XAgent based on ByteBuddy.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/25 00:15
 */
public class XAgentByteBuddy {

    public static void premain(String args, Instrumentation inst) {
        System.out.println("invoke premain: " + args);

        new AgentBuilder.Default().type(ElementMatchers.named("Demo"))
                .transform((builder, type, c, j) -> builder
                        .method(ElementMatchers.named("hello"))
                        .intercept(MethodDelegation.to(LogTimeAdvisor.class)))
                .installOn(inst);
    }

    // 注意这里必须public，否则interpret不起作用
    public static class LogTimeAdvisor {

        @RuntimeType
        public static Object intercept(@Origin Method method,
                                       @AllArguments Object[] args,
                                       @SuperCall Callable<?> callable) {
            System.out.println(" ==> Enter " + method.getName() + " with arguments: " + Arrays.toString(args));
            long start = System.currentTimeMillis();
            try {
                Object res = callable.call();
                System.out.println("result: " + res);
                return res;
            } catch (Exception e) {
                System.out.println("error: " + e.getMessage());
            } finally {
                long end = System.currentTimeMillis();
                System.out.println(" ==> Exit " + method.getName() + " execute in "+(end - start)+" ms");
            }
            return null;
        }

    }

//    public static class LoggerAdvisor {
//        static long _start = 0L;
//        @OnMethodEnter
//        public static void onMethodEnter(@Advice.Origin Method method, @AllArguments Object[] arguments) {
//            System.out.println("Enter " + method.getName() + " with arguments: " + Arrays.toString(arguments));
//            _start = System.currentTimeMillis();
//        }
//
//        @OnMethodExit
//        public static void onMethodExit(@Advice.Origin Method method, @AllArguments Object[] arguments, @Return Object ret) {
//            System.out.println("Exit " + method.getName() + " with arguments: " + Arrays.toString(arguments)
//                    + " return: " + ret + ", cost: " + (System.currentTimeMillis() - _start) + "ms");
//        }
//    }

//        new AgentBuilder.Default().type(ElementMatchers.named("Demo"))
//                .transform((builder, type, classLoader, module, pd) -> builder
//                .method(ElementMatchers.named("hello"))
//                        .intercept(MethodDelegation.to(XInterceptor.class)
//                                .andThen(SuperMethodCall.INSTANCE)))
//                .installOn(inst);
//    private static class XInterceptor {
//        static void intercept(@Origin Method method) {
//            System.out.println(method.getName());
//        }
//    }

}
