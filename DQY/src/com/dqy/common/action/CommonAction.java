package com.dqy.common.action;

import com.dqy.hr.service.HrUserService;
import com.google.inject.Inject;
import org.guiceside.web.action.BaseAction;
import org.guiceside.web.annotation.Action;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjiaWang
 * Date: 12-7-12
 * Time: 下午9:49
 * To change this template use File | Settings | File Templates.
 */
@Action(name = "common", namespace = "/common")
public class CommonAction extends BaseAction {

    @Inject
    private HrUserService hrUserService;

    @Override
    public String execute() throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
