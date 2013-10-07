package com.dqy.wf.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.sys.entity.SysOrg;
import com.dqy.sys.service.SysOrgService;
import com.dqy.web.support.ActionSupport;
import com.dqy.wf.entity.WfVariableGlobal;
import com.dqy.wf.service.WfVariableGlobalService;
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
@Action(name = "variableGlobal", namespace = "/wf")
public class WfVariableGlobalAction extends ActionSupport<WfVariableGlobal> {

    @Inject
    private WfVariableGlobalService wfVariableGlobalService;

    @Inject
    private SysOrgService sysOrgService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private WfVariableGlobal wfVariableGlobal;

    @ReqGet
    @ReqSet
    private Long id;


    @ReqSet
    private List<WfVariableGlobal> variableGlobalList;

    private List<Selector> searchModeCallback() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            selectorList.add(SelectorUtils.$eq("orgId.id", userInfo.getOrgId()));
        }
        return selectorList;
    }


    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/wf/variableGlobal/list.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("sys");
            userInfo.setLeftMenu("variableGlobal");
            pageObj = this.wfVariableGlobalService.getPageList(getStart(), rows, searchModeCallback());
            if(pageObj!=null){
                variableGlobalList=pageObj.getResultList();
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }


    @PageFlow(result = {@Result(name = "success", path = "/view/wf/variableGlobal/input.ftl", type = Dispatcher.FreeMarker)})
    public String input() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (id != null && userInfo != null) {
            wfVariableGlobal = this.wfVariableGlobalService.getById(id);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Token
    @PageFlow(result = {@Result(name = "success", path = "/wf/variableGlobal.dhtml", type = Dispatcher.Redirect)})
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && wfVariableGlobal != null) {
            if (wfVariableGlobal.getId() != null) {
                WfVariableGlobal old = this.wfVariableGlobalService.getById(wfVariableGlobal.getId());
                wfVariableGlobal = this.copy(wfVariableGlobal, old);
            }
            SysOrg sysOrg=sysOrgService.getById(userInfo.getOrgId());
            if(sysOrg!=null){
                wfVariableGlobal.setOrgId(sysOrg);
            }
            wfVariableGlobal.setVariableName(wfVariableGlobal.getVariableName().trim());
            wfVariableGlobal.setUseYn("Y");
            this.bind(wfVariableGlobal);
            this.wfVariableGlobalService.save(wfVariableGlobal);
        }
        return "success";
    }

    @PageFlow(result = {@Result(name = "success", path = "/wf/variableGlobal.dhtml", type = Dispatcher.Redirect)})
    public String delete() throws Exception {
        if (id != null) {
            wfVariableGlobal = this.wfVariableGlobalService.getById(id);
            if (wfVariableGlobal != null) {
                wfVariableGlobalService.delete(wfVariableGlobal);
            }
        }
        return "success";
    }

    @PageFlow(result = {@Result(name = "success", path = "/wf/variableGlobal.dhtml", type = Dispatcher.Redirect)})
    public String stop() throws Exception {
        if (id != null) {
            wfVariableGlobal = this.wfVariableGlobalService.getById(id);
            if (wfVariableGlobal != null) {
                wfVariableGlobal.setUseYn("N");
                wfVariableGlobalService.save(wfVariableGlobal);
            }
        }
        return "success";
    }
    @PageFlow(result = {@Result(name = "success", path = "/wf/variableGlobal.dhtml", type = Dispatcher.Redirect)})
    public String play() throws Exception {
        if (id != null) {
            wfVariableGlobal = this.wfVariableGlobalService.getById(id);
            if (wfVariableGlobal != null) {
                wfVariableGlobal.setUseYn("Y");
                wfVariableGlobalService.save(wfVariableGlobal);
            }
        }
        return "success";
    }

    public String validateName() throws Exception {
        JSONObject item = new JSONObject();
        item.put("result", false);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (wfVariableGlobal != null) {
                if (StringUtils.isNotBlank(wfVariableGlobal.getVariableName())) {
                    String ignore = getParameter("ignore");
                    if (StringUtils.isNotBlank(ignore)) {
                        if (ignore.equals(wfVariableGlobal.getVariableName())) {
                            item.put("result", true);
                            writeJsonByAction(item.toString());
                        } else {
                            Integer row = this.wfVariableGlobalService.validateName(userInfo.getOrgId(), wfVariableGlobal.getVariableName());
                            if (row.intValue() == 0) {
                                item.put("result", true);
                            }
                        }
                    } else {
                        Integer row = this.wfVariableGlobalService.validateName(userInfo.getOrgId(), wfVariableGlobal.getVariableName());
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
