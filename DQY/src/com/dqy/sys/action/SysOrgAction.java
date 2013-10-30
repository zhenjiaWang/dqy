package com.dqy.sys.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.sys.entity.SysOrg;
import com.dqy.sys.entity.SysOrgGroup;
import com.dqy.sys.service.SysOrgGroupService;
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
@Action(name = "org", namespace = "/sys")
public class SysOrgAction extends ActionSupport<SysOrg> {

    @Inject
    private SysOrgService sysOrgService;


    @Inject
    private SysOrgGroupService sysOrgGroupService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private SysOrg sysOrg;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private Long groupId;


    @ReqSet
    private List<SysOrg> orgList;


    @ReqSet
    private List<SysOrgGroup> orgGroupList;

    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/sys/org/list.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("sys");
            userInfo.setLeftMenu("org");
            pageObj = this.sysOrgService.getPageList(getStart(), rows, searchModeCallback());
            if(pageObj!=null){
                orgList=pageObj.getResultList();
            }

        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }


    private List<Selector> searchModeCallback() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            selectorList.add(SelectorUtils.$order("groupId.id"));
        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/sys/org/input.ftl", type = Dispatcher.FreeMarker)})
    public String input() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (id != null && userInfo != null) {
            sysOrg = this.sysOrgService.getById(id);
            groupId=sysOrg.getGroupId().getId();
        }
        List<Selector> selectorList=new ArrayList<Selector>();
        selectorList.add(SelectorUtils.$eq("useYn","Y"));
        orgGroupList=this.sysOrgGroupService.getListAll(selectorList);
        if(groupId==null){
            if(orgGroupList!=null&&!orgGroupList.isEmpty()){
                groupId=orgGroupList.get(0).getId();
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Token
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && sysOrg != null) {
            if (sysOrg.getId() != null) {
                SysOrg old = this.sysOrgService.getById(sysOrg.getId());
                sysOrg = this.copy(sysOrg, old);
            }
            if(sysOrg.getGroupId()!=null){
                if(sysOrg.getGroupId().getId()==null){
                    sysOrg.setGroupId(null);
                }
            }
            this.bind(sysOrg);
            this.sysOrgService.save(sysOrg);
        }
        return "saveSuccess";
    }

    @PageFlow(result = {@Result(name = "success", path = "/sys/org.dhtml", type = Dispatcher.Redirect)})
    public String delete() throws Exception {
        if (id != null) {
            sysOrg = this.sysOrgService.getById(id);
            if (sysOrg != null) {
                sysOrgService.delete(sysOrg);
            }
        }
        return "success";
    }


    public String validateName() throws Exception {
        JSONObject item = new JSONObject();
        item.put("result", false);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (sysOrg != null&&groupId!=null) {
                if (StringUtils.isNotBlank(sysOrg.getOrgName())) {
                    String ignore = getParameter("ignore");
                    if (StringUtils.isNotBlank(ignore)) {
                        if (ignore.equals(sysOrg.getOrgName())) {
                            item.put("result", true);
                            writeJsonByAction(item.toString());
                        } else {
                            Integer row = this.sysOrgService.validateName( groupId,sysOrg.getOrgName());
                            if (row.intValue() == 0) {
                                item.put("result", true);
                            }
                        }
                    } else {
                        Integer row = this.sysOrgService.validateName( groupId,sysOrg.getOrgName());
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

    public String validateNo() throws Exception {
        JSONObject item = new JSONObject();
        item.put("result", false);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (sysOrg != null&&groupId!=null) {
                if (StringUtils.isNotBlank(sysOrg.getOrgNo())) {
                    String ignore = getParameter("ignore");
                    if (StringUtils.isNotBlank(ignore)) {
                        if (ignore.equals(sysOrg.getOrgNo())) {
                            item.put("result", true);
                            writeJsonByAction(item.toString());
                        } else {
                            Integer row = this.sysOrgService.validateNo(groupId, sysOrg.getOrgNo());
                            if (row.intValue() == 0) {
                                item.put("result", true);
                            }
                        }
                    } else {
                        Integer row = this.sysOrgService.validateNo(groupId, sysOrg.getOrgNo());
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


    @PageFlow(result = {@Result(name = "success", path = "/sys/org.dhtml", type = Dispatcher.Redirect)})
    public String stop() throws Exception {
        if (id != null) {
            sysOrg = this.sysOrgService.getById(id);
            if (sysOrg != null) {
                sysOrg.setUseYn("N");
                sysOrgService.save(sysOrg);
            }
        }
        return "success";
    }
    @PageFlow(result = {@Result(name = "success", path = "/sys/org.dhtml", type = Dispatcher.Redirect)})
    public String play() throws Exception {
        if (id != null) {
            sysOrg = this.sysOrgService.getById(id);
            if (sysOrg != null) {
                sysOrg.setUseYn("Y");
                sysOrgService.save(sysOrg);
            }
        }
        return "success";
    }

}
