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
@Table(name = "WF_REQ_MY_FLOW")
public class WfReqMyFlow extends IdEntity implements Tracker {

    private static final long serialVersionUID = 1L;

    private Long id;

    private SysOrg orgId;

    private HrUser userId;

    private String applyId;

    private Date created;

    private String createdBy;

    private Date updated;

    private String updatedBy;

    private String useYn;
    
    private String flowName;

    @Id
    @GeneratedValue(generator="WF_REQ_MY_FLOW")
    @GenericGenerator(name="WF_REQ_MY_FLOW",strategy="seqhilo",parameters={@Parameter(name="sequence",value="SEQ_WF_REQ_MY_FLOW")})
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


    @Column(name = "FLOW_NAME")
    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
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
}
