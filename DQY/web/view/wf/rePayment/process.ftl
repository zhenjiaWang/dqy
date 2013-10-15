<#import "/view/template/structure/wf/wfCommon.ftl" as wfCommon>
<#import "/view/common/core.ftl" as c>
<@wfCommon.wf_common>
<script type="text/javascript">
    function setURL(url) {
        document.location.href=url;
    }
    function loadFlowNode() {
        $.ajax({
            type:'GET',
            url:'/wf/req!getFlowNodeList.dhtml?id=${(wfReq.id)?c}',
            dataType:'json',
            success:function (jsonData) {
                if (jsonData) {
                    if (jsonData['result'] == '0') {
                        var nodeApproveList = jsonData['nodeApproveList'];
                        if (nodeApproveList) {
                            $('#nodeUL').empty();
                            $(nodeApproveList).each(function (i, o) {
                                var nodeSeqText='';
                                if(o['nodeSeq']=='0000'){
                                    nodeSeqText='';
                                }else{
                                    nodeSeqText=o['nodeSeq'];
                                }
                                $('#nodeUL').append(String.formatmodel(flowApproveNodeShow,{nodeSeq:o['nodeSeq'],
                                    text:o['nodeText'],
                                    className:o['className'],
                                    nodeSeqText:nodeSeqText}));
                            });
                            deleteLastArrow();
                        }
                    }
                }
            },
            error:function (jsonData) {

            }
        });
    }
    function deleteLastArrow() {
        $('li', '#nodeUL').last().remove();
    }
    $(document).ready(function () {
        loadFlowNode();
        $('#backBtn').off('click').on('click', function () {
            history.back();
        });
        $('#okBtn').off('click').on('click', function () {
            WEBUTILS.popWindow.createPopWindow(500, 300, '审批通过', '/wf/reqTask!reason.dhtml?id=${wfReqTask.id?c}&approveIdea=1');
        });
        $('#noBtn').off('click').on('click', function () {
            WEBUTILS.popWindow.createPopWindow(500, 300, '审批否决', '/wf/reqTask!reason.dhtml?id=${wfReqTask.id?c}&approveIdea=2');
        });
        $('#forwardBtn').off('click').on('click', function () {
            WEBUTILS.popWindow.createPopWindow(650, 500, '审批转审', '/wf/reqTask!forward.dhtml?id=${wfReqTask.id?c}&approveIdea=3');
        });
        <#if wfReqTask.nodeSeq gt 1>
            $('#returnBtn').off('click').on('click', function () {
                WEBUTILS.popWindow.createPopWindow(500, 300, '审批回退', '/wf/reqTask!reason.dhtml?id=${wfReqTask.id?c}&approveIdea=4');
            });
        </#if>
    });
</script>
<div class="r-top clearfix">
    <p class="text-info text-center lead"><strong>预支还款申请单</strong><em style="font-size: 14px;color: #B94A48;">(No:${wfReq.reqNo?if_exists})</em></p>
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
                        <div class="process-bar font14">
                            <ul class="clearfix" id="nodeUL">

                            </ul>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReq.subject"
                                   style="width: 60px;color: #898989;font-weight: bold;">申请标题</label>
                            <div class="controls" style="margin-left: 70px;">
                                <label style="margin-top: 5px;padding-left:5px;font-size: 14px;">${wfReq.subject?if_exists}</label>
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
                                <label style="margin-top: 5px;padding-left:5px;font-size: 14px;">${(wfReq.userId.userName)?if_exists}</label>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label"
                                   style="width: 60px;color: #898989;font-weight: bold;">申请时间</label>

                            <div class="controls" style="margin-left: 70px;">
                                <label style="margin-top: 5px;padding-left:5px;font-size: 14px;">${(wfReq.sendDate)?string("yyyy-MM-dd HH:mm")}</label>
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
                                <label style="margin-top: 5px;padding-left:5px;font-size: 14px;">${(wfReqRePayment.amount)?double}</label>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqRePayment.payMethod"
                                   style="width: 60px;color: #898989;font-weight: bold;">支付方式</label>

                            <div class="controls" style="margin-left: 70px;" >
                                <label style="margin-top: 5px;padding-left:5px;font-size: 14px;">
                                    <#if wfReqRePayment.payMethod?exists>
                                        <#if wfReqRePayment.payMethod==1>
                                            现金
                                        <#elseif wfReqRePayment.payMethod==2>
                                            银行转账
                                        <#elseif wfReqRePayment.payMethod==3>
                                            支票
                                        </#if>
                                    </#if>
                                </label>
                            </div>
                        </div>
                    </td>
                </tr>
                    <#if detailList?exists&&detailList?size gt 0>
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
                                    </td>
                                </tr>
                                </thead>
                                <tbody>
                                    <#list detailList as detail>
                                    <tr >
                                        <td>${(detail.expenseType.expenseType)?if_exists}</td>
                                        <td>${(detail.expenseTitle.titleName)?if_exists}</td>
                                        <td>${detail.amount?double}</td>
                                        <td>${detail.remarks?if_exists}</td>
                                    </tr>
                                    </#list>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                    </#if>
                <tr>
                    <td colspan="2">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqRePayment.remarks"
                                   style="width: 60px;color: #898989;font-weight: bold;">备注</label>

                            <div class="controls" style="margin-left: 70px;">
                                <label style="margin-top: 5px;padding-left:5px;font-size: 14px;">${(wfReqRePayment.remarks)?if_exists}</label>
                            </div>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </form>
        <div style="" class="mart10">
            <table class="table table-bordered table-hover tableBgColor">
                <thead>
                <tr class="thColor">
                    <th width="80">操作人</th>
                    <th width="80">操作</th>
                    <th width="130">操作时间</th>
                    <th>备注</th>
                </tr>
                </thead>
                <tbody>
                    <#if reqCommentsList?exists&&reqCommentsList?size gt 0>
                        <#list reqCommentsList as comments>
                        <tr style="background-color:#ffffff">
                            <td>${(comments.userId.userName)?if_exists}</td>
                            <td>${comments.actionDesc?if_exists}</td>
                            <td>${comments.created?string("yyyy-MM-dd HH:mm")}</td>
                            <td>${comments.content?if_exists}</td>
                        </#list>
                    </#if>
                </tbody>
            </table>
        </div>
    </div>
    <p class="mart10  clearfix">
        <#if wfReqTask.taskState==0>
            <#if wfReqTask.nodeSeq gt 1>
                <button class="btn btn-inverse floatright " type="button" id="returnBtn">回退</button>
            </#if>
            <button class="btn btn-warning floatright " type="button" id="forwardBtn">转审</button>
            <button class="btn btn-danger floatright " type="button" id="noBtn">否决</button>
            <button class="btn btn-success floatright " type="button" id="okBtn">批准</button>
        <#elseif wfReqTask.taskState==1>
        <button class="btn btn-inverse floatright " type="button" id="backBtn">返回</button>
        </#if>
    </p>
    </#if>
</@wfCommon.wf_common>