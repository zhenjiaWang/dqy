package com.dqy.util;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2009-9-14
 * Time: 16:04:28
 * To change this template use File | Settings | File Templates.
 */
public class MakeZip {

    public void unZip(String compress, String decompression) throws Exception {
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec("C:\\Program Files\\WinRAR\\UNRAR.EXE x -o+ -p- " + compress + " " + decompression);
        StringBuffer sb = new StringBuffer();
        java.io.InputStream fis = p.getInputStream();
        int value = 0;
        while ((value = fis.read()) != -1) {
            sb.append((char) value);
        }
        fis.close();
        String result = new String(sb.toString().getBytes("ISO-8859-1"), "GBK");
        System.out.println(result);
    }


    public void zip(String outputRar, String compression) throws Exception {
        Runtime rt = Runtime.getRuntime();
        String exeStr = "rar.exe a  -r -s -ibck " + outputRar + " " + compression;
        Process p = rt.exec(exeStr + outputRar + " " + compression);
        StringBuffer sb = new StringBuffer();
        java.io.InputStream fis = p.getInputStream();
        int value = 0;
        while ((value = fis.read()) != -1) {
            sb.append((char) value);
        }
        fis.close();
        String result = new String(sb.toString().getBytes("ISO-8859-1"), "GBK");
        System.out.println(result);
    }


}