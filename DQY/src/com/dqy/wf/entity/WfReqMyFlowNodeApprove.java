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
@Table(name = "WF_REQ_MY_FLOW_NODE_APPROVE")
public class WfReqMyFlowNodeApprove extends IdEntity implements Tracker {

    private static final long serialVersionUID = 1L;

    private Long id;

    private WfReqMyFlowNode nodeId;

    private Integer approveType;

    private HrUser userId;

    private WfVariableGlobal globalId;

    private HrUser deptUserId;

    private Date created;

    private String createdBy;

    private Date updated;

    private String updatedBy;

    private String useYn;

    @Id
    @GeneratedValue(generator="WF_REQ_MY_FLOW_NODE_APPROVE")
    @GenericGenerator(name="WF_REQ_MY_FLOW_NODE_APPROVE",strategy="seqhilo",parameters={@Parameter(name="sequence",value="SEQ_WF_REQ_MY_FLOW_NODE_APP")})
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
    @JoinColumn(name = "NODE_ID")
    public WfReqMyFlowNode getNodeId() {
        return nodeId;
    }

    public void setNodeId(WfReqMyFlowNode nodeId) {
        this.nodeId = nodeId;
    }



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GLOBAL_ID")
    public WfVariableGlobal getGlobalId() {
        return globalId;
    }

    public void setGlobalId(WfVariableGlobal globalId) {
        this.globalId = globalId;
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
    @JoinColumn(name = "DEPT_USER_ID")
    public HrUser getDeptUserId() {
        return deptUserId;
    }

    public void setDeptUserId(HrUser deptUserId) {
        this.deptUserId = deptUserId;
    }

    @Column(name = "APPROVE_TYPE")
    public Integer getApproveType() {
        return approveType;
    }

    public void setApproveType(Integer approveType) {
        this.approveType = approveType;
    }
}
