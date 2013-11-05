package com.dqy.wf.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.hr.entity.HrDepartment;
import com.dqy.hr.service.HrDepartmentService;
import com.dqy.sys.entity.SysBudgetTitle;
import com.dqy.sys.entity.SysBudgetType;
import com.dqy.sys.service.SysBudgetTitleService;
import com.dqy.sys.service.SysBudgetTypeService;
import com.dqy.sys.service.SysOrgService;
import com.dqy.wf.entity.*;
import com.dqy.wf.service.*;
import com.google.inject.Inject;
import org.guiceside.commons.TimeUtils;
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
    private WfReqDailyTrueService wfReqDailyTrueService;

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

    @Inject
    private WfReqAttService wfReqAttService;

    @Inject
    private HrDepartmentService hrDepartmentService;

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
    private Long deptId;

    @ReqGet
    @ReqSet
    private Long taskId;

    @ReqGet
    private Integer detailCount;

    @ReqGet
    private Integer detailCount1;

    @ReqGet
    private Integer detailCount2;

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

    @ReqSet
    private List<WfReqDailyTrue> trueList;

    @ReqSet
    private List<HrDepartment> departmentList;

    @ReqSet
    private Date sendDate;

    @ReqGet
    @ReqSet
    private String trueAmount;

    @ReqSet
    private List<WfReqAtt> reqAttList;


    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/wf/daily/input.ftl", type = Dispatcher.FreeMarker),
            @Result(name = "true", path = "/view/wf/daily/inputTrue.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("apply");
            userInfo.setLeftMenu("daily");
            userInfo.setChildMenu(null);
            sendDate= DateFormatUtil.getCurrentDate(true);
            deptId=userInfo.getDepartmentId();
            List<Selector> selectorList=new ArrayList<Selector>();
            selectorList.add(SelectorUtils.$eq("orgId.id",userInfo.getOrgId()));
            selectorList.add(SelectorUtils.$order("deptNo"));
            selectorList.add(SelectorUtils.$eq("useYn","Y"));
            departmentList = this.hrDepartmentService.getByList(selectorList);
        }
        if(StringUtils.isNotBlank(trueAmount)){
            return "true";
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
                    Long deptId = getParameter("deptId" + i, Long.class);
                    Long typeId = getParameter("typeId" + i, Long.class);
                    Long titleId = getParameter("titleId" + i, Long.class);
                    Double amount = getParameter("amount" + i, Double.class);
                    String remarks = getParameter("remarks" + i);
                    String dateStr=getParameter("date"+i);
                    if (deptId!=null&&typeId != null && titleId != null && amount != null&& StringUtils.isNotBlank(dateStr)) {
                        if (amount == null) {
                            amount = 0.00d;
                        }
                        Date date=DateFormatUtil.parse(dateStr,DateFormatUtil.YEAR_MONTH_DAY_PATTERN);
                        HrDepartment hrDepartment = this.hrDepartmentService.getById(deptId);
                        SysBudgetType budgetType = this.sysBudgetTypeService.getById(typeId);
                        SysBudgetTitle budgetTitle = this.sysBudgetTitleService.getById(titleId);
                        if (hrDepartment!=null&&budgetTitle != null && budgetType != null&&date!=null) {
                            WfReqDailyDetail dailyDetail = new WfReqDailyDetail();
                            dailyDetail.setDailyId(wfReqDaily);
                            dailyDetail.setExpenseDept(hrDepartment);
                            dailyDetail.setExpenseType(budgetType);
                            dailyDetail.setExpenseTitle(budgetTitle);
                            dailyDetail.setAmount(amount);
                            dailyDetail.setAmountDate(date);
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
            wfReqDaily.setTrueAmount(0.00d);
            wfReqDaily.setReqId(wfReq);
            wfReqDaily.setUseYn("Y");
            bind(wfReqDaily);
            this.wfReqDailyService.save(wfReqDaily, detailList, wfReq, wfReqCommentsList, wfReqNoSeq, reqNodeApproveList, reqTaskList, wfReqMyFlowLast,reqAttList);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/wf/req!ingList.dhtml", type = Dispatcher.Redirect)})
    public String saveTrue() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && wfReqDaily != null) {
            initReq();
            Double totalAmount = 0.00d;
            Double trueAmount = 0.00d;
            if (detailCount1 != null&&detailCount2!=null) {
                detailList = new ArrayList<WfReqDailyDetail>();
                trueList=new ArrayList<WfReqDailyTrue>();
                String type="1";
                for (int i = 1; i <= detailCount1; i++) {
                    Long deptId = getParameter("deptId" + i+"_"+type, Long.class);
                    Long typeId = getParameter("typeId" + i+"_"+type, Long.class);
                    Long titleId = getParameter("titleId" + i+"_"+type, Long.class);
                    Double amount = getParameter("amount" + i+"_"+type, Double.class);
                    String remarks = getParameter("remarks" + i+"_"+type);
                    String dateStr=getParameter("date"+i+"_"+type);
                    if (deptId!=null&&typeId != null && titleId != null && amount != null&& StringUtils.isNotBlank(dateStr)) {
                        if (amount == null) {
                            amount = 0.00d;
                        }
                        Date date=DateFormatUtil.parse(dateStr,DateFormatUtil.YEAR_MONTH_DAY_PATTERN);
                        HrDepartment hrDepartment = this.hrDepartmentService.getById(deptId);
                        SysBudgetType budgetType = this.sysBudgetTypeService.getById(typeId);
                        SysBudgetTitle budgetTitle = this.sysBudgetTitleService.getById(titleId);
                        if (hrDepartment!=null&&budgetTitle != null && budgetType != null&&date!=null) {
                            WfReqDailyDetail dailyDetail = new WfReqDailyDetail();
                            dailyDetail.setDailyId(wfReqDaily);
                            dailyDetail.setExpenseDept(hrDepartment);
                            dailyDetail.setExpenseType(budgetType);
                            dailyDetail.setExpenseTitle(budgetTitle);
                            dailyDetail.setAmount(amount);
                            dailyDetail.setAmountDate(date);
                            dailyDetail.setRemarks(remarks);
                            bind(dailyDetail);
                            dailyDetail.setUseYn("Y");
                            totalAmount += amount;
                            detailList.add(dailyDetail);
                        }
                    }
                }
                type="2";
                for (int i = 1; i <= detailCount2; i++) {
                    Long deptId = getParameter("deptId" + i+"_"+type, Long.class);
                    Long typeId = getParameter("typeId" + i+"_"+type, Long.class);
                    Long titleId = getParameter("titleId" + i+"_"+type, Long.class);
                    Double amount = getParameter("amount" + i+"_"+type, Double.class);
                    String remarks = getParameter("remarks" + i+"_"+type);
                    String dateStr=getParameter("date"+i+"_"+type);
                    if (deptId!=null&&typeId != null && titleId != null && amount != null&& StringUtils.isNotBlank(dateStr)) {
                        if (amount == null) {
                            amount = 0.00d;
                        }
                        Date date=DateFormatUtil.parse(dateStr,DateFormatUtil.YEAR_MONTH_DAY_PATTERN);
                        HrDepartment hrDepartment = this.hrDepartmentService.getById(deptId);
                        SysBudgetType budgetType = this.sysBudgetTypeService.getById(typeId);
                        SysBudgetTitle budgetTitle = this.sysBudgetTitleService.getById(titleId);
                        if (hrDepartment!=null&&budgetTitle != null && budgetType != null&&date!=null) {
                            WfReqDailyTrue dailyTrue=new WfReqDailyTrue();
                            dailyTrue.setDailyId(wfReqDaily);
                            dailyTrue.setExpenseDept(hrDepartment);
                            dailyTrue.setExpenseType(budgetType);
                            dailyTrue.setExpenseTitle(budgetTitle);
                            dailyTrue.setAmount(amount);
                            dailyTrue.setAmountDate(date);
                            dailyTrue.setRemarks(remarks);
                            bind(dailyTrue);
                            dailyTrue.setUseYn("Y");
                            trueAmount += amount;
                            trueList.add(dailyTrue);
                        }
                    }
                }
            }
            wfReqDaily.setAmount(totalAmount);
            wfReqDaily.setTrueAmount(trueAmount);
            wfReqDaily.setReqId(wfReq);
            wfReqDaily.setUseYn("Y");
            bind(wfReqDaily);
            this.wfReqDailyService.save(wfReqDaily, detailList,trueList, wfReq, wfReqCommentsList, wfReqNoSeq, reqNodeApproveList, reqTaskList, wfReqMyFlowLast,reqAttList);
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
                trueList=this.wfReqDailyTrueService.getDetailListByDailyId(wfReqDaily.getId());
                wfReq = wfReqDaily.getReqId();
                if (wfReq != null) {
                    reqCommentsList = this.wfReqCommentsService.getCommentsListByReqId(wfReq.getId());
                    reqAttList=wfReqAttService.getByReqId(wfReq.getId());
                }
            }
        }
        return "success";
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/daily/financial.ftl", type = Dispatcher.FreeMarker)})
    public String financial() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && reqId != null) {
            wfReqDaily = this.wfReqDailyService.getByReqId(reqId);
            if (wfReqDaily != null) {
                detailList = this.wfReqDailyDetailService.getDetailListByDailyId(wfReqDaily.getId());
                trueList=this.wfReqDailyTrueService.getDetailListByDailyId(wfReqDaily.getId());
                wfReq = wfReqDaily.getReqId();
                if (wfReq != null) {
                    reqCommentsList = this.wfReqCommentsService.getCommentsListByReqId(wfReq.getId());
                    reqAttList=wfReqAttService.getByReqId(wfReq.getId());
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
                        trueList=this.wfReqDailyTrueService.getDetailListByDailyId(wfReqDaily.getId());
                    }
                    reqCommentsList = this.wfReqCommentsService.getCommentsListByReqId(wfReq.getId());
                    reqAttList=wfReqAttService.getByReqId(wfReq.getId());
                }
            }
        }
        return "success";
    }
}
