package com.dqy.common.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.hr.entity.HrDepartment;
import com.dqy.hr.entity.HrUser;
import com.dqy.hr.service.HrDepartmentService;
import com.dqy.hr.service.HrUserService;
import com.dqy.wf.entity.WfVariableGlobal;
import com.dqy.wf.service.WfVariableGlobalService;
import com.google.inject.Inject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.persistence.entity.search.SelectorUtils;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;
import org.guiceside.web.action.BaseAction;
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
@Action(name = "common", namespace = "/common")
public class CommonAction extends BaseAction {

    @Inject
    private HrUserService hrUserService;

    @Inject
    private HrDepartmentService hrDepartmentService;

    @Inject
    private WfVariableGlobalService wfVariableGlobalService;

    @ReqGet
    private Long parentId;

    @ReqGet
    private Integer approveTypeId;

    @ReqGet
    private Integer nodeType;

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
        List<HrDepartment> deptList = null;
        if (userInfo != null) {
            orgId = userInfo.getOrgId();
            if (parentId == null) {
                deptList = this.hrDepartmentService.getDeptListByLevel(userInfo.getOrgId(), 1, false);
            } else {
                deptList = this.hrDepartmentService.getDeptListByParentId(userInfo.getOrgId(), parentId, false);
            }
            if (deptList != null && !deptList.isEmpty()) {
                for (HrDepartment hrDepartment : deptList) {
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

    public String userTreeData() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        JSONArray jsonArray = new JSONArray();
        JSONObject node = null;
        List<HrDepartment> deptList = null;
        List<HrUser> userList=null;
        if (userInfo != null) {
            orgId = userInfo.getOrgId();
            if (parentId == null) {
                deptList = this.hrDepartmentService.getDeptListByLevel(userInfo.getOrgId(), 1, false);
            } else {
                deptList = this.hrDepartmentService.getDeptListByParentId(userInfo.getOrgId(), parentId, false);
                userList=this.hrUserService.getUserListByDeptId(userInfo.getOrgId(),userInfo.getGroupId(),parentId);
            }
            if (deptList != null && !deptList.isEmpty()) {
                for (HrDepartment hrDepartment : deptList) {
                    node = new JSONObject();
                    node.put("name", StringUtils.defaultIfEmpty(hrDepartment.getDeptName()));
                    node.put("id", StringUtils.defaultIfEmpty(hrDepartment.getId()));

                    Integer count = this.hrDepartmentService.getCountByParentId(userInfo.getOrgId(), hrDepartment.getId(), false);
                    Integer userCount=this.hrUserService.getCountUserByDeptId(userInfo.getOrgId(),userInfo.getGroupId(),hrDepartment.getId());
                    if (count == null) {
                        count = 0;
                    }
                    if (userCount == null) {
                        userCount = 0;
                    }
                    if (count.intValue() > 0||userCount.intValue()>0) {
                        node.put("isParent", true);
                    } else {
                        node.put("isParent", false);
                    }
                    jsonArray.add(node);
                }
            }
            if (userList != null && !userList.isEmpty()) {
                for (HrUser hrUser : userList) {
                    node = new JSONObject();
                    node.put("name", StringUtils.defaultIfEmpty(hrUser.getUserName()));
                    node.put("id", StringUtils.defaultIfEmpty(hrUser.getId()));
                    node.put("approveType", approveTypeId);
                    node.put("nodeType", 1);
                    jsonArray.add(node);
                }
            }
        }
        writeJsonByAction(jsonArray.toString());
        return null; //To change body of implemented methods use File | Settings | File Templates.
    }


    public String approveTreeData() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        JSONArray jsonArray = new JSONArray();
        JSONObject node = null;
        List<HrDepartment> deptList = null;
        List<HrUser> userList=null;
        if (userInfo != null) {
            orgId = userInfo.getOrgId();
            if(approveTypeId==null){
                node = new JSONObject();
                node.put("name", "审批岗位");
                node.put("id", "1");
                node.put("approveType", "1");
                node.put("nodeType", -1);
                node.put("isParent", true);
                jsonArray.add(node);

                node = new JSONObject();
                node.put("name", "直属上司");
                node.put("id", "2");
                node.put("approveType", "2");
                node.put("nodeType", -1);
                node.put("isParent", true);
                jsonArray.add(node);

                node = new JSONObject();
                node.put("name", "审批人员");
                node.put("id", "3");
                node.put("approveType", "3");
                node.put("nodeType", -1);
                node.put("isParent", true);
                jsonArray.add(node);
            }else{
                if(approveTypeId.intValue()==1){
                    List<Selector> selectorList=new ArrayList<Selector>();
                    selectorList.add(SelectorUtils.$eq("orgId.id",userInfo.getOrgId()));
                    selectorList.add(SelectorUtils.$eq("useYn","Y"));
                    selectorList.add(SelectorUtils.$order("variableName"));
                    List<WfVariableGlobal> variableGlobalList= wfVariableGlobalService.getList(selectorList);
                    if(variableGlobalList!=null&&!variableGlobalList.isEmpty()){
                        for(WfVariableGlobal variableGlobal:variableGlobalList){
                            node = new JSONObject();
                            node.put("name", variableGlobal.getVariableName());
                            node.put("id", variableGlobal.getId());
                            node.put("approveType", approveTypeId);
                            node.put("nodeType", 1);
                            jsonArray.add(node);
                        }
                    }
                }else if(approveTypeId.intValue()==2){
                    node = new JSONObject();
                    node.put("name", "暂无");
                    node.put("id", "1");
                    node.put("approveType", approveTypeId);
                    jsonArray.add(node);
                }else if(approveTypeId.intValue()==3){
                    if (nodeType.intValue()==-1) {
                        deptList = this.hrDepartmentService.getDeptListByLevel(userInfo.getOrgId(), 1, false);
                    } else if(parentId!=null) {
                        if(nodeType!=null){
                            deptList = this.hrDepartmentService.getDeptListByParentId(userInfo.getOrgId(), parentId, false);
                            userList=this.hrUserService.getUserListByDeptId(userInfo.getOrgId(),userInfo.getGroupId(),parentId);
                        }
                    }
                    if (deptList != null && !deptList.isEmpty()) {
                        for (HrDepartment hrDepartment : deptList) {
                            node = new JSONObject();
                            node.put("name", StringUtils.defaultIfEmpty(hrDepartment.getDeptName()));
                            node.put("id", StringUtils.defaultIfEmpty(hrDepartment.getId()));
                            node.put("approveType", approveTypeId);
                            node.put("nodeType", 0);
                            Integer count = this.hrDepartmentService.getCountByParentId(userInfo.getOrgId(), hrDepartment.getId(), false);
                            Integer userCount=this.hrUserService.getCountUserByDeptId(userInfo.getOrgId(),userInfo.getGroupId(),hrDepartment.getId());
                            if (count == null) {
                                count = 0;
                            }
                            if (userCount == null) {
                                userCount = 0;
                            }
                            if (count.intValue() > 0||userCount.intValue()>0) {
                                node.put("isParent", true);
                            } else {
                                node.put("isParent", false);
                            }
                            jsonArray.add(node);
                        }
                    }
                    if (userList != null && !userList.isEmpty()) {
                        for (HrUser hrUser : userList) {
                            node = new JSONObject();
                            node.put("name", StringUtils.defaultIfEmpty(hrUser.getUserName()));
                            node.put("id", StringUtils.defaultIfEmpty(hrUser.getId()));
                            node.put("approveType", approveTypeId);
                            node.put("nodeType", 1);
                            jsonArray.add(node);
                        }
                    }
                }
            }
        }
        writeJsonByAction(jsonArray.toString());
        return null; //To change body of implemented methods use File | Settings | File Templates.
    }
}
