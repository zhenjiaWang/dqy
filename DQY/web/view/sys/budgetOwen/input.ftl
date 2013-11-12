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
            url: "/common/common!titleTreeData.dhtml",
            autoParam: ["id=parentId", "name=n", "level=lv"]
        },
        callback: {
            onClick: zTreeOnClick
        }
    };
    function zTreeOnClick(event, treeId, treeNode) {
        srcNode = treeNode;
        if (treeNode) {
            $('#sysBudgetOwen\\.titleId\\.id').val(treeNode['id']);
            $('#sysBudgetOwen\\.title').val(treeNode['name']);
            $('.treeDiv').fadeOut();
        }
    }
    function initValidator() {
        WEBUTILS.validator.init({
            modes: [
                {
                    id: 'sysBudgetOwen\\.title',
                    required: true,
                    pattern: [
                        {type: 'blank', exp: '!=', msg: '不能为空'}
                    ]
                }
            ]
        }, true);
    }
    function dataSubmit() {
        if (!submited) {
            if($('#sysBudgetOwen\\.budgetTitle\\.id').val()!=''){
                if($('#sysBudgetOwen\\.budgetTitle\\.id').val()!='0000'){
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
        }
    }

    $(document).ready(function () {
        initValidator();
        $.fn.zTree.init($("#treeDemo"), setting);

        $('#searchDept').off('click').on('click', function () {
            var left = $(this).offset().left - 225;
            var top = $(this).offset().top + 15;
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
    <@c.joddForm bean="sysBudgetOwen" scope="request">
    <form class="form-horizontal" action="/sys/budgetOwen!save.dhtml" method="POST" name="editForm"
          id="editForm">
        <div class="control-group">
            <label class="control-label" for="sysBudgetOwen.budgetTitle.id">预算科目</label>
            <div class="controls">
                <select id="sysBudgetOwen.budgetTitle.id" name="sysBudgetOwen.budgetTitle.id">
                <#if sysBudgetOwen?exists>
                <option value="${sysBudgetOwen.budgetTitle.id?c}">${sysBudgetOwen.budgetTitle.titleName?if_exists}</option>
                <#elseif sysBudgetTitle?exists>
                    <option value="${sysBudgetTitle.id?c}">${sysBudgetTitle.titleName?if_exists}</option>
                </#if>
                </select>
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="sysBudgetOwen.title">财务科目</label>
            <div class="controls">
                <input type="text" id="sysBudgetOwen.title" name="sysBudgetOwen.title" placeholder="财务科目"
                    <#if sysBudgetOwen?exists> value="${(sysBudgetOwen.titleId.titleName)?if_exists}" </#if>  disabled>
                <span class="add-on" style="cursor: pointer;" id="searchDept"><i class="icon-th"></i></span>
                <span class="help-inline"></span>
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <label class="checkbox">
                    <input type="checkbox" value="Y" id="sysBudgetOwen.useYn" name="sysBudgetOwen.useYn"> 启用
                </label>
            </div>
        </div>
        <input type="hidden" name="sysBudgetOwen.id" id="sysBudgetOwen.id">
        <input type="hidden" name="sysBudgetOwen.titleId.id" id="sysBudgetOwen.titleId.id">
        <@c.token/>
    </form>
    </@c.joddForm>
</@common.html>