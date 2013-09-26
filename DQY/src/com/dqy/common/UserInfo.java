package com.dqy.common;


import java.io.Serializable;
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

    private String userId;
    
    private String userName;
    
    private String department;
    
    private String job;
    
    private String companyName;
    
    private String companyNameEn;

    private String companyId;
    

    private String logo;



    private boolean admin;

    private String mobileTab;



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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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


    public String getCompanyNameEn() {
        return companyNameEn;
    }

    public void setCompanyNameEn(String companyNameEn) {
        this.companyNameEn = companyNameEn;
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
}
