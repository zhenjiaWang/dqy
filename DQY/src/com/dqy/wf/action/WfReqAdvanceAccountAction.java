package com.dqy.wf.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.common.entity.TempAtt;
import com.dqy.common.service.TempAttService;
import com.dqy.sys.entity.SysOrg;
import com.dqy.sys.service.SysOrgService;
import com.dqy.web.support.ActionSupport;
import com.dqy.wf.entity.*;
import com.dqy.wf.service.*;
import com.google.inject.Inject;
import net.sf.json.JSONObject;
import org.guiceside.commons.lang.DateFormatUtil;
import org.guiceside.commons.lang.NumberUtils;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.persistence.entity.search.SelectorUtils;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;
import org.guiceside.support.file.FileManager;
import org.guiceside.web.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjiaWang
 * Date: 12-7-12
 * Time: 下午9:49
 * To change this template use File | Settings | File Templates.
 */
@Action(name = "advanceAccount", namespace = "/wf")
public class WfReqAdvanceAccountAction extends WfReqSupportAction<WfReqAdvanceAccount> {

    @Inject
    private WfVariableGlobalService wfVariableGlobalService;

    @Inject
    private WfReqExecuteService wfReqExecuteService;

    @Inject
    private WfReqService wfReqService;

    @Inject
    private WfReqAdvanceAccountService wfReqAdvanceAccountService;

    @Inject
    private WfReqCommentsService wfReqCommentsService;

    @Inject
    private SysOrgService sysOrgService;

    @Inject
    private WfReqTaskService wfReqTaskService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private WfReqAdvanceAccount wfReqAdvanceAccount;

    @Inject
    private WfReqAttService wfReqAttService;

    @Inject
    private TempAttService tempAttService;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private Long reqId;

    @ReqGet
    @ReqSet
    private Long taskId;

    @ReqGet
    @ReqSet
    private Long executeId;

    @ReqSet
    public final String applyId="ADVANCE_ACCOUNT";



    @ReqSet
    private WfReqTask wfReqTask;

    @ReqSet
    private WfReqExecute wfReqExecute;

    @ReqSet
    private List<WfReqComments> reqCommentsList;


    @ReqSet
    private Date sendDate;


    @ReqSet
    private String applyName;


    @ReqGet
    @ReqSet
    private String attToken;


    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/wf/advanceAccount/input.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("apply");
            userInfo.setLeftMenu("advanceAccount");
            userInfo.setChildMenu(null);
            sendDate= DateFormatUtil.getCurrentDate(true);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/wf/req!ingList.dhtml", type = Dispatcher.Redirect)})
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null&&wfReqAdvanceAccount!=null) {
            initReq();
            wfReqAdvanceAccount.setReqId(wfReq);
            wfReqAdvanceAccount.setUseYn("Y");
            wfReqAdvanceAccount.setRePayYn("N");
            bind(wfReqAdvanceAccount);
            wfReqAdvanceAccount.setAmount(NumberUtils.multiply(wfReqAdvanceAccount.getAmount(), 1, 2));
            this.wfReqAdvanceAccountService.save(wfReqAdvanceAccount,wfReq,  wfReqCommentsList, wfReqNoSeq, reqNodeApproveList, reqTaskList, wfReqMyFlowLast,reqAttList);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }
    @PageFlow(result = {@Result(name = "success", path = "/view/wf/advanceAccount/print.ftl", type = Dispatcher.FreeMarker)})
    public String print() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && reqId != null) {
            wfReqAdvanceAccount=this.wfReqAdvanceAccountService.getByReqId(reqId);
            if(wfReqAdvanceAccount!=null){
                wfReq = wfReqAdvanceAccount.getReqId();
                if (wfReq != null) {
                    reqCommentsList = this.wfReqCommentsService.getCommentsListByReqId(wfReq.getId());
                }
                if(StringUtils.isNotBlank(applyId)){
                    if(applyId.equals("ADVANCE_ACCOUNT")){
                        applyName="预支申请";
                    }else if(applyId.equals("REPAYMENT")){
                        applyName="还款申请";
                    }else if(applyId.equals("DAILY")){
                        applyName="费用报销";
                    }else if(applyId.equals("BUSINESS")){
                        applyName="事务申请";
                    }
                }
            }
        }
        return "success";
    }
    @PageFlow(result = {@Result(name = "success", path = "/view/wf/advanceAccount/view.ftl", type = Dispatcher.FreeMarker)})
    public String view() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null&&reqId!=null) {
            wfReqAdvanceAccount=this.wfReqAdvanceAccountService.getByReqId(reqId);
            if(wfReqAdvanceAccount!=null){
                wfReq=wfReqAdvanceAccount.getReqId();
                if(wfReq!=null){
                    reqCommentsList = this.wfReqCommentsService.getCommentsListByReqId(wfReq.getId());
                    reqAttList=wfReqAttService.getByReqId(wfReq.getId());
                }
            }
        }
        return "success";
    }
    @PageFlow(result = {@Result(name = "success", path = "/view/wf/advanceAccount/financial.ftl", type = Dispatcher.FreeMarker)})
    public String financial() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null&&reqId!=null) {
            wfReqAdvanceAccount=this.wfReqAdvanceAccountService.getByReqId(reqId);
            if(wfReqAdvanceAccount!=null){
                wfReq=wfReqAdvanceAccount.getReqId();
                if(wfReq!=null){
                    reqCommentsList = this.wfReqCommentsService.getCommentsListByReqId(wfReq.getId());
                    reqAttList=wfReqAttService.getByReqId(wfReq.getId());
                }
            }
        }
        return "success";
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/advanceAccount/process.ftl", type = Dispatcher.FreeMarker)})
    public String process() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null&&taskId!=null) {
            wfReqTask=this.wfReqTaskService.getById(taskId);
            if(wfReqTask!=null){
                wfReq=wfReqTask.getReqId();
                if(wfReq!=null){
                    wfReqAdvanceAccount=this.wfReqAdvanceAccountService.getByReqId(wfReq.getId());
                    reqCommentsList = this.wfReqCommentsService.getCommentsListByReqId(wfReq.getId());
                    reqAttList=wfReqAttService.getByReqId(wfReq.getId());
                }
            }
        }
        return "success";
    }





}
