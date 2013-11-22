<#import "/view/template/structure/sys/sysCommon.ftl" as sysCommon>
<#import "/view/template/page.ftl" as pager>
<#import "/view/common/core.ftl" as c>
<@sysCommon.sys_common>

<script type="text/javascript">
    function reload(){
        document.location.reload();
    }
    function pagerAction(start, rows) {
        var searchUrl = '/sys/budgetOwen.dhtml';
        searchUrl += '?start=' + start + '&rows=' + rows;
        var keyword=$('#keyword').val();
        if(keyword){
            searchUrl+='&keyword='+keyword;
        }
        searchUrl = encodeURI(searchUrl);
        document.location.href = searchUrl
    }
    $(document).ready(function () {
        $('.editBT').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                WEBUTILS.popWindow.createPopWindow(550, 400, '编辑科目关系', '/sys/budgetOwen!input.dhtml?id='+uid);
            }
        });
        $('.addBT').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                WEBUTILS.popWindow.createPopWindow(550, 400, '添加科目关系', '/sys/budgetOwen!input.dhtml?budgetTitleId='+uid);
            }
        });
        $('.stopBT').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                document.location.href='/sys/budgetOwen!stop.dhtml?id='+uid;
            }
        });
        $('.playBT').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                document.location.href='/sys/budgetOwen!play.dhtml?id='+uid;
            }
        });
        $('#searchBtn').off('click').on('click', function () {
            pagerAction(0,10);
        });
        $('#keyword').off('keyup').on('keyup', function (e) {
            e = (e) ? e : ((window.event) ? window.event : "");
            var keyCode = e.keyCode ? e.keyCode : (e.which ? e.which : e.charCode);
            if (keyCode == 13) {
                pagerAction(0,10);
            }
        });

    });
</script>
<!--搜索begin-->
<div class="r-top clearfix">
    <div class="input-append">
        <input type="text" class="span2" id="keyword" name="keyword" value="${keyword?if_exists}" placeholder="预算项目" >
        <button type="button" class="btn" id="searchBtn"><i class="icon-search"></i> 搜索</button>
    </div>
</div>
<!--搜索over-->

<div style="min-height: 419px;height: 419px;" class="mart10">
    <table class="table table-bordered table-hover tableBgColor">
        <thead>
        <tr class="thColor">
            <th>预算项目</th>
            <th style="width: 180px;">会计科目</th>
            <th width="80">启用</th>
            <th width="100">操作</th>
        </tr>
        </thead>
        <tbody>
            <#if budgetTitleList?exists&&budgetTitleList?size gt 0>
                <#list budgetTitleList as budgetTitle>
                <tr <#if (budgetTitle_index+1)%2!=0>class="oddBgColor"</#if>>
                    <td>${(budgetTitle.titleName)?if_exists}</td>
                    <td class="txt_hidden" style="width: 180px;">
                    <#if budgetTitle.sysBudgetOwen?exists>
                    ${(budgetTitle.sysBudgetOwen.titleId.titleName)?if_exists}
                    <#else >
                    &nbsp;
                    </#if></td>
                    <td style="text-align: center;">
                        <#if budgetTitle.sysBudgetOwen?exists>
                            <#if budgetTitle.sysBudgetOwen.useYn=="Y">是<#else>否</#if>
                        <#else >
                            无关系
                        </#if>
                    </td>
                    <td style="text-align: center;">
                    <#if budgetTitle.sysBudgetOwen?exists>
                        <span style="cursor: pointer;" class="editBT"  uid="${budgetTitle.sysBudgetOwen.id?c}"><i class="icon-edit"></i>编辑</span>
                        <#if budgetTitle.sysBudgetOwen.useYn=="Y">
                            <span style="cursor: pointer;" class="stopBT"  uid="${budgetTitle.sysBudgetOwen.id?c}"><i class="icon-pause"></i>停用</span>
                        <#else>
                            <span style="cursor: pointer;" class="playBT"  uid="${budgetTitle.sysBudgetOwen.id?c}"><i class="icon-play"></i>启用</span>
                        </#if>
                    <#else >
                        <span style="cursor: pointer;" class="addBT"  uid="${budgetTitle.id?c}"><i class="icon-plus"></i>添加</span>
                    </#if>
                    </td>
                </tr>
                </#list>
            </#if>
        </tbody>
    </table>
</div>
    <@pager.pagerCommon object=pageBudgetTitle?if_exists max=10/>
</@sysCommon.sys_common>