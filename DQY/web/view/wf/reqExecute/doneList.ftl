<#import "/view/template/structure/wf/wfCommon.ftl" as wfCommon>
<#import "/view/template/page.ftl" as pager>
<#import "/view/common/core.ftl" as c>
<@wfCommon.wf_common>

<script type="text/javascript">
    function pagerAction(start, rows) {
        var searchUrl = '/wf/reqExecute!doneList.dhtml';
        searchUrl += '?start=' + start + '&rows=' + rows;
        var keyword=$('#keyword').val();
        if(keyword){
            searchUrl+='&keyword='+keyword;
        }
        searchUrl = encodeURI(searchUrl);
        document.location.href = searchUrl
    }
    $(document).ready(function () {
        $('.viewReq').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                document.location.href='/wf/reqExecute!process.dhtml?id='+uid;
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
            <th width="50">操作</th>
        </tr>
        </thead>
        <tbody>
            <#if reqExecuteList?exists&&reqExecuteList?size gt 0>
                <#list reqExecuteList as reqExecute>
                <tr <#if (reqExecute_index+1)%2!=0>class="oddBgColor"</#if>>
                    <td style="text-align: left;" >
                        <div class="txt_hidden">${(reqExecute.reqId.reqNo)?if_exists}</div>
                    </td>
                    <td>
                        <div  class="txt_hidden" style="width: 360px;">${(reqExecute.reqId.subject)?if_exists}</div>
                    </td>
                    <td style="text-align: center;">${(reqExecute.reqId.userId.userName)?if_exists}</td>
                    <td style="text-align: center;">${(reqExecute.receiveDate)?string("yyyy-MM-dd HH:mm")}</td>
                    <td style="text-align: center;">
                        <span style="cursor: pointer;" class="viewReq"  uid="${reqExecute.id?c}"><i class="icon-file"></i>查看</span>
                    </td>
                </tr>
                </#list>
            </#if>
        </tbody>
    </table>
</div>
    <@pager.pagerCommon object=pageObj?if_exists max=10/>

</@wfCommon.wf_common>