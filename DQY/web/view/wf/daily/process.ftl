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
        $('#trueDetail').off('click').on('click', function () {
            if ( $('#trueList').is(':visible')) {
                $('i',this).removeClass().addClass('icon-eye-open');
                $('#trueList').hide();
            }else{
                $('i',this).removeClass().addClass('icon-eye-close');
                $('#trueList').show();
            }
        });
    });
</script>
<div class="r-top clearfix">
    <p class="text-info text-center lead">费用报销申请<em style="font-size: 12px;color: #B94A48;">(No:${wfReq.reqNo?if_exists})</em></p>
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
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${wfReq.subject?if_exists}</label>
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
                            <label class="control-label" for="wfReq.subject"
                                   style="width: 60px;color: #898989;">收款单位</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;word-wrap: break-word;word-break: break-all;">${wfReqDaily.payee?if_exists}</label>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label"
                                   style="width: 60px;color: #898989;">开户行</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(wfReqDaily.bank)?if_exists}</label>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label"
                                   style="width: 60px;color: #898989;">帐号</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(wfReqDaily.bankAccount)?if_exists}</label>
                            </div>
                        </div>
                    </td>
                </tr>
                    <#if trueList?exists&&trueList?size gt 0>
                    <tr>
                        <td >
                            <div class="control-group" style="margin-bottom: 5px;">
                                <label class="control-label" for="wfReqDaily.amount"
                                       style="width: 60px;color: #898989;">报销金额</label>

                                <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                    <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(wfReqDaily.amount)?double}</label>
                                </div>
                            </div>
                        </td>
                        <td >
                            <div class="control-group" style="margin-bottom: 5px;">
                                <label class="control-label" for="wfReqDaily.trueAmount"
                                       style="width: 60px;color: #898989;">实际花费</label>

                                <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                    <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(wfReqDaily.trueAmount)?double}</label>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <#else >
                    <tr>
                        <td colspan="2">
                            <div class="control-group" style="margin-bottom: 5px;">
                                <label class="control-label" for="wfReqDaily.amount"
                                       style="width: 60px;color: #898989;">报销金额</label>

                                <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                    <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(wfReqDaily.amount)?double}</label>
                                </div>
                            </div>
                        </td>
                    </tr>
                    </#if>
                    <tr>
                        <td colspan="2">
                            <table class="table nomar">
                                <tbody><tr>
                                    <td class="nopadding p-top5 "><span class="label label-info yearAmountTotal">预算总额：${totalAmount?if_exists}</span></td>
                                    <td class="nopadding p-top5"><span class="label label-success">已产生-已审批：${totalPassAmount?if_exists}</span></td>
                                    <td class="nopadding p-top5"><span class="label label-warning">已产生-待审批：${totalIngAmount?if_exists}</span></td>
                                    <td class="nopadding p-top5"><span class="label label-important">超出预算金额：${remnantAmount?if_exists}</span></td>
                                </tr>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                    <#if detailList?exists&&detailList?size gt 0>
                    <tr>
                        <td colspan="2">
                            <table style="width: 100%;"
                                   class="layout table table-bordered table-hover tableBgColor nomar nopadding">
                                <thead>
                                <tr>
                                    <td width="100">费用部门</td>
                                    <td width="100">费用类型</td>
                                    <td width="100">费用项目</td>
                                    <td width="110">费用日期</td>
                                    <td width="80">金额</td>
                                    <td>备注
                                        <#if trueList?exists&&trueList?size gt 0>
                                            <a href="##" id="trueDetail" style="float: right;"><i class="icon-eye-open"></i></a>
                                        </#if>
                                    </td>
                                </tr>
                                </thead>
                                <tbody>
                                    <#list detailList as detail>
                                    <tr >
                                        <td>${(detail.expenseDept.deptName)?if_exists}</td>
                                        <td>${(detail.expenseType.expenseType)?if_exists}</td>
                                        <td>${(detail.expenseTitle.titleName)?if_exists}</td>
                                        <td>${detail.amountDate?string("yyyy-MM-dd")}</td>
                                        <td>${detail.amount?double}</td>
                                        <td>${detail.remarks?if_exists}</td>
                                    </tr>
                                    </#list>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                    </#if>
                    <#if trueList?exists&&trueList?size gt 0>
                    <tr style="display: none;" id="trueList">
                        <td colspan="2">
                            <table style="width: 100%;"
                                   class="layout table table-bordered table-hover tableBgColor nomar nopadding">
                                <thead>
                                <tr>
                                    <td width="100">费用部门</td>
                                    <td width="100">费用类型</td>
                                    <td width="100">费用项目</td>
                                    <td width="110">费用日期</td>
                                    <td width="80">金额</td>
                                    <td>备注
                                    </td>
                                </tr>
                                </thead>
                                <tbody>
                                    <#list trueList as detail>
                                    <tr >
                                        <td>${(detail.expenseDept.deptName)?if_exists}</td>
                                        <td>${(detail.expenseType.expenseType)?if_exists}</td>
                                        <td>${(detail.expenseTitle.titleName)?if_exists}</td>
                                        <td>${detail.amountDate?string("yyyy-MM-dd")}</td>
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
                            <label class="control-label" for="wfReqDaily.remarks"
                                   style="width: 60px;color: #898989;">备注</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(wfReqDaily.remarks)?if_exists}</label>
                            </div>
                        </div>
                    </td>
                </tr>
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
                            <#if comments.action!=1>
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