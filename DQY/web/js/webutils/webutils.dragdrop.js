WEBUTILS.dragdrop = (function () {
    var _IsMousedown = false, _ClickLeft = 0, _ClickTop = 0;
    var _handlerDom = null;
    var _targetDom = null;
    var _appMove = 'false';
    return {
        appMove:function (appMove) {
            if (appMove) {
                _appMove = appMove;
            }
            return _appMove;
        },
        control_ddmove:function (controlObj, callback) {
            if (controlObj) {
                $(controlObj).off('mousedown').on('mousedown', function (e) {
                    e.preventDefault();
                    e.stopPropagation();
                    if ($('a', controlObj).hasClass('md-moved')) {
                        return false;
                    }
                    if (e.which == 1) {
                        WEBUTILS.dragdrop.appMove('false');

                        var oldobj = $(this), cx, cy, shadowObj,
                            lay = WEBUTILS.layout.block(),
                            shadowObj = $(String.formatmodel(ddmoveShadow, {"object":String.formatmodel(ddmoveControl, {'text':oldobj.text()})})),
                            shadowW = $(oldobj).width(),
                            shadowH = $(oldobj).height(),
                            xx = e.clientX,
                            yy = e.clientY;
                        $(lay).on("mousemove", function (e) {
                            lay.show();
                            if (xx != e.clientX || yy != e.clientY) {
                                cx = e.clientX <= 0 ? 0 : e.clientX >= $(document).width() ? $(document).width() : e.clientX - shadowW / 2;
                                cy = e.clientY <= 0 ? 0 : e.clientY >= $(document).height() ? $(document).height() : e.clientY - shadowH / 2;
                                $('.md-plus').append(shadowObj);
                                cx = cx - $('.md-plus').offset().left + 10;
                                shadowObj.css({left:cx, top:cy});
                                $('tr', '#controlTable').removeClass('mdcurrent-tr');
                                var currentIn = WEBUTILS.status.currentIn(e);
                                if (currentIn == 'controlTr') {
                                    var trInObj = WEBUTILS.status.controlTrInObj();
                                    if (trInObj) {
                                        $(trInObj).addClass('mdcurrent-tr');
                                    }
                                }
                            }
                            WEBUTILS.dragdrop.appMove('true');
                        });
                        $(lay).on("mouseup", function (e) {
                            $(this).off("mousemove").off("mouseup");
                            shadowObj.remove();
                            if (WEBUTILS.dragdrop.appMove() == 'true') {
                                $.doTimeout('ddmove', 50, function () {
                                    var currentIn = WEBUTILS.status.currentIn(e);
                                    if (currentIn == 'controlTr') {
                                        var trInObj = WEBUTILS.status.controlTrInObj();
                                        if (trInObj) {
                                            $(trInObj).removeClass('mdcurrent-tr');
                                            if ($('li', trInObj).size() < 2) {
                                                $('ul', trInObj).append(String.formatmodel(ddmoveControlSpan, {'id':oldobj.attr('uid'), 'text':oldobj.text()}));
                                                $('a', oldobj).addClass('md-moved');
                                                callback();
                                            }
                                        }
                                    }
                                });
                            }
                            lay.hide();
                            if ($.browser.msie) {
                                lay[0].releaseCapture();
                            }
                        });
                        if ($.browser.msie) {
                            lay[0].setCapture();
                        }
                    }
                });
            }
        },
        controlTr_ddmove:function (controlObj, callback) {
            if (controlObj) {
                $(controlObj).off('mousedown').on('mousedown', function (e) {
                    e.preventDefault();
                    e.stopPropagation();
                    if (e.which == 1) {
                        WEBUTILS.dragdrop.appMove('false');

                        var oldobj = $(this), cx, cy, shadowObj,
                            lay = WEBUTILS.layout.block(),
                            shadowObj = $(String.formatmodel(ddmoveShadow, {"object":String.formatmodel(ddmoveControl, {'text':$('a', oldobj).text()})})),
                            shadowW = $(oldobj).width(),
                            shadowH = $(oldobj).height(),
                            xx = e.clientX,
                            yy = e.clientY;
                        $(lay).on("mousemove", function (e) {
                            lay.show();
                            if (xx != e.clientX || yy != e.clientY) {
                                cx = e.clientX <= 0 ? 0 : e.clientX >= $(document).width() ? $(document).width() : e.clientX - shadowW / 2;
                                cy = e.clientY <= 0 ? 0 : e.clientY >= $(document).height() ? $(document).height() : e.clientY - shadowH / 2;
                                $('.md-plus').append(shadowObj);
                                cx = cx - $('.md-plus').offset().left + 10;
                                shadowObj.css({left:cx, top:cy});
                                $('tr', '#controlTable').removeClass('mdcurrent-tr');
                                var currentIn = WEBUTILS.status.currentIn(e);
                                if (currentIn == 'controlTr') {
                                    var trInObj = WEBUTILS.status.controlTrInObj();
                                    if (trInObj) {
                                        $(trInObj).addClass('mdcurrent-tr');
                                    }
                                }
                            }
                            WEBUTILS.dragdrop.appMove('true');
                        });
                        $(lay).on("mouseup", function (e) {
                            $(this).off("mousemove").off("mouseup");
                            shadowObj.remove();
                            if (WEBUTILS.dragdrop.appMove() == 'true') {
                                $.doTimeout('ddmove', 50, function () {
                                    var currentIn = WEBUTILS.status.currentIn(e);
                                    if (currentIn == 'controlTr') {
                                        var trInObj = WEBUTILS.status.controlTrInObj();
                                        if (trInObj) {
                                            $(trInObj).removeClass('mdcurrent-tr');
                                            if ($('li', trInObj).size() < 2) {
                                                $('ul', trInObj).append(String.formatmodel(ddmoveControlSpan, {'id':oldobj.attr('uid'), 'text':$('a', oldobj).text()}));
                                                $(oldobj).remove();
                                                callback();
                                            }
                                        }
                                    }
                                });
                            }
                            lay.hide();
                            if ($.browser.msie) {
                                lay[0].releaseCapture();
                            }
                        });
                        if ($.browser.msie) {
                            lay[0].setCapture();
                        }
                    }
                });
            }
        },
        apply_ddmove:function (controlObj,callback) {
            if (controlObj) {
                $(controlObj).off('mousedown').on('mousedown', function (e) {
                    e.preventDefault();
                    e.stopPropagation();
                    if (e.which == 1) {
                        WEBUTILS.dragdrop.appMove('false');
                        var oldobj = $(this), cx, cy, shadowObj,
                            lay = WEBUTILS.layout.block(),
                            shadowObj = $(String.formatmodel(ddmoveShadow, {
                                "object":String.formatmodel(ddmoveApply, {
                                    'applyName':$('.h2',oldobj).text(),
                                    'applyDesc':$('.md-doc-txt',oldobj).text()
                                })})),
                            shadowW = $(oldobj).width(),
                            shadowH = $(oldobj).height(),
                            xx = e.clientX,
                            yy = e.clientY;

                        $(lay).on("mousemove", function (e) {
                            lay.show();
                            if (xx != e.clientX || yy != e.clientY) {
                                cx = e.clientX <= 0 ? 0 : e.clientX >= $(document).width() ? $(document).width() : e.clientX - shadowW / 2;
                                cy = e.clientY <= 0 ? 0 : e.clientY >= $(document).height() ? $(document).height() : e.clientY - shadowH / 2;
                                $('.md-plus').append(shadowObj);
                                cx = cx - $('.md-plus').offset().left + 10;
                                cy=cy-100;
                                shadowObj.css({left:cx, top:cy});
                                $('.md-doc-item', '.md-doc-item-box-s').removeClass('current');
                                var currentIn = WEBUTILS.status.currentIn(e);
                                if (currentIn == 'applyDiv') {
                                    var applyDivInObj = WEBUTILS.status.applyDivInObj();
                                    if (applyDivInObj) {
                                        if($(applyDivInObj).index()!=$(oldobj).index()){
                                            $('.md-doc-item', applyDivInObj).addClass('current');
                                        }
                                    }
                                }
                            }
                            WEBUTILS.dragdrop.appMove('true');
                        });
                        $(lay).on("mouseup", function (e) {
                            $(this).off("mousemove").off("mouseup");
                            shadowObj.remove();
                            if (WEBUTILS.dragdrop.appMove() == 'true') {
                                $.doTimeout('ddmove', 50, function () {
                                    var currentIn = WEBUTILS.status.currentIn(e);
                                    if (currentIn == 'applyDiv') {
                                        var applyDivInObj = WEBUTILS.status.applyDivInObj();
                                        if (applyDivInObj) {
                                            var oldIndex=$(oldobj).index();
                                            var targetIndex=$(applyDivInObj).index();
                                            var size=$('.md-doc-item-box-s','.md-lise-box').size();
                                            if(targetIndex!=oldIndex){
                                                $('.md-doc-item', applyDivInObj).removeClass('current');
                                                var moveOldObj= $(oldobj).clone(true);
                                                var moveTargetObj= $(applyDivInObj).clone(true);
                                                var moveOldUID=$(oldobj).attr('uid');
                                                var moveTargetUID=$(applyDivInObj).attr('uid');
                                                if((targetIndex+1)==size){
                                                    $('.md-doc-item-box-s','.md-lise-box').last().before(moveOldObj);
                                                }else{
                                                    $('.md-doc-item-box-s','.md-lise-box').eq(targetIndex+1).before(moveOldObj);
                                                }
                                                if((oldIndex+1)==size){
                                                    $('.md-doc-item-box-s','.md-lise-box').last().before(moveTargetObj);
                                                }else{
                                                    $('.md-doc-item-box-s','.md-lise-box').eq(oldIndex+1).before(moveTargetObj);
                                                }
                                                oldobj.remove();
                                                applyDivInObj.remove();
                                                if(moveOldUID!=moveTargetUID){
                                                    callback(moveOldUID,moveTargetUID);
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                            lay.hide();
                            if ($.browser.msie) {
                                lay[0].releaseCapture();
                            }
                        });
                        if ($.browser.msie) {
                            lay[0].setCapture();
                        }
                    }
                });
            }
        },
        myFlowNode_ddmove:function (controlObj,callback) {
            if (controlObj) {
                var nodeSeq=$(controlObj).attr('nodeSeq');
                if(nodeSeq=='9999'||nodeSeq=='0000'){
                    return false;
                }
                $(controlObj).off('mousedown').on('mousedown', function (e) {
                    e.preventDefault();
                    e.stopPropagation();
                    if (e.which == 1) {
                        WEBUTILS.dragdrop.appMove('false');
                        var oldobj = $(this), cx, cy, shadowObj,
                            lay = WEBUTILS.layout.block(),
                            shadowObj = $(String.formatmodel(ddmoveShadow, {
                                "object":String.formatmodel(ddmoveMyFlowNode, {
                                    'approveName':$('em',oldobj).text()
                                })})),
                            shadowW = $(oldobj).width(),
                            shadowH = $(oldobj).height(),
                            xx = e.clientX,
                            yy = e.clientY;

                        $(lay).on("mousemove", function (e) {
                            lay.show();
                            if (xx != e.clientX || yy != e.clientY) {
                                cx = e.clientX <= 0 ? 0 : e.clientX >= $(document).width() ? $(document).width() : e.clientX - shadowW / 2;
                                cy = e.clientY <= 0 ? 0 : e.clientY >= $(document).height() ? $(document).height() : e.clientY - shadowH / 2;
                                $('.md-pop-main').append(shadowObj);
                                cx = cx - $('.md-pop-main').offset().left + 10;
                                shadowObj.css({left:cx, top:cy});
                            }
                            WEBUTILS.dragdrop.appMove('true');
                        });
                        $(lay).on("mouseup", function (e) {
                            $(this).off("mousemove").off("mouseup");
                            shadowObj.remove();
                            if (WEBUTILS.dragdrop.appMove() == 'true') {
                                $.doTimeout('ddmove', 50, function () {
                                    var currentIn = WEBUTILS.status.currentIn(e);
                                    if (currentIn == 'myFlowNode') {
                                        var myFlowNodeInObj = WEBUTILS.status.myFlowNodeInObj();
                                        if (myFlowNodeInObj) {
                                            var nodeSeq=$(myFlowNodeInObj).attr('nodeSeq');
                                            if(nodeSeq){
                                                if(nodeSeq=='9999'||nodeSeq=='0000'){
                                                    return false;
                                                }
                                            }
                                            var moveOldObj= $(oldobj).clone(false);
                                            var moveTargetObj= $(myFlowNodeInObj).clone(false);

                                            $(myFlowNodeInObj).attr('nodeType',$(moveOldObj).attr('nodeType'));
                                            $(myFlowNodeInObj).attr('approveType',$(moveOldObj).attr('approveType'));
                                            $(myFlowNodeInObj).attr('approveId',$(moveOldObj).attr('approveId'));
                                            $('em',myFlowNodeInObj).text($('em',moveOldObj).text());

                                            $(oldobj).attr('nodeType',$(moveTargetObj).attr('nodeType'));
                                            $(oldobj).attr('approveType',$(moveTargetObj).attr('approveType'));
                                            $(oldobj).attr('approveId',$(moveTargetObj).attr('approveId'));
                                            $('em',oldobj).text($('em',moveTargetObj).text());

                                            $(moveOldObj).remove();
                                            $(moveTargetObj).remove();
                                        }
                                    }
                                });
                            }
                            lay.hide();
                            if ($.browser.msie) {
                                lay[0].releaseCapture();
                            }
                        });
                        if ($.browser.msie) {
                            lay[0].setCapture();
                        }
                    }
                });
            }
        }
    }
})();