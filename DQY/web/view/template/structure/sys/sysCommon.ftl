<#import "/view/template/dqyCommon.ftl" as dqyCommon>
<#import "/view/common/core.ftl" as c>
<#macro sys_common>
    <@dqyCommon.dqyCommon>
    <script type="text/javascript">
        $(document).ready(function () {
        });
    </script>
    <div class="p-top10 clearfix">
        <!--左侧目录树begin-->
        <div class="side-left floatleft marl15">
            <div class="side-left-in font14">
                <!--一组begin-->
                <ul class="item-nav current"><!--ul上的current是为了控制当前这一组的背景色，li上的current是为了控制二级菜单-->
                    <li><a class="cur" href="#">事务申请</a></li>
                    <li class="current">
                        <a class="cur" href="#">费用报销</a>
                        <dl class="font12">
                            <dd class="current"><a href="/sys/orgGroup.dhtml"><i class="icon-hand-right"></i> 待审</a></dd>
                            <dd><a href="/sys/org.dhtml"><i class="icon-hand-right"></i> 已通过</a></dd>
                            <dd><a href="#"><i class="icon-hand-right"></i> 已驳回</a></dd>
                        </dl>
                    </li>
                </ul>
                <!--一组over-->
                <!--一组begin-->
                <ul class="item-nav">
                    <li><a class="cur" href="#">我的申请</a></li>
                    <li>
                        <a class="cur" href="#">单据审批</a>
                        <dl class="font12">
                            <dd><a href="#"><i class="icon-hand-right"></i>待审</a></dd>
                            <dd><a href="#"><i class="icon-hand-right"></i>已通过</a></dd>
                            <dd><a href="#"><i class="icon-hand-right"></i>已驳回</a></dd>
                        </dl>
                    </li>
                    <li><a class="cur" href="#">已审单据</a></li>
                </ul>
                <!--一组over-->
                <!--一组begin-->
                <ul class="item-nav">
                    <li><a class="cur" href="#">报销审批</a></li>
                    <li>
                        <a class="cur" href="#">预支审批</a>
                        <dl class="font12">
                            <dd><a href="#"><i class="icon-hand-right"></i>待审</a></dd>
                            <dd><a href="#"><i class="icon-hand-right"></i>已通过</a></dd>
                            <dd><a href="#"><i class="icon-hand-right"></i>已驳回</a></dd>
                        </dl>
                    </li>
                    <li><a class="cur" href="#">还款审批</a></li>
                </ul>
                <!--一组over-->
                <!--一组begin-->
                <ul class="item-nav">
                    <li><a class="cur" href="#">预算管理</a></li>
                    <li><a class="cur" href="#">财务科目</a></li>
                </ul>
            </div>
        </div>
        <!--左侧目录树over-->
        <!--右侧begin-->
        <div class="right-main floatleft">
            <!--当前位置begin-->
            <div class="location clearfix">
                <span class="font14">申请管理</span>
                <em><img src="../../images/cssimg/now-r.jpg"></em>
                <div class="floatright marr15">
                    <em>当前位置：</em>
                    <a href="#">申请管理</a>
                    <em>&gt;</em>
                    <a href="#">费用报销</a>
                </div>
            </div>
            <!--当前位置over-->
            <div class="right-inner mart5">
                <#nested/>
            </div>
        </div>
        <!--右侧over-->
    </div>
    </@dqyCommon.dqyCommon>
</#macro>