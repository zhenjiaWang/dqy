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
        <#if !sysOrg?exists>
            url = '/sys/org!validateName.dhtml?groupId=${groupId?c}';
        <#elseif sysOrg?exists>
            url = '/sys/org!validateName.dhtml?ignore=${sysOrg.orgName?if_exists}&groupId=${groupId?c}';
        </#if>
        return url;
    }

    function validatorNo() {
        var url = '';
        <#if !sysOrg?exists>
            url = '/sys/org!validateNo.dhtml?groupId=${groupId?c}';
        <#elseif sysOrg?exists>
            url = '/sys/org!validateNo.dhtml?ignore=${sysOrg.orgNo?if_exists}&groupId=${groupId?c}';
        </#if>
        return url;
    }

    function initValidator() {
        WEBUTILS.validator.init({
            modes: [
                {
                    id: 'sysOrg\\.orgNo',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'},
                        {
                            type:'ajax',
                            exp:validatorNo(),
                            msg:'不能重复'
                        }
                    ]
                },
                {
                    id: 'sysOrg\\.orgName',
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
    <@c.joddForm bean="sysOrg" scope="request">
    <form class="form-horizontal" action="/sys/org!save.dhtml" method="POST" name="editForm"
          id="editForm">
        <div class="control-group">
            <label class="control-label" for="sysOrg.groupId.id">集团名称</label>
            <div class="controls">
                <select  id="sysOrg.groupId.id" name="sysOrg.groupId.id">
                    <#if orgGroupList?exists&&orgGroupList?size gt 0>
                    <#list orgGroupList as orgGroup>
                        <option value="${orgGroup.id?c}">${orgGroup.groupName?if_exists}</option>
                    </#list>
                    </#if>
                </select>
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="sysOrg.orgNo">机构编码</label>
            <div class="controls">
                <input type="text" id="sysOrg.orgNo" name="sysOrg.orgNo" placeholder="机构编码">
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="sysOrg.orgName">机构名称</label>
            <div class="controls">
                <input type="text" id="sysOrg.orgName" name="sysOrg.orgName" placeholder="机构名称">
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <label class="checkbox">
                    <input type="checkbox" value="Y" id="sysOrg.useYn" name="sysOrg.useYn"> 启用
                </label>
            </div>
        </div>
        <input type="hidden" name="sysOrg.id" id="sysOrg.id">
        <@c.token/>
    </form>
    </@c.joddForm>
</@common.html>