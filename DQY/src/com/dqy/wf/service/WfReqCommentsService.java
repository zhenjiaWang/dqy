package com.dqy.wf.service;

import com.dqy.wf.entity.WfReqComments;
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
public class WfReqCommentsService extends HQuery {

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqComments getById(Long id) {
        return $(id).get(WfReqComments.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqComments wfReqComments) {
        $(wfReqComments).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<WfReqComments> reqCommentsList) {
        $(reqCommentsList).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReqComments wfReqComments) {
        $(wfReqComments).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<WfReqComments> reqCommentsList) {
        $(reqCommentsList).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfReqComments.class);
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqComments> getCommentsListByReqId(Long reqId) {
        return $($eq("reqId.id", reqId), $eq("useYn", "Y"), $order("id")).list(WfReqComments.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqComments> getCommentsListByReqIdApprove(Long reqId,Integer approve) {
        return $($eq("reqId.id", reqId),$eq("approve", approve), $eq("useYn", "Y"), $order("id")).list(WfReqComments.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqComments getCommentsByReqIdAction(Long reqId, Integer action) {
        return $($eq("reqId.id", reqId), $eq("action", action), $eq("useYn", "Y")).get(WfReqComments.class);
    }

}
