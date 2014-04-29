package com.dqy.sale.service;

import com.dqy.sale.entity.SaleCustomer;
import com.dqy.sale.entity.SaleDept;
import com.dqy.sale.entity.SaleDeptSystem;
import com.google.inject.Inject;
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
public class SaleDeptService extends HQuery {

    @Inject
    private SaleDeptSystemService saleDeptSystemService;

    @Transactional(type = TransactionType.READ_ONLY)
    public Page<SaleDept> getPageList(int start,
                                      int limit, List<Selector> selectorList) {
        return $(selectorList).page(SaleDept.class, start, limit);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<SaleDept> getList(Long channelId) {
        return $($eq("channelId.id", channelId), $eq("useYn", "Y")).list(SaleDept.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<SaleDept> getList(List<Selector> selectorList) {
        return $(selectorList).list(SaleDept.class);
    }

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public SaleDept getById(Long id) {
        return $(id).get(SaleDept.class);
    }


    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SaleDept saleDept) {
        $(saleDept).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SaleDept saleDept, List<SaleDeptSystem> saleDeptSystemList, List<SaleDeptSystem> delSaleDeptSystemList) {
        $(saleDept).save();
        if (delSaleDeptSystemList != null && !delSaleDeptSystemList.isEmpty()) {
            this.saleDeptSystemService.delete(delSaleDeptSystemList);
        }
        if (saleDeptSystemList != null && !saleDeptSystemList.isEmpty()) {
            this.saleDeptSystemService.save(saleDeptSystemList);
        }
    }


    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(SaleDept saleDept) {
        $(saleDept).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(SaleDept.class);
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<SaleDept> deptList) {
        $(deptList).save();
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<SaleDept> deptList) {
        $(deptList).delete();
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer validateName(String deptName) {
        return $($count("id"), $eq("deptName", deptName)).value(SaleDept.class, Integer.class);
    }


}
