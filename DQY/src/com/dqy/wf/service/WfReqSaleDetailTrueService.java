package com.dqy.wf.service;

import com.dqy.wf.entity.WfReqSaleDetail;
import com.dqy.wf.entity.WfReqSaleDetailTrue;
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
public class WfReqSaleDetailTrueService extends HQuery {

    @Inject
    private WfReqService wfReqService;
    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqSaleDetailTrue getById(Long id) {
        return $(id).get(WfReqSaleDetailTrue.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqSaleDetailTrue> getBySaleId(Long saleId) {
        return $($eq("saleId.id",saleId)).list(WfReqSaleDetailTrue.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqSaleDetailTrue wfReqSaleDetailTrue) {
        $(wfReqSaleDetailTrue).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<WfReqSaleDetailTrue> reqSaleDetailTrueList) {
        $(reqSaleDetailTrueList).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReqSaleDetailTrue wfReqSaleDetailTrue) {
        $(wfReqSaleDetailTrue).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<WfReqSaleDetailTrue> reqSaleDetailTrueList) {
        $(reqSaleDetailTrueList).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfReqSaleDetailTrue.class);
    }

}
