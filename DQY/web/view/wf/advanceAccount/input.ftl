<#import "/view/template/structure/wf/wfCommon.ftl" as wfCommon>
<#import "/view/common/core.ftl" as c>
<@wfCommon.wf_common>
<link href="/css/validator/validator.css" rel="stylesheet"/>
<script type="text/javascript" src="/js/webutils/webutils.validator.js"></script>
<script type="text/javascript" src="/js/webutils/reg.js"></script>
<script type="text/javascript">
    function initValidator() {
        WEBUTILS.validator.init({
            modes: [
                {
                    id: 'wfReq\\.subject',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'}
                    ]
                },
                {
                    id: 'wfReqAdvanceAccount\\.amount',
                    required: true,
                    pattern: [
                        {type: 'number', exp: '==', msg: '不能为空'}
                    ]
                },
                {
                    id: 'wfReqAdvanceAccount\\.purpose',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'}
                    ]
                }
            ]
        }, true);
    }
    function submitApply(){
        document.editForm.submit();
    }
    function clearApproveNode(){
        $('input[type="hidden"]','#nodeFlowHidden').remove();
    }
    function addApproveNode(nodeSeq,approveId,approveType,nodeType,nodeCount,flowId){
        $('#nodeFlowHidden').append('<input type="hidden" id="reqNodeSeq'+nodeSeq+'" name="reqNodeSeq'+nodeSeq+'" value="'+nodeSeq+'">');
        $('#nodeFlowHidden').append('<input type="hidden" id="reqNodeType'+nodeSeq+'" name="reqNodeType'+nodeSeq+'" value="'+nodeType+'">');
        $('#nodeFlowHidden').append('<input type="hidden" id="reqApproveId'+nodeSeq+'" name="reqApproveId'+nodeSeq+'" value="'+approveId+'">');
        $('#nodeFlowHidden').append('<input type="hidden" id="reqApproveType'+nodeSeq+'" name="reqApproveType'+nodeSeq+'" value="'+approveType+'">');
        $('#wfReq\\.nodeCount').val(nodeCount);
        $('#flowId').val(flowId);
    }

    function addFlow(){
        $('.modal-header','#myModal').find('.close').trigger('click');
    }
    $(document).ready(function () {
        initValidator();
        $('#nextBtn').off('click').on('click', function () {
            WEBUTILS.validator.checkAll();
            window.setTimeout(function () {
                var passed = WEBUTILS.validator.isPassed();
                if (passed) {
                    WEBUTILS.popWindow.createPopWindow(700, 535, '选择流程', '/wf/reqMyFlow!myFlowList.dhtml?applyId=${applyId?if_exists}');
                } else {
                    WEBUTILS.validator.showErrors();
                }
            }, 500);
        });
    });
</script>
<!--搜索begin-->
<div class="r-top clearfix">
    <p class="text-info text-center lead"><strong>预支申请单</strong></p>
</div>
<!--搜索over-->
    <#if Session["userSession"]?exists>
        <#assign userInfo=Session["userSession"]?if_exists>
    <div class="mart5">
        <form class="form-horizontal" action="/wf/advanceAccount!save.dhtml" method="POST" name="editForm"
              id="editForm">
            <table class="table application nomar">
                <tbody>
                <tr>
                    <td colspan="2">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReq.subject"
                                   style="width: 60px;color: #898989;font-weight: bold;">申请标题</label>

                            <div class="controls" style="margin-left: 70px;">
                                <input style="width: 95%;" type="text" id="wfReq.subject" name="wfReq.subject"
                                       placeholder="请输入标题" maxlength="20">
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label"
                                   style="width: 60px;color: #898989;font-weight: bold;">申请人</label>

                            <div class="controls" style="margin-left: 70px;">
                                <label style="margin-top: 5px;padding-left:5px;font-size: 14px;">${userInfo.userName?if_exists}</label>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label"
                                   style="width: 60px;color: #898989;font-weight: bold;">申请时间</label>

                            <div class="controls" style="margin-left: 70px;">
                                <label style="margin-top: 5px;padding-left:5px;font-size: 14px;">2009-12-12 12:12:00</label>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqAdvanceAccount.amount"
                                   style="width: 60px;color: #898989;font-weight: bold;">预支金额</label>

                            <div class="controls" style="margin-left: 70px;">
                                <input type="text" id="wfReqAdvanceAccount.amount" name="wfReqAdvanceAccount.amount" placeholder="预支金额" maxlength="10">
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqAdvanceAccount.payMethod"
                                   style="width: 60px;color: #898989;font-weight: bold;">支付方式</label>

                            <div class="controls" style="margin-left: 70px;" >
                                <select class="int2 width-160" id="wfReqAdvanceAccount.payMethod" name="wfReqAdvanceAccount.payMethod">
                                    <option value="1">现金</option>
                                    <option value="2">银行转账</option>
                                    <option value="3">支票</option>
                                </select>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqAdvanceAccount.purpose"
                                   style="width: 60px;color: #898989;font-weight: bold;">用途</label>

                            <div class="controls" style="margin-left: 70px;">
                                <textarea rows="2" style="width: 95%;" class=" font12" id="wfReqAdvanceAccount.purpose" name="wfReqAdvanceAccount.purpose" maxlength="100"></textarea>
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqAdvanceAccount.remarks"
                                   style="width: 60px;color: #898989;font-weight: bold;">备注</label>

                            <div class="controls" style="margin-left: 70px;">
                                <textarea rows="4" style="width: 95%;" class="font12" id="wfReqAdvanceAccount.remarks" name="wfReqAdvanceAccount.remarks" maxlength="400"></textarea>
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
            <p class="mart10  clearfix" style="width: 725px;">
                <button class="btn btn-success floatright " type="button" id="nextBtn">继续</button>
            </p>
            <input type="hidden" name="wfReq.applyId" id="wfReq.applyId" value="${applyId?if_exists}">
            <input type="hidden" name="wfReq.id" id="wfReq.id">
            <input type="hidden" name="flowId" id="flowId">
            <input type="hidden" name="wfReq.nodeCount" id="wfReq.nodeCount" value="0">
            <div id="nodeFlowHidden"></div>
        </form>
    </div>

    </#if>
</@wfCommon.wf_common>