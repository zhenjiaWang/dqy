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
        <#if !saleDept?exists>
            url = '/sale/dept!validateName.dhtml';
        <#elseif saleDept?exists>
            url = '/sale/dept!validateName.dhtml?ignore=${saleDept.deptName?if_exists}';
        </#if>
        return url;
    }


    function initValidator() {
        WEBUTILS.validator.init({
            modes: [
                {
                    id: 'saleDept\\.deptName',
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
    <@c.joddForm bean="saleDept" scope="request">
    <form class="form-horizontal" action="/sale/dept!save.dhtml" method="POST" name="editForm"
          id="editForm">
        <div class="control-group">
            <label class="control-label" for="saleDept.channelId.id">渠道名称</label>
            <div class="controls">
                <select  id="saleDept.channelId.id" name="saleDept.channelId.id">
                    <#if channelList?exists&&channelList?size gt 0>
                    <#list channelList as channel>
                        <option value="${channel.id?c}">${channel.channelName?if_exists}</option>
                    </#list>
                    </#if>
                </select>
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="saleDept.deptName">业态部门</label>
            <div class="controls">
                <input type="text" id="saleDept.deptName" name="saleDept.deptName" placeholder="业态部门">
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <label class="checkbox">
                    <input type="checkbox" value="Y" id="saleDept.useYn" name="saleDept.useYn"> 启用
                </label>
            </div>
        </div>
        <#if systemList?exists&&systemList?size gt 0>
            <div class="pop-in mart10">
                <table width="90%" class="sq-tb">
                    <tbody>
                        <#list systemList as system>
                            <#assign odd=system_index%3/>
                            <#if odd==0>
                            <tr>
                            </#if>
                            <td style="width: 123px;">
                                <label>
                                    <input type="checkbox"  name="systemIds" value="${system.id?c}" <#if system.checked?exists&&system.checked=="Y"> checked="checked"</#if> > ${system.systemName?if_exists}
                                </label>
                            </td>
                            <#if odd==2>
                            </tr>
                            <#elseif !system_has_next>
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

        <input type="hidden" name="saleDept.id" id="saleDept.id">
        <@c.token/>
    </form>
    </@c.joddForm>
</@common.html>