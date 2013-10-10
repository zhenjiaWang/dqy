<#import "/view/template/structure/sys/sysCommon.ftl" as sysCommon>
<#import "/view/template/page.ftl" as pager>
<#import "/view/common/core.ftl" as c>
<@sysCommon.sys_common>

<script type="text/javascript">
    function reload(){
        document.location.reload();
    }
    function pagerAction(start, rows) {
        var searchUrl = '/sys/org.dhtml';
        searchUrl += '?start=' + start + '&rows=' + rows;
        searchUrl = encodeURI(searchUrl);
        document.location.href = searchUrl
    }
    $(document).ready(function () {
        $('#newBtn').off('click').on('click', function () {
            WEBUTILS.popWindow.createPopWindow(550, null, '创建机构', '/sys/org!input.dhtml');
        });
        $('.dqy-eidt').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                WEBUTILS.popWindow.createPopWindow(550, null, '编辑机构', '/sys/org!input.dhtml?id='+uid);
            }
        });
        $('.dqy-del').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                WEBUTILS.alert.alertComfirm('删除数据','您确认要删除该组织机构?',function(){
                   document.location.href='/sys/org!delete.dhtml?id='+uid;
                });
            }
        });
    });
</script>
<!--搜索begin-->
<div class="r-top clearfix">
    <div class="input-append">
        <input type="text" id="appendedInputButton" class="span2">
        <button type="button" class="btn"><i class="icon-search"></i> 搜索</button>
    </div>
    <button class="btn btn-danger floatright" type="button">删除</button>
    <button class="btn btn-warning floatright marr10" type="button" id="newBtn">新增</button>
</div>
<!--搜索over-->

<div style="min-height: 419px;height: 419px;" class="mart10">
    <table class="table table-bordered table-hover tableBgColor" >
        <thead>
        <tr class="thColor">
            <th width="100">机构编号</th>
            <th width="120">机构名称</th>
            <th>所属集团</th>
            <th width="80">启用</th>
            <th width="100">操作</th>
        </tr>
        </thead>
        <tbody>
            <#if orgList?exists&&orgList?size gt 0>
                <#list orgList as org>
                <tr <#if (org_index+1)%2!=0>class="oddBgColor"</#if>>
                    <td>${org.orgNo?if_exists}</td>
                    <td>${org.orgName?if_exists}</td>
                    <td>${(org.groupId.groupName)?if_exists}</td>
                    <td style="text-align: center;"><#if org.useYn=="Y">是<#else>否</#if></td>
                    <td style="text-align: center;">
                        <a title="编辑" class="dqy-ico dqy-eidt" uid="${org.id?c}" href="#"></a>
                        <a title="删除" class="dqy-ico dqy-del" uid="${org.id?c}" href="#"></a>
                    </td>
                </tr>
                </#list>
            </#if>
        </tbody>
    </table>
</div>
    <@pager.pagerCommon object=pageObj?if_exists max=10/>

</@sysCommon.sys_common>