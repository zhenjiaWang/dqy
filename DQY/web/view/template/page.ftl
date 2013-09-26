<#macro pagerCommon actionJs="pagerAction"  start=0 max=10  object=pageObj>
    <#if object?exists&&object.totalPage gt 1>
    <script type="text/javascript">
        $(document).ready(function () {
            $('a', '.md-plus-page').each(function () {
                if (!$(this).hasClass('current')&&!$(this).hasClass('md-p-no')&&!$(this).hasClass('md-p-next')&&!$(this).hasClass('md-p-prev')) {
                    $(this).off('click').on('click', function () {
                        var p = $(this).text();
                        p = parseInt(p);
                        var rows =${object.everyPage?c};
                        rows = parseInt(rows);
                        var start = p * rows - rows;
                        eval(${actionJs}(start, rows));
                    });
                }
            });
            $('.md-p-next', '.md-plus-page').off('click').on('click', function () {
                if (!$(this).hasClass('disabled')) {
                    var last=$(this).prev();
                    var p = $(last).text();
                    p = parseInt(p);
                    var rows =${object.everyPage?c};
                    rows = parseInt(rows);
                    var start = p * rows;
                    eval(${actionJs}(start, rows));
                }
            });
            $('.md-p-prev', '.md-plus-page').off('click').on('click', function () {
                if (!$(this).hasClass('disabled')) {
                    var next=$(this).next();
                    var p = $(next).text();
                    p = parseInt(p);
                    p=p-5;
                    var rows =${object.everyPage?c};
                    rows = parseInt(rows);
                    var start = p * rows- rows;
                    eval(${actionJs}(start, rows));
                }
            });
            if($('.md-p-no').size()>0){
                $('.md-p-next').removeClass('disabled');
                $('.md-p-next').addClass('disabled');
            }
        });
    </script>
    <!--分页器begin-->
    <div class="md-plus-page aligncenter clearfix mart20">
        <#assign tp=object.totalPage>
        <#assign cp=object.currentPage>
        <#assign tr=object.totalRecord>
        <#assign ep=object.everyPage>
        <#assign preFlag=false>
        <#assign nextFlag=false>
        <#if cp gt 5>
            <#assign preFlag=true>
        </#if>
        <a class="md-p-prev <#if !preFlag>disabled</#if>" href="#">&lt;</a>
        <#assign mo=(cp%5)?int>
        <#assign divided=(cp/5)?int>
        <#if mo gt 0>
            <#assign divided=(divided+1)?int>
        </#if>
        <#assign endCur=(divided*5)?int>
        <#assign starCur=endCur-4>
        <#if tr gt endCur*ep>
            <#assign nextFlag=true>
        </#if>
        <#list starCur..endCur as p>
            <#if p==object.currentPage>
                <a href="#" class="current">${p}</a>
            <#elseif p gt object.totalPage>
                <a  class="md-p-no">${p}</a>
            <#else >
                <a href="#">${p}</a>
            </#if>
        </#list>
        <a class="md-p-next <#if !nextFlag>disabled</#if>" href="#">&gt;</a>
    </div>
    <!--分页器bover-->
    </#if>
</#macro>

