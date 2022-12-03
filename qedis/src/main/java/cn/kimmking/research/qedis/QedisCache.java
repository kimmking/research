package cn.kimmking.research.qedis;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2022/12/4 00:54
 */
@Component
@Data
public class QedisCache {

    private Map<String, String> map = new ConcurrentHashMap<>(1024);

}
