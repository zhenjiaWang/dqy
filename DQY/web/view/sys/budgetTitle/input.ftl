<#import "/view/template/common.ftl" as common>
<#import "/view/common/core.ftl" as c>
<@common.html>
<link href="/css/validator/validator.css" rel="stylesheet"/>
<script type="text/javascript" src="/js/webutils/webutils.validator.js"></script>
<script type="text/javascript" src="/js/webutils/reg.js"></script>
<script type="text/javascript">
    var submited = false;
    function validatorName() {
        var url = '';
        <#if !sysBudgetTitle?exists>
            url = '/sys/budgetTitle!validateName.dhtml?typeId=${typeId?c}';
        <#elseif sysBudgetTitle?exists>
            url = '/sys/budgetTitle!validateName.dhtml?ignore=${sysBudgetTitle.titleName?if_exists}&typeId=${typeId?c}';
        </#if>
        return url;
    }



    function initValidator() {
        WEBUTILS.validator.init({
            modes: [
                {
                    id: 'sysBudgetTitle\\.titleName',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'},
                        {
                            type:'ajax',
                            exp:validatorName(),
                            msg:'不能重复'
                        }
                    ]
                }
            ]
        }, true);
    }

    $(document).ready(function () {
        initValidator();
        $('#sysBudgetTitle\\.typeId\\.id').change(function(){
            <#if sysBudgetTitle?exists>
                WEBUTILS.popWindow.createPopWindow(550, null, '编辑预算科目', '/sys/budgetTitle!input.dhtml?id=${sysBudgetTitle.id?c}&typeId='+$(this).val());
            <#else >
                WEBUTILS.popWindow.createPopWindow(550, null, '创建预算科目', '/sys/budgetTitle!input.dhtml?typeId='+$(this).val());
            </#if>
        });
        $('#myModalOkBtn','#myModal').off('click').on('click',function(){
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
        });
    });
</script>
    <@c.joddForm bean="sysBudgetTitle" scope="request">
    <form class="form-horizontal" action="/sys/budgetTitle!save.dhtml" method="POST" name="editForm"
          id="editForm">
        <div class="control-group">
            <label class="control-label" for="sysBudgetTitle.typeId.id">预算类别</label>
            <div class="controls">
                <select  id="sysBudgetTitle.typeId.id" name="sysBudgetTitle.typeId.id">
                    <#if budgetTypeList?exists&&budgetTypeList?size gt 0>
                    <#list budgetTypeList as type>
                        <option value="${type.id?c}" <#if typeId?exists&&type.id==typeId> selected="selected" </#if> >[ ${(type.deptId.deptName)?if_exists} ]--${type.expenseType?if_exists}</option>
                    </#list>
                    </#if>
                </select>
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="sysBudgetTitle.titleName">预算科目</label>
            <div class="controls">
                <input type="text" id="sysBudgetTitle.titleName" name="sysBudgetTitle.titleName" placeholder="预算科目">
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <label class="checkbox">
                    <input type="checkbox" value="Y" id="sysBudgetTitle.useYn" name="sysBudgetTitle.useYn"> 启用
                </label>
            </div>
        </div>
        <input type="hidden" name="sysBudgetTitle.id" id="sysBudgetTitle.id">
        <@c.token/>
    </form>
    </@c.joddForm>
</@common.html>