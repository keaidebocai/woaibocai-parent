package top.woaibocai.common.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * @program: woaibocai-parent
 * @description: MD5加密
 * @author: woaibocai
 * @create: 2023-12-09 19:32
 **/
@Component
public class MD5util {
    private static final String salt = "woaibocai";

    public static String md5(String src) {
        return DigestUtils.md5DigestAsHex(src.getBytes());
    }

    // 一次加密，用上面固定的salt值加密
    public static String onlySaltPass(String password) {
        String str = "" + salt.charAt(1) + salt.charAt(6) + password + salt.charAt(2) + salt.charAt(8);
        return md5(str);
    }
    // 两次加密 salt 由用户输入 注意 salt 必须大于6位 你可以不用这个但是你不能没有
    public static String userSaltPass(String password,String salt) {
        String str = "" + salt.charAt(1) + salt.charAt(1) + salt.charAt(4) + password + salt.charAt(5) + salt.charAt(1) + salt.charAt(4);
        return md5(str);
    }
}
