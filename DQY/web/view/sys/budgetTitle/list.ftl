<#import "/view/template/structure/sys/sysCommon.ftl" as sysCommon>
<#import "/view/template/page.ftl" as pager>
<#import "/view/common/core.ftl" as c>
<@sysCommon.sys_common>

<script type="text/javascript">
    function pagerAction(start, rows) {
        var searchUrl = '/sys/budgetTitle.dhtml';
        searchUrl += '?start=' + start + '&rows=' + rows;
        searchUrl = encodeURI(searchUrl);
        document.location.href = searchUrl
    }
    $(document).ready(function () {
        $('#newBtn').off('click').on('click', function () {
            WEBUTILS.popWindow.createPopWindow(550, null, '创建预算科目', '/sys/budgetTitle!input.dhtml');
        });
        $('.editBT').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                WEBUTILS.popWindow.createPopWindow(550, null, '编辑预算科目', '/sys/budgetTitle!input.dhtml?id='+uid);
            }
        });
        $('.stopBT').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                document.location.href='/sys/budgetTitle!stop.dhtml?id='+uid;
            }
        });
        $('.playBT').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                document.location.href='/sys/budgetTitle!play.dhtml?id='+uid;
            }
        });
        $('#budgetOwenBtn').off('click').on('click', function () {
            WEBUTILS.popWindow.createPopWindow(650, null, '编辑科目', '/sys/budgetOwen.dhtml');
        });
    });
</script>
<!--搜索begin-->
<div class="r-top clearfix">
    <div class="input-append">
        <input type="text" id="appendedInputButton" class="span2">
        <button type="button" class="btn"><i class="icon-search"></i> 搜索</button>
    </div>
    <button class="btn btn-danger floatright" type="button" id="budgetOwenBtn">科目关系调整</button>
    <button class="btn btn-warning floatright marr10" type="button" id="newBtn">新增</button>
</div>
<!--搜索over-->

<div style="min-height: 419px;height: 419px;" class="mart10">
    <table class="table table-bordered table-hover tableBgColor">
        <thead>
        <tr class="thColor">
            <th>预算科目</th>
            <th>预算类别</th>
            <th width="120">预算部门</th>
            <th width="80">启用</th>
            <th width="100">操作</th>
        </tr>
        </thead>
        <tbody>
            <#if budgetTitleList?exists&&budgetTitleList?size gt 0>
                <#list budgetTitleList as budgetTitle>
                <tr <#if (budgetTitle_index+1)%2!=0>class="oddBgColor"</#if>>
                    <td>${budgetTitle.titleName?if_exists}</td>
                    <td>${(budgetTitle.typeId.expenseType)?if_exists}</td>
                    <td>${(budgetTitle.typeId.deptId.deptName)?if_exists}</td>
                    <td style="text-align: center;"><#if budgetTitle.useYn=="Y">是<#else>否</#if></td>
                    <td style="text-align: center;">
                        <span style="cursor: pointer;" class="editBT"  uid="${budgetTitle.id?c}"><i class="icon-edit"></i>编辑</span>
                        <#if budgetTitle.useYn=="Y">
                            <span style="cursor: pointer;" class="stopBT"  uid="${budgetTitle.id?c}"><i class="icon-pause"></i>停用</span>
                        <#else>
                            <span style="cursor: pointer;" class="playBT"  uid="${budgetTitle.id?c}"><i class="icon-play"></i>启用</span>
                        </#if>
                    </td>
                </tr>
                </#list>
            </#if>
        </tbody>
    </table>
</div>
    <@pager.pagerCommon object=pageObj?if_exists max=10/>
</@sysCommon.sys_common>