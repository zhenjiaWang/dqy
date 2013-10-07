package com.dqy.wf.service;

import com.dqy.wf.entity.WfReqNodeApprove;
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
public class WfReqNodeApproveService extends HQuery {

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqNodeApprove getById(Long id) {
        return $(id).get(WfReqNodeApprove.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqNodeApprove wfReqNodeApprove) {
        $(wfReqNodeApprove).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<WfReqNodeApprove> wfReqNodeApproveList) {
        $(wfReqNodeApproveList).save();
    }


    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReqNodeApprove wfReqNodeApprove) {
        $(wfReqNodeApprove).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<WfReqNodeApprove> wfReqNodeApproveList) {
        $(wfReqNodeApproveList).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfReqNodeApprove.class);
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqNodeApprove> getNodeListByReqIdNodeSeq(Long orgId,Long reqId, Integer nodeSeq) {
        return $($eq("orgId.id", orgId),$eq("reqId.id", reqId), $eq("nodeSeq", nodeSeq), $eq("useYn", "Y")).list(WfReqNodeApprove.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqNodeApprove> getGtNodeListByReqIdNodeSeq(Long orgId,Long reqId, Integer nodeSeq) {
        return $($eq("orgId.id", orgId),$eq("reqId.id", reqId), $gt("nodeSeq", nodeSeq), $eq("useYn", "Y")).list(WfReqNodeApprove.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqNodeApprove> getNodeListByReqId(Long orgId,Long reqId) {
        return $($eq("orgId.id", orgId),$eq("reqId.id", reqId), $eq("useYn", "Y"),$order("nodeSeq")).list(WfReqNodeApprove.class);
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public Integer getCountExecutorByReqId(Long orgId,Long reqId) {
        return $($eq("orgId.id", orgId),$count("id"),$eq("reqId.id", reqId), $eq("nodeSeq", 9999),$eq("useYn", "Y")).value(WfReqNodeApprove.class,Integer.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqNodeApprove getExecutorByReqId(Long orgId,Long reqId) {
        return $($eq("orgId.id", orgId),$eq("reqId.id", reqId), $eq("nodeSeq", 9999),$eq("useYn", "Y")).get(WfReqNodeApprove.class);
    }


}
