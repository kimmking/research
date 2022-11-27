package cn.kimmking.research.qedis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/11/26 16:28
 */
@Component
public class QedisApplicationListener implements ApplicationListener<ApplicationEvent> {

    AtomicInteger index = new AtomicInteger(0);

//    @Autowired
//    ApplicationContext context;

    @Autowired
    List<QedisPlugin> plugins;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        int seq = index.getAndIncrement();
        System.out.println(seq + " event: " +event.getClass().getName());
        System.out.println(seq + " event.getSource() => " + event.getSource());

        if(event instanceof ApplicationReadyEvent) {
            // launch plugin components
            //Map<String, QedisPlugin> plugins = context.getBeansOfType(QedisPlugin.class);
            //for (QedisPlugin plugin : plugins.values()) {
            for (QedisPlugin plugin : plugins) {
                plugin.init();
                plugin.startup();
            }

        } else if (event instanceof ContextClosedEvent) {
            // only shutdown and close non-spring resource
            //Map<String, QedisPlugin> plugins = context.getBeansOfType(QedisPlugin.class);
            //for (QedisPlugin plugin : plugins.values()) {
            for (QedisPlugin plugin : plugins) {
                plugin.shutdown();
            }
        }


    }
}
