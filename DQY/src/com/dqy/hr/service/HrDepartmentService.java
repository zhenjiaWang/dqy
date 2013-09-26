package com.dqy.hr.service;

import com.dqy.hr.entity.HrDepartment;
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
public class HrDepartmentService extends HQuery {

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public HrDepartment getById(Long id) {
        return $(id).get(HrDepartment.class);
    }


    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(HrDepartment hrDepartment) {
        $(hrDepartment).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(HrDepartment hrDepartment) {
        $(hrDepartment).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(HrDepartment.class);
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<HrDepartment> hrDepartmentList) {
        $(hrDepartmentList).save();
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<HrDepartment> hrDepartmentList) {
        $(hrDepartmentList).delete();
    }

}
