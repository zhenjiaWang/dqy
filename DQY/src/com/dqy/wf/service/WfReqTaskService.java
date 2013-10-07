package com.dqy.wf.service;

import com.dqy.wf.entity.WfReq;
import com.dqy.wf.entity.WfReqComments;
import com.dqy.wf.entity.WfReqNodeApprove;
import com.dqy.wf.entity.WfReqTask;
import com.google.inject.Inject;
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
public class WfReqTaskService extends HQuery {

    @Inject
    private WfReqCommentsService wfReqCommentsService;

    @Inject
    private WfReqNodeApproveService wfReqNodeApproveService;

    @Inject
    private WfReqService wfReqService;

    @Transactional(type = TransactionType.READ_ONLY)
    public Page<WfReqTask> getPageList(int start,
                                       int limit, List<Selector> selectorList) {
        return $(selectorList).page(WfReqTask.class, start, limit);
    }

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqTask getById(Long id) {
        return $(id).get(WfReqTask.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqTask wfReqTask) {
        $(wfReqTask).save();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(List<WfReqTask> reqTaskList) {
        $(reqTaskList).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReqTask wfReqTask) {
        $(wfReqTask).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<WfReqTask> reqTaskList) {
        $(reqTaskList).delete();
    }

    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfReqTask.class);
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqTask> getTaskListByUser(Long orgId, Long userId, Integer taskState) {
        return $($eq("orgId.id", orgId), $eq("userId.id", userId), $eq("taskState", taskState), $eq("useYn", "Y"), $order("receiveDate")).list(WfReqTask.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer getCountUnRead(Long orgId, Long userId) {
        return $($count("id"), $eq("orgId.id", orgId), $eq("userId.id", userId), $eq("taskRead", 0), $eq("taskState", 0), $eq("useYn", "Y")).value(WfReqTask.class, Integer.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer getCountUnApprove(Long orgId, Long userId) {
        return $($count("id"), $eq("orgId.id", orgId), $eq("userId.id", userId), $eq("taskRead", 1), $eq("taskState", 0), $eq("useYn", "Y")).value(WfReqTask.class, Integer.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqTask> getTaskListByReqId(Long orgId, Long reqId) {
        return $($eq("orgId.id", orgId), $eq("reqId.id", reqId), $eq("useYn", "Y")).list(WfReqTask.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqTask getTaskByReqIdNode(Long orgId, Long reqId, Integer nodeSeq, Long nodeId) {
        return $($eq("orgId.id", orgId), $eq("reqId.id", reqId), $eq("nodeId.id", nodeId), $eq("nodeSeq", nodeSeq), $eq("useYn", "Y")).get(WfReqTask.class);
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public Integer getCountUnApproveByNodeSeq(Long orgId, Long reqId, Integer nodeSeq) {
        return $($count("id"), $eq("orgId.id", orgId), $eq("reqId.id", reqId), $eq("nodeSeq", nodeSeq),
                $eq("taskState", 0), $eq("useYn", "Y")).value(WfReqTask.class, Integer.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqTask> getUnApproveListByNodeSeq(Long orgId,Long userId, Long reqId, Integer nodeSeq) {
        return $($eq("orgId.id", orgId), $eq("reqId.id", reqId), $not($eq("userId.id",userId)),$eq("nodeSeq", nodeSeq),
                $eq("taskState", 0), $eq("useYn", "Y")).list(WfReqTask.class);
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqTask wfReqTask, WfReqComments wfReqComments) {
        $(wfReqTask).save();
        wfReqCommentsService.save(wfReqComments);
    }


    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqTask wfReqTask, WfReq wfReq, List<WfReqComments> reqCommentsList, List<WfReqTask> reqTaskList) {
        $(wfReqTask).save();
        wfReqService.save(wfReq);
        if (reqCommentsList != null && !reqCommentsList.isEmpty()) {
            wfReqCommentsService.save(reqCommentsList);
        }

        if (reqTaskList != null && !reqTaskList.isEmpty()) {
            this.save(reqTaskList);
        }
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void saveDone(WfReqTask wfReqTask, WfReq wfReq, List<WfReqComments> reqCommentsList) {
        $(wfReqTask).save();
        wfReqService.save(wfReq);
        if (reqCommentsList != null && !reqCommentsList.isEmpty()) {
            wfReqCommentsService.save(reqCommentsList);
        }
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqTask wfReqTask, List<WfReqNodeApprove> gtNodeApproveList, List<WfReqNodeApprove> nodeApproveList, WfReq wfReq, List<WfReqComments> reqCommentsList, WfReqTask forwardReqTask) {
        $(wfReqTask).save();
        if (gtNodeApproveList != null && !gtNodeApproveList.isEmpty()) {
            this.wfReqNodeApproveService.save(gtNodeApproveList);
        }
        if (nodeApproveList != null && !nodeApproveList.isEmpty()) {
            this.wfReqNodeApproveService.save(nodeApproveList);
        }
        wfReqService.save(wfReq);
        if (reqCommentsList != null && !reqCommentsList.isEmpty()) {
            wfReqCommentsService.save(reqCommentsList);
        }
        $(forwardReqTask).save();
    }
}
