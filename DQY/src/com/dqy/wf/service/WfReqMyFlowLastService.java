package com.dqy.wf.service;

import com.dqy.wf.entity.WfReqMyFlowLast;
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
public class WfReqMyFlowLastService extends HQuery {

    @Transactional(type = TransactionType.READ_ONLY)
    public Page<WfReqMyFlowLast> getPageList(int start,
                                             int limit, List<Selector> selectorList) {
        return $(selectorList).page(WfReqMyFlowLast.class, start, limit);
    }

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqMyFlowLast getById(Long id) {
        return $(id).get(WfReqMyFlowLast.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqMyFlowLast wfReqMyFlowLast) {
        $(wfReqMyFlowLast).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReqMyFlowLast wfReqMyFlowLast) {
        $(wfReqMyFlowLast).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<WfReqMyFlowLast> reqMyFlowLastList) {
        $(reqMyFlowLastList).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfReqMyFlowLast.class);
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqMyFlowLast getByApplyId(Long orgId,String applyId, Long userId) {
        return $($eq("orgId.id", orgId),$eq("applyId", applyId), $eq("userId.id", userId), $eq("useYn", "Y")).get(WfReqMyFlowLast.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqMyFlowLast> getByFlowId(Long orgId,Long userId, Long flowId) {
        return $($eq("orgId.id", orgId),$eq("flowId.id", flowId), $eq("userId.id", userId), $eq("useYn", "Y")).list(WfReqMyFlowLast.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqMyFlowLast> getByApplyId(Long orgId,String applyId) {
        return $($eq("orgId.id", orgId),$eq("applyId", applyId), $eq("useYn", "Y")).list(WfReqMyFlowLast.class);
    }

}
