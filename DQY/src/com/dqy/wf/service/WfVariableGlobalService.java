package com.dqy.wf.service;

import com.dqy.wf.entity.WfVariableGlobal;
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
public class WfVariableGlobalService extends HQuery {

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public WfVariableGlobal getById(Long id) {
        return $(id).get(WfVariableGlobal.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Page<WfVariableGlobal> getPageList(int start,
                                     int limit, List<Selector> selectorList) {
        return $(selectorList).page(WfVariableGlobal.class, start, limit);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfVariableGlobal> getList(List<Selector> selectorList) {
        return $(selectorList).list(WfVariableGlobal.class);
    }
    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfVariableGlobal wfVariableGlobal) {
        $(wfVariableGlobal).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfVariableGlobal wfVariableGlobal) {
        $(wfVariableGlobal).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfVariableGlobal.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfVariableGlobal> getByOrgId(Long orgId) {
        return $($eq("orgId.id", orgId)).list(WfVariableGlobal.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer validateName(Long orgId, String variableName) {
        return $($count("id"), $eq("orgId.id", orgId), $eq("variableName", variableName)).value(WfVariableGlobal.class, Integer.class);
    }

}
