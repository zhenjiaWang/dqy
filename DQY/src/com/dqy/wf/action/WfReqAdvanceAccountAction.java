package com.dqy.wf.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.sys.entity.SysOrg;
import com.dqy.sys.service.SysOrgService;
import com.dqy.web.support.ActionSupport;
import com.dqy.wf.entity.WfReqAdvanceAccount;
import com.dqy.wf.entity.WfVariableGlobal;
import com.dqy.wf.service.WfReqAdvanceAccountService;
import com.dqy.wf.service.WfReqService;
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
@Action(name = "advanceAccount", namespace = "/wf")
public class WfReqAdvanceAccountAction extends ActionSupport<WfReqAdvanceAccount> {

    @Inject
    private WfVariableGlobalService wfVariableGlobalService;

    @Inject
    private WfReqService wfReqService;

    @Inject
    private WfReqAdvanceAccountService wfReqAdvanceAccountService;

    @Inject
    private SysOrgService sysOrgService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private WfReqAdvanceAccount wfReqAdvanceAccount;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqSet
    public final String applyId="ADVANCE_ACCOUNT";



    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/wf/advanceAccount/input.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("apply");
            userInfo.setLeftMenu("advanceAccount");
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

}
