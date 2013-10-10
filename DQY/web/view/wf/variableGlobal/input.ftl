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
    var setting = {
        view: {
            selectedMulti: false
        },
        async: {
            enable: true,
            url: "/common/common!userTreeData.dhtml",
            autoParam: ["id=parentId", "name=n", "level=lv"]
        },
        callback: {
            onClick: zTreeOnClick
        }
    };
    function zTreeOnClick(event, treeId, treeNode) {
        srcNode = treeNode;
        if(treeNode){
            $('#wfVariableGlobal\\.userId\\.id').val(treeNode['id']);
            $('#wfVariableGlobal\\.userId\\.userName').val(treeNode['name']);
            $('.treeDiv').fadeOut();
        }
    }


    function validatorName() {
        var url = '';
        <#if !wfVariableGlobal?exists>
            url = '/wf/variableGlobal!validateName.dhtml';
        <#elseif wfVariableGlobal?exists>
            url = '/wf/variableGlobal!validateName.dhtml?ignore=${wfVariableGlobal.variableName?if_exists}';
        </#if>
        return url;
    }

    function initValidator() {
        WEBUTILS.validator.init({
            modes: [
                {
                    id: 'wfVariableGlobal\\.variableName',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'},
                        {
                            type:'ajax',
                            exp:validatorName(),
                            msg:'不能重复'
                        }
                    ]
                },
                {
                    id: 'wfVariableGlobal\\.userId\\.userName',
                    required: true,
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

        $('#searchUser').off('click').on('click', function () {
            var left = $(this).offset().left;
            var top = $(this).offset().top+15;
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
    <@c.joddForm bean="wfVariableGlobal" scope="request">
    <form class="form-horizontal" action="/wf/variableGlobal!save.dhtml" method="POST" name="editForm"
          id="editForm">
        <div class="control-group">
            <label class="control-label" for="wfVariableGlobal.variableName">审批岗位</label>
            <div class="controls">
                <input type="text" id="wfVariableGlobal.variableName" name="wfVariableGlobal.variableName" placeholder="审批岗位">
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="wfVariableGlobal.userId.userName">审批人</label>
            <div class="controls">
                <input type="text" id="wfVariableGlobal.userId.userName" name="wfVariableGlobal.userId.userName" placeholder="审批人"disabled>
                <span class="add-on" style="cursor: pointer;" id="searchUser"><i class="icon-th"></i></span>
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <label class="checkbox">
                    <input type="checkbox" value="Y" id="wfVariableGlobal.useYn" name="wfVariableGlobal.useYn"> 启用
                </label>
            </div>
        </div>
        <input type="hidden" name="wfVariableGlobal.id" id="wfVariableGlobal.id">
        <input type="hidden" name="wfVariableGlobal.userId.id" id="wfVariableGlobal.userId.id">
        <@c.token/>
    </form>
    </@c.joddForm>
</@common.html>