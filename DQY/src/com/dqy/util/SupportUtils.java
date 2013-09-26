package com.dqy.util;

import org.guiceside.commons.lang.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjiaWang
 * Date: 12-6-14
 * Time: 上午11:29
 * To change this template use File | Settings | File Templates.
 */
public class SupportUtils {
    public static String getTreeNo(Integer no) {
        if (no < 10) {
            return "00" + no;
        } else if (no < 100) {
            return "0" + no;
        } else {
            return no + "";
        }
    }

    public static String getParentTreeNo(String no) {
        if (StringUtils.isNotBlank(no)) {
            int size = no.length();
            return no.substring(0, size - 3);
        }
        return null;
    }

    public static Integer getNo(String no) {
        if (StringUtils.isNotBlank(no)) {
            int size = no.length();
            String lastNo = no.substring(size - 3);
            return Integer.valueOf(lastNo);
        }
        return null;
    }

    public static int getLevel(String no) {
        if (StringUtils.isNotBlank(no)) {
            int size = no.length();
            return size / 3;
        }
        return 0;
    }

    public static String getParentTreeNoByLevel(String no,int level) {
        if (StringUtils.isNotBlank(no)) {
            int tmpLv=SupportUtils.getLevel(no);
            int diffLv=tmpLv-level;
            while (tmpLv>0&&tmpLv!=diffLv){
                no=SupportUtils.getParentTreeNo(no);
                tmpLv=SupportUtils.getLevel(no);
            }
            return no;
        }
        return null;
    }

}