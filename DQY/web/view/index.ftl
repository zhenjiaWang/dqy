<#import "/view/template/dqyCommon.ftl" as dqyCommon>
<#import "/view/common/core.ftl" as c>
<@dqyCommon.dqyCommon>
<script type="text/javascript">
    $(document).ready(function () {
    });
</script>
<div class="content p-top5">
    <div class="banner"><img src="../images/banner.jpg"></div>
    <div class="clearfix">
        <!--左侧四块begin-->
        <div class="left-count floatleft">
            <div class="item font12">
                <h2 class="clearfix">
                    <strong class="floatleft  marl5"><em class="dqy-ico dqy-ico-board"></em>公告栏</strong>
                    <a title="查看更多" class="more floatright marr10" href="#"><img src="../images/cssimg/more.jpg"></a>
                </h2>
                <ul class="news">
                    <li class="clearfix">
                        <a href="#">新OA系统开通</a>
                        <span class="time floatright">09.03.24</span>
                    </li>
                    <li class="clearfix">
                        <a href="#">部门填报预算开通</a>
                        <span class="time floatright">09.03.24</span>
                    </li>
                    <li class="clearfix">
                        <a href="#">费用预支开通</a>
                        <span class="time floatright">09.03.24</span>
                    </li>
                    <li class="clearfix">
                        <a href="#">预支还款开通</a>
                        <span class="time floatright">09.03.24</span>
                    </li>
                    <li class="clearfix">
                        <a href="#">费用申请开通</a>
                        <span class="time floatright">09.03.24</span>
                    </li>
                </ul>
            </div>
            <div class="item font12">
                <h2 class="clearfix">
                    <strong class="floatleft  marl5"><em class="dqy-ico dqy-ico-post"></em>我的申请</strong>
                    <a title="查看更多" class="more floatright marr10" href="/wf/req!ingList.dhtml"><img src="../images/cssimg/more.jpg"></a>
                </h2>
                <ul class="news">
                    <#if reqList?exists&&reqList?size gt 0>
                    <#list reqList as req>
                        <li class="clearfix">
                            <div  class="txt_hidden" style="width: 190px;"><a href="/wf/req!view.dhtml?id=${req.id?c}">${req.subject?if_exists}</a></div>
                            <#if req.applyState==1>
                                <span class="noread  floatright">审核中</span>
                            <#elseif req.applyState==2>
                                <#if req.applyResult==1>
                                    <span class="cross nocross floatright">已通过</span>
                                <#elseif req.applyResult==2>
                                    <span class="cross floatright">未通过</span>
                                </#if>
                            </#if>
                        </li>
                    </#list>
                    </#if>
                </ul>
            </div>
            <div class="item font12">
                <h2 class="clearfix">
                    <strong class="floatleft  marl5"><em class="dqy-ico dqy-ico-task"></em>我的事务</strong>
                    <a title="查看更多" class="more floatright marr10" href="#"><img src="../images/cssimg/more.jpg"></a>
                </h2>
                <ul class="news">
                    <li class="clearfix">
                        <a href="#">购买办公用品</a>
                        <span class="time floatright">09.03.24</span>
                    </li>
                    <li class="clearfix">
                        <a href="#">采购新产品</a>
                        <span class="time floatright">09.03.24</span>
                    </li>
                    <li class="clearfix">
                        <a href="#">促销计划</a>
                        <span class="time floatright">09.03.24</span>
                    </li>
                    <li class="clearfix">
                        <a href="#">新产品实验</a>
                        <span class="time floatright">09.03.24</span>
                    </li>
                    <li class="clearfix">
                        <a href="#">费用报销补充规定</a>
                        <span class="time floatright">09.03.24</span>
                    </li>
                </ul>
            </div>
            <div class="item font12">
                <h2 class="clearfix">
                    <strong class="floatleft  marl5"><em class="dqy-ico dqy-ico-re"></em>我的审批</strong>
                    <a title="查看更多" class="more floatright marr10" href="/wf/reqTask!approveList.dhtml"><img src="../images/cssimg/more.jpg"></a>
                </h2>
                <ul class="news">
                    <#if taskList?exists&&taskList?size gt 0>
                        <#list taskList as task>
                            <li class="clearfix">
                                <div  class="txt_hidden" style="width: 190px;"><a href="/wf/reqTask!process.dhtml?id=${task.id?c}">${(task.reqId.subject)?if_exists}</a></div>
                                <#if task.taskRead==0>
                                    <span class="noread floatright">未查看</span>
                                <#elseif task.taskRead==1>
                                    <span class="noread floatright">已查看</span>
                                </#if>
                            </li>
                        </#list>
                    </#if>
                </ul>
            </div>
        </div>
        <!--左侧四块over-->
        <!--右侧两块beign-->
        <div class="floatleft">
            <!--my page begin-->
            <div class="item2">
                <h2 class="aligncenter font16">快速导航</h2>
                <ul class="news">
                    <li class="clearfix">
                        <a href="/wf/reqTask!approveList.dhtml">未审批申请<span class="noread floatright">进入</span></a>
                    </li>
                    <li class="clearfix">
                        <a href="/wf/req!ingList.dhtml">已提交申请<span class="noread floatright">进入</span></a>
                    </li>
                    <li class="clearfix">
                        <a href="http://mail.dqy.com.cn" target="_blank">公司企业邮箱<span class="noread floatright">进入</span>
                        </a>
                    </li>
                </ul>
            </div>
            <!--my page over-->
            <!--代办提醒begin-->
            <div class="item2 mart10">
                <h2 class="aligncenter font16">未开放功能</h2>
            </div>
            <!--代办提醒over-->
        </div>
        <!--右侧两块over-->
    </div>
</div>
</@dqyCommon.dqyCommon>