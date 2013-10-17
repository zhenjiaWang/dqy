<#import "/view/template/common.ftl" as common>
<#import "/view/common/core.ftl" as c>
<@common.html>
<link href="/css/validator/validator.css" rel="stylesheet"/>
<script type="text/javascript" src="/js/webutils/webutils.validator.js"></script>
<script type="text/javascript" src="/js/webutils/reg.js"></script>

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
            autoParam: ["id=parentId", "name=n", "level=lv"],
            otherParam: { "orgId":"${orgId?c}"}
        },
        callback: {
            onClick: zTreeOnClick
        }
    };

    function initValidator() {
        WEBUTILS.validator.init({
            modes: [
                {
                    id: 'deptName',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'}
                    ]
                }
            ]
        }, true);
    }
    function zTreeOnClick(event, treeId, treeNode) {
        srcNode = treeNode;
        if(treeNode){
            $('#deptId').val(treeNode['id']);
            $('#deptName').val(treeNode['name']);
            $('.treeDiv').fadeOut();
        }
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
    });
</script>
    <form class="form-horizontal" action="/sys/authorized!saveDept.dhtml" method="POST" name="editForm"
          id="editForm">
        <div class="control-group">
            <label class="control-label" for="deptName">指派部门</label>
            <div class="controls">
                <input type="text" id="deptName" name="deptName" placeholder="所属部门"
                       value="${(sysAuthorized.deptId.deptName)?if_exists}" disabled>
                <span class="add-on" style="cursor: pointer;" id="searchDept"><i class="icon-th"></i></span>
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" >姓名</label>
            <div class="controls">
                <label style="margin-top: 5px;padding-left:5px;font-size: 14px;">${(sysAuthorized.userId.userName)?if_exists}</label>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" >部门</label>
            <div class="controls">
                <label style="margin-top: 5px;padding-left:5px;font-size: 14px;">${(sysAuthorized.userId.deptId.deptName)?if_exists}</label>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" >指派机构</label>
            <div class="controls">
                <label style="margin-top: 5px;padding-left:5px;font-size: 14px;">${(sysAuthorized.orgId.orgName)?if_exists}</label>
            </div>
        </div>
        <input type="hidden" name="id" id="id" value="${sysAuthorized.id?c}">
        <input type="hidden" name="deptId" id="deptId" <#if sysAuthorized.deptId?exists> value="${(sysAuthorized.deptId.id)?c}" </#if>>
        <@c.token/>
    </form>
</@common.html>