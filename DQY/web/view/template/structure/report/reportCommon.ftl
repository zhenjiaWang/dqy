<#import "/view/template/dqyCommon.ftl" as dqyCommon>
<#import "/view/common/core.ftl" as c>
<#macro report_common>
    <@dqyCommon.dqyCommon>
    <script type="text/javascript">
        $(document).ready(function () {
            $('.item-nav', '.side-left-in').hover(function () {
                $(this).addClass('current');
            }, function () {
                $(this).removeClass('current');
            });
            <#if Session["userSession"]?exists>
                <#assign userInfo=Session["userSession"]?if_exists>
                <#if userInfo?exists>
                    $('li', '.side-left-in').removeClass('current');
                    $('.${userInfo['leftMenu']?if_exists}', '.side-left-in').addClass('current');
                    $('.${userInfo['leftMenu']?if_exists}', '.side-left-in').parent().addClass('current');


                    var leftLocation, topLocation = $('.current', '#topMenu').text();
                    var leftHref = $('li.current', '.side-left-in').find('a').attr('href');
                    if (leftHref == '##') {
                        var obj = $('li.current', '.side-left-in');
                        leftLocation = $(obj).find('a').html();
                        leftLocation += ' > ' + $('dd.current', obj).find('a').text();
                    } else {
                        leftLocation = $('li.current', '.side-left-in').find('a').text();
                    }
                    if (topLocation && leftLocation) {
                        $('.currentLocation', '.location').text(leftLocation);
                        $('.topLocation', '.location').text(topLocation);
                        $('.menuLocation', '.location').text(leftLocation);
                    }
                </#if>
            </#if>
        });
    </script>
    <div class="p-top10 clearfix">
    <#--<dl class="font12">-->
    <#--<dd class="current"><a href="/sys/orgGroup.dhtml"><i class="icon-hand-right"></i> 待审</a></dd>-->
    <#--<dd><a href="/sys/org.dhtml"><i class="icon-hand-right"></i> 已通过</a></dd>-->
    <#--<dd><a href="#"><i class="icon-hand-right"></i> 已驳回</a></dd>-->
    <#--</dl>-->
        <!--左侧目录树begin-->

        <div class="side-left floatleft marl15">
            <div class="side-left-in font12">
                <!--一组begin-->
                <#assign roleId=userInfo["roleId"]?if_exists>
                <#if roleId?exists>
                    <ul class="item-nav"><!--ul上的current是为了控制当前这一组的背景色，li上的current是为了控制二级菜单-->
                        <#if roleId?contains("REPORT_SALE")>
                            <li class="sale">
                                <a class="cur" href="/report/req!saleList.dhtml">销售费用</a>
                            </li>
                        </#if>
                        <#if roleId?contains("REPORT_ADVANCE")>
                            <li class="advanceAccount">
                                <a class="cur" href="/report/req!advanceAccountList.dhtml">预支申请</a>
                            </li>
                        </#if>
                        <#if roleId?contains("REPORT_REPAYMENT")>
                            <li class="rePayment">
                                <a class="cur" href="/report/req!rePaymentList.dhtml">还款申请</a>
                            </li>
                        </#if>
                        <#if roleId?contains("REPORT_DAILY")>
                            <li class="daily">
                                <a class="cur" href="/report/req!dailyList.dhtml">费用报销</a>
                            </li>
                        </#if>
                    </ul>
                </#if>
                <!--一组over-->
            </div>
        </div>
        <!--左侧目录树over-->
        <!--右侧begin-->
        <div class="right-main floatleft">
            <!--当前位置begin-->
            <div class="location clearfix">
                <span class="font12 currentLocation"></span>
                <em><img src="../../images/cssimg/now-r.jpg"></em>

                <div class="floatright marr15">
                    <em>当前位置：</em>
                    <a href="#" class="topLocation"></a>
                    <em>&gt;</em>
                    <a href="#" class="menuLocation"></a>
                </div>
            </div>
            <!--当前位置over-->
            <div class="right-inner mart5 font12">
                <#nested/>
            </div>
        </div>
        <!--右侧over-->
    </div>
    </@dqyCommon.dqyCommon>
</#macro>