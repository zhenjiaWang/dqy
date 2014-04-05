package com.dqy.sale.service;

import com.dqy.sale.entity.SaleCustomer;
import com.dqy.sale.entity.SaleProduct;
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
public class SaleProductService extends HQuery {

    @Transactional(type = TransactionType.READ_ONLY)
    public Page<SaleProduct> getPageList(int start,
                                         int limit, List<Selector> selectorList) {
        return $(selectorList).page(SaleProduct.class, start, limit);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<SaleProduct> getList(Long seriesId) {
        return $($eq("seriesId.id",seriesId),$eq("useYn", "Y")).list(SaleProduct.class);
    }
    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public SaleProduct getById(Long id) {
        return $(id).get(SaleProduct.class);
    }


    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SaleProduct saleProduct) {
        $(saleProduct).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(SaleProduct saleProduct) {
        $(saleProduct).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(SaleProduct.class);
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<SaleProduct> productList) {
        $(productList).save();
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<SaleProduct> productList) {
        $(productList).delete();
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer validateCode(String productCode) {
        return $($count("id"), $eq("productCode", productCode)).value(SaleProduct.class, Integer.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer validateName(String productName) {
        return $($count("id"), $eq("productName", productName)).value(SaleProduct.class, Integer.class);
    }

}
