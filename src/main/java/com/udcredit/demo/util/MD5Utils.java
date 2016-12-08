package com.udcredit.demo.util;

import java.security.MessageDigest;

/**
 * MD5辅助类
 *
 * @author geosmart
 * @date 2016-10-05
 */
public class MD5Utils {
    private static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static final String MD5Encrpytion(byte[] source) {

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(source);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
                str[(k++)] = hexDigits[(byte0 & 0xF)];
            }
            for (int m = 0; m < str.length; ++m) {
                if ((str[m] >= 'a') && (str[m] <= 'z')) {
                    str[m] = (char) (str[m] - ' ');
                }
            }
            //System.out.println("[MD5Utils] [source String]" + source);
            //System.out.println("[MD5Utils] [MD5    String]" + new String(str));
            return new String(str);
        } catch (Exception e) {
        }
        return null;
    }
}