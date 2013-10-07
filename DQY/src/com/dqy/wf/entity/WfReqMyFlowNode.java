package com.dqy.wf.entity;

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
@Table(name = "WF_REQ_MY_FLOW_NODE")
public class WfReqMyFlowNode extends IdEntity implements Tracker {

    private static final long serialVersionUID = 1L;

    private Long id;

    private WfReqMyFlow flowId;

    private Integer nodeType;

    private Integer nodeSeq;

    private Date created;

    private String createdBy;

    private Date updated;

    private String updatedBy;

    private String useYn;

    @Id
    @GeneratedValue(generator="WF_REQ_MY_FLOW_NODE")
    @GenericGenerator(name="WF_REQ_MY_FLOW_NODE",strategy="seqhilo",parameters={@Parameter(name="sequence",value="SEQ_WF_REQ_MY_FLOW_NODE")})
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
    @JoinColumn(name = "FLOW_ID")
    public WfReqMyFlow getFlowId() {
        return flowId;
    }

    public void setFlowId(WfReqMyFlow flowId) {
        this.flowId = flowId;
    }

    @Column(name = "NODE_TYPE")
    public Integer getNodeType() {
        return nodeType;
    }

    public void setNodeType(Integer nodeType) {
        this.nodeType = nodeType;
    }



    @Column(name = "NODE_SEQ")
    public Integer getNodeSeq() {
        return nodeSeq;
    }

    public void setNodeSeq(Integer nodeSeq) {
        this.nodeSeq = nodeSeq;
    }
}
