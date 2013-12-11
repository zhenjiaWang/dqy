<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Content-Language" content="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>
    <title>打印 ${wfReq.subject?if_exists}</title>
    <link type="text/css" href="/css/bootstrap.css" rel="stylesheet">
    <link rel="stylesheet" href="/css/global.css" type="text/css">
    <link type="text/css" href="/css/dqy-style.css" rel="stylesheet">
    <script type="text/javascript">
        $(document).ready(function () {
            $('body').off('.data-api')
            WEBUTILS.popMask.close();
        });
    </script>
</head>
<body>
<div class="print">
    <div>
        <div class="clearfix">
            <span class="floatleft"><img width="70" src="../images/cssimg/dqy-logo.png"></span>
            <div class="print-title floatleft">
                <p class="font20 aligncenter">${applyName?if_exists}</p>
                <p class="alignright font12">单据编号：${wfReq.reqNo?if_exists}</p>
            </div>
        </div>
        <table width="100%" id="printTable" class="print-table mart10">
            <tbody>
            <tr>
                <th width="80">标题</th>
                <td colspan="3">${wfReq.subject?if_exists}</td>
            </tr>
            <tr>
                <th>申请人</th>
                <td>
                ${(wfReq.userId.userName)?if_exists}
                </td>
                <th width="80">申请时间</th>
                <td>
                ${(wfReq.sendDate)?string("yyyy-MM-dd HH:mm")}
                </td>
            </tr>
            <tr>
                <td colspan="4">
                ${wfReqBusiness.content?if_exists}
                </td>
            </tr>
            <#if wfReq.financialYn=="Y">
            <tr>
                <th>财务</th>
                <td colspan="3">
                    [已办理] ${wfReq.financialDesc?if_exists}
                </td>
            </tr>
            </#if>
            </tbody>
        </table>
    </div>
    <#if reqCommentsList?exists&&reqCommentsList?size gt 0>
        <table width="100%" class="print-table mart20">
            <thead>
            <tr>
                <th width="80" class="alignleft">操作人</th>
                <th width="150" class="alignleft">操作流程</th>
                <th width="150" class="alignleft">操作时间</th>
                <th  class="alignleft">备注</th>
            </tr>
            </thead>
            <tbody>
                <#list reqCommentsList as comments>
                    <#if comments.action!=1>
                    <tr style="background-color:#ffffff">
                        <td>${(comments.userId.userName)?if_exists}</td>
                        <td>${comments.actionDesc?if_exists}</td>
                        <td>${comments.created?string("yyyy-MM-dd HH:mm")}</td>
                        <td>${comments.content?if_exists}</td>
                    </tr>
                    </#if>
                </#list>
            </tbody>
        </table>
    </#if>
    <div class="print-bt clearfix">
        <p>申请人：${wfReq.userId.userName?if_exists}</p>
        <p>签字：  </p>
    </div>
</div>
</body>
</html>