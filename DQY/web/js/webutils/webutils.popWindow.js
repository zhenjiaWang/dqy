WEBUTILS.popWindow = (function () {
    return{
        closePopWindow:function () {
            $('.md-plus-pop', 'body').hide(500);
            $('.md-plus-pop', 'body').remove();
        },
        createPopWindow:function (width, height, title, url,scroll) {
            var mt,mr,mb,ml,margin,scrolling='no';
            mt=parseInt(height/2);
            mr=0;
            mb=0;
            ml=parseInt(width/2);
            margin='-'+mt+'px '+mr+' '+mb+' -'+ml+'px';
            if(scroll){
                scrolling='yes';
            }
            var popObj = String.formatmodel(popMdPlus, {'width':width, 'height':height, 'title':title, 'url':url,'margin':margin,'scrolling':scrolling});
            $('.md-plus-pop', 'body').remove();
            $('body').append(popObj);
            $('.md-plus-pop', 'body').find('.md-delect').off('click').on('click', function (e) {
                e.preventDefault();
                e.stopPropagation();
                $('.md-plus-pop', 'body').hide(500);
                $('.md-plus-pop', 'body').remove();
            });
            $('.md-plus-pop', 'body').fadeIn(500);
        },
        createPopMobileWindow:function (width, height, title,url) {
            var mt,mr,mb,ml,margin;
            mt=parseInt(height/2);
            mr=0;
            mb=0;
            ml=parseInt(width/2);
            var popObj = String.formatmodel(mobileModal, {'width':width, 'height':height, 'title':title,'url':url});
            $('body').append(popObj);
            $('#myModal','body').modal('show')
        },
        closePopMobileWindow:function () {
            $('#myModal','body').modal('hide')
            $('#myModal', 'body').remove();
        },
        offset:function (top,left) {
            if(top){
                $('.md-plus-pop', 'body').css({top:top});
            }
            if(left){
                $('.md-plus-pop', 'body').css({left:left});
            }
        }
    }
})();