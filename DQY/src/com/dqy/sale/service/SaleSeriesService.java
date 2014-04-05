package com.dqy.sale.service;

import com.dqy.sale.entity.SaleSeries;
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
public class SaleSeriesService extends HQuery {

    @Transactional(type = TransactionType.READ_ONLY)
    public Page<SaleSeries> getPageList(int start,
                                         int limit, List<Selector> selectorList) {
        return $(selectorList).page(SaleSeries.class, start, limit);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<SaleSeries> getList(List<Selector> selectorList) {
        return $(selectorList).list(SaleSeries.class);
    }
    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public SaleSeries getById(Long id) {
        return $(id).get(SaleSeries.class);
    }


    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SaleSeries saleSeries) {
        $(saleSeries).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(SaleSeries saleSeries) {
        $(saleSeries).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(SaleSeries.class);
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<SaleSeries> seriesList) {
        $(seriesList).save();
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<SaleSeries> seriesList) {
        $(seriesList).delete();
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer validateName(String seriesName) {
        return $($count("id"), $eq("seriesName", seriesName)).value(SaleSeries.class, Integer.class);
    }


}
