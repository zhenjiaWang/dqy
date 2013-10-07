package com.dqy.wf.service;

import com.dqy.wf.entity.WfReqMyFlowNode;
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
public class WfReqMyFlowNodeService extends HQuery {


    
    @Transactional(type = TransactionType.READ_ONLY)
    public Page<WfReqMyFlowNode> getPageList(int start,
                                             int limit, List<Selector> selectorList) {
        return $(selectorList).page(WfReqMyFlowNode.class, start, limit);
    }

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqMyFlowNode getById(Long id) {
        return $(id).get(WfReqMyFlowNode.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqMyFlowNode wfReqMyFlowNode) {
        $(wfReqMyFlowNode).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<WfReqMyFlowNode> reqMyFlowNodeList) {
        $(reqMyFlowNodeList).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReqMyFlowNode wfReqMyFlowNode) {
        $(wfReqMyFlowNode).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<WfReqMyFlowNode> reqMyFlowNodeList) {
        $(reqMyFlowNodeList).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfReqMyFlowNode.class);
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqMyFlowNode> getByFlowId(Long flowId) {
        return $($eq("flowId.id", flowId), $not($eq("nodeSeq",9999)),$eq("useYn", "Y"), $order("nodeSeq")).list(WfReqMyFlowNode.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqMyFlowNode> getAllByFlowId(Long flowId) {
        return $($eq("flowId.id", flowId), $eq("useYn", "Y"), $order("nodeSeq")).list(WfReqMyFlowNode.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqMyFlowNode getExecutorByFlowId(Long flowId) {
        return $($eq("flowId.id", flowId), $eq("nodeSeq",9999),$eq("useYn", "Y")).get(WfReqMyFlowNode.class);
    }

}
