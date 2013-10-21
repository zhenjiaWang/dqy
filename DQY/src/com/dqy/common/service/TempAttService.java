package com.dqy.common.service;

import com.dqy.common.entity.TempAtt;
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
public class TempAttService extends HQuery {

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public TempAtt getById(Long id) {
        return $(id).get(TempAtt.class);
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public List<TempAtt> getByAll(List<Selector> selectorList) {
        return $(selectorList).list(TempAtt.class);
    }

    /**
     * 保存对象
     *
     * @param tempAtt
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(TempAtt tempAtt) {
        $(tempAtt).save();
    }

    /**
     * 删除对象
     *
     * @param tempAtt
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(TempAtt tempAtt) {
        $(tempAtt).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<TempAtt> tempAttList) {
        $(tempAttList).delete();
    }

    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(TempAtt.class);
    }



    @Transactional(type = TransactionType.READ_ONLY)
    public List<TempAtt> getByFileName(String attKey, String tokenId, String oldName, String postfix) {
        return $($eq("attKey", attKey), $eq("tokenId", tokenId), $eq("oldName", oldName), $eq("postfix", postfix), $eq("useYn", "Y")).list(TempAtt.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<TempAtt> getListByTokenId(String attKey, String tokenId,Long orgId,Long userId) {
        return $($eq("orgId.id",orgId),$eq("userId.id",userId),
                $eq("attKey", attKey), $eq("tokenId", tokenId), $eq("useYn", "Y")).list(TempAtt.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<TempAtt> getListByNotTokenId(String attKey, String tokenId,Long orgId,Long userId) {
        return $($eq("orgId.id",orgId),$eq("userId.id",userId),
                $eq("attKey", attKey), $not($eq("tokenId", tokenId)), $eq("useYn", "Y")).list(TempAtt.class);
    }
}