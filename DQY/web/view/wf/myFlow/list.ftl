<#import "/view/template/common.ftl" as common>
<#import "/view/common/core.ftl" as c>
<@common.html>
<link href="/css/validator/validator.css" rel="stylesheet"/>
<script type="text/javascript" src="/js/webutils/webutils.validator.js"></script>
<script type="text/javascript" src="/js/webutils/reg.js"></script>
<script type="text/javascript">
    var submited = false;
    function submitForm(){
        if (!submited) {
            WEBUTILS.validator.checkAll();
            window.setTimeout(function () {
                var passed = WEBUTILS.validator.isPassed();
                if (passed) {
                    document.editForm.submit();
                    submited = true;
                } else {
                    WEBUTILS.validator.showErrors();
                }
            }, 500);
        }
    }
    function loadFlowNode() {
        $.ajax({
            type:'GET',
            url:'/wf/reqMyFlow!getFlowNodeList.dhtml?id=' + $('#flowId').val(),
            dataType:'json',
            success:function (jsonData) {
                if (jsonData) {
                    if (jsonData['result'] == '0') {
                        var nodeList = jsonData['nodeList'];
                        if (nodeList) {
                            $('#nodeUL').empty();
                            var nodeSize = $(nodeList).size();
                            $(nodeList).each(function (i, o) {
                                var approveList=o['approveList'];
                                var nodeSeqText='';
                                if(o['nodeSeq']=='0000'){
                                    nodeSeqText='';
                                }else{
                                    nodeSeqText=o['nodeSeq'];
                                }
                                if(approveList){
                                    $(approveList).each(function(j,ap){
                                        $('#nodeUL').append(String.formatmodel(flowApproveNodeShow,{nodeSeq:o['nodeSeq'],
                                            nodeType:o['nodeType'],approveType:ap['approveType'],approveId:ap['approveId'],text:ap['approveName'],
                                            nodeSeqText:nodeSeqText}));
                                    });
                                }
                            });
                            deleteLastArrow();h
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
        $('#flowId').off('change').on('change', function () {
            loadFlowNode();
        });
        $('#addFlowBtn').off('click').on('click', function () {
            WEBUTILS.popWindow.closePopWindow();
            window.setTimeout(function(){
                WEBUTILS.popWindow.createPopWindow(700, 550, '自拟流程', '/wf/reqMyFlow!input.dhtml?applyId=${applyId?if_exists}');
            },500);
        });
        $('#delteFlowBtn').off('click').on('click', function () {
            var flowId=$('#flowId').val();
            if(flowId&&flowId!=''){
                $.ajax({
                    type:'GET',
                    url:'/wf/reqMyFlow!delete.dhtml?id='+flowId,
                    dataType:'json',
                    success:function (jsonData) {
                        if (jsonData) {
                            if (jsonData['result'] == '0') {
                                flowId = jsonData['flowId'];
                                if (flowId) {
                                    var flowName = $('option[value="' + flowId + '"]', '#flowId').text();
                                    $('option[value="' + flowId + '"]', '#flowId').remove();
                                    if($('option','#flowId').size()==0){
                                        $('#flowId').append(' <option value="">[请自拟流程]</option>');
                                    }
                                }
                            }
                        }
                    },
                    error:function (jsonData) {

                    }
                });
            }
        });
    });
</script>
    <@c.joddForm bean="sysOrgGroup" scope="request">
    <form class="form-horizontal" action="/sys/orgGroup!save.dhtml" method="POST" name="editForm"
          id="editForm">
        <div class="control-group">
            <label class="control-label" for="sysOrgGroup.groupName" style="width: 100px;">审批流程</label>
            <div class="controls" style="margin-left: 110px;">
            <select class="int2 width-160" id="flowId" name="flowId">
                <#if reqMyFlowList?exists&&reqMyFlowList?size gt 0>
                        <#list reqMyFlowList as myFlow>
                            <option value="${myFlow.id?c}">${myFlow.flowName?if_exists}</option>
                        </#list>
                <#else>
                    <option value="">[请自拟流程]</option>
                </#if>
            </select>
                <button class="btn" type="button" id="addFlowBtn"><i class="icon-plus"></i>自拟流程</button>
                <button class="btn" type="button" id="delteFlowBtn"><i class="icon-trash"></i>删除流程</button>
            </div>
        </div>

        <div class="process-bar font14" style="margin-left: 40px;">
            <ul class="clearfix" id="nodeUL">
            </ul>
        </div>
        <input type="hidden" name="sysOrgGroup.id" id="sysOrgGroup.id">
        <@c.token/>
    </form>
    </@c.joddForm>
</@common.html>