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

    private Double monthAmount1;

    private Double monthAmount2;

    private Double monthAmount3;

    private Double monthAmount4;

    private Double monthAmount5;

    private Double monthAmount6;

    private Double monthAmount7;

    private Double monthAmount8;

    private Double monthAmount9;

    private Double monthAmount10;

    private Double monthAmount11;

    private Double monthAmount12;

    private Double yearAmount;

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

    public Double getMonthAmount1() {
        return monthAmount1;
    }

    public void setMonthAmount1(Double monthAmount1) {
        this.monthAmount1 = monthAmount1;
    }

    public Double getMonthAmount2() {
        return monthAmount2;
    }

    public void setMonthAmount2(Double monthAmount2) {
        this.monthAmount2 = monthAmount2;
    }

    public Double getMonthAmount3() {
        return monthAmount3;
    }

    public void setMonthAmount3(Double monthAmount3) {
        this.monthAmount3 = monthAmount3;
    }

    public Double getMonthAmount4() {
        return monthAmount4;
    }

    public void setMonthAmount4(Double monthAmount4) {
        this.monthAmount4 = monthAmount4;
    }

    public Double getMonthAmount5() {
        return monthAmount5;
    }

    public void setMonthAmount5(Double monthAmount5) {
        this.monthAmount5 = monthAmount5;
    }

    public Double getMonthAmount6() {
        return monthAmount6;
    }

    public void setMonthAmount6(Double monthAmount6) {
        this.monthAmount6 = monthAmount6;
    }

    public Double getMonthAmount7() {
        return monthAmount7;
    }

    public void setMonthAmount7(Double monthAmount7) {
        this.monthAmount7 = monthAmount7;
    }

    public Double getMonthAmount8() {
        return monthAmount8;
    }

    public void setMonthAmount8(Double monthAmount8) {
        this.monthAmount8 = monthAmount8;
    }

    public Double getMonthAmount9() {
        return monthAmount9;
    }

    public void setMonthAmount9(Double monthAmount9) {
        this.monthAmount9 = monthAmount9;
    }

    public Double getMonthAmount10() {
        return monthAmount10;
    }

    public void setMonthAmount10(Double monthAmount10) {
        this.monthAmount10 = monthAmount10;
    }

    public Double getMonthAmount11() {
        return monthAmount11;
    }

    public void setMonthAmount11(Double monthAmount11) {
        this.monthAmount11 = monthAmount11;
    }

    public Double getMonthAmount12() {
        return monthAmount12;
    }

    public void setMonthAmount12(Double monthAmount12) {
        this.monthAmount12 = monthAmount12;
    }

    public Double getYearAmount() {
        return yearAmount;
    }

    public void setYearAmount(Double yearAmount) {
        this.yearAmount = yearAmount;
    }
}
