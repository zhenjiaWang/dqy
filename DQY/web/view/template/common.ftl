<#macro html title="" >
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Content-Language" content="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>
    <title>${title?html}</title>
    <link rel="stylesheet" href="/css/global.css" type="text/css">
    <link type="text/css" href="/css/dqy-style.css" rel="stylesheet">
    <script src="/js/jquery.js"></script>
    <script src="/js/jquery.poshytip.min.js"></script>
    <script src="/js/support.js"></script>
    <script src="/js/webutils/webutils.templete.js"></script>
    <script src="/js/webutils/webutils.msg.js"></script>
    <script src="/js/webutils/webutils.dialog.js"></script>
    <script src="/js/webutils/webutils.searchTip.js"></script>
    <script src="/js/webutils/webutils.layout.js"></script>
    <script src="/js/webutils/webutils.status.js"></script>
    <script src="/js/webutils/webutils.dragdrop.js"></script>
    <script src="/js/webutils/webutils.popWindow.js"></script>
    <script src="/js/webutils/webutils.popMask.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            WEBUTILS.popMask.close();
        });
    </script>
</head>
<body>
    <#setting number_format="#">
<div class="container">
    <!--header begin-->
    <div class="header clearfix">
        <a title="德青源" class="logo floatleft" href="#"><img src="../images/cssimg/dqy-logo.png"></a>
        <ul class="nav font18 clearfix p-top40 floatleft">
            <li class="current"><a href="#"><em></em>首 页</a></li>
            <li><a href="#"><em></em>人事管理</a></li>
            <li><a href="#"><em></em>申请管理</a></li>
            <li><a href="#"><em></em>申请管理</a></li>
            <li><a href="/sys/orgGroup.dhtml"><em></em>系统管理</a></li>
        </ul>
        <!--右侧begin-->
        <div class="top-bar">
            <div class="search-box"><!--search-box上加减 current 控制三角的方向，和选项的折叠-->
                <a class="triangle" href="#"></a>
                <span class="choosed">机构选择</span>
                <ul>
                    <li><a href="#">北京东城分部</a></li>
                    <li><a href="#">石家庄分部</a></li>
                    <li><a href="#">天津城分部</a></li>
                    <li><a href="#">美国西部分部</a></li>
                </ul>
            </div>
            <p class="clearfix mart20 p-top5 font14 aligncenter">
                <a href="#"><em class="dqy-ico dqy-ico-set"></em>帐户设置</a> |
                <a href="#"><em class="dqy-ico dqy-ico-out"></em>退出登录</a>
            </p>
        </div>
        <!--右侧over-->
    </div>
    <!--header over-->
    <div class="main">
        <#nested/>
    </div>
</div>
</body>
</html>
</#macro>
