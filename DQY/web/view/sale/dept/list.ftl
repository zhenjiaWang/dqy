<#import "/view/template/structure/sale/saleCommon.ftl" as saleCommon>
<#import "/view/template/page.ftl" as pager>
<#import "/view/common/core.ftl" as c>
<@saleCommon.sale_common>

<script type="text/javascript">
    function pagerAction(start, rows) {
        var searchUrl = '/sale/dept.dhtml';
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
            WEBUTILS.popWindow.createPopWindow(550, null, '创建业态部门', '/sale/dept!input.dhtml');
        });
        $('.editBT').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                WEBUTILS.popWindow.createPopWindow(550, 500, '编辑业态部门', '/sale/dept!input.dhtml?id='+uid);
            }
        });

        $('.stopBT').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                document.location.href='/sale/dept!stop.dhtml?id='+uid;
            }
        });
        $('.playBT').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                document.location.href='/sale/dept!play.dhtml?id='+uid;
            }
        });
        $('.dqy-del').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                WEBUTILS.alert.alertComfirm('删除数据','您确认要删除该业态部门?',function(){
                   document.location.href='/sale/dept!delete.dhtml?id='+uid;
                });
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
        <input type="text" class="span2" id="keyword" name="keyword" value="${keyword?if_exists}" placeholder="业态部门名称" >
        <button type="button" class="btn" id="searchBtn"><i class="icon-search"></i> 搜索</button>
    </div>
    <button class="btn btn-warning floatright marr10" type="button" id="newBtn">新增</button>
</div>
<!--搜索over-->

<div style="min-height: 419px;height: 419px;" class="mart10">
    <table class="table table-bordered table-hover tableBgColor" >
        <thead>
        <tr class="thColor">
            <th width="120">所属渠道</th>
            <th >业态部门名称</th>
            <th width="80">启用</th>
            <th width="100">操作</th>
        </tr>
        </thead>
        <tbody>
            <#if deptList?exists&&deptList?size gt 0>
                <#list deptList as dept>
                <tr <#if (dept_index+1)%2!=0>class="oddBgColor"</#if>>
                    <td>${(dept.channelId.channelName)?if_exists}</td>
                    <td>${dept.deptName?if_exists}</td>
                    <td style="text-align: center;"><#if dept.useYn=="Y">是<#else>否</#if></td>
                    <td style="text-align: center;">
                        <span style="cursor: pointer;" class="editBT"  uid="${dept.id?c}"><i class="icon-edit"></i>编辑</span>
                        <#if dept.useYn=="Y">
                            <span style="cursor: pointer;" class="stopBT"  uid="${dept.id?c}"><i class="icon-pause"></i>停用</span>
                        <#else>
                            <span style="cursor: pointer;" class="playBT"  uid="${dept.id?c}"><i class="icon-play"></i>启用</span>
                        </#if>
                    </td>
                </tr>
                </#list>
            </#if>
        </tbody>
    </table>
</div>
    <@pager.pagerCommon object=pageObj?if_exists max=10/>

</@saleCommon.sale_common>