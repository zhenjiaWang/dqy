<#import "/view/template/common.ftl" as common>
<#import "/view/common/core.ftl" as c>
<@common.html>

<script type="text/javascript">
    var leftId,rightId;
    function leftEvent(){
        $('a','#budgetTitleUL').off('click').on('click',function(){
            $('#rightBtn').removeClass('disabled');
            $('#leftBtn').removeClass('disabled');
            $('#leftBtn').addClass('disabled');
            leftId=$(this).attr('uid');
        });
    }
    function rightEvent(){
        $('a','#budgetTitleOwenUL').off('click').on('click',function(){
            $('#rightBtn').removeClass('disabled');
            $('#leftBtn').removeClass('disabled');
            $('#rightBtn').addClass('disabled');
            rightId=$(this).attr('uid');
        });
    }
    function getOwenTitleList(){
        var titleId;
        if($('#lv3').val()!=''){
            titleId=$('#lv3').val();
        }else  if($('#lv2').val()!=''){
            titleId=$('#lv2').val();
        }else  if($('#lv1').val()!=''){
            titleId=$('#lv1').val();
        }
        if(titleId&&titleId!=''){
            $.ajax({
                type:'GET',
                url:'/sys/budgetOwen!getOwenList.dhtml?titleId='+titleId,
                dataType:'json',
                success:function (jsonData) {
                    if (jsonData) {
                        if (jsonData['result'] == '0') {
                            var titleList=jsonData['titleList'];
                            $('#budgetTitleOwenUL').empty();
                            if(titleList){
                                $(titleList).each(function(i,o){
                                    $('#budgetTitleOwenUL').append('<li><a href="#" uid="'+o['id']+'">'+ o['name']+'</a></li>');
                                });
                                rightEvent();
                            }
                        }
                    }
                },
                error:function (jsonData) {

                }
            });
        }
    }
    function getBudgetTitleList(){
        var typeId=$('#typeId').val();
        $.ajax({
            type:'GET',
            url:'/sys/budgetOwen!getTitleList.dhtml?typeId='+typeId,
            dataType:'json',
            success:function (jsonData) {
                if (jsonData) {
                    if (jsonData['result'] == '0') {
                        var titleList=jsonData['titleList'];
                        $('#budgetTitleUL').empty();
                        if(titleList){
                            $(titleList).each(function(i,o){
                                $('#budgetTitleUL').append('<li><a href="#" uid="'+o['id']+'">'+ o['name']+'</a></li>');
                            });
                            leftEvent();
                        }
                    }
                }
            },
            error:function (jsonData) {

            }
        });
    }
    function getTitileList2(b){
        var parentId=$('#lv1').val();
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
                            getTitileList3();
                            if(b){
                                getOwenTitleList();
                            }
                        }
                    }else{
                        if(b){
                            getOwenTitleList();
                        }
                    }
                }
            },
            error:function (jsonData) {

            }
        });
    }
    function getTitileList3(b){
        var parentId=$('#lv2').val();
        $.ajax({
            type:'GET',
            url:'/sys/financialTitle!getTitleList.dhtml?parentId='+parentId,
            dataType:'json',
            success:function (jsonData) {
                if (jsonData) {
                    if (jsonData['result'] == '0') {
                        var titleList=jsonData['titleList'];
                        $('#lv3').empty();
                        $('#lv3').append('<option value="">[请选择]</option>');
                        if(titleList){
                            $(titleList).each(function(i,o){
                                $('#lv3').append('<option value="'+o['id']+'">'+ o['name']+'</option>');
                            });
                            if(b){
                                getOwenTitleList();
                            }
                        }
                    }else{
                        if(b){
                            getOwenTitleList();
                        }
                    }
                }
            },
            error:function (jsonData) {

            }
        });
    }
    $(document).ready(function () {
    <#if budgetTypeList?exists&&budgetTypeList?size gt 0>
        getBudgetTitleList();
    </#if>
    <#if titleList?exists&&titleList?size gt 0>
        getTitileList2(false);
        getOwenTitleList();
    </#if>
        $('#lv1').change(function(){
            getTitileList2(true);
        });
        $('#lv2').change(function(){
            getTitileList3(true);
        });
        $('#typeId').change(function(){
            getBudgetTitleList();
        });
        $('#rightBtn').off('click').on('click',function(){
           if(leftId&&leftId!=''){
               if($('#lv3').val()!=''){
                   rightId=$('#lv3').val();
               }else  if($('#lv2').val()!=''){
                   rightId=$('#lv2').val();
               }else  if($('#lv1').val()!=''){
                   rightId=$('#lv1').val();
               }
               if(rightId&&rightId!=''){
                   $.ajax({
                       type:'GET',
                       url:'/sys/budgetOwen!addRight.dhtml?id='+leftId+'&titleId='+rightId,
                       dataType:'json',
                       success:function (jsonData) {
                           if (jsonData) {
                               if (jsonData['result'] == '0') {
                                   getOwenTitleList();
                                   getBudgetTitleList();
                               }
                           }
                       },
                       error:function (jsonData) {

                       }
                   });
               }
           }
        });
        $('#leftBtn').off('click').on('click',function(){
            if(rightId&&rightId!=''){
                $.ajax({
                    type:'GET',
                    url:'/sys/budgetOwen!removeLeft.dhtml?id='+rightId,
                    dataType:'json',
                    success:function (jsonData) {
                        if (jsonData) {
                            if (jsonData['result'] == '0') {
                                getOwenTitleList();
                                getBudgetTitleList();
                            }
                        }
                    },
                    error:function (jsonData) {

                    }
                });
            }
        });
    });
</script>

    <div style="width:560px;" class="mart30 clearfix mar-auto">
        <!--费用列begin-->
        <div class="pay-col floatleft">
            <select class="int1 width-160" id="typeId" name="typeId">
                <#if budgetTypeList?exists&&budgetTypeList?size gt 0>
                    <#list budgetTypeList as type>
                        <option value="${type.id?c}">${type.expenseType?if_exists}</option>
                    </#list>
                </#if>
            </select>
            <div class="pay-colinner mart5">
                <ul class="font12" id="budgetTitleUL">
                </ul>
            </div>
        </div>
        <!--费用列over-->
        <!--箭头beign-->
        <div class="floatleft marl15 marr15 mart50 p-top40">
            <p class="p-top40"><button class="btn disabled" type="button" id="rightBtn"><i class="icon-arrow-right"></i></button></p>
            <p class="p-top40"><button class="btn disabled" type="button" id="leftBtn"><i class="icon-arrow-left"></i></button></p>
        </div>
        <!--箭头over-->
        <!--科目列beign-->
        <div class="pay-col floatleft">
            <select class="int1 width-100" id="lv1" name="lv1">
                <#if titleList?exists&&titleList?size gt 0>
                    <#list titleList as title>
                        <option value="${title.id?c}">${title.titleName?if_exists}</option>
                    </#list>
                </#if>
            </select>
            <select class="int1 width-100" id="lv2" name="lv2">
                <option value="">[请选择]</option>
            </select>
            <select class="int1 width-100" id="lv3" name="lv3">
                <option value="">[请选择]</option>
            </select>
            <div class="pay-colinner mart5">
                <ul class="font12" id="budgetTitleOwenUL">
                </ul>
            </div>
        </div>
        <!--科目列over-->
</@common.html>