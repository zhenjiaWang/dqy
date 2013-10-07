<#macro pagerCommon actionJs="pagerAction"  start=0 max=10  object=pageObj>
    <#if object?exists&&object.totalPage gt 1>
    <script type="text/javascript">
        $(document).ready(function () {
            $('li', '#plus-page').each(function () {
                if (!$(this).hasClass('active')&&!$(this).hasClass('disabled')&&!$(this).hasClass('p-next')&&!$(this).hasClass('p-prev')) {
                    $(this).off('click').on('click', function () {
                        var p = $('a',this).text();
                        p = parseInt(p);
                        var rows =${object.everyPage?c};
                        rows = parseInt(rows);
                        var start = p * rows - rows;
                        eval(${actionJs}(start, rows));
                    });
                }
            });
            $('.p-next', '#plus-page').off('click').on('click', function () {
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
            $('.p-prev', '#plus-page').off('click').on('click', function () {
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
            if($('.disabled', '#plus-page').size()>1){
                $('.md-p-next', '.plus-page').removeClass('disabled');
                $('.md-p-next', '.plus-page').addClass('disabled');
            }
        });
    </script>
    <!--分页器begin-->

    <div class="pagination pagination-centered" id="plus-page">
        <ul>
            <#assign tp=object.totalPage>
            <#assign cp=object.currentPage>
            <#assign tr=object.totalRecord>
            <#assign ep=object.everyPage>
            <#assign preFlag=false>
            <#assign nextFlag=false>
            <#if cp gt 5>
                <#assign preFlag=true>
            </#if>
            <li class="p-prev <#if !preFlag>disabled</#if>"><a href="#">«</a></li>
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
                    <li class="active"><a href="#">${p}</a></li>
                <#elseif p gt object.totalPage>
                    <li class="disabled"><a href="#">${p}</a></li>
                <#else >
                    <li><a href="#">${p}</a></li>
                </#if>
            </#list>
            <li class="p-next <#if !nextFlag>disabled</#if>"><a href="#">»</a></li>
        </ul>
    </div>
    <!--分页器bover-->
    </#if>
</#macro>

