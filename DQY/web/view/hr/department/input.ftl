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
        <#if !hrDepartment?exists>
            url = '/hr/department!validateName.dhtml';
        <#elseif hrDepartment?exists>
            url = '/hr/department!validateName.dhtml?ignore=${hrDepartment.deptName?if_exists}';
        </#if>
        return url;
    }

    function initValidator() {
        WEBUTILS.validator.init({
            modes: [
                {
                    id: 'hrDepartment\\.deptName',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'},
                        {
                            type: 'ajax',
                            exp: validatorName(),
                            msg: '不能重复'
                        }
                    ]
                },
                {
                    id: 'hrDepartment\\.displayOrder',
                    required: true,
                    pattern: [
                        {type: 'int', exp: '==', msg: '不能为空'}
                    ]
                }
            ]
        }, true);
    }

    function submitForm() {
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
        $('#newBtn').off('click').on('click', function () {
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
    <#if Session["userSession"]?exists>
        <#assign userInfo=Session["userSession"]?if_exists>
        <#if userInfo?exists>
            <@c.joddForm bean="hrDepartment" scope="request">
            <form class="form-horizontal" action="/hr/department!save.dhtml" method="POST" name="editForm"
                  id="editForm">
                <div class="control-group">
                    <label class="control-label" for="hrDepartment.org">所属机构</label>
                    <div class="controls">
                        <input id="hrDepartment.org" type="text"
                               value="${userInfo["orgName"]?if_exists}" disabled>
                    </div>
                </div>
                <#if parentDepartment?exists>
                    <div class="control-group">
                        <label class="control-label" for="hrDepartment.parent">父级部门</label>
                        <div class="controls">
                            <input id="hrDepartment.parent" type="text"
                                   value="${parentDepartment.deptName?if_exists}" disabled>
                        </div>
                    </div>
                </#if>
                <div class="control-group">
                    <label class="control-label" for="hrDepartment.deptName">部门名称</label>
                    <div class="controls">
                        <input type="text" id="hrDepartment.deptName" name="hrDepartment.deptName" placeholder="部门名称">
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="hrDepartment.displayOrder">显示顺序</label>
                    <div class="controls">
                        <input type="text" id="hrDepartment.displayOrder" name="hrDepartment.displayOrder" placeholder="显示顺序">
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="hrDepartment.useYn">状态</label>
                    <div class="controls">
                        <label class="checkbox">
                            <input type="checkbox" value="Y" id="hrDepartment.useYn" name="hrDepartment.useYn"> 启用
                        </label>
                    </div>
                </div>
                <input type="hidden" name="hrDepartment.id" id="hrDepartment.id">
                <#if parentDepartment?exists>
                    <input type="hidden" name="hrDepartment.parentId.id" id="hrDepartment.parentId.id" value="${parentDepartment.id?c}">
                </#if>
                <@c.token/>
            </form>
            </@c.joddForm>
        <button class="btn btn-danger floatright" type="button">删除</button>
        <button class="btn btn-success floatright marr10" type="button" id="newBtn">保存</button>

        </#if>
    </#if>
</@common.html>