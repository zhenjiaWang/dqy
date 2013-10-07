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
@Table(name = "WF_REQ_TASK")
public class WfReqTask extends IdEntity implements Tracker {

    private static final long serialVersionUID = 1L;

    private Long id;

    private WfReq reqId;

    private Integer nodeSeq;

    private HrUser userId;
    
    private SysOrg orgId;

    private Integer taskState;

    private Integer taskRead;

    private Integer approveIdea;

    private Date receiveDate;

    private WfReqNodeApprove nodeId;

    private Date created;

    private String createdBy;

    private Date updated;

    private String updatedBy;

    private String useYn;

    @Id
    @GeneratedValue(generator="WF_REQ_TASK")
    @GenericGenerator(name="WF_REQ_TASK",strategy="seqhilo",parameters={@Parameter(name="sequence",value="SEQ_WF_REQ_TASK")})
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


    @Column(name = "NODE_SEQ")
    public Integer getNodeSeq() {
        return nodeSeq;
    }

    public void setNodeSeq(Integer nodeSeq) {
        this.nodeSeq = nodeSeq;
    }



    @Column(name = "TASK_STATE")
    public Integer getTaskState() {
        return taskState;
    }

    public void setTaskState(Integer taskState) {
        this.taskState = taskState;
    }

    @Column(name = "TASK_READ")
    public Integer getTaskRead() {
        return taskRead;
    }

    public void setTaskRead(Integer taskRead) {
        this.taskRead = taskRead;
    }

    @Column(name = "APPROVE_IDEA")
    public Integer getApproveIdea() {
        return approveIdea;
    }

    public void setApproveIdea(Integer approveIdea) {
        this.approveIdea = approveIdea;
    }

    @Column(name = "RECEIVE_DATE")
    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NODE_ID")
    public WfReqNodeApprove getNodeId() {
        return nodeId;
    }

    public void setNodeId(WfReqNodeApprove nodeId) {
        this.nodeId = nodeId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    public HrUser getUserId() {
        return userId;
    }

    public void setUserId(HrUser userId) {
        this.userId = userId;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORG_ID")
    public SysOrg getOrgId() {
        return orgId;
    }

    public void setOrgId(SysOrg orgId) {
        this.orgId = orgId;
    }
}
