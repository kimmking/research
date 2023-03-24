package cn.kimmking.research.codec;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2023/3/24 09:44
 */
public class SHA1Utils {
    /**
     * redis-cli -p 7000 script load "local a=1 return a"
     * "59654efe3e72566455691e750f8b744198b7ba87"
     */
    public static void main(String[] args) {
        String str = "local a=1 return a";
        String sha1Hex = DigestUtils.sha1Hex(str);
        System.out.println(sha1Hex); // 输出：59654efe3e72566455691e750f8b744198b7ba87
    }
}
