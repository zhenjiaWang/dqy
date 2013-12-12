<#import "/view/template/structure/sys/sysCommon.ftl" as sysCommon>
<#import "/view/common/core.ftl" as c>
<@sysCommon.sys_common>

<script type="text/javascript">
    $(document).ready(function () {
        $('.editReqNo').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                WEBUTILS.popWindow.createPopWindow(550, 300, '编辑申请编号', '/wf/reqNo!input.dhtml?applyId='+uid);
            }
        });
    });
</script>
<!--搜索begin-->
<div class="r-top clearfix">

</div>
<!--搜索over-->

<div style="min-height: 419px;height: 419px;" class="mart10">
    <table class="table table-bordered table-hover tableBgColor">
        <thead>
        <tr class="thColor">
            <th>申请单据名称</th>
            <th>申请单据编号</th>
            <th width="100">操作</th>
        </tr>
        </thead>
        <tbody>
            <#if applyList?exists&&applyList?size gt 0>
                <#list applyList as apply>
                <tr <#if (apply_index+1)%2!=0>class="oddBgColor"</#if>>
                    <td>
                        <#if apply=="ADVANCE_ACCOUNT">
                        预支申请
                        <#elseif apply=="REPAYMENT">
                            预支还款
                        <#elseif apply=="DAILY">
                            费用报销
                        <#elseif apply=="BUSINESS">
                            事务申请
                        </#if>
                    </td>
                    <td>
                        <#if applyReqNoMap?exists&&applyReqNoMap?size gt 0>
                        ${applyReqNoMap[apply+"_"]?if_exists}
                        </#if>
                    </td>
                    <td style="text-align: center;">
                        <span style="cursor: pointer;" class="editReqNo"  uid="${apply?if_exists}"><i class="icon-edit"></i>编辑</span>
                    </td>
                </tr>
                </#list>
            </#if>
        </tbody>
    </table>
</div>
</@sysCommon.sys_common>