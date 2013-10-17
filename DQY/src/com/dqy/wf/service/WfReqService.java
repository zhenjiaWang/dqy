package com.dqy.wf.service;

import com.dqy.wf.entity.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.guiceside.commons.Page;
import org.guiceside.persistence.TransactionType;
import org.guiceside.persistence.Transactional;
import org.guiceside.persistence.hibernate.dao.hquery.HQuery;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;

import java.util.List;

/**
 * @author zhenjiaWang
 * @version 1.0 2012-05
 * @since JDK1.5
 */
@Singleton
public class WfReqService extends HQuery {


    @Inject
    private WfReqCommentsService wfReqCommentsService;

    @Inject
    private WfReqNoSeqService wfReqNoSeqService;

    @Inject
    private WfReqNodeApproveService wfReqNodeApproveService;

    @Inject
    private WfReqTaskService wfReqTaskService;

    @Inject
    private WfReqRePaymentService wfReqRePaymentService;

    @Inject
    private WfReqRePaymentDetailService wfReqRePaymentDetailService;

    @Inject
    private WfReqAdvanceAccountService wfReqAdvanceAccountService;

    @Inject
    private WfReqBusinessService wfReqBusinessService;

    @Inject
    private WfReqDailyService wfReqDailyService;

    @Inject
    private WfReqDailyDetailService wfReqDailyDetailService;

    @Inject
    private WfReqMyFlowLastService wfReqMyFlowLastService;

    @Transactional(type = TransactionType.READ_ONLY)
    public Page<WfReq> getPageList(int start,
                                   int limit, List<Selector> selectorList) {
        return $(selectorList).page(WfReq.class, start, limit);
    }

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public WfReq getById(Long id) {
        return $(id).get(WfReq.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReq wfReq) {
        $(wfReq).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReq wfReq) {
        $(wfReq).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfReq.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer getCountByApplyId(Long orgId,String applyId) {
        return $($eq("orgId.id", orgId),$eq("applyId", applyId), $count("id")).value(WfReq.class, Integer.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer getCountPassed(Long orgId,Long  userId) {
        return $($count("id"), $eq("orgId.id", orgId), $eq("userId.id", userId),
                $eq("applyState", 2), $eq("applyResult", 1),
                $eq("complete", 1), $eq("tip", 1), $eq("useYn", "Y")).value(WfReq.class, Integer.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer getCountRejected(Long orgId,Long  userId) {
        return $($count("id"), $eq("orgId.id", orgId), $eq("userId.id", userId),
                $eq("applyState", 2), $eq("applyResult", 2),
                $eq("complete", 1), $eq("tip", 1), $eq("useYn", "Y")).value(WfReq.class, Integer.class);
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReq wfReq, List<WfReqComments> reqCommentsList,
                     WfReqNoSeq wfReqNoSeq, List<WfReqNodeApprove> reqNodeApproveList, List<WfReqTask> reqTaskList,
                     WfReqMyFlowLast wfReqMyFlowLast) {
        $(wfReq).save();
        if (reqCommentsList != null && !reqCommentsList.isEmpty()) {
            this.wfReqCommentsService.save(reqCommentsList);
        }
        if (wfReqNoSeq != null) {
            this.wfReqNoSeqService.save(wfReqNoSeq);
        }
        if (reqNodeApproveList != null && !reqNodeApproveList.isEmpty()) {
            this.wfReqNodeApproveService.save(reqNodeApproveList);
        }
        if (reqTaskList != null && !reqTaskList.isEmpty()) {
            this.wfReqTaskService.save(reqTaskList);
        }
        if (wfReqMyFlowLast != null) {
            this.wfReqMyFlowLastService.save(wfReqMyFlowLast);
        }
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReq wfReq,  WfReqAdvanceAccount advanceAccount,
            WfReqRePayment reqRePayment,
            List<WfReqRePaymentDetail> rePaymentDetailList,
            WfReqDaily reqDaily,
            List<WfReqDailyDetail> reqDailyDetailList,
            WfReqBusiness reqBusiness,
            List<WfReqComments> delReqCommentsList,
                       List<WfReqNodeApprove> delReqNodeApproveList, List<WfReqTask> delReqTaskList) {

        if (delReqCommentsList != null && !delReqCommentsList.isEmpty()) {
            this.wfReqCommentsService.delete(delReqCommentsList);
        }
        if (delReqNodeApproveList != null && !delReqNodeApproveList.isEmpty()) {
            this.wfReqNodeApproveService.delete(delReqNodeApproveList);
        }
        if (delReqTaskList != null && !delReqTaskList.isEmpty()) {
            this.wfReqTaskService.delete(delReqTaskList);
        }
        if(advanceAccount!=null){
            this.wfReqAdvanceAccountService.delete(advanceAccount);
        }
        if(reqRePayment!=null){
            this.wfReqRePaymentService.delete(reqRePayment);
        }
        if (rePaymentDetailList != null && !rePaymentDetailList.isEmpty()) {
            this.wfReqRePaymentDetailService.delete(rePaymentDetailList);
        }
        if(reqDaily!=null){
            this.wfReqDailyService.delete(reqDaily);
        }
        if (reqDailyDetailList != null && !reqDailyDetailList.isEmpty()) {
            this.wfReqDailyDetailService.delete(reqDailyDetailList);
        }
        if(reqBusiness!=null){
            this.wfReqBusinessService.delete(reqBusiness);
        }
        $(wfReq).delete();
    }
}
