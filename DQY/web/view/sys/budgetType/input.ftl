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
            url: "/common/common!orgTreeData.dhtml",
            autoParam: ["id=parentId", "name=n", "level=lv"]
        },
        callback: {
            onClick: zTreeOnClick
        }
    };
    function zTreeOnClick(event, treeId, treeNode) {
        srcNode = treeNode;
        if(treeNode){
            $('#sysBudgetType\\.deptId\\.id').val(treeNode['id']);
            $('#sysBudgetType\\.dept').val(treeNode['name']);
            $('.treeDiv').fadeOut();
        }
    }
    function initValidator() {
        WEBUTILS.validator.init({
            modes: [
                {
                    id: 'sysBudgetType\\.dept',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'}
                    ]
                },
                {
                    id: 'sysBudgetType\\.expenseType',
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

        $('#searchDept').off('click').on('click', function () {
            var left = $('#sysBudgetType\\.dept').offset().left;
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
    <@c.joddForm bean="sysBudgetType" scope="request">
    <form class="form-horizontal" action="/sys/budgetType!save.dhtml" method="POST" name="editForm"
          id="editForm">
        <div class="control-group">
            <label class="control-label" for="sysBudgetType.dept">预算部门</label>
            <div class="controls">
                <input type="text" id="sysBudgetType.dept" name="sysBudgetType.dept" placeholder="预算部门"
                     <#if sysBudgetType?exists> value="${(sysBudgetType.deptId.deptName)?if_exists}" </#if>  disabled>
                <span class="add-on" style="cursor: pointer;" id="searchDept"><i class="icon-th"></i></span>
                <span class="help-inline"></span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="sysBudgetType.expenseType">预算类别</label>
            <div class="controls">
                <input type="text" id="sysBudgetType.expenseType" name="sysBudgetType.expenseType" placeholder="机构名称">
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <label class="checkbox">
                    <input type="checkbox" value="Y" id="sysBudgetType.useYn" name="sysBudgetType.useYn"> 启用
                </label>
            </div>
        </div>
        <input type="hidden" name="sysBudgetType.id" id="sysBudgetType.id">
        <input type="hidden" name="sysBudgetType.deptId.id" id="sysBudgetType.deptId.id">
        <@c.token/>
    </form>
    </@c.joddForm>
</@common.html>