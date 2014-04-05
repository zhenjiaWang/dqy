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
        <#if !saleProduct?exists>
            url = '/sale/product!validateName.dhtml';
        <#elseif saleProduct?exists>
            url = '/sale/product!validateName.dhtml?ignore=${saleProduct.productName?if_exists}';
        </#if>
        return url;
    }
    function validatorCode() {
        var url = '';
        <#if !saleProduct?exists>
            url = '/sale/product!validateCode.dhtml';
        <#elseif saleProduct?exists>
            url = '/sale/product!validateCode.dhtml?ignore=${saleProduct.productCode?if_exists}';
        </#if>
        return url;
    }

    function initValidator() {
        WEBUTILS.validator.init({
            modes: [
                {
                    id: 'saleProduct\\.productName',
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
                    id: 'saleProduct\\.productCode',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'},
                        {
                            type:'ajax',
                            exp:validatorCode(),
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
    <@c.joddForm bean="saleProduct" scope="request">
    <form class="form-horizontal" action="/sale/product!save.dhtml" method="POST" name="editForm"
          id="editForm">
        <div class="control-group">
            <label class="control-label" for="saleProduct.seriesId.id">系列名称</label>
            <div class="controls">
                <select  id="saleProduct.seriesId.id" name="saleProduct.seriesId.id">
                    <#if seriesList?exists&&seriesList?size gt 0>
                    <#list seriesList as series>
                        <option value="${series.id?c}">${series.seriesName?if_exists}</option>
                    </#list>
                    </#if>
                </select>
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="saleProduct.productCode">单品编码</label>
            <div class="controls">
                <input type="text" id="saleProduct.productCode" name="saleProduct.productCode" placeholder="单品编码">
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="saleProduct.productName">单品名称</label>
            <div class="controls">
                <input type="text" id="saleProduct.productName" name="saleProduct.productName" placeholder="单品名称">
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <label class="checkbox">
                    <input type="checkbox" value="Y" id="saleProduct.useYn" name="saleProduct.useYn"> 启用
                </label>
            </div>
        </div>
        <input type="hidden" name="saleProduct.id" id="saleProduct.id">
        <@c.token/>
    </form>
    </@c.joddForm>
</@common.html>