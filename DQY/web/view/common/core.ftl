<#assign tag=JspTaglibs["/WEB-INF/tld/guiceSide.tld"]>
<#assign jodd=JspTaglibs["/WEB-INF/tld/form_tag.tld"]>
<#macro time>
    <@tag.time/>
</#macro>
<!--
-->
<#macro property value default="&nbsp;">${(value?eval)?default(default)?trim}</#macro>
<#macro propertyInt value default="0"><#if (value?eval)?exists>${(value?eval)?default(default)?c}<#else>${default}</#if></#macro>
<#macro propertyDate value format default="&nbsp;" ><#if (value?eval)?exists>${ (value?eval)?string(format)?trim}<#else>${default}</#if></#macro>
<#macro propertyObjDate object value format default="&nbsp;" ><#if  object?exists><#if object?string!=""><#if value?string!=""><#if object[value]?exists>${object[value]?string(format)}<#else>${default}</#if><#else>${default}</#if><#else>${default}</#if></#if></#macro>
<#macro propertyObjInt object value default="0" ><#if  object?exists><#if object?string!=""><#if value?string!=""><#if object[value]?exists>${object[value]?c}<#else>${default}</#if><#else>${default}</#if><#else>${default}</#if></#if></#macro>
<#macro propertyObj object value length=0 default="&nbsp;"><#if  object?exists><#if object?string!=""><#if value?string!=""><#if length==0>${object[value]?default(default)?trim}<#else ><#if object[value]?exists && object[value]?length gt length>${object[value][0..(length-1)]?default(default)?trim}<#else >${object[value]?default(default)?trim}</#if></#if><#else>${default}</#if><#else>${default}</#if></#if></#macro>

<!--
获取property 根据传入object进行解析
-->




<#macro me value>
    <#if Session["userSession"].hrEmployee.id=="${value?if_exists}">
        <#nested>
    </#if>
</#macro>

<#macro sessionProperty value>
    <@tag.sessionProperty value="${value}"/>
</#macro>


<#macro token>
    <@tag.token/>
</#macro>

<#macro tokenValue>
    <@tag.tokenValue/>
</#macro>

<#macro joddForm bean scope>
    <@jodd.form bean="${bean}" scope="${scope}">
        <#nested/>
    </@jodd.form>
</#macro>
