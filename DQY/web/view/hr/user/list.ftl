<#import "/view/template/structure/sys/sysCommon.ftl" as sysCommon>
<#import "/view/template/page.ftl" as pager>
<#import "/view/common/core.ftl" as c>
<@sysCommon.sys_common>

<script type="text/javascript">
    function pagerAction(start, rows) {
        var searchUrl = '/hr/user.dhtml';
        searchUrl += '?start=' + start + '&rows=' + rows;
        var keyword=$('#keyword').val();
        if(keyword){
            searchUrl+='&keyword='+keyword;
        }
        searchUrl = encodeURI(searchUrl);
        document.location.href = searchUrl
    }
    $(document).ready(function () {
        $('.viewUser').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
              document.location.href='/hr/user!view.dhtml?id='+uid;
            }
        });
        $('#newBtn').off('click').on('click', function () {
            WEBUTILS.popWindow.createPopWindow(800, 450, '新增用户', '/hr/user!input.dhtml');
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
        <input type="text"  class="span2" id="keyword" name="keyword" value="${keyword?if_exists}" placeholder="员工编号 姓名" >
        <button type="button" class="btn" id="searchBtn"><i class="icon-search"></i> 搜索</button>
    </div>
    <button class="btn btn-warning floatright marr10" type="button" id="newBtn">新增</button>
</div>
<!--搜索over-->

<div style="min-height: 419px;height: 419px;" class="mart10">
    <table class="table table-bordered table-hover tableBgColor" >
        <thead>
        <tr class="thColor">
            <th width="80">员工编号</th>
            <th width="100">员工名称</th>
            <th width="100">担任岗位</th>
            <th width="100">所属部门</th>
            <th>邮箱地址</th>
            <th width="100">操作</th>
        </tr>
        </thead>
        <tbody>
            <#if userList?exists&&userList?size gt 0>
                <#list userList as user>
                <tr <#if (user_index+1)%2!=0>class="oddBgColor"</#if>>
                    <td>${user.userNo?if_exists}</td>
                    <td>${user.userName?if_exists}</td>
                    <td><div class="txt_hidden">${(user.jobName)?if_exists}</div></td>
                    <td><div class="txt_hidden">${(user.deptId.deptName)?if_exists}</div></td>
                    <td><div class="txt_hidden">
                        <#if user.userEmail?exists>
                        ${(user.userEmail)?if_exists}@dqy.com.cn
                        <#else >
                            暂无
                        </#if>
                    </div></td>
                    <td style="text-align: center;">
                        <span style="cursor: pointer;" class="viewUser"  uid="${user.id?c}"><i class="icon-user"></i>查看</span>
                    </td>
                </tr>
                </#list>
            </#if>
        </tbody>
    </table>
</div>
    <@pager.pagerCommon object=pageObj?if_exists max=10/>

</@sysCommon.sys_common>