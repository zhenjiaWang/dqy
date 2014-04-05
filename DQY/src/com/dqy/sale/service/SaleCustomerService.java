package com.dqy.sale.service;

import com.dqy.sale.entity.SaleChannel;
import com.dqy.sale.entity.SaleCustomer;
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
public class SaleCustomerService extends HQuery {

    @Transactional(type = TransactionType.READ_ONLY)
    public Page<SaleCustomer> getPageList(int start,
                                         int limit, List<Selector> selectorList) {
        return $(selectorList).page(SaleCustomer.class, start, limit);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<SaleCustomer> getList(Long systemId) {
        return $($eq("systemId.id",systemId),$eq("useYn","Y")).list(SaleCustomer.class);
    }
    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public SaleCustomer getById(Long id) {
        return $(id).get(SaleCustomer.class);
    }


    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SaleCustomer saleCustomer) {
        $(saleCustomer).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(SaleCustomer saleCustomer) {
        $(saleCustomer).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(SaleCustomer.class);
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<SaleCustomer> customerList) {
        $(customerList).save();
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<SaleCustomer> customerList) {
        $(customerList).delete();
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer validateName(String customerName) {
        return $($count("id"), $eq("customerName", customerName)).value(SaleCustomer.class, Integer.class);
    }


}
