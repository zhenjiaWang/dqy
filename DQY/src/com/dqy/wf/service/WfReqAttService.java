package com.dqy.wf.service;

import com.dqy.wf.entity.WfReqAtt;
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
public class WfReqAttService extends HQuery {

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqAtt getById(Long id) {
        return $(id).get(WfReqAtt.class);
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqAtt> getByAll(List<Selector> selectorList) {
        return $(selectorList).list(WfReqAtt.class);
    }

    /**
     * 保存对象
     *
     * @param wfReqAtt
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqAtt wfReqAtt) {
        $(wfReqAtt).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<WfReqAtt> wfReqAttList) {
        $(wfReqAttList).save();
    }


    /**
     * 删除对象
     *
     * @param wfReqAtt
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReqAtt wfReqAtt) {
        $(wfReqAtt).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<WfReqAtt> reqAttList) {
        $(reqAttList).delete();
    }

    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfReqAtt.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqAtt> getByReqId(Long reqId) {
        return $($eq("reqId.id", reqId), $eq("useYn", "Y")).list(WfReqAtt.class);
    }

}