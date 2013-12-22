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
                    id: 'hrUser\\.userEmail',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'}
                    ]
                },
                {
                    id: 'hrUser\\.emailPwd',
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

    });
</script>
    <@c.joddForm bean="hrUser" scope="request">
    <form class="form-horizontal" action="/hr/user!createEmail.dhtml" method="POST" name="editForm"
          id="editForm">
        <table width="100%" class="layout">
            <tbody>
            <tr>
                <td>
                    <div class="control-group">
                        <label class="control-label" for="hrUser.userName" style="width: 60px;">员工姓名</label>

                        <div class="controls" style="margin-left: 80px;">
                            <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(hrUser.userName)?if_exists}</label>
                        </div>
                    </div>
                </td>
                <td>
                    <div class="control-group">
                        <label class="control-label" for="hrUser.userNo" style="width: 60px;">员工编号</label>

                        <div class="controls" style="margin-left: 80px;">
                            <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(hrUser.userNo)?if_exists}</label>
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="control-group">
                        <label class="control-label" for="hrUser.dept" style="width: 60px;">所属部门</label>

                        <div class="controls" style="margin-left: 80px;">
                            <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(hrUser.deptId.deptName)?if_exists}</label>
                        </div>
                    </div>
                </td>
                <td>
                    <div class="control-group">
                        <label class="control-label" for="hrUser.jobName" style="width: 60px;">担任岗位</label>

                        <div class="controls" style="margin-left: 80px;">
                            <label style="margin-top: 5px;padding-left:5px;font-size: 12px;">${(hrUser.jobName)?if_exists}</label>
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <div class="control-group">
                        <label class="control-label" for="hrUser.userEmail" style="width: 60px;">公司邮箱</label>

                        <div class="controls" style="margin-left: 80px;">
                            <input type="text" id="hrUser.userEmail" name="hrUser.userEmail" placeholder="公司邮箱">&nbsp;@dqy.com.cn
                            <#if emailErrorCode?exists>
                                <#if emailErrorCode==-303>
                                <span style="color: #ff0000;font-weight: bold;">用户已存在</span>
                                <#elseif emailErrorCode==-501>
                                    <span style="color: #ff0000;font-weight: bold;">由于当前用户数或邮箱空间已超出限制，不能添加</span>
                                <#elseif emailErrorCode==-4602>
                                    <span style="color: #ff0000;font-weight: bold;">超出域总空间限制</span>
                                </#if>
                            </#if>
                            <span class="help-inline"></span>
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <div class="control-group">
                        <label class="control-label" for="hrUser.emailPwd" style="width: 60px;">邮箱密码</label>
                        <div class="controls" style="margin-left: 80px;">
                            <input type="password" id="hrUser.emailPwd" name="hrUser.emailPwd">
                            <span class="help-inline"></span>
                        </div>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
        <input type="hidden" name="id" id="id" value="${hrUser.id?c}">
        <@c.token/>
    </form>
    </@c.joddForm>
</@common.html>