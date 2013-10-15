package com.dqy.wf.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.hr.entity.HrUser;
import com.dqy.hr.service.HrUserService;
import com.dqy.sys.entity.SysOrg;
import com.dqy.sys.service.SysOrgService;
import com.dqy.web.support.ActionSupport;
import com.dqy.wf.entity.*;
import com.dqy.wf.service.*;
import com.google.inject.Inject;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.commons.lang.DateFormatUtil;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.persistence.entity.search.SelectorUtils;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;
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
@Action(name = "reqTask", namespace = "/wf")
public class WfReqTaskAction extends ActionSupport<WfReqTask> {

    @Inject
    private WfReqService wfReqService;

    @Inject
    private WfReqTaskService wfReqTaskService;

    @Inject
    private WfReqCommentsService wfReqCommentsService;

    @Inject
    private WfReqNodeApproveService wfReqNodeApproveService;

    @Inject
    private WfVariableGlobalService wfVariableGlobalService;

    @Inject
    private WfReqRePaymentService wfReqRePaymentService;

    @Inject
    private WfReqRePaymentDetailService wfReqRePaymentDetailService;

    @Inject
    private WfReqAdvanceAccountService wfReqAdvanceAccountService;

    @Inject
    private HrUserService hrUserService;

    @Inject
    private SysOrgService sysOrgService;

    @ReqGet
    @ReqSet
    private Long id;


    @ReqGet
    @ModelDriver
    @ReqSet
    private WfReqTask wfReqTask;

    @ReqSet
    private List<WfReqTask> reqTaskList;

    @ReqGet
    @ReqSet
    private String orderKey;

    @ReqGet
    @ReqSet
    private String searchKey;

    @ReqGet
    private String reason;

    @ReqGet
    @ReqSet
    private String searchType;

    @ReqGet
    @ReqSet
    private Date searchDate;

    @ReqSet
    private Integer maxRows;

    @ReqGet
    private Integer approveIdea;

    @ReqGet
    private String forwardId;

    @ReqSet
    private String applyId;

    @ReqGet
    private Integer forwardType;

    @ReqGet
    private String forwardMe;

    @ReqSet
    private List<WfReqComments> reqCommentsList;

    private String defaultSubject;


    @Override
    public String execute() throws Exception {
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/wf/req!print.dhtml?id=${wfReqTask.reqId.id}", type = Dispatcher.Redirect)})
    public String print() throws Exception {
        if (id != null) {
            wfReqTask = this.wfReqTaskService.getById(id);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/reqTask/reason.ftl", type = Dispatcher.FreeMarker)})
    public String reason() throws Exception {
        if (id != null&&approveIdea!=null) {
            wfReqTask = this.wfReqTaskService.getById(id);
            wfReqTask.setApproveIdea(approveIdea);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/reqTask/forward.ftl", type = Dispatcher.FreeMarker)})
    public String forward() throws Exception {
        if (id != null&&approveIdea!=null) {
            wfReqTask = this.wfReqTaskService.getById(id);
            wfReqTask.setApproveIdea(approveIdea);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }


    private List<Selector> searchModeCallbackDoneList() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("apply");
            userInfo.setLeftMenu("myTask");
            userInfo.setChildMenu("done");
            Long orgId = userInfo.getOrgId();
            Long userId = userInfo.getUserId();
            if (orgId != null && userId != null) {
                selectorList.add(SelectorUtils.$eq("orgId.id", orgId));
                selectorList.add(SelectorUtils.$eq("userId.id", userId));
                selectorList.add(SelectorUtils.$eq("taskState", 1));
                selectorList.add(SelectorUtils.$eq("taskRead", 1));
                boolean aliasReq = false;
                if (StringUtils.isNotBlank(searchType) && StringUtils.isNotBlank(searchKey)) {
                    if (searchType.equals("reqNo")) {
                        selectorList.add(SelectorUtils.$alias("reqId", "reqId"));
                        selectorList.add(SelectorUtils.$like("reqId.reqNo", searchKey));
                        aliasReq = true;
                    } else if (searchType.equals("subject")) {
                        selectorList.add(SelectorUtils.$alias("reqId", "reqId"));
                        selectorList.add(SelectorUtils.$like("reqId.subject", searchKey));
                        aliasReq = true;
                    }
                }
                if (searchDate != null) {
                    String sDate = DateFormatUtil.format(searchDate, DateFormatUtil.YEAR_MONTH_DAY_PATTERN);
                    Date startDate = DateFormatUtil.parse(sDate + " 00:00:01", DateFormatUtil.YMDHMS_PATTERN);
                    Date endDate = DateFormatUtil.parse(sDate + " 23:59:59", DateFormatUtil.YMDHMS_PATTERN);
                    selectorList.add(SelectorUtils.$ge("receiveDate", startDate));
                    selectorList.add(SelectorUtils.$le("receiveDate", endDate));
                }
                if (StringUtils.isNotBlank(orderKey)) {
                    if (orderKey.equals("receiveDate")) {
                        selectorList.add(SelectorUtils.$order("receiveDate", isAsc()));
                    } else if (orderKey.equals("exigency")) {
                        if (!aliasReq) {
                            selectorList.add(SelectorUtils.$alias("reqId", "reqId"));
                        }
                        selectorList.add(SelectorUtils.$order("reqId.exigency", isAsc()));
                    } else if (orderKey.equals("applyType")) {
                        if (!aliasReq) {
                            selectorList.add(SelectorUtils.$alias("reqId", "reqId"));
                        }
                        selectorList.add(SelectorUtils.$order("reqId.applyId.id", isAsc()));
                    }
                } else {
                    selectorList.add(SelectorUtils.$order("receiveDate", false));
                }
            } else {
                selectorList.add(SelectorUtils.$eq("id", 0l));
            }

        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/reqTask/doneList.ftl", type = Dispatcher.FreeMarker)})
    public String doneList() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            pageObj = this.wfReqTaskService.getPageList(getStart(), 10, searchModeCallbackDoneList());
            if (pageObj != null) {
                reqTaskList = pageObj.getResultList();
            }
        }

        return "success";
    }


    private List<Selector> searchModeCallbackApproveList() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("apply");
            userInfo.setLeftMenu("myTask");
            userInfo.setChildMenu("approve");
            Long orgId = userInfo.getOrgId();
            Long userId = userInfo.getUserId();
            if (orgId != null && userId != null) {
                selectorList.add(SelectorUtils.$eq("orgId.id", orgId));
                selectorList.add(SelectorUtils.$eq("userId.id", userId));
                selectorList.add(SelectorUtils.$eq("taskState", 0));
                boolean aliasReq = false;
                if (StringUtils.isNotBlank(searchType) && StringUtils.isNotBlank(searchKey)) {
                    if (searchType.equals("reqNo")) {
                        selectorList.add(SelectorUtils.$alias("reqId", "reqId"));
                        selectorList.add(SelectorUtils.$like("reqId.reqNo", searchKey));
                        aliasReq = true;
                    } else if (searchType.equals("subject")) {
                        selectorList.add(SelectorUtils.$alias("reqId", "reqId"));
                        selectorList.add(SelectorUtils.$like("reqId.subject", searchKey));
                        aliasReq = true;
                    }
                }
                if (searchDate != null) {
                    String sDate = DateFormatUtil.format(searchDate, DateFormatUtil.YEAR_MONTH_DAY_PATTERN);
                    Date startDate = DateFormatUtil.parse(sDate + " 00:00:01", DateFormatUtil.YMDHMS_PATTERN);
                    Date endDate = DateFormatUtil.parse(sDate + " 23:59:59", DateFormatUtil.YMDHMS_PATTERN);
                    selectorList.add(SelectorUtils.$ge("receiveDate", startDate));
                    selectorList.add(SelectorUtils.$le("receiveDate", endDate));
                }
                if (StringUtils.isNotBlank(orderKey)) {
                    if (orderKey.equals("receiveDate")) {
                        selectorList.add(SelectorUtils.$order("receiveDate", isAsc()));
                    } else if (orderKey.equals("exigency")) {
                        if (!aliasReq) {
                            selectorList.add(SelectorUtils.$alias("reqId", "reqId"));
                        }
                        selectorList.add(SelectorUtils.$order("reqId.exigency", isAsc()));
                    } else if (orderKey.equals("applyType")) {
                        if (!aliasReq) {
                            selectorList.add(SelectorUtils.$alias("reqId", "reqId"));
                        }
                        selectorList.add(SelectorUtils.$order("reqId.applyId.id", isAsc()));
                    }
                } else {
                    selectorList.add(SelectorUtils.$order("receiveDate", false));
                }
            } else {
                selectorList.add(SelectorUtils.$eq("id", 0l));
            }

        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/reqTask/approveList.ftl", type = Dispatcher.FreeMarker)})
    public String approveList() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            pageObj = this.wfReqTaskService.getPageList(getStart(), 10, searchModeCallbackApproveList());
            if (pageObj != null) {
                reqTaskList = pageObj.getResultList();
            }
        }
        return "success";
    }

    @PageFlow(result = {@Result(name = "success", path = "/wf/req!view.dhtml?id=${wfReqTask.reqId.id}", type = Dispatcher.Redirect)})
    public String view() throws Exception {
        if (id != null) {
            wfReqTask = this.wfReqTaskService.getById(id);
        }
        return "success";
    }

    @PageFlow(result = {@Result(name = "ADVANCE_ACCOUNT", path = "/wf/advanceAccount!process.dhtml?taskId=${wfReqTask.id}", type = Dispatcher.Redirect),
            @Result(name = "REPAYMENT", path = "/wf/rePayment!process.dhtml?taskId=${wfReqTask.id}", type = Dispatcher.Redirect),
            @Result(name = "DAILY", path = "/wf/daily!process.dhtml?taskId=${wfReqTask.id}", type = Dispatcher.Redirect),
            @Result(name = "BUSINESS", path = "/wf/business!process.dhtml?taskId=${wfReqTask.id}", type = Dispatcher.Redirect),
            @Result(name = "view", path = "/wf/req!view.dhtml?id=${wfReqTask.reqId.id}", type = Dispatcher.Redirect)})
    public String process() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (id != null && userInfo != null) {
            wfReqTask = this.wfReqTaskService.getById(id);
            if (wfReqTask != null) {
                HrUser user = this.hrUserService.getById(userInfo.getUserId());
                if (wfReqTask.getTaskState().intValue() == 1) {
                    return "view";
                }
                if (!wfReqTask.getUserId().getId().equals(userInfo.getUserId())) {
                    return "view";
                }
                if (wfReqTask.getTaskRead().intValue() == 0) {
                    wfReqTask.setTaskRead(1);
                    bind(wfReqTask);
                    WfReqComments comments = new WfReqComments();
                    comments.setReqId(wfReqTask.getReqId());
                    comments.setApprove(0);
                    comments.setUserId(user);
                    comments.setAction(3);
                    comments.setUseYn("Y");
                    bind(comments);
                    wfReqTaskService.save(wfReqTask, comments);
                    Integer unRead = wfReqTaskService.getCountUnRead(userInfo.getOrgId(), userInfo.getUserId());
                    if (unRead == null) {
                        unRead = 0;
                    }
                    userInfo.setTaskUnRead(unRead);
                }
                WfReq wfReq = wfReqTask.getReqId();
                if (wfReq != null) {
                    applyId = wfReq.getApplyId();
                }
            }
        }
        return applyId;
    }


    public String approve() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && wfReqTask != null) {
            approveIdea = wfReqTask.getApproveIdea();
            if (approveIdea != null && wfReqTask.getId() != null) {
                wfReqTask = this.wfReqTaskService.getById(wfReqTask.getId());
                if (wfReqTask != null) {
                    WfReq wfReq = wfReqTask.getReqId();
                    SysOrg sysOrg = this.sysOrgService.getById(userInfo.getOrgId());
                    HrUser user = this.hrUserService.getById(userInfo.getUserId());
                    if (wfReq != null) {
                        wfReqTask.setTaskState(1);
                        wfReqTask.setApproveIdea(approveIdea);
                        bind(wfReqTask);

                        Integer action = null;
                        if (approveIdea.intValue() == 1) {
                            action = 4;
                        } else if (approveIdea.intValue() == 2) {
                            action = 5;
                        } else if (approveIdea.intValue() == 3) {
                            action = 8;
                        } else if (approveIdea.intValue() == 4) {
                            action = 9;
                        }
                        List<WfReqComments> reqCommentsList = null;
                        List<WfReqTask> reqNextTaskList = null;
                        if (action != null) {
                            reqCommentsList = new ArrayList<WfReqComments>();
                            WfReqComments comments = new WfReqComments();
                            comments.setReqId(wfReqTask.getReqId());
                            comments.setApprove(1);
                            comments.setUserId(wfReqTask.getUserId());
                            comments.setAction(action);
                            if (StringUtils.isNotBlank(reason)) {
                                comments.setContent(reason);
                            }
                            comments.setUseYn("Y");
                            bind(comments);
                            reqCommentsList.add(comments);
                            Date cudDate = DateFormatUtil.getCurrentDate(true);

                            WfReqNodeApprove currentNodeTask = wfReqTask.getNodeId();
                            if (action.intValue() == 4) {
                                //passed
                                List<WfReqNodeApprove> nextReqNodeApproveList = wfReqNodeApproveService.getNodeListByReqIdNodeSeq(userInfo.getOrgId(), wfReq.getId(),
                                        currentNodeTask.getNodeSeq() + 1);
                                if (nextReqNodeApproveList != null && !nextReqNodeApproveList.isEmpty()) {
                                    if (currentNodeTask.getNodeType().intValue() == 1) {
                                        //findNextNode
                                        reqNextTaskList = new ArrayList<WfReqTask>();
                                        for (WfReqNodeApprove nodeApprove : nextReqNodeApproveList) {
                                            wfReq.setCurrentNode(nodeApprove);
                                            WfReqTask reqTask = new WfReqTask();
                                            reqTask.setReqId(wfReq);
                                            reqTask.setUserId(nodeApprove.getUserId());
                                            reqTask.setOrgId(sysOrg);
                                            reqTask.setNodeSeq(nodeApprove.getNodeSeq());
                                            reqTask.setReceiveDate(cudDate);
                                            reqTask.setTaskRead(0);
                                            reqTask.setTaskState(0);
                                            reqTask.setApproveIdea(0);
                                            reqTask.setNodeId(nodeApprove);
                                            reqTask.setUseYn("Y");
                                            bind(reqTask);
                                            reqNextTaskList.add(reqTask);

                                            WfReqComments nextComments = new WfReqComments();
                                            nextComments.setReqId(wfReq);
                                            nextComments.setApprove(0);
                                            nextComments.setUserId(nodeApprove.getUserId());
                                            nextComments.setAction(2);
                                            nextComments.setUseYn("Y");
                                            bind(nextComments);
                                            reqCommentsList.add(nextComments);
                                        }
                                        this.wfReqTaskService.save(wfReqTask, wfReq, reqCommentsList, reqNextTaskList);
                                    } else if (currentNodeTask.getNodeType().intValue() == 2) {
                                        Integer countUnApprove = this.wfReqTaskService.getCountUnApproveByNodeSeq(userInfo.getOrgId(), wfReq.getId(),
                                                currentNodeTask.getNodeSeq());
                                        if (countUnApprove == null) {
                                            countUnApprove = 0;
                                        }
                                        if (countUnApprove.intValue() == 1) {
                                            reqNextTaskList = new ArrayList<WfReqTask>();
                                            for (WfReqNodeApprove nodeApprove : nextReqNodeApproveList) {
                                                wfReq.setCurrentNode(nodeApprove);
                                                WfReqTask reqTask = new WfReqTask();
                                                reqTask.setReqId(wfReq);
                                                reqTask.setUserId(nodeApprove.getUserId());
                                                reqTask.setOrgId(sysOrg);
                                                reqTask.setNodeSeq(nodeApprove.getNodeSeq());
                                                reqTask.setReceiveDate(cudDate);
                                                reqTask.setTaskRead(0);
                                                reqTask.setTaskState(0);
                                                reqTask.setApproveIdea(0);
                                                reqTask.setNodeId(nodeApprove);
                                                reqTask.setUseYn("Y");
                                                bind(reqTask);
                                                reqNextTaskList.add(reqTask);

                                                WfReqComments nextComments = new WfReqComments();
                                                nextComments.setReqId(wfReq);
                                                nextComments.setApprove(0);
                                                nextComments.setUserId(nodeApprove.getUserId());
                                                nextComments.setAction(2);
                                                nextComments.setUseYn("Y");
                                                bind(nextComments);
                                                reqCommentsList.add(nextComments);
                                            }
                                            this.wfReqTaskService.save(wfReqTask, wfReq, reqCommentsList, reqNextTaskList);
                                        } else {
                                            this.wfReqTaskService.save(wfReqTask, comments);
                                        }
                                    }
                                } else {
                                    if (currentNodeTask.getNodeType().intValue() == 1) {
                                        //done
                                        wfReq.setComplete(1);
                                        wfReq.setCompleteDate(cudDate);
                                        wfReq.setApplyState(2);
                                        wfReq.setApplyResult(1);
                                        wfReq.setTip(1);
                                        bind(wfReq);

                                        WfReqComments doneComments = new WfReqComments();
                                        doneComments.setReqId(wfReqTask.getReqId());
                                        doneComments.setApprove(0);
                                        doneComments.setUserId(wfReqTask.getUserId());
                                        doneComments.setAction(7);
                                        doneComments.setUseYn("Y");
                                        bind(doneComments);
                                        reqCommentsList.add(doneComments);
                                        if(wfReq.getApplyId().equals("REPAYMENT")){
                                            WfReqRePayment reqRePayment=this.wfReqRePaymentService.getByReqId(wfReq.getId());
                                            if(reqRePayment!=null){
                                                WfReqAdvanceAccount advanceAccount= reqRePayment.getAdvanceId();
                                                if(advanceAccount!=null){
                                                    Long orgId=reqRePayment.getReqId().getOrgId().getId();
                                                    Long userId=reqRePayment.getReqId().getUserId().getId();
                                                    Double reAmount= wfReqRePaymentService.getSumByReAmount(orgId,userId,advanceAccount.getId());
                                                    if(reAmount==null){
                                                        reAmount=0.0d;
                                                    }
                                                    reAmount+=reqRePayment.getAmount();
                                                    Double diffAmount=advanceAccount.getAmount()-reAmount;
                                                    if(diffAmount<=0){
                                                        advanceAccount.setRePayYn("Y");
                                                    }
                                                    this.wfReqTaskService.saveDone(wfReqTask,advanceAccount, wfReq, reqCommentsList);
                                                }else{
                                                    this.wfReqTaskService.saveDone(wfReqTask, wfReq, reqCommentsList);
                                                }
                                            }
                                        }else{
                                            this.wfReqTaskService.saveDone(wfReqTask, wfReq, reqCommentsList);
                                        }
                                    } else if (currentNodeTask.getNodeType().intValue() == 2) {
                                        Integer countUnApprove = this.wfReqTaskService.getCountUnApproveByNodeSeq(userInfo.getOrgId(), wfReq.getId(),
                                                currentNodeTask.getNodeSeq());
                                        if (countUnApprove == null) {
                                            countUnApprove = 0;
                                        }
                                        if (countUnApprove.intValue() == 1) {
                                            wfReq.setComplete(1);
                                            wfReq.setCompleteDate(cudDate);
                                            wfReq.setApplyState(2);
                                            wfReq.setApplyResult(1);
                                            wfReq.setTip(1);
                                            bind(wfReq);
                                            WfReqComments doneComments = new WfReqComments();
                                            doneComments.setReqId(wfReqTask.getReqId());
                                            doneComments.setApprove(0);
                                            doneComments.setUserId(wfReqTask.getUserId());
                                            doneComments.setAction(7);
                                            doneComments.setUseYn("Y");
                                            bind(doneComments);
                                            reqCommentsList.add(doneComments);
                                            if(wfReq.getApplyId().equals("REPAYMENT")){
                                                WfReqRePayment reqRePayment=this.wfReqRePaymentService.getByReqId(wfReq.getId());
                                                if(reqRePayment!=null){
                                                    WfReqAdvanceAccount advanceAccount= reqRePayment.getAdvanceId();
                                                    if(advanceAccount!=null){
                                                        Long orgId=reqRePayment.getReqId().getOrgId().getId();
                                                        Long userId=reqRePayment.getReqId().getUserId().getId();
                                                        Double reAmount= wfReqRePaymentService.getSumByReAmount(orgId,userId,advanceAccount.getId());
                                                        if(reAmount==null){
                                                            reAmount=0.0d;
                                                        }
                                                        Double diffAmount=advanceAccount.getAmount()-reAmount;
                                                        if(diffAmount<=0){
                                                            advanceAccount.setRePayYn("Y");
                                                        }
                                                        this.wfReqTaskService.saveDone(wfReqTask,advanceAccount, wfReq, reqCommentsList);
                                                    }else{
                                                        this.wfReqTaskService.saveDone(wfReqTask, wfReq, reqCommentsList);
                                                    }
                                                }
                                            }else{
                                                this.wfReqTaskService.saveDone(wfReqTask, wfReq, reqCommentsList);
                                            }
                                        } else {
                                            this.wfReqTaskService.save(wfReqTask, comments);
                                        }
                                    }
                                }
                            } else if (action.intValue() == 5) {
                                wfReq.setComplete(1);
                                wfReq.setCompleteDate(cudDate);
                                wfReq.setApplyState(2);
                                wfReq.setApplyResult(2);
                                wfReq.setTip(1);
                                bind(wfReq);

                                WfReqComments doneComments = new WfReqComments();
                                doneComments.setReqId(wfReqTask.getReqId());
                                doneComments.setApprove(0);
                                doneComments.setUserId(wfReqTask.getUserId());
                                doneComments.setAction(7);
                                doneComments.setUseYn("Y");
                                bind(doneComments);
                                reqCommentsList.add(doneComments);


                                if (currentNodeTask.getNodeType().intValue() == 1) {
                                    this.wfReqTaskService.save(wfReqTask, wfReq, reqCommentsList, null);
                                } else if (currentNodeTask.getNodeType().intValue() == 2) {
                                    List<WfReqTask> unApproveList = this.wfReqTaskService.getUnApproveListByNodeSeq(userInfo.getOrgId(), wfReqTask.getUserId().getId(), wfReq.getId(),
                                            currentNodeTask.getNodeSeq());
                                    if (unApproveList != null && !unApproveList.isEmpty()) {
                                        for (WfReqTask unApprove : unApproveList) {
                                            unApprove.setTaskRead(1);
                                            unApprove.setTaskState(1);
                                            unApprove.setApproveIdea(2);
                                            bind(unApprove);
                                        }
                                    }
                                    this.wfReqTaskService.save(wfReqTask, wfReq, reqCommentsList, unApproveList);
                                }

                            } else if (action.intValue() == 8) {
                                //forward
                                Long approveUserId = null;
                                if (forwardType != null && StringUtils.isNotBlank(forwardId)) {
                                    if (forwardType.intValue() == 1) {
                                        Long gID = BeanUtils.convertValue(forwardId, Long.class);
                                        if (gID != null) {
                                            WfVariableGlobal variableGlobal = wfVariableGlobalService.getById(gID);
                                            if (variableGlobal != null) {
                                                approveUserId = get(variableGlobal, "userId.id", Long.class);
                                            }
                                        }
                                    } else if (forwardType.intValue() == 2) {
                                        approveUserId = null;
                                    } else if (forwardType.intValue() == 3) {
                                        approveUserId = BeanUtils.convertValue(forwardId, Long.class);
                                        ;
                                    }
                                    if (approveUserId != null) {

                                        List<WfReqNodeApprove> addReqNodeApproveList = new ArrayList<WfReqNodeApprove>();

                                        List<WfReqNodeApprove> gtReqNodeApproveList = wfReqNodeApproveService.getGtNodeListByReqIdNodeSeq(userInfo.getOrgId(),
                                                wfReq.getId(),
                                                currentNodeTask.getNodeSeq());
                                        if (gtReqNodeApproveList != null && !gtReqNodeApproveList.isEmpty()) {
                                            for (WfReqNodeApprove nodeApprove : gtReqNodeApproveList) {
                                                if (StringUtils.isNotBlank(forwardMe) && forwardMe.equals("Y")) {
                                                    nodeApprove.setNodeSeq(nodeApprove.getNodeSeq() + 2);
                                                } else {
                                                    nodeApprove.setNodeSeq(nodeApprove.getNodeSeq() + 1);
                                                }
                                            }
                                        }
                                        HrUser approveUser = this.hrUserService.getById(approveUserId);
                                        WfReqNodeApprove forwardNodeApprove = new WfReqNodeApprove();
                                        forwardNodeApprove.setNodeSeq(currentNodeTask.getNodeSeq() + 1);
                                        forwardNodeApprove.setOrgId(sysOrg);
                                        forwardNodeApprove.setNodeType(1);
                                        forwardNodeApprove.setApproveType(forwardType);
                                        forwardNodeApprove.setReqId(wfReq);
                                        forwardNodeApprove.setUserId(approveUser);
                                        forwardNodeApprove.setUseYn("Y");
                                        bind(forwardNodeApprove);
                                        addReqNodeApproveList.add(forwardNodeApprove);

                                        if (StringUtils.isNotBlank(forwardMe) && forwardMe.equals("Y")) {
                                            WfReqNodeApprove forwardMeNodeApprove = new WfReqNodeApprove();
                                            forwardMeNodeApprove.setNodeSeq(forwardNodeApprove.getNodeSeq() + 1);
                                            forwardMeNodeApprove.setNodeType(1);
                                            forwardMeNodeApprove.setOrgId(sysOrg);
                                            forwardMeNodeApprove.setApproveType(3);
                                            forwardMeNodeApprove.setReqId(wfReq);
                                            forwardMeNodeApprove.setUserId(wfReqTask.getUserId());
                                            forwardMeNodeApprove.setUseYn("Y");
                                            bind(forwardMeNodeApprove);
                                            addReqNodeApproveList.add(forwardMeNodeApprove);
                                            wfReq.setNodeCount(wfReq.getNodeCount() + 2);
                                        } else {
                                            wfReq.setNodeCount(wfReq.getNodeCount() + 1);
                                        }
                                        wfReq.setCurrentNode(forwardNodeApprove);

                                        WfReqTask reqTask = new WfReqTask();
                                        reqTask.setReqId(wfReq);
                                        reqTask.setUserId(forwardNodeApprove.getUserId());
                                        reqTask.setOrgId(sysOrg);
                                        reqTask.setNodeSeq(forwardNodeApprove.getNodeSeq());
                                        reqTask.setReceiveDate(cudDate);
                                        reqTask.setTaskRead(0);
                                        reqTask.setTaskState(0);
                                        reqTask.setApproveIdea(0);
                                        reqTask.setNodeId(forwardNodeApprove);
                                        reqTask.setUseYn("Y");
                                        bind(reqTask);

                                        WfReqComments nextComments = new WfReqComments();
                                        nextComments.setReqId(wfReq);
                                        nextComments.setApprove(0);
                                        nextComments.setUserId(forwardNodeApprove.getUserId());
                                        nextComments.setAction(2);
                                        nextComments.setUseYn("Y");
                                        bind(nextComments);
                                        reqCommentsList.add(nextComments);
                                        this.wfReqTaskService.save(wfReqTask, gtReqNodeApproveList, addReqNodeApproveList, wfReq, reqCommentsList, reqTask);
                                    }
                                }
                            } else if (action.intValue() == 9) {
                                //forward
                                Integer backNodeSeq = currentNodeTask.getNodeSeq() - 1;
                                List<WfReqNodeApprove> backReqNodeApproveList = wfReqNodeApproveService.getNodeListByReqIdNodeSeq(userInfo.getOrgId(), wfReq.getId(),
                                        backNodeSeq);
                                if (backReqNodeApproveList != null && !backReqNodeApproveList.isEmpty()) {
                                    //findNextNode
                                    reqNextTaskList = new ArrayList<WfReqTask>();
                                    for (WfReqNodeApprove nodeApprove : backReqNodeApproveList) {
                                        WfReqTask backTask = wfReqTaskService.getTaskByReqIdNode(userInfo.getOrgId(), wfReq.getId(), backNodeSeq, nodeApprove.getId());
                                        if (backTask != null) {
                                            backTask.setTaskRead(0);
                                            backTask.setTaskState(0);
                                            backTask.setApproveIdea(0);
                                            bind(backTask);
                                            wfReq.setCurrentNode(nodeApprove);
                                            reqNextTaskList.add(backTask);

                                            WfReqComments nextComments = new WfReqComments();
                                            nextComments.setReqId(wfReq);
                                            nextComments.setApprove(0);
                                            nextComments.setUserId(backTask.getUserId());
                                            nextComments.setAction(2);
                                            nextComments.setUseYn("Y");
                                            bind(nextComments);
                                            reqCommentsList.add(nextComments);
                                        }
                                    }
                                    this.wfReqTaskService.save(wfReqTask, wfReq, reqCommentsList, reqNextTaskList);
                                }
                            }
                        }
                    }
                }
            }
        }
        Integer unApprove = wfReqTaskService.getCountUnApprove(userInfo.getOrgId(), userInfo.getUserId());
        if (unApprove == null) {
            unApprove = 0;
        }
        userInfo.setTaskUnApprove(unApprove);
        script = "parent.setURL('/wf/reqTask!approveList.dhtml');";
        return "saveSuccess";  //To change body of implemented methods use File | Settings | File Templates.
    }
}
