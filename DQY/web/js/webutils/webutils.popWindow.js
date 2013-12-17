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
                //$('#myModalFrame','#myModal').attr('height',(height-120));
                //$('#myModal').height(height);
            }else{
                height=437;
            }
            if(title){
                $('.pop-title','#myModal').text(title);
            }
            if(url){
                $('#myModalFrame','#myModal').attr('src',url);
            }
            var mt,mr,mb,ml,margin,
            mt=0;
            mr=0;
            mb=0;
            ml=parseInt(width/2);
            margin='-'+mt+'px '+mr+' '+mb+' -'+ml+'px';
            $('#myModal').modal('show');
            $('#myModal').css({'margin':margin});
            $('#myModal').on('hidden', function () {
                $('.treeDiv').fadeOut();
                $('#treeDemo','.treeDiv').empty();
                $('#myModalFrame','#myModal').attr('src','/view/common/blank.html');
                $('#myModalOkBtn','#myModal').unbind();
            });
            $('#myModalOkBtn','#myModal').off('click').on('click',function(){
                $('#myModalFrame','#myModal').get(0).contentWindow.dataSubmit();
            });
        }
    }
})();