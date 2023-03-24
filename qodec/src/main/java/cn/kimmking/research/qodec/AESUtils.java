package cn.kimmking.research.qodec;

import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2023/3/17 08:40
 */
public class AESUtils {

    private static final String ALGORITHM = "AES";
    //密钥,AES的秘钥规定是16、24、32位秘钥
    private static final String KEY = "1234567890123456";
    //字符集
    private static final String UTF8 = StandardCharsets.UTF_8.name();

    /**
     * 加密
     * @param text 待加密的内容
     * @return
     */
    public static String encrypt(String text) throws Exception{
        Cipher instance = Cipher.getInstance(ALGORITHM);
        SecretKey secretKey = new SecretKeySpec(KEY.getBytes(UTF8), ALGORITHM);
        instance.init(Cipher.ENCRYPT_MODE,secretKey);
        byte[] encodedBytes = instance.doFinal(text.getBytes(UTF8));
        return Base64Utils.encodeToString(encodedBytes);
    }

    /**
     * 解密
     * @param text 加密后的字符串
     * @return
     * @throws Exception
     */
    public static String decrypt(String text) throws Exception{
        byte[] bytes = Base64Utils.decodeFromString(text);
        Cipher instance = Cipher.getInstance(ALGORITHM);
        SecretKey secretKey = new SecretKeySpec(KEY.getBytes(UTF8), ALGORITHM);
        instance.init(Cipher.DECRYPT_MODE,secretKey);
        byte[] decryptedBytes = instance.doFinal(bytes);
        return new String(decryptedBytes,UTF8);
    }

    public static void main(String...args) throws Exception{
        String str = "this is an AES text";
        String encrypt = encrypt(str);
        String decrypt = decrypt(encrypt);
        System.out.println("加密后的结果："+encrypt);
        System.out.println("解码后的结果："+decrypt);
    }


}
