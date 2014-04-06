package com.dqy.wf.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.hr.entity.HrDepartment;
import com.dqy.hr.entity.HrUser;
import com.dqy.hr.service.HrDepartmentService;
import com.dqy.hr.service.HrUserService;
import com.dqy.sys.entity.SysBudgetTitle;
import com.dqy.sys.entity.SysBudgetType;
import com.dqy.sys.entity.SysOrg;
import com.dqy.sys.service.SysBudgetAmountService;
import com.dqy.sys.service.SysBudgetTitleService;
import com.dqy.sys.service.SysBudgetTypeService;
import com.dqy.sys.service.SysOrgService;
import com.dqy.wf.entity.*;
import com.dqy.wf.service.*;
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
@Action(name = "reqExecute", namespace = "/wf")
public class WfReqExecuteAction extends WfReqSupportAction<WfReqExecute> {

    @Inject
    private SysOrgService sysOrgService;

    @Inject
    private HrUserService hrUserService;

    @Inject
    private WfReqService wfReqService;

    @Inject
    private WfReqExecuteService wfReqExecuteService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private WfReqExecute wfReqExecute;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private Long reqId;

    @ReqGet
    @ReqSet
    private Long executeId;

    @ReqGet
    @ReqSet
    private String reason;

    @ReqGet
    @ReqSet
    private String orderKey;

    @ReqGet
    @ReqSet
    private String searchKey;

    @ReqSet
    private String applyId;



    @ReqGet
    @ReqSet
    private Date searchDate;

    @ReqGet
    @ReqSet
    private String keyword;

    @ReqSet
    private List<WfReqExecute> reqExecuteList;


    private List<Selector> searchModeCallbackDoneList() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("apply");
            userInfo.setLeftMenu("myExecute");
            userInfo.setChildMenu("done");
            Long orgId = userInfo.getOrgId();
            Long userId = userInfo.getUserId();
            if (orgId != null && userId != null) {
                selectorList.add(SelectorUtils.$eq("orgId.id", orgId));
                selectorList.add(SelectorUtils.$eq("userId.id", userId));
                selectorList.add(SelectorUtils.$eq("executeRead", 1));
                boolean aliasReq = false;
                if(StringUtils.isNotBlank(keyword)){
                    selectorList.add(SelectorUtils.$alias("reqId", "reqId"));
                    selectorList.add(SelectorUtils.$or(SelectorUtils.$like("reqId.reqNo",keyword),SelectorUtils.$like("reqId.subject",keyword)));
                }
                if (searchDate != null) {
                    String sDate = DateFormatUtil.format(searchDate, DateFormatUtil.YEAR_MONTH_DAY_PATTERN);
                    Date startDate = DateFormatUtil.parse(sDate + " 00:00:01", DateFormatUtil.YMDHMS_PATTERN);
                    Date endDate = DateFormatUtil.parse(sDate + " 23:59:59", DateFormatUtil.YMDHMS_PATTERN);
                    selectorList.add(SelectorUtils.$ge("receiveDate", startDate));
                    selectorList.add(SelectorUtils.$le("receiveDate", endDate));
                }
                selectorList.add(SelectorUtils.$order("receiveDate", false));
            } else {
                selectorList.add(SelectorUtils.$eq("id", 0l));
            }

        }
        return selectorList;
    }


    private List<Selector> searchModeCallbackExecuteList() throws Exception {
        List<Selector> selectorList = new ArrayList<Selector>();
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("apply");
            userInfo.setLeftMenu("myExecute");
            userInfo.setChildMenu("execute");
            Long orgId = userInfo.getOrgId();
            Long userId = userInfo.getUserId();
            if (orgId != null && userId != null) {
                selectorList.add(SelectorUtils.$eq("orgId.id", orgId));
                selectorList.add(SelectorUtils.$eq("userId.id", userId));
                selectorList.add(SelectorUtils.$eq("executeRead", 0));
                boolean aliasReq = false;
                if(StringUtils.isNotBlank(keyword)){
                    selectorList.add(SelectorUtils.$alias("reqId", "reqId"));
                    selectorList.add(SelectorUtils.$or(SelectorUtils.$like("reqId.reqNo",keyword),SelectorUtils.$like("reqId.subject",keyword)));
                }
                if (searchDate != null) {
                    String sDate = DateFormatUtil.format(searchDate, DateFormatUtil.YEAR_MONTH_DAY_PATTERN);
                    Date startDate = DateFormatUtil.parse(sDate + " 00:00:01", DateFormatUtil.YMDHMS_PATTERN);
                    Date endDate = DateFormatUtil.parse(sDate + " 23:59:59", DateFormatUtil.YMDHMS_PATTERN);
                    selectorList.add(SelectorUtils.$ge("receiveDate", startDate));
                    selectorList.add(SelectorUtils.$le("receiveDate", endDate));
                }
                selectorList.add(SelectorUtils.$order("receiveDate", false));
            } else {
                selectorList.add(SelectorUtils.$eq("id", 0l));
            }

        }
        return selectorList;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/reqExecute/doneList.ftl", type = Dispatcher.FreeMarker)})
    public String doneList() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            pageObj = this.wfReqExecuteService.getPageList(getStart(), 10, searchModeCallbackDoneList());
            if (pageObj != null) {
                reqExecuteList = pageObj.getResultList();
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/wf/reqExecute/executeList.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            pageObj = this.wfReqExecuteService.getPageList(getStart(), 10, searchModeCallbackExecuteList());
            if (pageObj != null) {
                reqExecuteList = pageObj.getResultList();
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/reqExecute/forward.ftl", type = Dispatcher.FreeMarker)})
    public String forward() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (reqId != null) {
                wfReq=this.wfReqService.getById(reqId);
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }
    public String add() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (reqId != null&&executeId!=null) {
                wfReq=this.wfReqService.getById(reqId);
                HrUser hrUser=this.hrUserService.getById(executeId);
                SysOrg sysOrg=this.sysOrgService.getById(userInfo.getOrgId());
                if(wfReq!=null&&hrUser!=null&&sysOrg!=null){
                    Date cudDate=DateFormatUtil.getCurrentDate(true);
                    wfReqExecute=new WfReqExecute();
                    wfReqExecute.setUserId(hrUser);
                    wfReqExecute.setExecuteRead(0);
                    wfReqExecute.setExecuteState(0);
                    wfReqExecute.setReqId(wfReq);
                    wfReqExecute.setOrgId(sysOrg);
                    wfReqExecute.setReason(reason);
                    wfReqExecute.setReceiveDate(cudDate);
                    wfReqExecute.setUseYn("Y");
                    bind(wfReqExecute);
                    this.wfReqExecuteService.save(wfReqExecute);
                }
            }
        }
        script = "parent.setURL('/wf/req!view.dhtml?id="+wfReq.getId()+"');";
        return "saveSuccess";  //To change body of implemented methods use File | Settings | File Templates.
    }



    @PageFlow(result = {
            @Result(name = "view", path = "/wf/req!view.dhtml?id=${wfReqExecute.reqId.id}", type = Dispatcher.Redirect),
            @Result(name = "ADVANCE_ACCOUNT", path = "/wf/advanceAccount!view.dhtml?reqId=${wfReq.id}", type = Dispatcher.Redirect),
            @Result(name = "REPAYMENT", path = "/wf/rePayment!view.dhtml?reqId=${wfReq.id}", type = Dispatcher.Redirect),
            @Result(name = "DAILY", path = "/wf/daily!view.dhtml?reqId=${wfReq.id}", type = Dispatcher.Redirect),
            @Result(name = "BUSINESS", path = "/wf/business!view.dhtml?reqId=${wfReq.id}", type = Dispatcher.Redirect),
            @Result(name = "SALE", path = "/wf/sale!view.dhtml?reqId=${wfReq.id}", type = Dispatcher.Redirect)})
    public String process() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (id != null && userInfo != null) {
            userInfo.setTopMenu("apply");
            userInfo.setLeftMenu("myExecute");
            wfReqExecute = this.wfReqExecuteService.getById(id);
            if (wfReqExecute != null) {
                if(wfReqExecute.getExecuteRead().intValue()==0){
                    userInfo.setChildMenu("execute");
                }else  if(wfReqExecute.getExecuteRead().intValue()==1){
                    userInfo.setChildMenu("done");
                }
                HrUser user = this.hrUserService.getById(userInfo.getUserId());
                if (wfReqExecute.getExecuteRead().intValue() == 0) {
                    wfReqExecute.setExecuteRead(1);
                    bind(wfReqExecute);
                    wfReqExecuteService.save(wfReqExecute);
                    Integer unRead = wfReqExecuteService.getCountUnRead(userInfo.getOrgId(), userInfo.getUserId());
                    if (unRead == null) {
                        unRead = 0;
                    }
                    userInfo.setExecuteUnRead(unRead);

                }
                 wfReq = wfReqExecute.getReqId();
                if (wfReq != null) {
                    applyId = wfReq.getApplyId();
                }
            }
        }
        return applyId;
    }
}
