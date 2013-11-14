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
            url = '/sys/budgetTitle!validateName.dhtml';
        <#elseif sysBudgetTitle?exists>
            url = '/sys/budgetTitle!validateName.dhtml?ignore=${sysBudgetTitle.titleName?if_exists}';
        </#if>
        return url;
    }

    function validatorNo() {
        var url = '';
        <#if !sysBudgetTitle?exists>
            url = '/sys/budgetTitle!validateNo.dhtml';
        <#elseif sysBudgetTitle?exists>
            url = '/sys/budgetTitle!validateNo.dhtml?ignore=${sysBudgetTitle.titleNo?if_exists}';
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
                },
                {
                    id: 'sysBudgetTitle\\.titleNo',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'},
                        {
                            type:'ajax',
                            exp:validatorNo(),
                            msg:'不能重复'
                        }
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
    <@c.joddForm bean="sysBudgetTitle" scope="request">
    <form class="form-horizontal" action="/sys/budgetTitle!save.dhtml" method="POST" name="editForm"
          id="editForm">
        <div class="control-group">
            <label class="control-label" for="sysBudgetTitle.titleName">项目代码</label>
            <div class="controls">
                <input type="text" id="sysBudgetTitle.titleNo" name="sysBudgetTitle.titleNo" placeholder="项目代码" maxlength="4">
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="sysBudgetTitle.titleName">预算项目</label>
            <div class="controls">
                <input type="text" id="sysBudgetTitle.titleName" name="sysBudgetTitle.titleName" placeholder="预算项目">
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