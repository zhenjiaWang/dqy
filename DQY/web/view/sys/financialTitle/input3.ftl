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
        var parentId=$('#sysFinancialTitle\\.parentId\\.id').val();
        <#if !sysFinancialTitle?exists>
            url = '/sys/financialTitle!validateName.dhtml?parentId='+parentId;
        <#elseif sysFinancialTitle?exists>
            url = '/sys/financialTitle!validateName.dhtml?ignore=${sysFinancialTitle.titleName?if_exists}&parentId='+parentId;
        </#if>
        return url;
    }

    function validatorNo() {
        var url = '';
        <#if !sysFinancialTitle?exists>
            url = '/sys/financialTitle!validateNo.dhtml';
        <#elseif sysFinancialTitle?exists>
            url = '/sys/financialTitle!validateNo.dhtml?ignore=${sysFinancialTitle.titleNo?if_exists}';
        </#if>
        return url;
    }
    function getTitileList(){
        var parentId=$('#firstLevel1').val();
        $.ajax({
            type:'GET',
            url:'/sys/financialTitle!getTitleList.dhtml?parentId='+parentId,
            dataType:'json',
            success:function (jsonData) {
                if (jsonData) {
                    if (jsonData['result'] == '0') {
                        var titleList=jsonData['titleList'];
                        if(titleList){
                            $('#sysFinancialTitle\\.parentId\\.id').empty();
                            $(titleList).each(function(i,o){
                                $('#sysFinancialTitle\\.parentId\\.id').append('<option value="'+o['id']+'">'+ o['name']+'</option>');
                            });
                            buildTitleNo();
                        }
                    }
                }
            },
            error:function (jsonData) {

            }
        });
    }

    function buildTitleNo(){
        var parentId=$('#sysFinancialTitle\\.parentId\\.id').val();
        $.ajax({
            type:'GET',
            url:'/sys/financialTitle!buildTitleNo.dhtml?parentId='+parentId,
            dataType:'json',
            success:function (jsonData) {
                if (jsonData) {
                    if (jsonData['result'] == '0') {
                        $('#sysFinancialTitle\\.titleNo').val(jsonData['autoTitleNo']);
                        $('#sysFinancialTitle\\.displayOrder').val(jsonData['autoMaxDisplayOrder']);
                    }
                }
            },
            error:function (jsonData) {

            }
        });
    }
    function initValidator() {
        WEBUTILS.validator.init({
            modes: [
                {
                    id: 'sysFinancialTitle\\.titleNo',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'},
                        {
                            type:'ajax',
                            exp:validatorNo(),
                            msg:'不能重复'
                        }
                    ]
                },
                {
                    id: 'sysFinancialTitle\\.titleName',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'},
                        {
                            type:'ajax',
                            exp:validatorName(),
                            msg:'不能重复'
                        }
                    ]
                },
                {
                    id: 'sysFinancialTitle\\.displayOrder',
                    required: true,
                    pattern: [
                        {type: 'int', exp: '==', msg: '不能为空'}
                    ]
                }
            ]
        }, true);
    }


    $(document).ready(function () {
        initValidator();
        $('#sysFinancialTitle\\.parentId\\.id').change(function(){
            buildTitleNo();
        });
        $('#firstLevel1').change(function(){
            getTitileList();
        });
        $('#myModalOkBtn','#myModal').off('click').on('click',function(){
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
    <@c.joddForm bean="sysFinancialTitle" scope="request">
    <form class="form-horizontal" action="/sys/financialTitle!save.dhtml" method="POST" name="editForm"
          id="editForm">
        <div class="control-group">
            <label class="control-label" for="sysFinancialTitle.orgId">机构名称</label>
            <div class="controls">
                <input type="text" id="sysFinancialTitle.orgId" name="sysFinancialTitle.orgId"  value="${sysOrg.orgName?if_exists}" disabled>
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="sysOrg.tl">科目级别</label>
            <div class="controls">
                <input type="text" id="sysFinancialTitle.tl" name="sysFinancialTitle.tl"  value="三级科目" disabled>
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="firstLevel1">一级科目</label>
            <div class="controls">
                <select id="firstLevel1" name="firstLevel1">
                    <#if titleList?exists&&titleList?size gt 0>
                        <#list titleList as ft>
                            <option value="${ft.id?c}" <#if lv1Id?exists&&lv1Id==ft.id>selected="selected" </#if>>${ft.titleName}</option>
                        </#list>
                    </#if>
                </select>
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="sysFinancialTitle.parentId.id">二级科目</label>
            <div class="controls">
                <select id="sysFinancialTitle.parentId.id" name="sysFinancialTitle.parentId.id">
                    <#if financialTitleList?exists&&financialTitleList?size gt 0>
                    <#list financialTitleList as ft>
                        <option value="${ft.id?c}">${ft.titleName}</option>
                    </#list>
                    </#if>
                </select>
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="sysFinancialTitle.titleNo">科目编码</label>
            <div class="controls">
                <input type="text" id="sysFinancialTitle.titleNo" name="sysFinancialTitle.titleNo" placeholder="科目编码"
                    <#if !sysFinancialTitle?exists> value="${autoTitleNo?if_exists}" </#if>>
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="sysFinancialTitle.titleName">科目名称</label>
            <div class="controls">
                <input type="text" id="sysFinancialTitle.titleName" name="sysFinancialTitle.titleName" placeholder="科目名称">
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="sysFinancialTitle.titleName">显示顺序</label>
            <div class="controls">
                <input type="text" id="sysFinancialTitle.displayOrder" name="sysFinancialTitle.displayOrder" placeholder="显示顺序"
                       <#if !sysFinancialTitle?exists>value="${autoMaxDisplayOrder?c}" </#if>>
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <label class="checkbox">
                    <input type="checkbox" value="Y" id="sysFinancialTitle.useYn" name="sysFinancialTitle.useYn"> 启用
                </label>
            </div>
        </div>
        <input type="hidden" name="sysFinancialTitle.id" id="sysFinancialTitle.id">
        <input type="hidden" name="sysFinancialTitle.titleLevel" id="sysFinancialTitle.titleLevel" value="3">
        <@c.token/>
    </form>
    </@c.joddForm>
</@common.html>