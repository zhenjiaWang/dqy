<#import "/view/template/common.ftl" as common>
<#import "/view/common/core.ftl" as c>
<@common.html>
<link href="/css/validator/validator.css" rel="stylesheet"/>
<script type="text/javascript" src="/js/webutils/webutils.validator.js"></script>
<script type="text/javascript" src="/js/webutils/reg.js"></script>


<link type="text/css" href="/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<script src="/js/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript">
    var submited = false;
    var srcNode;
    var treeNodeSeq;
    var setting = {
        view: {
            selectedMulti: false
        },
        async: {
            enable: true,
            url: "/common/common!approveTreeData.dhtml",
            autoParam: ["id=parentId", "approveType=approveTypeId", "nodeType=nodeType"],
            otherParam:  { "notMe":"Y"}
        },
        callback: {
            onClick: zTreeOnClick
        }
    };
    function zTreeOnClick(event, treeId, treeNode) {
        srcNode = treeNode;
        if (treeNode && treeNode['nodeType'] == '1' && treeNodeSeq) {
            var obj = $('.control-group[nodeSeq="' + treeNodeSeq + '"]');
            if (obj) {
                $('#node' + treeNodeSeq).val(treeNode['name']);
                $('#nodeType' + treeNodeSeq).val(1);
                $('#approveType' + treeNodeSeq).val(treeNode['approveType']);
                $('#approve' + treeNodeSeq).val(treeNode['id']);
            }
            $('.treeDiv').fadeOut();
        }
    }
    function initValidator() {
        WEBUTILS.validator.init({
            modes: [
                {
                    id: 'node1',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'}
                    ]
                }
            ]
        }, true);
    }
    function approveTreeEvent() {
        $('.approveBtn').off('click').on('click', function () {
            treeNodeSeq = $(this).parents('.control-group').attr('nodeSeq');
            var left = $(this).offset().left;
            var top = $(this).offset().top + 15;
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
    function dataSubmit() {
        if (!submited) {
            WEBUTILS.validator.checkAll();
            window.setTimeout(function () {
                var passed = WEBUTILS.validator.isPassed();
                if (passed) {
                    var currentNodeSeq = $('.control-group').last().attr('nodeSeq');
                    if (currentNodeSeq) {
                        $('#approveIndex').val(currentNodeSeq);
                        document.approveForm.submit();
                        submited = true;
                    }
                } else {
                    WEBUTILS.validator.showErrors();
                }
            }, 500);
        }
    }
    $(document).ready(function () {
        initValidator();
        approveTreeEvent();
        $('#addNode').off('click').on('click', function () {
            var currentNodeSeq = $('.control-group').last().attr('nodeSeq');
            if (currentNodeSeq) {
                currentNodeSeq = parseInt(currentNodeSeq);
                var nextNodeSeq = currentNodeSeq + 1;
                $('.control-group').last().after(String.formatmodel(flowApproveDQY, {nodeSeq: nextNodeSeq}));
                approveTreeEvent();
                WEBUTILS.validator.addMode({
                    id: 'node' + nextNodeSeq,
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'}
                    ]
                });
                submited = false;
            }
        });
        $('#deleteNode').off('click').on('click', function () {
            var currentNodeSeq = $('.control-group').last().attr('nodeSeq');
            if (currentNodeSeq) {
                currentNodeSeq = parseInt(currentNodeSeq);
                if (currentNodeSeq > 1) {
                    $('.control-group').last().remove();
                    WEBUTILS.validator.removeMode({
                        id: 'node' + currentNodeSeq
                    });
                }
            }
        });
        $.fn.zTree.init($("#treeDemo"), setting);

    });
</script>
<div style="min-height: 300px;">
    <form class="form-horizontal" action="/wf/reqMyFlow!save.dhtml" method="POST" name="approveForm"
          id="approveForm">
        <div class="control-group" nodeSeq="0">
            <label class="control-label" for="applyName">申请单据</label>

            <div class="controls">
                <input type="text" id="applyName" name="applyName"  disabled
                       value="${applyId?if_exists}">
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group" nodeSeq="1">
            <label class="control-label" for="node1">第1步审批人</label>

            <div class="controls">
                <input type="text" id="node1" name="node1"  readonly="readonly" nodeSeq="1">
                <div class="btn-group">
                    <a class="btn approveBtn" href="#"><i class="icon-user"></i></a>
                </div>
                <a href="#" id="addNode"><i class="icon-plus"></i> 增加</a>
                <a href="#" id="deleteNode"><i class="icon-minus"></i> 删除</a>
                <span class="help-inline"></span>
                <input type="hidden" name="nodeType1" id="nodeType1" nodeSeq="1">
                <input type="hidden" name="approveType1" id="approveType1" nodeSeq="1">
                <input type="hidden" name="approve1" id="approve1" nodeSeq="1">
            </div>
        </div>

        <input type="hidden" name="wfReqMyFlow.id" id="wfReqMyFlow.id">
        <input type="hidden" name="wfReqMyFlow.applyId" id="wfReqMyFlow.applyId" value="${applyId?if_exists}">

        <input type="hidden" name="approveIndex" id="approveIndex">
        <@c.token/>
    </form>
</div>
</@common.html>