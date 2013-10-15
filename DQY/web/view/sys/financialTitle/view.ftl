<#import "/view/template/common.ftl" as common>
<#import "/view/common/core.ftl" as c>
<@common.html>
<link href="/css/validator/validator.css" rel="stylesheet"/>
<script type="text/javascript" src="/js/webutils/webutils.validator.js"></script>
<script type="text/javascript" src="/js/webutils/reg.js"></script>
<script type="text/javascript">
    var submited = false;
    $(document).ready(function () {
        $('#btnDelete').off('click').on('click', function () {
            WEBUTILS.alert.alertComfirm('删除数据','您确认要删除该部门?',function(){
                document.location.href='/sys/financialTitle!delete.dhtml?id=${sysFinancialTitle.id?c}';
            });
        });
        $('#newBtn').off('click').on('click', function () {
            document.location.href='/sys/financialTitle!input.dhtml?parentId=${sysFinancialTitle.id?c}';
        });
        $('#editBtn').off('click').on('click', function () {
            document.location.href='/sys/financialTitle!input.dhtml?id=${sysFinancialTitle.id?c}';
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
        <form class="form-horizontal" action="" method="POST" name="editForm"
              id="editForm">
                <div class="control-group">
                    <label class="control-label" for="sysFinancialTitle.org">机构名称</label>
                    <div class="controls">
                        <p class="muted" style="font-size: 14px;line-height: 20px;margin-top: 4px;">${(sysFinancialTitle.orgId.orgName)?if_exists}</p>
                    </div>
                </div>
                <#if parentTitle?exists>
                    <div class="control-group">
                        <label class="control-label" for="sysFinancialTitle.parent">上级科目</label>
                        <div class="controls">
                            <p class="muted" style="font-size: 14px;line-height: 20px;margin-top: 4px;">${parentTitle.titleName?if_exists}</p>
                        </div>
                    </div>
                </#if>
                <div class="control-group">
                    <label class="control-label" for="sysFinancialTitle.titleNo">科目编码</label>
                    <div class="controls">
                        <p class="muted" style="font-size: 14px;line-height: 20px;margin-top: 4px;">${sysFinancialTitle.titleNo?if_exists}</p>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="sysFinancialTitle.titleName">科目名称</label>
                    <div class="controls">
                        <p class="muted" style="font-size: 14px;line-height: 20px;margin-top: 4px;">${sysFinancialTitle.titleName?if_exists}</p>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="sysFinancialTitle.onLoan">余额方向</label>
                    <div class="controls">
                        <p class="muted" style="font-size: 14px;line-height: 20px;margin-top: 4px;">
                            <#if sysFinancialTitle.onLoan==0>借<#else>贷</#if>
                        </p>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="sysFinancialTitle.displayOrder">显示顺序</label>
                    <div class="controls">
                        <p class="muted" style="font-size: 14px;line-height: 20px;margin-top: 4px;">${sysFinancialTitle.displayOrder?c}</p>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" for="sysFinancialTitle.useYn">状态</label>
                    <div class="controls">
                        <p class="muted" style="font-size: 14px;line-height: 20px;margin-top: 4px;"> <#if sysFinancialTitle.useYn=="Y">是<#else>否</#if></p>
                    </div>
                </div>
                <@c.token/>
                <#--<#if childCount==0>-->
                    <#--<button class="btn btn-danger floatright" type="button" id="btnDelete">删除</button>-->
                <#--</#if>-->
                <button class="btn btn-info floatright marr10" type="button" id="editBtn">编辑</button>
                <button class="btn btn-warning floatright marr10" type="button" id="newBtn">新增</button>
        </form>
        </#if>
    </#if>
</@common.html>