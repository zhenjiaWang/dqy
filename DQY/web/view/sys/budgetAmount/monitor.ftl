<#import "/view/template/structure/budget/budgetCommon.ftl" as budgetCommon>
<#import "/view/common/core.ftl" as c>

<@budgetCommon.budget_common>
<link href="/css/validator/validator.css" rel="stylesheet"/>
<script type="text/javascript" src="/js/webutils/webutils.validator.js"></script>
<script type="text/javascript" src="/js/webutils/reg.js"></script>


<link type="text/css" href="/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<script src="/js/jquery.ztree.all-3.5.min.js"></script>

<script type="text/javascript">
    function search() {
        var currentYear = $('#currentYear').val();
        document.location.href = '/sys/budgetAmount.dhtml?currentYear=' + currentYear;
    }
    $(document).ready(function () {
        $('#currentYear').change(function () {
            search();
        });
    });
</script>

<!--搜索begin-->
<form class="form-horizontal" action="##" method="POST" name="editForm"
      id="editForm">
    <div class="r-top clearfix">
        <input type="text" id="deptName" name="deptName" placeholder="预算部门" <#if hrDepartment?exists>
               value="${(hrDepartment.deptName)?if_exists}" </#if>disabled>
        <select class="span2 marl15" id="currentYear" name="currentYear">
            <#if yearList?exists&&yearList?size gt 0>
                <#list yearList as year>
                    <option value="${year?c}" <#if currentYear?exists&&currentYear==year>
                            selected="selected" </#if>>${year?c}年
                    </option>
                </#list>
            </#if>
        </select>
        <input type="hidden" id="deptId" name="deptId" <#if hrDepartment?exists> value="${hrDepartment.id?c}" </#if>/>
    </div>
    <!--搜索over-->
    <div style="min-height: 419px;" class="mart10">
        <table class="application nomar">
            <tbody>
                <#if sysBudgetAmount?exists>
                <tr>
                    <td colspan="2">
                        <div class="process-bar noborder nopadding nomar">
                            <table class="table  nomar">
                                <tbody>
                                <tr>
                                    <td>${currentYear?c}-01-01 至 ${currentYear?c}-12-01</td>
                                    <td>创建人：${sysBudgetAmount.createdBy?if_exists}</td>
                                    <td>制作时间：${sysBudgetAmount.created?string("yyyy-MM-dd HH:mm:ss")}</td>
                                    <td>修改人：${sysBudgetAmount.updatedBy?if_exists}</td>
                                    <td>修改时间：${sysBudgetAmount.updated?string("yyyy-MM-dd HH:mm:ss")}</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </td>
                </tr>
                </#if>
            <tr>
                <td colspan="2">
                    <table class="table nomar">
                        <tbody>
                        <tr>
                            <td class="nopadding p-top5"><span
                                    class="label label-info yearAmountTotal">预算总额：${totalAmount?if_exists}</span></td>
                        </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <div class="scroll-x mart10">
                    <div class="top-fied">
                        <table class="layout table table-bordered table-hover tableBgColor nomar nopadding ">
                            <thead>
                            <tr>
                                <td width="103"><strong>费用类别</strong></td>
                                <td width="124"><strong>费用名称</strong></td>
                                <td width="86"><strong>年汇总</strong></td>
                                <td width="86"><strong>1月</strong></td>
                                <td width="86"><strong>2月</strong></td>
                                <td width="86"><strong>3月</strong></td>
                                <td width="86"><strong>4月</strong></td>
                                <td width="86"><strong>5月</strong></td>
                                <td width="86"><strong>6月</strong></td>
                                <td width="86"><strong>7月</strong></td>
                                <td width="86"><strong>8月</strong></td>
                                <td width="86"><strong>9月</strong></td>
                                <td width="86"><strong>10月</strong></td>
                                <td width="86"><strong>11月</strong></td>
                                <td width="86"><strong>12月</strong></td>
                            </tr>
                            </thead>
                        </table>
                    </div>


                        <div class="scroll-x-inner">
                        <table class="layout table table-bordered table-hover tableBgColor nomar nopadding table-bordered-amt">
                        <tbody>
                        <#if tempBudgetAmountList?exists&&tempBudgetAmountList?size gt 0>
                        <#list tempBudgetAmountList as budgetAmount>
                        <tr>

                            <td width="235">
                            ${(budgetAmount.sysBudgetType.expenseType)?if_exists}
                            </td>
                            <td width="288">
                            ${(budgetAmount.sysFinancialTitle.titleName)?if_exists}
                            </td>
                            <td width="171">
                                0
                            </td>
                            <#list 1..12 as month>
                                <td width="200">
                                ${(budgetAmount["monthAmount"+month])?if_exists}
                                </td>
                            </#list>
                        </tr>
                        </#list>
                        </#if>
                        </tbody>
                        </table>
                        </div>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
        <input type="hidden" id="rows" name="rows"/>
    </div>
</form>

</@budgetCommon.budget_common>