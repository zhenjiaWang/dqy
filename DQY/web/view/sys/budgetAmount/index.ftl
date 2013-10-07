<#import "/view/template/structure/budget/budgetCommon.ftl" as budgetCommon>
<#import "/view/common/core.ftl" as c>

<@budgetCommon.budget_common>
<link href="/css/validator/validator.css" rel="stylesheet"/>
<script type="text/javascript" src="/js/webutils/webutils.validator.js"></script>
<script type="text/javascript" src="/js/webutils/reg.js"></script>

<link type="text/css" href="/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<script src="/js/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript">

    function initValidator() {
        WEBUTILS.validator.init({
            modes: [
    <#if budgetTypeList?exists&&budgetTypeList?size gt 0>
        <#if budgetTitleMap?exists&&budgetTitleMap?size gt 0>
            <#list budgetTypeList as type>
                <#assign titleList=budgetTitleMap[type.id+"_"]?if_exists>
                <#if titleList?exists&&titleList?size gt 0>
                    <#list titleList as title>
                        <#list 1..12 as c>
                            {
                                id: '${hrDepartment.id?c}_${title.id?c}_${type.id?c}_${c?c}',
                                required: true,
                                pattern: [
                                    {type: 'number', exp: '==', msg: '不能为空'}
                                ]
                            },
                        </#list>
                    </#list>
                </#if>
            </#list>
        </#if>
    </#if>
                {
                    id: 'deptName',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'}
                    ]
                }
            ]
        }, true);
    }
    var srcNode;
    var setting = {
        view: {
            selectedMulti: false
        },
        async: {
            enable: true,
            url: "/common/common!orgTreeData.dhtml",
            autoParam: ["id=parentId", "name=n", "level=lv"]
        },
        callback: {
            onClick: zTreeOnClick
        }
    };
    function zTreeOnClick(event, treeId, treeNode) {
        srcNode = treeNode;
        if (treeNode) {
            $('#deptId').val(treeNode['id']);
            $('#deptName').val(treeNode['name']);
            $('.treeDiv').fadeOut();
            search();
        }
    }

    function search() {
        var deptId = $('#deptId').val();
        if (deptId && deptId != '') {
            var currentYear = $('#currentYear').val();
            document.location.href = '/sys/budgetAmount.dhtml?deptId=' + deptId + '&currentYear=' + currentYear;
        }
    }
    $(document).ready(function () {
        initValidator();
        $.fn.zTree.init($("#treeDemo"), setting);
        $('#currentYear').change(function () {
            search();
        });
        $('#searchDept').off('click').on('click', function () {
            var left = $(this).offset().left + 10;
            var top = $(this).offset().top + 10;
            top += $(this).height();
            $('.treeDiv').css({
                        top: top,
                        left: left,
                        zIndex: 99999
                    }
            );
            $('.treeDiv').fadeIn();
            $('.treeDiv').find('div').show();
        });

        $('#saveBtn').off('click').on('click', function () {
            WEBUTILS.alert.alertComfirm('更改预算','您确认要更改当前预算?',function(){
                WEBUTILS.alert.close();
                WEBUTILS.alert.alertInfo('正在保存','请耐心等待..',100000);
                document.editForm.submit();
            });
        });
    });
</script>
<!--搜索begin-->
<form class="form-horizontal" action="/sys/budgetAmount!save.dhtml" method="POST" name="editForm"
      id="editForm">
<div class="r-top clearfix">
    <input type="text" id="deptName" name="deptName" placeholder="预算部门" <#if hrDepartment?exists>
           value="${(hrDepartment.deptName)?if_exists}" </#if>disabled>
    <span class="add-on" style="cursor: pointer;" id="searchDept"><i class="icon-th"></i></span>
    <select class="span2" id="currentYear" name="currentYear">
        <#if yearList?exists&&yearList?size gt 0>
            <#list yearList as year>
                <option value="${year?c}" <#if currentYear?exists&&currentYear==year>
                        selected="selected" </#if>>${year?c}年
                </option>
            </#list>
        </#if>
    </select>
    <button class="btn btn-danger floatright" type="button" id="resetBtn">重置</button>
    <button class="btn btn-success floatright marr10" type="button" id="saveBtn">保存</button>
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
                            <td>创建人：${sysBudgetAmount.createBy?if_exists}</td>
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
                        <td class="nopadding p-top5"><span class="label label-info">预算总额：${totalAmount?if_exists}</span></td>
                    </tr>
                    </tbody>
                </table>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <div class="scroll-x mart10">
                    <#if budgetTypeList?exists&&budgetTypeList?size gt 0>
                        <#if budgetTitleMap?exists&&budgetTitleMap?size gt 0>
                    <table class="layout table table-bordered table-hover tableBgColor nomar nopadding">
                        <thead>
                        <tr>
                            <td style="width: 160px;"><strong>归属部门</strong></td>
                            <td width="160"><strong>费用类别</strong></td>
                            <td width="160"><strong>费用名称</strong></td>
                            <td width="100"><strong>1月</strong></td>
                            <td width="100"><strong>2月</strong></td>
                            <td width="100"><strong>3月</strong></td>
                            <td width="100"><strong>4月</strong></td>
                            <td width="100"><strong>5月</strong></td>
                            <td width="100"><strong>6月</strong></td>
                            <td width="100"><strong>7月</strong></td>
                            <td width="100"><strong>8月</strong></td>
                            <td width="100"><strong>9月</strong></td>
                            <td width="100"><strong>10月</strong></td>
                            <td width="100"><strong>11月</strong></td>
                            <td width="100"><strong>12月</strong></td>
                        </tr>
                        </thead>
                        <tbody>
                                <#list budgetTypeList as type>
                                <#assign titleList=budgetTitleMap[type.id+"_"]?if_exists>
                                <#if titleList?exists&&titleList?size gt 0>
                                <#list titleList as title>
                                <tr>
                                    <td width="160">
                                        <select class="int2 width-100">
                                        <option>${hrDepartment.deptName?if_exists}</option>
                                    </select></td>
                                    <td width="160"><select class="int2 width-100">
                                        <option>${type.expenseType?if_exists}</option>
                                    </select></td>
                                    <td width="160"><select class="int2 width-100">
                                        <option>${title.titleName?if_exists}</option>
                                    </select></td>
                                    <#list 1..12 as c>
                                        <td width="100"><input type="text" id="${hrDepartment.id?c}_${title.id?c}_${type.id?c}_${c?c}" name="${hrDepartment.id?c}_${title.id?c}_${type.id?c}_${c?c}" class="int1 width-70"
                                                               <#if budgetAmountMap?exists&&budgetAmountMap?size gt 0>value="${budgetAmountMap[hrDepartment.id+"_"+title.id+"_"+type.id+"_"+c]?if_exists}</#if>"></td>
                                    </#list>
                                </tr>
                                </#list>
                                </#if>
                                </#list>
                        </tbody>
                    </table>

                        </#if>
                    </#if>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>
</form>

</@budgetCommon.budget_common>