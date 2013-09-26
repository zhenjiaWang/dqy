package com.dqy.web.support;


import com.dqy.common.SeachEntity;
import com.dqy.common.SeachRule;
import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import ognl.NoSuchPropertyException;
import ognl.OgnlException;
import org.guiceside.commons.Page;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.commons.lang.DateFormatUtil;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.persistence.entity.Tracker;
import org.guiceside.persistence.entity.search.SelectorUtils;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;
import org.guiceside.web.action.BaseAction;
import org.guiceside.web.annotation.ReqGet;
import org.guiceside.web.annotation.ReqSet;

import java.util.*;


public abstract class ActionSupport<T> extends BaseAction {
    @ReqSet
    protected Page<T> pageObj;

    @ReqGet
    protected boolean _search;

    @ReqGet
    protected String attToken;

    protected SeachEntity seachEntity;

    protected Map<String, Class> searchTypeMapping;


    @ReqGet
    @ReqSet
    protected int rows = 10;

    @ReqGet
    @ReqSet
    protected int start = 0;

    @ReqGet
    @ReqSet
    protected int page = 0;

    @ReqGet
    protected boolean ignoreUse;

    @ReqGet
    protected String filters;

    @ReqGet
    protected String sort;

    @ReqGet
    protected String sidx;

    @ReqGet
    @ReqSet
    protected String sord;

    protected Set<String> aliasSet = new HashSet<String>();

    protected Set<String> opSet = new HashSet<String>();
    @ReqSet
    protected String script = "parent.reload();";

    @ReqSet
    protected String alertMessage;

    @ReqSet
    protected String alertTitle;

    protected String mdURI = "https://api.dqy.com/auth2/authorize";

    protected boolean isAsc() {
        if (StringUtils.isNotBlank(sord)) {
            sord = sord.toUpperCase();
            if (sord.equals("ASC")) {
                return true;
            } else if (sord.equals("DESC")) {
                return false;
            }
        }
        return false;
    }


    protected void buildAlias(List<Selector> selectorList, SeachRule seachRule) {
        if (StringUtils.isNotBlank(seachRule.getField())) {
            if (seachRule.getField().indexOf(".") != -1) {
                //deptId.deptNameZh
                //deptId.cpnyId.cpnyNameZh
                String[] sidxs = seachRule.getField().split("\\.");
                if (sidxs.length == 2) {
                    if (!aliasSet.contains(sidxs[0])) {
                        selectorList.add(SelectorUtils.$alias(sidxs[0], sidxs[0]));
                        aliasSet.add(sidxs[0]);
                    }
                } else if (sidxs.length == 3) {
                    if (!aliasSet.contains(sidxs[0])) {
                        selectorList.add(SelectorUtils.$alias(sidxs[0], sidxs[0]));
                        aliasSet.add(sidxs[0]);
                    }
                    if (!aliasSet.contains(sidxs[1])) {
                        selectorList.add(SelectorUtils.$alias(sidxs[0] + "." + sidxs[1], sidxs[1]));
                        aliasSet.add(sidxs[1]);
                    }
                    seachRule.setField(sidxs[1] + "." + sidxs[2]);
                }
            }
        }
    }

    protected void buildOrder(List<Selector> selectorList) {
        if (StringUtils.isBlank(sidx)) {
            return;
        }
        SeachRule seachRules = new SeachRule();
        seachRules.setField(sidx);
        buildAlias(selectorList, seachRules);
        if (StringUtils.isNotBlank(sord)) {
            selectorList.add(SelectorUtils.$order(getOrderKey(), isAsc()));
        }

    }


    protected int getStart() {
        if (page > 0) {
            page = page - 1;
            start = page * rows;
        }
        return start;
    }

    protected String getOrderKey() {
        if (StringUtils.isNotBlank(sidx)) {
            if (sidx.indexOf(".") != -1) {
                String[] sidxs = sidx.split("\\.");
                if (sidxs.length == 2) {
                    return sidx;
                } else if (sidxs.length == 3) {
                    return sidxs[1] + "." + sidxs[2];
                }
            } else {
                return sidx;
            }
        }
        return null;
    }

    protected T copy(T fromEntity, T entity) throws Exception {
        BeanUtils.copyProperties(entity, fromEntity);
        fromEntity = null;
        return entity;
    }

    public static Object staticCopy(Object fromEntity, Object entity) throws Exception {
        BeanUtils.copyProperties(entity, fromEntity);
        fromEntity = null;
        return entity;
    }

    public static void staticBind(Object entity) throws Exception {
        if (entity instanceof Tracker) {
            BeanUtils.setValue(entity, "created", DateFormatUtil.getCurrentDate(true));
            BeanUtils.setValue(entity, "updated", DateFormatUtil.getCurrentDate(true));
        }
        try {
            String useYn = BeanUtils.getValue(entity, "useYn", String.class);
            if (StringUtils.isBlank(useYn)) {
                BeanUtils.setValue(entity, "useYn", "N");
            }
        } catch (NoSuchPropertyException e) {
            BeanUtils.setValue(entity, "useYn", "N");
        }
    }

    protected void bind(Object entity) throws Exception {
        if (entity instanceof Tracker) {
            if (BeanUtils.getValue(entity, "id") == null) {
                BeanUtils.setValue(entity, "created", DateFormatUtil.getCurrentDate(true));
            }
            UserInfo userInfo = UserSession.getUserInfo(this.getHttpServletRequest());
            BeanUtils.setValue(entity, "updated", DateFormatUtil.getCurrentDate(true));
            if (userInfo != null) {
                BeanUtils.setValue(entity, "createdBy", userInfo.getUserId());
                BeanUtils.setValue(entity, "updatedBy", userInfo.getUserId());
            }
        }
        try {
            String useYn = BeanUtils.getValue(entity, "useYn", String.class);
            if (StringUtils.isBlank(useYn)) {
                BeanUtils.setValue(entity, "useYn", "N");
            }
        } catch (NoSuchPropertyException e) {
            BeanUtils.setValue(entity, "useYn", "N");
        }
    }

    protected String getLanguagePrefernce() {
        return UserSession.getLanguagePrefernce(this.getHttpServletRequest());
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

    protected String getDate(Object entity, String property, String f) {
        Object result = null;
        if (entity != null) {
            try {
                result = BeanUtils.getValue(entity, property);
            } catch (OgnlException e) {
                result = null;
            }
        }
        return StringUtils.defaultIfEmptyByDate((Date) result, f);
    }

    @SuppressWarnings({"unchecked", "hiding"})
    protected <T> T get(Object entity, String property, Class<T> type) {
        Object result = null;
        if (entity != null) {
            try {
                result = BeanUtils.getValue(entity, property);
            } catch (OgnlException e) {
                result = null;
            }
        }
        result = StringUtils.defaultIfEmpty(result);
        result = BeanUtils.convertValue(result, type);
        return (T) result;
    }


    public String getDate(Date blogDate) throws Exception {
        Date cud = DateFormatUtil.getCurrentDate(true);
        long diff = DateFormatUtil.calendarSecondPlus(blogDate, cud);
        String unit = "秒";
        if (diff > 60) {
            diff = DateFormatUtil.calendarMinutePlus(blogDate, cud);
            unit = "分";
            if (diff > 60) {
                diff = DateFormatUtil.calendarHourPlus(blogDate, cud);
                unit = "小时";
                if (diff > 24) {
                    diff = DateFormatUtil.calendarSecondPlus(blogDate, cud);
                    if (diff == 1) {
                        return "昨天" + DateFormatUtil.format(blogDate, DateFormatUtil.HOUR_MINUTE_SECOND_PATTERN);
                    } else if (diff == 2) {
                        return "前天" + DateFormatUtil.format(blogDate, DateFormatUtil.MDHM_PATTERN);
                    } else {
                        return DateFormatUtil.format(blogDate, DateFormatUtil.MDHM_PATTERN);
                    }

                }
            }
        } else {
            return "刚刚";
        }
        return diff + "" + unit + "以前";
    }
}