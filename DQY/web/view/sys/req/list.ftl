<#import "/view/template/structure/sys/sysCommon.ftl" as sysCommon>
<#import "/view/template/page.ftl" as pager>
<#import "/view/common/core.ftl" as c>
<@sysCommon.sys_common>

<script type="text/javascript">
    function pagerAction(start, rows) {
        var searchUrl = '/wf/req!adminList.dhtml';
        searchUrl += '?start=' + start + '&rows=' + rows;
        var keyword=$('#keyword').val();
        if(keyword){
            searchUrl+='&keyword='+keyword;
        }
        searchUrl = encodeURI(searchUrl);
        document.location.href = searchUrl
    }
    $(document).ready(function () {
        $('.deleteReq').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                if(confirm("确认要删除这个申请吗?")){
                    document.location.href='/wf/req!delete.dhtml?id='+uid;
                }
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
        <input type="text"  class="span2" id="keyword" name="keyword" value="${keyword?if_exists}" placeholder="申请编号 标题">
        <button type="button" class="btn" id="searchBtn"><i class="icon-search"></i> 搜索</button>
    </div>
</div>
<!--搜索over-->

<div style="min-height: 419px;height: 419px;" class="mart10">
    <table class="table table-bordered table-hover tableBgColor">
        <thead>
        <tr class="thColor">
            <th width="90">申请编号</th>
            <th >标题</th>
            <th width="60">申请人</th>
            <th width="110">日期</th>
            <th width="110">操作</th>
        </tr>
        </thead>
        <tbody>
            <#if reqList?exists&&reqList?size gt 0>
                <#list reqList as req>
                <tr <#if (req_index+1)%2!=0>class="oddBgColor"</#if>>
                    <td style="text-align: left;" >
                    <div class="txt_hidden">${req.reqNo?if_exists}</div>
                    </td>
                    <td >
                        <div class="txt_hidden" style="width: 360px;">${req.subject?if_exists}</div>
                    </td>
                    <td style="text-align: center;">${(req.userId.userName)?if_exists}</td>
                    <td style="text-align: center;">${(req.sendDate)?string("yyyy-MM-dd HH:mm")}</td>
                    <td style="text-align: center;">
                        <a href="/wf/req!view.dhtml?id=${req.id?c}"><i class="icon-file"></i>查看</a>
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