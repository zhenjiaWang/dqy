package com.dqy.wf.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.sys.entity.SysOrg;
import com.dqy.sys.service.SysOrgService;
import com.dqy.web.support.ActionSupport;
import com.dqy.wf.entity.WfReqNo;
import com.dqy.wf.entity.WfVariableGlobal;
import com.dqy.wf.service.WfReqNoService;
import com.dqy.wf.service.WfVariableGlobalService;
import com.google.inject.Inject;
import net.sf.json.JSONObject;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.persistence.entity.search.SelectorUtils;
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
@Action(name = "reqNo", namespace = "/wf")
public class WfReqNoAction extends ActionSupport<WfReqNo> {

    @Inject
    private WfReqNoService wfReqNoService;

    @Inject
    private SysOrgService sysOrgService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private WfReqNo wfReqNo;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private String  applyId;


    @ReqSet
    private String applyName;
    @ReqSet
    private List<String> applyList;

    @ReqSet
    private Map<String,String> applyReqNoMap;


    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/wf/reqNo/index.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("sys");
            userInfo.setLeftMenu("reqNo");
            applyList=new ArrayList<String>();
            applyList.add("ADVANCE_ACCOUNT");
            applyList.add("REPAYMENT");
            applyList.add("DAILY");
            applyList.add("BUSINESS");
            applyReqNoMap=new HashMap<String, String>();
            for(String apply:applyList){
                wfReqNo = this.wfReqNoService.getCurrentReqNo(userInfo.getOrgId(),apply);
                if(wfReqNo!=null){
                    applyReqNoMap.put(apply+"_",wfReqNo.getReqNo());
                }else{
                    applyReqNoMap.put(apply+"_","暂无");
                }
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }
    @PageFlow(result = {@Result(name = "success", path = "/view/wf/reqNo/input.ftl", type = Dispatcher.FreeMarker)})
    public String input() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if ( userInfo != null&&StringUtils.isNotBlank(applyId)) {
            wfReqNo = this.wfReqNoService.getCurrentReqNo(userInfo.getOrgId(),applyId);
            if(applyId.equals("ADVANCE_ACCOUNT")){
                applyName="预支申请";
            }else if(applyId.equals("REPAYMENT")){
                applyName="预支还款";
            }else if(applyId.equals("DAILY")){
                applyName="费用报销";
            }else if(applyId.equals("BUSINESS")){
                applyName="事务申请";
            }
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }
    @Token
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && wfReqNo != null&&StringUtils.isNotBlank(applyId)) {
            if (wfReqNo.getId() != null) {
                WfReqNo old = this.wfReqNoService.getById(wfReqNo.getId());
                wfReqNo = this.copy(wfReqNo, old);
            }
            SysOrg sysOrg=sysOrgService.getById(userInfo.getOrgId());
            if(sysOrg!=null){
                wfReqNo.setOrgId(sysOrg);
            }
            wfReqNo.setApplyId(applyId);
            wfReqNo.setUseYn("Y");
            this.bind(wfReqNo);
            this.wfReqNoService.save(wfReqNo);
        }
        return "saveSuccess";
    }


    public String validateReqNo() throws Exception {
        JSONObject item = new JSONObject();
        item.put("result", false);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (wfReqNo != null) {
                if (StringUtils.isNotBlank(wfReqNo.getReqNo())) {
                    String ignore = getParameter("ignore");
                    if (StringUtils.isNotBlank(ignore)) {
                        if (ignore.equals(wfReqNo.getReqNo())) {
                            item.put("result", true);
                            writeJsonByAction(item.toString());
                        } else {
                            Integer row = this.wfReqNoService.validateReqNo(userInfo.getOrgId(), wfReqNo.getReqNo());
                            if (row.intValue() == 0) {
                                item.put("result", true);
                            }
                        }
                    } else {
                        Integer row = this.wfReqNoService.validateReqNo(userInfo.getOrgId(), wfReqNo.getReqNo());
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
}
