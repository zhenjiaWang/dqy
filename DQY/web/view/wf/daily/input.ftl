<#import "/view/template/structure/wf/wfCommon.ftl" as wfCommon>
<#import "/view/common/core.ftl" as c>
<@wfCommon.wf_common>
<link href="/css/datepicker.css" rel="stylesheet"/>
<link href="/css/validator/validator.css" rel="stylesheet"/>
<script type="text/javascript" src="/js/webutils/webutils.validator.js"></script>
<script type="text/javascript" src="/js/webutils/reg.js"></script>
<script type="text/javascript" src="/js/bootstrap-datepicker.js"></script>

<link href="/css/kendo.common.css" rel="stylesheet"/>
<link href="/css/kendo.metro.min.css" rel="stylesheet"/>
<link href="/css/kendo.dataviz.min.css" rel="stylesheet"/>
<link href="/css/kendo.dataviz.metro.min.css" rel="stylesheet"/>
<script type="text/javascript" src="/js/kendo.web.min.js"></script>
<style scoped>
    .file-icon
    {
        display: inline-block;
        float: left;
        width: 25px;
        height: 25px;
        margin-left: 10px;
        margin-top: 13.5px;
    }


    .img-file { background-image: url(/images/jpg.png) }
    .doc-file { background-image: url(/images/doc.png) }
    .pdf-file { background-image: url(/images/pdf.png) }
    .xls-file { background-image: url(/images/xls.png) }
    .zip-file { background-image: url(/images/zip.png) }
    .default-file { background-image: url(/images/default.png) }

    #example .file-heading
    {
        font-family: Arial;
        font-size: 1.1em;
        display: inline-block;
        float: left;
        width: 450px;
        margin: 0 0 0 20px;
        height: 25px;
        -ms-text-overflow: ellipsis;
        -o-text-overflow: ellipsis;
        text-overflow: ellipsis;
        overflow:hidden;
        white-space:nowrap;
    }

    #example .file-name-heading
    {
        font-weight: bold;
    }

    #example .file-size-heading
    {
        font-weight: normal;
        font-style: italic;
    }

    li.k-file .file-wrapper .k-upload-action
    {
        position: absolute;
        top: 0;
        right: 0;
    }

    li.k-file div.file-wrapper
    {
        position: relative;
        height: 40px;
    }
</style>
<script type="text/javascript">
    var submited = false;
    var uploadRemove=false;
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
                    id: 'wfReqDaily\\.amount',
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
                },{
                    id: 'date1',
                    required: true,
                    pattern: [
                        {type: 'reg', exp: '_date', msg: '`'}
                    ]
                },{
                    id: 'wfReqDaily\\.payee',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'}
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
    function getBudgetTypeList(seq){
        var deptObj=$('#deptId'+seq);
        var typeObj=$('#typeId'+seq);
        var deptId=$(deptObj).val();
        $.ajax({
            type:'GET',
            url:'/sys/budgetType!getTypeList.dhtml?deptId='+deptId,
            dataType:'json',
            success:function (jsonData) {
                if (jsonData) {
                    if (jsonData['result'] == '0') {
                        var typeList=jsonData['typeList'];
                        $(typeObj).empty();
                        if(typeList){
                            $(typeList).each(function(i,o){
                                $(typeObj).append('<option value="'+o['id']+'">'+o['name']+'</option>');
                            });
                        }else{
                            $(typeObj).append('<option value="">暂无数据</option>');
                        }
                    }else{
                        $(typeObj).empty();
                        $(typeObj).append('<option value="">暂无数据</option>');
                    }
                    getBudgetTitleList(seq);
                    $('#typeId'+seq).off('change').on('change',function () {
                        getBudgetTitleList(seq);
                    });
                }
            },
            error:function (jsonData) {

            }
        });
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
                        }else{
                            $(titleObj).append('<option value="">暂无数据</option>');
                        }
                    }else{
                        $(titleObj).empty();
                        $(titleObj).append('<option value="">暂无数据</option>');
                    }
                }
            },
            error:function (jsonData) {

            }
        });
    }
    function bindSelect(seq){
        <#if departmentList?exists&&departmentList?size gt 0>
            $('#deptId'+seq).empty();
            <#list departmentList as dept>
                var levelStr='';
                <#if dept.deptLevel gt 1>
                    <#list 1..dept.deptLevel as i>
                        levelStr+='&nbsp;&nbsp;';
                    </#list>
                </#if>
                $('#deptId'+seq).append('<option value="${dept.id?c}">'+levelStr+'${dept.deptName?if_exists}</option>');
            </#list>
            $('#deptId'+seq).val('${deptId?c}');
            getBudgetTypeList(seq);
            $('#deptId'+seq).off('change').on('change',function () {
                getBudgetTypeList(seq);
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
            $('#wfReqDaily\\.amount').val(totalAm);
        });
    }

    function addExtensionClass(extension) {
        switch (extension) {
            case '.jpg':
            case '.img':
            case '.png':
            case '.gif':
                return "img-file";
            case '.doc':
            case '.docx':
                return "doc-file";
            case '.xls':
            case '.xlsx':
                return "xls-file";
            case '.pdf':
                return "pdf-file";
            case '.zip':
            case '.rar':
                return "zip-file";
            default:
                return "default-file";
        }
    }

    function onSuccess(e) {
        if (!uploadRemove) {
            var lastLi = $('li', '.k-upload-files').last();
            if (lastLi) {
                var img=$('img',lastLi);
                if(img){
                    var extension=$(img).attr('extension');
                    if(extension){
                        extension=extension.substring(1,extension.length);
                        $(img).attr('src','/images/file/'+extension+'.png');
                    }
                }
            }
        }
    }
    function onRemove(e){
        uploadRemove=true;
    }
    function onUpload(e){
        uploadRemove=false;
    }

    function dateEvent(){
        $('.dateTd').each(function(i,o){
            var dateObj=$('input[type="text"]',o);
            if(dateObj){
                $(dateObj).datepicker({
                    format: 'yyyy-mm-dd'
                }).on('changeDate', function (ev) {
                            $(dateObj).datepicker('hide')
                        });
            }
        });
    }
    $(document).ready(function () {
        initValidator();
        $('#nextBtn').off('click').on('click', function () {
            var detailCount = $('.detailTr').last().attr('seq');
            if(detailCount){
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
                WEBUTILS.validator.addMode({
                    id: 'date' + nextNodeSeq,
                    required: true,
                    pattern: [
                        {type: 'reg', exp: '_date', msg: ''}
                    ]
                });
                bindSelect(nextNodeSeq);
                dateEvent();
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
                    WEBUTILS.validator.removeMode({
                        id: 'date' + currentNodeSeq
                    });
                }
            }
        });
        getBudgetTypeList(1);

        $('#deptId1').off('change').on('change',function () {
            getBudgetTypeList(1);
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
            $('#wfReqDaily\\.amount').val(totalAm);
        });

        $("#upload").kendoUpload({
            multiple: true,
            async: {
                saveUrl:"/common/common!upload.dhtml?attKey=${applyId?if_exists}&attToken=" + $("#attToken").val(),
                removeUrl: "/common/common!remove.dhtml?attKey=${applyId?if_exists}&attToken=" + $("#attToken").val(),
                autoUpload: true
            },
            success: onSuccess,
            remove: onRemove,
            upload: onUpload,
            localization:{
                "select":"上传文件",
                "cancel":"取消",
                "retry":"重试",
                "remove":"删除",
                "done":"完成",
                "uploadSelectedFiles":"Upload files",
                "dropFilesHere":"drop files here to upload",
                "statusUploading":"uploading",
                "statusUploaded":"uploaded",
                "statusFailed":"failed"
            },
            template: kendo.template($('#fileTemplate').html())
        });
        dateEvent();
    });
</script>
<!--搜索begin-->
<div class="r-top clearfix">
    <p class="text-info text-center lead"><strong>费用报销申请</strong></p>
</div>
<!--搜索over-->
    <#if Session["userSession"]?exists>
        <#assign userInfo=Session["userSession"]?if_exists>
    <div class="mart5">
        <form class="form-horizontal" action="/wf/daily!save.dhtml" method="POST" name="editForm"
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
                            <label class="control-label" for="wfReqDaily.payee"
                                   style="width: 60px;color: #898989;font-weight: bold;">收款单位</label>

                            <div class="controls" style="margin-left: 70px;">
                                <input style="width: 95%;" type="text" id="wfReqDaily.payee" name="wfReqDaily.payee"
                                       placeholder="收款单位/人" maxlength="20">
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqDaily.bank"
                                   style="width: 60px;color: #898989;font-weight: bold;">开户行</label>

                            <div class="controls" style="margin-left: 70px;">
                                <input type="text" id="wfReqDaily.bank" name="wfReqDaily.bank" placeholder="开户行" maxlength="20">
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqDaily.bankAccount"
                                   style="width: 60px;color: #898989;font-weight: bold;">帐号</label>
                            <div class="controls" style="margin-left: 70px;">
                                <input type="text" id="wfReqDaily.bankAccount" name="wfReqDaily.bankAccount" placeholder="银行帐号" maxlength="20">
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqDaily.amount"
                                   style="width: 60px;color: #898989;font-weight: bold;">报销金额</label>

                            <div class="controls" style="margin-left: 70px;">
                                <input type="text" id="wfReqDaily.amount" name="wfReqDaily.amount"
                                       placeholder="报销金额" maxlength="10" readonly="readonly">
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqDaily.remarks"
                                   style="width: 60px;color: #898989;font-weight: bold;">备注</label>

                            <div class="controls" style="margin-left: 70px;">
                                <textarea rows="4" style="width: 95%;" class="font12" id="wfReqDaily.remarks"
                                          name="wfReqDaily.remarks" maxlength="400"></textarea>
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
                                <td width="100"><strong>费用部门</strong></td>
                                <td width="100"><strong>费用类型</strong></td>
                                <td width="100"><strong>费用项目</strong></td>
                                <td width="110"><strong>费用日期</strong></td>
                                <td width="80"><strong>金额</strong></td>
                                <td>
                                    <a href="/wf/daily.dhtml?trueAmount=1" id="trueDetail" style="float: right;"><i class="icon-eye-open"></i>&nbsp;</a>
                                    <a href="#" id="deleteDetail" style="float: right;"><i class="icon-minus"></i>
                                        删除</a>
                                    <a href="#" id="addDetail" style="float: right;"><i class="icon-plus"></i> 增加</a>
                                </td>
                            </tr>
                            </thead>
                            <tbody>
                            <tr seq="1" class="detailTr">
                                <td><select class="int2 width-100" id="deptId1" name="deptId1">
                                    <#if departmentList?exists&&departmentList?size gt 0>
                                        <#list departmentList as dept>
                                            <option value="${dept.id?c}" <#if deptId==dept.id>selected="selected" </#if>>
                                                <#if dept.deptLevel gt 1>
                                                    <#list 1..dept.deptLevel as i>
                                                        &nbsp;&nbsp;
                                                    </#list>
                                                </#if>
                                            ${dept.deptName?if_exists}
                                            </option>
                                        </#list>
                                    </#if>
                                </select></td>
                                <td><select class="int2 width-100" id="typeId1" name="typeId1">
                                </select></td>
                                <td><select class="int2 width-100" id="titleId1" name="titleId1">
                                </select></td>
                                <td data-date-format="yyyy-mm-dd" data-date="" class="date dateTd">
                                    <div class="control-group" style="margin-bottom: 0px;">
                                        <input type="text" class="int1 width-100" id="date1" name="date1" >
                                    </div>
                                </td>
                                <td><input type="text" class="int1 width-70 amt" id="amount1" name="amount1" value="0.00"></td>
                                <td><input type="text" class="int1 " style="width: 95%;" id="remarks1" name="remarks1"></td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <input type="file" name="files" id="upload" />
                            <script id="fileTemplate" type="text/x-kendo-template">
                                <span class='k-progress'></span>
                                <div class='file-wrapper'>
                                    <img src="/images/file/file.png" extension="#=files[0].extension#" width="25" height="25" style="float: left;margin-right: 10px;">
                                    <h4 class='file-heading file-name-heading'> #=name# (#=size# bytes)</h4>
                                    <button type='button' class='k-upload-action'></button>
                                </div>
                            </script>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
            <p class="mart10  clearfix" style="width: 800px;">
                <button class="btn btn-success floatright " type="button" id="nextBtn">审批人</button>
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