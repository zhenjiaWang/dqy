WEBUTILS.dialog = (function () {
    var _layout='bottomRight';
    function notyInfo(type, text,timeout){
        noty({layout : _layout, theme : 'noty_theme_default', text: text, type: type,timeout:timeout});
    }
    function notyConfirm(type, text,okFn,cancelFn,timeout){
        noty({layout : _layout, theme : 'noty_theme_default', type : type, text: text,
            buttons: [
                {type: 'btn btn-primary', text: 'Ok', click: function($noty) {
                    okFn($noty);
                }
                },
                {type: 'btn btn-danger', text: 'Cancel', click: function($noty) {
                    cancelFn($noty);
                }
                }
            ], closable: false, timeout: false
        });
    }
    function notyConfirmAlert(type, text,viewFn,sendFn,timeout){
        noty({layout : _layout, theme : 'noty_theme_default', text: text, type: type,
            buttons: [
            {type: 'btn btn-primary', text: '查看信息', click: function($noty) {
                viewFn($noty);
            }
            },
            {type: 'btn btn-danger', text: '继续发送', click: function($noty) {
                sendFn($noty);
            }
            }
            ], closable: false, timeout: false
        });
    }
    return {
        alert:function (text, timeout) {
            notyInfo('alert',text,timeout);
        },
        error:function (text, timeout) {
            notyInfo('error',text,timeout);
        },
        success:function (text, timeout) {
            notyInfo('success',text,timeout);
        },
        information:function (text, timeout) {
            notyInfo('notification',text,timeout);
        },
        confirm:function (text,okFn,cancelFn) {
            notyConfirm('notification',text,okFn,cancelFn);
        },
        confirmAlert:function (text,viewFn,sendFn) {
            notyConfirmAlert('notification',text,viewFn,sendFn);
        },
        layout:function (layout) {
            if (layout) {
                _layout = layout;
            }
            return _layout;
        }
    }
})();