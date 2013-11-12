package com.dqy.sys.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.sys.entity.*;
import com.dqy.sys.service.*;
import com.dqy.web.support.ActionSupport;
import com.google.inject.Inject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.guiceside.commons.Page;
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
@Action(name = "budgetOwen", namespace = "/sys")
public class SysBudgetOwenAction extends ActionSupport<SysBudgetOwen> {

    @Inject
    private SysBudgetTypeService sysBudgetTypeService;

    @Inject
    private SysBudgetTitleService sysBudgetTitleService;

    @Inject
    private SysBudgetOwenService sysBudgetOwenService;

    @Inject
    private SysOrgService sysOrgService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private SysBudgetOwen sysBudgetOwen;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private Long typeId;

    @ReqGet
    @ReqSet
    private Long budgetTitleId;

    @ReqGet
    @ReqSet
    private Long titleId;

    @ReqGet
    @ReqSet
    private Long titleId1;

    @ReqGet
    @ReqSet
    private Long titleId2;

    @ReqGet
    @ReqSet
    private Long titleId3;

    @ReqSet
    private SysBudgetTitle sysBudgetTitle;
    @ReqGet
    @ReqSet
    private Page<SysBudgetTitle> pageBudgetTitle;

    @ReqSet
    private List<SysBudgetType> budgetTypeList;

    @ReqSet
    private List<SysBudgetTitle> budgetTitleList;

    @ReqSet
    private List<SysBudgetOwen> budgetOwenList;

    @ReqSet
    private List<SysFinancialTitle> titleList;

    @ReqGet
    @ReqSet
    private String keyword;

    @Inject
    private SysFinancialTitleService sysFinancialTitleService;

    private List<Selector> searchModeCallback() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            selectorList.add(SelectorUtils.$eq("orgId.id",userInfo.getOrgId()));
            if(StringUtils.isNotBlank(keyword)){
                selectorList.add(SelectorUtils.$like("titleName",keyword));
            }
            selectorList.add(SelectorUtils.$order("titleName"));
        }
        return selectorList;
    }

    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/sys/budgetOwen/list.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("sys");
            userInfo.setLeftMenu("budgetOwen");
            pageBudgetTitle = this.sysBudgetTitleService.getPageList(getStart(), rows, searchModeCallback());
            if(pageBudgetTitle!=null){
                budgetTitleList=pageBudgetTitle.getResultList();
                if(budgetTitleList!=null&&!budgetTitleList.isEmpty()){
                    for(SysBudgetTitle budgetTitle:budgetTitleList){
                         sysBudgetOwen= this.sysBudgetOwenService.getByBudgetTitleId(budgetTitle.getId());
                        if(sysBudgetOwen!=null){
                            budgetTitle.setSysBudgetOwen(sysBudgetOwen);
                        }
                    }
                }
            }
            List<Selector> selectorList = new ArrayList<Selector>();
            selectorList.add(SelectorUtils.$eq("orgId.id", userInfo.getOrgId()));
            selectorList.add(SelectorUtils.$order("displayOrder"));
            selectorList.add(SelectorUtils.$eq("titleLevel", 1));
            selectorList.add(SelectorUtils.$eq("useYn", "Y"));
            titleList = this.sysFinancialTitleService.getAllList(selectorList);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/sys/budgetOwen/input.ftl", type = Dispatcher.FreeMarker)})
    public String input() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (id != null && userInfo != null) {
            sysBudgetOwen = this.sysBudgetOwenService.getById(id);
            if(sysBudgetOwen!=null){
                sysBudgetTitle= sysBudgetOwen.getBudgetTitle();
            }
        }else if(budgetTitleId!=null){
            sysBudgetTitle= sysBudgetTitleService.getById(budgetTitleId);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Token
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && sysBudgetOwen != null) {
            if (sysBudgetOwen.getId() != null) {
                SysBudgetOwen old = this.sysBudgetOwenService.getById(sysBudgetOwen.getId());
                sysBudgetOwen = this.copy(sysBudgetOwen, old);
            }
            SysOrg sysOrg=this.sysOrgService.getById(userInfo.getOrgId());
            if(sysOrg!=null){
                sysBudgetOwen.setOrgId(sysOrg);
            }
            this.bind(sysBudgetOwen);
            this.sysBudgetOwenService.save(sysBudgetOwen);
        }
        return "saveSuccess";
    }
    public String getTitleList() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", -1);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (typeId != null && userInfo != null) {
            List<Long> bIdList= sysBudgetOwenService.getBudgetIdList(userInfo.getOrgId());
            List<Selector> selectorList = new ArrayList<Selector>();
            selectorList.add(SelectorUtils.$eq("typeId.id", typeId));
            selectorList.add(SelectorUtils.$order("titleName"));
            budgetTitleList = this.sysBudgetTitleService.getAllList(selectorList);
            if(budgetTitleList!=null&&!budgetTitleList.isEmpty()){
                JSONArray jsonArray=new JSONArray();
                for(SysBudgetTitle title:budgetTitleList){
                    if(bIdList!=null&&!bIdList.isEmpty()){
                        if(!bIdList.contains(title.getId())){
                            JSONObject item=new JSONObject();
                            item.put("id",title.getId());
                            item.put("name",title.getTitleName());
                            jsonArray.add(item);
                        }
                    }else{
                        JSONObject item=new JSONObject();
                        item.put("id",title.getId());
                        item.put("name",title.getTitleName());
                        jsonArray.add(item);
                    }
                }
                jsonObject.put("titleList", jsonArray);
            }
            jsonObject.put("result", 0);
        }
        writeJsonByAction(jsonObject.toString());
        return null;
    }

    public String getOwenList() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", -1);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (titleId != null && userInfo != null) {
            List<Selector> selectorList = new ArrayList<Selector>();
            selectorList.add(SelectorUtils.$alias("budgetTitle", "budgetTitle"));
            selectorList.add(SelectorUtils.$eq("orgId.id", userInfo.getOrgId()));
            selectorList.add(SelectorUtils.$eq("titleId.id", titleId));
            selectorList.add(SelectorUtils.$order("budgetTitle.titleName"));
            budgetOwenList = this.sysBudgetOwenService.getAllList(selectorList);
            if(budgetOwenList!=null&&!budgetOwenList.isEmpty()){
                JSONArray jsonArray=new JSONArray();
                for(SysBudgetOwen owen:budgetOwenList){
                    JSONObject item=new JSONObject();
                    item.put("id",owen.getId());
                    item.put("name", owen.getBudgetTitle().getTitleName());
                    jsonArray.add(item);
                }
                jsonObject.put("titleList", jsonArray);
            }
            jsonObject.put("result", 0);
        }
        writeJsonByAction(jsonObject.toString());
        return null;
    }

    @PageFlow(result = {@Result(name = "success", path = "/sys/budgetOwen.dhtml", type = Dispatcher.Redirect)})
    public String stop() throws Exception {
        if (id != null) {
            sysBudgetOwen = this.sysBudgetOwenService.getById(id);
            if (sysBudgetOwen != null) {
                sysBudgetOwen.setUseYn("N");
                sysBudgetOwenService.save(sysBudgetOwen);
            }
        }
        return "success";
    }
    @PageFlow(result = {@Result(name = "success", path = "/sys/budgetOwen.dhtml", type = Dispatcher.Redirect)})
    public String play() throws Exception {
        if (id != null) {
            sysBudgetOwen = this.sysBudgetOwenService.getById(id);
            if (sysBudgetOwen != null) {
                sysBudgetOwen.setUseYn("Y");
                sysBudgetOwenService.save(sysBudgetOwen);
            }
        }
        return "success";
    }


}
