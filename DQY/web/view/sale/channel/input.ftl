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
        <#if !saleChannel?exists>
            url = '/sale/channel!validateName.dhtml';
        <#elseif saleChannel?exists>
            url = '/sale/channel!validateName.dhtml?ignore=${saleChannel.channelName?if_exists}';
        </#if>
        return url;
    }

    function initValidator() {
        WEBUTILS.validator.init({
            modes: [
                {
                    id: 'saleChannel\\.channelName',
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
    <@c.joddForm bean="saleChannel" scope="request">
    <form class="form-horizontal" action="/sale/channel!save.dhtml" method="POST" name="editForm"
          id="editForm">
        <div class="control-group">
            <label class="control-label" for="saleChannel.channelName">渠道名称</label>
            <div class="controls">
                <input type="text" id="saleChannel.channelName" name="saleChannel.channelName" placeholder="渠道名称">
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <label class="checkbox">
                    <input type="checkbox" value="Y" id="saleChannel.useYn" name="saleChannel.useYn"> 启用
                </label>
            </div>
        </div>
        <input type="hidden" name="saleChannel.id" id="saleChannel.id">
        <@c.token/>
    </form>
    </@c.joddForm>
</@common.html>