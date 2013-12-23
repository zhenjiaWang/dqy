package com.dqy.hr.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.email.WsManagerUser;
import com.dqy.email.XmapiImpl;
import com.dqy.email.XmapiImplServiceLocator;
import com.dqy.hr.entity.HrDepartment;
import com.dqy.hr.entity.HrUser;
import com.dqy.hr.entity.HrUserDetail;
import com.dqy.hr.service.HrDepartmentService;
import com.dqy.hr.service.HrUserDetailService;
import com.dqy.hr.service.HrUserService;
import com.dqy.sys.entity.SysAuthorized;
import com.dqy.sys.entity.SysOrg;
import com.dqy.sys.entity.SysOrgGroup;
import com.dqy.sys.service.SysAuthorizedService;
import com.dqy.sys.service.SysOrgGroupService;
import com.dqy.sys.service.SysOrgService;
import com.dqy.util.EncryptUtil;
import com.dqy.web.support.ActionSupport;
import com.google.inject.Inject;
import net.sf.json.JSONObject;
import org.guiceside.commons.lang.DateFormatUtil;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.persistence.entity.search.SelectorUtils;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;
import org.guiceside.web.annotation.*;

import java.util.*;

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
    private SysAuthorizedService sysAuthorizedService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private HrUser hrUser;

    @ReqSet
    private HrUserDetail hrUserDetail;

    @ReqGet
    private String openEmail;

    @ReqSet
    private int emailErrorCode;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private String keyword;


    @ReqSet
    private List<HrUser> userList;

    @ReqSet
    private List<SysAuthorized> authorizedList;

    @ReqSet
    private boolean isPassword;

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
            selectorList.add(SelectorUtils.$alias("deptId", "deptId"));
            selectorList.add(SelectorUtils.$eq("groupId.id", userInfo.getGroupId()));
            selectorList.add(SelectorUtils.$eq("orgId.id", userInfo.getOrgId()));
            if (StringUtils.isNotBlank(keyword)) {
                selectorList.add(SelectorUtils.$or(SelectorUtils.$like("userName", keyword), SelectorUtils.$like("userNo", keyword)));
            }
            selectorList.add(SelectorUtils.$order("userNo",false));
        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/hr/user/view.ftl", type = Dispatcher.FreeMarker)})
    public String view() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && id != null) {
            hrUser = this.hrUserService.getById(id);
            if (hrUser != null) {
                if (StringUtils.isBlank(hrUser.getUserPwd())) {
                    isPassword = false;
                } else {
                    isPassword = true;
                }
                hrUserDetail = this.hrUserDetailService.getByUserId(hrUser.getId());
                List<SysOrg> orgAllList = this.sysOrgService.getOrgListByGroupId(userInfo.getGroupId());
                authorizedList = this.sysAuthorizedService.getAuthorizedList(userInfo.getGroupId(), hrUser.getId());
                if (authorizedList != null && !authorizedList.isEmpty()) {
                    Map<String, String> roleNameMap = new HashMap<String, String>();
                    roleNameMap.put("SYS_GROUP", "集团机构管理");
                    roleNameMap.put("SYS_USER", "部门用户管理");
                    roleNameMap.put("SYS_APPROVE", "审批管理");
                    roleNameMap.put("SYS_FINANCIAL", "财务科目管理");
                    roleNameMap.put("SYS_BUDGET", "预算科目管理");
                    roleNameMap.put("LOOK_BUDGET", "预算查看");
                    roleNameMap.put("SET_BUDGET", "预算填报");
                    roleNameMap.put("APPROVE_BUDGET", "预算审核");
                    roleNameMap.put("TASK_FINANCIAL", "财务审批");
                    roleNameMap.put("EMAIL_CD", "邮箱管理");
                    roleNameMap.put("EMAIL_PWD", "邮箱密码");
                    roleNameMap.put("GENERAL", "普通用户");

                    for (SysAuthorized authorized : authorizedList) {
                        String roleName = "";
                        String roleId = authorized.getRoleId();
                        if (StringUtils.isNotBlank(roleId)) {
                            String[] roleIds = roleId.split(",");
                            if (roleIds != null && roleIds.length > 0) {
                                for (String r : roleIds) {
                                    roleName += roleNameMap.get(r) + ",";
                                }
                            }
                        }
                        if (StringUtils.isBlank(roleName)) {
                            roleName = "&nbsp;";
                        }
                        authorized.setRoleName(roleName);
                    }
                }
                if (orgAllList != null && !orgAllList.isEmpty()) {
                    if (authorizedList != null && !authorizedList.isEmpty()) {
                        isAuthorized = orgAllList.size() == authorizedList.size() ? false : true;
                    } else {
                        isAuthorized = true;
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

    public String savePwd() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && hrUser != null) {
            HrUser old = this.hrUserService.getById(hrUser.getId());
            old.setUserPwd(hrUser.getUserPwd());
            old.setUseYn("Y");
            bind(old);
            hrUserService.save(old);
        }
        return "saveSuccess";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/hr/user/input.ftl", type = Dispatcher.FreeMarker)})
    public String input() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (id != null && userInfo != null) {
            hrUser = this.hrUserService.getById(id);
            if (hrUser != null) {
                hrUserDetail = this.hrUserDetailService.getByUserId(hrUser.getId());
                if (hrUserDetail != null) {
                    hrUser.setUserSex(hrUserDetail.getUserSex());
                    hrUser.setBirthday(hrUserDetail.getBirthday());
                    hrUser.setEduLevel(hrUserDetail.getEduLevel());
                }
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }



    @Token
    @PageFlow(result = {@Result(name = "success", path = "/hr/user!inputEmail.dhtml?id=${hrUser.id}", type = Dispatcher.Redirect)})
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && hrUser != null) {
            if (hrUser.getId() != null) {
                HrUser old = this.hrUserService.getById(hrUser.getId());
                old.setJobName(hrUser.getJobName());
                old.setUserName(hrUser.getUserName());
                HrDepartment department = this.hrDepartmentService.getById(hrUser.getDeptId().getId());
                if (department != null) {
                    old.setDeptId(department);
                }
                old.setWorkArea(hrUser.getWorkArea());
                old.setEntryDate(hrUser.getEntryDate());
                old.setUserState(hrUser.getUserState());
                old.setUserNo(hrUser.getUserNo());
                hrUserDetail = this.hrUserDetailService.getByUserId(hrUser.getId());
                if (hrUserDetail != null) {
                    hrUserDetail.setEduLevel(hrUser.getEduLevel());
                    hrUserDetail.setUserSex(hrUser.getUserSex());
                    hrUserDetail.setBirthday(hrUser.getBirthday());
                }
                this.bind(old);
                this.bind(hrUserDetail);
                this.hrUserService.save(old, hrUserDetail);
            } else {
                Date entryDate = hrUser.getEntryDate();
                if (entryDate != null) {
                    if (StringUtils.isBlank(hrUser.getUserNo())) {
                        Integer year = DateFormatUtil.getDayInYear(entryDate);
                        Integer month = DateFormatUtil.getDayInMonth(entryDate) + 1;
                        String monthStr=null;
                        if (month.intValue() < 10) {
                            monthStr = "0" + month;
                        } else {
                            monthStr =  "" + month;
                        }
                        String startDateStr=year+"-"+monthStr+"-01";
                        String endDateStr=year+"-"+monthStr+"-31";
                        Date startDate=DateFormatUtil.parse(startDateStr,DateFormatUtil.YEAR_MONTH_DAY_PATTERN);
                        Date endDate=DateFormatUtil.parse(endDateStr,DateFormatUtil.YEAR_MONTH_DAY_PATTERN);
                        Integer entryCount = this.hrUserService.getCountUserByEntryDate(userInfo.getOrgId(), userInfo.getGroupId(), startDate,endDate);
                        if (entryCount == null) {
                            entryCount = 0;
                        }
                        entryCount += 1;
                        String yearStr = year.toString();
                        if (StringUtils.isNotBlank(yearStr)) {
                            yearStr = yearStr.substring(2);
                        }
                        String userNo;
                        if (month.intValue() < 10) {
                            userNo = yearStr + "0" + month;
                        } else {
                            userNo = yearStr + "" + month;
                        }
                        if (entryCount.intValue() < 10) {
                            userNo += "000" + entryCount;
                        } else if (entryCount.intValue() < 100) {
                            userNo += "00" + entryCount;
                        } else if (entryCount.intValue() < 1000) {
                            userNo += "0" + entryCount;
                        }
                        hrUser.setUserNo(userNo);
                    }
                    HrDepartment department = null;
                    if (hrUser.getDeptId() != null) {
                        if (hrUser.getDeptId().getId() != null) {
                            department = hrDepartmentService.getById(hrUser.getDeptId().getId());
                            if (department != null) {
                                hrUser.setDeptId(department);
                            }
                        }
                    }
                    SysOrg sysOrg = this.sysOrgService.getById(userInfo.getOrgId());
                    if (sysOrg != null) {
                        hrUser.setOrgId(sysOrg);
                    }
                    SysOrgGroup sysOrgGroup = this.sysOrgGroupService.getById(userInfo.getGroupId());
                    if (sysOrgGroup != null) {
                        hrUser.setGroupId(sysOrgGroup);
                    }
                    hrUser.setUseYn("Y");
                    hrUser.setUserPwd("111111");
                    bind(hrUser);
                    hrUserDetail = new HrUserDetail();
                    hrUserDetail.setUserId(hrUser);
                    hrUserDetail.setBirthday(hrUser.getBirthday());
                    hrUserDetail.setEduLevel(hrUser.getEduLevel());
                    hrUserDetail.setUserSex(hrUser.getUserSex());
                    hrUserDetail.setUseYn("Y");
                    bind(hrUserDetail);

                    SysAuthorized authorized = new SysAuthorized();
                    authorized.setGroupId(sysOrgGroup);
                    authorized.setUserId(hrUser);
                    authorized.setDeptId(department);
                    authorized.setOldOrgId(sysOrg);
                    authorized.setOrgId(sysOrg);
                    authorized.setUseYn("Y");
                    authorized.setRoleId("GENERAL");
                    bind(authorized);
                    this.hrUserService.save(hrUser, hrUserDetail, authorized);
                }

            }
        }
        if (StringUtils.isNotBlank(openEmail)) {
            if (openEmail.equals("Y")) {
                return "success";
            }
        }
        return "saveSuccess";
    }

    public String validateNo() throws Exception {
        JSONObject item = new JSONObject();
        item.put("result", false);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (hrUser != null) {
                if (StringUtils.isNotBlank(hrUser.getUserNo())) {
                    String ignore = getParameter("ignore");
                    if (StringUtils.isNotBlank(ignore)) {
                        if (ignore.equals(hrUser.getUserNo())) {
                            item.put("result", true);
                            writeJsonByAction(item.toString());
                        } else {
                            Integer row = this.hrUserService.validateNo(userInfo.getOrgId(), hrUser.getUserNo());
                            if (row.intValue() == 0) {
                                item.put("result", true);
                            }
                        }
                    } else {
                        Integer row = this.hrUserService.validateNo(userInfo.getOrgId(), hrUser.getUserNo());
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

    @PageFlow(result = {@Result(name = "error", path = "/view/hr/user/inputEmail.ftl", type = Dispatcher.FreeMarker)})
    public String createEmail() throws Exception {
        String result = "error";
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && id != null&&hrUser!=null) {
            String userid = hrUser.getUserEmail();
            String domain = "dqy.com.cn";
            String passwd = hrUser.getEmailPwd();
            hrUser = this.hrUserService.getById(id);
            if(hrUser!=null){
                try {
                    XmapiImplServiceLocator locator = new XmapiImplServiceLocator();
                    XmapiImpl service = locator.getxmapi();
                    // If authorization is required
                    //((XmapiSoapBindingStub)service).setUsername("user3");

                    //((XmapiSoapBindingStub)service).setPassword("pass3");
                    // invoke business method
                    int crypttype4 = 0;
                    int gid = 33;
                    int departmentid = -1;
                    String username = EncryptUtil.getBASE64(hrUser.getUserName());
                    String offic = "";
                    String mobile = "";
                    String phone = "";
                    String fax = "";
                    String alias = "";
                    String alias2 = "";
                    int roleId = 0;
                    int changepwd = 0;
                    String account = "dqy.com.cn";
                    String sign = "Dg4#6Q2yxNa";
                    String jmSign = EncryptUtil.encrypt(userid + domain + passwd + sign, EncryptUtil.MD5);
                    int emailResult = service.regUser_New(userid, domain, passwd, crypttype4, gid, departmentid, username, offic, mobile,
                            phone, fax, alias, alias2, roleId, changepwd, account, jmSign);
                    if (emailResult == 0) {
                        result = "saveSuccess";
                        hrUser.setUserEmail(userid);
                        hrUser.setEmailPwd(passwd);
                        bind(hrUser);
                        this.hrUserService.save(hrUser);
                    }
                    emailErrorCode = emailResult;
                } catch (javax.xml.rpc.ServiceException ex) {
                    ex.printStackTrace();
                } catch (java.rmi.RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return result;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/hr/user/inputEmail.ftl", type = Dispatcher.FreeMarker)})
    public String inputEmail() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && id != null) {
            hrUser = this.hrUserService.getById(id);
        }
        return "success";
    }

    @PageFlow(result = {@Result(name = "success", path = "/hr/user!view.dhtml?id=${hrUser.id}", type = Dispatcher.Redirect)})
    public String deleteEmail() throws Exception {
        String result = "success";
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && id != null) {
            hrUser = this.hrUserService.getById(id);
            if(hrUser!=null){
                String userid = hrUser.getUserEmail();
                String domain = "dqy.com.cn";
                try {
                    XmapiImplServiceLocator locator = new XmapiImplServiceLocator();
                    XmapiImpl service = locator.getxmapi();
                    // If authorization is required
                    //((XmapiSoapBindingStub)service).setUsername("user3");

                    //((XmapiSoapBindingStub)service).setPassword("pass3");
                    // invoke business method
                    String account = "dqy.com.cn";
                    String sign = "Dg4#6Q2yxNa";
                    String jmSign = EncryptUtil.encrypt(userid + domain  + sign, EncryptUtil.MD5);
                    int emailResult = service.delUser_New(userid, domain, account, jmSign);
                    System.out.println(emailResult);
                    if (emailResult == 0) {
                        result = "success";
                        hrUser.setUserEmail(null);
                        hrUser.setEmailPwd(null);
                        bind(hrUser);
                        this.hrUserService.save(hrUser);
                    }
                    emailErrorCode = emailResult;
                } catch (javax.xml.rpc.ServiceException ex) {
                    ex.printStackTrace();
                } catch (java.rmi.RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return result;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/hr/user/emailPwd.ftl", type = Dispatcher.FreeMarker)})
    public String changeEmailPwd() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && id != null) {
            hrUser = this.hrUserService.getById(id);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String saveEmailPwd() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && id != null&&hrUser!=null) {
            String domain = "dqy.com.cn";
            String passwd = hrUser.getEmailPwd();
            HrUser old = this.hrUserService.getById(id);
            String userid = old.getUserEmail();
            try {
                XmapiImplServiceLocator locator = new XmapiImplServiceLocator();
                XmapiImpl service = locator.getxmapi();
                // If authorization is required
                //((XmapiSoapBindingStub)service).setUsername("user3");

                //((XmapiSoapBindingStub)service).setPassword("pass3");
                // invoke business method
                int crypttype4 = 0;
                String account = "dqy.com.cn";
                String sign = "Dg4#6Q2yxNa";
                String jmSign = EncryptUtil.encrypt(userid + domain + passwd + sign, EncryptUtil.MD5);
                int emailResult = service.modPasswd_New(userid, domain, passwd, crypttype4,account, jmSign);
                if (emailResult == 0) {
                    old.setEmailPwd(hrUser.getEmailPwd());
                    old.setUseYn("Y");
                    bind(old);
                    hrUserService.save(old);
                }
                emailErrorCode = emailResult;
            } catch (javax.xml.rpc.ServiceException ex) {
                ex.printStackTrace();
            } catch (java.rmi.RemoteException ex) {
                ex.printStackTrace();
            }
        }
        return "saveSuccess";  //To change body of implemented methods use File | Settings | File Templates.
    }

}
