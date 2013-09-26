package com.dqy.sys.service;

import com.dqy.sys.entity.SysAuthorized;
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
public class SysAuthorizedService extends HQuery {

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public SysAuthorized getById(Long id) {
        return $(id).get(SysAuthorized.class);
    }


    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SysAuthorized sysAuthorized) {
        $(sysAuthorized).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(SysAuthorized sysAuthorized) {
        $(sysAuthorized).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(SysAuthorized.class);
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<SysAuthorized> sysAuthorizedList) {
        $(sysAuthorizedList).save();
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<SysAuthorized> sysAuthorizedList) {
        $(sysAuthorizedList).delete();
    }

}
