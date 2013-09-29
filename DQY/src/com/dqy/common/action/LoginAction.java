package com.dqy.common.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.hr.entity.HrUser;
import com.dqy.hr.service.HrUserService;
import com.dqy.sys.entity.SysAuthorized;
import com.dqy.sys.entity.SysOrg;
import com.dqy.sys.service.SysAdminService;
import com.dqy.sys.service.SysAuthorizedService;
import com.dqy.sys.service.SysOrgService;
import com.google.inject.Inject;
import net.sf.json.JSONObject;
import ognl.OgnlException;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.commons.lang.StringUtils;
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
    private SysAdminService sysAdminService;

    @Inject
    private SysAuthorizedService sysAuthorizedService;

    @ReqGet
    private String orgNo;

    @ReqGet
    private Long authOrgId;

    @ReqGet
    private String userNo;

    @ReqGet
    private String userPwd;

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
                    userInfo.setDepartment(get(hrUser, "deptId.deptName"));
                    userInfo.setJob(hrUser.getJobName());
                    userInfo.setGroupId(hrUser.getGroupId().getId());
                    userInfo.setGroupName(get(hrUser, "groupId.groupName"));
                    userInfo.setOrgId(hrUser.getOrgId().getId());
                    userInfo.setOrgName(get(hrUser, "orgId.orgName"));
                    userInfo.setOrgNo(get(hrUser, "orgId.orgNo"));
                    userInfo.setAuthorize(true);
                    Integer adminCount=sysAdminService.validateAdmin(userInfo.getOrgId(),userInfo.getUserId());
                    if(adminCount==null){
                        adminCount=0;
                    }
                    if(adminCount.intValue()>0){
                        userInfo.setAdmin(true);
                    }else{
                        userInfo.setAdmin(false);
                    }

                    List<SysAuthorized> authorizedList= sysAuthorizedService.getAuthorizedList(userInfo.getGroupId(), userInfo.getUserId());
                    if(authorizedList!=null&&!authorizedList.isEmpty()){
                        List<SysOrg> authOrgList=new ArrayList<SysOrg>();
                        for(SysAuthorized authorized:authorizedList){
                            SysOrg authOrg=new SysOrg();
                            authOrg.setId(authorized.getOrgId().getId());
                            authOrg.setOrgName(authorized.getOrgId().getOrgName());
                            authOrg.setOrgNo(authorized.getOrgId().getOrgNo());
                            authOrgList.add(authOrg);
                        }
                        userInfo.setAuthOrgList(authOrgList);
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
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/common/login!index.dhtml", type = Dispatcher.Redirect)})
    public String authOrg() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null&&authOrgId!=null) {
            Integer authOrgCount= this.sysAuthorizedService.validateAuthOrg(userInfo.getGroupId(),authOrgId,userInfo.getUserId());
            if(authOrgCount==null){
                authOrgCount=0;
            }
            if(authOrgCount.intValue()>0){
                SysOrg authOrg=this.sysOrgService.getById(authOrgId);
                if(authOrg!=null){
                    userInfo.setOrgId(authOrgId);
                    userInfo.setOrgName(authOrg.getOrgName());
                    userInfo.setOrgNo(authOrg.getOrgNo());
                }
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
        Integer userCount=0;
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
