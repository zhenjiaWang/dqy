package com.dqy.hr.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.hr.entity.HrDepartment;
import com.dqy.hr.entity.HrUser;
import com.dqy.hr.entity.HrUserDetail;
import com.dqy.hr.service.HrDepartmentService;
import com.dqy.hr.service.HrUserDetailService;
import com.dqy.hr.service.HrUserService;
import com.dqy.sys.entity.SysAdmin;
import com.dqy.sys.entity.SysAuthorized;
import com.dqy.sys.entity.SysOrg;
import com.dqy.sys.entity.SysOrgGroup;
import com.dqy.sys.service.SysAdminService;
import com.dqy.sys.service.SysAuthorizedService;
import com.dqy.sys.service.SysOrgGroupService;
import com.dqy.sys.service.SysOrgService;
import com.dqy.web.support.ActionSupport;
import com.google.inject.Inject;
import org.guiceside.commons.lang.DateFormatUtil;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.persistence.entity.search.SelectorUtils;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;
import org.guiceside.web.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjiaWang
 * Date: 12-7-12
 * Time: 下午9:49
 * To change this template use File | Settings | File Templates.
 */
@Action(name = "user", namespace = "/hr")
public class HrUserAction extends ActionSupport<HrUser> {

    @Inject
    private HrUserService hrUserService;

    @Inject
    private SysOrgService sysOrgService;

    @Inject
    private SysOrgGroupService sysOrgGroupService;

    @Inject
    private HrUserDetailService hrUserDetailService;

    @Inject
    private HrDepartmentService hrDepartmentService;

    @Inject
    private SysAdminService sysAdminService;

    @Inject
    private SysAuthorizedService sysAuthorizedService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private HrUser hrUser;

    @ReqSet
    private HrUserDetail hrUserDetail;


    @ReqGet
    @ReqSet
    private Long id;


    @ReqSet
    private List<HrUser> userList;

    @ReqSet
    private List<SysAuthorized> authorizedList;

    @ReqSet
    private boolean isAdmin;

    @ReqSet
    private boolean isPassword;

    @ReqGet
    private String adminYn;


    @ReqSet
    private boolean isAuthorized;


    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/hr/user/list.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("sys");
            userInfo.setLeftMenu("user");
            pageObj = this.hrUserService.getPageList(getStart(), rows, searchModeCallback());
            if (pageObj != null) {
                userList = pageObj.getResultList();
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }


    private List<Selector> searchModeCallback() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            selectorList.add(SelectorUtils.$eq("groupId.id", userInfo.getGroupId()));
            selectorList.add(SelectorUtils.$eq("orgId.id", userInfo.getOrgId()));
            selectorList.add(SelectorUtils.$order("userNo"));
        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/hr/user/view.ftl", type = Dispatcher.FreeMarker)})
    public String view() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && id != null) {
            hrUser = this.hrUserService.getById(id);
            if(hrUser!=null){
                if(StringUtils.isBlank(hrUser.getUserPwd())){
                    isPassword=false;
                }else{
                    isPassword=true;
                }
                hrUserDetail=this.hrUserDetailService.getByUserId(hrUser.getId());
                isAdmin = false;
                Integer adminCount=this.sysAdminService.validateAdmin(userInfo.getOrgId(),hrUser.getId());
                if(adminCount==null){
                    adminCount=0;
                }
                if(adminCount.intValue()>0){
                    isAdmin=true;
                }
                List<SysOrg> orgAllList = this.sysOrgService.getOrgListByGroupId(userInfo.getGroupId());
                authorizedList=this.sysAuthorizedService.getAuthorizedList(userInfo.getGroupId(),hrUser.getId());
                if(orgAllList!=null&&!orgAllList.isEmpty()){
                    if(authorizedList!=null&&!authorizedList.isEmpty()){
                        isAuthorized=orgAllList.size()==authorizedList.size()?false:true;
                    }else{
                        isAuthorized=true;
                    }
                }
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/hr/user!view.dhtml?id=${hrUser.id}", type = Dispatcher.Redirect)})
    public String changeAdmin() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && id != null&& StringUtils.isNotBlank(adminYn)) {
            hrUser = this.hrUserService.getById(id);
            if(hrUser!=null){
                SysAdmin sysAdmin= sysAdminService.getAdminByOrgIdUserId(userInfo.getOrgId(), hrUser.getId());
                if(adminYn.equals("N")){
                    if(sysAdmin!=null){
                        this.sysAdminService.delete(sysAdmin);
                    }
                }else if(adminYn.equals("Y")){
                    if(sysAdmin==null){
                        sysAdmin=new SysAdmin();
                        SysOrg sysOrg=this.sysOrgService.getById(userInfo.getOrgId());
                        if(sysOrg!=null){
                            sysAdmin.setOrgId(sysOrg);
                            sysAdmin.setUserId(hrUser);
                            sysAdmin.setUseYn("Y");
                            bind(sysAdmin);
                        }
                        this.sysAdminService.save(sysAdmin);
                    }
                }
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/hr/user/pwd.ftl", type = Dispatcher.FreeMarker)})
    public String changePwd() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && id != null) {
            hrUser = this.hrUserService.getById(id);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/hr/user!view.dhtml?id=${hrUser.id}", type = Dispatcher.Redirect)})
    public String savePwd() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && hrUser != null) {
            HrUser old = this.hrUserService.getById(hrUser.getId());
            old.setUserPwd(hrUser.getUserPwd());
            old.setUseYn("Y");
            bind(old);
            hrUserService.save(old);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/hr/user/input.ftl", type = Dispatcher.FreeMarker)})
    public String input() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (id != null && userInfo != null) {
            hrUser = this.hrUserService.getById(id);
            if(hrUser!=null){
                hrUserDetail=this.hrUserDetailService.getByUserId(hrUser.getId());
                if(hrUserDetail!=null){
                    hrUser.setUserSex(hrUserDetail.getUserSex());
                    hrUser.setBirthday(hrUserDetail.getBirthday());
                    hrUser.setEduLevel(hrUserDetail.getEduLevel());
                }
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }
    @Token
    @PageFlow(result = {@Result(name = "success", path = "/hr/user!view.dhtml?id=${hrUser.id}", type = Dispatcher.Redirect)})
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && hrUser != null) {
            if (hrUser.getId() != null) {
                HrUser old = this.hrUserService.getById(hrUser.getId());
                old.setJobName(hrUser.getJobName());
                old.setUserName(hrUser.getUserName());
                HrDepartment department=this.hrDepartmentService.getById(hrUser.getDeptId().getId());
                if(department!=null){
                    old.setDeptId(department);
                }
                old.setWorkArea(hrUser.getWorkArea());
                old.setEntryDate(hrUser.getEntryDate());
                old.setUserState(hrUser.getUserState());
                hrUserDetail=this.hrUserDetailService.getByUserId(hrUser.getId());
                if(hrUserDetail!=null){
                    hrUserDetail.setEduLevel(hrUser.getEduLevel());
                    hrUserDetail.setUserSex(hrUser.getUserSex());
                    hrUserDetail.setBirthday(hrUser.getBirthday());
                }
                this.bind(old);
                this.bind(hrUserDetail);
                this.hrUserService.save(old,hrUserDetail);
            }else{
                Date entryDate=hrUser.getEntryDate();
                if(entryDate!=null){
                    Integer entryCount=this.hrUserService.getCountUserByEntryDate(userInfo.getOrgId(),userInfo.getGroupId(),entryDate);
                    if(entryCount==null){
                        entryCount=0;
                    }
                    entryCount+=1;
                    Integer year=DateFormatUtil.getDayInYear(entryDate);
                    Integer month=DateFormatUtil.getDayInMonth(entryDate)+1;
                    String userNo;
                    if(month.intValue()<10){
                        userNo=year+"0"+month;
                    }else{
                        userNo=year+""+month;
                    }
                    if(entryCount.intValue()<10){
                        userNo+="000"+entryCount;
                    }else if(entryCount.intValue()<100){
                        userNo+="00"+entryCount;
                    }else if(entryCount.intValue()<1000){
                        userNo+="0"+entryCount;
                    }
                    hrUser.setUserNo(userNo);
                    if(hrUser.getDeptId()!=null){
                        if(hrUser.getDeptId().getId()!=null){
                            HrDepartment department=hrDepartmentService.getById(hrUser.getDeptId().getId());
                            if(department!=null){
                                hrUser.setDeptId(department);
                            }
                        }
                    }
                    SysOrg sysOrg=this.sysOrgService.getById(userInfo.getOrgId());
                    if(sysOrg!=null){
                        hrUser.setOrgId(sysOrg);
                    }
                    SysOrgGroup sysOrgGroup=this.sysOrgGroupService.getById(userInfo.getGroupId());
                    if(sysOrgGroup!=null){
                        hrUser.setGroupId(sysOrgGroup);
                    }
                    hrUser.setUseYn("Y");
                    bind(hrUser);
                    hrUserDetail=new HrUserDetail();
                    hrUserDetail.setUserId(hrUser);
                    hrUserDetail.setBirthday(hrUser.getBirthday());
                    hrUserDetail.setEduLevel(hrUser.getEduLevel());
                    hrUserDetail.setUserSex(hrUser.getUserSex());
                    hrUserDetail.setUseYn("Y");
                    bind(hrUserDetail);
                    this.hrUserService.save(hrUser,hrUserDetail);
                }

            }
        }
        return "success";
    }
}
