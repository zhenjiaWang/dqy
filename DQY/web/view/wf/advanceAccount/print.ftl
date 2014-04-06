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
                <th>预支金额</th>
                <td>
                ${(wfReqAdvanceAccount.amount)?c}
                </td>
                <th width="80">支付方式</th>
                <td>
                <#if wfReqAdvanceAccount.payMethod?exists>
                    <#if wfReqAdvanceAccount.payMethod==1>
                        现金
                    <#elseif wfReqAdvanceAccount.payMethod==2>
                        银行转账
                    <#elseif wfReqAdvanceAccount.payMethod==3>
                        支票
                    </#if>
                </#if>
                </td>
            </tr>
            <#if wfReqAdvanceAccount.payMethod?exists>
                <#if wfReqAdvanceAccount.payMethod==2>
                <tr>
                    <th>收款单位</th>
                    <td colspan="3">
                    ${wfReqAdvanceAccount.payee?if_exists}
                    </td>
                </tr>
                <tr>
                    <th>开户行</th>
                    <td>
                    ${(wfReqAdvanceAccount.bank)?if_exists}
                    </td>
                    <th width="80">帐号</th>
                    <td>
                    ${(wfReqAdvanceAccount.bankAccount)?if_exists}
                    </td>
                </tr>
                </#if>
            </#if>
            <tr>
                <th>备注</th>
                <td colspan="3">
                ${(wfReqAdvanceAccount.remarks)?if_exists}
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