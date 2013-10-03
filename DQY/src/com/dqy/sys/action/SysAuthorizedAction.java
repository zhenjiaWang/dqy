package com.dqy.sys.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.hr.entity.HrUser;
import com.dqy.hr.service.HrUserService;
import com.dqy.sys.entity.SysAuthorized;
import com.dqy.sys.entity.SysOrg;
import com.dqy.sys.entity.SysOrgGroup;
import com.dqy.sys.service.SysAuthorizedService;
import com.dqy.sys.service.SysOrgGroupService;
import com.dqy.sys.service.SysOrgService;
import com.dqy.web.support.ActionSupport;
import com.google.inject.Inject;
import net.sf.json.JSONObject;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.commons.lang.StringUtils;
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
@Action(name = "authorized", namespace = "/sys")
public class SysAuthorizedAction extends ActionSupport<SysAuthorized> {

    @Inject
    private SysAuthorizedService sysAuthorizedService;

    @Inject
    private SysOrgGroupService sysOrgGroupService;

    @Inject
    private HrUserService hrUserService;

    @Inject
    private SysOrgService sysOrgService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private SysAuthorized sysAuthorized;

    @ReqSet
    private HrUser hrUser;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private Long userId;


    @ReqSet
    private List<SysOrg> orgList;

    @Override
    public String execute() throws Exception {
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/sys/authorized/input.ftl", type = Dispatcher.FreeMarker)})
    public String input() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userId != null && userInfo != null) {
            hrUser = hrUserService.getById(userId);
            if (hrUser != null) {
                List<SysAuthorized> authorizedList = this.sysAuthorizedService.getAuthorizedList(userInfo.getGroupId(), hrUser.getId());
                List<SysOrg> orgAllList = this.sysOrgService.getOrgListByGroupId(userInfo.getGroupId());
                if (orgAllList != null && !orgAllList.isEmpty()) {
                    orgList = new ArrayList<SysOrg>();
                    if (authorizedList != null && !authorizedList.isEmpty()) {
                        for (SysOrg org : orgAllList) {
                            Integer count = sysAuthorizedService.validateAuthOrg(userInfo.getGroupId(), org.getId(), hrUser.getId());
                            if (count == null) {
                                count = 0;
                            }
                            if (count.intValue() == 0) {
                                orgList.add(org);
                            }
                        }
                    }
                }
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Token
    @PageFlow(result = {@Result(name = "success", path = "/hr/user!view.dhtml?id=${hrUser.id}", type = Dispatcher.Redirect)})
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        List<SysAuthorized> authorizedList=null;
        if (userInfo != null && userId != null) {
            String[] orgIds = getHttpServletRequest().getParameterValues("orgId");
            if (orgIds != null && orgIds.length > 0) {
                SysOrgGroup orgGroup = this.sysOrgGroupService.getById(userInfo.getGroupId());
                hrUser = this.hrUserService.getById(userId);
                if (orgGroup != null && hrUser != null) {
                    authorizedList=new ArrayList<SysAuthorized>();
                    for (String oID : orgIds) {
                        Long orgId = BeanUtils.convertValue(oID, Long.class);
                        if (orgId != null) {
                            SysOrg org = this.sysOrgService.getById(orgId);
                            if (org != null) {
                                SysAuthorized authorized = new SysAuthorized();
                                authorized.setGroupId(orgGroup);
                                authorized.setUserId(hrUser);
                                authorized.setOrgId(org);
                                authorized.setUseYn("Y");
                                bind(authorized);
                                authorizedList.add(authorized);
                            }
                        }
                    }
                }
            }
            if(authorizedList!=null&&!authorizedList.isEmpty()){
                this.sysAuthorizedService.save(authorizedList);
            }
        }
        return "success";
    }


    @PageFlow(result = {@Result(name = "success", path = "/hr/user!view.dhtml?id=${hrUser.id}", type = Dispatcher.Redirect)})
    public String delete() throws Exception {
        if (id != null) {
            sysAuthorized = this.sysAuthorizedService.getById(id);
            if(sysAuthorized!=null){
                hrUser=sysAuthorized.getUserId();
                sysAuthorizedService.delete(sysAuthorized);
            }
        }
        return "success";
    }
}
