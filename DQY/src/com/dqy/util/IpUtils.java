package com.dqy.util;

import java.io.UnsupportedEncodingException;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjiaWang
 * Date: 2010-9-7
 * Time: 14:15:26
 * To change this template use File | Settings | File Templates.
 */
public class IpUtils {

    public static byte[] getIpByteArrayFromString(String ip) {
        byte[] ret = new byte[4];
        java.util.StringTokenizer st = new java.util.StringTokenizer(ip, ".");
        try {
            ret[0] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
            ret[1] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
            ret[2] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
            ret[3] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }

    /**
     * ��ԭʼ�ַ���б���ת�������ʧ�ܣ�����ԭʼ���ַ�
     *
     * @param s            ԭʼ�ַ�
     * @param srcEncoding  Դ���뷽ʽ
     * @param destEncoding Ŀ����뷽ʽ
     * @return ת���������ַ�ʧ�ܷ���ԭʼ�ַ�
     */
    public static String getString(String s, String srcEncoding,
                                   String destEncoding) {
        try {
            return new String(s.getBytes(srcEncoding), destEncoding);
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }

    /**
     * ���ĳ�ֱ��뷽ʽ���ֽ�����ת�����ַ�
     *
     * @param b        �ֽ�����
     * @param encoding ���뷽ʽ
     * @return ���encoding��֧�֣�����һ��ȱʡ������ַ�
     */
    public static String getString(byte[] b, String encoding) {
        try {
            return new String(b, encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(b);
        }
    }

    /**
     * ���ĳ�ֱ��뷽ʽ���ֽ�����ת�����ַ�
     *
     * @param b        �ֽ�����
     * @param offset   Ҫת������ʼλ��
     * @param len      Ҫת���ĳ���
     * @param encoding ���뷽ʽ
     * @return ���encoding��֧�֣�����һ��ȱʡ������ַ�
     */
    public static String getString(byte[] b, int offset, int len,
                                   String encoding) {
        try {
            return new String(b, offset, len, encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(b, offset, len);
        }
    }

    /**
     * @param ip ip���ֽ�������ʽ
     * @return �ַ���ʽ��ip
     */
    public static String getIpStringFromBytes(byte[] ip) {
        StringBuffer sb = new StringBuffer();
        sb.append(ip[0] & 0xFF);
        sb.append('.');
        sb.append(ip[1] & 0xFF);
        sb.append('.');
        sb.append(ip[2] & 0xFF);
        sb.append('.');
        sb.append(ip[3] & 0xFF);
        return sb.toString();
    }
}
