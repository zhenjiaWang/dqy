<#import "/view/template/common.ftl" as common>
<#macro dqyCommon>
    <@common.html title="DQY Intranet OA 办公自动化系统">
    <style>
        .tableBgColor {
            background-color: #FAFAFA;
        }

        .oddBgColor {
            background-color: #FAF3F3;
        }

        .thColor {
            color: #898989;
        }

        .thColor th {
            text-align: center;
        }
    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            $('body').off('.data-api');

            WEBUTILS.popMask.close();
            <#if Session["userSession"]?exists>
                <#assign userInfo=Session["userSession"]?if_exists>
                <#if userInfo?exists>
                    $('li', '#topMenu').removeClass('current');
                    $('.${userInfo['topMenu']?if_exists}', '#topMenu').addClass('current');
                </#if>
            </#if>
            $('.search-box').off('click').on('click', function () {
                if ($(this).hasClass('current')) {
                    $(this).removeClass('current');
                } else {
                    $(this).addClass('current');
                }
            });
            $('li', '.search-box').off('click').on('click', function () {
                var uid = $(this).attr('uid');
                if (uid) {
                    document.location.href='/common/login!authOrg.dhtml?authOrgId='+uid;
                }
            });
            <#if userInfo.taskUnRead gt 0 ||userInfo.taskUnApprove gt 0||userInfo.reqPassed gt 0 ||userInfo.reqRejected gt 0>
            $('.apply','#topMenu').find('a').append('<span class="ts"></span>');
            </#if>
            <#assign roleId=userInfo["roleId"]?if_exists>
            <#if roleId?exists>
                <#if !roleId?contains("SYS_GROUP")&&!roleId?contains("SYS_USER")&&!roleId?contains("SYS_FINANCIAL")&&!roleId?contains("SYS_BUDGET")&&!roleId?contains("SYS_APPROVE")>
                    $('.sys','#topMenu').addClass('no');
                </#if>
            </#if>
        });
    </script>
    <div class="container">
        <!--header begin-->
        <div class="header clearfix">
            <a title="德青源" class="logo floatleft" href="/common/login!index.dhtml"><img
                    src="../images/cssimg/dqy-logo.png"></a>

            <div class="clearfix">
                <!--天气begin-->
                <div class="weather"></div>
                <!--天气over-->
                <ul class="nav font14 clearfix floatleft" id="topMenu">
                    <li class="index"><a href="/common/login!index.dhtml"><em></em>首 页</a></li>
                    <li class="apply"><a href="/wf/advanceAccount.dhtml"><em></em>申请平台</a></li>
                    <li class="info no"><a href="#"><em></em>信息平台</a></li>
                    <li class="info no"><a href="#"><em></em>销售管理</a></li>
                    <#if userInfo["topMenuBudget"] gt 0>
                        <li class="budget"><a href="/common/common!budgetIndex.dhtml"><em></em>预算管理</a></li>
                    <#else >
                        <li class="budget no"><a href="##"><em></em>预算管理</a></li>
                    </#if>
                    <#if userInfo["topMenuSys"] gt 0>
                        <li class="sys"><a href="/common/common!sysIndex.dhtml"><em></em>系统管理</a></li>
                    <#else >
                        <li class="sys no"><a href="##"><em></em>系统管理</a></li>
                    </#if>
                </ul>
            </div>
            <!--右侧begin-->
            <div class="top-bar">
                <div class="search-box"><!--search-box上加减 current 控制三角的方向，和选项的折叠-->
                    <a class="triangle" href="#"></a>
                    <span class="choosed" uid="${userInfo["orgId"]?c}">${userInfo["orgName"]?if_exists} &nbsp;&nbsp;${userInfo.userName?if_exists}</span>
                    <ul id="authOrgList">
                        <#if userInfo?exists>
                            <#assign authOrgList=userInfo["authOrgList"]?if_exists>
                            <#if authOrgList?exists&&authOrgList?size gt 0>
                                <#list authOrgList as authOrg>
                                    <li uid="${authOrg.id?c}">
                                        <a href="#">${authOrg.orgName?if_exists}
                                        <#if authOrg.tip gt 0 ><em style="color: red;font-weight: bold;">&nbsp;&nbsp;[有信息]</em></#if>
                                        </a>
                                    </li>
                                </#list>
                            </#if>
                        </#if>
                    </ul>
                </div>
                <p class="clearfix mart20 p-top5 font12 aligncenter">
                    <a href="#"><em class="dqy-ico dqy-ico-set"></em>帐户设置</a> |
                    <a href="/common/login!logout.dhtml"><em class="dqy-ico dqy-ico-out"></em>退出登录</a>
                </p>
            </div>
            <!--右侧over-->
        </div>
        <!--header over-->
        <div class="main">
            <#nested/>
        </div>
    </div>

    <div class="modal hide fade" id="myModal">
        <div class="modal-header">
            <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
            <p class="pop-title"></p>
        </div>
        <div class="modal-body">
            <iframe
                    id="myModalFrame" name="myModalFrame" src="/view/common/blank.html"
                    width="100%" height="300"
                    frameborder="0"></iframe>
        </div>
        <div class="modal-footer">
            <a class="btn btn-success" id="myModalOkBtn">确定</a>
            <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
        </div>
    </div>


    </@common.html>
</#macro>
