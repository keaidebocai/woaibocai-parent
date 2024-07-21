package top.woaibocai.common.utils;

import java.util.Random;


/**
 * @program: woaibocai-parent
 * @description: 生成随机数组合的工具类
 * @author: LikeBocai
 * @create: 2024/7/18 14:17
 **/
public class RandomUtils {
    private  static final String CHARACTERS = "abcdefghigklmnopqrstuvwxyz";

    private  static final String NUMBERS = "0123456789";

    private  static final String CHARACTES_AND_NUMBERS = "abcdefghigklmnopqrstuvwxyz01234567890";

    public static String randomStringAndNumber(int length) {
        StringBuffer stringAndNumber = new StringBuffer(length);
        Random random = new Random();
        for (int i = 0; i < length; ++i) {
            int index = random.nextInt(CHARACTES_AND_NUMBERS.length());
            stringAndNumber.append(CHARACTES_AND_NUMBERS.charAt(index));
        }
        return stringAndNumber.toString();
    }

    public static String randomString(int length) {
        StringBuffer sb = new StringBuffer(length);
        Random random = new Random();
        for (int i = 0; i < length; ++i) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    public static String randomNumber(int length) {
        StringBuffer sb = new StringBuffer(length);
        Random random = new Random();
        for (int i = 0; i < length; ++i) {
            int index = random.nextInt(NUMBERS.length());
            sb.append(NUMBERS.charAt(index));
        }
        return sb.toString();
    }

    public static String randomBetween(Long max, Long min) {
        Random random = new Random();
        return String.valueOf(random.nextLong(max - min + 1L)+ min);
    }
}
