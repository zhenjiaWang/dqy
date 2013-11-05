package com.dqy.hr.service;

import com.dqy.hr.entity.HrDepartment;
import com.google.inject.Singleton;
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
public class HrDepartmentService extends HQuery {

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public HrDepartment getById(Long id) {
        return $(id).get(HrDepartment.class);
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public List<HrDepartment> getByList(List<Selector> selectorList) {
        return $(selectorList).list(HrDepartment.class);
    }
    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(HrDepartment hrDepartment) {
        $(hrDepartment).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(HrDepartment hrDepartment) {
        $(hrDepartment).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(HrDepartment.class);
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<HrDepartment> hrDepartmentList) {
        $(hrDepartmentList).save();
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<HrDepartment> hrDepartmentList) {
        $(hrDepartmentList).delete();
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public List<HrDepartment> getDeptListByLevel(Long orgId,Integer level, boolean ignoreUse) {
        if (ignoreUse) {
            return $($eq("orgId.id", orgId), $eq("deptLevel", level), $order("displayOrder")).list(HrDepartment.class);
        } else {
            return $($eq("orgId.id", orgId),$eq("deptLevel", level), $eq("useYn", "Y"), $order("displayOrder")).list(HrDepartment.class);
        }
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<HrDepartment> getDeptListByParentId(Long orgId,Long parentId, boolean ignoreUse) {
        if (ignoreUse) {
            return $($eq("orgId.id", orgId),$eq("parentId.id", parentId), $order("displayOrder")).list(HrDepartment.class);
        } else {
            return $($eq("orgId.id", orgId),$eq("parentId.id", parentId), $eq("useYn", "Y"), $order("displayOrder")).list(HrDepartment.class);
        }
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer getCountByParentId(Long orgId,Long parentId, boolean ignoreUse) {
        if (ignoreUse) {
            return $($eq("orgId.id", orgId),$eq("parentId.id", parentId), $count("id")).value(HrDepartment.class, Integer.class);
        } else {
            return $($eq("orgId.id", orgId),$eq("parentId.id", parentId), $eq("useYn", "Y"), $count("id")).value(HrDepartment.class, Integer.class);
        }
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer getMaxOrderByParentId(Long orgId,Long parentId, boolean ignoreUse) {
        if (ignoreUse) {
            return $($eq("orgId.id", orgId),$eq("parentId.id", parentId), $max("displayOrder")).value(HrDepartment.class, Integer.class);
        } else {
            return $($eq("orgId.id", orgId),$eq("parentId.id", parentId), $eq("useYn", "Y"), $max("displayOrder")).value(HrDepartment.class, Integer.class);
        }
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer getMaxOrderByOrgId(Long orgId,boolean ignoreUse) {
        if (ignoreUse) {
            return $($eq("orgId.id", orgId),  $eq("deptLevel", 1),$max("displayOrder")).value(HrDepartment.class, Integer.class);
        } else {
            return $($eq("orgId.id", orgId), $eq("deptLevel", 1),$eq("useYn", "Y"), $max("displayOrder")).value(HrDepartment.class, Integer.class);
        }
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer validateName(Long orgId,String deptName) {
        return $($count("id"),$eq("orgId.id", orgId), $eq("deptName", deptName)).value(HrDepartment.class, Integer.class);
    }
}
