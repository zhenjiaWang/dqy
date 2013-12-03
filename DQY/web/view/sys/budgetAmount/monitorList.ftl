<#import "/view/template/common.ftl" as common>
<#import "/view/common/core.ftl" as c>
<@common.html>
<script type="text/javascript">
</script>
<table class="table table-bordered table-hover tableBgColor" >
    <thead>
    <tr class="thColor">
        <th width="100">部门名称</th>
        <th width="120">费用类别</th>
        <th width="120">费用项目</th>
        <th width="80">金额</th>
        <th >备注</th>
    </tr>
    </thead>
    <tbody>
        <#if dailyDetailList?exists&&dailyDetailList?size gt 0>
            <#list dailyDetailList as daily>
            <tr <#if (daily_index+1)%2!=0>class="oddBgColor"</#if>>
                <td>${(daily.expenseDept.deptName)?if_exists}</td>
                <td>${(daily.expenseType.expenseType)?if_exists}</td>
                <td>${(daily.expenseTitle.titleName)?if_exists}</td>
                <td>${daily.amount?double}</td>
                <td style="text-align: left;">
                    ${daily.remarks?if_exists}
                </td>
            </tr>
            </#list>
        </#if>
        <#if paymentDetailList?exists&&paymentDetailList?size gt 0>
            <#list paymentDetailList as daily>
            <tr <#if (daily_index+1)%2!=0>class="oddBgColor"</#if>>
                <td>${(daily.expenseDept.deptName)?if_exists}</td>
                <td>${(daily.expenseType.expenseType)?if_exists}</td>
                <td>${(daily.expenseTitle.titleName)?if_exists}</td>
                <td>${daily.amount?double}</td>
                <td style="text-align: left;">
                ${daily.remarks?if_exists}
                </td>
            </tr>
            </#list>
        </#if>
    </tbody>
</table>
</@common.html>