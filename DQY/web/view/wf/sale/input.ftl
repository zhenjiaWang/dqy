<#import "/view/template/structure/sale/saleCommon.ftl" as saleCommon>
<#import "/view/common/core.ftl" as c>
<@saleCommon.sale_common>
<link href="/css/validator/validator.css" rel="stylesheet"/>
<link href="/js/editor/themes/default/css/umeditor.min.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="/js/webutils/webutils.validator.js"></script>
<script type="text/javascript" src="/js/webutils/reg.js"></script>
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

    function loadProduct(){
        var seriesId=$('#wfReqSale\\.seriesId\\.id').val();
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
                                var productObj=$('#wfReqSale\\.productId\\.id');
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
        $('#wfReqSale\\.seriesId\\.id').off('change').on('change', function () {
            loadProduct();
        });
        $('#wfReqSale\\.budgetYear').off('change').on('change', function () {
            loadBudgetAmount();
        });
        $('#nextBtn').off('click').on('click', function () {
            WEBUTILS.validator.checkAll();
            window.setTimeout(function () {
                var passed = WEBUTILS.validator.isPassed();
                $('#wfReqSale\\.remarks').val(getContent());
                if (passed) {
                    WEBUTILS.popWindow.createPopWindow(750, 535, '选择流程', '/wf/reqMyFlow!myFlowList.dhtml?applyId=${applyId?if_exists}');
                } else {
                    WEBUTILS.validator.showErrors();
                }
            }, 500);
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
        <form class="form-horizontal" action="/wf/sale!save.dhtml" method="POST" name="editForm"
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
                                <input type="text" onfocus=" this.style.imeMode='disabled' "  id="wfReqSale.amount" name="wfReqSale.amount" placeholder="报销金额" maxlength="10">
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
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.seriesId.id"
                                   style="width: 60px;color: #898989;">品类/系列</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;" >
                                <select class="int2 width-160" id="wfReqSale.seriesId.id" name="wfReqSale.seriesId.id">
                                    <#if seriesList?exists&&seriesList?size gt 0>
                                        <#list seriesList as series>
                                            <option value="${(series.id)?c}">${(series.seriesName)?if_exists}</option>
                                        </#list>
                                    </#if>
                                </select>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqSale.productId.id"
                                   style="width: 60px;color: #898989;">单品</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;" >
                                <select class="int2" id="wfReqSale.productId.id" name="wfReqSale.productId.id" style="width: 250px;">
                                    <#if productList?exists&&productList?size gt 0>
                                        <#list productList as product>
                                            <option value="${product.id?c}">(${product.productCode?if_exists})&nbsp;&nbsp;&nbsp;${product.productName?if_exists}</option>
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
            <div id="nodeFlowHidden"></div>
        </form>
    </div>

    </#if>
</@saleCommon.sale_common>