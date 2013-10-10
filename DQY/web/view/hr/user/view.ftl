<#import "/view/template/structure/sys/sysCommon.ftl" as sysCommon>
<#import "/view/common/core.ftl" as c>
<@sysCommon.sys_common>
<script type="text/javascript">
    function reload(){
        document.location.reload();
    }
    $(document).ready(function () {
        $('.changeAdmin').off('click').on('click', function () {
            <#if isAdmin>
                document.location.href = '/hr/user!changeAdmin.dhtml?id=${hrUser.id?c}&adminYn=N';
            <#else >
                document.location.href = '/hr/user!changeAdmin.dhtml?id=${hrUser.id?c}&adminYn=Y';
            </#if>
        });
        $('#passwordBtn').off('click').on('click', function () {
            WEBUTILS.popWindow.createPopWindow(550, null, '设置密码', '/hr/user!changePwd.dhtml?id=${hrUser.id?c}');
        });

        <#if isAuthorized>
            $('#authorizedBtn').off('click').on('click', function () {
                WEBUTILS.popWindow.createPopWindow(420, null, '授权机构', '/sys/authorized!input.dhtml?userId=${hrUser.id?c}');
            });
        </#if>
        $('.deleteAuthorized').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                WEBUTILS.alert.alertComfirm('删除数据','您确认要删除该组织机构授权?',function(){
                    document.location.href='/sys/authorized!delete.dhtml?id='+uid;
                });
            }
        });
        $('#editBtn').off('click').on('click', function () {
            WEBUTILS.popWindow.createPopWindow(800, null, '编辑信息', '/hr/user!input.dhtml?id=${hrUser.id?c}');
        });
        $('#newBtn').off('click').on('click', function () {
            WEBUTILS.popWindow.createPopWindow(800, null, '新增用户', '/hr/user!input.dhtml');
        });
    });
</script>
<!--搜索begin-->
<div class="r-top clearfix">
    <div class="input-append">
        <input type="text" id="appendedInputButton" class="span2">
        <button type="button" class="btn"><i class="icon-search"></i> 搜索</button>
    </div>
    <button class="btn btn-danger floatright" type="button">删除</button>
    <button class="btn btn-warning floatright marr10" type="button" id="newBtn">新增</button>
</div>
<!--搜索over-->

<div class="mart10">
    <table width="100%" class="layout">
        <thead>
        <tr>
            <td colspan="3"><strong>个人信息</strong></td>
            <td style="text-align: right;">
                <button class="btn btn-info" type="button" id="editBtn">编辑</button>
            </td>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>
                <strong>员工姓名：</strong>
                <span>${hrUser.userName?if_exists}</span>
            </td>
            <td>
                <strong>员工编号：</strong>
                <span>${hrUser.userNo?if_exists}</span>
            </td>
            <td>
                <strong>所属机构：</strong>
                <span>${(hrUser.orgId.orgName)?if_exists}</span>
            </td>
            <td>
                <strong>机构编号：</strong>
                <span>${(hrUser.orgId.orgNo)?if_exists}</span>
            </td>
        </tr>
        <tr>
            <td>
                <strong>所属部门：</strong>
                <span>${(hrUser.deptId.deptName)?if_exists}</span>
            </td>
            <td>
                <strong>担任岗位：</strong>
                <span>${hrUser.jobName?if_exists}</span>
            </td>
            <td>
                <strong>工作地点：</strong>
                <span>${hrUser.workArea?if_exists}</span>
            </td>
            <td>
                <strong>入职日期：</strong>
                <span>${hrUser.entryDate?string("yyyy-MM-dd")}</span>
            </td>
        </tr>
        <tr>
            <td>
                <strong>员工性别：</strong>
                <span>
                    <#if hrUserDetail.userSex==0>
                        男
                    <#elseif hrUserDetail.userSex==1>
                        女
                    </#if>
                </span>
            </td>
            <td>
                <strong>出生日期：</strong>
                <span>${hrUserDetail.birthday?string("yyyy-MM-dd")}</span>
            </td>
            <td>
                <strong>教育程度：</strong>
                <span><#if hrUserDetail.eduLevel==0>
                    高中
                <#elseif hrUserDetail.eduLevel==1>
                    大专
                <#elseif hrUserDetail.eduLevel==2>
                    本科
                <#elseif hrUserDetail.eduLevel==3>
                    硕士
                <#elseif hrUserDetail.eduLevel==4>
                    博士生
                </#if></span>
            </td>
            <td>
                <strong>员工状态：</strong>
                <span>
                    <#if hrUser.userState==0>
                        在职
                    <#elseif hrUser.userState==1>
                        离职
                    </#if>
                </span>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<div>
    <table width="100%" class="layout nomar">
        <thead>
        <tr>
            <td style="width: 50%;"><strong>授权信息</strong></td>
            <td style="text-align: right;">
                <#if !isPassword>
                    <button class="btn btn-primary" type="button" id="passwordBtn">设置密码</button>
                <#else >
                    <button class="btn btn-primary" type="button" id="passwordBtn">更改密码</button>
                </#if>
                <#if isAuthorized>
                    <button class="btn btn-success" type="button" id="authorizedBtn">授权</button>
                </#if>
                <#if isAdmin>
                    <button class="btn btn-info changeAdmin" type="button"><i class="icon-remove icon-white"></i>&nbsp;关闭管理员
                    </button>
                <#else >
                    <button class="btn btn-info changeAdmin" type="button"><i class="icon-ok icon-white"></i>&nbsp;开通管理员</button>
                </#if>
            </td>
        </tr>
        </thead>
    </table>
    <table class="table table-hover  table-bordered" style="margin-top: 5px;">
        <thead>
        <tr>
            <th>授权机构</th>
            <th>授权时间</th>
            <th>授权人</th>
            <th style="width: 75px;text-align: center;">操作</th>
        </tr>
        </thead>
        <#if authorizedList?exists&&authorizedList?size gt 0>
            <tbody>
                <#list authorizedList as authorized>
                <tr <#if (authorized_index+1)%2!=0>class="oddBgColor"</#if>>
                    <td>${(authorized.orgId.orgName)?if_exists}</td>
                    <td>${(authorized.created)?string("yyyy-MM-dd")}</td>
                    <td>${(authorized.createdBy)?if_exists}</td>
                    <td style="text-align: center;">
                        <a href="###" title="删除" uid="${authorized.id?c}" class="deleteAuthorized"><i class="icon-trash"></i> 删除</a>
                    </td>
                </tr>
                </#list>
            </tbody>
        </#if>
    </table>
</div>
</@sysCommon.sys_common>