package com.dqy.sys.service;

import com.dqy.sys.entity.SysFinancialTitle;
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
public class SysFinancialTitleService extends HQuery {

    @Transactional(type = TransactionType.READ_ONLY)
    public Page<SysFinancialTitle> getPageList(int start,
                                               int limit, List<Selector> selectorList) {
        return $(selectorList).page(SysFinancialTitle.class, start, limit);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<SysFinancialTitle> getAllList(List<Selector> selectorList) {
        return $(selectorList).list(SysFinancialTitle.class);
    }

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public SysFinancialTitle getById(Long id) {
        return $(id).get(SysFinancialTitle.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public SysFinancialTitle getByNo(Long orgId,String titleNo) {
        return $($eq("orgId.id",orgId),$eq("titleNo",titleNo)).get(SysFinancialTitle.class);
    }


    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(SysFinancialTitle sysFinancialTitle) {
        $(sysFinancialTitle).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(SysFinancialTitle sysFinancialTitle) {
        $(sysFinancialTitle).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(SysFinancialTitle.class);
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<SysFinancialTitle> sysFinancialTitleList) {
        $(sysFinancialTitleList).save();
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<SysFinancialTitle> sysFinancialTitleList) {
        $(sysFinancialTitleList).delete();
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer validateName(Long orgId, Long parentId, String titleName) {
        if (parentId == null) {
            return $($count("id"), $eq("orgId.id", orgId), $eq("titleLevel", 1), $eq("titleName", titleName)).value(SysFinancialTitle.class, Integer.class);
        } else {
            return $($count("id"), $eq("orgId.id", orgId), $eq("parentId.id", parentId), $eq("titleName", titleName)).value(SysFinancialTitle.class, Integer.class);
        }
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer validateNo(Long orgId, String titleNo) {
        return $($count("id"), $eq("orgId.id", orgId), $eq("titleNo", titleNo)).value(SysFinancialTitle.class, Integer.class);
    }




    @Transactional(type = TransactionType.READ_ONLY)
    public Integer getMaxOrderByOrgId(Long orgId,boolean ignoreUse) {
        if (ignoreUse) {
            return $($eq("orgId.id", orgId),  $eq("titleLevel", 1),$max("displayOrder")).value(SysFinancialTitle.class, Integer.class);
        } else {
            return $($eq("orgId.id", orgId), $eq("titleLevel", 1),$eq("useYn", "Y"), $max("displayOrder")).value(SysFinancialTitle.class, Integer.class);
        }
    }
    @Transactional(type = TransactionType.READ_ONLY)
    public Integer getMaxOrderByParentId(Long orgId,Long parentId, boolean ignoreUse) {
        if (ignoreUse) {
            return $($eq("orgId.id", orgId),$eq("parentId.id", parentId), $max("displayOrder")).value(SysFinancialTitle.class, Integer.class);
        } else {
            return $($eq("orgId.id", orgId),$eq("parentId.id", parentId), $eq("useYn", "Y"), $max("displayOrder")).value(SysFinancialTitle.class, Integer.class);
        }
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public List<SysFinancialTitle> getTitleListByLevel(Long orgId,Integer level, boolean ignoreUse) {
        if (ignoreUse) {
            return $($eq("orgId.id", orgId), $eq("titleLevel", level), $order("displayOrder")).list(SysFinancialTitle.class);
        } else {
            return $($eq("orgId.id", orgId),$eq("titleLevel", level), $eq("useYn", "Y"), $order("displayOrder")).list(SysFinancialTitle.class);
        }
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<SysFinancialTitle> getTitleListByParentId(Long orgId,Long parentId, boolean ignoreUse) {
        if (ignoreUse) {
            return $($eq("orgId.id", orgId),$eq("parentId.id", parentId), $order("displayOrder")).list(SysFinancialTitle.class);
        } else {
            return $($eq("orgId.id", orgId),$eq("parentId.id", parentId), $eq("useYn", "Y"), $order("displayOrder")).list(SysFinancialTitle.class);
        }
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public Integer getCountByParentId(Long orgId,Long parentId, boolean ignoreUse) {
        if (ignoreUse) {
            return $($eq("orgId.id", orgId),$eq("parentId.id", parentId), $count("id")).value(SysFinancialTitle.class, Integer.class);
        } else {
            return $($eq("orgId.id", orgId),$eq("parentId.id", parentId), $eq("useYn", "Y"), $count("id")).value(SysFinancialTitle.class, Integer.class);
        }
    }

}
