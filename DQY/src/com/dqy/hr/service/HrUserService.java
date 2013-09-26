package com.dqy.hr.service;

import com.dqy.hr.entity.HrUser;
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
public class HrUserService extends HQuery {

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public HrUser getById(Long id) {
        return $(id).get(HrUser.class);
    }


    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(HrUser hrUser) {
        $(hrUser).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(HrUser hrUser) {
        $(hrUser).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(HrUser.class);
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<HrUser> hrUserList) {
        $(hrUserList).save();
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<HrUser> hrUserList) {
        $(hrUserList).delete();
    }

}
