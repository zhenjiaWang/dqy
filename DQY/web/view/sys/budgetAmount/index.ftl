<#import "/view/template/structure/budget/budgetCommon.ftl" as budgetCommon>
<#import "/view/common/core.ftl" as c>

<@budgetCommon.budget_common>
<link href="/css/validator/validator.css" rel="stylesheet"/>
<script type="text/javascript" src="/js/webutils/webutils.validator.js"></script>
<script type="text/javascript" src="/js/webutils/reg.js"></script>

<script type="text/javascript">

    function initValidator() {
        WEBUTILS.validator.init({
            modes: [
                    <#if sysBudgetAmountList?exists&&sysBudgetAmountList?size gt 0&&rows?exists>
                        <#list 1..rows as int>
                            <#list 1..12 as c>
                                {
                                    id: 'amount${int?c}_${c?c}',
                                    required: true,
                                    pattern: [
                                        {type: 'number', exp: '==', msg: '不能为空'}
                                    ]
                                },
                            </#list>
                        </#list>
                    <#else >
                        <#list 1..10 as int>
                            <#list 1..12 as c>
                                {
                                    id: 'amount${int?c}_${c?c}',
                                    required: true,
                                    pattern: [
                                        {type: 'number', exp: '==', msg: '不能为空'}
                                    ]
                                },
                            </#list>
                        </#list>
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


    function search() {
        var currentYear = $('#currentYear').val();
        document.location.href = '/sys/budgetAmount.dhtml?currentYear=' + currentYear;
    }
    function calSumAmount(index) {
        var sum = 0;
        var tr=$('tr[index="'+index+'"]', '.table-bordered ');
        if(tr){
            $('.amt',tr).each(function(i,o){
                var v = $(o).val();
                if (v == "") {
                    v = 0;
                }
                sum += parseFloat(v);
            });
            $('#amountTotal'+index,tr).val(sum);
        }
    }
    $(document).ready(function () {
        initValidator();
        $('#currentYear').change(function () {
            search();
        });

        $('#resetBtn').off('click').on('click', function () {
            $('input[type="text"]','.table-bordered').each(function(){
                $(this).val('0.00');
            });
        });
        $('#saveBtn').off('click').on('click', function () {
            window.setTimeout(function () {
                var passed = WEBUTILS.validator.isPassed();
                if (passed) {
                    var lastTr=$('#addTr').prev();
                    var index=$(lastTr).attr('index');
                    $('#rows').val(index);
                    WEBUTILS.alert.alertComfirm('更改预算', '您确认要更改当前预算?', function () {
                        WEBUTILS.alert.close();
                        WEBUTILS.alert.alertInfo('正在保存', '请耐心等待..', 100000);
                        document.editForm.submit();
                    });
                } else {
                    WEBUTILS.validator.showErrors();
                }
            }, 500);
        });

        $('#addBudget').off('click').on('click', function () {
            $('#addTr').before($('#addTr').prev().clone());
            var lastTr=$('#addTr').prev();
            var index=$(lastTr).attr('index');
            if(index){
                index=parseInt(index);
                $('input',lastTr).val('0.00');
                var typeObj=$('#typeId'+index,lastTr);
                if(typeObj){
                    $(typeObj).attr('id','typeId'+(index+1));
                    $(typeObj).attr('name','typeId'+(index+1));
                }

                var titleObj=$('#titleId'+index,lastTr);
                if(titleObj){
                    $(titleObj).attr('id','titleId'+(index+1));
                    $(titleObj).attr('name','titleId'+(index+1));
                }
                var amountTotalObj=$('#amountTotal'+index,lastTr);
                if(amountTotalObj){
                    $(amountTotalObj).attr('id','amountTotal'+(index+1));
                    $(amountTotalObj).attr('name','amountTotal'+(index+1));
                }
                for(var i=1;i<=10;i++){

                    var amountObj=$('#amount'+index+"_"+i,lastTr);
                    if(amountObj){
                        $(amountObj).attr('id','amount'+(index+1)+"_"+i);
                        $(amountObj).attr('name','amount'+(index+1)+"_"+i);
                        WEBUTILS.validator.addMode({
                            id: 'amount'+(index+1)+"_"+i,
                            required: true,
                            pattern: [
                                {type: 'blank', exp: '!=', msg: ''}
                            ]
                        });
                    }
                }
                $(lastTr).attr('index',(index+1));
            }
        });
        $('#deleteBudget').off('click').on('click', function () {
            var deleteTr=$('#addTr').prev();
            var index=$(deleteTr).attr('index');
            if(index){
                index=parseInt(index);
                if(index>1){
                    for(var i=1;i<=10;i++){
                        var amountObj=$('#amount'+index+"_"+i,deleteTr);
                        if(amountObj){
                            WEBUTILS.validator.removeMode({
                                id: 'amount'+index+"_"+i
                            });
                        }
                    }
                    $(deleteTr).remove();
                }
            }
        });
        <#if idSets?exists&&idSets?size gt 0>
        <#list idSets as tt>
        <#assign ids=tt?split("_")>
        <#if ids?exists>
            $('#typeId${(tt_index+1)}','.table-bordered').val('${ids[0]?if_exists}');
            $('#titleId${(tt_index+1)}','.table-bordered').val('${ids[1]?if_exists}');
            <#list 1..12 as month>
                <#if budgetAmountMap?exists&&budgetAmountMap?size gt 0>
                    <#assign amount=budgetAmountMap[ids[0]?if_exists+"_"+ids[1]?if_exists+"_"+month]?if_exists/>
                    <#if amount?exists>
                        $('#amount${(tt_index+1)}_${month?c}','.table-bordered').val('${amount?if_exists}');
                    </#if>
                </#if>
            </#list>
        </#if>
        </#list>
            $('input[type="text"]','.table-bordered').each(function(){
                if($(this).val()==''){
                    $(this).val('0.00');
                }
            });
        </#if>

        $('tr','.table-bordered').each(function(i,o){
            calSumAmount($(o).attr('index'));
            $('.amt',o).off('blur').on('blur',function(){
                calSumAmount($(o).attr('index'));
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
        <select class="span2 marl15" id="currentYear" name="currentYear">
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
                            <td class="nopadding p-top5"><span
                                    class="label label-info">预算总额：${totalAmount?if_exists}</span></td>
                        </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <div class="scroll-x mart10">
                        <#if typeList?exists&&typeList?size gt 0>
                            <#if titleList?exists&&titleList?size gt 0>
                                <table class="layout table table-bordered table-hover tableBgColor nomar nopadding">
                                    <thead>
                                    <tr>
                                        <td style="width: 160px;"><strong>归属部门</strong></td>
                                        <td width="160"><strong>费用类别</strong></td>
                                        <td width="160"><strong>会计科目</strong></td>
                                        <td width="120"><strong>年合计</strong></td>
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
                                <#if sysBudgetAmountList?exists&&sysBudgetAmountList?size gt 0&&rows?exists>
                                    <#list 1..rows as int>
                                    <tr index="${int?c}">
                                        <td width="160">
                                            <select class="int2 width-100">
                                                <option>${hrDepartment.deptName?if_exists}</option>
                                            </select>
                                        </td>
                                        <td width="160">
                                            <select class="int2 width-100" id="typeId${int?c}" name="typeId${int?c}">
                                                <#list typeList as type>
                                                    <option value="${type.id?c}">${type.expenseType?if_exists}</option>
                                                </#list>
                                            </select>
                                        </td>
                                        <td width="160">
                                            <select class="int2 width-100" id="titleId${int?c}" name="titleId${int?c}">
                                                <#list titleList as title>
                                                    <option value="${title.id?c}">${title.titleName?if_exists}(${title.parentName?if_exists})</option>
                                                </#list>
                                            </select>
                                        </td>
                                        <td width="100">
                                            <input type="text" id="amountTotal${int?c}" name="amountTotal${int?c}" class="int1 width-70" value="0.00" readonly="readonly"/>
                                        </td>
                                        <#list 1..12 as c>
                                            <td width="100">
                                                <input type="text" id="amount${int?c}_${c?c}" name="amount${int?c}_${c?c}" class="int1 width-70 amt" value="0.00"/>
                                            </td>
                                        </#list>
                                    </tr>
                                    </#list>
                                <#else >
                                    <#list 1..10 as int>
                                    <tr index="${int?c}">
                                        <td width="160">
                                            <select class="int2 width-100">
                                                <option>${hrDepartment.deptName?if_exists}</option>
                                            </select>
                                        </td>
                                        <td width="160">
                                            <select class="int2 width-100" id="typeId${int?c}" name="typeId${int?c}">
                                                <#list typeList as type>
                                                    <option value="${type.id?c}">${type.expenseType?if_exists}</option>
                                                </#list>
                                            </select>
                                        </td>
                                        <td width="160">
                                            <select class="int2 width-100" id="titleId${int?c}" name="titleId${int?c}">
                                                <#list titleList as title>
                                                    <option value="${title.id?c}">${title.titleName?if_exists}(${title.parentName?if_exists})</option>
                                                </#list>
                                            </select>
                                        </td>
                                        <td width="100">
                                            <input type="text" id="amountTotal${int?c}" name="amountTotal${int?c}" class="int1 width-70" value="0.00" readonly="readonly"/>
                                        </td>
                                        <#list 1..12 as c>
                                            <td width="100">
                                                <input type="text" id="amount${int?c}_${c?c}" name="amount${int?c}_${c?c}" class="int1 width-70 amt" value="0.00"/>
                                            </td>
                                        </#list>
                                    </tr>
                                    </#list>
                                </#if>
                                    <tr id="addTr">
                                        <td colspan="15" style="padding-left: 5px;">
                                            <button class="btn" type="button" id="addBudget">增加预算项目行</button>
                                            <button class="btn" type="button" id="deleteBudget">删除预算项目行</button>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </#if>
                        </#if>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
        <input type="hidden" id="rows" name="rows"/>
    </div>
</form>

</@budgetCommon.budget_common>