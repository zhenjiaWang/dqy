<#import "/view/template/structure/wf/wfCommon.ftl" as wfCommon>
<#import "/view/common/core.ftl" as c>
<@wfCommon.wf_common>
<link href="/css/validator/validator.css" rel="stylesheet"/>
<script type="text/javascript" src="/js/webutils/webutils.validator.js"></script>
<script type="text/javascript" src="/js/webutils/reg.js"></script>
<script type="text/javascript">
    var submited = false;
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
                    id: 'wfReqRePayment\\.amount',
                    required: true,
                    pattern: [
                        {type: 'number', exp: '==', msg: '不能为空'}
                    ]
                },
                {
                    id: 'amount1',
                    required: true,
                    pattern: [
                        {type: 'number', exp: '==', msg: '`'}
                    ]
                }
            ]
        }, true);
    }
    function submitApply() {
        document.editForm.submit();
    }
    function clearApproveNode() {
        $('input[type="hidden"]', '#nodeFlowHidden').remove();
    }
    function addApproveNode(nodeSeq, approveId, approveType, nodeType, nodeCount, flowId) {
        $('#nodeFlowHidden').append('<input type="hidden" id="reqNodeSeq' + nodeSeq + '" name="reqNodeSeq' + nodeSeq + '" value="' + nodeSeq + '">');
        $('#nodeFlowHidden').append('<input type="hidden" id="reqNodeType' + nodeSeq + '" name="reqNodeType' + nodeSeq + '" value="' + nodeType + '">');
        $('#nodeFlowHidden').append('<input type="hidden" id="reqApproveId' + nodeSeq + '" name="reqApproveId' + nodeSeq + '" value="' + approveId + '">');
        $('#nodeFlowHidden').append('<input type="hidden" id="reqApproveType' + nodeSeq + '" name="reqApproveType' + nodeSeq + '" value="' + approveType + '">');
        $('#wfReq\\.nodeCount').val(nodeCount);
        $('#flowId').val(flowId);
    }

    function addFlow() {
        $('.modal-header', '#myModal').find('.close').trigger('click');
    }

    function getBudgetTitleList(seq){
        var typeObj=$('#typeId'+seq);
        var titleObj=$('#titleId'+seq);
        var typeId=$(typeObj).val();
        $.ajax({
            type:'GET',
            url:'/sys/budgetTitle!getTitleList.dhtml?typeId='+typeId,
            dataType:'json',
            success:function (jsonData) {
                if (jsonData) {
                    if (jsonData['result'] == '0') {
                        var titleList=jsonData['titleList'];
                        $(titleObj).empty();
                        if(titleList){
                            $(titleList).each(function(i,o){
                                $(titleObj).append('<option value="'+o['id']+'">'+o['name']+'</option>');
                            });
                        }
                    }
                }
            },
            error:function (jsonData) {

            }
        });
    }
    function bindSelect(seq){
        <#if typeList?exists&&typeList?size gt 0>
        $('#typeId'+seq).empty();
            <#list typeList as type>
                $('#typeId'+seq).append('<option value="${type.id?c}">${type.expenseType?if_exists}</option>');
            </#list>
            getBudgetTitleList(seq);
            $('#typeId'+seq).off('change').on('change',function () {
                getBudgetTitleList(seq);
            });
        </#if>
        $('#amount'+seq).off('blur').on('blur',function(){
            var totalAm=0.00;
            $('.amt').each(function(i,o){
                var am=$(o).val();
                if(am){
                    am=parseFloat(am);
                    if(am){
                        totalAm+=am;
                    }
                }
            });
            $('#wfReqRePayment\\.amount').val(totalAm);
        });
    }
    $(document).ready(function () {
        initValidator();
        $('#nextBtn').off('click').on('click', function () {
            var adId=$('#wfReqRePayment\\.advanceId\\.id').val();
            var detailCount = $('.detailTr').last().attr('seq');
            if(adId&&adId!='0'&&detailCount){
                $('#detailCount').val(detailCount);
                WEBUTILS.validator.checkAll();
                window.setTimeout(function () {
                    var passed = WEBUTILS.validator.isPassed();
                    if (passed) {
                        WEBUTILS.popWindow.createPopWindow(750, 535, '选择流程', '/wf/reqMyFlow!myFlowList.dhtml?applyId=${applyId?if_exists}');
                    } else {
                        WEBUTILS.validator.showErrors();
                    }
                }, 500);
            }else{
                alert('暂时无法申请');
            }
        });
        $('#addDetail').off('click').on('click', function () {
            var currentNodeSeq = $('.detailTr').last().attr('seq');
            if (currentNodeSeq) {
                currentNodeSeq = parseInt(currentNodeSeq);
                var nextNodeSeq = currentNodeSeq + 1;
                $('.detailTr').last().after(String.formatmodel(rePaymentDetail, {seq: nextNodeSeq}));
                WEBUTILS.validator.addMode({
                    id: 'amount' + nextNodeSeq,
                    required: true,
                    pattern: [
                        {type: 'number', exp: '==', msg: ''}
                    ]
                });
                bindSelect(nextNodeSeq);
                submited = false;
            }
        });
        $('#deleteDetail').off('click').on('click', function () {
            var currentNodeSeq = $('.detailTr').last().attr('seq');
            if (currentNodeSeq) {
                currentNodeSeq = parseInt(currentNodeSeq);
                if (currentNodeSeq > 1) {
                    $('.detailTr').last().remove();
                    WEBUTILS.validator.removeMode({
                        id: 'amount' + currentNodeSeq
                    });
                }
            }
        });
        getBudgetTitleList(1);
        $('#typeId1').change(function () {
            getBudgetTitleList(1);
        });
        $('#amount1').off('blur').on('blur',function(){
            var totalAm=0.00;
            $('.amt').each(function(i,o){
                var am=$(o).val();
                if(am){
                    am=parseFloat(am);
                    if(am){
                        totalAm+=am;
                    }
                }
            });
            $('#wfReqRePayment\\.amount').val(totalAm);
        });
    });
</script>
<!--搜索begin-->
<div class="r-top clearfix">
    <p class="text-info text-center lead"><strong>预支还款申请单</strong></p>
</div>
<!--搜索over-->
    <#if Session["userSession"]?exists>
        <#assign userInfo=Session["userSession"]?if_exists>
    <div class="mart5">
        <form class="form-horizontal" action="/wf/rePayment!save.dhtml" method="POST" name="editForm"
              id="editForm">
            <table class="table application nomar">
                <tbody>
                <tr>
                    <td colspan="2">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReq.subject"
                                   style="width: 60px;color: #898989;font-weight: bold;">标题</label>

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
                                <label style="margin-top: 5px;padding-left:5px;font-size: 14px;">${sendDate?string("yyyy-MM-dd HH:mm:ss")}</label>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqRePayment.advanceId.id"
                                   style="width: 60px;color: #898989;font-weight: bold;">预支申请</label>

                            <div class="controls" style="margin-left: 70px;">
                                <select class="int2" style="width: 300px;" id="wfReqRePayment.advanceId.id"
                                        name="wfReqRePayment.advanceId.id">
                                    <#if reqAdvanceAccountList?exists&&reqAdvanceAccountList?size gt 0>
                                    <#list reqAdvanceAccountList as advance>
                                        <option value="${advance.id?c}">${(advance.reqId.subject)?if_exists}&nbsp;&nbsp;已还款金额:${advance.reAmount?double}</option>
                                    </#list>
                                    <#else >
                                        <option value="0">没有未还款的预支申请 暂时无法选择</option>
                                    </#if>
                                </select>
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqRePayment.amount"
                                   style="width: 60px;color: #898989;font-weight: bold;">还款金额</label>

                            <div class="controls" style="margin-left: 70px;">
                                <input type="text" id="wfReqRePayment.amount" name="wfReqRePayment.amount"
                                       placeholder="还款金额" maxlength="10" readonly="readonly">
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqRePayment.payMethod"
                                   style="width: 60px;color: #898989;font-weight: bold;">支付方式</label>

                            <div class="controls" style="margin-left: 70px;">
                                <select class="int2 width-160" id="wfReqRePayment.payMethod"
                                        name="wfReqRePayment.payMethod">
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
                            <label class="control-label" for="wfReqRePayment.remarks"
                                   style="width: 60px;color: #898989;font-weight: bold;">备注</label>

                            <div class="controls" style="margin-left: 70px;">
                                <textarea rows="4" style="width: 95%;" class="font12" id="wfReqRePayment.remarks"
                                          name="wfReqRePayment.remarks" maxlength="400"></textarea>
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <table style="width: 100%;"
                               class="layout table table-bordered table-hover tableBgColor nomar nopadding">
                            <thead>
                            <tr>
                                <td width="100"><strong>费用类别</strong></td>
                                <td width="100"><strong>费用名称</strong></td>
                                <td width="100"><strong>金额</strong></td>
                                <td><strong>备注</strong>
                                    <a href="#" id="deleteDetail" style="float: right;"><i class="icon-minus"></i>
                                        删除</a>
                                    <a href="#" id="addDetail" style="float: right;"><i class="icon-plus"></i> 增加</a>
                                </td>
                            </tr>
                            </thead>
                            <tbody>
                            <tr seq="1" class="detailTr">
                                <td><select class="int2 width-100" id="typeId1" name="typeId1">
                                    <#if typeList?exists&&typeList?size gt 0>
                                        <#list typeList as type>
                                            <option value="${type.id?c}">${type.expenseType?if_exists}</option>
                                        </#list>
                                    </#if>
                                </select></td>
                                <td><select class="int2 width-100" id="titleId1" name="titleId1">
                                </select></td>
                                <td><input type="text" class="int1 width-70 amt" id="amount1" name="amount1" value="0.00"></td>
                                <td><input type="text" class="int1 " style="width: 95%;" id="remarks1" name="remarks1"></td>
                            </tr>
                            </tbody>
                        </table>
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
            <input type="hidden" name="detailCount" id="detailCount">
            <input type="hidden" name="wfReq.nodeCount" id="wfReq.nodeCount" value="0">

            <div id="nodeFlowHidden"></div>
        </form>
    </div>

    </#if>
</@wfCommon.wf_common>