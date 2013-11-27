package com.dqy.wf.service;

import com.dqy.wf.entity.WfReqDailyDetail;
import com.dqy.wf.entity.WfReqRePaymentDetail;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.guiceside.persistence.TransactionType;
import org.guiceside.persistence.Transactional;
import org.guiceside.persistence.hibernate.dao.hquery.HQuery;

import java.util.Date;
import java.util.List;

/**
 * @author zhenjiaWang
 * @version 1.0 2012-05
 * @since JDK1.5
 */
@Singleton
public class WfReqDailyDetailService extends HQuery {

    @Inject
    private WfReqService wfReqService;

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqDailyDetail getById(Long id) {
        return $(id).get(WfReqDailyDetail.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqDailyDetail getByReqId(Long reqId) {
        return $($eq("reqId.id", reqId)).get(WfReqDailyDetail.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqDailyDetail wfReqDailyDetail) {
        $(wfReqDailyDetail).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<WfReqDailyDetail> reqDailyDetailList) {
        $(reqDailyDetailList).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReqDailyDetail wfReqDailyDetail) {
        $(wfReqDailyDetail).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<WfReqDailyDetail> reqDailyDetailList) {
        $(reqDailyDetailList).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfReqDailyDetail.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqDailyDetail> getDetailListByDailyId(Long dailyId) {
        return $($eq("dailyId.id", dailyId),$order("id")).list(WfReqDailyDetail.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Double getSumAmountByPass(Long orgId,Long deptId,Date startDate,Date endDate) {
        return $($alias("dailyId","dailyId"),$alias("dailyId.reqId","reqId"),
                $eq("reqId.orgId.id",orgId),$eq("reqId.applyState",2),
                $eq("reqId.applyResult",1), $eq("reqId.complete",1),
                $eq("expenseDept.id", deptId),
                $ge("created",startDate),$le("created",endDate),
                $sum("amount")).value(WfReqDailyDetail.class, Double.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Double getSumAmountByIng(Long orgId,Long deptId,Date startDate,Date endDate) {
        return $($alias("dailyId","dailyId"),$alias("dailyId.reqId","reqId"),
                $eq("reqId.orgId.id",orgId),$eq("reqId.applyState",1),
                $eq("reqId.applyResult",0), $eq("reqId.complete",0),
                $eq("expenseDept.id", deptId),
                $ge("created",startDate),$le("created",endDate),
                $sum("amount")).value(WfReqDailyDetail.class,Double.class);
    }


}
