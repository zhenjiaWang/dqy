<#import "/view/template/dqyCommon.ftl" as dqyCommon>
<#import "/view/common/core.ftl" as c>
<#macro wf_common>
    <@dqyCommon.dqyCommon>
    <script type="text/javascript">
        $(document).ready(function () {
            $('.item-nav','.side-left-in').hover(function(){
                $(this).addClass('current');
            },function(){
                $(this).removeClass('current');
            });
            <#if Session["userSession"]?exists>
                <#assign userInfo=Session["userSession"]?if_exists>
                <#if userInfo?exists>
                    $('li', '.side-left-in').removeClass('current');
                    $('dd', '.side-left-in').removeClass('current');
                    $('.${userInfo['leftMenu']?if_exists}', '.side-left-in').addClass('current');
                    $('.${userInfo['leftMenu']?if_exists}', '.side-left-in').parent().addClass('current');
                    <#if userInfo['childMenu']?exists>
                        $('.${userInfo['childMenu']?if_exists}', '.side-left-in').addClass('current');
                    </#if>
                    var topLocation=$('.current', '#topMenu').text();
                    var leftLocation=$('li.current', '.side-left-in').text();
                    if(topLocation&&leftLocation){
                        $('.currentLocation','.location').text(leftLocation);
                        $('.topLocation','.location').text(topLocation);
                        $('.menuLocation','.location').text(leftLocation);
                    }

                    <#if userInfo.reqPassed gt 0 ||userInfo.reqRejected gt 0>
                        $('.myReq','.side-left-in').find('a.cur').append('<span class="ts"></span>');
                        <#if userInfo.reqPassed gt 0>
                            $('.pass','.side-left-in').find('a').append('<span class="badge badge-important marl10">${userInfo.reqPassed?c}</span>');
                        </#if>
                        <#if userInfo.reqRejected gt 0>
                            $('.overrule','.side-left-in').find('a').append('<span class="badge badge-important marl10">${userInfo.reqRejected?c}</span>');
                        </#if>

                    </#if>
                    <#if userInfo.taskUnRead gt 0 ||userInfo.taskUnApprove gt 0>
                        $('.myTask','.side-left-in').find('a.cur').append('<span class="ts"></span>');
                        <#if userInfo.taskUnApprove gt 0>
                            $('.approve','.side-left-in').find('a').append('<span class="badge badge-important marl10">${userInfo.taskUnApprove?c}</span>');
                        </#if>
                        <#if userInfo.taskUnRead gt 0>
                            $('.approve','.side-left-in').find('a').append('<span class="badge badge-important marl10">${userInfo.taskUnRead?c}</span>');
                        </#if>
                    </#if>
                </#if>
            </#if>

            $('.cur').each(function(){
                var href=$(this).attr('href');
                if(href=='##'){
                    $(this).off('click').on('click',function(){
                        if ( $(this).next().is(':visible')) {
                            $(this).next().hide();
                        }else{
                            $('dl','.side-left-in').hide();
                            $(this).next().show();
                        }
                    });
                }
            });
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
            <div class="side-left-in font14">
                <!--一组begin-->
                <ul class="item-nav"><!--ul上的current是为了控制当前这一组的背景色，li上的current是为了控制二级菜单-->
                    <li class="advanceAccount">
                        <a class="cur" href="/wf/advanceAccount.dhtml">预支申请</a>
                    </li>
                    <li class="rePayment">
                        <a class="cur" href="/wf/rePayment.dhtml">还款申请</a>
                    </li>
                </ul>
                <ul class="item-nav"><!--ul上的current是为了控制当前这一组的背景色，li上的current是为了控制二级菜单-->
                    <li class="department">
                        <a class="cur" href="#">费用报销</a>
                    </li>
                    <li class="user">
                        <a class="cur" href="#">事务申请</a>
                    </li>
                </ul>
                <ul class="item-nav"><!--ul上的current是为了控制当前这一组的背景色，li上的current是为了控制二级菜单-->
                    <li class="myReq">
                        <a class="cur" href="##">申请单据</a>
                        <dl class="font12">
                            <dd class="ing"><a href="/wf/req!ingList.dhtml"><i class="icon-hand-right"></i> 待审批</a></dd>
                            <dd class="pass"><a href="/wf/req!passList.dhtml"><i class="icon-hand-right"></i> 已通过</a></dd>
                            <dd class="overrule"><a href="/wf/req!overruleList.dhtml"><i class="icon-hand-right"></i> 已驳回</a></dd>
                        </dl>
                    </li>
                    <li class="myTask">
                        <a class="cur" href="##">审批单据</a>
                        <dl class="font12">
                            <dd class="approve"><a href="/wf/reqTask!approveList.dhtml"><i class="icon-hand-right"></i> 未审批</a></dd>
                            <dd class="done"><a href="/wf/reqTask!doneList.dhtml"><i class="icon-hand-right"></i> 已审批</a></dd>
                        </dl>
                    </li>
                    <li class=budgetTitle>
                        <a class="cur" href="#">经办单据</a>
                        <dl class="font12">
                            <dd><a href="#"><i class="icon-hand-right"></i> 未处理</a></dd>
                            <dd><a href="#"><i class="icon-hand-right"></i> 已处理</a></dd>
                        </dl>
                    </li>
                </ul>
                <!--一组over-->
            </div>
        </div>
        <!--左侧目录树over-->
        <!--右侧begin-->
        <div class="right-main floatleft">
            <!--当前位置begin-->
            <div class="location clearfix">
                <span class="font14 currentLocation" ></span>
                <em><img src="../../images/cssimg/now-r.jpg"></em>
                <div class="floatright marr15">
                    <em>当前位置：</em>
                    <a href="#" class="topLocation"></a>
                    <em>&gt;</em>
                    <a href="#" class="menuLocation"></a>
                </div>
            </div>
            <!--当前位置over-->
            <div class="right-inner mart5">
                <#nested/>
            </div>
        </div>
        <!--右侧over-->
    </div>
    </@dqyCommon.dqyCommon>
</#macro>