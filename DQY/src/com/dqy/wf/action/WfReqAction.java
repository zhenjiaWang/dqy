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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.commons.lang.DateFormatUtil;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.persistence.entity.search.SelectorUtils;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;
import org.guiceside.support.file.FileManager;
import org.guiceside.web.annotation.*;

import java.io.File;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjiaWang
 * Date: 12-7-12
 * Time: 下午9:49
 * To change this template use File | Settings | File Templates.
 */
@Action(name = "req", namespace = "/wf")
public class WfReqAction extends ActionSupport<WfReq> {

    @Inject
    private WfReqService wfReqService;

    @Inject
    private HrUserService hrUserService;

    @Inject
    private WfReqAttService wfReqAttService;

    @Inject
    private SysOrgService sysOrgService;

    @Inject
    private WfReqMyFlowService wfReqMyFlowService;

    @Inject
    private WfReqNoSeqService wfReqNoSeqService;


    @Inject
    private WfReqCommentsService wfReqCommentsService;

    @Inject
    private WfReqNodeApproveService wfReqNodeApproveService;

    @Inject
    private WfReqDailyService wfReqDailyService;

    @Inject
    private WfReqDailyDetailService wfReqDailyDetailService;

    @Inject
    private WfReqDailyTrueService wfReqDailyTrueService;

    @Inject
    private WfReqBusinessService wfReqBusinessService;


    @Inject
    private WfReqTaskService wfReqTaskService;


    @Inject
    private WfReqMyFlowLastService wfReqMyFlowLastService;

    @Inject
    private WfReqMyFlowNodeService wfReqMyFlowNodeService;

    @Inject
    private WfReqMyFlowNodeApproveService wfReqMyFlowNodeApproveService;

    @Inject
    private WfReqAdvanceAccountService wfReqAdvanceAccountService;

    @Inject
    private WfReqRePaymentService wfReqRePaymentService;

    @Inject
    private WfReqRePaymentDetailService wfReqRePaymentDetailService;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private Long applyMyId;

    @ReqGet
    @ReqSet
    private Long targetId;

    @ReqGet
    @ReqSet
    private String applyId;

    @ReqGet
    @ModelDriver
    @ReqSet
    private WfReq wfReq;


    @ReqSet
    private List<WfReq> reqList;

    @ReqGet
    @ReqSet
    private String orderKey;

    @ReqGet
    @ReqSet
    private String searchKey;


    @ReqGet
    @ReqSet
    private String searchType;

    @ReqGet
    @ReqSet
    private Date searchDate;

    @ReqSet
    private Integer maxRows;

    @ReqGet
    @ReqSet
    private Long flowId;


    @ReqSet
    private Integer maxCols;


    @ReqSet
    private List<WfReqComments> reqCommentsList;


    @ReqSet
    private String defaultSubject;

    @ReqGet
    @ReqSet
    private Integer flowUse;

    @ReqGet
    @ReqSet
    private String attToken;

    @ReqGet
    @ReqSet
    private String keyword;

    @ReqGet
    private String reason;

    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/apply/my/index.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }


    private List<Selector> searchModeCallbackOverruleList() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("apply");
            userInfo.setLeftMenu("myReq");
            userInfo.setChildMenu("overrule");
            Long orgId = userInfo.getOrgId();
            Long userId = userInfo.getUserId();
            if (orgId != null && userId != null) {
                selectorList.add(SelectorUtils.$eq("orgId.id", orgId));
                selectorList.add(SelectorUtils.$eq("userId.id", userId));
                selectorList.add(SelectorUtils.$eq("applyState", 2));
                selectorList.add(SelectorUtils.$eq("applyResult", 2));
                selectorList.add(SelectorUtils.$eq("complete", 1));
                if (StringUtils.isNotBlank(keyword)) {
                    selectorList.add(SelectorUtils.$or(SelectorUtils.$like("reqNo", keyword), SelectorUtils.$like("subject", keyword)));
                }
                if (StringUtils.isNotBlank(searchType) && StringUtils.isNotBlank(searchKey)) {
                    if (searchType.equals("reqNo")) {
                        selectorList.add(SelectorUtils.$like("reqNo", searchKey));
                    }
                }
                if (searchDate != null) {
                    String sDate = DateFormatUtil.format(searchDate, DateFormatUtil.YEAR_MONTH_DAY_PATTERN);
                    Date startDate = DateFormatUtil.parse(sDate + " 00:00:01", DateFormatUtil.YMDHMS_PATTERN);
                    Date endDate = DateFormatUtil.parse(sDate + " 23:59:59", DateFormatUtil.YMDHMS_PATTERN);
                    selectorList.add(SelectorUtils.$ge("sendDate", startDate));
                    selectorList.add(SelectorUtils.$le("sendDate", endDate));
                }
                if (StringUtils.isNotBlank(orderKey)) {
                    selectorList.add(SelectorUtils.$order(orderKey, isAsc()));
                } else {
                    selectorList.add(SelectorUtils.$order("sendDate", false));
                }
            } else {
                selectorList.add(SelectorUtils.$eq("id", 0l));
            }

        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/req/overruleList.ftl", type = Dispatcher.FreeMarker)})
    public String overruleList() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            pageObj = this.wfReqService.getPageList(getStart(), 10, searchModeCallbackOverruleList());
            if (pageObj != null) {
                reqList = pageObj.getResultList();
            }
        }
        return "success";
    }


    private List<Selector> searchModeCallbackPassList() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("apply");
            userInfo.setLeftMenu("myReq");
            userInfo.setChildMenu("pass");
            Long orgId = userInfo.getOrgId();
            Long userId = userInfo.getUserId();
            if (orgId != null && userId != null) {
                selectorList.add(SelectorUtils.$eq("orgId.id", orgId));
                selectorList.add(SelectorUtils.$eq("userId.id", userId));
                selectorList.add(SelectorUtils.$eq("applyState", 2));
                selectorList.add(SelectorUtils.$eq("applyResult", 1));
                selectorList.add(SelectorUtils.$eq("complete", 1));
                if (StringUtils.isNotBlank(keyword)) {
                    selectorList.add(SelectorUtils.$or(SelectorUtils.$like("reqNo", keyword), SelectorUtils.$like("subject", keyword)));
                }
                if (StringUtils.isNotBlank(searchType) && StringUtils.isNotBlank(searchKey)) {
                    if (searchType.equals("reqNo")) {
                        selectorList.add(SelectorUtils.$like("reqNo", searchKey));
                    }
                }
                if (searchDate != null) {
                    String sDate = DateFormatUtil.format(searchDate, DateFormatUtil.YEAR_MONTH_DAY_PATTERN);
                    Date startDate = DateFormatUtil.parse(sDate + " 00:00:01", DateFormatUtil.YMDHMS_PATTERN);
                    Date endDate = DateFormatUtil.parse(sDate + " 23:59:59", DateFormatUtil.YMDHMS_PATTERN);
                    selectorList.add(SelectorUtils.$ge("sendDate", startDate));
                    selectorList.add(SelectorUtils.$le("sendDate", endDate));
                }
                if (StringUtils.isNotBlank(orderKey)) {
                    selectorList.add(SelectorUtils.$order(orderKey, isAsc()));
                } else {
                    selectorList.add(SelectorUtils.$order("sendDate", false));
                }
            } else {
                selectorList.add(SelectorUtils.$eq("id", 0l));
            }
        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/req/passList.ftl", type = Dispatcher.FreeMarker)})
    public String passList() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            pageObj = this.wfReqService.getPageList(getStart(), 10, searchModeCallbackPassList());
            if (pageObj != null) {
                reqList = pageObj.getResultList();
            }
        }

        return "success";
    }


    private List<Selector> searchModeCallbackIngList() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("apply");
            userInfo.setLeftMenu("myReq");
            userInfo.setChildMenu("ing");
            Long orgId = userInfo.getOrgId();
            Long userId = userInfo.getUserId();
            if (orgId != null && userId != null) {
                selectorList.add(SelectorUtils.$eq("orgId.id", orgId));
                selectorList.add(SelectorUtils.$eq("userId.id", userId));
                selectorList.add(SelectorUtils.$eq("applyState", 1));
                selectorList.add(SelectorUtils.$eq("applyResult", 0));
                selectorList.add(SelectorUtils.$eq("complete", 0));
                if (StringUtils.isNotBlank(keyword)) {
                    selectorList.add(SelectorUtils.$or(SelectorUtils.$like("reqNo", keyword), SelectorUtils.$like("subject", keyword)));
                }
                if (StringUtils.isNotBlank(searchType) && StringUtils.isNotBlank(searchKey)) {
                    if (searchType.equals("reqNo")) {
                        selectorList.add(SelectorUtils.$like("reqNo", searchKey));
                    }
                }
                if (searchDate != null) {
                    String sDate = DateFormatUtil.format(searchDate, DateFormatUtil.YEAR_MONTH_DAY_PATTERN);
                    Date startDate = DateFormatUtil.parse(sDate + " 00:00:01", DateFormatUtil.YMDHMS_PATTERN);
                    Date endDate = DateFormatUtil.parse(sDate + " 23:59:59", DateFormatUtil.YMDHMS_PATTERN);
                    selectorList.add(SelectorUtils.$ge("sendDate", startDate));
                    selectorList.add(SelectorUtils.$le("sendDate", endDate));
                }
                if (StringUtils.isNotBlank(orderKey)) {
                    selectorList.add(SelectorUtils.$order(orderKey, isAsc()));
                } else {
                    selectorList.add(SelectorUtils.$order("sendDate", false));
                }
            } else {
                selectorList.add(SelectorUtils.$eq("id", 0l));
            }
        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/req/ingList.ftl", type = Dispatcher.FreeMarker)})
    public String ingList() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            pageObj = this.wfReqService.getPageList(getStart(), 10, searchModeCallbackIngList());
            if (pageObj != null) {
                reqList = pageObj.getResultList();
            }
        }

        return "success";
    }

    private List<Selector> searchModeCallbackAdminList() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("sys");
            userInfo.setLeftMenu("req");
            Long orgId = userInfo.getOrgId();
            if (orgId != null) {
                selectorList.add(SelectorUtils.$eq("orgId.id", orgId));
                if (StringUtils.isNotBlank(keyword)) {
                    selectorList.add(SelectorUtils.$or(SelectorUtils.$like("reqNo", keyword), SelectorUtils.$like("subject", keyword)));
                }
            }
            selectorList.add(SelectorUtils.$order("id", false));
        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/sys/req/list.ftl", type = Dispatcher.FreeMarker)})
    public String adminList() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            pageObj = this.wfReqService.getPageList(getStart(), 10, searchModeCallbackAdminList());
            if (pageObj != null) {
                reqList = pageObj.getResultList();
            }
        }

        return "success";
    }

    private List<Selector> searchModeCallbackFinancialList() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("apply");
            userInfo.setLeftMenu("myFinancial");
            userInfo.setChildMenu("financial");
            Long orgId = userInfo.getOrgId();
            if (orgId != null) {
                selectorList.add(SelectorUtils.$eq("orgId.id", orgId));
                selectorList.add(SelectorUtils.$eq("applyState", 2));
                selectorList.add(SelectorUtils.$eq("applyResult", 1));
                selectorList.add(SelectorUtils.$eq("complete", 1));
                selectorList.add(SelectorUtils.$eq("financialYn", "N"));
                if (StringUtils.isNotBlank(keyword)) {
                    selectorList.add(SelectorUtils.$or(SelectorUtils.$like("reqNo", keyword), SelectorUtils.$like("subject", keyword)));
                }
            }
            selectorList.add(SelectorUtils.$order("id", false));
        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/financial/financialList.ftl", type = Dispatcher.FreeMarker)})
    public String financialList() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            pageObj = this.wfReqService.getPageList(getStart(), 10, searchModeCallbackFinancialList());
            if (pageObj != null) {
                reqList = pageObj.getResultList();
            }
        }

        return "success";
    }

    private List<Selector> searchModeCallbackOverList() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("apply");
            userInfo.setLeftMenu("myFinancial");
            userInfo.setChildMenu("over");
            Long orgId = userInfo.getOrgId();
            if (orgId != null) {
                selectorList.add(SelectorUtils.$eq("orgId.id", orgId));
                selectorList.add(SelectorUtils.$eq("applyState", 2));
                selectorList.add(SelectorUtils.$eq("applyResult", 1));
                selectorList.add(SelectorUtils.$eq("complete", 1));
                selectorList.add(SelectorUtils.$eq("financialYn", "Y"));
                if (StringUtils.isNotBlank(keyword)) {
                    selectorList.add(SelectorUtils.$or(SelectorUtils.$like("reqNo", keyword), SelectorUtils.$like("subject", keyword)));
                }
            }
            selectorList.add(SelectorUtils.$order("id", false));
        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/financial/overList.ftl", type = Dispatcher.FreeMarker)})
    public String overList() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            pageObj = this.wfReqService.getPageList(getStart(), 10, searchModeCallbackOverList());
            if (pageObj != null) {
                reqList = pageObj.getResultList();
            }
        }

        return "success";
    }

    @PageFlow(result = {@Result(name = "success", path = "/wf/req!adminList.dhtml", type = Dispatcher.Redirect)})
    public String delete() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (id != null && userInfo != null) {
            wfReq = this.wfReqService.getById(id);
            if (wfReq != null) {
                List<WfReqNodeApprove> delReqNodeApproveList = this.wfReqNodeApproveService.getNodeListByReqId(userInfo.getOrgId(), wfReq.getId());
                List<WfReqComments> delReqCommentsList = this.wfReqCommentsService.getCommentsListByReqId(wfReq.getId());
                List<WfReqTask> delReqTaskList = this.wfReqTaskService.getTaskListByReqId(userInfo.getOrgId(), wfReq.getId());
                WfReqAdvanceAccount advanceAccount = null;
                WfReqRePayment reqRePayment = null;
                WfReqBusiness reqBusiness = null;
                WfReqDaily reqDaily = null;
                List<WfReqDailyDetail> reqDailyDetailList = null;
                List<WfReqDailyTrue> reqDailyTrueList = null;
                List<WfReqRePaymentDetail> rePaymentDetailList = null;
                List<WfReqAtt> reqAttList = null;
                if (wfReq.getApplyId().equals("ADVANCE_ACCOUNT")) {
                    advanceAccount = this.wfReqAdvanceAccountService.getByReqId(wfReq.getId());
                } else if (wfReq.getApplyId().equals("REPAYMENT")) {
                    reqRePayment = this.wfReqRePaymentService.getByReqId(wfReq.getId());
                    if (reqRePayment != null) {
                        rePaymentDetailList = this.wfReqRePaymentDetailService.getDetailListByRePaymentId(reqRePayment.getId());
                    }
                } else if (wfReq.getApplyId().equals("DAILY")) {
                    reqDaily = this.wfReqDailyService.getByReqId(wfReq.getId());
                    if (reqDaily != null) {
                        reqDailyDetailList = this.wfReqDailyDetailService.getDetailListByDailyId(reqDaily.getId());
                        reqDailyTrueList=this.wfReqDailyTrueService.getDetailListByDailyId(wfReq.getId());
                    }
                } else if (wfReq.getApplyId().equals("BUSINESS")) {
                    reqBusiness = this.wfReqBusinessService.getByReqId(wfReq.getId());
                }
                reqAttList = wfReqAttService.getByReqId(wfReq.getId());
                List<String> attSourceList = null;
                if (reqAttList != null && !reqAttList.isEmpty()) {
                    attSourceList = new ArrayList<String>();
                    for (WfReqAtt wfReqAtt : reqAttList) {
                        if (StringUtils.isNotBlank(wfReqAtt.getSource())) {
                            if (FileManager.isExists(wfReqAtt.getSource())) {
                                attSourceList.add(wfReqAtt.getSource());
                            }
                        }
                    }
                }
                this.wfReqService.delete(wfReq, advanceAccount, reqRePayment, rePaymentDetailList, reqDaily, reqDailyDetailList,reqDailyTrueList, reqBusiness,
                        delReqCommentsList, delReqNodeApproveList, delReqTaskList, reqAttList);
                if (attSourceList != null && !attSourceList.isEmpty()) {
                    for (String source : attSourceList) {
                        FileManager.deleteFile(source);
                    }
                }
            }
        }
        return "success";
    }

    public String getFlowNodeList() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        JSONObject root = new JSONObject();
        root.put("result", "-1");
        if (id != null && userInfo != null) {
            wfReq = this.wfReqService.getById(id);
            if (wfReq != null) {
                List<WfReqNodeApprove> reqNodeApproveList = wfReqNodeApproveService.getNodeListByReqId(userInfo.getOrgId(), wfReq.getId());
                Integer nodeCount = wfReq.getNodeCount();
                if (nodeCount != null) {
                    Integer count = wfReqNodeApproveService.getCountExecutorByReqId(userInfo.getOrgId(), wfReq.getId());
                    if (count == null) {
                        count = 0;
                    }
                    JSONArray array = new JSONArray();
                    JSONObject approve = null;
                    approve = new JSONObject();
                    approve.put("nodeSeq", "0000");
                    approve.put("className", "");
                    approve.put("nodeText", get(wfReq, "userId.userName"));
                    array.add(approve);

                    String nodeText = null;
                    WfReqNodeApprove currentNode = wfReq.getCurrentNode();
                    if (currentNode != null) {
                        for (int i = 1; i <= nodeCount; i++) {
                            List<WfReqNodeApprove> approveList = this.wfReqNodeApproveService.getNodeListByReqIdNodeSeq(userInfo.getOrgId(), wfReq.getId(), i);
                            approve = new JSONObject();
                            approve.put("nodeSeq", i);
                            String className = "";
                            if (i < currentNode.getNodeSeq().intValue()) {
                                className = "badge-success";
                            } else if (i == currentNode.getNodeSeq().intValue()) {
                                className = "badge-important";
                            } else if (i > currentNode.getNodeSeq().intValue()) {
                                className = "";
                            }
                            approve.put("className", className);
                            if (approveList != null && !approveList.isEmpty()) {
                                nodeText = "";
                                for (WfReqNodeApprove nodeApprove : approveList) {
                                    String userId = get(nodeApprove, "userId");
                                    if (StringUtils.isNotBlank(nodeText)) {
                                        nodeText += "&" + get(nodeApprove, "userId.userName");
                                    } else {
                                        nodeText = get(nodeApprove, "userId.userName");
                                    }
                                }
                            }
                            approve.put("nodeText", nodeText);
                            array.add(approve);
                        }
                    }
                    if (count.intValue() > 0) {
                        WfReqNodeApprove nodeApprove = wfReqNodeApproveService.getExecutorByReqId(userInfo.getOrgId(), wfReq.getId());
                        if (nodeApprove != null) {
                            approve = new JSONObject();
                            approve.put("nodeSeq", 9999);
                            String className = "";
                            if (nodeApprove.getNodeSeq().intValue() == currentNode.getNodeSeq().intValue()) {
                                className = "current";
                            } else if (nodeApprove.getNodeSeq().intValue() > currentNode.getNodeSeq().intValue()) {
                                className = "no-ok";
                            }
                            approve.put("className", className);
                            nodeText = get(nodeApprove, "userId.userName");
                            approve.put("nodeText", nodeText + "[执行]");
                            array.add(approve);
                        }
                    }
                    root.put("result", "0");
                    root.put("nodeApproveList", array);
                }
            }
        }
        writeJsonByAction(root.toString());
        return null;
    }
    @PageFlow(result = {@Result(name = "success", path = "/wf/req!ingList.dhtml", type = Dispatcher.Redirect)})
    public String stop() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && id != null) {
            wfReq = this.wfReqService.getById(id);
            if (wfReq != null) {
                wfReq.setComplete(1);
                Date cudDate=DateFormatUtil.getCurrentDate(true);
                wfReq.setCompleteDate(cudDate);
                wfReq.setApplyState(2);
                wfReq.setApplyResult(2);
                wfReq.setTip(0);
                bind(wfReq);

                HrUser user=hrUserService.getById(userInfo.getUserId());
                reqCommentsList=new ArrayList<WfReqComments>();
                WfReqComments comments = new WfReqComments();
                comments.setReqId(wfReq);
                comments.setApprove(0);
                comments.setUserId(user);
                comments.setAction(13);
                comments.setUseYn("Y");
                bind(comments);
                reqCommentsList.add(comments);

                WfReqComments doneComments = new WfReqComments();
                doneComments.setReqId(wfReq);
                doneComments.setApprove(0);
                doneComments.setUserId(user);
                doneComments.setAction(7);
                doneComments.setUseYn("Y");
                bind(doneComments);
                reqCommentsList.add(doneComments);
                this.wfReqService.save(wfReq,reqCommentsList);
            }
        }
        return "success";
    }

    @PageFlow(result = {@Result(name = "ADVANCE_ACCOUNT", path = "/wf/advanceAccount!print.dhtml?reqId=${wfReq.id}", type = Dispatcher.Redirect),
            @Result(name = "REPAYMENT", path = "/wf/rePayment!print.dhtml?reqId=${wfReq.id}", type = Dispatcher.Redirect),
            @Result(name = "DAILY", path = "/wf/daily!print.dhtml?reqId=${wfReq.id}", type = Dispatcher.Redirect),
            @Result(name = "BUSINESS", path = "/wf/business!print.dhtml?reqId=${wfReq.id}", type = Dispatcher.Redirect)})
    public String print() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && id != null) {
            wfReq = this.wfReqService.getById(id);
            if (wfReq != null) {
                applyId = wfReq.getApplyId();
            }
        }
        return applyId;
    }

    @PageFlow(result = {@Result(name = "ADVANCE_ACCOUNT", path = "/wf/advanceAccount!view.dhtml?reqId=${wfReq.id}", type = Dispatcher.Redirect),
            @Result(name = "REPAYMENT", path = "/wf/rePayment!view.dhtml?reqId=${wfReq.id}", type = Dispatcher.Redirect),
            @Result(name = "DAILY", path = "/wf/daily!view.dhtml?reqId=${wfReq.id}", type = Dispatcher.Redirect),
            @Result(name = "BUSINESS", path = "/wf/business!view.dhtml?reqId=${wfReq.id}", type = Dispatcher.Redirect)})
    public String view() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && id != null) {
            userInfo.setTopMenu("apply");
            userInfo.setLeftMenu("myReq");

            wfReq = this.wfReqService.getById(id);
            if (wfReq != null) {
                applyId = wfReq.getApplyId();
                if(wfReq.getApplyResult().intValue()==0){
                    userInfo.setChildMenu("ing");
                }else if(wfReq.getApplyResult().intValue()==1){
                    userInfo.setChildMenu("pass");
                }if(wfReq.getApplyResult().intValue()==2){
                    userInfo.setChildMenu("overrule");
                }
                if (wfReq.getUserId().getId().equals(userInfo.getUserId())) {
                    if (wfReq.getTip().intValue() == 1) {
                        wfReq.setTip(0);
                        bind(wfReq);
                        wfReqService.save(wfReq);
                        Integer reqPassed = wfReqService.getCountPassed(userInfo.getOrgId(), userInfo.getUserId());
                        if (reqPassed == null) {
                            reqPassed = 0;
                        }
                        userInfo.setReqPassed(reqPassed);

                        Integer reqRejected = wfReqService.getCountRejected(userInfo.getOrgId(), userInfo.getUserId());
                        if (reqRejected == null) {
                            reqRejected = 0;
                        }
                        userInfo.setReqRejected(reqRejected);
                    }
                }
            }
        }
        return applyId;
    }

    @PageFlow(result = {@Result(name = "ADVANCE_ACCOUNT", path = "/wf/advanceAccount!financial.dhtml?reqId=${wfReq.id}", type = Dispatcher.Redirect),
            @Result(name = "REPAYMENT", path = "/wf/rePayment!financial.dhtml?reqId=${wfReq.id}", type = Dispatcher.Redirect),
            @Result(name = "DAILY", path = "/wf/daily!financial.dhtml?reqId=${wfReq.id}", type = Dispatcher.Redirect),
            @Result(name = "BUSINESS", path = "/wf/business!financial.dhtml?reqId=${wfReq.id}", type = Dispatcher.Redirect)})
    public String financial() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && id != null) {
            wfReq = this.wfReqService.getById(id);
            if (wfReq != null) {
                applyId = wfReq.getApplyId();
                if (wfReq.getUserId().getId().equals(userInfo.getUserId())) {
                    if (wfReq.getTip().intValue() == 1) {
                        wfReq.setTip(0);
                        bind(wfReq);
                        wfReqService.save(wfReq);
                        Integer reqPassed = wfReqService.getCountPassed(userInfo.getOrgId(), userInfo.getUserId());
                        if (reqPassed == null) {
                            reqPassed = 0;
                        }
                        userInfo.setReqPassed(reqPassed);

                        Integer reqRejected = wfReqService.getCountRejected(userInfo.getOrgId(), userInfo.getUserId());
                        if (reqRejected == null) {
                            reqRejected = 0;
                        }
                        userInfo.setReqRejected(reqRejected);
                    }
                }
            }
        }
        return applyId;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/financial/desc.ftl", type = Dispatcher.FreeMarker)})
    public String desc() throws Exception {
        if (id != null) {
            wfReq = this.wfReqService.getById(id);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String financialSave() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && id != null) {
            wfReq = this.wfReqService.getById(id);
            wfReq.setFinancialYn("Y");
            if (StringUtils.isNotBlank(reason)) {
                wfReq.setFinancialDesc(reason);
            }
            bind(wfReq);
            this.wfReqService.save(wfReq);
        }
        script = "parent.setURL('/wf/req!financialList.dhtml');";
        return "saveSuccess";  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String download() throws Exception {
        if (id != null) {
            WfReqAtt tempAtt = this.wfReqAttService.getById(id);
            if (tempAtt != null) {
                String attchName = StringUtils.toUtf8String(tempAtt.getOldName() + "." + tempAtt.getPostfix());
                File file = FileManager.getFile(tempAtt.getSource(), false);
                download(file, attchName);
            }
        }
        return null;
    }
}
