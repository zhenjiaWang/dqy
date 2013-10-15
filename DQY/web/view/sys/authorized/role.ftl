<#import "/view/template/common.ftl" as common>
<#import "/view/common/core.ftl" as c>
<@common.html>
<link href="/css/validator/validator.css" rel="stylesheet"/>
<script type="text/javascript" src="/js/webutils/webutils.validator.js"></script>
<script type="text/javascript" src="/js/webutils/reg.js"></script>
<script type="text/javascript">
    var submited = false;


    function dataSubmit(){
        if (!submited) {
            document.editForm.submit();
            submited = true;
        }
    }
    $(document).ready(function () {
    <#if myRoleList?exists&&myRoleList?size gt 0>
        <#list myRoleList as myRole>
        $('input[value="${myRole?if_exists}"]').attr("checked",'true');
        </#list>
    </#if>
    });
</script>
    <form class="form-horizontal" action="/sys/authorized!saveRole.dhtml" method="POST" name="editForm"
          id="editForm">
        <table width="90%">
            <tbody>
            <tr>
                <th><strong>姓名：</strong></th>
                <td>${(sysAuthorized.userId.userName)?if_exists}</td>
                <th><strong>部门：</strong></th>
                <td>${(sysAuthorized.userId.deptId.deptName)?if_exists}</td>
            </tr>
            </tbody>
        </table>

        <#if roleList?exists&&roleList?size gt 0>
            <div class="pop-in mart10">
                <table width="90%" class="sq-tb">
                    <tbody>
                        <#list roleList as role>
                            <#assign odd=role_index%3/>
                            <#if odd==0>
                            <tr>
                            </#if>
                            <td style="width: 123px;">
                                <label>
                                    <input type="checkbox"  name="roleIds" value="${role?if_exists}" <#if !role_has_next> checked="checked" disabled</#if> > ${roleNameMap[role]?if_exists}
                                </label>
                            </td>
                            <#if odd==2>
                            </tr>
                            <#elseif !role_has_next>
                                <#list 1..((3-odd)-1) as bq>
                                <td style="width: 123px;">&nbsp;</td>
                                </#list>
                            </tr>
                            </#if>
                        </#list>
                    </tbody>
                </table>
            </div>
        </#if>
        <input type="hidden" name="id" id="id" value="${sysAuthorized.id?c}">
        <@c.token/>
    </form>
</@common.html>