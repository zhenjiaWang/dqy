WEBUTILS.layout = (function () {
    return{
        block:function () {
            if (!TEMP.blockLayout) {
                TEMP.blockLayout = $(String.formatmodel(blockLayout, null));
                $('body').append(TEMP.blockLayout);
            }
            return  ($.browser.msie) ? TEMP.blockLayout : $(window);
        }
    }
})();