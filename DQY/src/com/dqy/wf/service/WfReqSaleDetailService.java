package com.dqy.wf.service;

import com.dqy.wf.entity.*;
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
public class WfReqSaleDetailService extends HQuery {

    @Inject
    private WfReqService wfReqService;
    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqSaleDetail getById(Long id) {
        return $(id).get(WfReqSaleDetail.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqSaleDetail> getBySaleId(Long saleId) {
        return $($eq("saleId.id",saleId)).list(WfReqSaleDetail.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqSaleDetail wfReqSaleDetail) {
        $(wfReqSaleDetail).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<WfReqSaleDetail> reqSaleDetailList) {
        $(reqSaleDetailList).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReqSaleDetail wfReqSaleDetail) {
        $(wfReqSaleDetail).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<WfReqSaleDetail> reqSaleDetailList) {
        $(reqSaleDetailList).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfReqSaleDetail.class);
    }

}
