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
    function validatorName() {
        var url = '';
        <#if !sysBudgetType?exists>
            url = '/sys/budgetType!validateName.dhtml';
        <#elseif sysBudgetType?exists>
            url = '/sys/budgetType!validateName.dhtml?ignore=${sysBudgetType.expenseType?if_exists}';
        </#if>
        return url;
    }
    function initValidator() {
        WEBUTILS.validator.init({
            modes: [
                {
                    id: 'sysBudgetType\\.expenseType',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'},
                        {type: 'ajax', exp: validatorName(), msg: '不能重复'}
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
    });
</script>
    <@c.joddForm bean="sysBudgetType" scope="request">
    <form class="form-horizontal" action="/sys/budgetType!save.dhtml" method="POST" name="editForm"
          id="editForm">
        <div class="control-group">
            <label class="control-label" for="sysBudgetType.expenseType">费用类别</label>
            <div class="controls">
                <input type="text" id="sysBudgetType.expenseType" name="sysBudgetType.expenseType" placeholder="费用类别">
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