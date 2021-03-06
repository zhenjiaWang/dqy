package com.dqy.wf.entity;

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
@Table(name = "WF_REQ_NO_SEQ")
public class WfReqNoSeq extends IdEntity implements Tracker {

    private static final long serialVersionUID = 1L;

    private Long id;

    private SysOrg orgId;

    private String applyId;

    private Integer dateYear;

    private Integer dateMonth;

    private Integer nextSeq;

    private Date created;

    private String createdBy;

    private Date updated;

    private String updatedBy;

    private String useYn;

    @Id
    @GeneratedValue(generator="WF_REQ_NO_SEQ")
    @GenericGenerator(name="WF_REQ_NO_SEQ",strategy="seqhilo",parameters={@Parameter(name="sequence",value="SEQ_WF_REQ_NO_SEQ")})
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

    @Column(name = "APPLY_ID")
    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    @Column(name = "DATE_YEAR")
    public Integer getDateYear() {
        return dateYear;
    }

    public void setDateYear(Integer dateYear) {
        this.dateYear = dateYear;
    }

    @Column(name = "NEXT_SEQ")
    public Integer getNextSeq() {
        return nextSeq;
    }

    public void setNextSeq(Integer nextSeq) {
        this.nextSeq = nextSeq;
    }

    @Column(name = "DATE_MONTH")
    public Integer getDateMonth() {
        return dateMonth;
    }

    public void setDateMonth(Integer dateMonth) {
        this.dateMonth = dateMonth;
    }
}
