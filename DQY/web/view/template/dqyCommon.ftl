<#import "/view/template/common.ftl" as common>
<#macro dqyCommon>
    <@common.html title="德清源办公">
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
                <ul class="nav font18 clearfix floatleft" id="topMenu">
                    <li class="index"><a href="/common/login!index.dhtml"><em></em>首 页</a></li>
                    <li class="apply"><a href="/wf/advanceAccount.dhtml"><em></em>申请平台</a></li>
                    <li class="info"><a href="#"><em></em>信息平台</a></li>
                    <li class="budget"><a href="/sys/budgetAmount.dhtml"><em></em>预算管理</a></li>
                    <li class="sys"><a href="/sys/orgGroup.dhtml"><em></em>系统管理</a></li>
                </ul>
            </div>
            <!--右侧begin-->
            <div class="top-bar">
                <div class="search-box"><!--search-box上加减 current 控制三角的方向，和选项的折叠-->
                    <a class="triangle" href="#"></a>
                    <span class="choosed" uid="${userInfo["orgId"]?c}">${userInfo["orgName"]?if_exists}</span>
                    <ul id="authOrgList">
                        <#if userInfo?exists>
                            <#assign authOrgList=userInfo["authOrgList"]?if_exists>
                            <#if authOrgList?exists&&authOrgList?size gt 0>
                                <#list authOrgList as authOrg>
                                    <li uid="${authOrg.id?c}"><a href="#">${authOrg.orgName?if_exists}</a></li>
                                </#list>
                            </#if>
                        </#if>
                    </ul>
                </div>
                <p class="clearfix mart20 p-top5 font14 aligncenter">
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

        </div>
        <div class="modal-footer">
            <a class="btn btn-success" id="myModalOkBtn">确定</a>
            <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
        </div>
    </div>

    <div class="datepicker dropdown-menu treeDiv" style="display: none;width: 200px;height: 300px; z-index: 99999;">
        <div>
            <ul id="treeDemo" class="ztree"></ul>
        </div>
    </div>
    </@common.html>
</#macro>
