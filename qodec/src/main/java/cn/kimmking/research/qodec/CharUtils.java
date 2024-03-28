package cn.kimmking.research.qodec;

import lombok.SneakyThrows;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2023/7/4 10:27
 */
public class CharUtils {


    @SneakyThrows
    public static void main(String[] args) {
        String s = "0123456789asdfghjklm";
        System.out.println(s.length());
        System.out.println(s.getBytes("UTF-8").length);
    }

}
