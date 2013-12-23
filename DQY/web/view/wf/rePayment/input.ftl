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
    .file-icon {
        display: inline-block;
        float: left;
        width: 25px;
        height: 25px;
        margin-left: 10px;
        margin-top: 13.5px;
    }

    .img-file {
        background-image: url(/images/jpg.png)
    }

    .doc-file {
        background-image: url(/images/doc.png)
    }

    .pdf-file {
        background-image: url(/images/pdf.png)
    }

    .xls-file {
        background-image: url(/images/xls.png)
    }

    .zip-file {
        background-image: url(/images/zip.png)
    }

    .default-file {
        background-image: url(/images/default.png)
    }

    #example .file-heading {
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
        overflow: hidden;
        white-space: nowrap;
    }

    #example .file-name-heading {

    }

    #example .file-size-heading {
        font-weight: normal;
        font-style: italic;
    }

    li.k-file .file-wrapper .k-upload-action {
        position: absolute;
        top: 0;
        right: 0;
    }

    li.k-file div.file-wrapper {
        position: relative;
        height: 40px;
    }


</style>
<script type="text/javascript">
var submited = false;
var uploadRemove = false;
var currentSEQ = false;

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
        $('#deptId'+currentSEQ).val(treeNode['id']);
        $('#deptName'+currentSEQ).val(treeNode['name']);
        $('#deptName'+currentSEQ).trigger('blur');
        $('.treeDiv').fadeOut();
        currentSEQ=false;
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
                id: 'wfReqRePayment\\.amount',
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
            },
            {
                id: 'date1',
                required: true,
                pattern: [
                    {type: 'reg', exp: '_date', msg: '`'}
                ]
            },
            {
                id: 'deptName1',
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

function bindSelect(seq) {
    <#if typeList?exists&&typeList?size gt 0>
        $('#typeId' + seq).empty();
        <#list typeList as type>
            $('#typeId' + seq).append('<option value="${type.id?c}">${type.expenseType?if_exists}</option>');
        </#list>
    </#if>
    <#if titleList?exists&&titleList?size gt 0>
        $('#titleId' + seq).empty();
        <#list titleList as title>
            $('#titleId' + seq).append('<option value="${title.id?c}">${title.titleName?if_exists}</option>');
        </#list>
    </#if>
    $('#amount' + seq).off('blur').on('blur', function () {
        var totalAm = 0.00;
        $('.amt').each(function (i, o) {
            var am = $(o).val();
            if (am) {
                am = parseFloat(am);
                if (am) {
                    totalAm += am;
                }
            }
        });
        $('#wfReqRePayment\\.amount').val(totalAm);
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
            var img = $('img', lastLi);
            if (img) {
                var extension = $(img).attr('extension');
                if (extension) {
                    extension = extension.substring(1, extension.length);
                    $(img).attr('src', '/images/file/' + extension + '.png');
                }
            }
        }
    }
}
function onRemove(e) {
    uploadRemove = true;
}
function onUpload(e) {
    uploadRemove = false;
}
function dateEvent() {
    $('.dateTd').each(function (i, o) {
        var dateObj = $('input[type="text"]', o);
        if (dateObj) {
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

$(document).ready(function () {
    initValidator();
    $.fn.zTree.init($("#treeDemo"), setting);

    var ue = UM.getEditor('editor', {
        lang: 'zh-cn',
        langPath: UMEDITOR_CONFIG.UMEDITOR_HOME_URL + "lang/",
        focus: true
    });
    $('#nextBtn').off('click').on('click', function (e) {
        e.preventDefault();
        e.stopPropagation();
        var adId = $('#wfReqRePayment\\.advanceId\\.id').val();
        var detailCount = $('.detailTr').last().attr('seq');
        if (adId && adId != '0' && detailCount) {
            $('#detailCount').val(detailCount);
            WEBUTILS.validator.checkAll();
            window.setTimeout(function () {
                var passed = WEBUTILS.validator.isPassed();
                $('#wfReqRePayment\\.remarks').val(getContent());
                if (passed) {
                    WEBUTILS.popWindow.createPopWindow(750, 535, '选择流程', '/wf/reqMyFlow!myFlowList.dhtml?applyId=${applyId?if_exists}');
                } else {
                    WEBUTILS.validator.showErrors();
                }
            }, 500);
        } else {
            alert('暂时无法申请');
        }
    });
    $('#addDetail').off('click').on('click', function (e) {
        e.preventDefault();
        e.stopPropagation();
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
            WEBUTILS.validator.addMode({
                id: 'deptName' + nextNodeSeq,
                required: true,
                pattern: [
                    {type: 'blank', exp: '!=', msg: ''}
                ]
            });
            bindSelect(nextNodeSeq);
            dateEvent();
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
                    id: 'amount' + currentNodeSeq
                });
                WEBUTILS.validator.removeMode({
                    id: 'date' + currentNodeSeq
                });
                WEBUTILS.validator.removeMode({
                    id: 'deptName' + currentNodeSeq
                });
            }
        }
    });


    $('#amount1').off('blur').on('blur', function () {
        var totalAm = 0.00;
        $('.amt').each(function (i, o) {
            var am = $(o).val();
            if (am) {
                am = parseFloat(am);
                if (am) {
                    totalAm += am;
                }
            }
        });
        $('#wfReqRePayment\\.amount').val(totalAm);
    });

    $("#upload").kendoUpload({
        multiple: true,
        async: {
            saveUrl: "/common/common!upload.dhtml?attKey=${applyId?if_exists}&attToken=" + $("#attToken").val(),
            removeUrl: "/common/common!remove.dhtml?attKey=${applyId?if_exists}&attToken=" + $("#attToken").val(),
            autoUpload: true
        },
        success: onSuccess,
        remove: onRemove,
        upload: onUpload,
        localization: {
            "select": "上传文件",
            "cancel": "取消",
            "retry": "重试",
            "remove": "删除",
            "done": "完成",
            "uploadSelectedFiles": "Upload files",
            "dropFilesHere": "drop files here to upload",
            "statusUploading": "uploading",
            "statusUploaded": "uploaded",
            "statusFailed": "failed"
        },
        template: kendo.template($('#fileTemplate').html())
    });
    dateEvent();
    $('#wfReqRePayment\\.payMethod').change(function () {
        var payMethod = $('#wfReqRePayment\\.payMethod').val();
        if (payMethod == "2") {
            $('.bank').show();
            WEBUTILS.validator.addMode({
                id: 'wfReqRePayment\\.payee',
                required: true,
                pattern: [
                    {type: 'blank', exp: '!=', msg: ''}
                ]
            });
        } else {
            $('.bank').hide();
            WEBUTILS.validator.removeMode({
                id: 'wfReqRePayment\\.payee'
            });
        }
    });

    $('.application').off('click').on('click', function (e) {
        if ($('.treeDiv').is(':visible')) {
            $('.treeDiv').fadeOut();
        }
    });
});
</script>
<!--搜索begin-->
<div class="r-top clearfix">
    <p class="text-info text-center lead">预支还款申请单</p>
</div>
<!--搜索over-->
    <#if Session["userSession"]?exists>
        <#assign userInfo=Session["userSession"]?if_exists>
    <div class="mart5">
    <form class="form-horizontal" action="/wf/rePayment!save.dhtml" method="POST" name="editForm"
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
                <label class="control-label" for="wfReqRePayment.advanceId.id"
                       style="width: 60px;color: #898989;">预支申请</label>

                <div class="controls" style="margin-left: 70px;*margin-left:0;">
                    <select class="int2" style="width: 300px;" id="wfReqRePayment.advanceId.id"
                            name="wfReqRePayment.advanceId.id">
                        <#if reqAdvanceAccountList?exists&&reqAdvanceAccountList?size gt 0>
                            <#list reqAdvanceAccountList as advance>
                                <option value="${advance.id?c}">${(advance.reqId.subject)?if_exists} 预支金额(${advance.amount?c})
                                    &nbsp;&nbsp;已还款金额:${advance.reAmount?c}</option>
                            </#list>
                        <#else >
                            <option value="0">没有未还款的预支申请 暂时无法选择</option>
                        </#if>
                    </select>
                    <span class="help-inline"></span>
                </div>
            </div>
        </td>
    </tr>
    <tr>
        <td>
            <div class="control-group" style="margin-bottom: 5px;">
                <label class="control-label" for="wfReqRePayment.amount"
                       style="width: 60px;color: #898989;">还款金额</label>

                <div class="controls" style="margin-left: 70px;*margin-left:0;">
                    <input type="text" id="wfReqRePayment.amount" name="wfReqRePayment.amount"
                           placeholder="还款金额" maxlength="10" readonly="readonly">
                    <span class="help-inline"></span>
                </div>
            </div>
        </td>
        <td>
            <div class="control-group" style="margin-bottom: 5px;">
                <label class="control-label" for="wfReqRePayment.payMethod"
                       style="width: 60px;color: #898989;">支付方式</label>

                <div class="controls" style="margin-left: 70px;*margin-left:0;">
                    <select class="int2 width-160" id="wfReqRePayment.payMethod"
                            name="wfReqRePayment.payMethod">
                        <option value="1">现金</option>
                        <option value="2">银行转账</option>
                        <option value="3">支票</option>
                    </select>
                </div>
            </div>
        </td>
    </tr>
    <tr class="bank" style="display: none;">
        <td colspan="2">
            <div class="control-group" style="margin-bottom: 5px;">
                <label class="control-label" for="wfReqRePayment.payee"
                       style="width: 60px;color: #898989;">收款单位</label>

                <div class="controls" style="margin-left: 70px;*margin-left:0;">
                    <input style="width: 95%;" type="text" id="wfReqRePayment.payee" name="wfReqRePayment.payee"
                           placeholder="收款单位/人" maxlength="20">
                    <span class="help-inline"></span>
                </div>
            </div>
        </td>
    </tr>
    <tr class="bank" style="display: none;">
        <td>
            <div class="control-group" style="margin-bottom: 5px;">
                <label class="control-label" for="wfReqRePayment.bank"
                       style="width: 60px;color: #898989;">开户行</label>

                <div class="controls" style="margin-left: 70px;*margin-left:0;">
                    <input type="text" id="wfReqRePayment.bank" name="wfReqRePayment.bank" placeholder="开户行"
                           maxlength="20">
                    <span class="help-inline"></span>
                </div>
            </div>
        </td>
        <td>
            <div class="control-group" style="margin-bottom: 5px;">
                <label class="control-label" for="wfReqRePayment.bankAccount"
                       style="width: 60px;color: #898989;">帐号</label>

                <div class="controls" style="margin-left: 70px;*margin-left:0;">
                    <input type="text" id="wfReqRePayment.bankAccount" name="wfReqRePayment.bankAccount"
                           placeholder="银行帐号" maxlength="20">
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
                <tbody>
                <tr>
                    <td class="nopadding p-top5 "><span
                            class="label label-info yearAmountTotal">预算总额：${totalAmount?if_exists}</span></td>
                    <td class="nopadding p-top5"><span
                            class="label label-success">已产生-已审批：${totalPassAmount?if_exists}</span></td>
                    <td class="nopadding p-top5"><span
                            class="label label-warning">已产生-待审批：${totalIngAmount?if_exists}</span></td>
                    <td class="nopadding p-top5"><span
                            class="label label-important">超出预算金额：${remnantAmount?if_exists}</span></td>
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
                    <td width="100">费用部门</td>
                    <td width="100">费用类型</td>
                    <td width="100">费用项目</td>
                    <td width="110">费用日期</td>
                    <td width="90">金额</td>
                    <td>
                        <a href="##" id="addDetail" ><i class="icon-plus"></i> 增加</a>
                        <a href="##" id="deleteDetail" ><i class="icon-minus"></i>
                            删除</a>
                    </td>
                </tr>
                </thead>
                <tbody>
                <tr seq="1" class="detailTr">
                    <td>
                        <div class="control-group" style="margin-bottom: 0px;">
                            <input type="text" class="int1 width-100 dept" id="deptName1" name="deptName1" readonly="readonly" placeholder="选择部门">
                            <input type="hidden"  id="deptId1" name="deptId1" >
                        </div>
                    </td>
                    <td><select class="int2 width-100" id="typeId1" name="typeId1">
                        <#if typeList?exists&&typeList?size gt 0>
                            <#list typeList as type>
                                <option value="${type.id?c}">
                                ${type.expenseType?if_exists}
                                </option>
                            </#list>
                        </#if>
                    </select></td>
                    <td>
                        <select class="int2 width-100" id="titleId1" name="titleId1">
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
                            <input type="text" class="int1 width-100" id="date1" name="date1">
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
                <input type="file" name="files" id="upload"/>
                <script id="fileTemplate" type="text/x-kendo-template">
                    <span class='k-progress'></span>

                    <div class='file-wrapper'>
                        <img src="/images/file/file.png" extension="#=files[0].extension#" width="25" height="25"
                             style="float: left;margin-right: 10px;">
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
    <input type="hidden" name="wfReqRePayment.remarks" id="wfReqRePayment.remarks">

    <div id="nodeFlowHidden"></div>
    </form>
    </div>


    </#if>
</@wfCommon.wf_common>