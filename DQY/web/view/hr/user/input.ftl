<#import "/view/template/common.ftl" as common>
<#import "/view/common/core.ftl" as c>
<@common.html>
<link href="/css/datepicker.css" rel="stylesheet"/>
<link href="/css/validator/validator.css" rel="stylesheet"/>
<script type="text/javascript" src="/js/webutils/webutils.validator.js"></script>
<script type="text/javascript" src="/js/webutils/reg.js"></script>
<script type="text/javascript" src="/js/bootstrap-datepicker.js"></script>

<link type="text/css" href="/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<script src="/js/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript">
    var submited = false;
    var srcNode;
    var setting = {
        view: {
            selectedMulti: false
        },
        async: {
            enable: true,
            url: "/common/common!orgTreeData.dhtml",
            autoParam: ["id=parentId", "name=n", "level=lv"]
        },
        callback: {
            onClick: zTreeOnClick
        }
    };
    function zTreeOnClick(event, treeId, treeNode) {
        srcNode = treeNode;
        if(treeNode){
            $('#hrUser\\.deptId\\.id').val(treeNode['id']);
            $('#hrUser\\.dept').val(treeNode['name']);
            $('.treeDiv').fadeOut();
        }
    }
    function initValidator() {
        WEBUTILS.validator.init({
            modes: [
                {
                    id: 'hrUser\\.userName',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'}
                    ]
                },
                {
                    id: 'hrUser\\.jobName',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'}
                    ]
                },
                {
                    id: 'hrUser\\.dept',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'}
                    ]
                },
                {
                    id: 'hrUser\\.workArea',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'}
                    ]
                },
                {
                    id: 'hrUser\\.entryDate',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'}
                    ]
                },
                {
                    id: 'hrUser\\.birthday',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'}
                    ]
                }
            ]
        }, true);
    }
    function dataSubmit(){
        if (!submited) {
            WEBUTILS.validator.checkAll();
            window.setTimeout(function () {
                var passed = WEBUTILS.validator.isPassed();
                if (passed) {
                    document.editForm.submit();
                    submited = true;
                } else {
                    WEBUTILS.validator.showErrors();
                }
            }, 500);
        }
    }
    $(document).ready(function () {
        initValidator();
        $.fn.zTree.init($("#treeDemo"), setting);
        $('#searchDept').off('click').on('click', function () {
            var left = $(this).offset().left-225;
            var top = $(this).offset().top+15;
            top += $(this).height();
            $('.treeDiv').css({
                        top: top,
                        left: left,
                        zIndex: 99999
                    }
            );
            $('.treeDiv').fadeIn();
            $('.treeDiv').find('div').show();
        });

        $('#entryDateBtn').datepicker({
            format: 'yyyy-mm-dd'
        }).on('changeDate', function (ev) {
                    $('#entryDateBtn').datepicker('hide')
                });
        $('#birthdayBtn').datepicker({
            format: 'yyyy-mm-dd'
        }).on('changeDate', function (ev) {
                    $('#birthdayBtn').datepicker('hide')
                });
    });
</script>
    <@c.joddForm bean="hrUser" scope="request">
    <form class="form-horizontal" action="/hr/user!save.dhtml" method="POST" name="editForm"
          id="editForm">
        <table width="100%" class="layout">
            <tbody>
            <tr>
                <td>
                    <div class="control-group">
                        <label class="control-label" for="hrUser.userName" style="width: 60px;">员工姓名</label>

                        <div class="controls" style="margin-left: 80px;">
                            <input type="text" id="hrUser.userName" name="hrUser.userName" placeholder="员工姓名">
                            <span class="help-inline"></span>
                        </div>
                    </div>
                </td>
                <td>
                    <div class="control-group">
                        <label class="control-label" for="hrUser.userNo" style="width: 60px;">员工编号</label>

                        <div class="controls" style="margin-left: 80px;">
                            <input type="text" id="hrUser.userNo" name="hrUser.userNo" placeholder="自动生成" disabled>
                            <span class="help-inline"></span>
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="control-group">
                        <label class="control-label" for="hrUser.dept" style="width: 60px;">所属部门</label>

                        <div class="controls" style="margin-left: 80px;">
                            <input type="text" id="hrUser.dept" name="hrUser.dept" placeholder="所属部门"
                                   value="${(hrUser.deptId.deptName)?if_exists}" disabled>
                            <span class="add-on" style="cursor: pointer;" id="searchDept"><i class="icon-th"></i></span>
                            <span class="help-inline"></span>
                        </div>
                    </div>
                </td>
                <td>
                    <div class="control-group">
                        <label class="control-label" for="hrUser.jobName" style="width: 60px;">担任岗位</label>

                        <div class="controls" style="margin-left: 80px;">
                            <input type="text" id="hrUser.jobName" name="hrUser.jobName" placeholder="担任岗位">
                            <span class="help-inline"></span>
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="control-group">
                        <label class="control-label" for="hrUser.workArea" style="width: 60px;">工作地点</label>

                        <div class="controls" style="margin-left: 80px;">
                            <input type="text" id="hrUser.workArea" name="hrUser.workArea" placeholder="工作地点">
                            <span class="help-inline"></span>
                        </div>
                    </div>
                </td>
                <td>
                    <div class="control-group">
                        <label class="control-label" for="hrUser.entryDate" style="width: 60px;">入职日期</label>
                        <div class="controls date" style="margin-left: 80px;" id="entryDateBtn" data-date-format="yyyy-mm-dd" data-date="<#if hrUser?exists>${hrUser.entryDate?string("yyyy-MM-dd")}</#if>">
                            <input type="text" id="hrUser.entryDate" name="hrUser.entryDate" placeholder="入职日期" readonly="readonly">
                            <span class="add-on"  style="cursor: pointer;" ><i class="icon-calendar"></i></span>
                            <span class="help-inline"></span>
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="control-group">
                        <label class="control-label" for="hrUser.userSex" style="width: 60px;">员工性别</label>

                        <div class="controls" style="margin-left: 80px;">
                            <select id="hrUser.userSex" name="hrUser.userSex">
                                <option value="0">男</option>
                                <option value="1">女</option>
                            </select>
                            <span class="help-inline"></span>
                        </div>
                    </div>
                </td>
                <td>
                    <div class="control-group">
                        <label class="control-label" for="hrUser.birthday" style="width: 60px;">出生日期</label>

                        <div class="controls date" style="margin-left: 80px;" id="birthdayBtn" data-date-format="yyyy-mm-dd" data-date="<#if hrUser?exists>${hrUser.birthday?string("yyyy-MM-dd")}</#if>">
                            <input type="text" id="hrUser.birthday" name="hrUser.birthday" placeholder="出生日期" readonly="readonly">
                            <span class="add-on" style="cursor: pointer;" ><i class="icon-calendar"></i></span>
                            <span class="help-inline"></span>
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="control-group">
                        <label class="control-label" for="hrUser.eduLevel" style="width: 60px;">教育程度</label>

                        <div class="controls" style="margin-left: 80px;">
                            <select id="hrUser.eduLevel" name="hrUser.eduLevel">
                                <option value="0">高中</option>
                                <option value="1">大专</option>
                                <option value="2">本科</option>
                                <option value="3">硕士</option>
                                <option value="4">博士</option>
                            </select>
                            <span class="help-inline"></span>
                        </div>
                    </div>
                </td>
                <td>
                    <div class="control-group">
                        <label class="control-label" for="hrUser.userState" style="width: 60px;">员工状态</label>

                        <div class="controls" style="margin-left: 80px;">
                            <select id="hrUser.userState" name="hrUser.userState">
                                <option value="0">在职</option>
                                <option value="1">离职</option>
                            </select>
                            <span class="help-inline"></span>
                        </div>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
        <input type="hidden" name="hrUser.id" id="hrUser.id">
        <input type="hidden" name="hrUser.deptId.id" id="hrUser.deptId.id">
        <@c.token/>
    </form>
    </@c.joddForm>
</@common.html>