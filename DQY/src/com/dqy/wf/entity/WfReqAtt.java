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
 * @author zhenjiaWang
 * @version 1.0 2012-05
 * @since JDK1.5
 */
@Entity
@Table(name = "WF_REQ_ATT")
public class WfReqAtt extends IdEntity implements Tracker {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long id;

    private WfReq reqId;

    private String source;

    private String attKey;

    private String oldName;

    private String newName;

    private String postfix;

    private Long attSize;

    private Integer year;

    private Integer month;

    private Integer day;

    private Date created;

    private String createdBy;

    private Date updated;

    private String updatedBy;

    private String useYn;


    @Column(name = "CREATED", updatable = false)
    public Date getCreated() {
        return created;
    }

    @Column(name = "CREATED_BY", updatable = false)
    public String getCreatedBy() {
        return createdBy;
    }

    @Column(name = "UPDATED")
    public Date getUpdated() {
        return updated;
    }

    @Column(name = "UPDATED_BY")
    public String getUpdatedBy() {
        return updatedBy;
    }

    @Column(name = "USE_YN")
    public String getUseYn() {
        return useYn;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    @Id
    @GeneratedValue(generator="WF_REQ_ATT")
    @GenericGenerator(name="WF_REQ_ATT",strategy="seqhilo",parameters={@Parameter(name="sequence",value="SEQ_WF_REQ_ATT")})
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "POSTFIX")
    public String getPostfix() {
        return postfix;
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }

    @Column(name = "OLD_NAME")
    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    @Column(name = "NEW_NAME")
    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    @Column(name = "YEAR")
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Column(name = "MONTH")
    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    @Column(name = "DAY")
    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }



    @Column(name = "SOURCE")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }




    @Column(name = "ATT_KEY")
    public String getAttKey() {
        return attKey;
    }

    public void setAttKey(String attKey) {
        this.attKey = attKey;
    }


    @Column(name = "ATT_SIZE")
    public Long getAttSize() {
        return attSize;
    }

    public void setAttSize(Long attSize) {
        this.attSize = attSize;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REQ_ID")
    public WfReq getReqId() {
        return reqId;
    }

    public void setReqId(WfReq reqId) {
        this.reqId = reqId;
    }
}