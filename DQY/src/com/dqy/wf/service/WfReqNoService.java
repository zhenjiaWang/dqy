package com.dqy.wf.service;

import com.dqy.wf.entity.WfReqNo;
import com.google.inject.Singleton;
import org.guiceside.persistence.TransactionType;
import org.guiceside.persistence.Transactional;
import org.guiceside.persistence.hibernate.dao.hquery.HQuery;

/**
 * @author zhenjiaWang
 * @version 1.0 2012-05
 * @since JDK1.5
 */
@Singleton
public class WfReqNoService extends HQuery {

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqNo getById(Long id) {
        return $(id).get(WfReqNo.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqNo wfReqNo) {
        $(wfReqNo).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReqNo wfReqNo) {
        $(wfReqNo).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfReqNo.class);
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqNo getCurrentReqNo(Long orgId, String applyId) {
        return $($eq("orgId.id", orgId), $eq("applyId", applyId), $eq("useYn", "Y")).get(WfReqNo.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer validateReqNo(Long orgId, String reqNo) {
        return $($count("id"), $eq("orgId.id", orgId), $eq("reqNo", reqNo)).value(WfReqNo.class, Integer.class);
    }
}
