package com.dqy.sys.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.sys.entity.SysBudgetTitle;
import com.dqy.sys.entity.SysBudgetType;
import com.dqy.sys.entity.SysOrg;
import com.dqy.sys.service.SysBudgetTitleService;
import com.dqy.sys.service.SysBudgetTypeService;
import com.dqy.sys.service.SysOrgService;
import com.dqy.web.support.ActionSupport;
import com.google.inject.Inject;
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
@Action(name = "budgetTitle", namespace = "/sys")
public class SysBudgetTitleAction extends ActionSupport<SysBudgetTitle> {

    @Inject
    private SysBudgetTypeService sysBudgetTypeService;

    @Inject
    private SysBudgetTitleService sysBudgetTitleService;

    @Inject
    private SysOrgService sysOrgService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private SysBudgetTitle sysBudgetTitle;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private Long typeId;

    @ReqSet
    private List<SysBudgetType> budgetTypeList;

    @ReqSet
    private List<SysBudgetTitle> budgetTitleList;

    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/sys/budgetTitle/list.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("sys");
            userInfo.setLeftMenu("budgetTitle");
            pageObj = this.sysBudgetTitleService.getPageList(getStart(), rows, searchModeCallback());
            if(pageObj!=null){
                budgetTitleList=pageObj.getResultList();
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }


    private List<Selector> searchModeCallback() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            selectorList.add(SelectorUtils.$alias("typeId", "typeId"));
            selectorList.add(SelectorUtils.$eq("typeId.orgId.id",userInfo.getOrgId()));
            selectorList.add(SelectorUtils.$order("typeId.id"));
            selectorList.add(SelectorUtils.$order("typeId.deptId.id"));
            selectorList.add(SelectorUtils.$order("titleName"));
        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/sys/budgetTitle/input.ftl", type = Dispatcher.FreeMarker)})
    public String input() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (id != null && userInfo != null) {
            sysBudgetTitle = this.sysBudgetTitleService.getById(id);
        }
        List<Selector> selectorList = new ArrayList<Selector>();
        selectorList.add(SelectorUtils.$eq("orgId.id", userInfo.getOrgId()));
        selectorList.add(SelectorUtils.$eq("useYn", "Y"));
        selectorList.add(SelectorUtils.$order("deptId.id"));
        selectorList.add(SelectorUtils.$order("expenseType"));
        budgetTypeList=this.sysBudgetTypeService.getAllList(selectorList);
        if(typeId==null){
            if(budgetTypeList!=null&&!budgetTypeList.isEmpty()){
                SysBudgetType sysBudgetType=budgetTypeList.get(0);
                if(sysBudgetType!=null){
                    typeId=sysBudgetType.getId();
                }
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Token
    @PageFlow(result = {@Result(name = "success", path = "/sys/budgetTitle.dhtml", type = Dispatcher.Redirect)})
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && sysBudgetTitle != null) {
            if (sysBudgetTitle.getId() != null) {
                SysBudgetTitle old = this.sysBudgetTitleService.getById(sysBudgetTitle.getId());
                sysBudgetTitle = this.copy(sysBudgetTitle, old);
            }
            if(sysBudgetTitle.getTypeId()!=null){
                if(sysBudgetTitle.getTypeId().getId()==null){
                    sysBudgetTitle.setTypeId(null);
                }
            }
            this.bind(sysBudgetTitle);
            this.sysBudgetTitleService.save(sysBudgetTitle);
        }
        return "success";
    }

    @PageFlow(result = {@Result(name = "success", path = "/sys/budgetTitle.dhtml", type = Dispatcher.Redirect)})
    public String stop() throws Exception {
        if (id != null) {
            sysBudgetTitle = this.sysBudgetTitleService.getById(id);
            if (sysBudgetTitle != null) {
                sysBudgetTitle.setUseYn("N");
                sysBudgetTitleService.save(sysBudgetTitle);
            }
        }
        return "success";
    }
    @PageFlow(result = {@Result(name = "success", path = "/sys/budgetTitle.dhtml", type = Dispatcher.Redirect)})
    public String play() throws Exception {
        if (id != null) {
            sysBudgetTitle = this.sysBudgetTitleService.getById(id);
            if (sysBudgetTitle != null) {
                sysBudgetTitle.setUseYn("Y");
                sysBudgetTitleService.save(sysBudgetTitle);
            }
        }
        return "success";
    }


    public String validateName() throws Exception {
        JSONObject item = new JSONObject();
        item.put("result", false);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (sysBudgetTitle != null&&typeId!=null) {
                if (StringUtils.isNotBlank(sysBudgetTitle.getTitleName())) {
                    String ignore = getParameter("ignore");
                    if (StringUtils.isNotBlank(ignore)) {
                        if (ignore.equals(sysBudgetTitle.getTitleName())) {
                            item.put("result", true);
                            writeJsonByAction(item.toString());
                        } else {
                            Integer row = this.sysBudgetTitleService.validateName( typeId,sysBudgetTitle.getTitleName());
                            if (row.intValue() == 0) {
                                item.put("result", true);
                            }
                        }
                    } else {
                        Integer row = this.sysBudgetTitleService.validateName( typeId,sysBudgetTitle.getTitleName());
                        if (row.intValue() == 0) {
                            item.put("result", true);
                        }
                    }
                }
            }
        }
        writeJsonByAction(item.toString());
        return null;
    }
}
