package com.dqy.wf.service;

import com.dqy.wf.entity.WfReqAtt;
import com.dqy.wf.entity.WfReqExecute;
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
public class WfReqExecuteService extends HQuery {

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqExecute getById(Long id) {
        return $(id).get(WfReqExecute.class);
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqExecute> getByAll(List<Selector> selectorList) {
        return $(selectorList).list(WfReqExecute.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Page<WfReqExecute> getPageList(int start,
                                       int limit, List<Selector> selectorList) {
        return $(selectorList).page(WfReqExecute.class, start, limit);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer getCountUnRead(Long orgId, Long userId) {
        return $($alias("reqId","reqId"),$count("id"), $eq("orgId.id", orgId), $eq("userId.id", userId), $eq("executeRead", 0), $eq("executeState", 0), $eq("reqId.complete", 1),$eq("useYn", "Y")).value(WfReqExecute.class, Integer.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer getCountUnApprove(Long orgId, Long userId) {
        return $($alias("reqId","reqId"),$count("id"), $eq("orgId.id", orgId), $eq("userId.id", userId), $eq("executeRead", 1), $eq("executeState", 0),$eq("reqId.complete", 1), $eq("useYn", "Y")).value(WfReqExecute.class, Integer.class);
    }

    /**
     * 保存对象
     *
     * @param wfReqExecute
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqExecute wfReqExecute) {
        $(wfReqExecute).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<WfReqExecute> wfReqExecuteList) {
        $(wfReqExecuteList).save();
    }


    /**
     * 删除对象
     *
     * @param wfReqExecute
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReqExecute wfReqExecute) {
        $(wfReqExecute).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<WfReqExecute> wfReqExecuteList) {
        $(wfReqExecuteList).delete();
    }

    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfReqExecute.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqExecute> getByReqId(Long reqId) {
        return $($eq("reqId.id", reqId), $eq("useYn", "Y")).list(WfReqExecute.class);
    }

}