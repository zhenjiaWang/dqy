<#import "/view/template/structure/wf/wfCommon.ftl" as wfCommon>
<#import "/view/common/core.ftl" as c>
<@wfCommon.wf_common>
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
                    id: 'wfReqAdvanceAccount\\.amount',
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


    $(document).ready(function () {
        initValidator();
        var ue = UM.getEditor('editor', {
            lang:'zh-cn',
            langPath:UMEDITOR_CONFIG.UMEDITOR_HOME_URL + "lang/",
            focus: true
        });
        $('#wfReqAdvanceAccount\\.payMethod').change(function(){
            var payMethod=$('#wfReqAdvanceAccount\\.payMethod').val();
            if(payMethod=="2"){
                $('.bank').show();
                WEBUTILS.validator.addMode({
                    id: 'wfReqAdvanceAccount\\.payee',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: ''}
                    ]
                });
            }else{
                $('.bank').hide();
                WEBUTILS.validator.removeMode({
                    id: 'wfReqAdvanceAccount\\.payee'
                });
            }
        });
        $('#nextBtn').off('click').on('click', function () {
            WEBUTILS.validator.checkAll();
            window.setTimeout(function () {
                var passed = WEBUTILS.validator.isPassed();
                $('#wfReqAdvanceAccount\\.remarks').val(getContent());
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
    <p class="text-info text-center lead">预支申请单</p>
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
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqAdvanceAccount.amount"
                                   style="width: 60px;color: #898989;">预支金额</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <input type="text" onfocus=" this.style.imeMode='disabled' "  id="wfReqAdvanceAccount.amount" name="wfReqAdvanceAccount.amount" placeholder="预支金额" maxlength="10">
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqAdvanceAccount.payMethod"
                                   style="width: 60px;color: #898989;">支付方式</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;" >
                                <select class="int2 width-160" id="wfReqAdvanceAccount.payMethod" name="wfReqAdvanceAccount.payMethod">
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
                            <label class="control-label" for="wfReqAdvanceAccount.payee"
                                   style="width: 60px;color: #898989;">收款单位</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <input style="width: 95%;" type="text" id="wfReqAdvanceAccount.payee" name="wfReqAdvanceAccount.payee"
                                       placeholder="收款单位/人" maxlength="60">
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr class="bank" style="display: none;">
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqAdvanceAccount.bank"
                                   style="width: 60px;color: #898989;">开户行</label>

                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <input type="text" id="wfReqAdvanceAccount.bank" name="wfReqAdvanceAccount.bank" placeholder="开户行" maxlength="60">
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="wfReqAdvanceAccount.bankAccount"
                                   style="width: 60px;color: #898989;">帐号</label>
                            <div class="controls" style="margin-left: 70px;*margin-left:0;">
                                <input type="text" id="wfReqAdvanceAccount.bankAccount" name="wfReqAdvanceAccount.bankAccount" placeholder="银行帐号" maxlength="60">
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
            <input type="hidden" name="wfReqAdvanceAccount.remarks" id="wfReqAdvanceAccount.remarks">
            <input type="hidden" name="attToken" id="attToken" value="<@c.tokenValue/>">
            <input type="hidden" name="wfReq.nodeCount" id="wfReq.nodeCount" value="0">
            <div id="nodeFlowHidden"></div>
        </form>
    </div>

    </#if>
</@wfCommon.wf_common>