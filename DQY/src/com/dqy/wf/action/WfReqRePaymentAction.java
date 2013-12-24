package com.dqy.wf.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.hr.entity.HrDepartment;
import com.dqy.hr.service.HrDepartmentService;
import com.dqy.sys.entity.SysBudgetTitle;
import com.dqy.sys.entity.SysBudgetType;
import com.dqy.sys.service.SysBudgetAmountService;
import com.dqy.sys.service.SysBudgetTitleService;
import com.dqy.sys.service.SysBudgetTypeService;
import com.dqy.sys.service.SysOrgService;
import com.dqy.wf.entity.*;
import com.dqy.wf.service.*;
import com.google.inject.Inject;
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
@Action(name = "rePayment", namespace = "/wf")
public class WfReqRePaymentAction extends WfReqSupportAction<WfReqRePayment> {

    @Inject
    private WfVariableGlobalService wfVariableGlobalService;
    @Inject
    private WfReqDailyDetailService wfReqDailyDetailService;

    @Inject
    private SysBudgetAmountService sysBudgetAmountService;

    @Inject
    private WfReqService wfReqService;

    @Inject
    private WfReqAdvanceAccountService wfReqAdvanceAccountService;

    @Inject
    private WfReqRePaymentService wfReqRePaymentService;

    @Inject
    private WfReqRePaymentDetailService wfReqRePaymentDetailService;

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
    private WfReqRePayment wfReqRePayment;

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


    @ReqSet
    public final String applyId="REPAYMENT";



    @ReqSet
    private WfReqTask wfReqTask;

    @ReqSet
    private List<WfReqComments> reqCommentsList;

    @ReqSet
    private List<SysBudgetTitle> titleList;

    @ReqSet
    private List<SysBudgetType> typeList;

    @ReqSet
    private List<WfReqRePaymentDetail> detailList;

    @ReqSet
    private List<WfReqAdvanceAccount> reqAdvanceAccountList;

    @ReqSet
    private Date sendDate;


    @ReqSet
    private List<HrDepartment> departmentList;

    @ReqSet
    private List<WfReqAtt> reqAttList;


    @ReqSet
    private Double totalAmount;

    @ReqSet
    private Double totalPassAmount;

    @ReqSet
    private Double totalIngAmount;

    @ReqSet
    private Double remnantAmount;

    @ReqSet
    private String applyName;

    @ReqSet
    private List<Integer> yearList;


    @ReqSet
    private Integer currentYear;



    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/wf/rePayment/input.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("apply");
            userInfo.setLeftMenu("rePayment");
            userInfo.setChildMenu(null);
            sendDate= DateFormatUtil.getCurrentDate(true);

            deptId=userInfo.getDepartmentId();
            List<Selector> selectorList=new ArrayList<Selector>();
            selectorList.add(SelectorUtils.$eq("orgId.id",userInfo.getOrgId()));
            selectorList.add(SelectorUtils.$order("expenseType"));
            selectorList.add(SelectorUtils.$eq("useYn","Y"));
            typeList= this.sysBudgetTypeService.getAllList(selectorList);

            selectorList=new ArrayList<Selector>();
            selectorList.add(SelectorUtils.$eq("orgId.id",userInfo.getOrgId()));
            selectorList.add(SelectorUtils.$order("titleName"));
            selectorList.add(SelectorUtils.$eq("useYn","Y"));
            titleList= this.sysBudgetTitleService.getAllList(selectorList);

            Date currentDate = DateFormatUtil.getCurrentDate(false);
            if (currentYear == null) {
                currentYear = DateFormatUtil.getDayInYear(currentDate);
            }
            yearList = new ArrayList<Integer>();
            yearList.add(currentYear - 1);
            yearList.add(currentYear);
            yearList.add(currentYear + 1);

            reqAdvanceAccountList=this.wfReqAdvanceAccountService.getListByReUserId(userInfo.getOrgId(),userInfo.getUserId());
            if(reqAdvanceAccountList!=null&&!reqAdvanceAccountList.isEmpty()){
                for(WfReqAdvanceAccount advanceAccount:reqAdvanceAccountList){
                    Double reAmount= wfReqRePaymentService.getSumByReAmount(userInfo.getOrgId(),userInfo.getUserId(),advanceAccount.getId());
                    if(reAmount==null){
                        reAmount=0.00d;
                    }
                    advanceAccount.setReAmount(reAmount);
                }
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/wf/req!ingList.dhtml", type = Dispatcher.Redirect)})
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null&&wfReqRePayment!=null) {
            initReq();
            Double totalAmount=0.00d;

            if(detailCount!=null){
                detailList=new ArrayList<WfReqRePaymentDetail>();
                for(int i=1;i<=detailCount;i++){
                    Long deptId = getParameter("deptId" + i, Long.class);
                    Long typeId=getParameter("typeId"+i,Long.class);
                    Long titleId=getParameter("titleId"+i,Long.class);
                    Double amount=getParameter("amount"+i,Double.class);
                    String dateStr=getParameter("date"+i);
                    String remarks=getParameter("remarks"+i);
                    if(typeId!=null&&titleId!=null&&amount!=null&& StringUtils.isNotBlank(dateStr)){
                        if(amount==null){
                            amount=0.00d;
                        }
                        Date date=DateFormatUtil.parse(dateStr,DateFormatUtil.YEAR_MONTH_DAY_PATTERN);
                        HrDepartment hrDepartment = this.hrDepartmentService.getById(deptId);
                        SysBudgetType budgetType=this.sysBudgetTypeService.getById(typeId);
                        SysBudgetTitle budgetTitle=this.sysBudgetTitleService.getById(titleId);
                        if(hrDepartment!=null&&budgetTitle!=null&&budgetType!=null&&date!=null){
                            WfReqRePaymentDetail paymentDetail=new WfReqRePaymentDetail();
                            paymentDetail.setRePaymentId(wfReqRePayment);
                            paymentDetail.setExpenseDept(hrDepartment);
                            paymentDetail.setExpenseType(budgetType);
                            paymentDetail.setExpenseTitle(budgetTitle);
                            paymentDetail.setAmount(amount);
                            paymentDetail.setAmountDate(date);
                            paymentDetail.setRemarks(remarks);
                            bind(paymentDetail);
                            paymentDetail.setUseYn("Y");
                            totalAmount+=amount;
                            detailList.add(paymentDetail);
                        }
                    }
                }
            }
            wfReqRePayment.setAmount(totalAmount);
            wfReqRePayment.setReqId(wfReq);
            wfReqRePayment.setUseYn("Y");
            bind(wfReqRePayment);
            this.wfReqRePaymentService.save(wfReqRePayment,detailList,wfReq,  wfReqCommentsList, wfReqNoSeq, reqNodeApproveList, reqTaskList, wfReqMyFlowLast,reqAttList);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/rePayment/print.ftl", type = Dispatcher.FreeMarker)})
    public String print() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && reqId != null) {
            wfReqRePayment=this.wfReqRePaymentService.getByReqId(reqId);
            if(wfReqRePayment!=null){
                detailList=this.wfReqRePaymentDetailService.getDetailListByRePaymentId(wfReqRePayment.getId());
                wfReq = wfReqRePayment.getReqId();
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

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/rePayment/view.ftl", type = Dispatcher.FreeMarker)})
    public String view() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null&&reqId!=null) {
            wfReqRePayment=this.wfReqRePaymentService.getByReqId(reqId);
            if(wfReqRePayment!=null){
                detailList=this.wfReqRePaymentDetailService.getDetailListByRePaymentId(wfReqRePayment.getId());
                wfReq=wfReqRePayment.getReqId();
                if(wfReq!=null){
                    reqCommentsList = this.wfReqCommentsService.getCommentsListByReqId(wfReq.getId());
                    reqAttList=wfReqAttService.getByReqId(wfReq.getId());
                }
            }
        }
        return "success";
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/rePayment/financial.ftl", type = Dispatcher.FreeMarker)})
    public String financial() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null&&reqId!=null) {
            wfReqRePayment=this.wfReqRePaymentService.getByReqId(reqId);
            if(wfReqRePayment!=null){
                detailList=this.wfReqRePaymentDetailService.getDetailListByRePaymentId(wfReqRePayment.getId());
                wfReq=wfReqRePayment.getReqId();
                if(wfReq!=null){
                    reqCommentsList = this.wfReqCommentsService.getCommentsListByReqId(wfReq.getId());
                    reqAttList=wfReqAttService.getByReqId(wfReq.getId());

                    HrDepartment hrDepartment=wfReq.getUserId().getDeptId();
                    if(hrDepartment!=null){
                        totalBudgetAmount(hrDepartment,wfReqRePayment.getBudgetYear(),userInfo);
                    }
                }
            }
        }
        return "success";
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/rePayment/process.ftl", type = Dispatcher.FreeMarker)})
    public String process() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null&&taskId!=null) {
            wfReqTask=this.wfReqTaskService.getById(taskId);
            if(wfReqTask!=null){
                wfReq=wfReqTask.getReqId();
                if(wfReq!=null){
                    wfReqRePayment=this.wfReqRePaymentService.getByReqId(wfReq.getId());
                    if(wfReqRePayment!=null){
                        detailList=this.wfReqRePaymentDetailService.getDetailListByRePaymentId(wfReqRePayment.getId());
                    }
                    reqCommentsList = this.wfReqCommentsService.getCommentsListByReqId(wfReq.getId());
                    reqAttList=wfReqAttService.getByReqId(wfReq.getId());

                    HrDepartment hrDepartment=wfReq.getUserId().getDeptId();
                    if(hrDepartment!=null){
                        totalBudgetAmount(hrDepartment,wfReqRePayment.getBudgetYear(),userInfo);
                    }
                }
            }
        }
        return "success";
    }

    private void totalBudgetAmount(HrDepartment hrDepartment,Integer budgetYear,UserInfo userInfo){
        if(hrDepartment!=null&&budgetYear!=null){
            totalAmount=sysBudgetAmountService.geTotalAmount(userInfo.getOrgId(),budgetYear,hrDepartment.getId());
            if(totalAmount==null){
                totalAmount=0.00d;
            }
            String startDateStr=budgetYear+"-01-01 00:00:01";
            String endDateStr=budgetYear+"-12-31 23:23:59";
            Date startDate=DateFormatUtil.parse(startDateStr,DateFormatUtil.YMDHMS_PATTERN);
            Date endDate=DateFormatUtil.parse(endDateStr,DateFormatUtil.YMDHMS_PATTERN);
            Double dailyIng=this.wfReqDailyDetailService.getSumAmountByIng(userInfo.getOrgId(),hrDepartment.getId(),startDate,endDate);
            Double dailyPass=this.wfReqDailyDetailService.getSumAmountByPass(userInfo.getOrgId(),hrDepartment.getId(),startDate,endDate);

            Double rePaymentIng=this.wfReqRePaymentDetailService.getSumAmountByIng(userInfo.getOrgId(),hrDepartment.getId(),startDate,endDate);
            Double rePaymentPass=this.wfReqRePaymentDetailService.getSumAmountByPass(userInfo.getOrgId(),hrDepartment.getId(),startDate,endDate);
            if(dailyIng==null){
                dailyIng=0.00d;
            }
            if(dailyPass==null){
                dailyPass=0.00d;
            }
            if(rePaymentIng==null){
                rePaymentIng=0.00d;
            }
            if(rePaymentPass==null){
                rePaymentPass=0.00d;
            }
            totalIngAmount=dailyIng+rePaymentIng;

            totalPassAmount=dailyPass+rePaymentPass;

            remnantAmount=totalPassAmount-totalAmount;
            if(remnantAmount.doubleValue()<0){
                remnantAmount=0.00d;
            }
        }
    }
}
