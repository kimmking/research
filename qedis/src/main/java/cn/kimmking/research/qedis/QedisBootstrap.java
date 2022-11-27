package cn.kimmking.research.qedis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/26 16:23
 */

@SpringBootApplication
public class QedisBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(QedisBootstrap.class);
    }

    @Bean
    public ApplicationRunner createRunner(@Autowired ApplicationContext context) {
        return x -> {
            System.out.println("ApplicationArguments: " + x);
//            new Thread( () -> {
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                System.out.println("After sleep 3s => SpringApplication.exit.");
//                SpringApplication.exit(context, () -> 1);
//            }).start();
        };
    }

//    @Bean
//    public ApplicationListener<ApplicationReadyEvent> createReadyListener() {
//        return x -> {
//            System.out.println("ApplicationReadyEvent => " + x.getClass().getName());
//        };
//    }

}
