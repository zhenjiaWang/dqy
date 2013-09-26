package com.dqy.sys.action;

import com.dqy.hr.service.HrUserService;
import com.dqy.sys.entity.SysOrg;
import com.dqy.sys.entity.SysOrgGroup;
import com.dqy.sys.service.SysOrgGroupService;
import com.google.inject.Inject;
import org.guiceside.web.action.BaseAction;
import org.guiceside.web.annotation.Action;
import org.guiceside.web.annotation.Dispatcher;
import org.guiceside.web.annotation.PageFlow;
import org.guiceside.web.annotation.Result;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjiaWang
 * Date: 12-7-12
 * Time: 下午9:49
 * To change this template use File | Settings | File Templates.
 */
@Action(name = "orgGroup", namespace = "/sys")
public class SysOrgGroupAction extends BaseAction {

    @Inject
    private SysOrgGroupService sysOrgGroupService;

    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/sys/orgGroup/list.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

}
