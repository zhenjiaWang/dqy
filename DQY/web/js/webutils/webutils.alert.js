WEBUTILS.alert = (function () {
    return {
        alertInfo:function (text,desc, time) {
            if (!time) {
                time = 1500;
            }
            $('body').append(String.formatmodel(alertInfoDQY,{'text':text,'desc':desc}));
            $('#myAlertInfo').alert();
            $('#myAlertInfo').show();
            $.doTimeout('myAlertInfoClose', time, function () {
                $('#myAlertInfo').alert('close');
                $('#myAlertInfo').remove();
            });
        },
        alertSuccess:function (text,desc, time) {
            if (!time) {
                time = 1500;
            }
            $('body').append(String.formatmodel(alertSuccessDQY,{'text':text,'desc':desc}));
            $('#myAlertSuccess').alert();
            $('#myAlertSuccess').show();
            $.doTimeout('myAlertSuccessClose', time, function () {
                $('#myAlertSuccess').alert('close');
                $('#myAlertSuccess').remove();
            });
        },
        alertError:function (text,desc, time) {
            if (!time) {
                time = 1500;
            }
            $('body').append(String.formatmodel(alertErrorDQY,{'text':text,'desc':desc}));
            $('#myAlertError').alert();
            $('#myAlertError').show();
            $.doTimeout('myAlertErrorClose', time, function () {
                $('#myAlertError').alert('close');
                $('#myAlertError').remove();
            });
        },
        alertWarning:function (text,desc, time) {
            if (!time) {
                time = 1500;
            }
            $('body').append(String.formatmodel(alertWarningDQY,{'text':text,'desc':desc}));
            $('#myAlertWarning').alert();
            $('#myAlertWarning').show();
            $.doTimeout('myAlertWarningClose', time, function () {
                $('#myAlertWarning').alert('close');
                $('#myAlertWarning').remove();
            });
        },
        alertComfirm:function (text,desc,yesCallback) {
            $('body').append(String.formatmodel(alertConfirmDQY,{'text':text,'desc':desc}));
            $('#yesBtn','#myAlertConfirm').off('click').on('click', function () {
                yesCallback();
            });
            $('#noBtn','#myAlertConfirm').off('click').on('click', function () {
                $('#myAlertConfirm').alert('close');
                $('#myAlertConfirm').remove();
            });
            $('#myAlertConfirm').alert();
            $('#myAlertConfirm').show();
        },
        close:function () {
            $('#myAlertInfo').alert('close');
            $('#myAlertInfo').remove();

            $('#myAlertSuccess').alert('close');
            $('#myAlertSuccess').remove();

            $('#myAlertError').alert('close');
            $('#myAlertError').remove();

            $('#myAlertWarning').alert('close');
            $('#myAlertWarning').remove();

            $('#myAlertConfirm').alert('close');
            $('#myAlertConfirm').remove();
        }
    }
})();