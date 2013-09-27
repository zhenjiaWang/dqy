package com.dqy.common.action;

import com.dqy.hr.service.HrUserService;
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
@Action(name = "login", namespace = "/common")
public class LoginAction extends BaseAction {

    @Inject
    private HrUserService hrUserService;

    @Override
    @PageFlow(result = {@Result(name = "success", path = "/common/login!index.dhtml", type = Dispatcher.Redirect),
            @Result(name = "login", path = "/view/authorize.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        System.out.println("wz121j");
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/index.ftl", type = Dispatcher.FreeMarker)})
    public String index() throws Exception {
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }
}
