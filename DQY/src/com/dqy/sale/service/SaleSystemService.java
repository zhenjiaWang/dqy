package com.dqy.sale.service;

import com.dqy.sale.entity.SaleDept;
import com.dqy.sale.entity.SaleSystem;
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
public class SaleSystemService extends HQuery {

    @Transactional(type = TransactionType.READ_ONLY)
    public Page<SaleSystem> getPageList(int start,
                                         int limit, List<Selector> selectorList) {
        return $(selectorList).page(SaleSystem.class, start, limit);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<SaleSystem> getList(List<Selector> selectorList) {
        return $(selectorList).list(SaleSystem.class);
    }
    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public SaleSystem getById(Long id) {
        return $(id).get(SaleSystem.class);
    }


    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SaleSystem saleSystem) {
        $(saleSystem).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(SaleSystem saleSystem) {
        $(saleSystem).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(SaleSystem.class);
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<SaleSystem> systemList) {
        $(systemList).save();
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<SaleSystem> systemList) {
        $(systemList).delete();
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer validateName(String systemName) {
        return $($count("id"), $eq("systemName", systemName)).value(SaleSystem.class, Integer.class);
    }


}
