package com.dqy.wf.service;

import com.dqy.wf.entity.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.guiceside.persistence.TransactionType;
import org.guiceside.persistence.Transactional;
import org.guiceside.persistence.hibernate.dao.hquery.HQuery;

import java.util.List;

/**
 * @author zhenjiaWang
 * @version 1.0 2012-05
 * @since JDK1.5
 */
@Singleton
public class WfReqBusinessService extends HQuery {

    @Inject
    private WfReqService wfReqService;


    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqBusiness getById(Long id) {
        return $(id).get(WfReqBusiness.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqBusiness getByReqId(Long reqId) {
        return $($eq("reqId.id", reqId)).get(WfReqBusiness.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqBusiness wfReqBusiness) {
        $(wfReqBusiness).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<WfReqBusiness> wfReqBusinessList) {
        $(wfReqBusinessList).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReqBusiness wfReqBusiness) {
        $(wfReqBusiness).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<WfReqBusiness> wfReqBusinessList) {
        $(wfReqBusinessList).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfReqBusiness.class);
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqBusiness wfReqBusiness,  WfReq wfReq, List<WfReqComments> reqCommentsList,
                     WfReqNoSeq wfReqNoSeq, List<WfReqNodeApprove> reqNodeApproveList, List<WfReqTask> reqTaskList,
                     WfReqMyFlowLast wfReqMyFlowLast) {
        this.wfReqService.save(wfReq, reqCommentsList, wfReqNoSeq, reqNodeApproveList, reqTaskList, wfReqMyFlowLast);
        this.save(wfReqBusiness);
    }

}
