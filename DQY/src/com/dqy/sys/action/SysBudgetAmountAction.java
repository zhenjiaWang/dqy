package com.dqy.sys.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.hr.entity.HrDepartment;
import com.dqy.hr.service.HrDepartmentService;
import com.dqy.sys.entity.*;
import com.dqy.sys.service.*;
import com.dqy.web.support.ActionSupport;
import com.google.inject.Inject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
    private SysBudgetTitleService sysBudgetTitleService;

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
    @ReqSet
    private Long deptId;


    @ReqGet
    @ReqSet
    private Integer currentYear;

    @ReqSet
    private Double totalAmount;

    @ReqSet
    private List<Integer> yearList;

    @ReqSet
    private List<SysBudgetType> budgetTypeList;

    @ReqSet
    private List<SysBudgetAmount> sysBudgetAmountList;

    @ReqSet
    private Map<String,List<SysBudgetTitle>> budgetTitleMap;

    @ReqSet
    private Map<String,Double> budgetAmountMap;
    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/sys/budgetAmount/index.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            totalAmount=0.00d;
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
            if (deptId != null) {
                hrDepartment = this.hrDepartmentService.getById(deptId);
                if (hrDepartment != null) {
                    List<Selector> selectorList = new ArrayList<Selector>();
                    selectorList.add(SelectorUtils.$eq("orgId.id", userInfo.getOrgId()));
                    selectorList.add(SelectorUtils.$eq("deptId.id", hrDepartment.getId()));
                    selectorList.add(SelectorUtils.$eq("useYn", "Y"));
                    selectorList.add(SelectorUtils.$order("expenseType"));
                    budgetTypeList=sysBudgetTypeService.getAllList(selectorList);
                    if(budgetTypeList!=null&&!budgetTypeList.isEmpty()){
                        budgetTitleMap=new HashMap<String, List<SysBudgetTitle>>();
                        budgetAmountMap=new HashMap<String, Double>();
                        List<SysBudgetTitle> budgetTitleList=null;
                        for(SysBudgetType budgetType:budgetTypeList){
                            selectorList.clear();
                            selectorList.add(SelectorUtils.$eq("typeId.id", budgetType.getId()));
                            selectorList.add(SelectorUtils.$eq("useYn", "Y"));
                            selectorList.add(SelectorUtils.$order("titleName"));
                            budgetTitleList=this.sysBudgetTitleService.getAllList(selectorList);
                            if(budgetTitleList!=null&&!budgetTitleList.isEmpty()){
                                budgetTitleMap.put(budgetType.getId()+"_",budgetTitleList);
                                if(budgetTitleList!=null&&!budgetTitleList.isEmpty()){
                                    for(SysBudgetTitle budgetTitle:budgetTitleList){
                                        for(int i=1;i<=12;i++){
                                            budgetAmountMap.put(hrDepartment.getId()+"_"+budgetTitle.getId()+"_"+budgetType.getId()+"_"+i,0.00d);
                                        }
                                    }
                                }
                            }
                        }
                        totalAmount=this.sysBudgetAmountService.geTotalAmount(userInfo.getOrgId(),currentYear,hrDepartment.getId());
                        sysBudgetAmountList=this.sysBudgetAmountService.getAmountList(userInfo.getOrgId(),currentYear,hrDepartment.getId());
                        if(sysBudgetAmountList!=null&&!sysBudgetAmountList.isEmpty()){
                            for(SysBudgetAmount budgetAmount:sysBudgetAmountList){
                                sysBudgetAmount=budgetAmount;
                                budgetAmountMap.put(hrDepartment.getId()+"_"+get(budgetAmount,"titleId.id")+"_"+get(budgetAmount,"titleId.typeId.id")+"_"+budgetAmount.getMonth(),budgetAmount.getAmount());
                            }
                        }
                    }
                }
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }
    @PageFlow(result = {@Result(name = "success", path = "/sys/budgetAmount.dhtml?currentYear=${currentYear}&deptId=${deptId}", type = Dispatcher.Redirect)})
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if(currentYear!=null&&deptId!=null){
                hrDepartment = this.hrDepartmentService.getById(deptId);
                SysOrg sysOrg=this.sysOrgService.getById(userInfo.getOrgId());
                if (hrDepartment != null) {
                    List<Selector> selectorList = new ArrayList<Selector>();
                    selectorList.add(SelectorUtils.$eq("orgId.id", userInfo.getOrgId()));
                    selectorList.add(SelectorUtils.$eq("deptId.id", hrDepartment.getId()));
                    selectorList.add(SelectorUtils.$eq("useYn", "Y"));
                    selectorList.add(SelectorUtils.$order("expenseType"));
                    budgetTypeList=sysBudgetTypeService.getAllList(selectorList);
                    if(budgetTypeList!=null&&!budgetTypeList.isEmpty()){
                        sysBudgetAmountList=new ArrayList<SysBudgetAmount>();
                        List<SysBudgetTitle> budgetTitleList=null;
                        for(SysBudgetType budgetType:budgetTypeList){
                            selectorList.clear();
                            selectorList.add(SelectorUtils.$eq("typeId.id", budgetType.getId()));
                            selectorList.add(SelectorUtils.$eq("useYn", "Y"));
                            selectorList.add(SelectorUtils.$order("titleName"));
                            budgetTitleList=this.sysBudgetTitleService.getAllList(selectorList);
                            if(budgetTitleList!=null&&!budgetTitleList.isEmpty()){
                                for(SysBudgetTitle budgetTitle:budgetTitleList){
                                    for(int i=1;i<=12;i++){
                                        Double amount= getParameter(hrDepartment.getId()+"_"+budgetTitle.getId()+"_"+budgetType.getId()+"_"+i,Double.class);
                                        if(amount==null){
                                            amount=0d;
                                        }
                                        sysBudgetAmount= this.sysBudgetAmountService.getAmount(userInfo.getOrgId(),hrDepartment.getId(),budgetTitle.getId(),currentYear,i);
                                        if(sysBudgetAmount==null){
                                            sysBudgetAmount=new SysBudgetAmount();
                                            sysBudgetAmount.setOrgId(sysOrg);
                                            sysBudgetAmount.setTitleId(budgetTitle);
                                            sysBudgetAmount.setYear(currentYear);
                                            sysBudgetAmount.setDeptId(hrDepartment);
                                            sysBudgetAmount.setMonth(i);
                                        }
                                        sysBudgetAmount.setAmount(amount);
                                        sysBudgetAmount.setUseYn("Y");
                                        bind(sysBudgetAmount);
                                        sysBudgetAmountList.add(sysBudgetAmount);
                                    }
                                }
                            }
                        }
                    }
                    if(sysBudgetAmountList!=null&&!sysBudgetAmountList.isEmpty()){
                        this.sysBudgetAmountService.save(sysBudgetAmountList);
                    }
                }
            }
        }
        return "success";
    }



}
