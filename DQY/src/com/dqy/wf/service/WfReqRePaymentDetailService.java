package com.dqy.wf.service;

import com.dqy.wf.entity.*;
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
public class WfReqRePaymentDetailService extends HQuery {

    @Inject
    private WfReqService wfReqService;

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqRePaymentDetail getById(Long id) {
        return $(id).get(WfReqRePaymentDetail.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqRePaymentDetail getByReqId(Long reqId) {
        return $($eq("reqId.id", reqId)).get(WfReqRePaymentDetail.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqRePaymentDetail wfReqRePaymentDetail) {
        $(wfReqRePaymentDetail).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<WfReqRePaymentDetail> reqRePaymentDetails) {
        $(reqRePaymentDetails).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReqRePaymentDetail wfReqRePaymentDetail) {
        $(wfReqRePaymentDetail).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<WfReqRePaymentDetail> reqRePaymentDetails) {
        $(reqRePaymentDetails).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfReqRePaymentDetail.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqRePaymentDetail> getDetailListByRePaymentId(Long rePaymentId) {
        return $($eq("rePaymentId.id", rePaymentId),$order("id")).list(WfReqRePaymentDetail.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Double getSumAmountByPass(Long orgId,Long deptId,Date startDate,Date endDate) {
        return $($alias("rePaymentId","dailyId"),$alias("rePaymentId.reqId","reqId"),
                $eq("reqId.orgId.id",orgId),$eq("reqId.applyState",2),
                $eq("reqId.applyResult",1), $eq("reqId.complete",1),
                $eq("expenseDept.id", deptId),
                $ge("created",startDate),$le("created",endDate),
                $sum("amount")).value(WfReqRePaymentDetail.class,Double.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Double getSumAmountByPass(Long orgId,Long deptId,Long expenseType,List<Long> titleIds, Date startDate,Date endDate) {
        return $($alias("rePaymentId","dailyId"),$alias("rePaymentId.reqId","reqId"),
                $eq("reqId.orgId.id",orgId),$eq("reqId.applyState",2),
                $eq("reqId.applyResult",1), $eq("reqId.complete",1),
                $eq("expenseDept.id", deptId),
                $eq("expenseType.id", expenseType),
                $in("expenseTitle.id", titleIds),
                $ge("created",startDate),$le("created",endDate),
                $sum("amount")).value(WfReqRePaymentDetail.class, Double.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqRePaymentDetail> getListSumAmountByPass(Long orgId,Long deptId,Long expenseType,List<Long> titleIds, Date startDate,Date endDate) {
        return $($alias("rePaymentId","dailyId"),$alias("rePaymentId.reqId","reqId"),
                $eq("reqId.orgId.id",orgId),$eq("reqId.applyState",2),
                $eq("reqId.applyResult",1), $eq("reqId.complete",1),
                $eq("expenseDept.id", deptId),
                $eq("expenseType.id", expenseType),
                $in("expenseTitle.id", titleIds),
                $ge("created",startDate),$le("created",endDate)).list(WfReqRePaymentDetail.class);
    }
    @Transactional(type = TransactionType.READ_ONLY)
    public Double getSumAmountByIng(Long orgId,Long deptId,Date startDate,Date endDate) {
        return $($alias("rePaymentId","rePaymentId"),$alias("rePaymentId.reqId","reqId"),
                $eq("reqId.orgId.id",orgId),$eq("reqId.applyState",1),
                $eq("reqId.applyResult",0), $eq("reqId.complete",0),
                $eq("expenseDept.id", deptId),
                $ge("created",startDate),$le("created",endDate),
                $sum("amount")).value(WfReqRePaymentDetail.class,Double.class);
    }
}
