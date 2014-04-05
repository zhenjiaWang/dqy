package com.dqy.sale.service;

import com.dqy.sale.entity.SaleDept;
import com.dqy.sale.entity.SaleDeptSystem;
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
public class SaleDeptSystemService extends HQuery {

    @Transactional(type = TransactionType.READ_ONLY)
    public Page<SaleDeptSystem> getPageList(int start,
                                         int limit, List<Selector> selectorList) {
        return $(selectorList).page(SaleDeptSystem.class, start, limit);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<SaleDeptSystem> getList(Long deptId) {
        return $($eq("deptId.id",deptId),$eq("useYn", "Y")).list(SaleDeptSystem.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<SaleDeptSystem> getList(List<Selector> selectorList) {
        return $(selectorList).list(SaleDeptSystem.class);
    }
    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public SaleDeptSystem getById(Long id) {
        return $(id).get(SaleDeptSystem.class);
    }


    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SaleDeptSystem saleDeptSystem) {
        $(saleDeptSystem).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(SaleDeptSystem saleDeptSystem) {
        $(saleDeptSystem).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(SaleDeptSystem.class);
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<SaleDeptSystem> deptSystemList) {
        $(deptSystemList).save();
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<SaleDeptSystem> deptSystemList) {
        $(deptSystemList).delete();
    }
}
