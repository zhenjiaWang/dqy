package com.dqy.sys.service;

import com.dqy.sys.entity.SysBudgetType;
import com.google.inject.Singleton;
import org.guiceside.commons.Page;
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
public class SysBudgetTypeService extends HQuery {


    @Transactional(type = TransactionType.READ_ONLY)
    public Page<SysBudgetType> getPageList(int start,
                                         int limit, List<Selector> selectorList) {
        return $(selectorList).page(SysBudgetType.class, start, limit);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<SysBudgetType> getAllList(List<Selector> selectorList) {
        return $(selectorList).list(SysBudgetType.class);
    }

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public SysBudgetType getById(Long id) {
        return $(id).get(SysBudgetType.class);
    }


    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SysBudgetType sysBudgetType) {
        $(sysBudgetType).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(SysBudgetType sysBudgetType) {
        $(sysBudgetType).delete();
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer validateName(Long orgId,String expenseType) {
        return $($count("id"),$eq("orgId.id",orgId), $eq("expenseType", expenseType)).value(SysBudgetType.class, Integer.class);
    }

    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(SysBudgetType.class);
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<SysBudgetType> sysBudgetTypeList) {
        $(sysBudgetTypeList).save();
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<SysBudgetType> sysBudgetTypeList) {
        $(sysBudgetTypeList).delete();
    }

}
