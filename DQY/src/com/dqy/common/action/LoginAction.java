package com.dqy.common.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.hr.entity.HrUser;
import com.dqy.hr.service.HrUserService;
import com.dqy.sys.entity.SysAuthorized;
import com.dqy.sys.entity.SysFinancialTitle;
import com.dqy.sys.entity.SysOrg;
import com.dqy.sys.service.SysAuthorizedService;
import com.dqy.sys.service.SysFinancialTitleService;
import com.dqy.sys.service.SysOrgService;
import com.dqy.wf.entity.WfReq;
import com.dqy.wf.entity.WfReqTask;
import com.dqy.wf.service.WfReqService;
import com.dqy.wf.service.WfReqTaskService;
import com.google.inject.Inject;
import net.sf.json.JSONObject;
import ognl.OgnlException;
import org.guiceside.commons.Page;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.persistence.entity.search.SelectorUtils;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;
import org.guiceside.web.action.BaseAction;
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
@Action(name = "login", namespace = "/common")
public class LoginAction extends BaseAction {

    @Inject
    private HrUserService hrUserService;

    @Inject
    private SysOrgService sysOrgService;

    @Inject
    private SysFinancialTitleService sysFinancialTitleService;

    @Inject
    private SysAuthorizedService sysAuthorizedService;

    @Inject
    private WfReqTaskService wfReqTaskService;

    @Inject
    private WfReqService wfReqService;

    @ReqGet
    private String orgNo;

    @ReqGet
    private Long authOrgId;

    @ReqGet
    private String userNo;

    @ReqGet
    private String userPwd;

    @ReqSet
    private List<WfReq> reqList;

    @ReqSet
    private List<WfReqTask> taskList;

    @Override
    public String execute() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("login", "-1");
        HrUser hrUser = null;
        if (StringUtils.isNotBlank(orgNo)
                && StringUtils.isNotBlank(userNo) && StringUtils.isNotBlank(userPwd)) {
            try {
                hrUser = this.hrUserService.getLoginUser(orgNo, userNo, userPwd);
                if (hrUser != null) {
                    UserInfo userInfo = UserSession.create(getHttpServletRequest());
                    userInfo.setLoggedIn(true);
                    userInfo.setUserId(hrUser.getId());
                    userInfo.setUserNo(hrUser.getUserNo());
                    userInfo.setUserName(hrUser.getUserName());
                    userInfo.setDepartmentId(hrUser.getDeptId().getId());
                    userInfo.setDepartment(get(hrUser, "deptId.deptName"));
                    userInfo.setJob(hrUser.getJobName());
                    userInfo.setGroupId(hrUser.getGroupId().getId());
                    userInfo.setGroupName(get(hrUser, "groupId.groupName"));
                    userInfo.setOrgId(hrUser.getOrgId().getId());
                    userInfo.setOrgName(get(hrUser, "orgId.orgName"));
                    userInfo.setOrgNo(get(hrUser, "orgId.orgNo"));
                    userInfo.setAuthorize(true);


                    List<SysAuthorized> authorizedList = sysAuthorizedService.getAuthorizedList(userInfo.getGroupId(), userInfo.getUserId());
                    if (authorizedList != null && !authorizedList.isEmpty()) {
                        List<SysOrg> authOrgList = new ArrayList<SysOrg>();
                        for (SysAuthorized authorized : authorizedList) {
                            SysOrg authOrg = new SysOrg();
                            authOrg.setId(authorized.getOrgId().getId());
                            authOrg.setOrgName(authorized.getOrgId().getOrgName());
                            authOrg.setOrgNo(authorized.getOrgId().getOrgNo());
                            authOrg.setTip(0);
                            authOrgList.add(authOrg);
                        }
                        userInfo.setAuthOrgList(authOrgList);
                    }
                    SysAuthorized authorized = this.sysAuthorizedService.getAuthOrg(userInfo.getGroupId(), userInfo.getOrgId(), userInfo.getUserId());
                    userInfo.setRoleList(null);
                    userInfo.setRoleId(null);
                    if (authorized != null) {
                        String roleId = authorized.getRoleId();
                        if (StringUtils.isNotBlank(roleId)) {
                            userInfo.setRoleId(roleId);
                            String[] roleIds = roleId.split(",");
                            if (roleIds != null && roleIds.length > 0) {
                                List<String> roleList = new ArrayList<String>();
                                for (String r : roleIds) {
                                    roleList.add(r);
                                }
                                userInfo.setRoleList(roleList);
                            }
                        }
                    }
                    jsonObject.put("login", "0");
                    jsonObject.put("url", "/common/login!index.dhtml");
                }
            } catch (Exception e) {

            }
        }
        writeJsonByAction(jsonObject.toString());
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/index.ftl", type = Dispatcher.FreeMarker)})
    public String index() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("index");

            Integer unRead = wfReqTaskService.getCountUnRead(userInfo.getOrgId(), userInfo.getUserId());
            if (unRead == null) {
                unRead = 0;
            }
            userInfo.setTaskUnRead(unRead);

            Integer unApprove = wfReqTaskService.getCountUnApprove(userInfo.getOrgId(), userInfo.getUserId());
            if (unApprove == null) {
                unApprove = 0;
            }
            userInfo.setTaskUnApprove(unApprove);

            Integer reqPassed = wfReqService.getCountPassed(userInfo.getOrgId(), userInfo.getUserId());
            if (reqPassed == null) {
                reqPassed = 0;
            }
            userInfo.setReqPassed(reqPassed);

            Integer reqRejected = wfReqService.getCountRejected(userInfo.getOrgId(), userInfo.getUserId());
            if (reqRejected == null) {
                reqRejected = 0;
            }
            userInfo.setReqRejected(reqRejected);

            List<Selector> selectorList = new ArrayList<Selector>();
            selectorList.add(SelectorUtils.$eq("orgId.id", userInfo.getOrgId()));
            selectorList.add(SelectorUtils.$eq("userId.id", userInfo.getUserId()));
            selectorList.add(SelectorUtils.$eq("taskState", 0));
            selectorList.add(SelectorUtils.$order("receiveDate", false));
            Page<WfReqTask> pageTaskObj = this.wfReqTaskService.getPageList(0, 5, selectorList);
            if(pageTaskObj!=null){
                taskList=pageTaskObj.getResultList();
            }

            selectorList.clear();
            selectorList.add(SelectorUtils.$eq("orgId.id", userInfo.getOrgId()));
            selectorList.add(SelectorUtils.$eq("userId.id", userInfo.getUserId()));
            selectorList.add(SelectorUtils.$order("sendDate", false));
            Page<WfReq> pageReqObj = this.wfReqService.getPageList(0, 5, selectorList);
            if(pageReqObj!=null){
                reqList=pageReqObj.getResultList();
            }

            List<SysOrg> sysOrgList=userInfo.getAuthOrgList();
            if(sysOrgList!=null&&!sysOrgList.isEmpty()){
                for(SysOrg sysOrg:sysOrgList){
                    sysOrg.setTip(0);
                    if(sysOrg.getId().equals(userInfo.getOrgId())){
                        continue;
                    }
                    unRead = wfReqTaskService.getCountUnRead(sysOrg.getId(), userInfo.getUserId());
                    if (unRead == null) {
                        unRead = 0;
                    }
                    if(unRead.intValue()>0){
                        sysOrg.setTip(1);
                        continue;
                    }
                     unApprove = wfReqTaskService.getCountUnApprove(sysOrg.getId(), userInfo.getUserId());
                    if (unApprove == null) {
                        unApprove = 0;
                    }
                    if(unApprove.intValue()>0){
                        sysOrg.setTip(1);
                        continue;
                    }

                    reqPassed = wfReqService.getCountPassed(sysOrg.getId(), userInfo.getUserId());
                    if (reqPassed == null) {
                        reqPassed = 0;
                    }
                    if(reqPassed.intValue()>0){
                        sysOrg.setTip(1);
                        continue;
                    }

                     reqRejected = wfReqService.getCountRejected(sysOrg.getId(), userInfo.getUserId());
                    if (reqRejected == null) {
                        reqRejected = 0;
                    }
                    if(reqRejected.intValue()>0){
                        sysOrg.setTip(1);
                        continue;
                    }
                }
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/common/login!index.dhtml", type = Dispatcher.Redirect),
            @Result(name = "logout", path = "/common/login!logout.dhtml", type = Dispatcher.Redirect)})
    public String authOrg() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && authOrgId != null) {
            Integer authOrgCount = this.sysAuthorizedService.validateAuthOrg(userInfo.getGroupId(), authOrgId, userInfo.getUserId());
            if (authOrgCount == null) {
                authOrgCount = 0;
            }
            if (authOrgCount.intValue() > 0) {
                SysOrg authOrg = this.sysOrgService.getById(authOrgId);
                if (authOrg != null) {
                    userInfo.setOrgId(authOrgId);
                    userInfo.setOrgName(authOrg.getOrgName());
                    userInfo.setOrgNo(authOrg.getOrgNo());

                    SysAuthorized authorized = this.sysAuthorizedService.getAuthOrg(userInfo.getGroupId(), authOrgId, userInfo.getUserId());
                    userInfo.setRoleList(null);
                    userInfo.setRoleId(null);
                    if (authorized != null) {
                        String roleId = authorized.getRoleId();
                        if (StringUtils.isNotBlank(roleId)) {
                            userInfo.setRoleId(roleId);
                            String[] roleIds = roleId.split(",");
                            if (roleIds != null && roleIds.length > 0) {
                                List<String> roleList = new ArrayList<String>();
                                for (String r : roleIds) {
                                    roleList.add(r);
                                }
                                userInfo.setRoleList(roleList);
                            }
                        }
                    }
                }
            } else {
                return "logout";
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }


    @PageFlow(result = {@Result(name = "success", path = "/view/index.html", type = Dispatcher.Redirect)})
    public String logout() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            UserSession.invalidate(getHttpServletRequest());
        }
        return "success";
    }

    public String validateUser() throws Exception {
        Integer userCount = 0;
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isNotBlank(orgNo)
                && StringUtils.isNotBlank(userNo)) {
            userCount = hrUserService.validateUserAccount(orgNo, userNo);
            if (userCount == null) {
                userCount = 0;
            }
        }
        jsonObject.put("userCount", userCount);
        writeJsonByAction(jsonObject.toString());
        return null;
    }


    protected String get(Object entity, String property) {
        Object result = null;
        if (entity != null) {
            try {
                result = BeanUtils.getValue(entity, property);
            } catch (OgnlException e) {
                result = null;
            }
        }
        return StringUtils.defaultIfEmpty(result);
    }

}
