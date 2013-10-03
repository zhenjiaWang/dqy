package com.dqy.sys.service;

import com.dqy.sys.entity.SysAdmin;
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
public class SysAdminService extends HQuery {

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public SysAdmin getById(Long id) {
        return $(id).get(SysAdmin.class);
    }


    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SysAdmin sysAdmin) {
        $(sysAdmin).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(SysAdmin sysAdmin) {
        $(sysAdmin).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(SysAdmin.class);
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<SysAdmin> sysAdminList) {
        $(sysAdminList).save();
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<SysAdmin> sysAdminList) {
        $(sysAdminList).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public Integer validateAdmin(Long orgId,Long userId) {
        return $($eq("orgId.id", orgId), $eq("userId.id", userId),
                $count("id")).value(SysAdmin.class, Integer.class);
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public SysAdmin getAdminByOrgIdUserId(Long orgId,Long userId) {
        return $($eq("orgId.id", orgId), $eq("userId.id", userId),$eq("useYn","Y")).get(SysAdmin.class);
    }

}
