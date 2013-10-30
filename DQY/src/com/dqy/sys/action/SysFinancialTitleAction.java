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
    private SysFinancialTitle parentTitle;

    @ReqSet
    private SysFinancialTitle rootTitle;

    @ReqSet
    private Long lv1Id;

    @ReqSet
    private Long orgId;

    @ReqSet
    private String autoTitleNo;


    @ReqSet
    private List<SysFinancialTitle> financialTitleList;

    @ReqSet
    private List<SysFinancialTitle> titleList;

    @ReqSet
    private Integer maxDisplayOrder;

    @ReqSet
    private Integer childCount = 0;

    @ReqGet
    @ReqSet
    private Long reloadTree;


    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/sys/financialTitle/index.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("sys");
            userInfo.setLeftMenu("financialTitle");
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
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
    public String treeData() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        JSONArray jsonArray = new JSONArray();
        JSONObject node = null;
        if (userInfo != null) {
            orgId = userInfo.getOrgId();
            if (parentId == null) {
                titleList = this.sysFinancialTitleService.getTitleListByLevel(orgId, 1, true);
            } else {
                titleList = this.sysFinancialTitleService.getTitleListByParentId(orgId, parentId, true);
            }
            if (titleList != null && !titleList.isEmpty()) {
                for (SysFinancialTitle financialTitle : titleList) {
                    node = new JSONObject();
                    node.put("name", StringUtils.defaultIfEmpty(financialTitle.getTitleName()));
                    node.put("id", StringUtils.defaultIfEmpty(financialTitle.getId()));
                    Integer count = this.sysFinancialTitleService.getCountByParentId(orgId, financialTitle.getId(), true);
                    if (count == null) {
                        count = 0;
                    }
                    if (count.intValue() > 0) {
                        node.put("isParent", true);
                    } else {
                        node.put("isParent", false);
                    }
                    jsonArray.add(node);
                }
            }
        }
        writeJsonByAction(jsonArray.toString());
        return null; //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/sys/financialTitle/input.ftl", type = Dispatcher.FreeMarker)})
    public String input() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            sysOrg = this.sysOrgService.getById(userInfo.getOrgId());
            autoTitleNo = sysOrg.getOrgNo() + "-";
            if (id != null) {
                sysFinancialTitle = this.sysFinancialTitleService.getById(id);
                parentTitle=this.sysFinancialTitle.getParentId();
                maxDisplayOrder=sysFinancialTitle.getDisplayOrder();
            }else{
                if (parentId != null) {
                    parentTitle = this.sysFinancialTitleService.getById(parentId);
                    if(parentTitle!=null){
                        maxDisplayOrder=this.sysFinancialTitleService.getMaxOrderByParentId(userInfo.getOrgId(),parentTitle.getId(),true);
                    }
                }else{
                    maxDisplayOrder=this.sysFinancialTitleService.getMaxOrderByOrgId(userInfo.getOrgId(),true);
                }
                if(maxDisplayOrder==null){
                    maxDisplayOrder=0;
                }
                maxDisplayOrder+=1;
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/sys/financialTitle/view.ftl", type = Dispatcher.FreeMarker)})
    public String view() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (id != null) {
                this.sysFinancialTitle = this.sysFinancialTitleService.getById(id);
                if (sysFinancialTitle != null) {
                    childCount = this.sysFinancialTitleService.getCountByParentId(userInfo.getOrgId(), sysFinancialTitle.getId(), true);
                    parentTitle = sysFinancialTitle.getParentId();
                }
            }
        }
        return "success";
    }

    @Token
    @PageFlow(result = {@Result(name = "success", path = "/sys/financialTitle!view.dhtml?id=${sysFinancialTitle.id}&reloadTree=${reloadTree}", type = Dispatcher.Redirect)})
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (sysFinancialTitle != null) {
                if (sysFinancialTitle != null) {
                    reloadTree=1l;
                    if (sysFinancialTitle.getId() != null) {
                        SysFinancialTitle old = this.sysFinancialTitleService.getById(sysFinancialTitle.getId());
                        sysFinancialTitle = this.copy(sysFinancialTitle, old);
                        reloadTree=2l;
                    }
                    if (sysFinancialTitle.getParentId() != null) {
                        if (sysFinancialTitle.getParentId().getId() != null) {
                            SysFinancialTitle parentId = this.sysFinancialTitleService.getById(sysFinancialTitle.getParentId().getId());
                            if (parentId != null) {
                                sysFinancialTitle.setTitleLevel(Integer.valueOf(parentId.getTitleLevel().intValue() + 1));
                            }
                        } else {
                            sysFinancialTitle.setParentId(null);
                            sysFinancialTitle.setTitleLevel(Integer.valueOf(1));
                        }
                    } else {
                        sysFinancialTitle.setParentId(null);
                        sysFinancialTitle.setTitleLevel(Integer.valueOf(1));
                    }
                    SysOrg org = this.sysOrgService.getById(userInfo.getOrgId());
                    if(org!=null){
                        sysFinancialTitle.setOrgId(org);
                    }
                }
                this.bind(sysFinancialTitle);
                this.sysFinancialTitleService.save(sysFinancialTitle);
            }
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
}
