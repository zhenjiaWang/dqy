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
public class WfReqSaleService extends HQuery {

    @Inject
    private WfReqService wfReqService;

    @Inject
    private WfReqSaleDetailService wfReqSaleDetailService;

    @Inject
    private WfReqSaleDetailTrueService wfReqSaleDetailTrueService;

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqSale getById(Long id) {
        return $(id).get(WfReqSale.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqSale> getByList(List<Selector> selectorList) {
        return $(selectorList).list(WfReqSale.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqSale getByReqId(Long reqId) {
        return $($eq("reqId.id", reqId)).get(WfReqSale.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqSale wfReqSale) {
        $(wfReqSale).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<WfReqSale> saleList) {
        $(saleList).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReqSale wfReqSale) {
        $(wfReqSale).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<WfReqSale> saleList) {
        $(saleList).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfReqSale.class);
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqSale wfReqSale, List<WfReqSaleDetail> reqSaleDetailList, WfReq wfReq, List<WfReqComments> reqCommentsList,
                     WfReqNoSeq wfReqNoSeq, List<WfReqNodeApprove> reqNodeApproveList, List<WfReqTask> reqTaskList,
                     WfReqMyFlowLast wfReqMyFlowLast, List<WfReqAtt> reqAttList) {
        this.wfReqService.save(wfReq, reqCommentsList, wfReqNoSeq, reqNodeApproveList, reqTaskList, wfReqMyFlowLast, reqAttList);
        this.save(wfReqSale);
        if (reqSaleDetailList != null && !reqSaleDetailList.isEmpty()) {
            this.wfReqSaleDetailService.save(reqSaleDetailList);
        }
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void saveTrue(WfReqSale wfReqSale, List<WfReqSaleDetail> reqSaleDetailList, List<WfReqSaleDetailTrue> reqSaleDetailTrueList, WfReq wfReq, List<WfReqComments> reqCommentsList,
                         WfReqNoSeq wfReqNoSeq, List<WfReqNodeApprove> reqNodeApproveList, List<WfReqTask> reqTaskList,
                         WfReqMyFlowLast wfReqMyFlowLast, List<WfReqAtt> reqAttList) {
        this.wfReqService.save(wfReq, reqCommentsList, wfReqNoSeq, reqNodeApproveList, reqTaskList, wfReqMyFlowLast, reqAttList);
        this.save(wfReqSale);
        if (reqSaleDetailList != null && !reqSaleDetailList.isEmpty()) {
            this.wfReqSaleDetailService.save(reqSaleDetailList);
        }
        if (reqSaleDetailTrueList != null && !reqSaleDetailTrueList.isEmpty()) {
            this.wfReqSaleDetailTrueService.save(reqSaleDetailTrueList);
        }
    }
}
