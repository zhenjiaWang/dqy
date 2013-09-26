package com.dqy.util;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjiaWang
 * Date: 2010-9-7
 * Time: 14:13:48
 * To change this template use File | Settings | File Templates.
 */
public class IPEntry {
    public String beginIp;
    public String endIp;
    public String country;
    public String area;

    public IPEntry() {
        beginIp = "";
        endIp = "";
        country = "";
        area = "";
    }

    @Override
    public String toString() {
        return this.area + "  " + this.country + "  IP:" + this.beginIp + "-"
                + this.endIp;
    }
}