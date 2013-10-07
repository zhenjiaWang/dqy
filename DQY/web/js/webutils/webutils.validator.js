WEBUTILS.validator = (function () {
    var _private;
    var _options = {};
    var validationList;
    var validation_failed_class = 'validation-failed';
    var validation_required_class = 'validation-required';
    var removeMode = function (mode) {
        _private.validations.remove(mode.id);
        _private.errorIds.remove(mode.id);
        var elem = $('#' + mode.id);
        var srcTag = elem.tagName;
        var stype = elem.type;

        if (stype == "radio") {
            elem = $('input:radio[name=\'' + mode.id + '\']')
        }
        var tempModeIds = new Array();

        for (var s = 0; s < _private.modeIds.length; s++) {
            if (mode.id == _private.modeIds[s]) {
                destory(elem);
            } else {
                tempModeIds.push(_private.modeIds[s]);
            }
        }
        _private.modeIds.length = 0;
        _private.modeIds = tempModeIds;
    };
    var destory = function (elem) {
        elem.removeClass(validation_required_class);
        elem.removeClass('validation-success');
        elem.removeClass(validation_failed_class);
        elem.removeClass(validation_failed_class);
        elem.removeClass('this-error');
        elem.off('blur');
        elem.off('keypress');
        elem.off('mouseover');//mouseover
    };
    var buildExp = function (condition, value) {
        var conditions = condition.ToCharArray();
        var exp = '';
        var history = '';
        $.each(conditions, function (i, o) {
            if (o.trim() == '>' || o.trim() == '<') {
                exp += value + o;
            } else if (o.trim() == '=' && o.trim() != history.trim()) {
                exp += value + o;
            } else if (o.trim() == '=' && o.trim() == history.trim()) {
                exp += o;
            } else {
                exp += o;
            }
            history = o;
        });
        return exp;
    };
    var validateMeans = function (elem, v, mode) {
        var result = false;
        var msg = '';
        if (!mode.required) {
            if (v.trim() == '') {
                return true;
            }
        }
        if (mode.pattern) {
            $.each(mode.pattern, function (i, pattern) {
                result = false;
                msg = pattern.msg;
                switch (pattern.type.trim()) {
                    case 'blank':
                        if (pattern.exp.trim() == '!=') {
                            result = v.trim() != '';
                        } else if (pattern.exp.trim() == '==') {
                            result = v.trim() == '';
                        }
                        refresh(result, elem, mode, msg);
                        break;
                    case 'length':
                        var exp = buildExp(pattern.exp, v.length);
                        result = eval(exp);
                        refresh(result, elem, mode, msg);
                        break;
                    case 'number':
                        if (pattern.exp.trim() == '!=') {
                            result = v.IsNumeric() == false;
                        } else if (pattern.exp.trim() == '==') {
                            result = v.IsNumeric()
                        } else {

                            if (v.IsNumeric()) {
                                var exp = buildExp(pattern.exp, v.trim());
                                result = eval(exp);
                            }
                        }
                        refresh(result, elem, mode, msg);
                        break;
                    case 'int':
                        if (pattern.exp.trim() == '!=') {
                            result = v.IsInt() == false;
                        } else if (pattern.exp.trim() == '==') {
                            result = v.IsInt()
                        } else {
                            if (v.IsInt()) {
                                var exp = buildExp(pattern.exp, v.trim());
                                if(exp.indexOf('$')!=-1){
                                    var si=exp.indexOf('=');
                                    var ei=exp.lastIndexOf('$');
                                    if(si<ei){
                                        var bs=exp.substring(si+1,ei);
                                        if(bs){
                                            v=parseInt(v);
                                            bs=parseInt(bs);
                                            result=v%bs==0;
                                        }
                                    }
                                }else{
                                    result = eval(exp);
                                }

                            }
                        }
                        refresh(result, elem, mode, msg);
                        break;
                    case 'eq':
                        result = v.trim() == pattern.exp;
                        refresh(result, elem, mode, msg);
                        break;
                    case '!eq':
                        result = v.trim() != pattern.exp;
                        refresh(result, elem, mode, msg);
                        break;
                    case 'gt':
                        result = v.trim() > pattern.exp;
                        refresh(result, elem, mode, msg);
                        break;
                    case 'ge':
                        result = v.trim() >= pattern.exp;
                        refresh(result, elem, mode, msg);
                        break;
                    case 'lt':
                        result = v.trim() < pattern.exp;
                        refresh(result, elem, mode, msg);
                        break;
                    case 'le':
                        result = v.trim() <= pattern.exp;
                        refresh(result, elem, mode, msg);
                        break;
                    case 'reg':
                        result = eval('regObj.' + pattern.exp + '.test(\'' + v.trim() + '\')');
                        refresh(result, elem, mode, msg);
                        break;
                    case 'radio':
                        elem = $('input:radio[name=\'' + mode.id + '\']')
                        elem.each(function (i, o) {
                            if ($(this).attr('checked')) {
                                result = true;
                                return false;
                            } else {
                                result = false;
                            }
                        });
                        refresh(result, elem, mode, msg);
                        break;
                    case 'ajax':
                        var URL = '';
                        v = v.trim();
                        if (v > '') {
                            if (typeof(pattern.exp) == 'function') {
                                URL = pattern.exp();
                            } else {
                                URL = pattern.exp;
                            }
                            var p = URL.indexOf("?");
                            var key = mode.id.replace('\\.', '.');
                            if (p != -1) {
                                URL += "&" + key + "=" + v;
                            } else if (p == -1) {
                                URL += "?" + key + "=" + v;
                            }


                            URL = encodeURI(URL);
                            $.ajax({
                                type:'GET',
                                url:URL,
                                dataType:'json',
                                success:function (jsonData) {
                                    if (jsonData) {
                                        if (jsonData.result) {
                                            result = true;
                                        }
                                        refresh(result, elem, mode, msg);
                                    }
                                },
                                error:function (jsonData) {
                                    result = false;
                                    refresh(result, elem, mode, msg);
                                }
                            });
                        }
                        break;
                }
                if (!result) {
                    return false;
                }
            });
        }
        return result;
    };
    var validate = function (mode) {
        var elem = false;
        var v = false;
        var result = false;
        var multi = false;
        if (mode.id.indexOf('|') != -1 || mode.id.indexOf('&') != -1) {
            if (mode.id.indexOf('|') != -1) {
                var ids = mode.id.split('|');
                $.each(ids, function (i, o) {
                    v = $('#' + o).val().toString();
                    elem = $("#" + o).get(0);
                    result = validateMeans(elem, v, mode);
                    if (result) {
                        return false;
                    }
                });
                $.each(ids, function (i, o) {
                    elem = $("#" + o).get(0);
                    refresh(result, elem, mode);
                });

            } else if (mode.id.indexOf('&') != -1) {
                var ids = mode.id.split('&');
                $.each(ids, function (i, o) {
                    v = $('#' + o).val().toString();
                    elem = $("#" + o).get(0);
                    result = validateMeans(elem, v, mode);
                    if (!result) {
                        return false;
                    }
                });
                $.each(ids, function (i, o) {
                    elem = $("#" + o).get(0);
                    refresh(result, elem, mode);
                });
            }
        } else {
            v = $('#' + mode.id).val().toString();
            elem = $("#" + mode.id).get(0);
            result = validateMeans(elem, v, mode);
        }
    };
    var passed = function (value) {
        if (_private.modeIds) {
            $.each(_private.modeIds, function (i, modeId) {
                var modePassed = _private.errorIds.get(modeId);
                if (!modePassed) {
                    _private.passed = false;
                    return false;
                } else {
                    _private.passed = true;
                }
            });
        }
        return _private.passed;
    };
    var refresh = function (result, elem, mode, msg) {
        _private.errorIds.put(mode.id, result);
        if (!result) {
            var controlGroup=$(elem).parents('.control-group');
            if(controlGroup){
                controlGroup.remove('warning');
                controlGroup.remove('error');
                controlGroup.remove('info');
                controlGroup.remove('success');
                controlGroup.addClass('error');
                $('.help-inline',controlGroup).text(msg).fadeIn();
            }
        } else {
            var controlGroup=$(elem).parents('.control-group');
            if(controlGroup){
                controlGroup.remove('warning');
                controlGroup.remove('error');
                controlGroup.remove('info');
                controlGroup.remove('success');
                controlGroup.addClass('success');
            }
        }
    };

    var checkAll = function () {
        $.each(_private.modeIds, function (i, modeId) {
            var validationModeArr = _private.validations.get(modeId);
            $.each(validationModeArr, function (ii, mode) {
                validate(mode);
            });
        });
    };
    var setUp = function () {
        $.each(_private.modeIds, function (i, modeId) {
            var validationModeArr = _private.validations.get(modeId);
            $.each(validationModeArr, function (ii, mode) {
                var elem = false;
                if (mode.id.indexOf('|') != -1 || mode.id.indexOf('&') != -1) {
                    if (mode.id.indexOf('|') != -1) {
                        var ids = mode.id.split('|');
                        $.each(ids, function (i, o) {
                            elem = $("#" + o).get(0);
                            label(elem, mode);
                        });
                    } else if (mode.id.indexOf('&') != -1) {
                        var ids = mode.id.split('&');
                        $.each(ids, function (i, o) {
                            elem = $("#" + o).get(0);
                            label(elem, mode);
                        });
                    }
                } else {
                    elem = $("#" + mode.id).get(0);
                    label(elem, mode);
                }

            })
        });
        if ($('#validator-tooltip', 'body').get(0) == undefined) {
            $(document).bind('click', function () {
                $('#validator-tooltip').hide();
            });
        }
    };
    var label = function (elem, mode) {
        if (elem) {
            var srcTag = elem.tagName;
            var stype = elem.type;
            if (stype == "radio") {
                elem = $('input:radio[name=\'' + mode.id + '\']')
            }
            if (srcTag.trim() == 'INPUT' || srcTag.trim() == 'TEXTAREA' || srcTag.trim() == 'SELECT') {
                if (mode.required) {
                    $(elem).addClass(validation_required_class);
                }
                $(elem).bind('blur', function () {
                    validate(mode);
                });
                if (mode.pattern) {
                    $.each(mode.pattern, function (i, o) {
                        if (o.type.trim() == 'number') {
                            $(elem).css({imeMode:'disabled'});
                            $(elem).attr({oncopy:'return false', onpaste:'return false'});
                            $(elem).bind('keypress', function (e) {
                                e = (e) ? e : ((window.event) ? window.event : "")
                                var keyCode = e.keyCode ? e.keyCode : (e.which ? e.which : e.charCode);
                                if ((keyCode >= 48 && keyCode <= 57) || (keyCode == 46 || keyCode == 45)||keyCode==8) {
                                    e.returnValue = true;
                                } else {
                                    e.returnValue = false;
                                    return false;
                                }
                            });
                        } else if (o.type.trim() == 'int') {
                            $(elem).css({imeMode:'disabled'});
                            $(elem).attr({oncopy:'return false', onpaste:'return false'});
                            $(elem).bind('keypress', function (e) {
                                e = (e) ? e : ((window.event) ? window.event : "")
                                var keyCode = e.keyCode ? e.keyCode : (e.which ? e.which : e.charCode);
                                if ((keyCode >= 48 && keyCode <= 57)||keyCode==8) {
                                    e.returnValue = true;
                                } else {
                                    e.returnValue = false;
                                    return false;
                                }
                            });
                        }
                    });
                }
            }
        }
    };
    var addMode = function (mode) {
        var validationList = null;
        if (_private.validations.get(mode.id) == undefined) {
            validationList = new Array();
            validationList.push(mode);
            _private.modeIds.push(mode.id);
        } else {
            validationList = _private.validations.get(mode.id)
            validationList.push(mode);
        }
        _private.validations.put(mode.id, validationList);
        _private.errorIds.put(mode.id, true);

        var validationModeArr = _private.validations.get(mode.id);
        $.each(validationModeArr, function (ii, modeArr) {
            var elem = false;
            if (modeArr.id.indexOf('|') != -1 || modeArr.id.indexOf('&') != -1) {
                if (modeArr.id.indexOf('|') != -1) {
                    var ids = modeArr.id.split('|');
                    $.each(ids, function (i, o) {
                        elem = $("#" + o).get(0);
                        label(elem, modeArr);
                    });
                } else if (modeArr.id.indexOf('&') != -1) {
                    var ids = modeArr.id.split('&');
                    $.each(ids, function (i, o) {
                        elem = $("#" + o).get(0);
                        label(elem, modeArr);
                    });
                }
            } else {
                elem = $("#" + modeArr.id).get(0);
                label(elem, modeArr);
            }
        })
    };
    var showErrors = function () {

    };
    return{
        init:function (options, diyClass) {
            if (diyClass) {
                validation_failed_class = 'validation-failed-c';
                validation_required_class = 'validation-required-c';
                $('.help-inline','#editForm').hide();
            }
            _options = $.extend(_options, options);
            _private = {
                validations:new HashMap(),
                modeIds:new Array(),
                errorIds:new HashMap(),
                passed:true
            };
            if (_options.modes) {
                $.each(_options.modes, function (i, mode) {
                    validationList = null;
                    if (_private.validations.get(mode.id) == undefined) {
                        validationList = new Array();
                        validationList.push(mode);
                        _private.modeIds.push(mode.id);
                    } else {
                        validationList = _private.validations.get(mode.id)
                        validationList.push(mode);
                    }
                    _private.validations.put(mode.id, validationList);
                    _private.errorIds.put(mode.id, true);
                });
                setUp();
            }
        },
        checkAll:function () {
            checkAll();
        },
        isPassed:function () {
            return passed();
        },
        addMode:function (mode) {
            addMode(mode);
        },
        removeMode:function (mode) {
            removeMode(mode);
        },
        showErrors:function () {
            showErrors();
        }
    }
})()