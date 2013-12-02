package com.dqy.common.entity;

import com.dqy.hr.entity.HrDepartment;
import com.dqy.sys.entity.SysBudgetType;
import com.dqy.sys.entity.SysFinancialTitle;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjiaWang
 * Date: 13-7-31
 * Time: 下午7:36
 * To change this template use File | Settings | File Templates.
 */
public class TempBudgetAmount implements Serializable {

    private HrDepartment hrDepartment;

    private SysBudgetType sysBudgetType;

    private SysFinancialTitle sysFinancialTitle;

    private Integer monthAmount1;

    private Integer monthAmount2;

    private Integer monthAmount3;

    private Integer monthAmount4;

    private Integer monthAmount5;

    private Integer monthAmount6;

    private Integer monthAmount7;

    private Integer monthAmount8;

    private Integer monthAmount9;

    private Integer monthAmount10;

    private Integer monthAmount11;

    private Integer monthAmount12;

    public HrDepartment getHrDepartment() {
        return hrDepartment;
    }

    public void setHrDepartment(HrDepartment hrDepartment) {
        this.hrDepartment = hrDepartment;
    }

    public SysBudgetType getSysBudgetType() {
        return sysBudgetType;
    }

    public void setSysBudgetType(SysBudgetType sysBudgetType) {
        this.sysBudgetType = sysBudgetType;
    }

    public SysFinancialTitle getSysFinancialTitle() {
        return sysFinancialTitle;
    }

    public void setSysFinancialTitle(SysFinancialTitle sysFinancialTitle) {
        this.sysFinancialTitle = sysFinancialTitle;
    }

    public Integer getMonthAmount1() {
        return monthAmount1;
    }

    public void setMonthAmount1(Integer monthAmount1) {
        this.monthAmount1 = monthAmount1;
    }

    public Integer getMonthAmount2() {
        return monthAmount2;
    }

    public void setMonthAmount2(Integer monthAmount2) {
        this.monthAmount2 = monthAmount2;
    }

    public Integer getMonthAmount3() {
        return monthAmount3;
    }

    public void setMonthAmount3(Integer monthAmount3) {
        this.monthAmount3 = monthAmount3;
    }

    public Integer getMonthAmount4() {
        return monthAmount4;
    }

    public void setMonthAmount4(Integer monthAmount4) {
        this.monthAmount4 = monthAmount4;
    }

    public Integer getMonthAmount5() {
        return monthAmount5;
    }

    public void setMonthAmount5(Integer monthAmount5) {
        this.monthAmount5 = monthAmount5;
    }

    public Integer getMonthAmount6() {
        return monthAmount6;
    }

    public void setMonthAmount6(Integer monthAmount6) {
        this.monthAmount6 = monthAmount6;
    }

    public Integer getMonthAmount7() {
        return monthAmount7;
    }

    public void setMonthAmount7(Integer monthAmount7) {
        this.monthAmount7 = monthAmount7;
    }

    public Integer getMonthAmount8() {
        return monthAmount8;
    }

    public void setMonthAmount8(Integer monthAmount8) {
        this.monthAmount8 = monthAmount8;
    }

    public Integer getMonthAmount9() {
        return monthAmount9;
    }

    public void setMonthAmount9(Integer monthAmount9) {
        this.monthAmount9 = monthAmount9;
    }

    public Integer getMonthAmount10() {
        return monthAmount10;
    }

    public void setMonthAmount10(Integer monthAmount10) {
        this.monthAmount10 = monthAmount10;
    }

    public Integer getMonthAmount11() {
        return monthAmount11;
    }

    public void setMonthAmount11(Integer monthAmount11) {
        this.monthAmount11 = monthAmount11;
    }

    public Integer getMonthAmount12() {
        return monthAmount12;
    }

    public void setMonthAmount12(Integer monthAmount12) {
        this.monthAmount12 = monthAmount12;
    }
}
