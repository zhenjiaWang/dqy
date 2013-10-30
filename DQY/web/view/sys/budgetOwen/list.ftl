<#import "/view/template/structure/sys/sysCommon.ftl" as sysCommon>
<#import "/view/template/page.ftl" as pager>
<#import "/view/common/core.ftl" as c>
<@sysCommon.sys_common>

<script type="text/javascript">
    function reload(){
        document.location.reload();
    }
    function pagerAction(start, rows) {
        var searchUrl = '/sys/budgetOwen.dhtml';
        searchUrl += '?start=' + start + '&rows=' + rows;
        var lv1Id=$('#lv1').val();
        var lv2Id=$('#lv2').val();
        var lv3Id=$('#lv3').val();
        if(lv3Id&&lv3Id!=''){
            searchUrl += '&titleId=' + lv3Id + '&titleId1=' + lv1Id+ '&titleId2=' + lv2Id+ '&titleId3=' + lv3Id;
        }else if(lv2Id&&lv2Id!=''){
            searchUrl += '&titleId=' + lv2Id + '&titleId1=' + lv1Id+ '&titleId2=' + lv2Id;
        }else if(lv1Id&&lv1Id!=''){
            searchUrl += '&titleId=' + lv1Id + '&titleId1=' + lv1Id;
        }
        searchUrl = encodeURI(searchUrl);
        document.location.href = searchUrl
    }

    function getTitileList2(){
        var parentId=$('#lv1').val();
        if(parentId&&parentId!=''){
            $.ajax({
                type:'GET',
                url:'/sys/financialTitle!getTitleList.dhtml?parentId='+parentId,
                dataType:'json',
                success:function (jsonData) {
                    if (jsonData) {
                        if (jsonData['result'] == '0') {
                            var titleList=jsonData['titleList'];
                            $('#lv2').empty();
                            $('#lv2').append('<option value="">[请选择]</option>');
                            if(titleList){
                                $(titleList).each(function(i,o){
                                    $('#lv2').append('<option value="'+o['id']+'">'+ o['name']+'</option>');
                                });
                                <#if titleId2?exists>
                                $('#lv2').val('${titleId2?c}');
                                </#if>
                                getTitileList3();
                            }
                        }
                    }
                },
                error:function (jsonData) {

                }
            });
        }
    }
    function getTitileList3(){
        var parentId=$('#lv2').val();
        if(parentId&&parentId!=''){
            $.ajax({
                type:'GET',
                url:'/sys/financialTitle!getTitleList.dhtml?parentId='+parentId,
                dataType:'json',
                success:function (jsonData) {
                    if (jsonData) {
                        if (jsonData['result'] == '0') {
                            var titleList=jsonData['titleList'];
                            $('#lv3').empty();
                            $('#lv2').append('<option value="">[请选择]</option>');
                            if(titleList){
                                $(titleList).each(function(i,o){
                                    $('#lv3').append('<option value="'+o['id']+'">'+ o['name']+'</option>');
                                });
                                <#if titleId3?exists>
                                    $('#lv3').val('${titleId3?c}');
                                </#if>
                            }
                        }else{

                        }
                    }
                },
                error:function (jsonData) {

                }
            });
        }
    }
    $(document).ready(function () {
        $('#searchBtn').off('click').on('click', function () {
            pagerAction(0,10);
        });
        $('#newBtn').off('click').on('click', function () {
            WEBUTILS.popWindow.createPopWindow(550, 600, '增加科目关系', '/sys/budgetOwen!input.dhtml');
        });
        $('.editBT').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                WEBUTILS.popWindow.createPopWindow(550, 600, '编辑科目关系', '/sys/budgetOwen!input.dhtml?id='+uid);
            }
        });
        $('.stopBT').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                document.location.href='/sys/budgetOwen!stop.dhtml?id='+uid;
            }
        });
        $('.playBT').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                document.location.href='/sys/budgetOwen!play.dhtml?id='+uid;
            }
        });

        <#if titleList?exists&&titleList?size gt 0>
            getTitileList2();
        </#if>
        $('#lv1').change(function(){
            getTitileList2();
        });
        $('#lv2').change(function(){
            getTitileList3();
        });
    });
</script>
<!--搜索begin-->
<div class="r-top clearfix">
    一级科目
    <select class="int1 width-120" id="lv1" name="lv1">
        <option value="">[请选择]</option>
        <#if titleList?exists&&titleList?size gt 0>
            <#list titleList as title>
                <option value="${title.id?c}" <#if titleId1?exists&&titleId1==title.id> selected="selected" </#if>>${title.titleName?if_exists}</option>
            </#list>
        </#if>
    </select>
    二级科目
    <select class="int1 width-120" id="lv2" name="lv2">
        <option value="">[请选择]</option>
    </select>
    三级科目
    <select class="int1 width-120" id="lv3" name="lv3">
        <option value="">[请选择]</option>
    </select>
    <button class="btn btn-warning floatright marr10" type="button" id="newBtn">新增</button>
    <button class="btn  floatright" type="button" id="searchBtn"><i class="icon-search"></i>搜索</button>
</div>
<!--搜索over-->

<div style="min-height: 419px;height: 419px;" class="mart10">
    <table class="table table-bordered table-hover tableBgColor">
        <thead>
        <tr class="thColor">
            <th>财务科目</th>
            <th>预算科目</th>
            <th>预算类别</th>
            <th width="120">预算部门</th>
            <th width="80">启用</th>
            <th width="100">操作</th>
        </tr>
        </thead>
        <tbody>
            <#if budgetOwenList?exists&&budgetOwenList?size gt 0>
                <#list budgetOwenList as budgetOwen>
                <tr <#if (budgetOwen_index+1)%2!=0>class="oddBgColor"</#if>>
                    <td>${(budgetOwen.titleId.titleName)?if_exists}</td>
                    <td>${(budgetOwen.budgetTitle.titleName)?if_exists}</td>
                    <td>${(budgetOwen.budgetTitle.typeId.expenseType)?if_exists}</td>
                    <td>${(budgetOwen.budgetTitle.typeId.deptId.deptName)?if_exists}</td>
                    <td style="text-align: center;"><#if budgetOwen.useYn=="Y">是<#else>否</#if></td>
                    <td style="text-align: center;">
                        <span style="cursor: pointer;" class="editBT"  uid="${budgetOwen.id?c}"><i class="icon-edit"></i>编辑</span>
                        <#if budgetOwen.useYn=="Y">
                            <span style="cursor: pointer;" class="stopBT"  uid="${budgetOwen.id?c}"><i class="icon-pause"></i>停用</span>
                        <#else>
                            <span style="cursor: pointer;" class="playBT"  uid="${budgetOwen.id?c}"><i class="icon-play"></i>启用</span>
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