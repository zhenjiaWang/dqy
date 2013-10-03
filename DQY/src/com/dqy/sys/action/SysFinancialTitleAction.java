package com.dqy.sys.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.sys.entity.SysFinancialTitle;
import com.dqy.sys.entity.SysOrg;
import com.dqy.sys.entity.SysOrgGroup;
import com.dqy.sys.service.SysFinancialTitleService;
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
@Action(name = "financialTitle", namespace = "/sys")
public class SysFinancialTitleAction extends ActionSupport<SysFinancialTitle> {

    @Inject
    private SysOrgService sysOrgService;

    @Inject
    private SysFinancialTitleService sysFinancialTitleService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private SysFinancialTitle sysFinancialTitle;

    @ReqSet
    private SysOrg sysOrg;

    @ReqGet
    @ReqSet
    private Integer level;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private Long parentId;

    @ReqSet
    private Long lv1Id;


    @ReqSet
    private String autoTitleNo;

    @ReqSet
    private Integer autoMaxDisplayOrder;

    @ReqSet
    private List<SysFinancialTitle> financialTitleList;

    @ReqSet
    private List<SysFinancialTitle> titleList;

    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/sys/financialTitle/list.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("sys");
            userInfo.setLeftMenu("financialTitle");
            pageObj = this.sysFinancialTitleService.getPageList(getStart(), rows, searchModeCallback());
            if (pageObj != null) {
                financialTitleList = pageObj.getResultList();
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }


    private List<Selector> searchModeCallback() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            selectorList.add(SelectorUtils.$eq("orgId.id", userInfo.getOrgId()));
            selectorList.add(SelectorUtils.$order("titleNo"));
            selectorList.add(SelectorUtils.$order("titleLevel"));
            selectorList.add(SelectorUtils.$order("displayOrder"));
        }
        return selectorList;
    }

    public String buildTitleNo() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", -1);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (parentId != null && userInfo != null) {
            sysFinancialTitle = this.sysFinancialTitleService.getById(parentId);
            if (sysFinancialTitle != null) {
                autoMaxDisplayOrder = this.sysFinancialTitleService.getMaxOrderByOrgId(userInfo.getOrgId(), sysFinancialTitle.getId());
                autoTitleNo = sysFinancialTitle.getTitleNo() + "-";
            }
            if (autoMaxDisplayOrder == null) {
                autoMaxDisplayOrder = 0;
            }
            autoMaxDisplayOrder = autoMaxDisplayOrder + 1;
            if (autoMaxDisplayOrder.intValue() < 10) {
                autoTitleNo += "0" + autoMaxDisplayOrder;
            } else {
                autoTitleNo += +autoMaxDisplayOrder;
            }
            jsonObject.put("autoMaxDisplayOrder", autoMaxDisplayOrder);
            jsonObject.put("autoTitleNo", autoTitleNo);
            jsonObject.put("result", 0);
        }
        writeJsonByAction(jsonObject.toString());
        return null;
    }

    public String getTitleList() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", -1);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (parentId != null && userInfo != null) {
            List<Selector> selectorList = new ArrayList<Selector>();
            selectorList.add(SelectorUtils.$eq("orgId.id", userInfo.getOrgId()));
            selectorList.add(SelectorUtils.$order("displayOrder"));
            selectorList.add(SelectorUtils.$eq("parentId.id", parentId));
            selectorList.add(SelectorUtils.$eq("useYn", "Y"));
            financialTitleList = this.sysFinancialTitleService.getAllList(selectorList);
            if(financialTitleList!=null&&!financialTitleList.isEmpty()){
                JSONArray jsonArray=new JSONArray();
                for(SysFinancialTitle title:financialTitleList){
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

    @PageFlow(result = {@Result(name = "input1", path = "/view/sys/financialTitle/input1.ftl", type = Dispatcher.FreeMarker),
            @Result(name = "input2", path = "/view/sys/financialTitle/input2.ftl", type = Dispatcher.FreeMarker),
            @Result(name = "input3", path = "/view/sys/financialTitle/input3.ftl", type = Dispatcher.FreeMarker)})
    public String input() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (id != null) {
                sysFinancialTitle = this.sysFinancialTitleService.getById(id);
                if (sysFinancialTitle != null) {
                    level = sysFinancialTitle.getTitleLevel();
                    if(level.intValue()==3){
                        SysFinancialTitle lv2=sysFinancialTitle.getParentId();
                        if(lv2!=null){
                            SysFinancialTitle lv1=lv2.getParentId();
                            if(lv1!=null){
                                lv1Id=lv1.getId();
                            }
                        }
                    }
                }
            }
            List<Selector> selectorList = new ArrayList<Selector>();
            selectorList.add(SelectorUtils.$eq("orgId.id", userInfo.getOrgId()));
            selectorList.add(SelectorUtils.$order("displayOrder"));
            if (level.intValue() == 2) {
                selectorList.add(SelectorUtils.$eq("titleLevel", 1));
                selectorList.add(SelectorUtils.$eq("useYn", "Y"));
                financialTitleList = this.sysFinancialTitleService.getAllList(selectorList);
            } else if (level.intValue() == 3) {
                selectorList.add(SelectorUtils.$eq("titleLevel", 1));
                selectorList.add(SelectorUtils.$eq("useYn", "Y"));
                titleList = this.sysFinancialTitleService.getAllList(selectorList);

                selectorList.clear();
                selectorList.add(SelectorUtils.$eq("orgId.id", userInfo.getOrgId()));
                selectorList.add(SelectorUtils.$order("displayOrder"));
                selectorList.add(SelectorUtils.$eq("useYn", "Y"));
                if (titleList != null && !titleList.isEmpty()) {
                    if(lv1Id==null){
                        SysFinancialTitle first = titleList.get(0);
                        if (first != null) {
                            selectorList.add(SelectorUtils.$eq("parentId.id", first.getId()));
                            financialTitleList = this.sysFinancialTitleService.getAllList(selectorList);
                        }
                    }else{
                        selectorList.add(SelectorUtils.$eq("parentId.id", lv1Id));
                        financialTitleList = this.sysFinancialTitleService.getAllList(selectorList);
                    }
                }
            }
            sysOrg = this.sysOrgService.getById(userInfo.getOrgId());
            autoTitleNo = sysOrg.getOrgNo() + "-";
            if (level.intValue() == 1 && id == null) {
                autoMaxDisplayOrder = this.sysFinancialTitleService.getMaxOrderByOrgId(sysOrg.getId());
            } else if (level.intValue() > 1 && id == null) {
                if (financialTitleList != null && !financialTitleList.isEmpty()) {
                    SysFinancialTitle first = financialTitleList.get(0);
                    if (first != null) {
                        autoMaxDisplayOrder = this.sysFinancialTitleService.getMaxOrderByOrgId(sysOrg.getId(), first.getId());
                        autoTitleNo = first.getTitleNo() + "-";
                    }
                }
            }
            if (autoMaxDisplayOrder == null) {
                autoMaxDisplayOrder = 0;
            }
            autoMaxDisplayOrder = autoMaxDisplayOrder + 1;
            if (autoMaxDisplayOrder.intValue() < 10) {
                autoTitleNo += "0" + autoMaxDisplayOrder;
            } else {
                autoTitleNo += +autoMaxDisplayOrder;
            }

        }

        return "input" + level;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Token
    @PageFlow(result = {@Result(name = "success", path = "/sys/financialTitle.dhtml", type = Dispatcher.Redirect)})
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && sysFinancialTitle != null) {
            if (sysFinancialTitle.getId() != null) {
                SysFinancialTitle old = this.sysFinancialTitleService.getById(sysFinancialTitle.getId());
                sysFinancialTitle = this.copy(sysFinancialTitle, old);
            }
            sysOrg = sysOrgService.getById(userInfo.getOrgId());
            if (sysOrg != null) {
                sysFinancialTitle.setOrgId(sysOrg);
            }
            if (sysFinancialTitle.getParentId() != null) {
                if (sysFinancialTitle.getParentId().getId() == null) {
                    sysFinancialTitle.setParentId(null);
                }
            }
            this.bind(sysFinancialTitle);
            this.sysFinancialTitleService.save(sysFinancialTitle);
        }
        return "success";
    }

    public String validateNo() throws Exception {
        JSONObject item = new JSONObject();
        item.put("result", false);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (sysFinancialTitle != null) {
                if (StringUtils.isNotBlank(sysFinancialTitle.getTitleNo())) {
                    String ignore = getParameter("ignore");
                    if (StringUtils.isNotBlank(ignore)) {
                        if (ignore.equals(sysFinancialTitle.getTitleNo())) {
                            item.put("result", true);
                            writeJsonByAction(item.toString());
                        } else {
                            Integer row = this.sysFinancialTitleService.validateNo(userInfo.getOrgId(), sysFinancialTitle.getTitleNo());
                            if (row.intValue() == 0) {
                                item.put("result", true);
                            }
                        }
                    } else {
                        Integer row = this.sysFinancialTitleService.validateNo(userInfo.getOrgId(), sysFinancialTitle.getTitleNo());
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
            if (sysFinancialTitle != null) {
                if (StringUtils.isNotBlank(sysFinancialTitle.getTitleName())) {
                    String ignore = getParameter("ignore");
                    if (StringUtils.isNotBlank(ignore)) {
                        if (ignore.equals(sysFinancialTitle.getTitleName())) {
                            item.put("result", true);
                            writeJsonByAction(item.toString());
                        } else {
                            Integer row = this.sysFinancialTitleService.validateName(userInfo.getOrgId(), parentId, sysFinancialTitle.getTitleName());
                            if (row.intValue() == 0) {
                                item.put("result", true);
                            }
                        }
                    } else {
                        Integer row = this.sysFinancialTitleService.validateName(userInfo.getOrgId(), parentId, sysFinancialTitle.getTitleName());
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

    @PageFlow(result = {@Result(name = "success", path = "/sys/financialTitle.dhtml", type = Dispatcher.Redirect)})
    public String stop() throws Exception {
        if (id != null) {
            sysFinancialTitle = this.sysFinancialTitleService.getById(id);
            if (sysFinancialTitle != null) {
                sysFinancialTitle.setUseYn("N");
                sysFinancialTitleService.save(sysFinancialTitle);
            }
        }
        return "success";
    }
    @PageFlow(result = {@Result(name = "success", path = "/sys/financialTitle.dhtml", type = Dispatcher.Redirect)})
    public String play() throws Exception {
        if (id != null) {
            sysFinancialTitle = this.sysFinancialTitleService.getById(id);
            if (sysFinancialTitle != null) {
                sysFinancialTitle.setUseYn("Y");
                sysFinancialTitleService.save(sysFinancialTitle);
            }
        }
        return "success";
    }

}
