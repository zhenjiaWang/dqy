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
        <#if !saleCustomer?exists>
            url = '/sale/customer!validateName.dhtml';
        <#elseif saleCustomer?exists>
            url = '/sale/customer!validateName.dhtml?ignore=${saleCustomer.customerName?if_exists}';
        </#if>
        return url;
    }


    function initValidator() {
        WEBUTILS.validator.init({
            modes: [
                {
                    id: 'saleCustomer\\.customerName',
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
    <@c.joddForm bean="saleCustomer" scope="request">
    <form class="form-horizontal" action="/sale/customer!save.dhtml" method="POST" name="editForm"
          id="editForm">
        <div class="control-group">
            <label class="control-label" for="saleCustomer.systemId.id">系统名称</label>
            <div class="controls">
                <select  id="saleCustomer.systemId.id" name="saleCustomer.systemId.id">
                    <#if systemList?exists&&systemList?size gt 0>
                    <#list systemList as system>
                        <option value="${system.id?c}">${system.systemName?if_exists}</option>
                    </#list>
                    </#if>
                </select>
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="saleCustomer.customerName">客户/门店</label>
            <div class="controls">
                <input type="text" id="saleCustomer.customerName" name="saleCustomer.customerName" placeholder="客户/门店">
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <label class="checkbox">
                    <input type="checkbox" value="Y" id="saleCustomer.useYn" name="saleCustomer.useYn"> 启用
                </label>
            </div>
        </div>
        <input type="hidden" name="saleCustomer.id" id="saleCustomer.id">
        <@c.token/>
    </form>
    </@c.joddForm>
</@common.html>