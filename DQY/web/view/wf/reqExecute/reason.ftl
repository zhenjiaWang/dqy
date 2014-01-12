<#import "/view/template/common.ftl" as common>
<#import "/view/common/core.ftl" as c>
<@common.html>
<link href="/css/validator/validator.css" rel="stylesheet"/>
<script type="text/javascript" src="/js/webutils/webutils.validator.js"></script>
<script type="text/javascript" src="/js/webutils/reg.js"></script>
<script type="text/javascript">
    var submited = false;

    function initValidator() {
        WEBUTILS.validator.init({
            modes: [
                {
                    id: 'reason',
                    required: false,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'}
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
    <@c.joddForm bean="wfReqExecute" scope="request">
    <form class="form-horizontal" action="/wf/reqExecute!ok.dhtml" method="POST" name="editForm"
          id="editForm">
        <div class="control-group">
            <label class="control-label" for="reason">请填写处理结果</label>
            <div class="controls">
                <textarea name="reason" id="reason" rows="4" style="width: 90%;"  ></textarea>
                <span class="help-inline"></span>
            </div>
        </div>
        <input type="hidden" name="wfReqExecute.id" id="wfReqExecute.id">
        <@c.token/>
    </form>
    </@c.joddForm>
</@common.html>