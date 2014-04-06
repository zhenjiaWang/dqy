<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Content-Language" content="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>
    <title>打印 ${wfReq.subject?if_exists}</title>
    <link rel="stylesheet" href="/css/global.css" type="text/css">
    <link type="text/css" href="/css/dqy-style.css" rel="stylesheet">
    <script type="text/javascript">

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
                <th>报销金额</th>
                <td colspan="3">
                ${(wfReqSale.amount)?c}
                </td>
            </tr>
            <tr>
                <th>费用类别</th>
                <td >
                ${(wfReqSale.expenseType.expenseType)?if_exists}
                </td>
                <th>费用项目</th>
                <td >
                ${(wfReqSale.expenseTitle.titleName)?if_exists}
                </td>
            </tr>
            <tr>
                <th>渠道</th>
                <td >
                ${(wfReqSale.channelId.channelName)?if_exists}
                </td>
                <th>业态/部门</th>
                <td >
                ${(wfReqSale.deptId.deptName)?if_exists}
                </td>
            </tr>
            <tr>
                <th>系统</th>
                <td >
                ${(wfReqSale.systemId.systemName)?if_exists}
                </td>
                <th>门店</th>
                <td >
                ${(wfReqSale.customerId.customerName)?if_exists}
                </td>
            </tr>
            <tr>
                <th>品类/系列</th>
                <td >
                ${(wfReqSale.seriesId.seriesName)?if_exists}
                </td>
                <th>单品</th>
                <td >
                (${(wfReqSale.productId.productCode)?if_exists})&nbsp;&nbsp;&nbsp;${(wfReqSale.productId.productName)?if_exists}
                </td>
            </tr>
            <tr>
                <th>备注</th>
                <td colspan="3">
                ${(wfReqSale.remarks)?if_exists}
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
                    <#if comments.action gt 3>
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