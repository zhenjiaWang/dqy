package com.dqy.sys.service;

import com.dqy.sys.entity.SysBudgetTitle;
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
public class SysBudgetTitleService extends HQuery {

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public SysBudgetTitle getById(Long id) {
        return $(id).get(SysBudgetTitle.class);
    }


    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SysBudgetTitle sysBudgetTitle) {
        $(sysBudgetTitle).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(SysBudgetTitle sysBudgetTitle) {
        $(sysBudgetTitle).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(SysBudgetTitle.class);
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<SysBudgetTitle> sysBudgetTitleList) {
        $(sysBudgetTitleList).save();
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<SysBudgetTitle> sysBudgetTitleList) {
        $(sysBudgetTitleList).delete();
    }

}
