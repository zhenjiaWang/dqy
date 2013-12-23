<#import "/view/template/structure/sys/sysCommon.ftl" as sysCommon>
<#import "/view/common/core.ftl" as c>
<@sysCommon.sys_common>
<script type="text/javascript">
    function pagerAction(start, rows) {
        var searchUrl = '/hr/user.dhtml';
        searchUrl += '?start=' + start + '&rows=' + rows;
        var keyword=$('#keyword').val();
        if(keyword){
            searchUrl+='&keyword='+keyword;
        }
        searchUrl = encodeURI(searchUrl);
        document.location.href = searchUrl
    }
    $(document).ready(function () {
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
        $('.addRole').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                WEBUTILS.popWindow.createPopWindow(420, null, '增加角色', '/sys/authorized!role.dhtml?id='+uid);
            }
        });
        $('.addDept').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                WEBUTILS.popWindow.createPopWindow(550, 450, '指派部门', '/sys/authorized!dept.dhtml?id='+uid);
            }
        });
        $('.editDept').off('click').on('click', function () {
            var uid = $(this).attr('uid');
            if (uid) {
                WEBUTILS.popWindow.createPopWindow(550, 450, '修改指派部门', '/sys/authorized!dept.dhtml?id='+uid);
            }
        });

        $('#editBtn').off('click').on('click', function () {
            WEBUTILS.popWindow.createPopWindow(800, 450, '编辑信息', '/hr/user!input.dhtml?id=${hrUser.id?c}');
        });
        $('#newBtn').off('click').on('click', function () {
            WEBUTILS.popWindow.createPopWindow(800, 450, '新增用户', '/hr/user!input.dhtml');
        });

        $('#searchBtn').off('click').on('click', function () {
            pagerAction(0,10);
        });
        $('#keyword').off('keyup').on('keyup', function (e) {
            e = (e) ? e : ((window.event) ? window.event : "");
            var keyCode = e.keyCode ? e.keyCode : (e.which ? e.which : e.charCode);
            if (keyCode == 13) {
                pagerAction(0,10);
            }
        });
        $('#deleteEmailBtn').off('click').on('click', function () {
           if(confirm("确定要删除该${hrUser.userName?if_exists}的公司邮箱:${hrUser.userEmail?if_exists}@dqy.com.cn")){
               document.location.href='/hr/user!deleteEmail.dhtml?id=${hrUser.id?c}';
           }
        });
        $('#createEmailBtn').off('click').on('click', function () {
            WEBUTILS.popWindow.createPopWindow(800, 450, '创建邮箱', '/hr/user!inputEmail.dhtml?id=${hrUser.id?c}');
        });

        $('#passwordEmailBtn').off('click').on('click', function () {
            WEBUTILS.popWindow.createPopWindow(550, null, '设置邮箱密码', '/hr/user!changeEmailPwd.dhtml?id=${hrUser.id?c}');
        });

    });
</script>
<!--搜索begin-->
<div class="r-top clearfix">
    <div class="input-append">
        <input type="text"  class="span2" id="keyword" name="keyword" value="${keyword?if_exists}">
        <button type="button" class="btn" id="searchBtn"><i class="icon-search"></i> 搜索</button>
    </div>
    <button class="btn btn-danger floatright" type="button">删除</button>
    <button class="btn btn-warning floatright marr10" type="button" id="newBtn">新增</button>
</div>
<!--搜索over-->

<div class="mart10">
    <table width="100%" class="layout">
        <thead>
        <tr>
            <td colspan="3"><strong>个人信息 <#if hrUser.userEmail?exists>${hrUser.userEmail?if_exists}@dqy.com.cn</#if></strong></td>
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

                    <#if Session["userSession"]?exists>
                        <#assign userInfo=Session["userSession"]?if_exists>
                        <#assign roleId=userInfo["roleId"]?if_exists>
                        <#if roleId?exists>
                            <#if roleId?contains("EMAIL_CD")>
                                <#if hrUser.userEmail?exists>
                                    <button class="btn btn-primary" type="button" id="deleteEmailBtn">删除邮箱</button>
                                <#else >
                                    <button class="btn btn-primary" type="button" id="createEmailBtn">创建邮箱</button>
                                </#if>
                            </#if>
                            <#if roleId?contains("EMAIL_PWD")>
                                <button class="btn btn-primary" type="button" id="passwordEmailBtn">更改邮箱密码</button>
                            </#if>
                        </#if>
                        <#if !isPassword>
                            <button class="btn btn-primary" type="button" id="passwordBtn">设置密码</button>
                        <#else >
                            <button class="btn btn-primary" type="button" id="passwordBtn">更改密码</button>
                        </#if>
                        <#if isAuthorized>
                            <#if roleId?exists>
                                <#if roleId?contains("SYS_AUTHORIZED")>
                                    <button class="btn btn-success" type="button" id="authorizedBtn">授权机构</button>
                                </#if>
                            </#if>
                        </#if>
                    </#if>
            </td>
        </tr>
        </thead>
    </table>
    <table class="table table-hover  table-bordered" style="margin-top: 5px;">
        <thead>
        <tr >
            <th  style="width: 160px;text-align: center;">授权机构</th>

            <th style="text-align: center;">指派部门</th>
            <th  style="width: 330px; text-align: center;">权限</th>
            <th style="width: 120px;text-align: center;">操作</th>
        </tr>
        </thead>
        <#if authorizedList?exists&&authorizedList?size gt 0>
            <tbody>
                <#list authorizedList as authorized>
                <tr <#if (authorized_index+1)%2!=0>class="oddBgColor"</#if>>
                    <td class="txt_hidden" style="width: 160px; text-align: center;">${(authorized.orgId.orgName)?if_exists}</td>
                    <td style="text-align: center;">
                    <#if (authorized.orgId.id)!=(hrUser.orgId.id)&&(authorized.groupId.id)==(hrUser.groupId.id)>
                        <#if authorized.deptId?exists>
                            <a href="###" orgId="${(authorized.orgId.id)?c}"  uid="${authorized.id?c}" class="editDept"> <i class="icon-edit"></i>${(authorized.deptId.deptName)?if_exists}</a>
                        <#else >
                            <a href="###" orgId="${(authorized.orgId.id)?c}"  uid="${authorized.id?c}" class="addDept"> <i class="icon-plus"></i>指派部门</a>
                        </#if>
                    <#else >
                    ${(authorized.deptId.deptName)?if_exists}
                    </#if>
                    </td>
                    <td class="txt_hidden" style="width:330px;text-align: left;">
                    <span title="${(authorized.roleName)?if_exists}">${(authorized.roleName)?if_exists}</span>
                    </td>
                    <td style="text-align: center;">
                        <#if roleId?exists>
                            <#if roleId?contains("SYS_AUTHORIZED")>
                                <a href="###" title="增加角色" uid="${authorized.id?c}" class="addRole"><i class="icon-plus"></i>设置权限</a>
                                <#if (authorized.orgId.id)!=(hrUser.orgId.id)&&(authorized.groupId.id)==(hrUser.groupId.id)>
                                    <a href="###" title="删除" uid="${authorized.id?c}" class="deleteAuthorized"><i class="icon-trash"></i> 删除</a>
                                </#if>
                            </#if>
                        </#if>
                    </td>
                </tr>
                </#list>
            </tbody>
        </#if>
    </table>
</div>
</@sysCommon.sys_common>