<#import "/view/template/structure/sys/sysCommon.ftl" as sysCommon>
<#import "/view/template/page.ftl" as pager>
<#import "/view/common/core.ftl" as c>
<@sysCommon.sys_common>

<script type="text/javascript">
    function pagerAction(start, rows) {
        var searchUrl = '/sys/financialTitle.dhtml';
        searchUrl += '?start=' + start + '&rows=' + rows;
        searchUrl = encodeURI(searchUrl);
        document.location.href = searchUrl
    }
    $(document).ready(function () {
        $('#newBtn3').off('click').on('click', function () {
            WEBUTILS.popWindow.createPopWindow(550, null, '创建科目', '/sys/financialTitle!input.dhtml?level=3');
            WEBUTILS.popWindow.okCallback(function () {
                submitForm();
            });
        });
        $('#newBtn2').off('click').on('click', function () {
            WEBUTILS.popWindow.createPopWindow(550, null, '创建科目', '/sys/financialTitle!input.dhtml?level=2');
            WEBUTILS.popWindow.okCallback(function () {
                submitForm();
            });
        });
        $('#newBtn1').off('click').on('click', function () {
            WEBUTILS.popWindow.createPopWindow(550, null, '创建科目', '/sys/financialTitle!input.dhtml?level=1');
            WEBUTILS.popWindow.okCallback(function () {
                submitForm();
            });
        });
        $('.editFT').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                WEBUTILS.popWindow.createPopWindow(550, null, '编辑科目', '/sys/financialTitle!input.dhtml?id='+uid);
                WEBUTILS.popWindow.okCallback(function () {
                    submitForm();
                });
            }
        });
        $('.stopFT').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                document.location.href='/sys/financialTitle!stop.dhtml?id='+uid;
            }
        });
        $('.playFT').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                document.location.href='/sys/financialTitle!play.dhtml?id='+uid;
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
    <button class="btn btn-primary floatright marr10" type="button" id="newBtn3">三级科目</button>
    <button class="btn btn-info floatright marr10" type="button" id="newBtn2">二级科目</button>
    <button class="btn btn-warning floatright marr10" type="button" id="newBtn1">一级科目</button>
</div>
<!--搜索over-->

<div style="min-height: 419px;height: 419px;" class="mart10">
    <table class="table table-bordered table-hover tableBgColor" >
        <thead>
        <tr class="thColor">
            <th >科目编号</th>
            <th width="140">科目名称</th>
            <th width="140">上级科目</th>
            <th width="80">科目级别</th>
            <th width="80">启用</th>
            <th width="100">操作</th>
        </tr>
        </thead>
        <tbody>
            <#if financialTitleList?exists&&financialTitleList?size gt 0>
                <#list financialTitleList as financialTitle>
                <tr <#if (financialTitle_index+1)%2!=0>class="oddBgColor"</#if>>
                    <td>${financialTitle.titleNo?if_exists}</td>
                    <td>${financialTitle.titleName?if_exists}</td>
                    <td>${(financialTitle.parentId.titleName)?if_exists}</td>
                    <td>
                        <#if financialTitle.titleLevel==1>
                            一级科目
                        <#elseif financialTitle.titleLevel==2>
                            二级科目
                        <#elseif financialTitle.titleLevel==3>
                            三级科目
                        </#if>
                    </td>
                    <td style="text-align: center;">
                    <#if financialTitle.useYn=="Y">
                        是
                    <#else>
                        否
                    </#if>
                    </td>
                    <td style="text-align: center;">
                        <span style="cursor: pointer;" class="editFT"  uid="${financialTitle.id?c}"><i class="icon-edit"></i>编辑</span>
                        <#if financialTitle.useYn=="Y">
                            <span style="cursor: pointer;" class="stopFT"  uid="${financialTitle.id?c}"><i class="icon-pause"></i>停用</span>
                        <#else>
                            <span style="cursor: pointer;" class="playFT"  uid="${financialTitle.id?c}"><i class="icon-play"></i>启用</span>
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