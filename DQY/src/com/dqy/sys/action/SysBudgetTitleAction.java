package com.dqy.sys.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.hr.entity.HrDepartment;
import com.dqy.hr.service.HrDepartmentService;
import com.dqy.sys.entity.SysBudgetTitle;
import com.dqy.sys.entity.SysBudgetType;
import com.dqy.sys.entity.SysOrg;
import com.dqy.sys.service.SysBudgetTitleService;
import com.dqy.sys.service.SysBudgetTypeService;
import com.dqy.sys.service.SysOrgService;
import com.dqy.util.PinyinUtils;
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
@Action(name = "budgetTitle", namespace = "/sys")
public class SysBudgetTitleAction extends ActionSupport<SysBudgetTitle> {

    @Inject
    private SysBudgetTypeService sysBudgetTypeService;

    @Inject
    private SysBudgetTitleService sysBudgetTitleService;

    @Inject
    private HrDepartmentService hrDepartmentService;

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
    private Long deptId;

    @ReqGet
    @ReqSet
    private String keyword;

    @ReqGet
    @ReqSet
    private Long typeId;

    @ReqSet
    private List<SysBudgetType> budgetTypeList;

    @ReqSet
    private List<SysBudgetTitle> budgetTitleList;

    @ReqSet
    private List<HrDepartment> departmentList;

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
            selectorList.add(SelectorUtils.$eq("orgId.id",userInfo.getOrgId()));
            if(StringUtils.isNotBlank(keyword)){
                selectorList.add(SelectorUtils.$like("titleName",keyword));
            }
            selectorList.add(SelectorUtils.$order("titleNo"));
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
        selectorList.add(SelectorUtils.$order("expenseType"));
        budgetTypeList=this.sysBudgetTypeService.getAllList(selectorList);
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Token
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && sysBudgetTitle != null) {
            if (sysBudgetTitle.getId() != null) {
                SysBudgetTitle old = this.sysBudgetTitleService.getById(sysBudgetTitle.getId());
                sysBudgetTitle = this.copy(sysBudgetTitle, old);
            }
            SysOrg sysOrg=this.sysOrgService.getById(userInfo.getOrgId());
            if(sysOrg!=null){
                sysBudgetTitle.setOrgId(sysOrg);
            }
            sysBudgetTitle.setTitlePy(PinyinUtils.getJPinYin(sysBudgetTitle.getTitleName()));
            this.bind(sysBudgetTitle);
            this.sysBudgetTitleService.save(sysBudgetTitle);
        }
        return "saveSuccess";
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

    public String validateNo() throws Exception {
        JSONObject item = new JSONObject();
        item.put("result", false);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (sysBudgetTitle != null) {
                if (StringUtils.isNotBlank(sysBudgetTitle.getTitleNo())) {
                    String ignore = getParameter("ignore");
                    if (StringUtils.isNotBlank(ignore)) {
                        if (ignore.equals(sysBudgetTitle.getTitleNo())) {
                            item.put("result", true);
                            writeJsonByAction(item.toString());
                        } else {
                            Integer row = this.sysBudgetTitleService.validateNo( userInfo.getOrgId(),sysBudgetTitle.getTitleNo());
                            if (row.intValue() == 0) {
                                item.put("result", true);
                            }
                        }
                    } else {
                        Integer row = this.sysBudgetTitleService.validateNo( userInfo.getOrgId(),sysBudgetTitle.getTitleNo());
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
    public String validateName() throws Exception {
        JSONObject item = new JSONObject();
        item.put("result", false);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (sysBudgetTitle != null) {
                if (StringUtils.isNotBlank(sysBudgetTitle.getTitleName())) {
                    String ignore = getParameter("ignore");
                    if (StringUtils.isNotBlank(ignore)) {
                        if (ignore.equals(sysBudgetTitle.getTitleName())) {
                            item.put("result", true);
                            writeJsonByAction(item.toString());
                        } else {
                            Integer row = this.sysBudgetTitleService.validateName( userInfo.getOrgId(),sysBudgetTitle.getTitleName());
                            if (row.intValue() == 0) {
                                item.put("result", true);
                            }
                        }
                    } else {
                        Integer row = this.sysBudgetTitleService.validateName( userInfo.getOrgId(),sysBudgetTitle.getTitleName());
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

    public String getTitleList() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", -1);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (typeId != null && userInfo != null) {
            List<Selector> selectorList = new ArrayList<Selector>();
            selectorList.add(SelectorUtils.$eq("typeId.id", typeId));
            selectorList.add(SelectorUtils.$eq("useYn", "Y"));
            selectorList.add(SelectorUtils.$order("titleName"));
            budgetTitleList = this.sysBudgetTitleService.getAllList(selectorList);
            if(budgetTitleList!=null&&!budgetTitleList.isEmpty()){
                JSONArray jsonArray=new JSONArray();
                for(SysBudgetTitle title:budgetTitleList){
                    JSONObject item=new JSONObject();
                    item.put("id",title.getId());
                    item.put("name",title.getTitleName());
                    jsonArray.add(item);
                }
                jsonObject.put("titleList", jsonArray);
                jsonObject.put("result", 0);
            }
        }
        writeJsonByAction(jsonObject.toString());
        return null;
    }
}
