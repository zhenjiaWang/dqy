package com.dqy.common;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjia
 * Date: 2010-7-6
 * Time: 15:37:46
 * To change this template use File | Settings | File Templates.
 */
public class SeachEntity {
    private String groupOp;

    private List<SeachRule> seachRules;

    public String getGroupOp() {
        return groupOp;
    }

    public void setGroupOp(String groupOp) {
        this.groupOp = groupOp;
    }

    public List<SeachRule> getSeachRules() {
        return seachRules;
    }

    public void setSeachRules(List<SeachRule> seachRules) {
        this.seachRules = seachRules;
    }
}