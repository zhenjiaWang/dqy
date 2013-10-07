<#import "/view/template/structure/wf/wfCommon.ftl" as wfCommon>
<#import "/view/common/core.ftl" as c>
<@wfCommon.wf_common>

<script type="text/javascript">

    function addFlow(){
        $('.modal-header','#myModal').find('.close').trigger('click');
    }
    $(document).ready(function () {
        $('#nextBtn').off('click').on('click', function () {
            WEBUTILS.popWindow.createPopWindow(700, null, '选择流程', '/wf/reqMyFlow!myFlowList.dhtml?applyId=${applyId?if_exists}');
        });

    });
</script>
<!--搜索begin-->
<div class="r-top clearfix">
    <p class="text-info text-center lead"><strong>预支申请单</strong></p>
</div>
<!--搜索over-->
    <#if Session["userSession"]?exists>
        <#assign userInfo=Session["userSession"]?if_exists>
    <div class="mart5">
        <form class="form-horizontal" action="/hr/user!save.dhtml" method="POST" name="editForm"
              id="editForm">
            <table class="table application nomar">
                <tbody>
                <tr>
                    <td colspan="2">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="hrUser.userName"
                                   style="width: 60px;color: #898989;font-weight: bold;">申请标题</label>

                            <div class="controls" style="margin-left: 70px;">
                                <input class="width-510" type="text" id="hrUser.userName" name="hrUser.userName"
                                       placeholder="员工姓名">
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="hrUser.userName"
                                   style="width: 60px;color: #898989;font-weight: bold;">申请人</label>

                            <div class="controls" style="margin-left: 70px;">
                                <label style="margin-top: 5px;font-size: 14px;">${userInfo.userName?if_exists}</label>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="hrUser.userName"
                                   style="width: 60px;color: #898989;font-weight: bold;">申请时间</label>

                            <div class="controls" style="margin-left: 70px;">
                                <label style="margin-top: 5px;font-size: 14px;">2009-12-12 12:12:00</label>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="hrUser.userName"
                                   style="width: 60px;color: #898989;font-weight: bold;">预支金额</label>

                            <div class="controls" style="margin-left: 70px;">
                                <input type="text" id="hrUser.userName" name="hrUser.userName" placeholder="预支金额">
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="hrUser.userName"
                                   style="width: 60px;color: #898989;font-weight: bold;">支付方式</label>

                            <div class="controls" style="margin-left: 70px;">
                                <select class="int2 width-160">
                                    <option value="1">现金</option>
                                    <option value="2">银行转账</option>
                                    <option value="3">支票</option>
                                </select>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="hrUser.userName"
                                   style="width: 60px;color: #898989;font-weight: bold;">用途</label>

                            <div class="controls" style="margin-left: 70px;">
                                <textarea rows="2" class="width-510 font12"></textarea>
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div class="control-group" style="margin-bottom: 5px;">
                            <label class="control-label" for="hrUser.userName"
                                   style="width: 60px;color: #898989;font-weight: bold;">备注</label>

                            <div class="controls" style="margin-left: 70px;">
                                <textarea rows="4" class="width-510 font12"></textarea>
                                <span class="help-inline"></span>
                            </div>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </form>
    </div>
    <p class="mart10 marr15 clearfix">
        <button class="btn btn-success floatright " type="button" id="nextBtn">继续</button>
    </p>
    </#if>
</@wfCommon.wf_common>