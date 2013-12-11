var validateTipTemp =
    '<div class="reqformError parentFormformID formError" style="opacity: 0.87; position: absolute; top: {top}px; left: {left}px;" forId="{forId}">' +
        '<div class="formErrorContent">* {message}<br></div>' +
        '<div class="formErrorArrow">' +
        '<div class="line10">&nbsp;</div>' +
        '<div class="line9">&nbsp;</div>' +
        '<div class="line8">&nbsp;</div>' +
        '<div class="line7">&nbsp;</div>' +
        '<div class="line6">&nbsp;</div>' +
        '<div class="line5">&nbsp;</div>' +
        '<div class="line4">&nbsp;</div>' +
        '<div class="line3">&nbsp;</div>' +
        '<div class="line2">&nbsp;</div>' +
        '<div class="line1">&nbsp;</div>' +
        '</div>' +
        '</div>';

var searchAllTemp = '<li ><a href="###" type="all">显示全部数据</a></li>';

/**********遮罩*********/
var blockLayout =
    '<div id="blockLayout" style=" display: none; z-index: 1000000003; border: medium none; margin: 0pt; padding: 0pt; width: 100%; height: 100%; top: 0pt; left: 0pt; background:none repeat scroll 0 0 transparent; cursor: wait; position: fixed;"></div>';
var ddmoveShadow = '<div id="shortcut_shadow" style="position:absolute;z-index: 100000003;">{object}</div>';

var ddmoveControl = '<span class="md-p-in"><a>{text}</a></span>';

var ddmoveApply = '<div class="md-doc-item-box-s floatleft">' +
    '<div class="md-doc-item" title="员工入职申请">' +
    '<a href="#" class="h2">{applyName}</a>' +
    '<div class="md-plus-h clearfix">' +
    '<span class="md-ico floatleft md-ico-01"></span>' +
    '<span class="md-doc-txt floatleft">{applyDesc}</span>' +
    '</div>' +
    '<p class="mart10 clearfix"><a href="#" class="md-plus-btn floatright">填 写<em class="md-plus-btn-r"></em></a></p>' +
    '</div>' +
    '</div>';

var ddmoveControlSpan = '<li uid="{id}">' +
    '<span class="md-p-in">' +
    '<a>{text}</a>' +
    '<a class="md-delect" href="#"></a>' +
    '</span>' +
    '</li>';

var popMdPlus = ' <div style="width:{width}px;top:50%;left:50%;display:none;margin:{margin}" class="md-plus-pop">' +
    '<div class="md-plus-pop-inner">' +
    '<a class="md-delect"></a>' +
    '<p class="font18 md-title">{title}</p>' +
    '<div>' +
    '<iframe width="100%" scrolling="{scrolling}" height="{height}px" frameborder="0" src="{url}" style="overflow-x:hidden;" id="pop-md-plus-iframe">' +
    '</iframe>' +
    '</div>' +
    '</div>' +
    '</div>';

var popMdDiv = ' <div style="width:{width}px;top:50%;left:50%;display:none;margin:{margin}" class="md-plus-pop">' +
    '<div class="md-plus-pop-inner">' +
    '<a class="md-delect"></a>' +
    '<p class="font18 md-title">{title}</p>' +
    '<div>' +
    '<div width="100%"  height="{height}px"  style="overflow-x:hidden;" id="pop-md-div">' +
    '</div>' +
    '</div>' +
    '</div>' +
    '</div>';

var flowDD = '<dd uid="{flowId}">' +
    '<a class="flowNodeA">{flowName}</a>' +
    '<div class="md-btn-team flowDiv">' +
    '<a class="md-mi-ico2 md-mi-edit" uid="{flowId}"></a>' +
    '<a class="md-mi-ico2 md-mi-delete" uid="{flowId}"></a>' +
    '<a class="md-mi-ico2 md-mi-up" uid="{flowId}"></a>' +
    '<a class="md-mi-ico2 md-mi-down" uid="{flowId}"></a>' +
    '</div>' +
    '</dd>';

var dataDD = '<dd uid="{dataId}">' +
    '<a class="dataA">{keyName}</a>' +
    '<div class="md-btn-team dataDiv">' +
    '<a class="md-mi-ico2 md-mi-edit" uid="{dataId}"></a>' +
    '<a class="md-mi-ico2 md-mi-delete" uid="{dataId}"></a>' +
    '<a class="md-mi-ico2 md-mi-up" uid="{dataId}"></a>' +
    '<a class="md-mi-ico2 md-mi-down" uid="{dataId}"></a>' +
    '</div>' +
    '</dd>';

var dataDDLI = '<li uid="{dataId}" class="current">' +
    '<div><span class="m-p-plus"></span>' +
    '<a> {keyName}</a>' +
    '<div class="md-btn-team">' +
    '<a uid="{dataId}" class="md-mi-ico2 md-mi-add dataAdd"></a>' +
    '</div>' +
    '</div>' +
    '<dl style="display: none;">' +
    '</dl>' +
    '</li>';


var resultUserDIV = '<div style="left:{left}px;top:{top}px;display: none;" class="md-result-user"></div>';
var resultUserItemDIV = '<div class="md-results {classMd} clearfix" uid="{userId}" type="{type}">' +
    '<span><img src="{avStar100}"></span>' +
    '<span class="md-name">{userName}</span>' +
    '<span>（{deptName}</span> ' +
    '<span>{jobName}）</span>' +
    '</div>';

var resultVariableItemDIV = '<div class="md-results {classMd} clearfix" uid="{id}" type="{type}">' +
    '<span><img src="{avStar100}"></span>' +
    '<span class="md-name">{name}</span>' +
    '<span>（{deptName}</span> ' +
    '<span>{jobName}）</span>' +
    '</div>';

var addUserItem = '<li uid="{userId}" type="{type}"><span class="md-pro-user"><em>{userName}</em><a title="删除此人" class="md-delect"></a></span></li>';


var alertSuccess = '<div style="display:none;top:30%;left:50%;" class="alertDialog" id="alertSuccessDialogMD">' +
    '<span>{text}</span>' +
    '<a class="close" href="#">×</a>' +
    '</div>';

var alertError = '<div style="display:none;top:30%;left:50%;" class="errorDialog" id="alertErrorDialogMD">' +
    '<span>{text}</span>' +
    '<a class="close" href="#">×</a>' +
    '</div>';

var addMyFlowNode = '<tr>' +
    '<th><span class="font14">第{index}步审批</span></th>' +
    '<td>' +
    '<div class="md-clander floatleft">' +
    '<input type="text" class="md-int-mid searchApprove" id="node{index}" name="node{index}" nodeSeq="{index}">' +
    '<a class="md-ser-ico" nodeSeq="{index}"></a>' +
    '<ul class="add-user-list clearfix" nodeSeq="{index}">' +
    '</ul>' +
    '</div>' +
    '</td>' +
    '</tr>';
var addMyFlowNodeHide = '<input type="hidden" name="nodeType{index}" id="nodeType{index}" nodeSeq="{index}">' +
    '<input type="hidden" name="approveType{index}" id="approveType{index}" nodeSeq="{index}">' +
    '<input type="hidden" name="approve{index}" id="approve{index}" nodeSeq="{index}">';

var approveNodeDiv = '<div class="md-ap-pro" rows="{rows}">' +
    '<ul class="clearfix mart10" >' +
    '</ul>' +
    '</div>';

var approveNodeBox = '<div class="md-doc-set-box" style="margin-left: 15px;" rows="{rows}">' +
    '<div class="md-ap-pro">' +
    '<ul class="clearfix mart10">' +
    '</ul>' +
    '</div>' +
    '</div>';


var approveNodeLi = '<li class="flowNodeLi {className}" nodeType="{nodeType}" approveType="{approveType}" approveId="{approveId}" nodeSeq="{nodeSeq}">' +
    '<span class="md-num-ico">{nodeIndex}</span>' +
    '<span class="md-pro-user" >' +
    '<em>{approveName}</em>' +
    '<a title="删除此审批人" class="md-delect"></a>' +
    '</span>' +
    '</li><li class="arrow"><em class="md-r-arrow"></em></li>';

var approveNodeLiMobile='<li class="flowNodeLi" nodeType="{nodeType}" approveType="{approveType}" approveId="{approveId}" nodeSeq="{nodeSeq}">' +
    '<span class="label label-info">{nodeIndex}</span><a href="#">{approveName}' +
    '</a><span class="icon-arrow-right"></span></li>';

var ddmoveMyFlowNode = '<span class="md-pro-user" >' +
    '<em>{approveName}</em>' +
    '</span>';

var maskDiv = '<div class="mask">' +
    '<span class="mask-x">{text}</span>' +
    '</div>';

var maskMobileDiv = '<div style="display:block;" class="mask">' +
    ' <span class="mask-x">{text}</span>' +
    '</div>';

var appTable = '<div class="md-doc-set-box" rows="{rows}">' +
    '<a title="删除此行" class="md-delect cut-table" href="#"></a>' +
    '<table width="100%" class="md-set-table">' +
    '<tbody><tr>' +
    '<th></th>' +
    '<td>' +
    '<div class="td-inner clearfix">' +
    '<a class="add-controls" href="#">添加控件</a>' +
    '<span class="floatright md-make-btns">' +
    '</span>' +
    '</div>' +
    '</td>' +
    '<th></th>' +
    '<td>' +
    '<div class="td-inner clearfix">' +
    '<a class="add-controls" href="#">添加控件</a>' +
    '<span class="floatright md-make-btns">' +
    '<a href="#" class="md-minus-ico" title="删除一列"></a>' +
    '</span>' +
    '</div>' +
    '</td>' +
    '</tr>' +
    '</tbody></table>' +
    '</div>';

var applyBoxDiv = '<div class="md-doc-item-box floatleft">' +
    '<div title="{applyName}" class="md-doc-item">' +
    '<h2 class="font14"><a href="#">{applyName}</a></h2>' +
    '<div class="md-plus-h clearfix">' +
    '<span class="md-ico floatleft md-ico-01"> <img src="/images/theme{themeType}-img/{applyIco}"></span>' +
    '<span class="md-doc-txt floatleft">{applyDesc}</span>' +
    '</div>' +
    '<p class="alignright">' +
    '<a title="编辑" class="md-mi-ico2 md-mi-edit" uid="{applyId}"></a>' +
    '<a title="删除" class="md-mi-ico2 md-mi-delete" uid="{applyId}"></a>' +
    '</p>' +
    '</div>' +
    '</div>';

var applyBoxDivAdd = '<div class="md-doc-item-box floatleft addBox">' +
    '<div class="md-doc-item addApply" style="cursor: pointer;">' +
    '<p class="aligncenter mart30"><i class="md-plus-add"></i></p>' +
    '<p class="aligncenter font14 mart10 strong"><a href="#">创建申请模板</a></p>' +
    '</div>' +
    '</div>';


var globalDiv = '<div class="md-doc-item-box floatleft"><div class="md-doc-item">' +
    '<h2>' +
    '<span class="txt_hidden font14">{globalName}</span>' +
    '<span class="floatright">' +
    '<a uid="{globalId}" class="md-mi-ico2 md-mi-edit" title="编辑"></a>' +
    '<a uid="{globalId}" class="md-mi-ico2 md-mi-delete" title="删除"></a>' +
    '</span>' +
    '</h2>' +
    '<div class="clearfix mart10">' +
    '<div class="md-plus-pho floatleft"><img src="{avstar}"></div>' +
    '<ul class="md-user-info marl5 floatleft">' +
    '<li>{userName}</li>' +
    '<li>{deptName}</li>' +
    '<li>{jobName}</li>' +
    '</ul>' +
    '</div>' +
    '</div></div>';

var globalDivAdd = '<div class="md-doc-item-box floatleft">' +
    '<div style="cursor: pointer;" class="md-doc-item addGlobal">' +
    '<p class="aligncenter mart30"><i class="md-plus-add"></i></p>' +
    '<p class="aligncenter font14 mart10 strong"><a href="#">创建审批职位</a></p>' +
    '</div>' +
    '</div>';

var mobileModal='<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">' +
'<div class="modal-header">' +
    '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>' +
    '<h3 id="myModalLabel">{title}</h3>' +
    '</div>' +
    '<div class="modal-body">' +
    '<iframe width="100%" scrolling="{scrolling}" height="{height}px" frameborder="0" src="{url}" style="overflow-x:hidden;" id="pop-md-plus-iframe">' +
    '</iframe>' +
    '</div>' +
    '<div class="modal-footer">' +
    '<button class="btn" data-dismiss="modal" aria-hidden="true">关 闭</button>' +
    '<button class="btn btn-primary">保 存</button>' +
    '</div>' +
'</div>';

var alertSuccessDQY='<div class="alert fade in alert-success" id="myAlertSuccess" style="display: none;width: 350px;height: 40px;top: 50%;left: 50%;z-index: 99999;position: fixed;margin: -20px 0 0 -175px;">' +
    '<button data-dismiss="alert" class="close" type="button">×</button>' +
    '<strong>{text}</strong><span>{desc}</span>' +
    '</div>';

var alertWarningDQY='<div class="alert fade in alert-warning" id="myAlertWarning" style="display: none;width: 350px;height: 40px;top: 50%;left: 50%;z-index: 99999;position: fixed;margin: -20px 0 0 -175px;">' +
    '<button data-dismiss="alert" class="close" type="button">×</button>' +
    '<strong>{text}</strong><span>{desc}</span>' +
    '</div>';


var alertErrorDQY='<div class="alert fade in alert-error" id="myAlertError" style="display: none;width: 350px;height: 40px;top: 50%;left: 50%;z-index: 99999;position: fixed;margin: -20px 0 0 -175px;">' +
    '<button data-dismiss="alert" class="close" type="button">×</button>' +
    '<strong>{text}</strong><span>{desc}</span>' +
    '</div>';

var alertInfoDQY='<div class="alert fade in alert-info" id="myAlertInfo" style="display: none;width: 350px;height: 40px;top: 50%;left: 50%;z-index: 99999;position: fixed;margin: -20px 0 0 -175px;">' +
    '<button data-dismiss="alert" class="close" type="button">×</button>' +
    '<strong>{text}</strong><span>{desc}</span>' +
    '</div>';

var alertConfirmDQY=' <div class="alert alert-block alert-warning fade in" id="myAlertConfirm" style="display: none;width: 350px;height: 70px;top: 50%;left: 50%;z-index: 99999;position: fixed;margin: -35px 0 0 -175px;">' +
    '<button data-dismiss="alert" class="close" type="button">×</button>' +
    '<h4 class="alert-heading">{text}</h4>' +
    '<p class="alert-desc">{desc}</p>' +
    '<p>' +
    '<a href="#" class="btn btn-danger" id="yesBtn">确 认</a> <a href="#" class="btn" id="noBtn">取 消</a>' +
    '</p>' +
    '</div>';


var flowApproveDQY=' <div class="control-group" nodeSeq="{nodeSeq}">' +
    '<label class="control-label" for="node{nodeSeq}">第{nodeSeq}步审批人</label>' +
    '<div class="controls">' +
    '<input type="text"  id="node{nodeSeq}" name="node{nodeSeq}" placeholder="请选择审批人" readonly="readonly" nodeSeq="{nodeSeq}">' +
    '<div class="btn-group" style="margin-left:6px;">' +
    '&nbsp;<a class="btn approveBtn" href="#"><i class="icon-user"></i></a>' +
    '</div>' +
    '<span class="help-inline"></span>' +
    '<input type="hidden" name="nodeType{nodeSeq}" id="nodeType{nodeSeq}" nodeSeq="{nodeSeq}">' +
    '<input type="hidden" name="approveType{nodeSeq}" id="approveType{nodeSeq}" nodeSeq="{nodeSeq}">' +
    '<input type="hidden" name="approve{nodeSeq}" id="approve{nodeSeq}" nodeSeq="{nodeSeq}">' +
    '</div>' +
    '</div>';


var rePaymentDetail='<tr seq="{seq}" class="detailTr">' +
    '<td><div class="control-group" style="margin-bottom: 0px;"><input type="text" class="int1 width-100 dept" id="deptName{seq}" name="deptName{seq}" readonly="readonly" placeholder="选择部门">' +
    '<input type="hidden"  id="deptId{seq}" name="deptId{seq}" ></div></td>' +
    '<td><select class="int2 width-100" id="typeId{seq}" name="typeId{seq}">' +
    '</select></td>' +
    '<td><select class="int2 width-100" id="titleId{seq}" name="titleId{seq}">' +
    '</select></td>' +
    '<td data-date-format="yyyy-mm-dd" data-date="" class="date dateTd">' +
    '<div class="control-group" style="margin-bottom: 0px;">' +
    '<input type="text" class="int1 width-100" id="date{seq}" name="date{seq}" >' +
    '</div>' +
    '</td>' +
    '<td><input type="text" class="int1 width-70 amt" id="amount{seq}" name="amount{seq}" value="0.00"></td>' +
    '<td><input type="text" class="int1 " style="width: 95%;" id="remarks{seq}" name="remarks{seq}"></td>' +
    '</tr>';
var rePaymentDetailTrue='<tr seq="{seq}" class="detailTr{type}" type="{type}">' +
    '<td><div class="control-group" style="margin-bottom: 0px;"><input type="text" class="int1 width-100 dept" id="deptName{seq}_{type}" name="deptName{seq}_{type}" readonly="readonly" placeholder="选择部门">' +
    '<input type="hidden"  id="deptId{seq}_{type}" name="deptId{seq}_{type}" ></div></td>' +
    '<td><select class="int2 width-100" id="typeId{seq}_{type}" name="typeId{seq}_{type}">' +
    '</select></td>' +
    '<td><select class="int2 width-100" id="titleId{seq}_{type}" name="titleId{seq}_{type}">' +
    '</select></td>' +
    '<td data-date-format="yyyy-mm-dd" data-date="" class="date dateTd">' +
    '<div class="control-group" style="margin-bottom: 0px;">' +
    '<input type="text" class="int1 width-100" id="date{seq}_{type}" name="date{seq}_{type}" >' +
    '</div>' +
    '</td>' +
    '<td><input type="text" class="int1 width-70 amt{type}" id="amount{seq}_{type}" name="amount{seq}_{type}" value="0.00"></td>' +
    '<td><input type="text" class="int1 " style="width: 95%;" id="remarks{seq}_{type}" name="remarks{seq}_{type}"></td>' +
    '</tr>';


var flowApproveNodeShow='<li class="nodeLi" nodeSeq="{nodeSeq}" nodeType="{nodeType}" approveType="{approveType}" approveId="{approveId}"><a href="#"><span class="badge {className}">{nodeSeqText}</span>{text}</a></li><li><i class="dqy-ico dqy-r"></i></li>';