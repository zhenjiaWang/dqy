package com.dqy.sys.service;

import com.dqy.sys.entity.SysOrgGroup;
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
public class SysOrgGroupService extends HQuery {

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public SysOrgGroup getById(Long id) {
        return $(id).get(SysOrgGroup.class);
    }


    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SysOrgGroup sysOrgGroup) {
        $(sysOrgGroup).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(SysOrgGroup sysOrgGroup) {
        $(sysOrgGroup).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(SysOrgGroup.class);
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<SysOrgGroup> sysOrgGroupList) {
        $(sysOrgGroupList).save();
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<SysOrgGroup> sysOrgGroupList) {
        $(sysOrgGroupList).delete();
    }

}
