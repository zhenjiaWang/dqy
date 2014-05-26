<#import "/view/template/structure/wf/wfCommon.ftl" as wfCommon>
<#import "/view/common/core.ftl" as c>
<@wfCommon.wf_common>
<link href="/css/datepicker.css" rel="stylesheet"/>
<link href="/js/editor/themes/default/css/umeditor.min.css" type="text/css" rel="stylesheet">
<link href="/css/validator/validator.css" rel="stylesheet"/>
<script type="text/javascript" src="/js/webutils/webutils.validator.js"></script>
<script type="text/javascript" src="/js/webutils/reg.js"></script>
<script type="text/javascript" src="/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/editor/umeditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/editor/umeditor.min.js"></script>
<script type="text/javascript" src="/js/editor/lang/zh-cn/zh-cn.js"></script>

<link href="/css/kendo.common.css" rel="stylesheet"/>
<link href="/css/kendo.metro.min.css" rel="stylesheet"/>
<link href="/css/kendo.dataviz.min.css" rel="stylesheet"/>
<link href="/css/kendo.dataviz.metro.min.css" rel="stylesheet"/>
<script type="text/javascript" src="/js/kendo.web.min.js"></script>

<link type="text/css" href="/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<script src="/js/jquery.ztree.all-3.5.min.js"></script>
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
    var currentSEQ = false;
    var currentTYPE = false;

    var srcNode;
    var setting = {
        view: {
            selectedMulti: false
        },
        async: {
            enable: true,
            url: "/common/common!orgTreeData.dhtml",
            autoParam: ["id=parentId", "name=n", "level=lv"]
        },
        callback: {
            onClick: zTreeOnClick
        }
    };
    function zTreeOnClick(event, treeId, treeNode) {
        srcNode = treeNode;
        if (treeNode&&currentSEQ) {
            $('#deptId'+currentSEQ+'_'+currentTYPE).val(treeNode['id']);
            $('#deptName'+currentSEQ+'_'+currentTYPE).val(treeNode['name']);
            $('#deptName'+currentSEQ+'_'+currentTYPE).trigger('blur');
            $('.treeDiv').fadeOut();
            currentSEQ=false;
            currentTYPE=false;
        }
    }

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
                    id: 'wfReqDaily\\.trueAmount',
                    required: true,
                    pattern: [
                        {type: 'number', exp: '==', msg: '不能为空'}
                    ]
                },
                {
                    id: 'amount1_1',
                    required: true,
                    pattern: [
                        {type: 'number', exp: '==', msg: '`'}
                    ]
                },{
                    id: 'date1_1',
                    required: true,
                    pattern: [
                        {type: 'reg', exp: '_date', msg: '`'}
                    ]
                },
                {
                    id: 'amount1_2',
                    required: true,
                    pattern: [
                        {type: 'number', exp: '==', msg: '`'}
                    ]
                },{
                    id: 'date1_2',
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
                },{
                    id: 'deptName1_1',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '`'}
                    ]
                },{
                    id: 'deptName1_2',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '`'}
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
    function bindSelect(type,seq){
        <#if typeList?exists&&typeList?size gt 0>
            $('#typeId'+seq+'_'+type).empty();
            <#list typeList as type>
                $('#typeId'+seq+'_'+type).append('<option value="${type.id?c}">${type.expenseType?if_exists}</option>');
            </#list>
        </#if>
        <#if titleList?exists&&titleList?size gt 0>
            $('#titleId'+seq+'_'+type).empty();
            <#list titleList as title>
                $('#titleId'+seq+'_'+type).append('<option value="${title.id?c}">${title.titleName?if_exists}</option>');
            </#list>
        </#if>
        if(type==1){
            $('#amount'+seq+'_'+type).off('blur').on('blur',function(){
                var totalAm=0.00;
                $('.amt1').each(function(i,o){
                    var am=$(o).val();
                    if(am){
                        am=parseFloat(am);
                        if(am){
                            am=am.toFixed(2);
                            am=parseFloat(am);
                            totalAm+=am;
                        }
                    }
                });
                $('#wfReqDaily\\.amount').val(totalAm);
            });
        }else if(type==2){
            $('#amount'+seq+'_'+type).off('blur').on('blur',function(){
                var totalAm=0.00;
                $('.amt2').each(function(i,o){
                    var am=$(o).val();
                    if(am){
                        am=parseFloat(am);
                        if(am){
                            am=am.toFixed(2);
                            am=parseFloat(am);
                            totalAm+=am;
                        }
                    }
                });
                $('#wfReqDaily\\.trueAmount').val(totalAm);
            });
        }
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
        if (e.operation == "upload") {
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
    }
    function onUpload(e){
        // Array with information about the uploaded files
        var files = e.files;

        // Check the extension of each file and abort the upload if it is not .jpg
        $.each(files, function () {
            if (this.extension.toLowerCase() == ".exe"
                    ||this.extension.toLowerCase() == ".dll"
                    ||this.extension.toLowerCase() == ".js"
                    ) {
                WEBUTILS.msg.alertFail('抱歉,你上传的附件包含有风险的文件!');
                e.preventDefault();
            }
            if(this.size>=20971520){
                WEBUTILS.msg.alertFail('抱歉,你上传的附件超过最大20M大小限制!');
                e.preventDefault();
            }
        });
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

        $('.dept').off('click').on('click', function (e) {
            e.preventDefault();
            e.stopPropagation();
            currentSEQ=$(this).parent().parent().parent().attr('seq');
            currentTYPE=$(this).parent().parent().parent().attr('type');
            var left = $(this).offset().left ;
            var top = $(this).offset().top -25;
            top += $(this).height();
            $('.treeDiv').css({
                        top: top,
                        left: left,
                        zIndex: 99999
                    }
            );
            $('.treeDiv').fadeIn();
            $('.treeDiv').find('div').show();
        });
    }

    function getContent() {
        return UM.getEditor('editor').getContent();
    }
    function hasContent() {
        return UM.getEditor('editor').hasContents();
    }
    function loadBudgetAmount() {
        var budgetYear=$('#wfReqDaily\\.budgetYear').val();
        if(budgetYear&&budgetYear!=''){
            WEBUTILS.alert.alertInfo('预算信息','正在获取'+budgetYear+'年的预算');
            $.ajax({
                type:'GET',
                url:'/wf/req!getBudgetAmount.dhtml?budgetYear=' + budgetYear,
                dataType:'json',
                success:function (jsonData) {
                    if (jsonData) {
                        if (jsonData['result'] == '0') {
                            $('#totalAmount').text(jsonData['totalAmount']);
                            $('#totalIngAmount').text(jsonData['totalIngAmount']);
                            $('#totalPassAmount').text(jsonData['totalPassAmount']);
                            $('#remnantAmount').text(jsonData['remnantAmount']);
                        }else{
                            $('#totalAmount').text('00.00');
                            $('#totalIngAmount').text('00.00');
                            $('#totalPassAmount').text('00.00');
                            $('#remnantAmount').text('00.00');
                        }
                    }
                },
                error:function (jsonData) {

                }
            });
        }else{
        }
    }
    $(document).ready(function () {
        initValidator();
        loadBudgetAmount();
        $('#wfReqDaily\\.budgetYear').off('change').on('change', function () {
            loadBudgetAmount();
        });
        $.fn.zTree.init($("#treeDemo"), setting);
        var ue = UM.getEditor('editor', {
            lang:'zh-cn',
            langPath:UMEDITOR_CONFIG.UMEDITOR_HOME_URL + "lang/",
            focus: true
        });
        $('#nextBtn').off('click').on('click', function (e) {
            e.preventDefault();
            e.stopPropagation();
            var detailCount1 = $('.detailTr1').last().attr('seq');
            var detailCount2 = $('.detailTr2').last().attr('seq');
            if(detailCount1&&detailCount2){
                $('#detailCount1').val(detailCount1);
                $('#detailCount2').val(detailCount2);
                WEBUTILS.validator.checkAll();
                window.setTimeout(function () {
                    var passed = WEBUTILS.validator.isPassed();
                    $('#wfReqDaily\\.remarks').val(getContent());
                    if (passed) {
                        WEBUTILS.popWindow.createPopWindow(750, 535, '选择流程', '/wf/reqMyFlow!myFlowList.dhtml?applyId=${applyId?if_exists}');
                    } else {
                        WEBUTILS.validator.showErrors();
                    }
                }, 500);
            }
        });
        $('.addDetail').off('click').on('click', function (e) {
            e.preventDefault();
            e.stopPropagation();
            var type=$(this).attr('type');
            if(type){
                var currentNodeSeq = $('.detailTr'+type).last().attr('seq');
                if (currentNodeSeq) {
                    currentNodeSeq = parseInt(currentNodeSeq);
                    var nextNodeSeq = currentNodeSeq + 1;
                    $('.detailTr'+type).last().after(String.formatmodel(rePaymentDetailTrue, {seq: nextNodeSeq,type:type}));
                    WEBUTILS.validator.addMode({
                        id: 'amount' + nextNodeSeq+'_'+type,
                        required: true,
                        pattern: [
                            {type: 'number', exp: '==', msg: ''}
                        ]
                    });
                    WEBUTILS.validator.addMode({
                        id: 'date' + nextNodeSeq+'_'+type,
                        required: true,
                        pattern: [
                            {type: 'reg', exp: '_date', msg: ''}
                        ]
                    });
                    WEBUTILS.validator.addMode({
                        id: 'deptName' + nextNodeSeq+'_'+type,
                        required: true,
                        pattern: [
                            {type: 'blank', exp: '!=', msg: ''}
                        ]
                    });
                    bindSelect(type,nextNodeSeq);
                    dateEvent();
                    submited = false;
                }
            }
        });
        $('.deleteDetail').off('click').on('click', function (e) {
            e.preventDefault();
            e.stopPropagation();
            var type=$(this).attr('type');
            if(type){
                var currentNodeSeq = $('.detailTr'+type).last().attr('seq');
                if (currentNodeSeq) {
                    currentNodeSeq = parseInt(currentNodeSeq);
                    if (currentNodeSeq > 1) {
                        $('.detailTr'+type).last().remove();
                        WEBUTILS.validator.removeMode({
                            id: 'amount' + currentNodeSeq+'_'+type
                        });
                        WEBUTILS.validator.removeMode({
                            id: 'date' + currentNodeSeq+'_'+type
                        });
                        WEBUTILS.validator.removeMode({
                            id: 'deptName' + currentNodeSeq+'_'+type
                        });
                    }
                }
            }
        });
        $('#amount1_1').off('blur').on('blur',function(){
            var totalAm=0.00;
            $('.amt1').each(function(i,o){
                var am=$(o).val();
                if(am){
                    am=parseFloat(am);
                    if(am){
                        am=am.toFixed(2);
                        am=parseFloat(am);
                        totalAm+=am;
                    }
                }
            });
            $('#wfReqDaily\\.amount').val(totalAm);
        });
        $('#amount1_2').off('blur').on('blur',function(){
            var totalAm=0.00;
            $('.amt2').each(function(i,o){
                var am=$(o).val();
                if(am){
                    am=parseFloat(am);
                    if(am){
                        am=am.toFixed(2);
                        am=parseFloat(am);
                        totalAm+=am;
                    }
                }
            });
            $('#wfReqDaily\\.trueAmount').val(totalAm);
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
        $('.application').off('click').on('click', function (e) {
            if ($('.treeDiv').is(':visible')) {
                $('.treeDiv').fadeOut();
            }
        });

        $('#trueDetail').off('click').on('click', function (e) {
            if(confirm("此操作将返回费用报销业务,您这页的数据将会丢失,需要重新填写,是否继续?")){
                document.location.href='/wf/daily.dhtml';
            }
        });
    });
</script>
<!--搜索begin-->
<div class="r-top clearfix">
    <p class="text-info text-center lead">费用报销申请</p>
</div>
<!--搜索over-->
    <#if Session["userSession"]?exists>
        <#assign userInfo=Session["userSession"]?if_exists>
    <div class="mart5">
        <form class="form-horizontal" action="/wf/daily!saveTrue.dhtml" method="POST" name="editForm"
              id="editForm">
            <table class="table application nomar">
                <tbody>
                <tr>
                    <td colspan="2">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReq.subject"
                                   style="width: 60px;color: #898989;">标题</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <input style="width: 95%;" type="text" id="wfReq.subject" name="wfReq.subject"
                                       placeholder="请输入标题" maxlength="60">
                                <span class="help-inline"></span>
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
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${userInfo.userName?if_exists}</label>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label"
                                   style="width: 60px;color: #898989;">申请时间</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${sendDate?string("yyyy-MM-dd HH:mm:ss")}</label>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqDaily.payee"
                                   style="width: 60px;color: #898989;">收款单位</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <input style="width: 95%;" type="text" id="wfReqDaily.payee" name="wfReqDaily.payee"
                                       placeholder="收款单位/人" maxlength="60">
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqDaily.bank"
                                   style="width: 60px;color: #898989;">开户行</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <input type="text" id="wfReqDaily.bank" name="wfReqDaily.bank" placeholder="开户行" maxlength="60">
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqDaily.bankAccount"
                                   style="width: 60px;color: #898989;">帐号</label>
                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <input type="text" id="wfReqDaily.bankAccount" name="wfReqDaily.bankAccount" placeholder="银行帐号" maxlength="60">
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqDaily.payMethod"
                                   style="width: 60px;color: #898989;">支付方式</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;" >
                                <select class="int2 width-160" id="wfReqDaily.payMethod" name="wfReqDaily.payMethod">
                                    <option value="1">现金</option>
                                    <option value="2">银行转账</option>
                                    <option value="3">支票</option>
                                    <option value="4">帐扣</option>
                                </select>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqDaily.budgetYear"
                                   style="width: 60px;color: #898989;">预算年份</label>
                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <select name="wfReqDaily.budgetYear" id="wfReqDaily.budgetYear" class="int2 width-160">
                                    <#if yearList?exists&&yearList?size gt 0>
                                        <#list yearList as year>
                                            <option value="${year?c}" <#if currentYear==year>selected="selected" </#if> >${year?c}年</option>
                                        </#list>
                                    </#if>
                                </select>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td >
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqDaily.amount"
                                   style="width: 60px;color: #898989;">报销金额</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <input type="text" id="wfReqDaily.amount" name="wfReqDaily.amount"
                                       placeholder="报销金额" maxlength="20" readonly="readonly">
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqDaily.trueAmount"
                                   style="width: 60px;color: #898989;">实际金额</label>
                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <input type="text" id="wfReqDaily.trueAmount" name="wfReqDaily.trueAmount" placeholder="花费金额" maxlength="20" readonly="readonly">
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="border-top: 0px;">
                        <div style="width:790px;">
                            <script type="text/plain" id="editor" style="width:790px;height:200px;"></script>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <table class="table nomar">
                            <tbody><tr>
                                <td class="nopadding p-top5 "><span class="label label-info yearAmountTotal">预算总额：<span id="totalAmount">00.00</span></span></td>
                                <td class="nopadding p-top5"><span class="label label-success">已产生-已审批：<span id="totalPassAmount">00.00</span></span></td>
                                <td class="nopadding p-top5"><span class="label label-warning">已产生-待审批：<span id="totalIngAmount">00.00</span></span></td>
                                <td class="nopadding p-top5"><span class="label label-important">超出预算金额：<span id="remnantAmount">00.00</span></span></td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <table style="width: 100%;" id="type1"
                               class="layout table table-bordered table-hover tableBgColor nomar nopadding">
                            <thead>
                            <tr>
                                <td width="100">费用部门</td>
                                <td width="100">费用类型</td>
                                <td width="100">费用项目</td>
                                <td width="110">费用日期</td>
                                <td width="80">金额</td>
                                <td>
                                    <a href="##" type="1" class="addDetail"><i class="icon-plus"></i> 增加</a>
                                    <a href="##" type="1" class="deleteDetail"><i class="icon-minus"></i>
                                        删除</a>
                                    <a href="/wf/daily.dhtml" id="trueDetail" ><i class="icon-eye-close"></i>&nbsp;</a>

                                </td>
                            </tr>
                            </thead>
                            <tbody>
                            <tr seq="1" class="detailTr1" type="1">
                                <td>
                                    <div class="control-group" style="margin-bottom: 0px;">
                                        <input type="text" class="int1 width-100 dept" id="deptName1_1" name="deptName1_1" readonly="readonly" placeholder="选择部门">
                                        <input type="hidden"  id="deptId1_1" name="deptId1_1" >
                                    </div>
                                </td>
                                <td><select class="int2 width-100" id="typeId1_1" name="typeId1_1">
                                    <#if typeList?exists&&typeList?size gt 0>
                                        <#list typeList as type>
                                            <option value="${type.id?c}">
                                            ${type.expenseType?if_exists}
                                            </option>
                                        </#list>
                                    </#if>
                                </select></td>
                                <td>
                                    <select class="int2 width-100" id="titleId1_1" name="titleId1_1">
                                        <#if titleList?exists&&titleList?size gt 0>
                                            <#list titleList as title>
                                                <option value="${title.id?c}">
                                                ${title.titleName?if_exists}
                                                </option>
                                            </#list>
                                        </#if>
                                    </select>
                                    </td>
                                <td data-date-format="yyyy-mm-dd" data-date="" class="date dateTd">
                                    <div class="control-group" style="margin-bottom: 0px;">
                                        <input type="text" class="int1 width-100" id="date1_1" name="date1_1" >
                                    </div>
                                </td>
                                <td><input type="text" class="int1 width-70 amt1" id="amount1_1" name="amount1_1" value="0.00" style="ime-mode:disabled"></td>
                                <td><input type="text" class="int1 " style="width: 95%;" id="remarks1_1" name="remarks1_1"></td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <table style="width: 100%;" id="type2"
                               class="layout table table-bordered table-hover tableBgColor nomar nopadding">
                            <thead>
                            <tr>
                                <td width="100">费用部门</td>
                                <td width="100">费用类型</td>
                                <td width="100">费用项目</td>
                                <td width="110">费用日期</td>
                                <td width="80">金额</td>
                                <td>
                                    <a href="##" type="2" class="addDetail" ><i class="icon-plus"></i> 增加</a>
                                    <a href="##" type="2" class="deleteDetail" ><i class="icon-minus"></i>
                                        删除</a>
                                    <a href="##" id="trueDetail" ><i class="icon-eye-close"></i>&nbsp;</a>
                                </td>
                            </tr>
                            </thead>
                            <tbody>
                            <tr seq="1" class="detailTr2" type="2">
                                <td>
                                    <div class="control-group" style="margin-bottom: 0px;">
                                        <input type="text" class="int1 width-100 dept" id="deptName1_2" name="deptName1_2" readonly="readonly" placeholder="选择部门">
                                        <input type="hidden"  id="deptId1_2" name="deptId1_2" >
                                    </div>
                                </td>
                                <td><select class="int2 width-100" id="typeId1_2" name="typeId1_2">
                                    <#if typeList?exists&&typeList?size gt 0>
                                        <#list typeList as type>
                                            <option value="${type.id?c}">
                                            ${type.expenseType?if_exists}
                                            </option>
                                        </#list>
                                    </#if>
                                </select></td>
                                <td>
                                    <select class="int2 width-100" id="titleId1_2" name="titleId1_2">
                                        <#if titleList?exists&&titleList?size gt 0>
                                            <#list titleList as title>
                                                <option value="${title.id?c}">
                                                ${title.titleName?if_exists}
                                                </option>
                                            </#list>
                                        </#if>
                                    </select>
                                </td>
                                <td data-date-format="yyyy-mm-dd" data-date="" class="date dateTd">
                                    <div class="control-group" style="margin-bottom: 0px;">
                                        <input type="text" class="int1 width-100" id="date1_2" name="date1_2" >
                                    </div>
                                </td>
                                <td><input type="text" class="int1 width-70 amt2" id="amount1_2" name="amount1_2" value="0.00" style="ime-mode:disabled"></td>
                                <td><input type="text" class="int1 " style="width: 95%;" id="remarks1_2" name="remarks1_2"></td>
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
            <input type="hidden" name="detailCount1" id="detailCount1">
            <input type="hidden" name="detailCount2" id="detailCount2">
            <input type="hidden" name="wfReq.nodeCount" id="wfReq.nodeCount" value="0">
            <input type="hidden" name="wfReqDaily.remarks" id="wfReqDaily.remarks">
            <input type="hidden" name="attToken" id="attToken" value="<@c.tokenValue/>">
            <div id="nodeFlowHidden"></div>
        </form>
    </div>

    </#if>
</@wfCommon.wf_common>