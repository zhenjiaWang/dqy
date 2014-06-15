<#import "/view/template/structure/sale/saleCommon.ftl" as saleCommon>
<#import "/view/common/core.ftl" as c>
<@saleCommon.sale_common>
<link href="/css/datepicker.css" rel="stylesheet"/>
<link href="/css/validator/validator.css" rel="stylesheet"/>
<link href="/js/editor/themes/default/css/umeditor.min.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="/js/webutils/webutils.validator.js"></script>
<script type="text/javascript" src="/js/webutils/reg.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/editor/umeditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/editor/umeditor.min.js"></script>
<script type="text/javascript" src="/js/editor/lang/zh-cn/zh-cn.js"></script>
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
                    id: 'wfReqSale\\.amount',
                    required: true,
                    pattern: [
                        {type: 'number', exp: '==', msg: '不能为空'}
                    ]
                },
                {
                    id: 'productAmount1',
                    required: true,
                    pattern: [
                        {type: 'number', exp: '==', msg: '不能为空'}
                    ]
                },
                {
                    id: 'wfReqSale\\.startDate',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'}
                    ]
                },
                {
                    id: 'wfReqSale\\.endDate',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'}
                    ]
                },
                {
                    id: 'wfReqSale\\.payDate',
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

    function getContent() {
        return UM.getEditor('editor').getContent();
    }
    function hasContent() {
        return UM.getEditor('editor').hasContents();
    }


    function loadDept(){
        var channelId=$('#wfReqSale\\.channelId\\.id').val();
        if(channelId){
            $.ajax({
                type:'GET',
                url:'/common/common!searchDept.dhtml?id=' + channelId,
                dataType:'json',
                success:function (jsonData) {
                    if (jsonData) {
                        if (jsonData['result'] == '0') {
                           var deptList=jsonData['deptList'];
                           if(deptList){
                               var deptObj=$('#wfReqSale\\.deptId\\.id');
                               if(deptObj){
                                   $(deptObj).empty();
                                   $(deptList).each(function(i,o){
                                       $(deptObj).append('<option value="'+o['id']+'">'+o['name']+'</option>');
                                   });
                                   window.setTimeout(function(){
                                       loadSystem();
                                   },500);

                               }
                           }
                        }
                    }
                },
                error:function (jsonData) {

                }
            });
        }
    }
    function loadSystem(){
        var deptId=$('#wfReqSale\\.deptId\\.id').val();
        if(deptId){
            $.ajax({
                type:'GET',
                url:'/common/common!searchSystem.dhtml?id=' + deptId,
                dataType:'json',
                success:function (jsonData) {
                    if (jsonData) {
                        if (jsonData['result'] == '0') {
                            var systemList=jsonData['systemList'];
                            if(systemList){
                                var systemObj=$('#wfReqSale\\.systemId\\.id');
                                if(systemObj){
                                    $(systemObj).empty();
                                    $(systemList).each(function(i,o){
                                        $(systemObj).append('<option value="'+o['id']+'">'+o['name']+'</option>');
                                    });
                                    window.setTimeout(function(){
                                        loadCustomer();
                                    },1000);
                                }
                            }
                        }
                    }
                },
                error:function (jsonData) {

                }
            });
        }
    }
    function loadCustomer(){
        var systemId=$('#wfReqSale\\.systemId\\.id').val();
        if(systemId){
            $.ajax({
                type:'GET',
                url:'/common/common!searchCustomer.dhtml?id=' + systemId,
                dataType:'json',
                success:function (jsonData) {
                    if (jsonData) {
                        if (jsonData['result'] == '0') {
                            var customerList=jsonData['customerList'];
                            if(customerList){
                                var customerObj=$('#wfReqSale\\.customerId\\.id');
                                if(customerObj){
                                    $(customerObj).empty();
                                    $(customerList).each(function(i,o){
                                        $(customerObj).append('<option value="'+o['id']+'">'+o['name']+'</option>');
                                    });
                                }
                            }
                        }
                    }
                },
                error:function (jsonData) {

                }
            });
        }
    }

    function loadProduct(seriesObj,productObj){
        if(seriesObj&&productObj){
            $(seriesObj).off('change').on('change', function () {
                var seriesId=$(this).val();
                if(seriesId){
                    $.ajax({
                        type:'GET',
                        url:'/common/common!searchProduct.dhtml?id=' + seriesId,
                        dataType:'json',
                        success:function (jsonData) {
                            if (jsonData) {
                                if (jsonData['result'] == '0') {
                                    var productList=jsonData['productList'];
                                    if(productList){
                                        if(productObj){
                                            $(productObj).empty();
                                            $(productList).each(function(i,o){
                                                $(productObj).append('<option value="'+o['id']+'">('+o['code']+')&nbsp;&nbsp;&nbsp;'+o['name']+'</option>');
                                            });
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
        }
    }
    function loadBudgetAmount() {
        var budgetYear=$('#wfReqSale\\.budgetYear').val();
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
    function bindSelect(seq){
        <#if seriesList?exists&&seriesList?size gt 0>
            $('#seriesId'+seq).empty();
            <#list seriesList as series>
                $('#seriesId'+seq).append('<option value="${(series.id)?c}">${(series.seriesName)?if_exists}</option>');
            </#list>
        </#if>
        <#if productList?exists&&productList?size gt 0>
            $('#productId'+seq).empty();
            <#list productList as product>
                $('#productId'+seq).append('<option value="${product.id?c}">(${product.productCode?if_exists})&nbsp;&nbsp;&nbsp;${product.productName?if_exists}</option>');
            </#list>
        </#if>
        $('#productAmount'+seq).off('blur').on('blur',function(){
            var totalAm=0.00;
            $('.amt','.detailTr').each(function(i,o){
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
            $('#wfReqSale\\.amount').val(totalAm);
        });
    }

    function bindSelectTrue(seq){

        <#if typeList?exists&&typeList?size gt 0>
            $('#expType'+seq).empty();
            <#list typeList as type>
                $('#expType'+seq).append('<option value="${(type.id)?c}">${(type.expenseType)?if_exists}</option>');
            </#list>
        </#if>

        <#if titleList?exists&&titleList?size gt 0>
            $('#expTitle'+seq).empty();
            <#list titleList as title>
                $('#expTitle'+seq).append('<option value="${title.id?c}">${title.titleName?if_exists}</option>');
            </#list>
        </#if>
        $('#productAmount1'+seq).off('blur').on('blur',function(){
            var totalAm=0.00;
            $('.amt','.detailTr1').each(function(i,o){
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
            $('#wfReqSale\\.trueAmount').val(totalAm);
        });
    }
    $(document).ready(function () {
        initValidator();
        loadBudgetAmount();
        var ue = UM.getEditor('editor', {
            lang:'zh-cn',
            langPath:UMEDITOR_CONFIG.UMEDITOR_HOME_URL + "lang/",
            focus: true
        });
        $('#wfReqSale\\.channelId\\.id').off('change').on('change', function () {
            loadDept();
        });
        $('#wfReqSale\\.deptId\\.id').off('change').on('change', function () {
            loadSystem();
        });
        $('#wfReqSale\\.systemId\\.id').off('change').on('change', function () {
            loadCustomer();
        });
        loadProduct($('#seriesId1'),$('#productId1'));
        $('#wfReqSale\\.budgetYear').off('change').on('change', function () {
            loadBudgetAmount();
        });
        $('#nextBtn').off('click').on('click', function () {
            WEBUTILS.validator.checkAll();
            var detailCount = $('.detailTr').last().attr('seq');
            var detailCount1 = $('.detailTr1').last().attr('seq');
            if(detailCount&&detailCount1){
                $('#detailCount').val(detailCount);
                $('#detailCount1').val(detailCount1);
                window.setTimeout(function () {
                    var passed = WEBUTILS.validator.isPassed();
                    $('#wfReqSale\\.remarks').val(getContent());
                    if (passed) {
                        WEBUTILS.popWindow.createPopWindow(750, 535, '选择流程', '/wf/reqMyFlow!myFlowList.dhtml?applyId=${applyId?if_exists}');
                    } else {
                        WEBUTILS.validator.showErrors();
                    }
                }, 500);
            }
        });

        $('#productAmount1').off('blur').on('blur',function(){
            var totalAm=0.00;
            $('.amt','.detailTr').each(function(i,o){
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
            $('#wfReqSale\\.amount').val(totalAm);
        });

        $('#productAmount11').off('blur').on('blur',function(){
            var totalAm=0.00;
            $('.amt','.detailTr1').each(function(i,o){
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
            $('#wfReqSale\\.trueAmount').val(totalAm);
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

        $('#addDetail').off('click').on('click', function (e) {
            e.preventDefault();
            e.stopPropagation();
            var currentNodeSeq = $('.detailTr').last().attr('seq');
            if (currentNodeSeq) {
                currentNodeSeq = parseInt(currentNodeSeq);
                var nextNodeSeq = currentNodeSeq + 1;
                $('.detailTr').last().after(String.formatmodel(saleDetail, {seq: nextNodeSeq}));
                WEBUTILS.validator.addMode({
                    id: 'productAmount' + nextNodeSeq,
                    required: true,
                    pattern: [
                        {type: 'number', exp: '==', msg: ''}
                    ]
                });
                bindSelect(nextNodeSeq);
                loadProduct($('#seriesId'+nextNodeSeq),$('#productId'+nextNodeSeq));
                submited = false;
            }
        });
        $('#deleteDetail').off('click').on('click', function (e) {
            e.preventDefault();
            e.stopPropagation();
            var currentNodeSeq = $('.detailTr').last().attr('seq');
            if (currentNodeSeq) {
                currentNodeSeq = parseInt(currentNodeSeq);
                if (currentNodeSeq > 1) {
                    $('.detailTr').last().remove();
                    WEBUTILS.validator.removeMode({
                        id: 'productAmount' + currentNodeSeq
                    });
                }
            }
        });

        $('#addDetail1').off('click').on('click', function (e) {
            e.preventDefault();
            e.stopPropagation();
            var currentNodeSeq = $('.detailTr1').last().attr('seq');
            if (currentNodeSeq) {
                currentNodeSeq = parseInt(currentNodeSeq);
                var nextNodeSeq = currentNodeSeq + 1;
                $('.detailTr1').last().after(String.formatmodel(saleDetailTrue, {seq: nextNodeSeq}));
                WEBUTILS.validator.addMode({
                    id: 'productAmount1' + nextNodeSeq,
                    required: true,
                    pattern: [
                        {type: 'number', exp: '==', msg: ''}
                    ]
                });
                bindSelectTrue(nextNodeSeq);
                submited = false;
            }
        });
        $('#deleteDetail1').off('click').on('click', function (e) {
            e.preventDefault();
            e.stopPropagation();
            var currentNodeSeq = $('.detailTr1').last().attr('seq');
            if (currentNodeSeq) {
                currentNodeSeq = parseInt(currentNodeSeq);
                if (currentNodeSeq > 1) {
                    $('.detailTr1').last().remove();
                    WEBUTILS.validator.removeMode({
                        id: 'productAmount1' + currentNodeSeq
                    });
                }
            }
        });
        $('#trueDetail').off('click').on('click', function (e) {
            if(confirm("此操作将返回费用报销业务,您这页的数据将会丢失,需要重新填写,是否继续?")){
                document.location.href='/wf/sale.dhtml';
            }
        });

        $('#wfReqSale\\.payMethod').change(function () {
            var payMethod = $('#wfReqSale\\.payMethod').val();
            if (payMethod == "2") {
                $('.bank').show();
                WEBUTILS.validator.addMode({
                    id: 'wfReqSale\\.payee',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: ''}
                    ]
                });
                WEBUTILS.validator.addMode({
                    id: 'wfReqSale\\.bank',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: ''}
                    ]
                });
                WEBUTILS.validator.addMode({
                    id: 'wfReqSale\\.bankAccount',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: ''}
                    ]
                });
            } else {
                $('.bank').hide();
                WEBUTILS.validator.removeMode({
                    id: 'wfReqSale\\.payee'
                });
                WEBUTILS.validator.removeMode({
                    id: 'wfReqSale\\.bank'
                });
                WEBUTILS.validator.removeMode({
                    id: 'wfReqSale\\.bankAccount'
                });
            }
        });
    });
</script>
<!--搜索begin-->
<div class="r-top clearfix">
    <p class="text-info text-center lead">销售费用报销申请单</p>
</div>
<!--搜索over-->
    <#if Session["userSession"]?exists>
        <#assign userInfo=Session["userSession"]?if_exists>
    <div class="mart5">
        <form class="form-horizontal" action="/wf/sale!saveTrue.dhtml" method="POST" name="editForm"
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
                    <td >
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.amount"
                                   style="width: 60px;color: #898989;">报销金额</label>
                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <input type="text" onfocus=" this.style.imeMode='disabled' "  id="wfReqSale.amount" name="wfReqSale.amount" placeholder="报销金额" maxlength="10" readonly>
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.trueAmount"
                                   style="width: 60px;color: #898989;">实际金额</label>
                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <input type="text" onfocus=" this.style.imeMode='disabled' "  id="wfReqSale.trueAmount" name="wfReqSale.trueAmount" placeholder="实际金额" maxlength="10" readonly>
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.payMethod"
                                   style="width: 60px;color: #898989;">支付方式</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <select class="int2 width-160" id="wfReqSale.payMethod"
                                        name="wfReqSale.payMethod">
                                    <option value="1">现金</option>
                                    <option value="2">银行转账</option>
                                    <option value="3">支票</option>
                                    <option value="4">帐扣</option>
                                </select>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr class="bank" style="display: none;">
                    <td colspan="2">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.payee"
                                   style="width: 60px;color: #898989;">收款单位</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <input style="width: 95%;" type="text" id="wfReqSale.payee" name="wfReqSale.payee"
                                       placeholder="收款单位/人" maxlength="60">
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr class="bank" style="display: none;">
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.bank"
                                   style="width: 60px;color: #898989;">开户行</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <input type="text" id="wfReqSale.bank" name="wfReqSale.bank" placeholder="开户行"
                                       maxlength="60">
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.bankAccount"
                                   style="width: 60px;color: #898989;">帐号</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <input type="text" id="wfReqSale.bankAccount" name="wfReqSale.bankAccount"
                                       placeholder="银行帐号" maxlength="60">
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td data-date-format="yyyy-mm-dd" data-date="" class="date dateTd">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.startDate"
                                   style="width: 60px;color: #898989;">开始日期</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <input type="text" class="int1 " id="wfReqSale.startDate" name="wfReqSale.startDate" >
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                    <td data-date-format="yyyy-mm-dd" data-date="" class="date dateTd">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.endDate"
                                   style="width: 60px;color: #898989;">结束日期</label>
                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <input type="text" class="int1 " id="wfReqSale.endDate" name="wfReqSale.endDate" >
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td data-date-format="yyyy-mm-dd" data-date="" class="date dateTd" >
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.payDate"
                                   style="width: 60px;color: #898989;">核销日期</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <input type="text" class="int1 " id="wfReqSale.payDate" name="wfReqSale.payDate" >
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.budgetYear"
                                   style="width: 60px;color: #898989;">预算年份</label>
                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <select name="wfReqSale.budgetYear" id="wfReqSale.budgetYear" class="int2 width-160">
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
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.expenseType.id"
                                   style="width: 60px;color: #898989;">费用类别</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;" >
                                <select class="int2 width-160" id="wfReqSale.expenseType.id" name="wfReqSale.expenseType.id">
                                    <#if typeList?exists&&typeList?size gt 0>
                                        <#list typeList as type>
                                        <option value="${type.id?c}">${type.expenseType?if_exists}</option>
                                        </#list>
                                    </#if>
                                </select>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.expenseTitle.id"
                                   style="width: 60px;color: #898989;">费用项目</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;" >
                                <select class="int2 width-160" id="wfReqSale.expenseTitle.id" name="wfReqSale.expenseTitle.id">
                                    <#if titleList?exists&&titleList?size gt 0>
                                        <#list titleList as title>
                                            <option value="${title.id?c}">${title.titleName?if_exists}</option>
                                        </#list>
                                    </#if>
                                </select>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.channelId.id"
                                   style="width: 60px;color: #898989;">渠道</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;" >
                                <select class="int2 width-160" id="wfReqSale.channelId.id" name="wfReqSale.channelId.id">
                                    <#if channelList?exists&&channelList?size gt 0>
                                        <#list channelList as channel>
                                            <option value="${channel.id?c}">${channel.channelName?if_exists}</option>
                                        </#list>
                                    </#if>
                                </select>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.deptId.id"
                                   style="width: 60px;color: #898989;">业态/部门</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;" >
                                <select class="int2 width-160" id="wfReqSale.deptId.id" name="wfReqSale.deptId.id">
                                    <#if deptList?exists&&deptList?size gt 0>
                                        <#list deptList as dept>
                                            <option value="${dept.id?c}">${dept.deptName?if_exists}</option>
                                        </#list>
                                    </#if>
                                </select>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.systemId.id"
                                   style="width: 60px;color: #898989;">系统</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;" >
                                <select class="int2 width-160" id="wfReqSale.systemId.id" name="wfReqSale.systemId.id">
                                    <#if deptSystemList?exists&&deptSystemList?size gt 0>
                                        <#list deptSystemList as deptSystem>
                                            <option value="${(deptSystem.systemId.id)?c}">${(deptSystem.systemId.systemName)?if_exists}</option>
                                        </#list>
                                    </#if>
                                </select>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.customerId.id"
                                   style="width: 60px;color: #898989;">门店</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;" >
                                <select class="int2" id="wfReqSale.customerId.id" name="wfReqSale.customerId.id" style="width: 250px;">
                                    <#if customerList?exists&&customerList?size gt 0>
                                        <#list customerList as customer>
                                            <option value="${customer.id?c}">${customer.customerName?if_exists}</option>
                                        </#list>
                                    </#if>
                                </select>
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
                        <table style="width: 100%;"
                               class="layout table table-bordered table-hover tableBgColor nomar nopadding">
                            <thead>
                            <tr>
                                <td width="80">金额</td>
                                <td width="160">品类/系列</td>
                                <td width="250">单品</td>
                                <td>
                                    <a href="##" id="addDetail" ><i class="icon-plus"></i> 增加</a>
                                    <a href="##" id="deleteDetail" ><i class="icon-minus"></i>
                                        删除</a>
                                    <a href="##" id="trueDetail" ><i class="icon-eye-close"></i>&nbsp;</a>
                                </td>
                            </tr>
                            </thead>
                            <tbody>
                            <tr seq="1" class="detailTr">
                                <td><input type="text" class="int1 width-70 amt" id="productAmount1" name="productAmount1" value="0.00" style="ime-mode:disabled"></td>
                                <td>
                                    <select class="int2 width-160" id="seriesId1" name="seriesId1">
                                        <#if seriesList?exists&&seriesList?size gt 0>
                                            <#list seriesList as series>
                                                <option value="${(series.id)?c}">${(series.seriesName)?if_exists}</option>
                                            </#list>
                                        </#if>
                                    </select>
                                </td>
                                <td colspan="2">
                                    <select class="int2" id="productId1" name="productId1" style="width: 480px;">
                                        <#if productList?exists&&productList?size gt 0>
                                            <#list productList as product>
                                                <option value="${product.id?c}">(${product.productCode?if_exists})&nbsp;&nbsp;&nbsp;${product.productName?if_exists}</option>
                                            </#list>
                                        </#if>
                                    </select>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <table style="width: 100%;"
                               class="layout table table-bordered table-hover tableBgColor nomar nopadding">
                            <thead>
                            <tr>
                                <td width="80">金额</td>
                                <td width="160">费用类别</td>
                                <td width="250">费用项目</td>
                                <td>
                                    <a href="##" id="addDetail1" ><i class="icon-plus"></i> 增加</a>
                                    <a href="##" id="deleteDetail1" ><i class="icon-minus"></i>
                                        删除</a>
                                </td>
                            </tr>
                            </thead>
                            <tbody>
                            <tr seq="1" class="detailTr1">
                                <td><input type="text" class="int1 width-70 amt" id="productAmount11" name="productAmount11" value="0.00" style="ime-mode:disabled"></td>
                                <td>
                                    <select class="int2 width-160" id="expType1" name="expType1">
                                        <#if typeList?exists&&typeList?size gt 0>
                                            <#list typeList as type>
                                                <option value="${type.id?c}">${type.expenseType?if_exists}</option>
                                            </#list>
                                        </#if>
                                    </select>
                                </td>
                                <td colspan="2">
                                    <select class="int2" id="expTitle1" name="expTitle1" style="width: 480px;">
                                        <#if titleList?exists&&titleList?size gt 0>
                                            <#list titleList as title>
                                                <option value="${title.id?c}">${title.titleName?if_exists}</option>
                                            </#list>
                                        </#if>
                                    </select>
                                </td>
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
            <input type="hidden" name="wfReqSale.remarks" id="wfReqSale.remarks">
            <input type="hidden" name="attToken" id="attToken" value="<@c.tokenValue/>">
            <input type="hidden" name="wfReq.nodeCount" id="wfReq.nodeCount" value="0">
            <input type="hidden" name="detailCount" id="detailCount">
            <input type="hidden" name="detailCount1" id="detailCount1">
            <div id="nodeFlowHidden"></div>
        </form>
    </div>

    </#if>
</@saleCommon.sale_common>