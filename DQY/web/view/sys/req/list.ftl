<#import "/view/template/structure/sys/sysCommon.ftl" as sysCommon>
<#import "/view/template/page.ftl" as pager>
<#import "/view/common/core.ftl" as c>
<@sysCommon.sys_common>

<script type="text/javascript">
    function reload(){
        document.location.reload();
    }
    function pagerAction(start, rows) {
        var searchUrl = '/wf/req!adminList.dhtml';
        searchUrl += '?start=' + start + '&rows=' + rows;
        searchUrl = encodeURI(searchUrl);
        document.location.href = searchUrl
    }
    $(document).ready(function () {
        $('.deleteReq').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                document.location.href='/wf/req!delete.dhtml?id='+uid;
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
</div>
<!--搜索over-->

<div style="min-height: 419px;height: 419px;" class="mart10">
    <table class="table table-bordered table-hover tableBgColor">
        <thead>
        <tr class="thColor">
            <th width="80">申请编号</th>
            <th>标题</th>
            <th width="60">申请人</th>
            <th width="110">日期</th>
            <th width="50">操作</th>
        </tr>
        </thead>
        <tbody>
            <#if reqList?exists&&reqList?size gt 0>
                <#list reqList as req>
                <tr <#if (req_index+1)%2!=0>class="oddBgColor"</#if>>
                    <td style="text-align: center;">${req.reqNo?if_exists}</td>
                    <td>${req.subject?if_exists}</td>
                    <td style="text-align: center;">${(req.userId.userName)?if_exists}</td>
                    <td style="text-align: center;">${(req.sendDate)?string("yyyy-MM-dd HH:mm")}</td>
                    <td style="text-align: center;">
                        <span style="cursor: pointer;" class="deleteReq"  uid="${req.id?c}"><i class="icon-trash"></i>删除</span>
                    </td>
                </tr>
                </#list>
            </#if>
        </tbody>
    </table>
</div>
    <@pager.pagerCommon object=pageObj?if_exists max=10/>

</@sysCommon.sys_common>