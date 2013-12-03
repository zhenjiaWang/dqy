package com.dqy.sys.service;

import com.dqy.sys.entity.SysBudgetOwen;
import com.google.inject.Singleton;
import org.guiceside.commons.Page;
import org.guiceside.persistence.TransactionType;
import org.guiceside.persistence.Transactional;
import org.guiceside.persistence.hibernate.dao.enums.Match;
import org.guiceside.persistence.hibernate.dao.hquery.HQuery;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;

import java.util.List;

/**
 * @author zhenjiaWang
 * @version 1.0 2012-05
 * @since JDK1.5
 */
@Singleton
public class SysBudgetOwenService extends HQuery {

    @Transactional(type = TransactionType.READ_ONLY)
    public List<SysBudgetOwen> getAllList(List<Selector> selectorList) {
        return $(selectorList).list(SysBudgetOwen.class);
    }

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

    @Transactional(type = TransactionType.READ_ONLY)
    public List<Long> getBudgetIdList(Long orgId) {
        return $($eq("orgId.id",orgId),$distinct("budgetTitle.id")).list(SysBudgetOwen.class,Long.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<Long> getBudgetIdList(Long orgId,String titleNo) {
        return $($alias("titleId","titleId"),$eq("orgId.id",orgId),
                $like("titleId.titleNo",titleNo, Match.END),
                $distinct("budgetTitle.id")).list(SysBudgetOwen.class, Long.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Page<SysBudgetOwen> getPageList(int start,
                                            int limit, List<Selector> selectorList) {
        return $(selectorList).page(SysBudgetOwen.class, start, limit);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public SysBudgetOwen getByBudgetTitleId(Long budgetTitleId) {
        return $($eq("budgetTitle.id",budgetTitleId)).get(SysBudgetOwen.class);
    }

}
