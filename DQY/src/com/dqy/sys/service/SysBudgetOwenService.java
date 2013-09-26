package com.dqy.sys.service;

import com.dqy.sys.entity.SysBudgetOwen;
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
public class SysBudgetOwenService extends HQuery {

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public SysBudgetOwen getById(Long id) {
        return $(id).get(SysBudgetOwen.class);
    }


    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SysBudgetOwen sysBudgetOwen) {
        $(sysBudgetOwen).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(SysBudgetOwen sysBudgetOwen) {
        $(sysBudgetOwen).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(SysBudgetOwen.class);
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<SysBudgetOwen> sysBudgetOwenList) {
        $(sysBudgetOwenList).save();
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<SysBudgetOwen> sysBudgetOwenList) {
        $(sysBudgetOwenList).delete();
    }

}
