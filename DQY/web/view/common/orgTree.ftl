<#import "/view/template/common.ftl" as common>
<#import "/view/common/core.ftl" as c>
<@common.html>
<link type="text/css" href="/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<script src="/js/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript">
    var srcNode;
    var setting = {
        view: {
            selectedMulti: false
        },
        async: {
            enable: true,
            url:"/common/common!orgTreeData.dhtml",
            autoParam:["id=parentId", "name=n", "level=lv"]
        },
        callback: {
            onClick: zTreeOnClick
        }
    };
    function zTreeOnClick(event, treeId, treeNode) {
        srcNode=treeNode;
    };
    $(document).ready(function () {
        $.fn.zTree.init($("#treeDemo"), setting);
    });
</script>
<ul id="treeDemo" class="ztree"></ul>
</@common.html>