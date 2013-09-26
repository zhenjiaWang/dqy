package com.dqy.hr.service;

import com.dqy.hr.entity.HrUserDetail;
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
public class HrUserDetailService extends HQuery {

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public HrUserDetail getById(Long id) {
        return $(id).get(HrUserDetail.class);
    }


    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(HrUserDetail hrUserDetail) {
        $(hrUserDetail).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(HrUserDetail hrUserDetail) {
        $(hrUserDetail).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(HrUserDetail.class);
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<HrUserDetail> hrUserDetailList) {
        $(hrUserDetailList).save();
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<HrUserDetail> hrUserDetailList) {
        $(hrUserDetailList).delete();
    }

}
