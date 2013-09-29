<#import "/view/template/structure/sys/sysCommon.ftl" as sysCommon>
<#import "/view/common/core.ftl" as c>
<@sysCommon.sys_common>
<link type="text/css" href="/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<script src="/js/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript">
    var setting = {
        view: {
            selectedMulti: false
        },
        async: {
            enable: true,
            url:"../asyncData/getNodes.php",
            autoParam:["id", "name=n", "level=lv"],
            otherParam:{"groupId":"zTreeAsyncTest"}
        }
    };
    function refreshNode(e) {
        var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
                type = e.data.type,
                silent = e.data.silent,
                nodes = zTree.getSelectedNodes();
        if (nodes.length == 0) {
            alert("请先选择一个父节点");
        }
        for (var i=0, l=nodes.length; i<l; i++) {
            zTree.reAsyncChildNodes(nodes[i], type, silent);
            if (!silent) zTree.selectNode(nodes[i]);
        }
    }
    $(document).ready(function () {
        $.fn.zTree.init($("#treeDemo"), setting);
    });
</script>
<!--搜索begin-->
<div class="r-top clearfix">
    <button class="btn btn-danger floatright" type="button">删除</button>
    <button class="btn btn-warning floatright marr10" type="button" id="newBtn">新增</button>
</div>
<!--搜索over-->

<div style="min-height: 600px;height: 600px;" class="mart10">
    <table style="height: 600px;width:100%;text-align: left;" border="0" >
        <tbody>
        <tr>
            <td style="width: 30%;text-align: left;vertical-align: top;border-right: 1px dashed rgb(153, 153, 153);">
            &nbsp;
            </td>
            <td style="width: auto;text-align: left;vertical-align: top;">

            </td>
        </tr>
        </tbody>
    </table>
</div>
</@sysCommon.sys_common>