package com.siliver.admin.util;

import org.apache.commons.lang3.RandomUtils;

import java.awt.*;
import java.util.Random;

/**
 * 验证码随机生成工具类
 */
public class CustomRandomUtils extends RandomUtils {

    private static final char[] CODE_SEQ = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J',
            'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9'};

    private static final char[] NUMBER_ARRAY = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private static final Random random = new Random();

    /**
     * 随机长度的字母数字的字符串生成
     *
     * @param length 要生成的长度
     * @return 生成结果
     */
    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CODE_SEQ[random.nextInt(CODE_SEQ.length)]);
        }
        return sb.toString();
    }

    /**
     * 随机长度的数字的字符串生成
     *
     * @param length 要生成的长度
     * @return 生成结果
     */
    public static String randomNumberString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(NUMBER_ARRAY[random.nextInt(NUMBER_ARRAY.length)]);
        }
        return sb.toString();
    }

    /**
     * 随机颜色生成
     *
     * @param fc 基数
     * @param bc 变数
     * @return 生成结果
     */
    public static Color randomColor(int fc, int bc) {
        fc = Math.min(fc, 255);
        bc = Math.min(bc, 255);
        return new Color(fc + random.nextInt(bc - fc), fc + random.nextInt(bc - fc), fc + random.nextInt(bc - fc));
    }
}
