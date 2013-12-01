package com.dqy.sys.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.hr.entity.HrDepartment;
import com.dqy.hr.entity.HrUser;
import com.dqy.hr.service.HrDepartmentService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private HrDepartmentService hrDepartmentService;

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
    private Long orgId;

    @ReqGet
    @ReqSet
    private Long deptId;

    @ReqGet
    @ReqSet
    private Long userId;


    @ReqSet
    private List<SysOrg> orgList;

    @ReqSet
    private List<String> roleList;

    @ReqSet
    private Map<String, String> roleNameMap;

    @ReqSet
    private List<String> myRoleList;

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
                    } else {
                        orgList = orgAllList;
                    }
                }
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/sys/authorized/dept.ftl", type = Dispatcher.FreeMarker)})
    public String dept() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (id != null && userInfo != null) {
            sysAuthorized = this.sysAuthorizedService.getById(id);
            if (sysAuthorized != null) {
                SysOrg sysOrg = sysAuthorized.getOrgId();
                if (sysOrg != null) {
                    orgId = sysOrg.getId();
                }
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/sys/authorized/role.ftl", type = Dispatcher.FreeMarker)})
    public String role() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (id != null && userInfo != null) {
            sysAuthorized = this.sysAuthorizedService.getById(id);
            if (sysAuthorized != null) {
                roleList = new ArrayList<String>();
                roleList.add("SYS_GROUP");
                roleList.add("SYS_USER");
                roleList.add("SYS_APPROVE");
                roleList.add("SYS_FINANCIAL");
                roleList.add("SYS_BUDGET");
                roleList.add("LOOK_BUDGET");
                roleList.add("SET_BUDGET");
                roleList.add("APPROVE_BUDGET");
                roleList.add("TASK_FINANCIAL");
                roleList.add("GENERAL");

                roleNameMap = new HashMap<String, String>();
                roleNameMap.put("SYS_GROUP", "集团机构管理");
                roleNameMap.put("SYS_USER", "部门用户管理");
                roleNameMap.put("SYS_APPROVE", "审批岗位设置");
                roleNameMap.put("SYS_FINANCIAL", "财务科目管理");
                roleNameMap.put("SYS_BUDGET", "预算科目管理");
                roleNameMap.put("LOOK_BUDGET", "预算查看");
                roleNameMap.put("SET_BUDGET","预算填报");
                roleNameMap.put("APPROVE_BUDGET","预算审核");
                roleNameMap.put("TASK_FINANCIAL", "财务审批");
                roleNameMap.put("GENERAL", "普通用户");

                String roleId = sysAuthorized.getRoleId();
                if (StringUtils.isNotBlank(roleId)) {
                    String roleIds[] = roleId.split(",");
                    if (roleIds != null && roleIds.length > 0) {
                        myRoleList = new ArrayList<String>();
                        for (String r : roleIds) {
                            myRoleList.add(r);
                        }
                    }
                }
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Token
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        List<SysAuthorized> authorizedList = null;
        if (userInfo != null && userId != null) {
            SysOrg oldOrg = this.sysOrgService.getById(userInfo.getOrgId());
            if (oldOrg != null) {
                String[] orgIds = getHttpServletRequest().getParameterValues("orgId");
                if (orgIds != null && orgIds.length > 0) {
                    SysOrgGroup orgGroup = this.sysOrgGroupService.getById(userInfo.getGroupId());
                    hrUser = this.hrUserService.getById(userId);
                    if (orgGroup != null && hrUser != null) {
                        authorizedList = new ArrayList<SysAuthorized>();
                        for (String oID : orgIds) {
                            Long orgId = BeanUtils.convertValue(oID, Long.class);
                            if (orgId != null) {
                                SysOrg org = this.sysOrgService.getById(orgId);
                                if (org != null) {
                                    SysAuthorized authorized = new SysAuthorized();
                                    authorized.setGroupId(orgGroup);
                                    authorized.setUserId(hrUser);
                                    authorized.setOrgId(org);
                                    authorized.setOldOrgId(oldOrg);
                                    authorized.setUseYn("Y");
                                    authorized.setRoleId("GENERAL");
                                    bind(authorized);
                                    authorizedList.add(authorized);
                                }
                            }
                        }
                    }
                }
            }
            if (authorizedList != null && !authorizedList.isEmpty()) {
                this.sysAuthorizedService.save(authorizedList);
            }
        }
        return "saveSuccess";
    }

    @Token
    public String saveRole() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && id != null) {
            sysAuthorized = this.sysAuthorizedService.getById(id);
            if (sysAuthorized != null) {
                String[] roleIds = getHttpServletRequest().getParameterValues("roleIds");
                String role = "";
                if (roleIds != null && roleIds.length > 0) {
                    for (String r : roleIds) {
                        role += r + ",";
                    }
                }
                if (StringUtils.isBlank(role)) {
                    role = "GENERAL";
                }
                sysAuthorized.setRoleId(role);
                bind(sysAuthorized);
                this.sysAuthorizedService.save(sysAuthorized);
            }
        }
        return "saveSuccess";
    }

    @Token
    public String saveDept() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && id != null && deptId != null) {
            sysAuthorized = this.sysAuthorizedService.getById(id);
            HrDepartment department = this.hrDepartmentService.getById(deptId);
            if (sysAuthorized != null && department != null) {
                sysAuthorized.setDeptId(department);
                bind(sysAuthorized);
                this.sysAuthorizedService.save(sysAuthorized);
            }
        }
        return "saveSuccess";
    }


    @PageFlow(result = {@Result(name = "success", path = "/hr/user!view.dhtml?id=${hrUser.id}", type = Dispatcher.Redirect)})
    public String delete() throws Exception {
        if (id != null) {
            sysAuthorized = this.sysAuthorizedService.getById(id);
            if (sysAuthorized != null) {
                hrUser = sysAuthorized.getUserId();
                sysAuthorizedService.delete(sysAuthorized);
            }
        }
        return "success";
    }
}
