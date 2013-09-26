WEBUTILS.searchTip = (function () {
    var tempSearchLiS;
    return {
        search:function (obj, width,leftDiff,topDiff,fn) {
            tempSearchLiS = $('ul', '.ser_pop').html();
            if(!leftDiff){
                leftDiff=0;
            }
            if(!topDiff){
                topDiff=0;
            }
            if(!width){
                width=200;
            }
            $(obj).off('keyup').on('keyup', function (e) {
                e = (e) ? e : ((window.event) ? window.event : "")
                var keyCode = e.keyCode ? e.keyCode : (e.which ? e.which : e.charCode);
                var searchKey = $('#searchKey').val().trim();
                $('.ser_pop').css('width',width);
                if (keyCode != 38 && keyCode != 40 && keyCode != 13) {
                    //回车 up down;
                    if (searchKey == '') {
                        //存取原来的筛选条件
                        $('ul', '.ser_pop').empty().append(searchAllTemp);
                        $('a', '.ser_pop').first().addClass('current');
                    } else {
                        $('ul', '.ser_pop').empty().append(tempSearchLiS);
                        $('a', '.ser_pop').removeClass('current');
                        $('a', '.ser_pop').first().addClass('current');
                    }
                    var left = $(this).parent().offset().left;
                    var top = $(this).parent().offset().top;
                    top += $(this).parent().height();
                    $('.ser_pop').css({left:left-leftDiff, top:top-topDiff}).show();
                    $('em', '.ser_pop').text(searchKey);
                    $('a', '.ser_pop').off('click').on('click', function (e) {
                        var searchType = $(this).attr('type');
                        if (searchType == 'all') {
                            searchKey = '';
                        }
                        if(fn&&typeof(fn)=='function'){
                            fn(searchType, searchKey);
                        }
                        $('.ser_pop').hide();
                    });
                } else {
                    var searchCount = $('li', '.ser_pop').size();
                    if (keyCode == 38) {
                        //up
                        var li = $('.current', '.ser_pop').parent();
                        var currentIndex = $(li).index();
                        $('a', '.ser_pop').removeClass('current');
                        if (currentIndex == 0) {
                            $('a', '.ser_pop').last().addClass('current');
                        } else {
                            $(li).prev().find('a').addClass('current');
                        }
                    } else if (keyCode == 40) {
                        //down
                        var li = $('.current', '.ser_pop').parent();
                        var currentIndex = $(li).index();
                        $('a', '.ser_pop').removeClass('current');
                        if (currentIndex == searchCount - 1) {
                            $('a', '.ser_pop').first().addClass('current');
                        } else {
                            $(li).next().find('a').addClass('current');
                        }
                    } else if (keyCode == 13) {
                        var searchType = $('.current', '.ser_pop').attr('type');
                        if (searchType == 'all') {
                            searchKey = '';
                        }
                        if(fn&&typeof(fn)=='function'){
                            fn(searchType, searchKey);
                        }
                        $('.ser_pop').hide();
                    }
                }
            });
        }
    }
})();