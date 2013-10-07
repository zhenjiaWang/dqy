<#import "/view/template/common.ftl" as common>
<#import "/view/common/core.ftl" as c>
<@common.html>
<link href="/css/validator/validator.css" rel="stylesheet"/>
<script type="text/javascript" src="/js/webutils/webutils.validator.js"></script>
<script type="text/javascript" src="/js/webutils/reg.js"></script>
<script type="text/javascript">
    var submited = false;


    $(document).ready(function () {
        $('#myModalOkBtn','#myModal').off('click').on('click',function(){
            if (!submited) {
                document.editForm.submit();
                submited = true;
            }
        });
    });
</script>
    <@c.joddForm bean="sysOrg" scope="request">
    <form class="form-horizontal" action="/sys/authorized!save.dhtml" method="POST" name="editForm"
          id="editForm">
        <table width="100%">
            <tbody>
            <tr>
                <th><strong>姓名：</strong></th>
                <td>${hrUser.userName?if_exists}</td>
                <th><strong>部门：</strong></th>
                <td>${(hrUser.deptId.deptName)?if_exists}</td>
            </tr>
            </tbody>
        </table>
        <#if orgList?exists&&orgList?size gt 0>
            <div class="pop-in mart10">
                <table width="100%" class="sq-tb">
                    <tbody>
                        <#list orgList as org>
                            <#assign odd=org_index%3/>
                            <#if odd==0>
                            <tr>
                            </#if>
                            <td style="width: 123px;"><label><input type="checkbox"  name="orgId" value="${org.id?c}"> ${org.orgName?if_exists}</label></td>
                            <#if odd==2>
                            </tr>
                            <#elseif !org_has_next>
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
        <input type="hidden" name="userId" id="userId" value="${hrUser.id?c}">
        <@c.token/>
    </form>
    </@c.joddForm>
</@common.html>