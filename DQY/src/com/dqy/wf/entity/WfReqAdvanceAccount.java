package com.dqy.wf.entity;

import com.dqy.hr.entity.HrUser;
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
@Table(name = "WF_REQ_ADVANCE_ACCOUNT")
public class WfReqAdvanceAccount extends IdEntity implements Tracker {

    private static final long serialVersionUID = 1L;

    private Long id;

    private WfReq reqId;

    private Double amount;

    private Integer payMethod;

    private String purpose;

    private String remarks;

    private Date created;

    private String createdBy;

    private Date updated;

    private String updatedBy;

    private String useYn;

    @Id
    @GeneratedValue(generator="WF_REQ_ADVANCE_ACCOUNT")
    @GenericGenerator(name="WF_REQ_ADVANCE_ACCOUNT",strategy="seqhilo",parameters={@Parameter(name="sequence",value="SEQ_WF_REQ_ADVANCE_ACCOUNT")})
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

    @Column(name = "PAY_METHOD")
    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    @Column(name = "PURPOSE")
    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @Column(name = "REMARKS")
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
