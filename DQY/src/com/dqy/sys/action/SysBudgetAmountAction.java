package com.dqy.sys.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.hr.entity.HrDepartment;
import com.dqy.hr.service.HrDepartmentService;
import com.dqy.sys.entity.*;
import com.dqy.sys.service.*;
import com.dqy.web.support.ActionSupport;
import com.dqy.wf.entity.WfReqDaily;
import com.dqy.wf.entity.WfReqDailyDetail;
import com.dqy.wf.entity.WfReqRePaymentDetail;
import com.dqy.wf.service.WfReqDailyDetailService;
import com.dqy.wf.service.WfReqRePaymentDetailService;
import com.google.inject.Inject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.guiceside.commons.lang.DateFormatUtil;
import org.guiceside.persistence.entity.search.SelectorUtils;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;
import org.guiceside.web.annotation.*;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjiaWang
 * Date: 12-7-12
 * Time: 下午9:49
 * To change this template use File | Settings | File Templates.
 */
@Action(name = "budgetAmount", namespace = "/sys")
public class SysBudgetAmountAction extends ActionSupport<SysBudgetAmount> {

    @Inject
    private SysBudgetAmountService sysBudgetAmountService;

    @Inject
    private SysBudgetTypeService sysBudgetTypeService;

    @Inject
    private WfReqDailyDetailService wfReqDailyDetailService;

    @Inject
    private WfReqRePaymentDetailService wfReqRePaymentDetailService;

    @Inject
    private SysFinancialTitleService sysFinancialTitleService;

    @Inject
    private SysOrgService sysOrgService;

    @Inject
    private HrDepartmentService hrDepartmentService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private SysBudgetAmount sysBudgetAmount;

    @ReqSet
    private HrDepartment hrDepartment;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    private String approveBudget;

    @ReqGet
    @ReqSet
    private Long deptId;

    @ReqGet
    @ReqSet
    private Integer rows;

    @ReqGet
    @ReqSet
    private Integer currentYear;

    @ReqSet
    private Double totalAmount;

    @ReqSet
    private Double totalPassAmount;

    @ReqSet
    private Double totalIngAmount;

    @ReqSet
    private Double remnantAmount;
    @ReqSet
    private List<Integer> yearList;

    @ReqSet
    private List<SysBudgetType> budgetTypeList;

    @ReqSet
    private List<SysBudgetAmount> sysBudgetAmountList;

    @ReqSet
    private List<SysFinancialTitle> titleList;

    @ReqSet
    private List<SysBudgetType> typeList;
    @ReqSet
    private List<HrDepartment> departmentList;

    @ReqSet
    private Set<String> idSets;


    @ReqSet
    private Map<String, List<SysBudgetTitle>> budgetTitleMap;

    @ReqSet
    private Map<String, Double> budgetAmountMap;



    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/sys/budgetAmount/index.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            totalAmount = 0.00d;
            userInfo.setTopMenu("budget");
            userInfo.setLeftMenu("budgetAmount");
            Date currentDate = DateFormatUtil.getCurrentDate(false);
            if (currentYear == null) {
                currentYear = DateFormatUtil.getDayInYear(currentDate);
            }
            yearList = new ArrayList<Integer>();
            yearList.add(currentYear - 1);
            yearList.add(currentYear);
            yearList.add(currentYear + 1);
            yearList.add(currentYear + 2);

            List<Selector> selectorList=new ArrayList<Selector>();
            selectorList.add(SelectorUtils.$eq("orgId.id",userInfo.getOrgId()));
            selectorList.add(SelectorUtils.$order("expenseType"));
            selectorList.add(SelectorUtils.$eq("useYn","Y"));
            typeList= this.sysBudgetTypeService.getAllList(selectorList);




            hrDepartment = this.hrDepartmentService.getById(userInfo.getDepartmentId());

            rows=0;
            sysBudgetAmountList=this.sysBudgetAmountService.getAmountList(userInfo.getOrgId(),currentYear,hrDepartment.getId());
            if(sysBudgetAmountList!=null&&!sysBudgetAmountList.isEmpty()){
                if(sysBudgetAmount==null){
                    sysBudgetAmount=sysBudgetAmountList.get(0);
                }
                idSets=new HashSet<String>();
                budgetAmountMap=new HashMap<String, Double>();
                for(SysBudgetAmount budgetAmount:sysBudgetAmountList){
                    idSets.add(get(budgetAmount,"typeId.id")+"_"+get(budgetAmount,"titleId.id")+"_"+get(budgetAmount,"titleId.titleName"));
                    budgetAmountMap.put(get(budgetAmount,"typeId.id")+"_"+get(budgetAmount,"titleId.id")+"_"+budgetAmount.getMonth(),
                            budgetAmount.getAmount());
                }
                if(idSets!=null&&!idSets.isEmpty()){
                    rows=idSets.size();
                }
            }
            totalAmount=sysBudgetAmountService.geTotalAmount(userInfo.getOrgId(),currentYear,hrDepartment.getId());
            if(totalAmount==null){
                totalAmount=0.00d;
            }

        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/sys/budgetAmount!approve.dhtml?currentYear=${currentYear}&deptId=${deptId}", type = Dispatcher.Redirect)})
    public String lock() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null&&deptId!=null&&currentYear!=null) {
           List<SysBudgetAmount> budgetAmountList= this.sysBudgetAmountService.getAmountList(userInfo.getOrgId(),currentYear, deptId);
            if(budgetAmountList!=null&&!budgetAmountList.isEmpty()){
                for(SysBudgetAmount budgetAmount:budgetAmountList){
                    budgetAmount.setLockYn("Y");
                    bind(budgetAmount);
                }
                this.sysBudgetAmountService.save(budgetAmountList);
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }
    @PageFlow(result = {@Result(name = "success", path = "/sys/budgetAmount!approve.dhtml?currentYear=${currentYear}&deptId=${deptId}", type = Dispatcher.Redirect)})
    public String unLock() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null&&deptId!=null&&currentYear!=null) {
            List<SysBudgetAmount> budgetAmountList= this.sysBudgetAmountService.getAmountList(userInfo.getOrgId(),currentYear, deptId);
            if(budgetAmountList!=null&&!budgetAmountList.isEmpty()){
                for(SysBudgetAmount budgetAmount:budgetAmountList){
                    budgetAmount.setLockYn("N");
                    bind(budgetAmount);
                }
                this.sysBudgetAmountService.save(budgetAmountList);
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }
    @PageFlow(result = {@Result(name = "success", path = "/view/sys/budgetAmount/approve.ftl", type = Dispatcher.FreeMarker)})
    public String approve() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            totalAmount = 0.00d;
            userInfo.setTopMenu("budget");
            userInfo.setLeftMenu("budgetAmountApprove");
            Date currentDate = DateFormatUtil.getCurrentDate(false);
            if (currentYear == null) {
                currentYear = DateFormatUtil.getDayInYear(currentDate);
            }
            yearList = new ArrayList<Integer>();
            yearList.add(currentYear - 1);
            yearList.add(currentYear);
            yearList.add(currentYear + 1);
            yearList.add(currentYear + 2);

            List<Selector> selectorList=new ArrayList<Selector>();
            selectorList.add(SelectorUtils.$eq("orgId.id",userInfo.getOrgId()));
            selectorList.add(SelectorUtils.$order("expenseType"));
            selectorList.add(SelectorUtils.$eq("useYn","Y"));
            typeList= this.sysBudgetTypeService.getAllList(selectorList);

            if(deptId==null){
                deptId=userInfo.getDepartmentId();
            }

            selectorList.clear();
            selectorList.add(SelectorUtils.$eq("orgId.id",userInfo.getOrgId()));
            selectorList.add(SelectorUtils.$order("deptNo"));
            selectorList.add(SelectorUtils.$eq("useYn","Y"));
            departmentList = this.hrDepartmentService.getByList(selectorList);
            if(departmentList!=null&&!departmentList.isEmpty()){
                for(HrDepartment department:departmentList){
                    totalAmount=sysBudgetAmountService.geTotalAmount(userInfo.getOrgId(),currentYear,department.getId());
                    if(totalAmount==null){
                        totalAmount=0.00d;
                    }
                    department.setBudgetAmount(totalAmount);
                }
            }
            hrDepartment = this.hrDepartmentService.getById(deptId);

            rows=0;
            sysBudgetAmountList=this.sysBudgetAmountService.getAmountList(userInfo.getOrgId(),currentYear,hrDepartment.getId());
            if(sysBudgetAmountList!=null&&!sysBudgetAmountList.isEmpty()){
                if(sysBudgetAmount==null){
                    sysBudgetAmount=sysBudgetAmountList.get(0);
                }
                idSets=new HashSet<String>();
                budgetAmountMap=new HashMap<String, Double>();
                for(SysBudgetAmount budgetAmount:sysBudgetAmountList){
                    idSets.add(get(budgetAmount,"typeId.id")+"_"+get(budgetAmount,"titleId.id")+"_"+get(budgetAmount,"titleId.titleName"));
                    budgetAmountMap.put(get(budgetAmount,"typeId.id")+"_"+get(budgetAmount,"titleId.id")+"_"+budgetAmount.getMonth(),
                            budgetAmount.getAmount());
                }
                if(idSets!=null&&!idSets.isEmpty()){
                    rows=idSets.size();
                }
            }
            totalAmount=sysBudgetAmountService.geTotalAmount(userInfo.getOrgId(),currentYear,hrDepartment.getId());
            if(totalAmount==null){
                totalAmount=0.00d;
            }
            String startDateStr=currentYear+"-01-01 00:00:01";
            String endDateStr=currentYear+"-12-31 23:23:59";
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
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/sys/budgetAmount.dhtml?currentYear=${currentYear}", type = Dispatcher.Redirect),
            @Result(name = "successApprove", path = "/sys/budgetAmount!approve.dhtml?currentYear=${currentYear}&deptId=${deptId}", type = Dispatcher.Redirect)})
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (currentYear != null && deptId != null&&rows!=null&&rows.intValue()>0) {
                hrDepartment = this.hrDepartmentService.getById(deptId);
                SysOrg sysOrg = this.sysOrgService.getById(userInfo.getOrgId());
                List<SysBudgetAmount> delBudgeAmountList=null;
                Set<String> setStr=new HashSet<String>();
                if (hrDepartment != null) {
                    delBudgeAmountList=this.sysBudgetAmountService.getAmountList(userInfo.getOrgId(),currentYear,hrDepartment.getId());
                    sysBudgetAmountList=new ArrayList<SysBudgetAmount>();
                    SysBudgetType budgetType=null;
                    SysFinancialTitle sysFinancialTitle=null;
                    for(int index=1;index<=rows;index++){
                        budgetType=null;
                        sysFinancialTitle=null;
                        Long typeId=getParameter("typeId"+index,Long.class);
                        if(typeId!=null){
                             budgetType=this.sysBudgetTypeService.getById(typeId);
                        }
                        Long titleId=getParameter("titleId"+index,Long.class);
                        if(titleId!=null){
                            sysFinancialTitle=this.sysFinancialTitleService.getById(titleId);
                        }
                        String str=hrDepartment.getId()+"_"+typeId+"_"+titleId;
                        if(!setStr.contains(str)){
                            setStr.add(str);
                            for(int month=1;month<=12;month++){
                                Double amount=getParameter("amount"+index+"_"+month,Double.class);
                                if(amount==null){
                                    amount=0.00d;
                                }
                                if(amount.doubleValue()>0){
                                    sysBudgetAmount = new SysBudgetAmount();
                                    sysBudgetAmount.setOrgId(sysOrg);
                                    sysBudgetAmount.setTypeId(budgetType);
                                    sysBudgetAmount.setTitleId(sysFinancialTitle);
                                    sysBudgetAmount.setYear(currentYear);
                                    sysBudgetAmount.setDeptId(hrDepartment);
                                    sysBudgetAmount.setMonth(month);
                                    sysBudgetAmount.setAmount(amount);
                                    sysBudgetAmount.setUseYn("Y");
                                    sysBudgetAmount.setLockYn("N");
                                    bind(sysBudgetAmount);
                                    sysBudgetAmountList.add(sysBudgetAmount);
                                }
                            }
                        }
                    }
                    if ((sysBudgetAmountList != null && !sysBudgetAmountList.isEmpty())||
                            (delBudgeAmountList != null && !delBudgeAmountList.isEmpty())) {
                        this.sysBudgetAmountService.save(delBudgeAmountList,sysBudgetAmountList);
                    }
                }
            }
        }
        if(StringUtils.isNotBlank(approveBudget)){
            return "successApprove";
        }
        return "success";
    }


}
