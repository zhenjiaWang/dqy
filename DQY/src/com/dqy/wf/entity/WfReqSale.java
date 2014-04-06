package com.dqy.wf.entity;

import com.dqy.sale.entity.*;
import com.dqy.sys.entity.SysBudgetTitle;
import com.dqy.sys.entity.SysBudgetType;
import org.guiceside.persistence.entity.IdEntity;
import org.guiceside.persistence.entity.Tracker;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 12-8-15
 * Time: 下午4:26
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "WF_REQ_SALE")
public class WfReqSale extends IdEntity implements Tracker {

    private static final long serialVersionUID = 1L;

    private Long id;

    private WfReq reqId;

    private Double amount;

    private SysBudgetType expenseType;

    private SysBudgetTitle expenseTitle;

    private SaleChannel channelId;

    private SaleDept deptId;

    private SaleSystem systemId;

    private SaleCustomer customerId;

    private SaleSeries seriesId;

    private SaleProduct productId;

    private Integer budgetYear;

    private Date startDate;

    private Date endDate;

    private Date payDate;

    private Date created;

    private String createdBy;

    private Date updated;

    private String updatedBy;

    private String useYn;


    @Id
    @GeneratedValue(generator="WF_REQ_SALE")
    @GenericGenerator(name="WF_REQ_SALE",strategy="seqhilo",parameters={@Parameter(name="sequence",value="SEQ_WF_REQ_SALE")})
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "CREATED",updatable = false)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Column(name = "CREATED_BY")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "UPDATED")
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Column(name = "UPDATED_BY")
    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Column(name = "USE_YN")
    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REQ_ID")
    public WfReq getReqId() {
        return reqId;
    }

    public void setReqId(WfReq reqId) {
        this.reqId = reqId;
    }

    @Column(name = "AMOUNT")
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXPENSE_TYPE")
    public SysBudgetType getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(SysBudgetType expenseType) {
        this.expenseType = expenseType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXPENSE_TITLE")
    public SysBudgetTitle getExpenseTitle() {
        return expenseTitle;
    }

    public void setExpenseTitle(SysBudgetTitle expenseTitle) {
        this.expenseTitle = expenseTitle;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHANNEL_ID")
    public SaleChannel getChannelId() {
        return channelId;
    }

    public void setChannelId(SaleChannel channelId) {
        this.channelId = channelId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPT_ID")
    public SaleDept getDeptId() {
        return deptId;
    }

    public void setDeptId(SaleDept deptId) {
        this.deptId = deptId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SYSTEM_ID")
    public SaleSystem getSystemId() {
        return systemId;
    }

    public void setSystemId(SaleSystem systemId) {
        this.systemId = systemId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    public SaleCustomer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(SaleCustomer customerId) {
        this.customerId = customerId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERIES_ID")
    public SaleSeries getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(SaleSeries seriesId) {
        this.seriesId = seriesId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    public SaleProduct getProductId() {
        return productId;
    }

    public void setProductId(SaleProduct productId) {
        this.productId = productId;
    }

    @Column(name = "BUDGET_YEAR")
    public Integer getBudgetYear() {
        return budgetYear;
    }

    public void setBudgetYear(Integer budgetYear) {
        this.budgetYear = budgetYear;
    }

    @Column(name = "START_DATE")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Column(name = "END_DATE")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(name = "PAY_DATE")
    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
}
