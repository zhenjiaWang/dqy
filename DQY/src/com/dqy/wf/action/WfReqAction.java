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
    private WfReqTaskService wfReqTaskService;



    @Inject
    private WfReqMyFlowLastService wfReqMyFlowLastService;

    @Inject
    private WfReqMyFlowNodeService wfReqMyFlowNodeService;

    @Inject
    private WfReqMyFlowNodeApproveService wfReqMyFlowNodeApproveService;

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
            userInfo.setLeftMenu("overrule");
            Long orgId = userInfo.getOrgId();
            Long userId = userInfo.getUserId();
            if (orgId!=null&&userId!=null) {
                selectorList.add(SelectorUtils.$eq("orgId.id", orgId));
                selectorList.add(SelectorUtils.$eq("userId.id", userId));
                selectorList.add(SelectorUtils.$eq("applyState", 2));
                selectorList.add(SelectorUtils.$eq("applyResult", 2));
                selectorList.add(SelectorUtils.$eq("complete", 1));
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

    @PageFlow(result = {@Result(name = "success", path = "/view/req/result/overruleList.ftl", type = Dispatcher.FreeMarker)})
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
            userInfo.setLeftMenu("pass");
            Long orgId = userInfo.getOrgId();
            Long userId = userInfo.getUserId();
            if (orgId!=null&&userId!=null) {
                selectorList.add(SelectorUtils.$eq("orgId.id", orgId));
                selectorList.add(SelectorUtils.$eq("userId.id", userId));
                selectorList.add(SelectorUtils.$eq("applyState", 2));
                selectorList.add(SelectorUtils.$eq("applyResult", 1));
                selectorList.add(SelectorUtils.$eq("complete", 1));
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

    @PageFlow(result = {@Result(name = "success", path = "/view/req/result/passList.ftl", type = Dispatcher.FreeMarker)})
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
            userInfo.setLeftMenu("ing");
            Long orgId = userInfo.getOrgId();
            Long userId = userInfo.getUserId();
            if (orgId!=null&&userId!=null) {
                selectorList.add(SelectorUtils.$eq("orgId.id", orgId));
                selectorList.add(SelectorUtils.$eq("userId.id", userId));
                selectorList.add(SelectorUtils.$eq("applyState", 1));
                selectorList.add(SelectorUtils.$eq("applyResult", 0));
                selectorList.add(SelectorUtils.$eq("complete", 0));
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

    @PageFlow(result = {@Result(name = "success", path = "/view/req/result/ingList.ftl", type = Dispatcher.FreeMarker)})
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
            if (orgId!=null) {
                selectorList.add(SelectorUtils.$eq("orgId.id", orgId));
            }
            selectorList.add(SelectorUtils.$order("id", false));
        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/admin/req/list.ftl", type = Dispatcher.FreeMarker)})
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

    @PageFlow(result = {@Result(name = "success", path = "/wf/req!adminList.dhtml", type = Dispatcher.Redirect)})
    public String delete() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (id != null && userInfo != null) {
            wfReq = this.wfReqService.getById(id);
            if (wfReq != null) {
                List<WfReqNodeApprove> delReqNodeApproveList = this.wfReqNodeApproveService.getNodeListByReqId(userInfo.getOrgId(), wfReq.getId());
                List<WfReqComments> delReqCommentsList = this.wfReqCommentsService.getCommentsListByReqId(wfReq.getId());
                List<WfReqTask> delReqTaskList = this.wfReqTaskService.getTaskListByReqId(userInfo.getOrgId(), wfReq.getId());
                this.wfReqService.delete(wfReq,  delReqCommentsList, delReqNodeApproveList, delReqTaskList);
            }
        }
        return "success";
    }



    private WfReqNoSeq buildReqNo(Long orgId, String applyId) throws Exception {
        Date currentDate = DateFormatUtil.getCurrentDate(true);
        int dateYear = DateFormatUtil.getDayInYear(currentDate);
        WfReqNoSeq wfReqNoSeq = wfReqNoSeqService.getCurrentReqNoSeq(orgId, applyId, dateYear);
        if (wfReqNoSeq == null) {
            SysOrg sysOrg=this.sysOrgService.getById(orgId);
            wfReqNoSeq = new WfReqNoSeq();
            wfReqNoSeq.setOrgId(sysOrg);
            wfReqNoSeq.setApplyId(applyId);
            wfReqNoSeq.setDateYear(dateYear);
            wfReqNoSeq.setNextSeq(1);
            wfReqNoSeq.setUseYn("Y");
        } else {
            wfReqNoSeq.setNextSeq(wfReqNoSeq.getNextSeq() + 1);
        }
        bind(wfReqNoSeq);
        return wfReqNoSeq;
    }



    @PageFlow(result = {@Result(name = "success", path = "/wf/req!ingList.dhtml", type = Dispatcher.Redirect)})
    public synchronized String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && applyId != null) {
            if ( wfReq != null) {
                HrUser user=hrUserService.getById(userInfo.getUserId());
                SysOrg sysOrg=this.sysOrgService.getById(userInfo.getOrgId());
                List<WfReqComments> wfReqCommentsList = new ArrayList<WfReqComments>();
                List<WfReqNodeApprove> reqNodeApproveList = null;
                List<WfReqTask> reqTaskList = null;
                WfReqNoSeq wfReqNoSeq = null;
                WfReqComments comments = new WfReqComments();
                comments.setReqId(wfReq);
                comments.setApprove(0);
                comments.setUserId(user);
                comments.setAction(0);
                comments.setUseYn("Y");
                bind(comments);
                wfReqCommentsList.add(comments);

                if (user != null) {
                    defaultSubject = user.getUserName() + "的" + applyId;
                }

                wfReq.setTip(0);
                wfReq.setApplyState(0);
                wfReq.setApplyResult(0);
                wfReq.setComplete(0);
                wfReq.setApplyId(applyId);
                wfReq.setUserId(user);
                wfReq.setOrgId(sysOrg);
                if (wfReq.getExigency() == null) {
                    wfReq.setExigency(0);
                }
                wfReq.setUseYn("Y");
                bind(wfReq);


                wfReqNoSeq = buildReqNo(userInfo.getOrgId(), applyId);
                int next = wfReqNoSeq.getNextSeq();
                String no = null;
                if (next < 10) {
                    no = "000" + next;
                } else if (next < 100) {
                    no = "00" + next;
                } else if (next < 1000) {
                    no = "0" + next;
                } else {
                    no = String.valueOf(next);
                }
                String reqNo = applyId + "-" + wfReqNoSeq.getDateYear() + "-" + no;
                wfReq.setReqNo(reqNo);
                Date currentDate = DateFormatUtil.getCurrentDate(true);
                wfReq.setSendDate(currentDate);
                wfReq.setApplyState(1);
                comments = new WfReqComments();
                comments.setReqId(wfReq);
                comments.setApprove(0);
                comments.setUserId(user);
                comments.setAction(1);
                comments.setUseYn("Y");
                bind(comments);
                wfReqCommentsList.add(comments);
                reqNodeApproveList = new ArrayList<WfReqNodeApprove>();
                if (wfReq.getNodeCount().intValue() > 0) {
                    for (int i = 1; i <= wfReq.getNodeCount(); i++) {
                        Integer nodeSeq = getParameter("nodeSeq" + i, Integer.class);
                        Integer nodeType = getParameter("nodeType" + i, Integer.class);
                        if (nodeSeq != null) {
                            if (nodeType.intValue() == 1) {
                                Long approveId = getParameter("approveId" + i,Long.class);
                                Integer approveType = getParameter("approveType" + i, Integer.class);
                                if (approveId!=null && approveType != null) {
                                    if (approveId.equals(wfReq.getUserId().getId())) {
                                        continue;
                                    }
                                    HrUser approveUser=this.hrUserService.getById(approveId);
                                    if(approveUser!=null){
                                        WfReqNodeApprove nodeApprove = new WfReqNodeApprove();
                                        nodeApprove.setReqId(wfReq);
                                        nodeApprove.setNodeSeq(nodeSeq);
                                        nodeApprove.setNodeType(nodeType);
                                        nodeApprove.setUserId(approveUser);
                                        nodeApprove.setApproveType(approveType);
                                        nodeApprove.setUseYn("Y");
                                        bind(nodeApprove);
                                        reqNodeApproveList.add(nodeApprove);
                                    }
                                }
                            } else if (nodeType.intValue() == 2) {
                                String approveId = getParameter("approveId" + i);
                                String approveType = getParameter("approveType" + i);
                                if (StringUtils.isNotBlank(approveId) && StringUtils.isNotBlank(approveType)) {
                                    String approveIds[] = approveId.split(",");
                                    String approveTypes[] = approveType.split(",");
                                    if (approveIds != null && approveTypes != null) {
                                        if (approveIds.length == approveTypes.length) {
                                            for (int j = 0; j < approveIds.length; j++) {
                                                Long apId = BeanUtils.convertValue(approveIds[j], Long.class);
                                                Integer apType = BeanUtils.convertValue(approveTypes[j], Integer.class);
                                                if (apId!=null && apType != null) {
                                                    if (apId.equals(wfReq.getUserId().getId())) {
                                                        continue;
                                                    }
                                                    HrUser approveUser=this.hrUserService.getById(apId);
                                                    if(approveUser!=null){
                                                        WfReqNodeApprove nodeApprove = new WfReqNodeApprove();
                                                        nodeApprove.setReqId(wfReq);
                                                        nodeApprove.setNodeSeq(nodeSeq);
                                                        nodeApprove.setNodeType(nodeType);
                                                        nodeApprove.setUserId(approveUser);
                                                        nodeApprove.setApproveType(apType);
                                                        nodeApprove.setUseYn("Y");
                                                        bind(nodeApprove);
                                                        reqNodeApproveList.add(nodeApprove);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                for (int i = 9999; i <= 9999; i++) {
                    Integer nodeSeq = getParameter("nodeSeq" + i, Integer.class);
                    Integer nodeType = getParameter("nodeType" + i, Integer.class);
                    if (nodeSeq != null) {
                        if (nodeType.intValue() == 1) {
                            Long approveId = getParameter("approveId" + i,Long.class);
                            Integer approveType = getParameter("approveType" + i, Integer.class);
                            if (approveId!=null && approveType != null) {
                                if (approveId.equals(wfReq.getUserId().getId())) {
                                    continue;
                                }
                                HrUser approveUser=this.hrUserService.getById(approveId);
                                if(approveUser!=null){
                                    WfReqNodeApprove nodeApprove = new WfReqNodeApprove();
                                    nodeApprove.setReqId(wfReq);
                                    nodeApprove.setNodeSeq(nodeSeq);
                                    nodeApprove.setNodeType(nodeType);
                                    nodeApprove.setUserId(approveUser);
                                    nodeApprove.setApproveType(approveType);
                                    nodeApprove.setUseYn("Y");
                                    bind(nodeApprove);
                                    reqNodeApproveList.add(nodeApprove);
                                }
                            }
                        }
                    }
                }

                Integer firstNodeSeq = null;
                List<WfReqNodeApprove> firstNodeApproveList = new ArrayList<WfReqNodeApprove>();
                if (reqNodeApproveList != null && !reqNodeApproveList.isEmpty()) {
                    WfReqNodeApprove reqNodeApprove = reqNodeApproveList.get(0);
                    if (reqNodeApprove != null) {
                        firstNodeApproveList.add(reqNodeApprove);
                        firstNodeSeq = reqNodeApprove.getNodeSeq();
                    }
                }
                if (firstNodeApproveList != null && !firstNodeApproveList.isEmpty()) {
                    if (firstNodeSeq.intValue() != 9999) {
                        reqTaskList = new ArrayList<WfReqTask>();
                        for (WfReqNodeApprove firstNodeApprove : firstNodeApproveList) {
                            if (firstNodeApprove != null) {
                                wfReq.setCurrentNode(firstNodeApprove);
                                comments = new WfReqComments();
                                comments.setReqId(wfReq);
                                comments.setApprove(0);
                                comments.setUserId(firstNodeApprove.getUserId());
                                comments.setAction(2);
                                comments.setUseYn("Y");
                                bind(comments);
                                wfReqCommentsList.add(comments);


                                currentDate = DateFormatUtil.getCurrentDate(true);
                                WfReqTask reqTask = new WfReqTask();
                                reqTask.setReqId(wfReq);
                                reqTask.setUserId(firstNodeApprove.getUserId());
                                reqTask.setOrgId(sysOrg);
                                reqTask.setNodeSeq(firstNodeApprove.getNodeSeq());
                                reqTask.setReceiveDate(currentDate);
                                reqTask.setTaskRead(0);
                                reqTask.setTaskState(0);
                                reqTask.setApproveIdea(0);
                                reqTask.setNodeId(firstNodeApprove);
                                reqTask.setUseYn("Y");
                                bind(reqTask);
                                reqTaskList.add(reqTask);
                            }
                        }
                    } else {
//                        reqExecuteList = new ArrayList<WfReqExecute>();
//                        for (WfReqNodeApprove firstNodeApprove : firstNodeApproveList) {
//                            if (firstNodeApprove != null) {
//                                wfReq.setCurrentNode(firstNodeApprove);
//                                wfReq.setComplete(1);
//                                wfReq.setCompleteDate(currentDate);
//                                wfReq.setApplyState(2);
//                                wfReq.setApplyResult(1);
//
//                                WfReqComments doneComments = new WfReqComments();
//                                doneComments.setReqId(wfReq);
//                                doneComments.setApprove(0);
//                                doneComments.setUserId(wfReq.getUserId());
//                                doneComments.setAction(7);
//                                doneComments.setUseYn("Y");
//                                bind(doneComments);
//                                wfReqCommentsList.add(doneComments);
//
//                                comments = new WfReqComments();
//                                comments.setReqId(wfReq);
//                                comments.setApprove(0);
//                                comments.setUserId(firstNodeApprove.getUserId());
//                                comments.setAction(10);
//                                comments.setUseYn("Y");
//                                bind(comments);
//                                wfReqCommentsList.add(comments);
//
//
//                                currentDate = DateFormatUtil.getCurrentDate(true);
//                                WfReqExecute reqExecute = new WfReqExecute();
//                                reqExecute.setReqId(wfReq);
//                                reqExecute.setUserId(firstNodeApprove.getUserId());
//                                reqExecute.setCompanyId(userInfo.getCompanyId());
//                                reqExecute.setReceiveDate(currentDate);
//                                reqExecute.setExecuteRead(0);
//                                reqExecute.setExecuteState(0);
//                                reqExecute.setNodeId(firstNodeApprove);
//                                reqExecute.setUseYn("Y");
//                                bind(reqExecute);
//                                reqExecuteList.add(reqExecute);
//                            }
//                        }
                    }
                }
                WfReqMyFlowLast wfReqMyFlowLast = null;
                if (flowId != null) {
                    WfReqMyFlow reqMyFlow = null;
                    reqMyFlow = this.wfReqMyFlowService.getById(flowId);
                    if (reqMyFlow != null) {
                        wfReqMyFlowLast = wfReqMyFlowLastService.getByApplyId(userInfo.getOrgId(),applyId, userInfo.getUserId());
                        if (wfReqMyFlowLast == null) {
                            wfReqMyFlowLast = new WfReqMyFlowLast();
                            wfReqMyFlowLast.setApplyId(applyId);
                            wfReqMyFlowLast.setUserId(user);
                            wfReqMyFlowLast.setUseYn("Y");
                        }
                        wfReqMyFlowLast.setFlowId(reqMyFlow);
                        bind(wfReqMyFlowLast);
                    }
                }
                this.wfReqService.save(wfReq,  wfReqCommentsList, wfReqNoSeq, reqNodeApproveList, reqTaskList, wfReqMyFlowLast);
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
                List<WfReqNodeApprove> reqNodeApproveList = wfReqNodeApproveService.getNodeListByReqId(userInfo.getOrgId(),wfReq.getId());
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
                            List<WfReqNodeApprove> approveList = this.wfReqNodeApproveService.getNodeListByReqIdNodeSeq(userInfo.getOrgId(),wfReq.getId(), i);
                            approve = new JSONObject();
                            approve.put("nodeSeq", i);
                            String className = "";
                            if (i == currentNode.getNodeSeq().intValue()) {
                                className = "current";
                            } else if (i > currentNode.getNodeSeq().intValue()) {
                                className = "no-ok";
                            }
                            approve.put("className", className);
                            if (approveList != null && !approveList.isEmpty()) {
                                nodeText = "";
                                for (WfReqNodeApprove nodeApprove : approveList) {
                                    String userId = get(nodeApprove, "userId");
                                    if (StringUtils.isNotBlank(nodeText)) {
                                        nodeText += "&" + get(nodeApprove, "userId.userName");
                                    } else {
                                        nodeText = get(nodeApprove,"userId.userName");
                                    }
                                }
                            }
                            approve.put("nodeText", nodeText);
                            array.add(approve);
                        }
                    }
                    if (count.intValue() > 0) {
                        WfReqNodeApprove nodeApprove = wfReqNodeApproveService.getExecutorByReqId(userInfo.getOrgId(),wfReq.getId());
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
}
