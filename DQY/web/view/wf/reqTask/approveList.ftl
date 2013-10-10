<#import "/view/template/structure/wf/wfCommon.ftl" as wfCommon>
<#import "/view/template/page.ftl" as pager>
<#import "/view/common/core.ftl" as c>
<@wfCommon.wf_common>

<script type="text/javascript">
    function reload(){
        document.location.reload();
    }
    function pagerAction(start, rows) {
        var searchUrl = '/wf/reqTask!approveList.dhtml';
        searchUrl += '?start=' + start + '&rows=' + rows;
        searchUrl = encodeURI(searchUrl);
        document.location.href = searchUrl
    }
    $(document).ready(function () {
        $('.taskReq').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                document.location.href='/wf/reqTask!process.dhtml?id='+uid;
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
            <th width="60">查看</th>
            <th width="110">日期</th>
            <th width="50">操作</th>
        </tr>
        </thead>
        <tbody>
            <#if reqTaskList?exists&&reqTaskList?size gt 0>
                <#list reqTaskList as reqTask>
                <tr <#if (reqTask_index+1)%2!=0>class="oddBgColor"</#if>>
                    <td style="text-align: center;">${(reqTask.reqId.reqNo)?if_exists}</td>
                    <td>${(reqTask.reqId.subject)?if_exists}</td>
                    <td style="text-align: center;">${(reqTask.reqId.userId.userName)?if_exists}</td>
                    <td style="text-align: center;">
                        <#if reqTask.taskRead==0>
                        未查看
                        <#elseif reqTask.taskRead==1>
                            已查看
                        </#if>
                    </td>
                    <td style="text-align: center;">${(reqTask.receiveDate)?string("yyyy-MM-dd HH:mm")}</td>
                    <td style="text-align: center;">
                        <span style="cursor: pointer;" class="taskReq"  uid="${reqTask.id?c}"><i class="icon-hand-down"></i>审批</span>
                    </td>
                </tr>
                </#list>
            </#if>
        </tbody>
    </table>
</div>
    <@pager.pagerCommon object=pageObj?if_exists max=10/>

</@wfCommon.wf_common>