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
        <#if !sysOrgGroup?exists>
            url = '/sys/orgGroup!validateName.dhtml';
        <#elseif sysOrgGroup?exists>
            url = '/sys/orgGroup!validateName.dhtml?ignore=${sysOrgGroup.groupName?if_exists}';
        </#if>
        return url;
    }

    function initValidator() {
        WEBUTILS.validator.init({
            modes: [
                {
                    id: 'sysOrgGroup\\.groupName',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'}
                    ]
                }
            ]
        }, true);
    }

    function submitForm(){
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
    <@c.joddForm bean="sysOrgGroup" scope="request">
    <form class="form-horizontal" action="/sys/orgGroup!save.dhtml" method="POST" name="editForm"
          id="editForm">
        <div class="control-group">
            <label class="control-label" for="sysOrgGroup.groupName">集团名称</label>
            <div class="controls">
                <input type="text" id="sysOrgGroup.groupName" name="sysOrgGroup.groupName" placeholder="集团名称">
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <label class="checkbox">
                    <input type="checkbox" value="Y" id="sysOrgGroup.useYn" name="sysOrgGroup.useYn"> 启用
                </label>
            </div>
        </div>
        <input type="hidden" name="sysOrgGroup.id" id="sysOrgGroup.id">
        <@c.token/>
    </form>
    </@c.joddForm>
</@common.html>