package com.dqy.hr.service;

import com.dqy.hr.entity.HrUser;
import com.dqy.hr.entity.HrUserDetail;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.guiceside.commons.Page;
import org.guiceside.persistence.TransactionType;
import org.guiceside.persistence.Transactional;
import org.guiceside.persistence.hibernate.dao.hquery.HQuery;
import org.guiceside.persistence.hibernate.dao.hquery.Selector;

import java.util.Date;
import java.util.List;

/**
 * @author zhenjiaWang
 * @version 1.0 2012-05
 * @since JDK1.5
 */
@Singleton
public class HrUserService extends HQuery {

    @Inject
    private HrUserDetailService hrUserDetailService;


    @Transactional(type = TransactionType.READ_ONLY)
    public Page<HrUser> getPageList(int start,
                                    int limit, List<Selector> selectorList) {
        return $(selectorList).page(HrUser.class, start, limit);
    }
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

    @Transactional(type = TransactionType.READ_WRITE)
    public Integer validateUserAccount(String orgNo, String userNo) {
        return $($alias("orgId", "orgId"), $eq("orgId.orgNo", orgNo), $eq("userNo", userNo),
                $count("id")).value(HrUser.class, Integer.class);
    }
    @Transactional(type = TransactionType.READ_WRITE)
    public Integer validateUser(String orgNo, String userNo, String userPwd) {
        return $($alias("orgId", "orgId"), $eq("orgId.orgNo", orgNo), $eq("userNo", userNo), $eq("userPwd", userPwd),
                $count("id")).value(HrUser.class, Integer.class);
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public Integer getCountUserByEntryDate(Long orgId, Long groupId, Date entryDate) {
        return $($eq("groupId.id", groupId),$eq("orgId.id", orgId), $eq("entryDate", entryDate),
                $count("id")).value(HrUser.class, Integer.class);
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public Integer getCountUserByDeptId(Long orgId, Long groupId, Long deptId) {
        return $($eq("groupId.id", groupId),$eq("orgId.id", orgId), $eq("deptId.id", deptId),
                $eq("useYn","Y"),$eq("userState",0),$count("id")).value(HrUser.class, Integer.class);
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public List<HrUser> getUserListByDeptId(Long orgId, Long groupId, Long deptId) {
        return $($eq("groupId.id", groupId),$eq("orgId.id", orgId), $eq("deptId.id", deptId),
                $eq("useYn","Y"),$eq("userState",0),$order("userName")).list(HrUser.class);
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public HrUser getLoginUser(String orgNo, String userNo, String userPwd) {
        return $($alias("orgId", "orgId"), $eq("orgId.orgNo", orgNo), $eq("userNo", userNo), $eq("userPwd", userPwd)).get(HrUser.class);
    }





    @Transactional(type = TransactionType.READ_WRITE)
    public void save(HrUser hrUser,HrUserDetail hrUserDetail) {
        $(hrUser).save();
        if(hrUserDetail!=null){
            hrUserDetailService.save(hrUserDetail);
        }
    }
}
