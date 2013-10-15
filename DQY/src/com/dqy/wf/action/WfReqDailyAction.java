package com.dqy.wf.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.sys.entity.SysBudgetTitle;
import com.dqy.sys.entity.SysBudgetType;
import com.dqy.sys.service.SysBudgetTitleService;
import com.dqy.sys.service.SysBudgetTypeService;
import com.dqy.sys.service.SysOrgService;
import com.dqy.wf.entity.*;
import com.dqy.wf.service.*;
import com.google.inject.Inject;
import org.guiceside.persistence.entity.search.SelectorUtils;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;
import org.guiceside.web.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjiaWang
 * Date: 12-7-12
 * Time: 下午9:49
 * To change this template use File | Settings | File Templates.
 */
@Action(name = "daily", namespace = "/wf")
public class WfReqDailyAction extends WfReqSupportAction<WfReqDaily> {

    @Inject
    private WfVariableGlobalService wfVariableGlobalService;

    @Inject
    private WfReqService wfReqService;

    @Inject
    private WfReqDailyService wfReqDailyService;

    @Inject
    private WfReqDailyDetailService wfReqDailyDetailService;

    @Inject
    private WfReqCommentsService wfReqCommentsService;

    @Inject
    private SysBudgetTypeService sysBudgetTypeService;

    @Inject
    private SysBudgetTitleService sysBudgetTitleService;

    @Inject
    private SysOrgService sysOrgService;

    @Inject
    private WfReqTaskService wfReqTaskService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private WfReqDaily wfReqDaily;

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
    private Integer detailCount;


    @ReqSet
    public final String applyId = "DAILY";


    @ReqSet
    private WfReqTask wfReqTask;

    @ReqSet
    private List<WfReqComments> reqCommentsList;

    @ReqSet
    private List<SysBudgetTitle> titleList;

    @ReqSet
    private List<SysBudgetType> typeList;

    @ReqSet
    private List<WfReqDailyDetail> detailList;


    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/wf/daily/input.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("apply");
            userInfo.setLeftMenu("daily");
            userInfo.setChildMenu(null);

            List<Selector> selectorList = new ArrayList<Selector>();
            selectorList.add(SelectorUtils.$eq("orgId.id", userInfo.getOrgId()));
            selectorList.add(SelectorUtils.$eq("deptId.id", userInfo.getDepartmentId()));
            selectorList.add(SelectorUtils.$eq("useYn", "Y"));
            selectorList.add(SelectorUtils.$order("expenseType"));
            typeList = sysBudgetTypeService.getAllList(selectorList);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/wf/req!ingList.dhtml", type = Dispatcher.Redirect)})
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && wfReqDaily != null) {
            initReq();
            Double totalAmount = 0.00d;

            if (detailCount != null) {
                detailList = new ArrayList<WfReqDailyDetail>();
                for (int i = 1; i <= detailCount; i++) {
                    Long typeId = getParameter("typeId" + i, Long.class);
                    Long titleId = getParameter("titleId" + i, Long.class);
                    Double amount = getParameter("amount" + i, Double.class);
                    String remarks = getParameter("remarks" + i);
                    if (typeId != null && titleId != null && amount != null) {
                        if (amount == null) {
                            amount = 0.00d;
                        }
                        SysBudgetType budgetType = this.sysBudgetTypeService.getById(typeId);
                        SysBudgetTitle budgetTitle = this.sysBudgetTitleService.getById(titleId);
                        if (budgetTitle != null && budgetType != null) {
                            WfReqDailyDetail dailyDetail = new WfReqDailyDetail();
                            dailyDetail.setDailyId(wfReqDaily);
                            dailyDetail.setExpenseType(budgetType);
                            dailyDetail.setExpenseTitle(budgetTitle);
                            dailyDetail.setAmount(amount);
                            dailyDetail.setRemarks(remarks);
                            bind(dailyDetail);
                            dailyDetail.setUseYn("Y");
                            totalAmount += amount;
                            detailList.add(dailyDetail);
                        }
                    }
                }
            }
            wfReqDaily.setAmount(totalAmount);
            wfReqDaily.setReqId(wfReq);
            wfReqDaily.setUseYn("Y");
            bind(wfReqDaily);
            this.wfReqDailyService.save(wfReqDaily, detailList, wfReq, wfReqCommentsList, wfReqNoSeq, reqNodeApproveList, reqTaskList, wfReqMyFlowLast);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/daily/view.ftl", type = Dispatcher.FreeMarker)})
    public String view() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && reqId != null) {
            wfReqDaily = this.wfReqDailyService.getByReqId(reqId);
            if (wfReqDaily != null) {
                detailList = this.wfReqDailyDetailService.getDetailListByDailyId(wfReqDaily.getId());
                wfReq = wfReqDaily.getReqId();
                if (wfReq != null) {
                    reqCommentsList = this.wfReqCommentsService.getCommentsListByReqId(wfReq.getId());
                }
            }
        }
        return "success";
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/daily/process.ftl", type = Dispatcher.FreeMarker)})
    public String process() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && taskId != null) {
            wfReqTask = this.wfReqTaskService.getById(taskId);
            if (wfReqTask != null) {
                wfReq = wfReqTask.getReqId();
                if (wfReq != null) {
                    wfReqDaily = this.wfReqDailyService.getByReqId(wfReq.getId());
                    if (wfReqDaily != null) {
                        detailList = this.wfReqDailyDetailService.getDetailListByDailyId(wfReqDaily.getId());
                    }
                    reqCommentsList = this.wfReqCommentsService.getCommentsListByReqId(wfReq.getId());
                }
            }
        }
        return "success";
    }
}
