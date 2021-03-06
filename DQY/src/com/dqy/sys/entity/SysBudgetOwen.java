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
@Table(name = "SYS_BUDGET_OWEN")
public class SysBudgetOwen extends IdEntity implements Tracker {

    private static final long serialVersionUID = 1L;

    private Long id;

    private SysOrg orgId;

    private SysBudgetTitle budgetTitle;

    private SysFinancialTitle titleId;

    private Date created;

    private String createdBy;

    private Date updated;

    private String updatedBy;

    private String useYn;

    @Id
    @GeneratedValue(generator="SYS_BUDGET_OWEN")
    @GenericGenerator(name="SYS_BUDGET_OWEN",strategy="seqhilo",parameters={@Parameter(name="sequence",value="SEQ_SYS_BUDGET_OWEN")})
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
    @JoinColumn(name = "ORG_ID")
    public SysOrg getOrgId() {
        return orgId;
    }

    public void setOrgId(SysOrg orgId) {
        this.orgId = orgId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUDGET_TITLE")
    public SysBudgetTitle getBudgetTitle() {
        return budgetTitle;
    }

    public void setBudgetTitle(SysBudgetTitle budgetTitle) {
        this.budgetTitle = budgetTitle;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TITLE_ID")
    public SysFinancialTitle getTitleId() {
        return titleId;
    }

    public void setTitleId(SysFinancialTitle titleId) {
        this.titleId = titleId;
    }
}
