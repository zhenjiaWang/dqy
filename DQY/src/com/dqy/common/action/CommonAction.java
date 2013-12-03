package com.dqy.common.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.common.entity.TempAtt;
import com.dqy.common.service.TempAttService;
import com.dqy.hr.entity.HrDepartment;
import com.dqy.hr.entity.HrUser;
import com.dqy.hr.service.HrDepartmentService;
import com.dqy.hr.service.HrUserService;
import com.dqy.sys.entity.SysAuthorized;
import com.dqy.sys.entity.SysBudgetTitle;
import com.dqy.sys.entity.SysFinancialTitle;
import com.dqy.sys.entity.SysOrg;
import com.dqy.sys.service.SysAuthorizedService;
import com.dqy.sys.service.SysBudgetTitleService;
import com.dqy.sys.service.SysFinancialTitleService;
import com.dqy.sys.service.SysOrgService;
import com.dqy.wf.entity.WfVariableGlobal;
import com.dqy.wf.service.WfVariableGlobalService;
import com.google.inject.Inject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import ognl.NoSuchPropertyException;
import org.apache.http.HttpRequest;
import org.guiceside.commons.FileIdUtils;
import org.guiceside.commons.FileObject;
import org.guiceside.commons.Page;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.commons.lang.DateFormatUtil;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.persistence.entity.Tracker;
import org.guiceside.persistence.entity.search.SelectorUtils;
import org.guiceside.persistence.hibernate.dao.enums.Match;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;
import org.guiceside.support.file.FileManager;
import org.guiceside.support.upload.FileUploadManager;
import org.guiceside.web.action.BaseAction;
import org.guiceside.web.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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
@Action(name = "common", namespace = "/common")
public class CommonAction extends BaseAction {

    @Inject
    private TempAttService tempAttService;

    @Inject
    private HrDepartmentService hrDepartmentService;

    @Inject
    private SysBudgetTitleService sysBudgetTitleService;

    @Inject
    private WfVariableGlobalService wfVariableGlobalService;

    @Inject
    private SysAuthorizedService sysAuthorizedService;

    @Inject
    private SysFinancialTitleService sysFinancialTitleService;

    @Inject
    private SysOrgService sysOrgService;

    @Inject
    private HrUserService hrUserService;

    @ReqGet
    private Long parentId;

    @ReqGet
    private Integer approveTypeId;

    @ReqGet
    private Integer nodeType;

    @ReqGet
    @ReqSet
    private Long orgId;

    @ReqSet
    private String authUrl;

    @ReqGet
    @ReqSet
    private String attKey;


    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private String attToken;

    @ReqGet
    private String fileNames;

    @ReqGet
    private String searchKey;

    @ReqSet
    private List<SysFinancialTitle> titleList;

    @Override
    public String execute() throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/common/orgTree.ftl", type = Dispatcher.FreeMarker)})
    public String orgTree() throws Exception {
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String titleTreeData() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        JSONArray jsonArray = new JSONArray();
        JSONObject node = null;
        if (userInfo != null) {
            if (orgId == null) {
                orgId = userInfo.getOrgId();
            }
            if (parentId == null) {
                titleList = this.sysFinancialTitleService.getTitleListByLevel(orgId, 1, false);
            } else {
                titleList = this.sysFinancialTitleService.getTitleListByParentId(orgId, parentId, false);
            }
            if (titleList != null && !titleList.isEmpty()) {
                for (SysFinancialTitle financialTitle : titleList) {
                    node = new JSONObject();
                    node.put("name", StringUtils.defaultIfEmpty(financialTitle.getTitleName()));
                    node.put("id", StringUtils.defaultIfEmpty(financialTitle.getId()));
                    Integer count = this.sysFinancialTitleService.getCountByParentId(orgId, financialTitle.getId(), false);
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

    public String orgTreeData() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        JSONArray jsonArray = new JSONArray();
        JSONObject node = null;
        List<HrDepartment> deptList = null;
        if (userInfo != null) {
            if (orgId == null) {
                orgId = userInfo.getOrgId();
            }

            if (parentId == null) {
                deptList = this.hrDepartmentService.getDeptListByLevel(orgId, 1, false);
            } else {
                deptList = this.hrDepartmentService.getDeptListByParentId(orgId, parentId, false);
            }
            if (deptList != null && !deptList.isEmpty()) {
                for (HrDepartment hrDepartment : deptList) {
                    node = new JSONObject();
                    node.put("name", StringUtils.defaultIfEmpty(hrDepartment.getDeptName()));
                    node.put("id", StringUtils.defaultIfEmpty(hrDepartment.getId()));
                    Integer count = this.hrDepartmentService.getCountByParentId(orgId, hrDepartment.getId(), false);
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
        List<HrUser> userList = null;
        if (userInfo != null) {
            orgId = userInfo.getOrgId();
            if (parentId == null) {
                deptList = this.hrDepartmentService.getDeptListByLevel(userInfo.getOrgId(), 1, false);
            } else {
                deptList = this.hrDepartmentService.getDeptListByParentId(userInfo.getOrgId(), parentId, false);
                userList = this.hrUserService.getUserListByDeptId(userInfo.getOrgId(), userInfo.getGroupId(), parentId);
            }
            if (deptList != null && !deptList.isEmpty()) {
                for (HrDepartment hrDepartment : deptList) {
                    node = new JSONObject();
                    node.put("name", StringUtils.defaultIfEmpty(hrDepartment.getDeptName()));
                    node.put("id", StringUtils.defaultIfEmpty(hrDepartment.getId()));

                    Integer count = this.hrDepartmentService.getCountByParentId(userInfo.getOrgId(), hrDepartment.getId(), false);
                    Integer userCount = this.hrUserService.getCountUserByDeptId(userInfo.getOrgId(), userInfo.getGroupId(), hrDepartment.getId());
                    if (count == null) {
                        count = 0;
                    }
                    if (userCount == null) {
                        userCount = 0;
                    }
                    if (count.intValue() > 0 || userCount.intValue() > 0) {
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
        List<HrUser> userList = null;
        List<SysAuthorized> authorizedList = null;
        if (userInfo != null) {
            orgId = userInfo.getOrgId();
            if (approveTypeId == null) {
//                node = new JSONObject();
//                node.put("name", "审批岗位");
//                node.put("id", "1");
//                node.put("approveType", "1");
//                node.put("nodeType", -1);
//                node.put("isParent", true);
//                jsonArray.add(node);
//
//                node = new JSONObject();
//                node.put("name", "直属上司");
//                node.put("id", "2");
//                node.put("approveType", "2");
//                node.put("nodeType", -1);
//                node.put("isParent", true);
//                jsonArray.add(node);

                node = new JSONObject();
                node.put("name", "审批人员");
                node.put("id", "3");
                node.put("approveType", "3");
                node.put("nodeType", -1);
                node.put("isParent", true);
                jsonArray.add(node);
            } else {
                if (approveTypeId.intValue() == 1) {
                    List<Selector> selectorList = new ArrayList<Selector>();
                    selectorList.add(SelectorUtils.$eq("orgId.id", userInfo.getOrgId()));
                    selectorList.add(SelectorUtils.$eq("useYn", "Y"));
                    selectorList.add(SelectorUtils.$order("variableName"));
                    List<WfVariableGlobal> variableGlobalList = wfVariableGlobalService.getList(selectorList);
                    if (variableGlobalList != null && !variableGlobalList.isEmpty()) {
                        for (WfVariableGlobal variableGlobal : variableGlobalList) {
                            node = new JSONObject();
                            node.put("name", variableGlobal.getVariableName());
                            node.put("id", variableGlobal.getId());
                            node.put("approveType", approveTypeId);
                            node.put("nodeType", 1);
                            jsonArray.add(node);
                        }
                    }
                } else if (approveTypeId.intValue() == 2) {
                    node = new JSONObject();
                    node.put("name", "暂无");
                    node.put("id", "1");
                    node.put("approveType", approveTypeId);
                    jsonArray.add(node);
                } else if (approveTypeId.intValue() == 3) {
                    if (nodeType.intValue() == -1) {
                        deptList = this.hrDepartmentService.getDeptListByLevel(userInfo.getOrgId(), 1, false);
                    } else if (parentId != null) {
                        if (nodeType != null) {
                            deptList = this.hrDepartmentService.getDeptListByParentId(userInfo.getOrgId(), parentId, false);
                            userList = this.hrUserService.getUserListByDeptId(userInfo.getOrgId(), userInfo.getGroupId(), parentId);
                            authorizedList = this.sysAuthorizedService.getAuthOrgDept(userInfo.getGroupId(), userInfo.getOrgId(), parentId);
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
                            Integer userCount = this.hrUserService.getCountUserByDeptId(userInfo.getOrgId(), userInfo.getGroupId(), hrDepartment.getId());
                            Integer authCount = this.sysAuthorizedService.getCounttAuthOrgDept(userInfo.getGroupId(), userInfo.getOrgId(), hrDepartment.getId());
                            if (count == null) {
                                count = 0;
                            }
                            if (userCount == null) {
                                userCount = 0;
                            }
                            if (authCount == null) {
                                authCount = 0;
                            }
                            if (count.intValue() > 0 || userCount.intValue() > 0 || authCount.intValue() > 0) {
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
                    if (authorizedList != null && !authorizedList.isEmpty()) {
                        for (SysAuthorized authorized : authorizedList) {
                            node = new JSONObject();
                            node.put("name", StringUtils.defaultIfEmpty(authorized.getUserId().getUserName()) + "[外派]");
                            node.put("id", StringUtils.defaultIfEmpty(authorized.getUserId().getId()));
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

    @PageFlow(result = {@Result(name = "success", path = "${authUrl}", type = Dispatcher.Redirect),
            @Result(name = "index", path = "/common/login!index.dhtml", type = Dispatcher.Redirect)})
    public String sysIndex() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            List<String> roleList = userInfo.getRoleList();
            if (roleList != null && !roleList.isEmpty()) {
                if (roleList.contains("SYS_GROUP")) {
                    authUrl = "/sys/orgGroup.dhtml";
                } else if (roleList.contains("SYS_USER")) {
                    authUrl = "/hr/department.dhtml";
                } else if (roleList.contains("SYS_FINANCIAL") || roleList.contains("SYS_BUDGET")) {
                    if (roleList.contains("SYS_FINANCIAL")) {
                        authUrl = "/sys/financialTitle.dhtml";
                    } else if (roleList.contains("SYS_BUDGET")) {
                        authUrl = "/sys/budgetType.dhtml";
                    }
                } else if (roleList.contains("SYS_APPROVE")) {
                    authUrl = "/wf/variableGlobal.dhtml";
                }
                if (StringUtils.isNotBlank(authUrl)) {
                    return "success";
                }
            }
        }
        return "index";  //To change body of implemented methods use File | Settings | File Templates.
    }
    @PageFlow(result = {@Result(name = "success", path = "${authUrl}", type = Dispatcher.Redirect),
            @Result(name = "index", path = "/common/login!index.dhtml", type = Dispatcher.Redirect)})
    public String budgetIndex() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            List<String> roleList = userInfo.getRoleList();
            if (roleList != null && !roleList.isEmpty()) {
                if (roleList.contains("SET_BUDGET") || roleList.contains("APPROVE_BUDGET")) {
                    if (roleList.contains("SET_BUDGET")) {
                        authUrl = "/sys/budgetAmount.dhtml";
                    } else if (roleList.contains("APPROVE_BUDGET")) {
                        authUrl = "/sys/budgetAmount!approve.dhtml";
                    }
                }
                if (roleList.contains("LOOK_BUDGET")) {
                    //authUrl = "/sys/orgGroup.dhtml";
                }
                if (StringUtils.isNotBlank(authUrl)) {
                    return "success";
                }
            }
        }
        return "index";  //To change body of implemented methods use File | Settings | File Templates.
    }


    public String upload() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && StringUtils.isNotBlank(attKey) && StringUtils.isNotBlank(attToken)) {
            /*获得当前的用户*/
            /*保存上传的文件*/
            try {
                FileUploadManager manager = new FileUploadManager(getRootPath());
                List<FileObject> fileItems = manager.getFiles(this
                        .getHttpServletRequest());
                Date cud = DateFormatUtil.getCurrentDate(false);
                SysOrg sysOrg = this.sysOrgService.getById(userInfo.getOrgId());
                HrUser hrUser = this.hrUserService.getById(userInfo.getUserId());
                if (fileItems != null && !fileItems.isEmpty() && sysOrg != null && hrUser != null) {
                    for (FileObject fileObject : fileItems) {
                        TempAtt att = new TempAtt();
                        int year = DateFormatUtil.getDayInYear(cud);
                        int month = DateFormatUtil.getDayInMonth(cud) + 1;
                        int day = DateFormatUtil.getDayInDay(cud);
                        att.setYear(year);
                        att.setMonth(month);
                        att.setDay(day);
                        att.setTokenId(attToken);
                        att.setOldName(fileObject.getFileName());
                        att.setNewName(FileIdUtils.getFileUnId());
                        String postfix = fileObject.getPostfix();
                        postfix = postfix.toLowerCase();
                        att.setPostfix(postfix);
                        att.setAttSize(fileObject.getSize());
                        att.setAttKey(attKey);
                        String source = manager.getFilePath(attKey, year, month, day, att.getNewName() + "." + att.getPostfix());
                        att.setSource(source);
                        att.setUseYn("Y");
                        att.setOrgId(sysOrg);
                        att.setUserId(hrUser);
                        if (fileObject.getFileItem() != null) {
                            manager.write(attKey, fileObject.getFileItem(), att.getNewName() + "." + att.getPostfix(), cud);
                        } else {
                            FileOutputStream fs = null;
                            InputStream is = null;
                            try {
                                is = fileObject.getInputStream();
                                String path = att.getSource();
                                String uploadPath = manager.parsePathByDate(manager.parsePath(attKey), cud);
                                FileManager.newFloder(uploadPath);
                                fs = new FileOutputStream(path, true);
                                int max = 1024;
                                if (fileObject.getSize() < 1024) {
                                    max = (int) fileObject.getSize();
                                }
                                byte[] content = new byte[max];
                                int length = is.read(content, 0, max);
                                while (length != -1) {
                                    fs.write(content, 0, length);
                                    length = is.read(content, 0, max);
                                }
                                fs.close();
                                is.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (fs != null) {
                                    fs.close();
                                }
                                if (is != null) {
                                    is.close();
                                }
                            }
                        }
                        bind(att, userInfo);
                        tempAttService.save(att);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String remove() throws Exception {
        JSONObject root = new JSONObject();
        root.put("success", false);
        if (StringUtils.isNotBlank(fileNames) && StringUtils.isNotBlank(attKey) && StringUtils.isNotBlank(attToken)) {
            try {
                int last = fileNames.lastIndexOf(".");
                String oldName = fileNames.substring(0, last);
                String postfix = fileNames.substring(last + 1);
                if (StringUtils.isNotBlank(oldName) && StringUtils.isNotBlank(postfix)) {
                    List<TempAtt> tempAttList = tempAttService.getByFileName(attKey, attToken, oldName, postfix);
                    if (tempAttList != null && !tempAttList.isEmpty()) {
                        for (TempAtt tempAtt : tempAttList) {
                            if (tempAtt != null) {
                                if (StringUtils.isNotBlank(tempAtt.getSource())) {
                                    FileManager.deleteFile(tempAtt.getSource());
                                }
                                tempAttService.delete(tempAtt);
                            }
                        }
                    }
                }
                root.put("success", true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        writeJsonByAction(root.toString());
        return null;
    }


    public String download() throws Exception {
        if (id != null) {
            TempAtt tempAtt = this.tempAttService.getById(id);
            if (tempAtt != null) {
                String attchName = StringUtils.toUtf8String(tempAtt.getOldName() + "." + tempAtt.getPostfix());
                File file = FileManager.getFile(tempAtt.getSource(), false);
                download(file, attchName);
            }
        }
        return null;
    }


    public String searchBudgetTitle() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        JSONObject root=new JSONObject();
        root.put("result",-1);
        JSONArray jsonArray = new JSONArray();
        JSONObject node = null;
        if (userInfo != null&&StringUtils.isNotBlank(searchKey)) {
            if (orgId == null) {
                orgId = userInfo.getOrgId();
            }
            List<Selector> selectorList=new ArrayList<Selector>();
            selectorList.add(SelectorUtils.$eq("orgId.id", orgId));
            selectorList.add(SelectorUtils.$or(SelectorUtils.$like("titleNo",searchKey, Match.END),SelectorUtils.$like("titleName",searchKey)));
            selectorList.add(SelectorUtils.$eq("useYn","Y"));
            Page<SysBudgetTitle> titlePage=this.sysBudgetTitleService.getPageList(0,10,selectorList);
            if(titlePage!=null){
                List<SysBudgetTitle> sysBudgetTitleList=titlePage.getResultList();
                if(sysBudgetTitleList!=null&&!sysBudgetTitleList.isEmpty()){
                    for(SysBudgetTitle budgetTitle:sysBudgetTitleList){
                        node = new JSONObject();
                        node.put("titleName", StringUtils.defaultIfEmpty(budgetTitle.getTitleName()));
                        node.put("titleNo", StringUtils.defaultIfEmpty(budgetTitle.getTitleNo()));
                        node.put("id", StringUtils.defaultIfEmpty(budgetTitle.getId()));
                        jsonArray.add(node);
                    }
                }
            }
        }
        root.put("titleList",jsonArray);
        root.put("result",0);
        writeJsonByAction(root.toString());
        return null; //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void bind(Object entity, UserInfo userInfo) throws Exception {
        if (entity instanceof Tracker) {
            if (BeanUtils.getValue(entity, "id") == null) {
                BeanUtils.setValue(entity, "created", DateFormatUtil.getCurrentDate(true));
            }
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
}
