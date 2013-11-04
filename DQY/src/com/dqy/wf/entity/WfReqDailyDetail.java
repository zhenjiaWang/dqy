package com.dqy.wf.entity;

import com.dqy.hr.entity.HrDepartment;
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
@Table(name = "WF_REQ_DAILY_DETAIL")
public class WfReqDailyDetail extends IdEntity implements Tracker {

    private static final long serialVersionUID = 1L;

    private Long id;

    private WfReqDaily dailyId;

    private SysBudgetType expenseType;

    private SysBudgetTitle expenseTitle;

    private HrDepartment expenseDept;

    private Date amountDate;

    private Double amount;

    private String remarks;

    private Date created;

    private String createdBy;

    private Date updated;

    private String updatedBy;

    private String useYn;

    @Id
    @GeneratedValue(generator="WF_REQ_DAILY_DETAIL")
    @GenericGenerator(name="WF_REQ_DAILY_DETAIL",strategy="seqhilo",parameters={@Parameter(name="sequence",value="SEQ_WF_REQ_DAILY_DETAIL")})
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




    @Column(name = "AMOUNT")
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }


    @Column(name = "REMARKS")
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DAILY_ID")
    public WfReqDaily getDailyId() {
        return dailyId;
    }

    public void setDailyId(WfReqDaily dailyId) {
        this.dailyId = dailyId;
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

    @Column(name = "AMOUNT_DATE")
    public Date getAmountDate() {
        return amountDate;
    }

    public void setAmountDate(Date amountDate) {
        this.amountDate = amountDate;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXPENSE_DEPT")
    public HrDepartment getExpenseDept() {
        return expenseDept;
    }

    public void setExpenseDept(HrDepartment expenseDept) {
        this.expenseDept = expenseDept;
    }
}
