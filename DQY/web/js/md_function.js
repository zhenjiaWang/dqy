
var MDobj = {
    paras: { access_token: "" },
    baseUrl: "https://api.mingdao.com/",

    //获取明道头部的html
    GetHeader: function (callback) {
        var ajaxUrl = MDobj.baseUrl + "md/header.aspx?jsoncallback=?";

        $.getJSON(ajaxUrl,
        { u_key: MDobj.paras.access_token },
        function (data) {
            callback(data);
        });

    },

    //获取明道左侧菜单栏html
    GetLeftMenu: function (callback) {
        var ajaxUrl = MDobj.baseUrl + "md/leftmenu.aspx?jsoncallback=?";
        $.getJSON(ajaxUrl,
        { u_key: MDobj.paras.access_token },
        function (data) {
            callback(data);
        });
    },

    //获取明道底部html
    GetFooter: function (callback) {
        var ajaxUrl = MDobj.baseUrl + "md/footer.aspx?jsoncallback=?";
        $.getJSON(ajaxUrl,
        function (data) {
            callback(data);
        });
    },

    //获取明道头部、左侧菜单、底部html
    GetAll: function (callback) {
        var ajaxUrl = MDobj.baseUrl + "md/all.aspx?jsoncallback=?";
        $.getJSON(ajaxUrl,
        { u_key: MDobj.paras.access_token },
        function (data) {
            callback(data);
        });
    }

}