package com.dqy.wf.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.hr.entity.HrUser;
import com.dqy.hr.service.HrUserService;
import com.dqy.sys.entity.SysOrg;
import com.dqy.sys.service.SysOrgService;
import com.dqy.web.support.ActionSupport;
import com.dqy.wf.entity.*;
import com.dqy.wf.service.*;
import com.google.inject.Inject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.web.annotation.*;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjiaWang
 * Date: 12-7-12
 * Time: 下午9:49
 * To change this template use File | Settings | File Templates.
 */
@Action(name = "reqMyFlow", namespace = "/wf")
public class WfReqMyFlowAction extends ActionSupport<WfReqMyFlow> {

    @Inject
    private WfReqMyFlowService wfReqMyFlowService;

    @Inject
    private SysOrgService sysOrgService;

    @Inject
    private WfReqMyFlowNodeService wfReqMyFlowNodeService;

    @Inject
    private WfReqMyFlowNodeApproveService wfReqMyFlowNodeApproveService;

    @Inject
    private WfVariableGlobalService wfVariableGlobalService;

    @Inject
    private WfReqMyFlowLastService wfReqMyFlowLastService;

    @Inject
    private HrUserService hrUserService;


    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private String applyId;

    @ReqGet
    @ModelDriver
    @ReqSet
    private WfReqMyFlow wfReqMyFlow;


    @ReqGet
    @ReqSet
    private Integer approveIndex;

    @ReqSet
    private List<WfReqMyFlow> reqMyFlowList;

    @ReqGet
    @ReqSet
    private Long flowId;

    @Override
    public String execute() throws Exception {

        return null;
    }


    @PageFlow(result = {@Result(name = "success", path = "/view/wf/myFlow/edit.ftl", type = Dispatcher.FreeMarker)})
    public String input() throws Exception {
       if (id != null) {
            wfReqMyFlow = this.wfReqMyFlowService.getById(id);
            if (wfReqMyFlow != null) {
                applyId = wfReqMyFlow.getApplyId();
            }
        }
        return "success";
    }

    private void bindNodeApprove(int i, List<WfReqMyFlowNode> reqMyFlowNodeList, List<WfReqMyFlowNodeApprove> reqMyFlowNodeApproveList,
                                 Integer nodeType, String approveType, Long approve, boolean inLine) throws Exception {
        Integer apType = BeanUtils.convertValue(approveType, Integer.class);
        String userId = null;
        String deptUserId = null;
        WfVariableGlobal global = null;
        HrUser approveUser=null;
        if (apType != null) {
            WfReqMyFlowNode reqMyFlowNode = null;
            if (!inLine) {
                reqMyFlowNode = new WfReqMyFlowNode();
                reqMyFlowNode.setFlowId(wfReqMyFlow);
                reqMyFlowNode.setNodeSeq(i);
                reqMyFlowNode.setNodeType(nodeType);
                reqMyFlowNode.setUseYn("Y");
                bind(reqMyFlowNode);
                reqMyFlowNodeList.add(reqMyFlowNode);
            } else {
                reqMyFlowNode = reqMyFlowNodeList.get(reqMyFlowNodeList.size() - 1);
            }
            WfReqMyFlowNodeApprove reqMyFlowNodeApprove = new WfReqMyFlowNodeApprove();
            reqMyFlowNodeApprove.setNodeId(reqMyFlowNode);
            reqMyFlowNodeApprove.setApproveType(apType);
            reqMyFlowNodeApprove.setUseYn("Y");
            bind(reqMyFlowNodeApprove);
            if(approve!=null){
                approveUser=this.hrUserService.getById(approve);
            }
            if (apType.intValue() == 1) {
                Long globalId = BeanUtils.convertValue(approve, Long.class);
                if (globalId != null) {
                    global = this.wfVariableGlobalService.getById(globalId);
                    if (global != null) {
                        reqMyFlowNodeApprove.setGlobalId(global);
                        reqMyFlowNodeApprove.setUserId(null);
                        reqMyFlowNodeApprove.setDeptUserId(null);
                    }
                }
            } else if (apType.intValue() == 2) {
                reqMyFlowNodeApprove.setGlobalId(null);
                reqMyFlowNodeApprove.setUserId(null);
                reqMyFlowNodeApprove.setDeptUserId(approveUser);
            } else if (apType.intValue() == 3) {
                reqMyFlowNodeApprove.setGlobalId(null);
                reqMyFlowNodeApprove.setUserId(approveUser);
                reqMyFlowNodeApprove.setDeptUserId(null);
            }
            reqMyFlowNodeApproveList.add(reqMyFlowNodeApprove);
        }

    }

    public String delete() throws Exception {
        JSONObject root = new JSONObject();
        root.put("result", "-1");
        List<WfReqMyFlowNode> delReqMyFlowNodeList = null;
        List<WfReqMyFlowNodeApprove> delReqMyFlowNodeApproveList = null;
        List<WfReqMyFlowLast> delReqMyFlowLastList = null;
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (id != null && userInfo != null) {
            wfReqMyFlow = this.wfReqMyFlowService.getById(id);
            if (wfReqMyFlow != null) {
                root.put("flowId", wfReqMyFlow.getId());
                delReqMyFlowNodeList = this.wfReqMyFlowNodeService.getAllByFlowId(wfReqMyFlow.getId());
                delReqMyFlowNodeApproveList = this.wfReqMyFlowNodeApproveService.getByFlowId(wfReqMyFlow.getId());
                delReqMyFlowLastList = this.wfReqMyFlowLastService.getByFlowId(userInfo.getOrgId(),userInfo.getUserId(), wfReqMyFlow.getId());
                this.wfReqMyFlowService.delete(wfReqMyFlow, delReqMyFlowNodeList, delReqMyFlowNodeApproveList, delReqMyFlowLastList);
                root.put("result", "0");
            }
        }
        writeJsonByAction(root.toString());
        return null;
    }

    @PageFlow(result = {@Result(name = "success", path = "/view/wf/myFlow/list.ftl", type = Dispatcher.FreeMarker)})
    public String myFlowList() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && applyId != null) {
                WfReqMyFlowLast wfReqMyFlowLast = this.wfReqMyFlowLastService.getByApplyId(userInfo.getOrgId(),applyId, userInfo.getUserId());
                if (wfReqMyFlowLast != null) {
                    WfReqMyFlow reqMyFlow = wfReqMyFlowLast.getFlowId();
                    if (reqMyFlow != null) {
                        flowId = reqMyFlow.getId();
                    }
                }
                reqMyFlowList = wfReqMyFlowService.getByApplyId(userInfo.getOrgId(),applyId, userInfo.getUserId());
                if (reqMyFlowList != null && !reqMyFlowList.isEmpty()) {
                    for (WfReqMyFlow reqMyFlow : reqMyFlowList) {
                        if (StringUtils.isBlank(reqMyFlow.getFlowName())) {
                            List<WfReqMyFlowNode> reqMyFlowNodeList = this.wfReqMyFlowNodeService.getByFlowId(reqMyFlow.getId());
                            if (reqMyFlowNodeList != null && !reqMyFlowNodeList.isEmpty()) {
                                boolean flag = false;
                                boolean flagAll = false;
                                String flowNodeStep = "";
                                for (WfReqMyFlowNode reqMyFlowNode : reqMyFlowNodeList) {
                                    flag = false;
                                    flagAll = false;
                                    List<String> approveStepList = new ArrayList<String>();
                                    List<WfReqMyFlowNodeApprove> reqMyFlowNodeApproveList = this.wfReqMyFlowNodeApproveService.getByNodeId(reqMyFlowNode.getId());
                                    if (reqMyFlowNodeApproveList != null && !reqMyFlowNodeApproveList.isEmpty()) {
                                        Long approveUserId = null;
                                        String approve = null;
                                        for (WfReqMyFlowNodeApprove nodeApprove : reqMyFlowNodeApproveList) {
                                            flag = false;
                                            approve = null;
                                            if (nodeApprove.getApproveType().intValue() == 1) {
                                                approveUserId = get(nodeApprove, "globalId.userId.id",Long.class);
                                                if (approveUserId!=null) {
                                                    if (approveUserId.equals(userInfo.getUserId())) {
                                                        flag = true;
                                                        flagAll = true;
                                                        continue;
                                                    }
                                                    flagAll = false;
                                                    approve = get(nodeApprove, "globalId.variableName");
                                                }
                                            } else if (nodeApprove.getApproveType().intValue() == 2) {
                                                approveUserId = get(nodeApprove, "deptUserId.id",Long.class);
                                                if (approveUserId!=null) {
//                                                    User managerUser = RequestUser.getManagerUser(userInfo.getAccessToken(), approveUserId);
//                                                    if (managerUser != null) {
//                                                        if (managerUser != null) {
//                                                            if (managerUser.getId().equals(userInfo.getUserId())) {
//                                                                flag = true;
//                                                                flagAll = true;
//                                                                continue;
//                                                            }
//                                                            flagAll = false;
//                                                            approve = "直属上司";
//                                                        }
//                                                    }
                                                }
                                            } else if (nodeApprove.getApproveType().intValue() == 3) {
                                                approveUserId = get(nodeApprove, "userId.id",Long.class);
                                                if (approveUserId!=null) {
                                                    if (approveUserId.equals(userInfo.getUserId())) {
                                                        flag = true;
                                                        flagAll = true;
                                                        continue;
                                                    }
                                                    flagAll = false;
                                                    if(nodeApprove.getUserId()!=null){
                                                        approve = nodeApprove.getUserId().getUserName();
                                                    }
                                                }
                                            }
                                            if (!flag) {
                                                if (approve != null) {
                                                    approveStepList.add(approve);
                                                }
                                            }
                                        }
                                    }
                                    if (reqMyFlowNode.getNodeType().intValue() == 1 && !flag) {
                                        if (approveStepList != null && !approveStepList.isEmpty()) {
                                            String apStepDesc = "";
                                            for (String apStep : approveStepList) {
                                                apStepDesc += apStep + "&";
                                            }
                                            if (StringUtils.isNotBlank(apStepDesc)) {
                                                apStepDesc = apStepDesc.substring(0, apStepDesc.length() - 1);
                                            }
                                            flowNodeStep += apStepDesc + "->";
                                        }
                                    } else if (reqMyFlowNode.getNodeType().intValue() == 2 && !flagAll) {
                                        if (approveStepList != null && !approveStepList.isEmpty()) {
                                            String apStepDesc = "";
                                            for (String apStep : approveStepList) {
                                                apStepDesc += apStep + "&";
                                            }
                                            if (StringUtils.isNotBlank(apStepDesc)) {
                                                apStepDesc = apStepDesc.substring(0, apStepDesc.length() - 1);
                                            }
                                            flowNodeStep += apStepDesc + "->";
                                        }
                                    }
                                }
                                if (StringUtils.isNotBlank(flowNodeStep)) {
                                    flowNodeStep = flowNodeStep.substring(0, flowNodeStep.length() - 2);
                                    reqMyFlow.setFlowName(flowNodeStep);
                                }
                            }
                            WfReqMyFlowNode executorNode = wfReqMyFlowNodeService.getExecutorByFlowId(reqMyFlow.getId());
                            if (executorNode != null) {
                                List<WfReqMyFlowNodeApprove> executorNodeApproveList = this.wfReqMyFlowNodeApproveService.getByNodeId(executorNode.getId());
                                if (executorNodeApproveList != null && !executorNodeApproveList.isEmpty()) {
                                    for (WfReqMyFlowNodeApprove nodeApprove : executorNodeApproveList) {
                                        String approve = null;
                                        if (nodeApprove.getApproveType().intValue() == 1) {
                                            approve = get(nodeApprove, "globalId.variableName");
                                        } else if (nodeApprove.getApproveType().intValue() == 2) {
                                            approve = "直属上司";
                                        } else if (nodeApprove.getApproveType().intValue() == 3) {
                                            if(nodeApprove.getUserId()!=null){
                                                approve = get(nodeApprove, "userId.userName");
                                            }
                                        }
                                        if (StringUtils.isNotBlank(approve)) {
                                            if (StringUtils.isNotBlank(reqMyFlow.getFlowName())) {
                                                reqMyFlow.setFlowName(reqMyFlow.getFlowName() + "->" + approve + "[执行]");
                                            } else {
                                                reqMyFlow.setFlowName(approve + "[执行]");
                                            }
                                        }
                                    }
                                }
                            }
                            this.wfReqMyFlowService.save(reqMyFlow);
                        }
                    }
                }
        }
        return "success";
    }

    @Token
    @PageFlow(result = {@Result(name = "success", path = "/wf/reqMyFlow!myFlowList.dhtml?applyId=${wfReqMyFlow.applyId}", type = Dispatcher.Redirect)})
    public String save() throws Exception {
        JSONObject root=new JSONObject();
        root.put("result","-1");

        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null && approveIndex != null) {
            HrUser user=this.hrUserService.getById(userInfo.getUserId());
            List<WfReqMyFlowNode> delReqMyFlowNodeList = null;
            List<WfReqMyFlowNodeApprove> delReqMyFlowNodeApproveList = null;

            List<WfReqMyFlowNode> reqMyFlowNodeList = null;
            List<WfReqMyFlowNodeApprove> reqMyFlowNodeApproveList = null;
            if (wfReqMyFlow.getId() != null) {
                WfReqMyFlow old = this.wfReqMyFlowService.getById(wfReqMyFlow.getId());
                wfReqMyFlow = this.copy(wfReqMyFlow, old);
                delReqMyFlowNodeList = this.wfReqMyFlowNodeService.getAllByFlowId(wfReqMyFlow.getId());
                delReqMyFlowNodeApproveList = this.wfReqMyFlowNodeApproveService.getByFlowId(wfReqMyFlow.getId());
            }
            wfReqMyFlow.setUserId(user);
            reqMyFlowNodeList = new ArrayList<WfReqMyFlowNode>();
            reqMyFlowNodeApproveList = new ArrayList<WfReqMyFlowNodeApprove>();
            for (int i = 1; i <= approveIndex; i++) {
                Integer nodeType = getParameter("nodeType" + i, Integer.class);
                String approveType = getParameter("approveType" + i);
                String approve = getParameter("approve" + i);
                if (nodeType != null && StringUtils.isNotBlank(approveType) && StringUtils.isNotBlank(approve)) {
                    if (approveType.indexOf(",") != -1 && approve.indexOf(",") != -1) {
                        String[] approveTypes = approveType.split(",");
                        String[] approves = approve.split(",");
                        if (approveTypes != null && approves != null) {
                            if (approveTypes.length == approves.length) {
                                for (int j = 0; j < approves.length; j++) {
                                    String apt = approveTypes[j];
                                    Long a = BeanUtils.convertValue(approves[j],Long.class);
                                    if (StringUtils.isNotBlank(apt) && a!=null) {
                                        if (j == 0) {
                                            bindNodeApprove(i, reqMyFlowNodeList, reqMyFlowNodeApproveList, nodeType, apt, a, false);
                                        } else {
                                            bindNodeApprove(i, reqMyFlowNodeList, reqMyFlowNodeApproveList, nodeType, apt, a, true);
                                        }

                                    }
                                }
                            }
                        }
                    } else {
                        Long apId=BeanUtils.convertValue(approve,Long.class);
                        if(apId!=null){
                            bindNodeApprove(i, reqMyFlowNodeList, reqMyFlowNodeApproveList, nodeType, approveType, apId, false);
                        }
                    }
                }
            }
            SysOrg sysOrg=this.sysOrgService.getById(userInfo.getOrgId());

            wfReqMyFlow.setOrgId(sysOrg);
            wfReqMyFlow.setUseYn("Y");
            bind(wfReqMyFlow);
            wfReqMyFlowService.save(wfReqMyFlow, delReqMyFlowNodeList, delReqMyFlowNodeApproveList, reqMyFlowNodeList, reqMyFlowNodeApproveList);
            //rename
            if (wfReqMyFlow != null) {
                reqMyFlowNodeList = this.wfReqMyFlowNodeService.getByFlowId(wfReqMyFlow.getId());
                if (reqMyFlowNodeList != null && !reqMyFlowNodeList.isEmpty()) {
                    boolean flag = false;
                    boolean flagAll = false;
                    String flowNodeStep = "";
                    for (WfReqMyFlowNode reqMyFlowNode : reqMyFlowNodeList) {
                        flag = false;
                        flagAll = false;
                        List<String> approveStepList = new ArrayList<String>();
                        reqMyFlowNodeApproveList = this.wfReqMyFlowNodeApproveService.getByNodeId(reqMyFlowNode.getId());
                        if (reqMyFlowNodeApproveList != null && !reqMyFlowNodeApproveList.isEmpty()) {
                            Long approveUserId = null;
                            String approve = null;
                            for (WfReqMyFlowNodeApprove nodeApprove : reqMyFlowNodeApproveList) {
                                flag = false;
                                approve = null;
                                if (nodeApprove.getApproveType().intValue() == 1) {
                                    approveUserId = get(nodeApprove, "globalId.userId.id",Long.class);
                                    if (approveUserId!=null) {
                                        if (approveUserId.equals(userInfo.getUserId())) {
                                            flag = true;
                                            flagAll = true;
                                            continue;
                                        }
                                        flagAll = false;
                                        approve = get(nodeApprove, "globalId.variableName");
                                    }
                                } else if (nodeApprove.getApproveType().intValue() == 2) {
                                    approveUserId = get(nodeApprove, "deptUserId.id",Long.class);
                                    if (approveUserId!=null) {
//                                        User managerUser = RequestUser.getManagerUser(userInfo.getAccessToken(), approveUserId);
//                                        if (managerUser != null) {
//                                            if (managerUser != null) {
//                                                if (managerUser.getId().equals(userInfo.getUserId())) {
//                                                    flag = true;
//                                                    flagAll = true;
//                                                    continue;
//                                                }
//                                                flagAll = false;
//                                                approve = "直属上司";
//                                            }
//                                        }
                                    }
                                } else if (nodeApprove.getApproveType().intValue() == 3) {
                                    approveUserId = get(nodeApprove, "userId.id",Long.class);
                                    if (approveUserId!=null) {
                                        if (approveUserId.equals(userInfo.getUserId())) {
                                            flag = true;
                                            flagAll = true;
                                            continue;
                                        }
                                        flagAll = false;
                                        if(nodeApprove.getUserId()!=null){
                                            approve=nodeApprove.getUserId().getUserName();
                                        }
                                    }
                                }
                                if (!flag) {
                                    if (approve != null) {
                                        approveStepList.add(approve);
                                    }
                                }
                            }
                        }
                        if (reqMyFlowNode.getNodeType().intValue() == 1 && !flag) {
                            if (approveStepList != null && !approveStepList.isEmpty()) {
                                String apStepDesc = "";
                                for (String apStep : approveStepList) {
                                    apStepDesc += apStep + "&";
                                }
                                if (StringUtils.isNotBlank(apStepDesc)) {
                                    apStepDesc = apStepDesc.substring(0, apStepDesc.length() - 1);
                                }
                                flowNodeStep += apStepDesc + "->";
                            }
                        } else if (reqMyFlowNode.getNodeType().intValue() == 2 && !flagAll) {
                            if (approveStepList != null && !approveStepList.isEmpty()) {
                                String apStepDesc = "";
                                for (String apStep : approveStepList) {
                                    apStepDesc += apStep + "&";
                                }
                                if (StringUtils.isNotBlank(apStepDesc)) {
                                    apStepDesc = apStepDesc.substring(0, apStepDesc.length() - 1);
                                }
                                flowNodeStep += apStepDesc + "->";
                            }
                        }
                    }
                    if (StringUtils.isNotBlank(flowNodeStep)) {
                        flowNodeStep = flowNodeStep.substring(0, flowNodeStep.length() - 2);
                        wfReqMyFlow.setFlowName(flowNodeStep);
                    }
                }
                wfReqMyFlowService.save(wfReqMyFlow);
                root.put("result","0");
            }
        }
        return "success";
    }

    public String getFlowNodeList() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        JSONObject root = new JSONObject();
        root.put("result", "-1");
        if (id != null && userInfo != null) {
            wfReqMyFlow = this.wfReqMyFlowService.getById(id);
            applyId = wfReqMyFlow.getApplyId();
            if (StringUtils.isNotBlank(applyId)) {
                List<WfReqMyFlowNode> reqMyFlowNodeList = this.wfReqMyFlowNodeService.getByFlowId(wfReqMyFlow.getId());
                JSONArray nodeArray = new JSONArray();
                JSONObject node = null;
                JSONArray approveArray = null;
                JSONObject approve = null;

                node = new JSONObject();
                node.put("nodeSeq", "0000");
                node.put("nodeType", "-1");
                approveArray = new JSONArray();
                approve = new JSONObject();

                approve.put("approveType", "-1");
                approve.put("approveId", userInfo.getUserId());
                approve.put("approveName", userInfo.getUserName());
                approveArray.add(approve);
                node.put("approveList", approveArray);
                nodeArray.add(node);

                if (reqMyFlowNodeList != null && !reqMyFlowNodeList.isEmpty()) {
                    boolean flag = false;
                    boolean flagAll = false;

                    for (WfReqMyFlowNode reqMyFlowNode : reqMyFlowNodeList) {
                        flag = false;
                        flagAll = false;
                        node = new JSONObject();
                        node.put("nodeSeq", reqMyFlowNode.getNodeSeq());
                        node.put("nodeType", reqMyFlowNode.getNodeType());
                        List<WfReqMyFlowNodeApprove> reqMyFlowNodeApproveList = this.wfReqMyFlowNodeApproveService.getByNodeId(reqMyFlowNode.getId());
                        if (reqMyFlowNodeApproveList != null && !reqMyFlowNodeApproveList.isEmpty()) {
                            approveArray = new JSONArray();
                            Long approveUserId = null;
                            for (WfReqMyFlowNodeApprove nodeApprove : reqMyFlowNodeApproveList) {
                                flag = false;
                                approve = new JSONObject();
                                approve.put("approveType", nodeApprove.getApproveType());
                                if (nodeApprove.getApproveType().intValue() == 1) {
                                    approveUserId = get(nodeApprove, "globalId.userId.id",Long.class);
                                    if (approveUserId!=null) {
                                        if (approveUserId.equals(userInfo.getUserId())) {
                                            flag = true;
                                            flagAll = true;
                                            continue;
                                        }
                                        flagAll = false;
                                        approve.put("approveId", approveUserId);
                                        approve.put("approveName", get(nodeApprove, "globalId.variableName"));
                                    }
                                } else if (nodeApprove.getApproveType().intValue() == 2) {
                                    approveUserId = get(nodeApprove, "deptUserId.id",Long.class);
                                    if (approveUserId!=null) {
//                                        User managerUser = null;
//                                        try {
//                                            RequestUser.getManagerUser(userInfo.getAccessToken(), approveUserId);
//                                                if (managerUser != null) {
//                                                    if(managerUser.getId()!=null){
//                                                        if (managerUser.getId().equals(userInfo.getUserId())) {
//                                                            flag = true;
//                                                            flagAll = true;
//                                                            continue;
//                                                        }
//                                                        approve.put("approveId", managerUser.getId());
//                                                        flagAll = false;
//                                                        approve.put("approveName", "直属上司");
//                                                    }else{
//                                                        flagAll = false;
//                                                        if(managerUser.getErrorCode()!=null){
//                                                            if(managerUser.getErrorCode().equals("10401")){
//                                                                approve.put("approveId", "#####");
//                                                                approve.put("approveName", "直属上司(请让管理员安装)");
//                                                            }else if(managerUser.getErrorCode().equals("00000")){
//                                                                approve.put("approveName", "直属上司(请设置直属上司)");
//                                                            }
//                                                        }
//                                                    }
//                                                }
//                                        } catch (Exception e) {
//
//                                        }
                                    }
                                } else if (nodeApprove.getApproveType().intValue() == 3) {
                                    approveUserId = get(nodeApprove, "userId.id",Long.class);
                                    if (approveUserId!=null) {
                                        if (approveUserId.equals(userInfo.getUserId())) {
                                            flag = true;
                                            flagAll = true;
                                            continue;
                                        }
                                        flagAll = false;
                                        approve.put("approveId", approveUserId);
                                        approve.put("approveName", get(nodeApprove,"userId.userName"));
                                    }
                                }
                                if (!flag) {
                                    approveArray.add(approve);
                                }
                            }
                            node.put("approveList", approveArray);
                        }
                        if (reqMyFlowNode.getNodeType().intValue() == 1 && !flag) {
                            nodeArray.add(node);
                        } else if (reqMyFlowNode.getNodeType().intValue() == 2 && !flagAll) {
                            nodeArray.add(node);
                        }
                    }
                }
                root.put("result", "0");
                root.put("nodeList", nodeArray);
            }
        }
        writeJsonByAction(root.toString());
        return null;
    }
}
