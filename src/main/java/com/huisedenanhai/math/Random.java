/*
 * Created by huisedenanhai on 2018/06/04
 */

package com.huisedenanhai.math;

public class Random {
    public static String randomString(String charset, int length) {
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        int charsetSize = charset.length();

        for (int i = 0; i < length; ++i) {
            sb.append(charset.charAt(random.nextInt(charsetSize)));
        }
        return sb.toString();
    }
}
