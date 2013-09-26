package com.dqy.util;

/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 2008-10-30
 * @since JDK1.5
 */
public class PasswordUtils {
    private static final String author = "wangzhenjia";

    private PasswordUtils() {

    }

    public static char[] getLI(String password) {
        char[] li_3 = null;
        char[] authorChars = author.toCharArray();
        if (password.length() <= authorChars.length) {
            int index = 0;
            li_3 = new char[password.length()];
            for (char c : authorChars) {
                if (index == password.length()) {
                    break;
                }
                li_3[index] = c;
                index++;
            }
        } else if (password.length() > authorChars.length) {
            int basic = 0;
            int diff = 0;
            if (password.length() % authorChars.length == 0) {
                basic = password.length() / authorChars.length - 1;
            } else {
                if (password.length() / authorChars.length == 1) {
                    diff = password.length() % authorChars.length;
                } else {
                    basic = password.length() / authorChars.length - 1;
                    diff = password.length() % authorChars.length;
                }
            }
            li_3 = new char[password.length()];
            int index = 0;
            if (basic > 0) {
                for (int i = 0; i <= basic; i++) {
                    for (char c : authorChars) {
                        li_3[index] = c;
                        index++;
                    }
                }
            }
            if (diff > 0) {
                for (int i = 0; i < diff; i++) {
                    li_3[index] = authorChars[i];
                    index++;
                }
            }
        }
        return li_3;
    }

    public static String decodePasswd(String password) throws Exception {
        char[] li_3 = getLI(password);
        String sDecode = "";
        char[] lc_2 = new char[password.length()];
        char[] charTemp = password.toCharArray();
        for (int i = 0; i < charTemp.length; i++) {
            if (i == 0 || i == charTemp.length - 1) {
                charTemp[i] = (char) ((int) charTemp[i] - 1);
            } else {
                charTemp[i] = (char) ((int) charTemp[i] + li_3[i]);
            }
            lc_2[i] = charTemp[i];
        }
        sDecode = new String(lc_2);
        return sDecode.trim();
    }

    public static String encodePasswd(String password) throws Exception {
        char[] li_3 = getLI(password);
        String sEncode = "";
        char[] lc_2 = new char[password.length()];
        char[] charTemp = password.toCharArray();
        for (int i = 0; i < charTemp.length; i++) {
            if (i == 0 || i == charTemp.length - 1) {
                charTemp[i] = (char) ((int) charTemp[i] + 1);
            } else {
                charTemp[i] = (char) ((int) charTemp[i] - li_3[i]);
            }
            lc_2[i] = charTemp[i];
        }
        sEncode = new String(lc_2);
        return sEncode.trim();
    }

    public static String toString(String password) {
        String sEncode = "";
        char[] lc_2 = new char[password.length()];
        char[] charTemp = password.toCharArray();
        for (int i = 0; i < charTemp.length; i++) {
            lc_2[i] = '*';
        }
        sEncode = new String(lc_2);
        return sEncode.trim();
    }
}