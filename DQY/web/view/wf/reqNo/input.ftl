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
        <#if !wfReqNo?exists>
            url = '/wf/reqNo!validateReqNo.dhtml';
        <#elseif wfReqNo?exists>
            url = '/wf/reqNo!validateReqNo.dhtml?ignore=${wfReqNo.reqNo?if_exists}';
        </#if>
        return url;
    }

    function initValidator() {
        WEBUTILS.validator.init({
            modes: [
                {
                    id: 'wfReqNo\\.reqNo',
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
    <@c.joddForm bean="wfReqNo" scope="request">
    <form class="form-horizontal" action="/wf/reqNo!save.dhtml" method="POST" name="editForm"
          id="editForm">
        <div class="control-group">
            <label class="control-label" for="applyName">申请单据名称</label>
            <div class="controls">
                <input type="text" id="applyName" name="applyName" value="${applyName?if_exists}" disabled>
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="wfReqNo.reqNo">申请单据编号</label>
            <div class="controls">
                <input type="text" id="wfReqNo.reqNo" name="wfReqNo.reqNo" placeholder="集团名称">
                <span class="help-inline"></span>
            </div>
        </div>
        <input type="hidden" name="wfReqNo.id" id="wfReqNo.id">
        <input type="hidden" name="applyId" id="applyId" value="${applyId?if_exists}">
        <@c.token/>
    </form>
    </@c.joddForm>
</@common.html>