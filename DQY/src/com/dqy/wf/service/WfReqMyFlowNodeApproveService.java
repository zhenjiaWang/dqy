package com.dqy.wf.service;

import com.dqy.wf.entity.WfReqMyFlowNodeApprove;
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
public class WfReqMyFlowNodeApproveService extends HQuery {

    @Transactional(type = TransactionType.READ_ONLY)
    public Page<WfReqMyFlowNodeApprove> getPageList(int start,
                                                    int limit, List<Selector> selectorList) {
        return $(selectorList).page(WfReqMyFlowNodeApprove.class, start, limit);
    }

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqMyFlowNodeApprove getById(Long id) {
        return $(id).get(WfReqMyFlowNodeApprove.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqMyFlowNodeApprove wfReqMyFlowNodeApprove) {
        $(wfReqMyFlowNodeApprove).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<WfReqMyFlowNodeApprove> reqMyFlowNodeApproveList) {
        $(reqMyFlowNodeApproveList).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReqMyFlowNodeApprove wfReqMyFlowNodeApprove) {
        $(wfReqMyFlowNodeApprove).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<WfReqMyFlowNodeApprove> reqMyFlowNodeApproveList) {
        $(reqMyFlowNodeApproveList).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfReqMyFlowNodeApprove.class);
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqMyFlowNodeApprove> getByNodeId(Long nodeId) {
        return $($eq("nodeId.id", nodeId), $eq("useYn", "Y")).list(WfReqMyFlowNodeApprove.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqMyFlowNodeApprove> getByFlowId(Long flowId) {
        return $($alias("nodeId", "nodeId"), $eq("nodeId.flowId.id", flowId), $eq("useYn", "Y")).list(WfReqMyFlowNodeApprove.class);
    }


}
