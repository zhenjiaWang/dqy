package com.dqy.sys.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.sys.entity.SysOrgGroup;
import com.dqy.sys.service.SysOrgGroupService;
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
@Action(name = "orgGroup", namespace = "/sys")
public class SysOrgGroupAction extends ActionSupport<SysOrgGroup> {

    @Inject
    private SysOrgGroupService sysOrgGroupService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private SysOrgGroup sysOrgGroup;

    @ReqGet
    @ReqSet
    private Long id;


    @ReqSet
    private List<SysOrgGroup> orgGroupList;

    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/sys/orgGroup/list.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("sys");
            userInfo.setLeftMenu("orgGroup");
            pageObj = this.sysOrgGroupService.getPageList(getStart(), rows, searchModeCallback());
            if(pageObj!=null){
                orgGroupList=pageObj.getResultList();
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }


    private List<Selector> searchModeCallback() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {

        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/sys/orgGroup/input.ftl", type = Dispatcher.FreeMarker)})
    public String input() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (id != null && userInfo != null) {
            sysOrgGroup = this.sysOrgGroupService.getById(id);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Token
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && sysOrgGroup != null) {
            if (sysOrgGroup.getId() != null) {
                SysOrgGroup old = this.sysOrgGroupService.getById(sysOrgGroup.getId());
                sysOrgGroup = this.copy(sysOrgGroup, old);
            }
            sysOrgGroup.setGroupName(sysOrgGroup.getGroupName().trim());
            this.bind(sysOrgGroup);
            this.sysOrgGroupService.save(sysOrgGroup);
        }
        return "saveSuccess";
    }

    @PageFlow(result = {@Result(name = "success", path = "/sys/orgGroup.dhtml", type = Dispatcher.Redirect)})
    public String delete() throws Exception {
        if (id != null) {
            sysOrgGroup = this.sysOrgGroupService.getById(id);
            if (sysOrgGroup != null) {
                sysOrgGroupService.delete(sysOrgGroup);
            }
        }
        return "success";
    }


    public String validateName() throws Exception {
        JSONObject item = new JSONObject();
        item.put("result", false);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (sysOrgGroup != null) {
                if (StringUtils.isNotBlank(sysOrgGroup.getGroupName())) {
                    String ignore = getParameter("ignore");
                    if (StringUtils.isNotBlank(ignore)) {
                        if (ignore.equals(sysOrgGroup.getGroupName())) {
                            item.put("result", true);
                            writeJsonByAction(item.toString());
                        } else {
                            Integer row = this.sysOrgGroupService.validateName( sysOrgGroup.getGroupName());
                            if (row.intValue() == 0) {
                                item.put("result", true);
                            }
                        }
                    } else {
                        Integer row = this.sysOrgGroupService.validateName( sysOrgGroup.getGroupName());
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

    @PageFlow(result = {@Result(name = "success", path = "/sys/orgGroup.dhtml", type = Dispatcher.Redirect)})
    public String stop() throws Exception {
        if (id != null) {
            sysOrgGroup = this.sysOrgGroupService.getById(id);
            if (sysOrgGroup != null) {
                sysOrgGroup.setUseYn("N");
                sysOrgGroupService.save(sysOrgGroup);
            }
        }
        return "success";
    }
    @PageFlow(result = {@Result(name = "success", path = "/sys/orgGroup.dhtml", type = Dispatcher.Redirect)})
    public String play() throws Exception {
        if (id != null) {
            sysOrgGroup = this.sysOrgGroupService.getById(id);
            if (sysOrgGroup != null) {
                sysOrgGroup.setUseYn("Y");
                sysOrgGroupService.save(sysOrgGroup);
            }
        }
        return "success";
    }

}
