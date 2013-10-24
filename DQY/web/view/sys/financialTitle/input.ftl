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
        <#if !sysFinancialTitle?exists>
            url = '/sys/financialTitle!validateName.dhtml';
        <#elseif sysFinancialTitle?exists>
            url = '/sys/financialTitle!validateName.dhtml?ignore=${sysFinancialTitle.titleName?if_exists}';
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

        $('#sysFinancialTitle\\.titleNo').off('keyup').on('keyup',function(){
            var value=$(this).val();
            if(value&&value!=''){
                if(value.indexOf('${autoTitleNo?if_exists}')==-1){
                    $(this).val('${autoTitleNo?if_exists}'+value);
                }
            }
        });

        <#if reloadTree?exists>
            <#if reloadTree==1>
                parent.reloadTree(false);
            <#elseif  reloadTree==2>
                parent.reloadTree(true);
            </#if>

        </#if>
    });
</script>
    <#if Session["userSession"]?exists>
        <#assign userInfo=Session["userSession"]?if_exists>
        <#if userInfo?exists>
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
                <#if parentTitle?exists>
                    <div class="control-group">
                        <label class="control-label" for="sysFinancialTitle.parent">上级科目</label>
                        <div class="controls">
                            <input id="sysFinancialTitle.parent" type="text"
                                   value="${parentTitle.titleName?if_exists}" disabled>
                        </div>
                    </div>
                </#if>
                <div class="control-group">
                    <label class="control-label" for="sysFinancialTitle.titleNo">科目编码</label>
                    <div class="controls">
                        <input type="text" id="sysFinancialTitle.titleNo" name="sysFinancialTitle.titleNo" placeholder="科目编码" >
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
                    <label class="control-label" for="sysFinancialTitle.titleName">余额方向</label>
                    <div class="controls">
                        <select id="sysFinancialTitle.onLoan" name="sysFinancialTitle.onLoan">
                            <option value="0">借</option>
                            <option value="1">贷</option>
                        </select>
                        <span class="help-inline"></span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="sysFinancialTitle.titleName">显示顺序</label>
                    <div class="controls">
                        <input type="text" id="sysFinancialTitle.displayOrder" name="sysFinancialTitle.displayOrder" placeholder="显示顺序"
                               <#if !sysFinancialTitle?exists>value="${maxDisplayOrder?c}" </#if>>
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
                <#if parentTitle?exists>
                    <input type="hidden" name="sysFinancialTitle.parentId.id" id="sysFinancialTitle.parentId.id" value="${parentTitle.id?c}">
                </#if>
                <@c.token/>
            </form>
            </@c.joddForm>
        <button class="btn btn-danger floatright" type="button">删除</button>
        <button class="btn btn-success floatright marr10" type="button" id="newBtn">保存</button>
        </#if>
    </#if>
</@common.html>