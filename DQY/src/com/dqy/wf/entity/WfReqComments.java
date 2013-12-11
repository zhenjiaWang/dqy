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
@Table(name = "WF_REQ_COMMENTS")
public class WfReqComments extends IdEntity implements Tracker {

    private static final long serialVersionUID = 1L;

    private Long id;

    private HrUser userId;

    private WfReq reqId;

    private Integer action;
    


    private Integer approve;

    private String content;

    private Date created;

    private String createdBy;

    private Date updated;

    private String updatedBy;

    private String useYn;

    private String actionDesc;

    @Id
    @GeneratedValue(generator="WF_REQ_COMMENTS")
    @GenericGenerator(name="WF_REQ_COMMENTS",strategy="seqhilo",parameters={@Parameter(name="sequence",value="SEQ_WF_REQ_COMMENTS")})
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

    @Column(name = "ACTION")
    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }


    @Column(name = "APPROVE")
    public Integer getApprove() {
        return approve;
    }

    public void setApprove(Integer approve) {
        this.approve = approve;
    }

    @Column(name = "CONTENT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    public HrUser getUserId() {
        return userId;
    }

    public void setUserId(HrUser userId) {
        this.userId = userId;
    }

    @Transient
    public String getActionDesc() {
        if (getAction() == null) {
            return "&nbsp;";
        }
        String actionDesc = null;
        if (getAction().intValue() == 0) {
            actionDesc = "起草申请";
        } else if (getAction().intValue() == 1) {
            actionDesc = "启动流程";
        } else if (getAction().intValue() == 2) {
            actionDesc = "接收申请";
        } else if (getAction().intValue() == 3) {
            actionDesc = "查看申请";
        } else if (getAction().intValue() == 4) {
            actionDesc = "批准申请";
        } else if (getAction().intValue() == 5) {
            actionDesc = "否决申请";
        } else if (getAction().intValue() == 6) {
            actionDesc = "确认申请";
        } else if (getAction().intValue() == 7) {
            actionDesc = "流程结束";
        } else if (getAction().intValue() == 8) {
            actionDesc = "委托待审";
        } else if (getAction().intValue() == 9) {
            actionDesc = "回退流程";
        } else if (getAction().intValue() == 10) {
            actionDesc = "执行人接收";
        }else if (getAction().intValue() == 11) {
            actionDesc = "执行人查看";
        }else if (getAction().intValue() == 12) {
            actionDesc = "执行人处理";
        }else if (getAction().intValue() == 13) {
            actionDesc = "申请人撤销";
        }
        return actionDesc;
    }

    public void setActionDesc(String actionDesc) {
        this.actionDesc = actionDesc;
    }
}
