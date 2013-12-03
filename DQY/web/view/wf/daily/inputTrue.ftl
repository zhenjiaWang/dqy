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
    var uploadRemove=false;
    var tempTitleName = false;
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
        <#if departmentList?exists&&departmentList?size gt 0>
            $('#deptId'+seq+'_'+type).empty();
            <#list departmentList as dept>
                var levelStr='';
                <#if dept.deptLevel gt 1>
                    <#list 1..dept.deptLevel as i>
                        levelStr+='&nbsp;&nbsp;';
                    </#list>
                </#if>
                $('#deptId'+seq+'_'+type).append('<option value="${dept.id?c}">'+levelStr+'${dept.deptName?if_exists}</option>');
            </#list>
            $('#deptId'+seq+'_'+type).val('${deptId?c}');
        </#if>
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

        $('.titleNo').off('keydown').on('keydown', function (e) {
            var titleNoObj = $(this);
            tempTitleName = $(titleNoObj).val();
        });

        $('.titleNo').off('keyup').on('keyup', function (e) {
            var titleNoObj = $(this);
            e = (e) ? e : ((window.event) ? window.event : "");
            var keyCode = e.keyCode ? e.keyCode : (e.which ? e.which : e.charCode);
            var searchKey = $(this).val();
            if (keyCode == 8 || keyCode == 46) {
                var seq = $(titleNoObj).attr('seq');
                if (seq) {
                    if ($('#titleId' + seq).val() != '') {
                        if (confirm("确认清空当前费用项目?")) {
                            $('#titleNo' + seq).val('');
                            $('#titleId' + seq).val('');
                        } else {
                            if (tempTitleName) {
                                $('#titleNo' + seq).val(tempTitleName);
                                tempTitleName = false;
                            }
                        }
                    }
                }
            }
            if (keyCode == 13) {
                if (searchKey && searchKey != '') {
                    searchKey = encodeURI(searchKey);
                    if ($('.titlepop').is(':visible')) {
                        var li = $('.active', '.titlepop');
                        if (li) {
                            var seq = $(titleNoObj).attr('seq');
                            if (seq) {
                                $('#titleNo' + seq).val($(li).attr('titleName'));
                                $('#titleId' + seq).val($(li).attr('uid'));
                            }
                        }
                        $('.titlepop').hide();
                    } else {
                        $.ajax({
                            type: 'GET',
                            url: '/common/common!searchBudgetTitle.dhtml?searchKey=' + searchKey,
                            dataType: 'json',
                            success: function (jsonData) {
                                if (jsonData) {
                                    if (jsonData['result'] == '0') {
                                        $('ul', '.titlepop').empty();
                                        var titleList = jsonData['titleList'];
                                        if (titleList) {
                                            $(titleList).each(function (i, o) {
                                                $('ul', '.titlepop').append('<li  uid="' + o['id'] + '" titleName="' + o["titleName"] + '"><a href="##">(' + o["titleNo"] + ')' + o["titleName"] + '</a></li>');
                                            });
                                            var left = $(titleNoObj).offset().left;
                                            var top = $(titleNoObj).offset().top + 30;
                                            $('.titlepop').css({left: left + 'px', top: top + 'px'}).show();
                                            $('li', '.titlepop').first().addClass('active');
                                            $('li', '.titlepop').off('click').on('click',function(){
                                                if ($('.titlepop').is(':visible')) {
                                                    var li = $(this);
                                                    if (li) {
                                                        var seq = $(titleNoObj).attr('seq');
                                                        if (seq) {
                                                            $('#titleNo' + seq).val($(li).attr('titleName'));
                                                            $('#titleId' + seq).val($(li).attr('uid'));
                                                        }
                                                    }
                                                    $('.titlepop').hide();
                                                }
                                            });
                                        } else {
                                            alert('暂无结果,请重新输入');
                                        }
                                    } else {
                                        alert('暂无结果,请重新输入');
                                    }
                                }
                            },
                            error: function (jsonData) {

                            }
                        });
                    }
                }
            } else {
                var searchCount = $('li', '.titlepop').size();
                if (keyCode == 38) {
                    //up
                    var li = $('.active', '.titlepop');
                    var currentIndex = $(li).index();
                    $('li', '.titlepop').removeClass('active');
                    if (currentIndex == 0) {
                        $('li', '.titlepop').last().addClass('active');
                    } else {
                        $(li).prev().addClass('active');
                    }
                } else if (keyCode == 40) {
                    //down
                    var li = $('.active', '.titlepop');
                    var currentIndex = $(li).index();
                    $('li', '.titlepop').removeClass('active');
                    if (currentIndex == searchCount - 1) {
                        $('li', '.titlepop').first().addClass('active');
                    } else {
                        $(li).next().addClass('active');
                    }
                } else if (keyCode == 13) {

                }
            }
        });
    }

    function getContent() {
        return UM.getEditor('editor').getContent();
    }
    function hasContent() {
        return UM.getEditor('editor').hasContents();
    }
    $(document).ready(function () {
        initValidator();
        var ue = UM.getEditor('editor', {
            lang:'zh-cn',
            langPath:UMEDITOR_CONFIG.UMEDITOR_HOME_URL + "lang/",
            focus: true
        });
        $('#nextBtn').off('click').on('click', function () {
            var titleIdFlag=true;
            $('.titleNo').each(function(i,o){
                var seq=$(o).attr('seq');
                if(seq){
                    if($('#titleId'+seq).val()==''){
                        titleIdFlag=false;
                    }
                }
            });
            if(!titleIdFlag){
                alert('请确认每一项费用项目都进行填写');
                return false;
            }
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
        $('.addDetail').off('click').on('click', function () {
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
                    bindSelect(type,nextNodeSeq);
                    dateEvent();
                    submited = false;
                }
            }
        });
        $('.deleteDetail').off('click').on('click', function () {
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
                                   style="width: 60px;color: #898989;">申请人</label>

                            <div class="controls" style="margin-left: 70px;">
                                <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${userInfo.userName?if_exists}</label>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label"
                                   style="width: 60px;color: #898989;">申请时间</label>

                            <div class="controls" style="margin-left: 70px;">
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
                                   style="width: 60px;color: #898989;">开户行</label>

                            <div class="controls" style="margin-left: 70px;">
                                <input type="text" id="wfReqDaily.bank" name="wfReqDaily.bank" placeholder="开户行" maxlength="20">
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqDaily.bankAccount"
                                   style="width: 60px;color: #898989;">帐号</label>
                            <div class="controls" style="margin-left: 70px;">
                                <input type="text" id="wfReqDaily.bankAccount" name="wfReqDaily.bankAccount" placeholder="银行帐号" maxlength="20">
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td >
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqDaily.amount"
                                   style="width: 60px;color: #898989;">报销金额</label>

                            <div class="controls" style="margin-left: 70px;">
                                <input type="text" id="wfReqDaily.amount" name="wfReqDaily.amount"
                                       placeholder="报销金额" maxlength="10" readonly="readonly">
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqDaily.trueAmount"
                                   style="width: 60px;color: #898989;">实际金额</label>
                            <div class="controls" style="margin-left: 70px;">
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
                                <td class="nopadding p-top5 "><span class="label label-info yearAmountTotal">预算总额：${totalAmount?if_exists}</span></td>
                                <td class="nopadding p-top5"><span class="label label-success">已产生-已审批：${totalPassAmount?if_exists}</span></td>
                                <td class="nopadding p-top5"><span class="label label-warning">已产生-待审批：${totalIngAmount?if_exists}</span></td>
                                <td class="nopadding p-top5"><span class="label label-important">超出预算金额：${remnantAmount?if_exists}</span></td>
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
                                    <a href="/wf/daily.dhtml" id="trueDetail" style="float: right;"><i class="icon-eye-close"></i>&nbsp;</a>
                                    <a href="#" type="1" class="deleteDetail" style="float: right;"><i class="icon-minus"></i>
                                        删除</a>
                                    <a href="#" type="1" class="addDetail" style="float: right;"><i class="icon-plus"></i> 增加</a>
                                </td>
                            </tr>
                            </thead>
                            <tbody>
                            <tr seq="1" class="detailTr1">
                                <td><select class="int2 width-100" id="deptId1_1" name="deptId1_1">
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
                                    <input type="text" class="int1 width-100 titleNo" id="titleNo1_1" name="titleNo1_1"
                                           placeholder="费用项目" maxlength="20" seq="1_1">
                                    <input type="hidden" class="int1 width-100" id="titleId1_1" name="titleId1_1">

                                    </td>
                                <td data-date-format="yyyy-mm-dd" data-date="" class="date dateTd">
                                    <div class="control-group" style="margin-bottom: 0px;">
                                        <input type="text" class="int1 width-100" id="date1_1" name="date1_1" >
                                    </div>
                                </td>
                                <td><input type="text" class="int1 width-70 amt1" id="amount1_1" name="amount1_1" value="0.00"></td>
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
                                    <a href="/wf/daily.dhtml" id="trueDetail" style="float: right;"><i class="icon-eye-close"></i>&nbsp;</a>
                                    <a href="#" type="2" class="deleteDetail" style="float: right;"><i class="icon-minus"></i>
                                        删除</a>
                                    <a href="#" type="2" class="addDetail" style="float: right;"><i class="icon-plus"></i> 增加</a>
                                </td>
                            </tr>
                            </thead>
                            <tbody>
                            <tr seq="1" class="detailTr2">
                                <td><select class="int2 width-100" id="deptId1_2" name="deptId1_2">
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
                                    <input type="text" class="int1 width-100 titleNo" id="titleNo1_2" name="titleNo1_2"
                                           placeholder="费用项目" maxlength="20" seq="1_2">
                                    <input type="hidden" class="int1 width-100" id="titleId1_2" name="titleId1_2">
                                </td>
                                <td data-date-format="yyyy-mm-dd" data-date="" class="date dateTd">
                                    <div class="control-group" style="margin-bottom: 0px;">
                                        <input type="text" class="int1 width-100" id="date1_2" name="date1_2" >
                                    </div>
                                </td>
                                <td><input type="text" class="int1 width-70 amt2" id="amount1_2" name="amount1_2" value="0.00"></td>
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
            <div id="nodeFlowHidden"></div>
        </form>
    </div>

    </#if>
</@wfCommon.wf_common>