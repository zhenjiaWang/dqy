package com.dqy.wf.entity;

import com.dqy.hr.entity.HrUser;
import com.dqy.sys.entity.SysOrg;
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
@Table(name = "WF_REQ")
public class WfReq extends IdEntity implements Tracker {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String subject;

    private SysOrg orgId;

    private HrUser userId;

    private String reqNo;

    private String applyId;

    private Date sendDate;

    private Integer applyState;

    private Integer applyResult;

    private WfReqNodeApprove currentNode;

    private Integer complete;

    private Date completeDate;

    private Integer exigency;

    private Integer nodeCount;
    
    private Integer tip;

    private Date created;

    private String createdBy;

    private Date updated;

    private String updatedBy;

    private String useYn;


    @Id
    @GeneratedValue(generator="WF_REQ")
    @GenericGenerator(name="WF_REQ",strategy="seqhilo",parameters={@Parameter(name="sequence",value="SEQ_WF_REQ")})
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



    @Column(name = "REQ_NO")
    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }




    @Column(name = "SEND_DATE")
    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    @Column(name = "APPLY_STATE")
    public Integer getApplyState() {
        return applyState;
    }

    public void setApplyState(Integer applyState) {
        this.applyState = applyState;
    }

    @Column(name = "APPLY_RESULT")
    public Integer getApplyResult() {
        return applyResult;
    }

    public void setApplyResult(Integer applyResult) {
        this.applyResult = applyResult;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURRENT_NODE")
    public WfReqNodeApprove getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(WfReqNodeApprove currentNode) {
        this.currentNode = currentNode;
    }



    @Column(name = "COMPLETE_DATE")
    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    @Column(name = "NODE_COUNT")
    public Integer getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(Integer nodeCount) {
        this.nodeCount = nodeCount;
    }

    @Column(name = "EXIGENCY")
    public Integer getExigency() {
        return exigency;
    }

    public void setExigency(Integer exigency) {
        this.exigency = exigency;
    }

    @Column(name = "COMPLETE")
    public Integer getComplete() {
        return complete;
    }

    public void setComplete(Integer complete) {
        this.complete = complete;
    }



    @Column(name = "TIP")
    public Integer getTip() {
        return tip;
    }

    public void setTip(Integer tip) {
        this.tip = tip;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORG_ID")
    public SysOrg getOrgId() {
        return orgId;
    }

    public void setOrgId(SysOrg orgId) {
        this.orgId = orgId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    public HrUser getUserId() {
        return userId;
    }

    public void setUserId(HrUser userId) {
        this.userId = userId;
    }

    @Column(name = "APPLY_ID")
    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    @Column(name = "SUBJECT")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
