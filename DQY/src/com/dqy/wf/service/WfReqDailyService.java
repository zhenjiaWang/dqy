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
public class WfReqDailyService extends HQuery {

    @Inject
    private WfReqService wfReqService;

    @Inject
    private WfReqDailyDetailService wfReqDailyDetailService;


    @Inject
    private WfReqDailyTrueService wfReqDailyTrueService;


    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqDaily getById(Long id) {
        return $(id).get(WfReqDaily.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqDaily> getByList(List<Selector> selectorList) {
        return $(selectorList).list(WfReqDaily.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqDaily getByReqId(Long reqId) {
        return $($eq("reqId.id", reqId)).get(WfReqDaily.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqDaily wfReqDaily) {
        $(wfReqDaily).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<WfReqDaily> wfReqDailyList) {
        $(wfReqDailyList).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReqDaily wfReqDaily) {
        $(wfReqDaily).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<WfReqDaily> wfReqDailyList) {
        $(wfReqDailyList).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfReqDaily.class);
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqDaily wfReqDaily, List<WfReqDailyDetail> detailList, WfReq wfReq, List<WfReqComments> reqCommentsList,
                     WfReqNoSeq wfReqNoSeq, List<WfReqNodeApprove> reqNodeApproveList, List<WfReqTask> reqTaskList,
                     WfReqMyFlowLast wfReqMyFlowLast, List<WfReqAtt> reqAttList) {
        this.wfReqService.save(wfReq, reqCommentsList, wfReqNoSeq, reqNodeApproveList, reqTaskList, wfReqMyFlowLast, reqAttList);
        this.save(wfReqDaily);
        if (detailList != null && !detailList.isEmpty()) {
            wfReqDailyDetailService.save(detailList);
        }
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqDaily wfReqDaily, List<WfReqDailyDetail> detailList, List<WfReqDailyTrue> trueList, WfReq wfReq, List<WfReqComments> reqCommentsList,
                     WfReqNoSeq wfReqNoSeq, List<WfReqNodeApprove> reqNodeApproveList, List<WfReqTask> reqTaskList,
                     WfReqMyFlowLast wfReqMyFlowLast, List<WfReqAtt> reqAttList) {
        this.wfReqService.save(wfReq, reqCommentsList, wfReqNoSeq, reqNodeApproveList, reqTaskList, wfReqMyFlowLast, reqAttList);
        this.save(wfReqDaily);
        if (detailList != null && !detailList.isEmpty()) {
            wfReqDailyDetailService.save(detailList);
        }
        if (trueList != null && !trueList.isEmpty()) {
            wfReqDailyTrueService.save(trueList);
        }
    }

}
