package com.dqy.hr.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.hr.entity.HrDepartment;
import com.dqy.hr.service.HrDepartmentService;
import com.dqy.sys.entity.SysOrg;
import com.dqy.sys.entity.SysOrgGroup;
import com.dqy.sys.service.SysOrgGroupService;
import com.dqy.sys.service.SysOrgService;
import com.dqy.web.support.ActionSupport;
import com.google.inject.Inject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.persistence.entity.search.SelectorUtils;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;
import org.guiceside.web.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjiaWang
 * Date: 12-7-12
 * Time: 下午9:49
 * To change this template use File | Settings | File Templates.
 */
@Action(name = "department", namespace = "/hr")
public class HrDepartmentAction extends ActionSupport<HrDepartment> {

    @Inject
    private SysOrgService sysOrgService;

    @Inject
    private HrDepartmentService hrDepartmentService;

    @ReqGet
    @ModelDriver
    @ReqSet
    private HrDepartment hrDepartment;

    @ReqSet
    private HrDepartment parentDepartment;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private Long orgId;


    @ReqGet
    private Long parentId;

    @ReqSet
    private List<SysOrg> orgList;

    @ReqSet
    private Integer childCount = 0;

    @ReqSet
    private String deptNo;

    @ReqGet
    @ReqSet
    private Long reloadTree;

    @ReqSet
    private Integer maxDisplayOrder;

    @Override
    @PageFlow(result = {@Result(name = "success", path = "/view/hr/department/index.ftl", type = Dispatcher.FreeMarker)})
    public String execute() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            userInfo.setTopMenu("sys");
            userInfo.setLeftMenu("department");
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String treeData() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        JSONArray jsonArray = new JSONArray();
        JSONObject node = null;
        List<HrDepartment> deptList = null;
        if (userInfo != null) {
            orgId = userInfo.getOrgId();
            if (parentId == null) {
                deptList = this.hrDepartmentService.getDeptListByLevel(userInfo.getOrgId(), 1, true);
            } else {
                deptList = this.hrDepartmentService.getDeptListByParentId(userInfo.getOrgId(), parentId, true);
            }
            if (deptList != null && !deptList.isEmpty()) {
                for (HrDepartment hrDepartment : deptList) {
                    node = new JSONObject();
                    node.put("name", StringUtils.defaultIfEmpty(hrDepartment.getDeptName()));
                    node.put("id", StringUtils.defaultIfEmpty(hrDepartment.getId()));
                    Integer count = this.hrDepartmentService.getCountByParentId(userInfo.getOrgId(), hrDepartment.getId(), true);
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

    @PageFlow(result = {@Result(name = "success", path = "/view/hr/department/input.ftl", type = Dispatcher.FreeMarker)})
    public String input() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (id != null) {
                hrDepartment = this.hrDepartmentService.getById(id);
                parentDepartment=this.hrDepartment.getParentId();
                maxDisplayOrder=hrDepartment.getDisplayOrder();
            }else{
                if (parentId != null) {
                    parentDepartment = this.hrDepartmentService.getById(parentId);
                    maxDisplayOrder=this.hrDepartmentService.getMaxOrderByParentId(userInfo.getOrgId(),parentDepartment.getId(),true);
                }else{
                    maxDisplayOrder=this.hrDepartmentService.getMaxOrderByOrgId(userInfo.getOrgId(),true);
                }
                if(maxDisplayOrder==null){
                    maxDisplayOrder=0;
                }
                maxDisplayOrder+=1;
                String no="";
                if(maxDisplayOrder<10){
                    no="00"+maxDisplayOrder;
                }else if(maxDisplayOrder<100){
                    no="0"+maxDisplayOrder;
                }else if(maxDisplayOrder<1000){
                    no=""+maxDisplayOrder;
                }
                if(parentDepartment==null){
                    deptNo=userInfo.getOrgNo()+"-"+no;
                }else{
                    deptNo=parentDepartment.getDeptNo()+"-"+no;
                }
            }

        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }


    @PageFlow(result = {@Result(name = "success", path = "/view/hr/department/view.ftl", type = Dispatcher.FreeMarker)})
    public String view() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (id != null) {
                this.hrDepartment = this.hrDepartmentService.getById(id);
                if (hrDepartment != null) {
                    childCount = this.hrDepartmentService.getCountByParentId(userInfo.getOrgId(), hrDepartment.getId(), true);
                    parentDepartment = hrDepartment.getParentId();
                }
            }
        }
        return "success";
    }

    /**
     * 保存数据对象
     *
     * @return
     * @throws Exception
     */
    @Token
    @PageFlow(result = {@Result(name = "success", path = "/hr/department!view.dhtml?id=${hrDepartment.id}&reloadTree=${reloadTree}", type = Dispatcher.Redirect)})
    public String save() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (hrDepartment != null) {
                if (hrDepartment != null) {
                    reloadTree=1l;
                    if (hrDepartment.getId() != null) {
                        HrDepartment old = this.hrDepartmentService.getById(hrDepartment.getId());
                        hrDepartment = this.copy(hrDepartment, old);
                        reloadTree=2l;
                    }
                    if (hrDepartment.getParentId() != null) {
                        if (hrDepartment.getParentId().getId() != null) {
                            HrDepartment parentId = this.hrDepartmentService.getById(hrDepartment.getParentId().getId());
                            if (parentId != null) {
                                hrDepartment.setDeptLevel(Integer.valueOf(parentId.getDeptLevel().intValue() + 1));
                            }
                        } else {
                            hrDepartment.setParentId(null);
                            hrDepartment.setDeptLevel(Integer.valueOf(1));
                        }
                    } else {
                        hrDepartment.setParentId(null);
                        hrDepartment.setDeptLevel(Integer.valueOf(1));
                    }
                    SysOrg org = this.sysOrgService.getById(userInfo.getOrgId());
                    if(org!=null){
                        hrDepartment.setOrgId(org);
                    }
                }
                this.bind(hrDepartment);
                this.hrDepartmentService.save(hrDepartment);
            }
        }
        return "success";
    }

    /**
     * 删除数据对象
     *
     * @return
     * @throws Exception
     */
    @PageFlow(result = {@Result(name = "success", path = "/hr/department!input.dhtml?reloadTree=${reloadTree}", type = Dispatcher.Redirect)})
    public String delete() throws Exception {
        if (id != null) {
            reloadTree=2l;
            this.hrDepartmentService.deleteById(id);
        }
        return "success";
    }

    public String validateName() throws Exception {
        JSONObject item = new JSONObject();
        item.put("result", false);
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (hrDepartment != null) {
                if (StringUtils.isNotBlank(hrDepartment.getDeptName())) {
                    String ignore = getParameter("ignore");
                    if (StringUtils.isNotBlank(ignore)) {
                        if (ignore.equals(hrDepartment.getDeptName())) {
                            item.put("result", true);
                            writeJsonByAction(item.toString());
                        } else {
                            Integer row = this.hrDepartmentService.validateName(userInfo.getOrgId(), hrDepartment.getDeptName());
                            if (row.intValue() == 0) {
                                item.put("result", true);
                            }
                        }
                    } else {
                        Integer row = this.hrDepartmentService.validateName(userInfo.getOrgId(), hrDepartment.getDeptName());
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
