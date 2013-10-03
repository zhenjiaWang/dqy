package com.dqy.common.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.hr.entity.HrDepartment;
import com.dqy.hr.service.HrDepartmentService;
import com.dqy.hr.service.HrUserService;
import com.google.inject.Inject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.web.action.BaseAction;
import org.guiceside.web.annotation.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjiaWang
 * Date: 12-7-12
 * Time: 下午9:49
 * To change this template use File | Settings | File Templates.
 */
@Action(name = "common", namespace = "/common")
public class CommonAction extends BaseAction {

    @Inject
    private HrUserService hrUserService;

    @Inject
    private HrDepartmentService hrDepartmentService;


    @ReqGet
    private Long parentId;

    @ReqGet
    @ReqSet
    private Long orgId;


    @Override
    public String execute() throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/common/orgTree.ftl", type = Dispatcher.FreeMarker)})
    public String orgTree() throws Exception {
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }
    public String orgTreeData() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        JSONArray jsonArray = new JSONArray();
        JSONObject node = null;
        List<HrDepartment> depeList = null;
        if (userInfo != null) {
            orgId = userInfo.getOrgId();
            if (parentId == null) {
                depeList = this.hrDepartmentService.getDeptListByLevel(userInfo.getOrgId(), 1, false);
            } else {
                depeList = this.hrDepartmentService.getDeptListByParentId(userInfo.getOrgId(), parentId, false);
            }
            if (depeList != null && !depeList.isEmpty()) {
                for (HrDepartment hrDepartment : depeList) {
                    node = new JSONObject();
                    node.put("name", StringUtils.defaultIfEmpty(hrDepartment.getDeptName()));
                    node.put("id", StringUtils.defaultIfEmpty(hrDepartment.getId()));
                    Integer count = this.hrDepartmentService.getCountByParentId(userInfo.getOrgId(), hrDepartment.getId(), false);
                    if (count == null) {
                        count = 0;
                    }
                    if (count.intValue() > 0) {
                        node.put("isParent", true);
                    } else {
                        node.put("isParent", false);
                    }
                    jsonArray.add(node);
                }
            }
        }
        writeJsonByAction(jsonArray.toString());
        return null; //To change body of implemented methods use File | Settings | File Templates.
    }
}
