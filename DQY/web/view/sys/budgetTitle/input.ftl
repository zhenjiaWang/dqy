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
            url = '/sys/budgetTitle!validateName.dhtml?typeId='+$('#sysBudgetTitle\\.typeId\\.id').val();
        <#elseif sysBudgetTitle?exists>
            url = '/sys/budgetTitle!validateName.dhtml?ignore=${sysBudgetTitle.titleName?if_exists}&typeId='+$('#sysBudgetTitle\\.typeId\\.id').val();
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
        $('#sysBudgetTitle\\.typeId\\.id').change(function(){
            WEBUTILS.validator.removeMode({
                id: 'sysBudgetTitle\\.titleName'
            });
            <#if sysBudgetTitle?exists>
                var _url = '/sys/budgetTitle!validateName.dhtml?ignore=${sysBudgetTitle.titleName?if_exists}&typeId='+$(this).val();
                WEBUTILS.validator.addMode({
                    id: 'sysBudgetTitle\\.titleName',
                    required: true,
                    pattern: [
                        {type: 'ajax', exp: _url, msg: '不能重复'}
                    ]
                });
            <#else >
                var _url = '/sys/budgetTitle!validateName.dhtml?typeId='+$(this).val();
                WEBUTILS.validator.addMode({
                    id: 'sysBudgetTitle\\.titleName',
                    required: true,
                    pattern: [
                        {type: 'ajax', exp: _url, msg: '不能重复'}
                    ]
                });
            </#if>
        });
    });
</script>
    <@c.joddForm bean="sysBudgetTitle" scope="request">
    <form class="form-horizontal" action="/sys/budgetTitle!save.dhtml" method="POST" name="editForm"
          id="editForm">
        <div class="control-group">
            <label class="control-label" for="sysBudgetTitle.typeId.id">预算类型</label>
            <div class="controls">
                <select  id="sysBudgetTitle.typeId.id" name="sysBudgetTitle.typeId.id">
                    <#if budgetTypeList?exists&&budgetTypeList?size gt 0>
                    <#list budgetTypeList as type>
                        <option value="${type.id?c}" >${type.expenseType?if_exists}</option>
                    </#list>
                    </#if>
                </select>
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="sysBudgetTitle.titleName">项目代码</label>
            <div class="controls">
                <input type="text" id="sysBudgetTitle.titleNo" name="sysBudgetTitle.titleNo" placeholder="项目代码">
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