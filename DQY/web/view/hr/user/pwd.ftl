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
                    id: 'hrUser\\.userPwd',
                    required: true,
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
    <@c.joddForm bean="hrUser" scope="request">
    <form class="form-horizontal" action="/hr/user!savePwd.dhtml" method="POST" name="editForm"
          id="editForm">
        <div class="control-group">
            <label class="control-label" for="hrUser.userName">员工姓名</label>
            <div class="controls">
                <input type="text" id="hrUser.userName" name="hrUser.userName" placeholder="员工姓名" disabled>
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="hrUser.userNo">登陆帐号</label>
            <div class="controls">
                <input type="text" id="hrUser.userNo" name="hrUser.userNo" placeholder="员工姓名" disabled>
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="hrUser.userPwd">登陆密码</label>
            <div class="controls">
                <input type="password" id="hrUser.userPwd" name="hrUser.userPwd" >
                <span class="help-inline"></span>
            </div>
        </div>

        <input type="hidden" name="hrUser.id" id="hrUser.id">
        <@c.token/>
    </form>
    </@c.joddForm>
</@common.html>