package com.vteba.utils.common;

import org.apache.commons.lang3.StringUtils;

/**
 * 驼峰命名法和下换线命名法互相转换。
 * @author yinlei
 * date 2013-6-15 下午10:28:26
 */
public class CaseUtils {

    public static final char SEPARATOR = '_';

    /**
     * 将驼峰命名法转换为下划线风格
     * @param s 待转换字符串
     * @return 转换后字符串
     * @author yinlei
     * date 2013-6-15 下午11:24:59
     */
    public static String underCase(String s) {
        if (s == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            
            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }
    
    /**
     * 将驼峰命名法转换为下划线风格，以 _ 开头
     * @param s 待转换字符串
     * @return 转换后字符串
     * @author yinlei
     * date 2013-6-15 下午11:24:59
     */
    public static String toUnderCase(String s) {
        if (s == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(SEPARATOR);
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            
            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }    
    /**
     * 下划线风格转换为驼峰命名法
     * @param s 待转换字符串
     * @return 转换后字符
     * @author yinlei
     * date 2013-6-15 下午11:23:52
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线风格转换为首字母大写的驼峰命名法
     * @param s 待转换字符串
     * @return 转换后字符串
     * @author yinlei
     * date 2013-6-15 下午11:26:46
     */
    public static String toCapCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return StringUtils.capitalize(s);
    }

}
