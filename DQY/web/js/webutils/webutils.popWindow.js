WEBUTILS.popWindow = (function () {
    var _okCallback = false;
    return{
        closePopWindow:function () {
            $('#myModal').modal('hide');
        },
        createPopWindow:function (width, height, title, url) {
            if(width){
                $('#myModal').width(width);
            }
            if(height){
                $('#myModal').height(height);
            }
            if(title){
                $('.pop-title','#myModal').text(title);
            }
            $('#myModal').modal({remote:url});
            $('#myModal').modal('show');
            $('#myModalOkBtn','#myModal').off('click').on('click',function(){
                _okCallback();
            });
        },
        okCallback:function (okFun) {
            _okCallback=okFun;
        }
    }
})();