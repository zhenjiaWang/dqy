package com.dqy.wf.service;

import com.dqy.wf.entity.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
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
public class WfReqRePaymentService extends HQuery {

    @Inject
    private WfReqService wfReqService;

    @Inject
    private WfReqRePaymentDetailService wfReqRePaymentDetailService;

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqRePayment getById(Long id) {
        return $(id).get(WfReqRePayment.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqRePayment getByReqId(Long reqId) {
        return $($eq("reqId.id", reqId)).get(WfReqRePayment.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqRePayment> getByList(List<Selector> selectorList) {
        return $(selectorList).list(WfReqRePayment.class);
    }
    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqRePayment wfReqRePayment) {
        $(wfReqRePayment).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<WfReqRePayment> reqRePaymentList) {
        $(reqRePaymentList).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReqRePayment wfReqRePayment) {
        $(wfReqRePayment).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<WfReqRePayment> reqRePaymentList) {
        $(reqRePaymentList).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfReqRePayment.class);
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqRePayment wfReqRePayment, List<WfReqRePaymentDetail> detailList,WfReq wfReq, List<WfReqComments> reqCommentsList,
                     WfReqNoSeq wfReqNoSeq, List<WfReqNodeApprove> reqNodeApproveList, List<WfReqTask> reqTaskList,
                     WfReqMyFlowLast wfReqMyFlowLast,List<WfReqAtt> reqAttList) {
        this.wfReqService.save(wfReq, reqCommentsList, wfReqNoSeq, reqNodeApproveList, reqTaskList, wfReqMyFlowLast,reqAttList);
        this.save(wfReqRePayment);
        if(detailList!=null&&!detailList.isEmpty()){
            wfReqRePaymentDetailService.save(detailList);
        }
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public Double getSumByReAmount(Long orgId, Long userId, Long advanceId) {
        return $($alias("reqId", "reqId"),
                $eq("reqId.orgId.id", orgId), $eq("reqId.userId.id", userId),
                $eq("reqId.applyState", 2), $eq("reqId.applyResult", 1), $eq("reqId.complete", 1),
                $eq("advanceId.id", advanceId), $sum("amount")).value(WfReqRePayment.class, Double.class);
    }


}
