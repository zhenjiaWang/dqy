<#import "/view/template/structure/wf/wfCommon.ftl" as wfCommon>
<#import "/view/common/core.ftl" as c>
<@wfCommon.wf_common>
<script type="text/javascript">
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
        $('#executeBtn').off('click').on('click', function () {
            WEBUTILS.popWindow.createPopWindow(650, 500, '申请转发', '/wf/reqExecute!forward.dhtml?reqId=${wfReq.id?c}');
        });
    });
</script>
<div class="r-top clearfix">
    <p class="text-info text-center lead">销售费用报销申请<em style="font-size: 12px;color: #B94A48;">(No:${wfReq.reqNo?if_exists})</em></p>
</div>
<!--搜索over-->
    <#if Session["userSession"]?exists>
        <#assign userInfo=Session["userSession"]?if_exists>
    <div class="mart5">
        <form class="form-horizontal" action="" method="POST" name="editForm"
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
                                   style="width: 60px;color: #898989;">标题</label>
                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;word-wrap: break-word;word-break: break-all;">${wfReq.subject?if_exists}</label>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label"
                                   style="width: 60px;color: #898989;">申请人</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(wfReq.userId.userName)?if_exists}</label>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label"
                                   style="width: 60px;color: #898989;">申请时间</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(wfReq.sendDate)?string("yyyy-MM-dd HH:mm")}</label>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.amount"
                                   style="width: 60px;color: #898989;">报销金额</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(wfReqSale.amount)?c}</label>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.expenseType"
                                   style="width: 60px;color: #898989;">开始日期</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(wfReqSale.startDate)?string("yyyy-MM-dd")}</label>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.expenseTitle"
                                   style="width: 60px;color: #898989;">结束日期</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;" >
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(wfReqSale.endDate)?string("yyyy-MM-dd")}</label>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.expenseType"
                                   style="width: 60px;color: #898989;">核销日期</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(wfReqSale.payDate)?string("yyyy-MM-dd")}</label>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.expenseType"
                                   style="width: 60px;color: #898989;">费用类别</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(wfReqSale.expenseType.expenseType)?if_exists}</label>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.expenseTitle"
                                   style="width: 60px;color: #898989;">费用项目</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;" >
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(wfReqSale.expenseTitle.titleName)?if_exists}</label>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.channelId"
                                   style="width: 60px;color: #898989;">渠道</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(wfReqSale.channelId.channelName)?if_exists}</label>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.deptId"
                                   style="width: 60px;color: #898989;">业态/部门</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;" >
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(wfReqSale.deptId.deptName)?if_exists}</label>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.systemId"
                                   style="width: 60px;color: #898989;">系统</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(wfReqSale.systemId.systemName)?if_exists}</label>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.customerId"
                                   style="width: 60px;color: #898989;">门店</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;" >
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(wfReqSale.customerId.customerName)?if_exists}</label>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.seriesId"
                                   style="width: 60px;color: #898989;">品类/系列</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(wfReqSale.seriesId.seriesName)?if_exists}</label>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.productId"
                                   style="width: 60px;color: #898989;">单品</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;" >
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(wfReqSale.productId.productCode)?if_exists})&nbsp;&nbsp;&nbsp;${(wfReqSale.productId.productName)?if_exists}</label>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.remarks"
                                   style="width: 60px;color: #898989;">备注</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;word-wrap: break-word;word-break: break-all;">${(wfReqSale.remarks)?if_exists}</label>
                            </div>
                        </div>
                    </td>
                </tr>
                <#if wfReq.financialYn=="Y">
                <tr>
                    <td colspan="2">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.remarks"
                                   style="width: 60px;color: #898989;">财务</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;word-wrap: break-word;word-break: break-all;">[已办理] ${wfReq.financialDesc?if_exists}</label>
                            </div>
                        </div>
                    </td>
                </tr>
                </#if>
                    <#if reqAttList?exists&&reqAttList?size gt 0>
                    <tr>
                        <td colspan="2">
                            <div class="control-group" style="margin-bottom: 5px;">
                                <div class="md-attachment floatleft">
                                    <a><em class="md-mi-attachment"></em>下载附件</a>
                                    <ul class="all-att">
                                        <#list reqAttList as att>
                                            <li class="clearfix">
                                                <span class="docimg"><img width="32" height="32" src="/images/file/${att.postfix?if_exists}.png"></span>
                                                <span class="txt_hidden">${att.oldName?if_exists}</span>
                                                <a href="/wf/req!download.dhtml?id=${att.id?c}" target="_blank">下载</a>
                                            </li>
                                        </#list>
                                    </ul>
                                </div>
                            </div>
                        </td>
                    </tr>
                    </#if>
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
                            <#if comments.action gt 2>
                            <tr style="background-color:#ffffff">
                                <td>${(comments.userId.userName)?if_exists}</td>
                                <td>${comments.actionDesc?if_exists}</td>
                                <td>${comments.created?string("yyyy-MM-dd HH:mm")}</td>
                                <td>${comments.content?if_exists}</td>
                            </tr>
                            </#if>
                        </#list>
                    </#if>
                </tbody>
            </table>
        </div>
    </div>
    <p class="mart10  clearfix">
        <button class="btn btn-inverse floatright " type="button" id="backBtn">返回</button>
        <#if wfReq.complete==1&&wfReq.applyState==2&&wfReq.applyResult==1>
            <a href="/wf/req!print.dhtml?id=${wfReq.id?c}" id="printA" target="_blank"><button class="btn btn-info floatright " type="button" id="printBtn">打印</button></a>
            <#if Session["userSession"]?exists>
                <#assign userInfo=Session["userSession"]?if_exists>
                <#if userInfo?exists>
                    <#if userInfo["userId"]==wfReq.userId.id>
                        <a href="##" id="executeBtn"><button class="btn btn-warning floatright " type="button" id="printBtn">转发</button></a>
                    </#if>
                </#if>
            </#if>
        </#if>
    </p>
    </#if>
</@wfCommon.wf_common>