package com.dqy.sys.service;

import com.dqy.sys.entity.SysBudgetAmount;
import com.dqy.sys.entity.SysBudgetOwen;
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
public class SysBudgetAmountService extends HQuery {

    @Transactional(type = TransactionType.READ_ONLY)
    public List<SysBudgetAmount> getAllList(List<Selector> selectorList) {
        return $(selectorList).list(SysBudgetAmount.class);
    }

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public SysBudgetAmount getById(Long id) {
        return $(id).get(SysBudgetAmount.class);
    }


    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SysBudgetAmount sysBudgetAmount) {
        $(sysBudgetAmount).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(SysBudgetAmount sysBudgetAmount) {
        $(sysBudgetAmount).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(SysBudgetAmount.class);
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<SysBudgetAmount> sysBudgetAmountList) {
        $(sysBudgetAmountList).save();
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<SysBudgetAmount> sysBudgetAmountList) {
        $(sysBudgetAmountList).delete();
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public SysBudgetAmount getAmount(Long orgId,Long deptId,Long titleId,Integer year,Integer month) {
        return $($eq("orgId.id",orgId),$eq("deptId.id",deptId),$eq("titleId.id",titleId),$eq("year",year),$eq("month",month)).get(SysBudgetAmount.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<SysBudgetAmount> getAmountList(Long orgId,Integer year,Long deptId) {
        return $($eq("orgId.id",orgId),$eq("year",year),$eq("deptId.id",deptId)).list(SysBudgetAmount.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Double geTotalAmount(Long orgId,Integer year,Long deptId) {
        return $($eq("orgId.id",orgId),$eq("year",year),$eq("deptId.id",deptId),$sum("amount")).value(SysBudgetAmount.class,Double.class);
    }
}
