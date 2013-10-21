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
public class WfReqAdvanceAccountService extends HQuery {

    @Inject
    private WfReqService wfReqService;
    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqAdvanceAccount getById(Long id) {
        return $(id).get(WfReqAdvanceAccount.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqAdvanceAccount getByReqId(Long reqId) {
        return $($eq("reqId.id",reqId)).get(WfReqAdvanceAccount.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqAdvanceAccount wfReqAdvanceAccount) {
        $(wfReqAdvanceAccount).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<WfReqAdvanceAccount> reqAdvanceAccountList) {
        $(reqAdvanceAccountList).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReqAdvanceAccount wfReqAdvanceAccount) {
        $(wfReqAdvanceAccount).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<WfReqAdvanceAccount> reqAdvanceAccountList) {
        $(reqAdvanceAccountList).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfReqAdvanceAccount.class);
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqAdvanceAccount wfReqAdvanceAccount, WfReq wfReq, List<WfReqComments> reqCommentsList,
                     WfReqNoSeq wfReqNoSeq, List<WfReqNodeApprove> reqNodeApproveList, List<WfReqTask> reqTaskList,
                     WfReqMyFlowLast wfReqMyFlowLast,List<WfReqAtt> reqAttList) {
        this.wfReqService.save(wfReq,  reqCommentsList, wfReqNoSeq, reqNodeApproveList, reqTaskList, wfReqMyFlowLast,reqAttList);
        this.save(wfReqAdvanceAccount);
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqAdvanceAccount> getListByReUserId(Long orgId,Long userId) {
        return $($alias("reqId","reqId"),$eq("reqId.orgId.id",orgId),$eq("reqId.userId.id",userId),$eq("rePayYn","N")).list(WfReqAdvanceAccount.class);
    }

}
