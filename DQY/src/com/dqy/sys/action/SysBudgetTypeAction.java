package com.dqy.sys.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.sys.entity.SysBudgetType;
import com.dqy.sys.entity.SysOrg;
import com.dqy.sys.entity.SysOrgGroup;
import com.dqy.sys.service.SysBudgetTypeService;
import com.dqy.sys.service.SysOrgGroupService;
import com.dqy.sys.service.SysOrgService;
import com.dqy.web.support.ActionSupport;
import com.google.inject.Inject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.guiceside.commons.lang.StringUtils;
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
@Action(name = "budgetType", namespace = "/sys")
public class SysBudgetTypeAction extends ActionSupport<SysBudgetType> {

    @Inject
    private SysBudgetTypeService sysBudgetTypeService;

    @Inject
    private SysOrgService sysOrgService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private SysBudgetType sysBudgetType;

    @ReqGet
    @ReqSet
    private String keyword;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private Long deptId;
    @ReqSet
    private List<SysBudgetType> budgetTypeList;

    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/sys/budgetType/list.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("sys");
            userInfo.setLeftMenu("budgetType");
            pageObj = this.sysBudgetTypeService.getPageList(getStart(), rows, searchModeCallback());
            if(pageObj!=null){
                budgetTypeList=pageObj.getResultList();
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }


    private List<Selector> searchModeCallback() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            selectorList.add(SelectorUtils.$eq("orgId.id",userInfo.getOrgId()));
            if(StringUtils.isNotBlank(keyword)){
                selectorList.add(SelectorUtils.$like("expenseType",keyword));
            }
            selectorList.add(SelectorUtils.$order("deptId.id"));
        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/sys/budgetType/input.ftl", type = Dispatcher.FreeMarker)})
    public String input() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (id != null && userInfo != null) {
            sysBudgetType = this.sysBudgetTypeService.getById(id);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Token
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && sysBudgetType != null) {
            if (sysBudgetType.getId() != null) {
                SysBudgetType old = this.sysBudgetTypeService.getById(sysBudgetType.getId());
                sysBudgetType = this.copy(sysBudgetType, old);
            }
            SysOrg sysOrg=this.sysOrgService.getById(userInfo.getOrgId());
            if(sysOrg!=null){
                sysBudgetType.setOrgId(sysOrg);
            }
            this.bind(sysBudgetType);
            this.sysBudgetTypeService.save(sysBudgetType);
        }
        return "saveSuccess";
    }

    @PageFlow(result = {@Result(name = "success", path = "/sys/budgetType.dhtml", type = Dispatcher.Redirect)})
    public String stop() throws Exception {
        if (id != null) {
            sysBudgetType = this.sysBudgetTypeService.getById(id);
            if (sysBudgetType != null) {
                sysBudgetType.setUseYn("N");
                sysBudgetTypeService.save(sysBudgetType);
            }
        }
        return "success";
    }
    @PageFlow(result = {@Result(name = "success", path = "/sys/budgetType.dhtml", type = Dispatcher.Redirect)})
    public String play() throws Exception {
        if (id != null) {
            sysBudgetType = this.sysBudgetTypeService.getById(id);
            if (sysBudgetType != null) {
                sysBudgetType.setUseYn("Y");
                sysBudgetTypeService.save(sysBudgetType);
            }
        }
        return "success";
    }

    public String getTypeList() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", -1);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (deptId != null && userInfo != null) {
            List<Selector> selectorList = new ArrayList<Selector>();
            selectorList.add(SelectorUtils.$eq("orgId.id", userInfo.getOrgId()));
            selectorList.add(SelectorUtils.$eq("deptId.id", deptId));
            selectorList.add(SelectorUtils.$eq("useYn", "Y"));
            selectorList.add(SelectorUtils.$order("expenseType"));
            budgetTypeList = this.sysBudgetTypeService.getAllList(selectorList);
            if(budgetTypeList!=null&&!budgetTypeList.isEmpty()){
                JSONArray jsonArray=new JSONArray();
                for(SysBudgetType type:budgetTypeList){
                    JSONObject item=new JSONObject();
                    item.put("id",type.getId());
                    item.put("name",type.getExpenseType());
                    jsonArray.add(item);
                }
                jsonObject.put("typeList", jsonArray);
                jsonObject.put("result", 0);
            }
        }
        writeJsonByAction(jsonObject.toString());
        return null;
    }
}
