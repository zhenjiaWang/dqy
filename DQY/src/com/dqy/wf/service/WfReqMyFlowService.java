package com.dqy.wf.service;

import com.dqy.wf.entity.WfReqMyFlow;
import com.dqy.wf.entity.WfReqMyFlowLast;
import com.dqy.wf.entity.WfReqMyFlowNode;
import com.dqy.wf.entity.WfReqMyFlowNodeApprove;
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
public class WfReqMyFlowService extends HQuery {

    @Inject
    private WfReqMyFlowNodeService wfReqMyFlowNodeService;

    @Inject
    private WfReqMyFlowNodeApproveService wfReqMyFlowNodeApproveService;

    @Inject
    private WfReqMyFlowLastService wfReqMyFlowLastService;

    @Transactional(type = TransactionType.READ_ONLY)
    public Page<WfReqMyFlow> getPageList(int start,
                                         int limit, List<Selector> selectorList) {
        return $(selectorList).page(WfReqMyFlow.class, start, limit);
    }

    /**
     * @param id
     * @return 根据Id获取代码
     */
    @Transactional(type = TransactionType.READ_ONLY)
    public WfReqMyFlow getById(Long id) {
        return $(id).get(WfReqMyFlow.class);
    }

    /**
     * 保存对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqMyFlow wfReqMyFlow) {
        $(wfReqMyFlow).save();
    }

    /**
     * 删除对象
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReqMyFlow wfReqMyFlow) {
        $(wfReqMyFlow).delete();
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(List<WfReqMyFlow> reqMyFlowList) {
        $(reqMyFlowList).delete();
    }


    /**
     * 根据id 删除对象
     *
     * @param id
     */
    @Transactional(type = TransactionType.READ_WRITE)
    public void deleteById(Long id) {
        $(id).delete(WfReqMyFlow.class);
    }


    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqMyFlow> getByApplyId(Long orgId,String applyId, Long userId) {
        return $($eq("orgId.id", orgId), $eq("applyId", applyId), $eq("userId.id", userId), $eq("useYn", "Y")).list(WfReqMyFlow.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public List<WfReqMyFlow> getByApplyId(Long orgId,String applyId) {
        return $($eq("orgId.id", orgId), $eq("applyId", applyId),  $eq("useYn", "Y")).list(WfReqMyFlow.class);
    }

    @Transactional(type = TransactionType.READ_ONLY)
    public Integer validateName(Long orgId,String applyId,Long userId, String flowName) {
        return $($count("id"), $eq("orgId.id", orgId), $eq("applyId", applyId), $eq("userId.id", userId), $eq("flowName", flowName)).value(WfReqMyFlow.class, Integer.class);
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void save(WfReqMyFlow wfReqMyFlow, List<WfReqMyFlowNode> delReqMyFlowNodeList, List<WfReqMyFlowNodeApprove> delReqMyFlowNodeApproveList,
                     List<WfReqMyFlowNode> reqMyFlowNodeList, List<WfReqMyFlowNodeApprove> reqMyFlowNodeApproveList) {
        $(wfReqMyFlow).save();
        if (delReqMyFlowNodeList != null && !delReqMyFlowNodeList.isEmpty()) {
            wfReqMyFlowNodeService.delete(delReqMyFlowNodeList);
        }
        if (delReqMyFlowNodeApproveList != null && !delReqMyFlowNodeApproveList.isEmpty()) {
            wfReqMyFlowNodeApproveService.delete(delReqMyFlowNodeApproveList);
        }
        if (reqMyFlowNodeList != null && !reqMyFlowNodeList.isEmpty()) {
            wfReqMyFlowNodeService.save(reqMyFlowNodeList);
        }
        if (reqMyFlowNodeApproveList != null && !reqMyFlowNodeApproveList.isEmpty()) {
            wfReqMyFlowNodeApproveService.save(reqMyFlowNodeApproveList);
        }
    }

    @Transactional(type = TransactionType.READ_WRITE)
    public void delete(WfReqMyFlow wfReqMyFlow, List<WfReqMyFlowNode> delReqMyFlowNodeList, List<WfReqMyFlowNodeApprove> delReqMyFlowNodeApproveList,
                       List<WfReqMyFlowLast> delReqMyFlowLastList) {
        if (delReqMyFlowNodeList != null && !delReqMyFlowNodeList.isEmpty()) {
            wfReqMyFlowNodeService.delete(delReqMyFlowNodeList);
        }
        if (delReqMyFlowNodeApproveList != null && !delReqMyFlowNodeApproveList.isEmpty()) {
            wfReqMyFlowNodeApproveService.delete(delReqMyFlowNodeApproveList);
        }
        if (delReqMyFlowLastList != null && !delReqMyFlowLastList.isEmpty()) {
            wfReqMyFlowLastService.delete(delReqMyFlowLastList);
        }
        if (wfReqMyFlow != null) {
           $(wfReqMyFlow).delete();
        }
    }
}
