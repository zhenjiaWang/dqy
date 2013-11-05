package com.dqy.wf.service;

import com.dqy.wf.entity.WfReqDailyDetail;
import com.dqy.wf.entity.WfReqDailyTrue;
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
public class WfReqDailyTrueService extends HQuery {

    @Inject
    private WfReqService wfReqService;

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqDailyTrue getById(Long id) {
        return $(id).get(WfReqDailyTrue.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqDailyTrue getByReqId(Long reqId) {
        return $($eq("reqId.id", reqId)).get(WfReqDailyTrue.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqDailyTrue wfReqDailyTrue) {
        $(wfReqDailyTrue).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<WfReqDailyTrue> reqDailyTrueList) {
        $(reqDailyTrueList).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReqDailyTrue wfReqDailyTrue) {
        $(wfReqDailyTrue).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<WfReqDailyTrue> reqDailyTrueList) {
        $(reqDailyTrueList).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfReqDailyTrue.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqDailyTrue> getDetailListByDailyId(Long dailyId) {
        return $($eq("dailyId.id", dailyId),$order("id")).list(WfReqDailyTrue.class);
    }

}
