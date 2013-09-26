package com.dqy.util;

import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.web.jsp.taglib.BaseTag;

import java.sql.Clob;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: LawrenceQi
 * Date: 2009-4-21
 * Time: 17:06:17
 * To change this template use File | Settings | File Templates.
 */
public class ClobOutput extends BaseTag {
    public static String getClobValueToString(Object entity, String value) throws Exception {
        StringBuilder sb = new StringBuilder();
        Clob clob = (Clob) BeanUtils.getValue(entity, value);
        if (clob != null) {
            try {
                sb.append(clob.getSubString(1, (int) clob.length()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}