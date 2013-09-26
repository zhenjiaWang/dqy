package com.dqy.util;

import com.dqy.common.SeachEntity;
import com.dqy.common.SeachRule;
import org.guiceside.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2010-7-6
 * Time: 15:41:02
 * To change this template use File | Settings | File Templates.
 */
public class SeachRuleUtils {
    public static SeachEntity aa(String filters) {
        //
        if (StringUtils.isNotBlank(filters)) {
            int start = -1;
            int end = -1;
            SeachEntity seachEntity = new SeachEntity();
            start = filters.indexOf(",");
            String groupOpStr = filters.substring(0, start);
            String rulesStr = filters.substring(start + 1);
            if (StringUtils.isNotBlank(groupOpStr)) {
                String[] groupOps = groupOpStr.split(":");
                if (groupOps != null && groupOps.length == 2) {
                    seachEntity.setGroupOp(groupOps[1].replace("\"", "").toUpperCase());
                }
            }
            if (StringUtils.isNotBlank(rulesStr)) {
                start = rulesStr.indexOf("[");
                if (start > 0) {
                    String temp = rulesStr.substring(start);
                    start = temp.indexOf("[");
                    end = temp.indexOf("]");
                    temp = temp.substring(start + 1, end);
                    String[] tempRules = temp.split("},");
                    if (tempRules != null && tempRules.length > 0) {
                        List<SeachRule> seachRules = new ArrayList<SeachRule>();
                        int index = 0;
                        for (String tempRule : tempRules) {
                            if (index < tempRules.length - 1) {
                                tempRule += "}";
                            }
                            start = tempRule.indexOf("{");
                            end = tempRule.indexOf("}");
                            tempRule = tempRule.substring(start + 1, end);
                            String[] ruleDetails = tempRule.split(",");
                            if (ruleDetails != null && ruleDetails.length == 3) {
                                SeachRule seachRule = new SeachRule();
                                String field = ruleDetails[0];
                                String[] keyValues = field.split(":");
                                if (keyValues != null && keyValues.length == 2) {
                                    seachRule.setField(keyValues[1].replace("\"", ""));
                                }
                                String op = ruleDetails[1];
                                keyValues = op.split(":");
                                if (keyValues != null && keyValues.length == 2) {
                                    seachRule.setOp(keyValues[1].replace("\"", "").toUpperCase());
                                }
                                String data = ruleDetails[2];
                                keyValues = data.split(":");
                                if (keyValues != null && keyValues.length == 2) {
                                    seachRule.setData(keyValues[1].replace("\"", ""));
                                }
                                seachRules.add(seachRule);
                            }
                            index++;
                        }
                        seachEntity.setSeachRules(seachRules);
                        return seachEntity;
                    }
                }
            }
        }
        return null;
    }

    public static void main(String[] as) {
        String ss = "{\"groupOp\":\"AND\",\"rules\":[{\"field\":\"id\",\"op\":\"eq\",\"data\":\"12\"},{\"field\":\"id\",\"op\":\"eq\",\"data\":\"21\"},{\"field\":\"languageId\",\"op\":\"eq\",\"data\":\"3\"}]}";
        SeachEntity seachEntity = aa(ss);
        System.out.println(seachEntity.getGroupOp());
    }
}