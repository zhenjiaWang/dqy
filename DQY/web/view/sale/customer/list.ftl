<#import "/view/template/structure/sale/saleCommon.ftl" as saleCommon>
<#import "/view/template/page.ftl" as pager>
<#import "/view/common/core.ftl" as c>
<@saleCommon.sale_common>

<script type="text/javascript">
    function pagerAction(start, rows) {
        var searchUrl = '/sale/customer.dhtml';
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
            WEBUTILS.popWindow.createPopWindow(550, null, '创建客户门店', '/sale/customer!input.dhtml');
        });
        $('.editBT').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                WEBUTILS.popWindow.createPopWindow(550, null, '编辑客户门店', '/sale/customer!input.dhtml?id='+uid);
            }
        });

        $('.stopBT').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                document.location.href='/sale/customer!stop.dhtml?id='+uid;
            }
        });
        $('.playBT').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                document.location.href='/sale/customer!play.dhtml?id='+uid;
            }
        });
        $('.dqy-del').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                WEBUTILS.alert.alertComfirm('删除数据','您确认要删除该客户门店?',function(){
                   document.location.href='/sale/customer!delete.dhtml?id='+uid;
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
        <input type="text" class="span2" id="keyword" name="keyword" value="${keyword?if_exists}" placeholder="客户门店" >
        <button type="button" class="btn" id="searchBtn"><i class="icon-search"></i> 搜索</button>
    </div>
    <button class="btn btn-warning floatright marr10" type="button" id="newBtn">新增</button>
</div>
<!--搜索over-->

<div style="min-height: 419px;height: 419px;" class="mart10">
    <table class="table table-bordered table-hover tableBgColor" >
        <thead>
        <tr class="thColor">
            <th width="120">所属系统</th>
            <th >客户门店</th>
            <th width="80">启用</th>
            <th width="100">操作</th>
        </tr>
        </thead>
        <tbody>
            <#if customerList?exists&&customerList?size gt 0>
                <#list customerList as customer>
                <tr <#if (customer_index+1)%2!=0>class="oddBgColor"</#if>>
                    <td>${(customer.systemId.systemName)?if_exists}</td>
                    <td>${customer.customerName?if_exists}</td>
                    <td style="text-align: center;"><#if customer.useYn=="Y">是<#else>否</#if></td>
                    <td style="text-align: center;">
                        <span style="cursor: pointer;" class="editBT"  uid="${customer.id?c}"><i class="icon-edit"></i>编辑</span>
                        <#if customer.useYn=="Y">
                            <span style="cursor: pointer;" class="stopBT"  uid="${customer.id?c}"><i class="icon-pause"></i>停用</span>
                        <#else>
                            <span style="cursor: pointer;" class="playBT"  uid="${customer.id?c}"><i class="icon-play"></i>启用</span>
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