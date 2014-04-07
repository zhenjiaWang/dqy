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
    <style type="text/css">
        .printtab{border:1px solid #000;border-left: 1px dashed #c8c8c8;border-right: 1px dashed #c8c8c8;}
        .printtab th,.printtab td{padding-left:10px;height:30px;line-height:30px;border:1px dotted #464646;font-size:13px;color:#333;}
        .printtab th{background-color:#f2f2f2;color:#000;}
    </style>
</head>
<body>
<div style="width:760px;margin:0 auto;position: relative;margin-top: 50px;">
    <div class="clearfix">
        <span style="position: absolute;left:0;"><img src="../images/cssimg/dqy-logo.png"></span>
        <p class="alignright p-top40 font20">${applyName?if_exists}</p>
        <p class="mart30 alignright font14">单据编号：${wfReq.reqNo?if_exists}</p>
    </div>
    <table width="100%" class="printtab mart30">
        <tbody>

        <tr>
            <th style="width: 140px;">标题</th>
            <td style="width: auto;" colspan="3">${wfReq.subject?if_exists}</td>
        </tr>
        <tr>
            <th style="width: 140px;">申请人</th>
            <td style="width: 200px;">${(wfReq.userId.userName)?if_exists}</td>
            <th style="width: 140px;">申请日期</th>
            <td>${wfReq.sendDate?string("yyyy-MM-dd HH:mm:ss")}</td>
        </tr>
        <tr>
            <th style="width: 140px;">预支金额</th>
            <td style="width: 200px;">${(wfReqAdvanceAccount.amount)?c}</td>
            <th style="width: 140px;">支付方式</th>
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
                <th style="width: 140px;">收款单位</th>
                <td style="width: auto;" colspan="3">
                ${wfReqAdvanceAccount.payee?if_exists}
                </td>
            </tr>
            <tr>
                <th style="width: 140px;">开户行</th>
                <td style="width: 200px;">${(wfReqAdvanceAccount.bank)?if_exists}</td>
                <th style="width: 140px;">帐号</th>
                <td>${(wfReqAdvanceAccount.bankAccount)?if_exists}</td>
            </tr>
            </#if>
        </#if>
        <tr>
            <th style="width: 140px;">备注</th>
            <td style="width: auto;" colspan="3">
            ${(wfReqAdvanceAccount.remarks)?if_exists}
            </td>
        </tr>
        <#if wfReq.financialYn=="Y">
        <tr>
            <th style="width: 140px;">财务</th>
            <td style="width: auto;" colspan="3">
                [已办理] ${wfReq.financialDesc?if_exists}
            </td>
        </tr>
        </#if>
        </tbody>
    </table>

    <div class="clearfix mart30">
        <div class="floatleft font14">
            <p class="p-top10">申请人：${wfReq.userId.userName?if_exists}</p>
            <p class="p-top10">签字：&nbsp;</p>
        </div>
    <#if reqCommentsList?exists&&reqCommentsList?size gt 0>
        <div class="floatright">
            <table style="width: 500px;" class="printtab">
                <thead>
                <tr>
                    <th style="width: 60px;">操作人</th>
                    <th style="width: 75px;">操作</th>
                    <th style="width: 120px;">操作时间</th>
                    <th>备注</th>
                </tr>
                </thead>
                <tbody>
                    <#list reqCommentsList as comments>
                        <#if comments.action gt 2>
                        <tr>
                            <td>${(comments.userId.userName)?if_exists}</td>
                            <td>${comments.actionDesc?if_exists}</td>
                            <td>${comments.created?string("yyyy-MM-dd HH:mm")}</td>
                            <td>${comments.content?if_exists}</td>
                        </tr>
                        </#if>
                    </#list>
                </tbody>
            </table>
        </div>
    </#if>
    </div>
</div>


</body>
</html>