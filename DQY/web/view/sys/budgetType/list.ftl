<#import "/view/template/structure/sys/sysCommon.ftl" as sysCommon>
<#import "/view/template/page.ftl" as pager>
<#import "/view/common/core.ftl" as c>
<@sysCommon.sys_common>

<script type="text/javascript">
    function pagerAction(start, rows) {
        var searchUrl = '/sys/budgetType.dhtml';
        searchUrl += '?start=' + start + '&rows=' + rows;
        var keyword=$('#keyword').val();
        if(keyword){
            searchUrl+='&keyword='+keyword;
        }
        searchUrl = encodeURI(searchUrl);
        document.location.href = searchUrl
    }
    $(document).ready(function () {
        $('#newBtn').off('click').on('click', function () {
            WEBUTILS.popWindow.createPopWindow(520, 300, '创建费用类别', '/sys/budgetType!input.dhtml');
        });
        $('.editBT').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                WEBUTILS.popWindow.createPopWindow(520, 300, '编辑费用类别', '/sys/budgetType!input.dhtml?id='+uid);
            }
        });
        $('.stopBT').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                document.location.href='/sys/budgetType!stop.dhtml?id='+uid;
            }
        });
        $('.playBT').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                document.location.href='/sys/budgetType!play.dhtml?id='+uid;
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
        <input type="text"  class="span2" id="keyword" name="keyword" value="${keyword?if_exists}" placeholder="费用类别" >
        <button type="button" class="btn"  id="searchBtn"><i class="icon-search"></i> 搜索</button>
    </div>
    <button class="btn btn-warning floatright marr10" type="button" id="newBtn">新增</button>
</div>
<!--搜索over-->

<div style="min-height: 419px;height: 419px;" class="mart10">
    <table class="table table-bordered table-hover tableBgColor">
        <thead>
        <tr class="thColor">
            <th>费用类别</th>
            <th width="80">启用</th>
            <th width="100">操作</th>
        </tr>
        </thead>
        <tbody>
            <#if budgetTypeList?exists&&budgetTypeList?size gt 0>
                <#list budgetTypeList as budgetType>
                <tr <#if (budgetType_index+1)%2!=0>class="oddBgColor"</#if>>
                    <td>${budgetType.expenseType?if_exists}</td>
                    <td style="text-align: center;"><#if budgetType.useYn=="Y">是<#else>否</#if></td>
                    <td style="text-align: center;">
                        <span style="cursor: pointer;" class="editBT"  uid="${budgetType.id?c}"><i class="icon-edit"></i>编辑</span>
                        <#if budgetType.useYn=="Y">
                            <span style="cursor: pointer;" class="stopBT"  uid="${budgetType.id?c}"><i class="icon-pause"></i>停用</span>
                        <#else>
                            <span style="cursor: pointer;" class="playBT"  uid="${budgetType.id?c}"><i class="icon-play"></i>启用</span>
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