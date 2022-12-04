//package cn.kimmking.research.qedis.server;
//
//import io.netty.handler.codec.redis.RedisMessage;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Description for this class.
// *
// * @Author : kimmking(kimmking@apache.org)
// * @create 2022/11/26 22:18
// */
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//public class QedisThreadLocalContext {
//
//    private StringBuffer qedisBuffer = new StringBuffer(64);
//    private List<RedisMessage> commands = new ArrayList<>(16);
//
//    private final static ThreadLocal<QedisThreadLocalContext> QEDIS_THREAD_LOCAL_CONTEXT = new ThreadLocal<>() {
//        @Override
//        protected QedisThreadLocalContext initialValue() {
//            return new QedisThreadLocalContext();
//        }
//    };
//
//    public static QedisThreadLocalContext get() {
//        return QEDIS_THREAD_LOCAL_CONTEXT.get();
//    }
//}
