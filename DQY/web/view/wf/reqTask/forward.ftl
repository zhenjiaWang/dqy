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

    var treeNodeSeq;
    var setting = {
        view: {
            selectedMulti: false
        },
        async: {
            enable: true,
            url: "/common/common!approveTreeData.dhtml",
            autoParam: ["id=parentId", "approveType=approveTypeId", "nodeType=nodeType"]
        },
        callback: {
            onClick: zTreeOnClick
        }
    };
    function zTreeOnClick(event, treeId, treeNode) {
        srcNode = treeNode;
        if (treeNode && treeNode['nodeType'] == '1') {
            var obj = $('.control-group[nodeSeq="1"]');
            if (obj) {
                $('#node').val(treeNode['name']);
                $('#forwardType').val(treeNode['approveType']);
                $('#forwardId').val(treeNode['id']);
            }
            $('.treeDiv').fadeOut();
        }
    }

    function initValidator() {
        WEBUTILS.validator.init({
            modes: [
                {
                    id: 'reason',
                    required: false,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'}
                    ]
                }
            ]
        }, true);
    }
    function dataSubmit(){
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
    $(document).ready(function () {
        initValidator();
        $.fn.zTree.init($("#treeDemo"), setting);
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

    });
</script>
    <@c.joddForm bean="wfReqTask" scope="request">
    <form class="form-horizontal" action="/wf/reqTask!approve.dhtml" method="POST" name="editForm"
          id="editForm">
        <div class="control-group" nodeSeq="1">
            <label class="control-label" for="node1">转审人员</label>
            <div class="controls">
                <input type="text" id="node" name="node"  readonly="readonly" nodeSeq="1">
                <div class="btn-group">
                    <a class="btn approveBtn" href="#"><i class="icon-user"></i></a>
                </div>
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <label class="checkbox">
                    <input type="checkbox" value="Y" id="forwardMe" name="forwardMe"> (转审人员通过以后再次流转到我)
                </label>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="reason">请填写您的审批意见</label>
            <div class="controls">
                <textarea name="reason" id="reason" rows="4" style="width: 90%;"  ></textarea>
                <span class="help-inline"></span>
            </div>
        </div>
        <input type="hidden" name="wfReqTask.id" id="wfReqTask.id">
        <input type="hidden" name="forwardType" id="forwardType">
        <input type="hidden" name="forwardId" id="forwardId">
        <input type="hidden" name="wfReqTask.approveIdea" id="wfReqTask.approveIdea">
        <@c.token/>
    </form>
    </@c.joddForm>
</@common.html>