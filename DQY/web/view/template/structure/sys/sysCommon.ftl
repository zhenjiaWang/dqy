<#import "/view/template/dqyCommon.ftl" as dqyCommon>
<#import "/view/common/core.ftl" as c>
<#macro sys_common>
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
                    $('.${userInfo['leftMenu']?if_exists}', '.side-left-in').addClass('current');
                    $('.${userInfo['leftMenu']?if_exists}', '.side-left-in').parent().addClass('current');

                    var topLocation=$('.current', '#topMenu').text();
                    var leftLocation=$('li.current', '.side-left-in').text();
                    if(topLocation&&leftLocation){
                        $('.currentLocation','.location').text(leftLocation);
                        $('.topLocation','.location').text(topLocation);
                        $('.menuLocation','.location').text(leftLocation);
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
            <div class="side-left-in font14">
                <!--一组begin-->
                <#assign roleId=userInfo["roleId"]?if_exists>
                <#if roleId?exists>
                    <#if roleId?contains("SYS_GROUP")>
                        <ul class="item-nav"><!--ul上的current是为了控制当前这一组的背景色，li上的current是为了控制二级菜单-->
                            <li class="orgGroup">
                                <a class="cur" href="/sys/orgGroup.dhtml">集团管理</a>
                            </li>
                            <li class="org">
                                <a class="cur" href="/sys/org.dhtml">机构管理</a>
                            </li>
                        </ul>
                    </#if>
                    <#if roleId?contains("SYS_USER")>
                    <ul class="item-nav"><!--ul上的current是为了控制当前这一组的背景色，li上的current是为了控制二级菜单-->
                        <li class="department">
                            <a class="cur" href="/hr/department.dhtml">组织机构</a>
                        </li>
                        <li class="user">
                            <a class="cur" href="/hr/user.dhtml">员工信息</a>
                        </li>
                    </ul>
                    </#if>
                    <#if roleId?contains("SYS_FINANCIAL")||roleId?contains("SYS_BUDGET")>
                    <ul class="item-nav"><!--ul上的current是为了控制当前这一组的背景色，li上的current是为了控制二级菜单-->
                        <#if roleId?contains("SYS_FINANCIAL")>
                        <li class="financialTitle">
                            <a class="cur" href="/sys/financialTitle.dhtml">财务科目</a>
                        </li>
                        </#if>
                        <#if roleId?contains("SYS_BUDGET")>
                        <li class="budgetType">
                            <a class="cur" href="/sys/budgetType.dhtml">预算类别</a>
                        </li>
                        <li class=budgetTitle>
                            <a class="cur" href="/sys/budgetTitle.dhtml">预算科目</a>
                        </li>
                        </#if>
                    </ul>
                    </#if>
                    <#if roleId?contains("SYS_APPROVE")>
                    <ul class="item-nav"><!--ul上的current是为了控制当前这一组的背景色，li上的current是为了控制二级菜单-->
                        <li class="variableGlobal">
                            <a class="cur" href="/wf/variableGlobal.dhtml">审批岗位</a>
                        </li>
                        <li class="reqNo">
                            <a class="cur" href="/wf/reqNo.dhtml">申请编号</a>
                        </li>
                    </ul>
                    </#if>
                </#if>
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