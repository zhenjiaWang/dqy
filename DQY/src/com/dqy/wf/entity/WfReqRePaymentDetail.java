package com.dqy.wf.entity;

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
@Table(name = "WF_REQ_REPAYMENT_DETAIL")
public class WfReqRePaymentDetail extends IdEntity implements Tracker {

    private static final long serialVersionUID = 1L;

    private Long id;

    private WfReqRePayment rePaymentId;

    private SysBudgetType expenseType;

    private SysBudgetTitle expenseTitle;

    private Double amount;

    private String remarks;

    private Date created;

    private String createdBy;

    private Date updated;

    private String updatedBy;

    private String useYn;

    @Id
    @GeneratedValue(generator="WF_REQ_REPAYMENT_DETAIL")
    @GenericGenerator(name="WF_REQ_REPAYMENT_DETAIL",strategy="seqhilo",parameters={@Parameter(name="sequence",value="SEQ_WF_REQ_REPAYMENT_DETAIL")})
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
    @JoinColumn(name = "REPAYMENT_ID")
    public WfReqRePayment getRePaymentId() {
        return rePaymentId;
    }

    public void setRePaymentId(WfReqRePayment rePaymentId) {
        this.rePaymentId = rePaymentId;
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
}
