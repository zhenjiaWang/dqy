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
                <th>收款单位</th>
                <td colspan="3">
                ${wfReqDaily.payee?if_exists}
                </td>
            </tr>
            <tr>
                <th>开户行</th>
                <td>
                ${(wfReqDaily.bank)?if_exists}
                </td>
                <th width="80">帐号</th>
                <td>
                ${(wfReqDaily.bankAccount)?if_exists}
                </td>
            </tr>
            <tr>
                <th>报销金额</th>
                <td colspan="3">
                ${(wfReqDaily.amount)?c}
                </td>
            </tr>
            <tr>
                <th>备注</th>
                <td colspan="3">
                ${(wfReqDaily.remarks)?if_exists}
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
    <#if detailList?exists&&detailList?size gt 0>
        <table width="100%" id="printTable" class="print-table mart10">
            <thead>
            <tr>
                <th width="80" class="alignleft">费用部门</th>
                <th width="60" class="alignleft">费用类型</th>
                <th width="60"class="alignleft">费用项目</th>
                <th width="70" class="alignleft">费用日期</th>
                <th width="50" class="alignleft">金额</th>
                <th class="alignleft">备注</th>
            </tr>
            </thead>
            <tbody>
                <#list detailList as detail>
                <tr >
                    <td>${(detail.expenseDept.deptName)?if_exists}</td>
                    <td>${(detail.expenseType.expenseType)?if_exists}</td>
                    <td>${(detail.expenseTitle.titleName)?if_exists}</td>
                    <td>${detail.amountDate?string("yyyy-MM-dd")}</td>
                    <td>${detail.amount?c}</td>
                    <td>${detail.remarks?if_exists}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </#if>
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