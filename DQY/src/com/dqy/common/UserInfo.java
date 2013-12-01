package com.dqy.common;


import com.dqy.sys.entity.SysOrg;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 2008-10-30
 * @since JDK1.5
 */
public class UserInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_LANGUAGE_PREFERENCE = "zh";

    public static final String DEFAULT_COUNTRY_PREFERENCE = "CN";

    private String languagePreference = DEFAULT_LANGUAGE_PREFERENCE;

    private String countryPreference = DEFAULT_COUNTRY_PREFERENCE;

    public static final Locale DEFAULT_Locale = new Locale(
            DEFAULT_LANGUAGE_PREFERENCE, DEFAULT_COUNTRY_PREFERENCE);

    private boolean authorize = false;

    private boolean loggedIn = false;



    private String sessionId;

    private Long userId;

    private String userNo;

    private Long groupId;

    private String groupName;

    private Long orgId;

    private String orgNo;

    private String orgName;

    private String userName;
    
    private String department;

    private Long departmentId;
    
    private String job;
    
    private String logo;

    private boolean admin;

    private String mobileTab;

    private String topMenu;

    private String leftMenu;

    private String childMenu;

    private List<SysOrg> authOrgList;

    private List<String> roleList;

    private String roleId;

    private Integer taskUnRead;

    private Integer taskUnApprove;

    private Integer reqPassed;

    private Integer reqRejected;

    private Integer topMenuApply;

    private Integer topMenuInfo;

    private Integer topMenuBudget;

    private Integer topMenuSys;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isAuthorize() {
        return authorize;
    }

    public void setAuthorize(boolean authorize) {
        this.authorize = authorize;
    }


    public String getLanguagePreference() {
        return languagePreference;
    }

    public void setLanguagePreference(String languagePreference) {
        this.languagePreference = languagePreference;
    }

    public String getCountryPreference() {
        return countryPreference;
    }

    public void setCountryPreference(String countryPreference) {
        this.countryPreference = countryPreference;
    }


    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }


    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getMobileTab() {
        return mobileTab;
    }

    public void setMobileTab(String mobileTab) {
        this.mobileTab = mobileTab;
    }

    public String getTopMenu() {
        return topMenu;
    }

    public void setTopMenu(String topMenu) {
        this.topMenu = topMenu;
    }

    public String getLeftMenu() {
        return leftMenu;
    }

    public void setLeftMenu(String leftMenu) {
        this.leftMenu = leftMenu;
    }

    public String getChildMenu() {
        return childMenu;
    }

    public void setChildMenu(String childMenu) {
        this.childMenu = childMenu;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public List<SysOrg> getAuthOrgList() {
        return authOrgList;
    }

    public void setAuthOrgList(List<SysOrg> authOrgList) {
        this.authOrgList = authOrgList;
    }


    public String getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }


    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getTaskUnRead() {
        return taskUnRead;
    }

    public void setTaskUnRead(Integer taskUnRead) {
        this.taskUnRead = taskUnRead;
    }

    public Integer getTaskUnApprove() {
        return taskUnApprove;
    }

    public void setTaskUnApprove(Integer taskUnApprove) {
        this.taskUnApprove = taskUnApprove;
    }

    public Integer getReqPassed() {
        return reqPassed;
    }

    public void setReqPassed(Integer reqPassed) {
        this.reqPassed = reqPassed;
    }

    public Integer getReqRejected() {
        return reqRejected;
    }

    public void setReqRejected(Integer reqRejected) {
        this.reqRejected = reqRejected;
    }

    public Integer getTopMenuApply() {
        return topMenuApply;
    }

    public void setTopMenuApply(Integer topMenuApply) {
        this.topMenuApply = topMenuApply;
    }

    public Integer getTopMenuInfo() {
        return topMenuInfo;
    }

    public void setTopMenuInfo(Integer topMenuInfo) {
        this.topMenuInfo = topMenuInfo;
    }

    public Integer getTopMenuBudget() {
        return topMenuBudget;
    }

    public void setTopMenuBudget(Integer topMenuBudget) {
        this.topMenuBudget = topMenuBudget;
    }

    public Integer getTopMenuSys() {
        return topMenuSys;
    }

    public void setTopMenuSys(Integer topMenuSys) {
        this.topMenuSys = topMenuSys;
    }
}
