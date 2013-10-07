WEBUTILS.popWindow = (function () {
    return{
        closePopWindow:function () {
            $('.modal-header','#myModal').find('.close').trigger('click');
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
            $('#myModal').on('hidden', function () {
                $('.treeDiv').fadeOut();
                $('#treeDemo','.treeDiv').empty();
                $('.modal-body','#myModal').empty();
                $('#myModalOkBtn','#myModal').unbind();
            });
        }
    }
})();