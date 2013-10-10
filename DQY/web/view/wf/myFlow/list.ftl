<#import "/view/template/common.ftl" as common>
<#import "/view/common/core.ftl" as c>
<@common.html>
<script type="text/javascript">
    var submited = false;
    function buildNodeApprove(){
        parent.clearApproveNode();
        var nodeCount=$('.nodeLi[nodeSeq!="0000"]','#nodeUL').size();
        $('.nodeLi[nodeSeq!="0000"]','#nodeUL').each(function(i,o){
            var nodeSeq=$(o).attr('nodeSeq');
            var approveId=$(o).attr('approveId');
            var approveType=$(o).attr('approveType');
            var nodeType=$(o).attr('nodeType');
            var flowId=$('#myFlowId').val();
            parent.addApproveNode(nodeSeq,approveId,approveType,nodeType,nodeCount,flowId);
        });
    }
    function loadFlowNode() {
        var myFlowId=$('#myFlowId').val();
        if(myFlowId&&myFlowId!=''){
            $.ajax({
                type:'GET',
                url:'/wf/reqMyFlow!getFlowNodeList.dhtml?id=' + myFlowId,
                dataType:'json',
                success:function (jsonData) {
                    if (jsonData) {
                        if (jsonData['result'] == '0') {
                            var nodeList = jsonData['nodeList'];
                            if (nodeList) {
                                $('#nodeUL').empty();
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
                                deleteLastArrow();
                            }
                        }
                    }
                },
                error:function (jsonData) {

                }
            });
        }
    }

    function deleteLastArrow() {
        $('li', '#nodeUL').last().remove();
    }
    function dataSubmit() {
        if (!submited) {
            window.setTimeout(function () {
                var nodeCount=$('.nodeLi[nodeSeq!="0000"]','#nodeUL').size();
                if(nodeCount>0){
                    var r=confirm("确定要提交申请并且启动审批流程吗");
                    if(r==true){
                        if (!submited) {
                            buildNodeApprove();
                            window.setTimeout(function () {
                                parent.submitApply();
                                submited = true;
                            }, 1000);
                        }
                    }
                }else if(nodeCount==0){
                    WEBUTILS.alert.alertError('错误','请选择一个审批流程再进行提交，没有审批流程请自行创建');
                }
            }, 500);
        }
    }
    $(document).ready(function () {
        $('#addFlowBtn').off('click').on('click', function () {
            document.location.href='/wf/reqMyFlow!input.dhtml?applyId=${applyId?if_exists}';
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
        loadFlowNode();
        $('#myFlowId').off('change').on('change', function () {
            loadFlowNode();
        });
    });
</script>
    <@c.joddForm bean="sysOrgGroup" scope="request">
    <form class="form-horizontal" action="/sys/orgGroup!save.dhtml" method="POST" name="editForm"
          id="editForm">
        <div class="control-group">
            <label class="control-label" for="myFlowId" style="width: 100px;">审批流程</label>
            <div class="controls" style="margin-left: 110px;">
            <select class="int2 width-160" id="myFlowId" name="myFlowId">
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