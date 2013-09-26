WEBUTILS.popMask = (function () {
    return{
        show:function(text){
            if(!text||text=='undefined'||text==null){
                text='努力加载中，请稍后..';
            }
            var maskObj=String.formatmodel(maskDiv,{'text':text});
            var maskSize=$('.mask','body').size();
            if(maskSize==0){
                $('body').append(maskObj);
            }else{
                $('.mask','body').find('span').text(text);
            }
        },
        showMobile:function(text){
            if(!text||text=='undefined'||text==null){
                text='努力加载中，请稍后..';
            }
            var maskObj=String.formatmodel(maskMobileDiv,{'text':text});
            var maskSize=$('.mask','body').size();
            if(maskSize==0){
                $('body').append(maskObj);
            }else{
                $('.mask','body').find('span').text(text);
            }
        },
        close:function(){
            $('.mask','body').remove();
        }
    }
})();