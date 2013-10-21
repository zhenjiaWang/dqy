package com.dqy.wf.action;

import com.dqy.common.UserInfo;
import com.dqy.common.UserSession;
import com.dqy.common.entity.TempAtt;
import com.dqy.common.service.TempAttService;
import com.dqy.hr.entity.HrUser;
import com.dqy.hr.service.HrUserService;
import com.dqy.sys.entity.SysOrg;
import com.dqy.sys.service.SysOrgService;
import com.dqy.web.support.ActionSupport;
import com.dqy.wf.entity.*;
import com.dqy.wf.service.*;
import com.google.inject.Inject;
import org.guiceside.commons.lang.DateFormatUtil;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.support.file.FileManager;
import org.guiceside.web.annotation.ModelDriver;
import org.guiceside.web.annotation.ReqGet;
import org.guiceside.web.annotation.ReqSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 13-10-7
 * Time: 下午7:48
 * To change this template use File | Settings | File Templates.
 */
public class WfReqSupportAction<T> extends ActionSupport<T> {
    @Inject
    private WfReqNoSeqService wfReqNoSeqService;

    @Inject
    private WfReqNoService wfReqNoService;

    @Inject
    private WfReqMyFlowLastService wfReqMyFlowLastService;
    @Inject
    private SysOrgService sysOrgService;

    @Inject
    private HrUserService hrUserService;

    @Inject
    private WfReqMyFlowService wfReqMyFlowService;

    @Inject
    private WfReqMyFlowNodeService wfReqMyFlowNodeService;

    @Inject
    private WfReqMyFlowNodeApproveService wfReqMyFlowNodeApproveService;

    @Inject
    private TempAttService tempAttService;

    @ReqGet
    @ModelDriver
    @ReqSet
    protected WfReq wfReq;

    @ReqGet
    private Long flowId;

    protected List<WfReqComments> wfReqCommentsList;

    protected WfReqNoSeq wfReqNoSeq;

    protected List<WfReqNodeApprove> reqNodeApproveList;

    protected List<WfReqTask> reqTaskList;

    protected WfReqMyFlowLast wfReqMyFlowLast;

    protected List<WfReqAtt> reqAttList;

    @Override
    public String execute() throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private List<WfReqAtt> bindReqAtt(UserInfo userInfo,WfReq wfReq,String applyId) throws Exception{
        List<WfReqAtt> attList=null;
        if(wfReq!=null&&StringUtils.isNotBlank(applyId)&& StringUtils.isNotBlank(attToken)){
            List<TempAtt> delTempAttList=tempAttService.getListByNotTokenId(applyId, attToken, userInfo.getOrgId(), userInfo.getUserId());
            if(delTempAttList!=null&&!delTempAttList.isEmpty()){
                for(TempAtt att:delTempAttList){
                    if(StringUtils.isNotBlank(att.getSource())){
                        if(FileManager.isExists(att.getSource())){
                            FileManager.deleteFile(att.getSource());
                        }
                    }
                }
                this.tempAttService.delete(delTempAttList);
            }

            List<TempAtt> tempAttList=tempAttService.getListByTokenId(applyId, attToken, userInfo.getOrgId(), userInfo.getUserId());
            if(tempAttList!=null&&!tempAttList.isEmpty()){
                attList=new ArrayList<WfReqAtt>();
                for(TempAtt att:tempAttList){
                    WfReqAtt wfReqAtt=new WfReqAtt();
                    wfReqAtt.setReqId(wfReq);
                    wfReqAtt.setSource(att.getSource());
                    wfReqAtt.setAttKey(att.getAttKey());
                    wfReqAtt.setOldName(att.getOldName());
                    wfReqAtt.setNewName(att.getNewName());
                    wfReqAtt.setPostfix(att.getPostfix());
                    wfReqAtt.setAttSize(att.getAttSize());
                    wfReqAtt.setYear(att.getYear());
                    wfReqAtt.setMonth(att.getMonth());
                    wfReqAtt.setDay(att.getDay());
                    wfReqAtt.setUseYn("Y");
                    attList.add(wfReqAtt);
                }
                this.tempAttService.delete(tempAttList);
            }
        }
        return attList;
    }
    public void initReq() throws Exception {
        UserInfo userInfo = UserSession.getUserInfo(getHttpServletRequest());
        if (userInfo != null) {
            if (wfReq != null && flowId != null) {
                HrUser user = hrUserService.getById(userInfo.getUserId());
                SysOrg sysOrg = this.sysOrgService.getById(userInfo.getOrgId());
                if (user != null && sysOrg != null) {
                    wfReq.setUserId(user);
                    wfReq.setOrgId(sysOrg);
                    wfReq.setTip(0);
                    wfReq.setApplyState(0);
                    wfReq.setApplyResult(0);
                    wfReq.setComplete(0);
                    wfReq.setFinancialYn("N");
                    wfReq.setUseYn("Y");
                    bind(wfReq);

                    wfReqCommentsList = new ArrayList<WfReqComments>();
                    reqNodeApproveList = null;
                    reqTaskList = null;
                    wfReqNoSeq = null;
                    /*起草申请*/
                    WfReqComments comments = new WfReqComments();
                    comments.setReqId(wfReq);
                    comments.setApprove(0);
                    comments.setUserId(user);
                    comments.setAction(0);
                    comments.setUseYn("Y");
                    bind(comments);
                    wfReqCommentsList.add(comments);

                    wfReqNoSeq = buildReqNo(userInfo.getOrgId(), wfReq.getApplyId());
                    reqAttList=bindReqAtt(userInfo,wfReq, wfReq.getApplyId());
                    String reqNo = null;
                    if (wfReqNoSeq != null) {
                        int next = wfReqNoSeq.getNextSeq();
                        String no = null;
                        if (next < 10) {
                            no = "000" + next;
                        } else if (next < 100) {
                            no = "00" + next;
                        } else if (next < 1000) {
                            no = "0" + next;
                        } else {
                            no = String.valueOf(next);
                        }
                        WfReqNo wfReqNo = this.wfReqNoService.getCurrentReqNo(userInfo.getOrgId(),wfReq.getApplyId());
                        if(wfReqNo!=null){
                            reqNo = wfReqNo.getReqNo() + "-" + wfReqNoSeq.getDateYear() + "-" + no;
                        }else{
                            reqNo = "NO" + "-" + wfReqNoSeq.getDateYear() + "-" + no;
                        }
                    }
                    wfReq.setReqNo(reqNo);
                    Date currentDate = DateFormatUtil.getCurrentDate(true);
                    wfReq.setSendDate(currentDate);
                    wfReq.setApplyState(1);
                    /*发送申请*/
                    comments = new WfReqComments();
                    comments.setReqId(wfReq);
                    comments.setApprove(0);
                    comments.setUserId(user);
                    comments.setAction(1);
                    comments.setUseYn("Y");
                    bind(comments);
                    wfReqCommentsList.add(comments);
                    WfReqMyFlow wfReqMyFlow = this.wfReqMyFlowService.getById(flowId);
                    if (wfReqMyFlow != null) {
                        reqNodeApproveList = new ArrayList<WfReqNodeApprove>();
                        if (wfReq.getNodeCount().intValue() > 0) {
                            for (int i = 1; i <= wfReq.getNodeCount(); i++) {
                                Integer nodeSeq = getParameter("reqNodeSeq" + i, Integer.class);
                                Integer nodeType = getParameter("reqNodeType" + i, Integer.class);
                                if (nodeSeq != null) {
                                    if (nodeType.intValue() == 1) {
                                        Long approveId = getParameter("reqApproveId" + i, Long.class);
                                        Integer approveType = getParameter("reqApproveType" + i, Integer.class);
                                        if (approveId != null && approveType != null) {
                                            if (approveId.equals(wfReq.getUserId().getId())) {
                                                continue;
                                            }
                                            HrUser approveUser = this.hrUserService.getById(approveId);
                                            if (approveUser != null) {
                                                WfReqNodeApprove nodeApprove = new WfReqNodeApprove();
                                                nodeApprove.setReqId(wfReq);
                                                nodeApprove.setOrgId(sysOrg);
                                                nodeApprove.setNodeSeq(nodeSeq);
                                                nodeApprove.setNodeType(nodeType);
                                                nodeApprove.setUserId(approveUser);
                                                nodeApprove.setApproveType(approveType);
                                                nodeApprove.setUseYn("Y");
                                                bind(nodeApprove);
                                                reqNodeApproveList.add(nodeApprove);
                                            }
                                        }
                                    }
                                }
                            }
                            Integer firstNodeSeq = null;
                            List<WfReqNodeApprove> firstNodeApproveList = new ArrayList<WfReqNodeApprove>();
                            if (reqNodeApproveList != null && !reqNodeApproveList.isEmpty()) {
                                WfReqNodeApprove reqNodeApprove = reqNodeApproveList.get(0);
                                if (reqNodeApprove != null) {
                                    firstNodeApproveList.add(reqNodeApprove);
                                    firstNodeSeq = reqNodeApprove.getNodeSeq();
                                }
                            }
                            if (firstNodeApproveList != null && !firstNodeApproveList.isEmpty()) {
                                reqTaskList = new ArrayList<WfReqTask>();
                                for (WfReqNodeApprove firstNodeApprove : firstNodeApproveList) {
                                    if (firstNodeApprove != null) {
                                        wfReq.setCurrentNode(firstNodeApprove);
                                        comments = new WfReqComments();
                                        comments.setReqId(wfReq);
                                        comments.setApprove(0);
                                        comments.setUserId(firstNodeApprove.getUserId());
                                        comments.setAction(2);
                                        comments.setUseYn("Y");
                                        bind(comments);
                                        wfReqCommentsList.add(comments);


                                        currentDate = DateFormatUtil.getCurrentDate(true);
                                        WfReqTask reqTask = new WfReqTask();
                                        reqTask.setReqId(wfReq);
                                        reqTask.setUserId(firstNodeApprove.getUserId());
                                        reqTask.setOrgId(sysOrg);
                                        reqTask.setNodeSeq(firstNodeApprove.getNodeSeq());
                                        reqTask.setReceiveDate(currentDate);
                                        reqTask.setTaskRead(0);
                                        reqTask.setTaskState(0);
                                        reqTask.setApproveIdea(0);
                                        reqTask.setNodeId(firstNodeApprove);
                                        reqTask.setUseYn("Y");
                                        bind(reqTask);
                                        reqTaskList.add(reqTask);
                                    }
                                }
                            }
                            wfReqMyFlowLast = null;
                            if (flowId != null) {
                                WfReqMyFlow reqMyFlow = null;
                                reqMyFlow = this.wfReqMyFlowService.getById(flowId);
                                if (reqMyFlow != null) {
                                    wfReqMyFlowLast = wfReqMyFlowLastService.getByApplyId(userInfo.getOrgId(), wfReq.getApplyId(), userInfo.getUserId());
                                    if (wfReqMyFlowLast == null) {
                                        wfReqMyFlowLast = new WfReqMyFlowLast();
                                        wfReqMyFlowLast.setApplyId(wfReq.getApplyId());
                                        wfReqMyFlowLast.setUserId(user);
                                        wfReqMyFlowLast.setUseYn("Y");
                                    }
                                    wfReqMyFlowLast.setFlowId(reqMyFlow);
                                    bind(wfReqMyFlowLast);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private WfReqNoSeq buildReqNo(Long orgId, String applyId) throws Exception {
        Date currentDate = DateFormatUtil.getCurrentDate(true);
        int dateYear = DateFormatUtil.getDayInYear(currentDate);
        WfReqNoSeq wfReqNoSeq = wfReqNoSeqService.getCurrentReqNoSeq(orgId, applyId, dateYear);
        if (wfReqNoSeq == null) {
            SysOrg sysOrg = this.sysOrgService.getById(orgId);
            wfReqNoSeq = new WfReqNoSeq();
            wfReqNoSeq.setOrgId(sysOrg);
            wfReqNoSeq.setApplyId(applyId);
            wfReqNoSeq.setDateYear(dateYear);
            wfReqNoSeq.setNextSeq(1);
            wfReqNoSeq.setUseYn("Y");
        } else {
            wfReqNoSeq.setNextSeq(wfReqNoSeq.getNextSeq() + 1);
        }
        bind(wfReqNoSeq);
        return wfReqNoSeq;
    }
}
