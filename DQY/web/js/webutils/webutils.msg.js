WEBUTILS.msg = (function () {
    return {
        alertInfo:function (text, time) {
            if (!time) {
                time = 1500;
            }
            $('body').append(String.formatmodel(alertSuccess,{'text':text}));
            $('#alertSuccessDialogMD').fadeIn(500);
            $.doTimeout('alertSuccessDialogMDClose', time, function () {
                $('#alertSuccessDialogMD').hide();
                $('#alertSuccessDialogMD').remove();
            });
        },
        alertSuccess:function (text, time) {
            if (!time) {
                time = 1500;
            }
            $('body').append(String.formatmodel(alertSuccess,{'text':text}));
            $('#alertSuccessDialogMD').fadeIn(500);
            $('#alertSuccessDialogMD').css({'marginLeft':'-'+$('#alertSuccessDialogMD').width()/2+'px'});
            $.doTimeout('alertSuccessDialogMDClose', time, function () {
                $('#alertSuccessDialogMD').hide();
                $('#alertSuccessDialogMD').remove();
            });
        },
        alertFail:function (text, time) {
            if (!time) {
                time = 1500;
            }
            $('body').append(String.formatmodel(alertError,{'text':text}));
            $('#alertErrorDialogMD').fadeIn(500);
            $('#alertErrorDialogMD').css({'marginLeft':'-'+$('#alertErrorDialogMD').width()/2+'px'});
            $.doTimeout('alertErrorDialogMDClose', time, function () {
                $('#alertErrorDialogMD').hide();
                $('#alertErrorDialogMD').remove();
            });
        },
        alertLoading:function (text, time) {
            if (!time) {
                time = 50000;
            }
            $('body').append(String.formatmodel(alertSuccess,{'text':text}));
            $('#alertSuccessDialogMD').fadeIn(500);
            $('#alertSuccessDialogMD').css({'marginLeft':'-'+$('#alertSuccessDialogMD').width()/2+'px'});
            $.doTimeout('alertSuccessDialogMDClose', time, function () {
                $('#alertSuccessDialogMD').hide();
                $('#alertSuccessDialogMD').remove();
            });
        },
        close:function () {
            $('#alertSuccessDialogMD').hide();
            $('#alertSuccessDialogMD').remove();

            $('#alertErrorDialogMD').hide();
            $('#alertErrorDialogMD').remove();
        }
    }
})();