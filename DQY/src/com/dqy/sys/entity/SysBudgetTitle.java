package com.dqy.sys.entity;

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
@Table(name = "SYS_BUDGET_TITLE")
public class SysBudgetTitle extends IdEntity implements Tracker {

    private static final long serialVersionUID = 1L;

    private Long id;

    private SysOrg orgId;

    private String titleNo;

    private String titlePy;

    private String titleName;

    private Date created;

    private String createdBy;

    private Date updated;

    private String updatedBy;

    private String useYn;

    private SysBudgetOwen sysBudgetOwen;
    @Id
    @GeneratedValue(generator="SYS_BUDGET_TITLE")
    @GenericGenerator(name="SYS_BUDGET_TITLE",strategy="seqhilo",parameters={@Parameter(name="sequence",value="SEQ_SYS_BUDGET_TITLE")})
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

    @Column(name = "TITLE_NAME")
    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    @Column(name = "TITLE_NO")
    public String getTitleNo() {
        return titleNo;
    }

    public void setTitleNo(String titleNo) {
        this.titleNo = titleNo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORG_ID")
    public SysOrg getOrgId() {
        return orgId;
    }

    public void setOrgId(SysOrg orgId) {
        this.orgId = orgId;
    }

    @Column(name = "TITLE_PY")
    public String getTitlePy() {
        return titlePy;
    }

    public void setTitlePy(String titlePy) {
        this.titlePy = titlePy;
    }

    @Transient
    public SysBudgetOwen getSysBudgetOwen() {
        return sysBudgetOwen;
    }

    public void setSysBudgetOwen(SysBudgetOwen sysBudgetOwen) {
        this.sysBudgetOwen = sysBudgetOwen;
    }
}
