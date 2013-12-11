package com.dqy.wf.service;

import com.dqy.wf.entity.WfReqNoSeq;
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
public class WfReqNoSeqService extends HQuery {

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqNoSeq getById(Long id) {
        return $(id).get(WfReqNoSeq.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqNoSeq wfReqNoSeq) {
        $(wfReqNoSeq).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReqNoSeq wfReqNoSeq) {
        $(wfReqNoSeq).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfReqNoSeq.class);
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqNoSeq getCurrentReqNoSeq(Long orgId, String applyId, Integer dateYear,Integer dateMonth) {
        return $($eq("orgId.id", orgId), $eq("applyId", applyId), $eq("dateYear", dateYear),  $eq("dateMonth", dateMonth), $eq("useYn", "Y")).get(WfReqNoSeq.class);
    }
}
