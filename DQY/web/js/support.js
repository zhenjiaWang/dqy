function StringBuilder(value)
{
    this.strings = new Array("");
    this.append(value);
}
StringBuilder.prototype.append = function (value)
{
    if (value)
    {
        this.strings.push(value);
    }
}
StringBuilder.prototype.clear = function ()
{
    this.strings.length = 1;
}
StringBuilder.prototype.toString = function ()
{
    return this.strings.join("");
}
function HashMap()
{
    /** Map ��С **/
    var size = 0;
    /** ���� **/
    var entry = [];

    /** �� **/
    this.put = function (key, value)
    {
        if (!this.containsKey(key))
        {
            size ++;
        }
        entry[key] = value;
    }

    /** ȡ **/
    this.get = function (key)
    {
        return this.containsKey(key) ? entry[key] : null;
    }

    /** ɾ�� **/
    this.remove = function (key)
    {
        if (this.containsKey(key) && ( delete entry[key] ))
        {
            size --;
        }
    }

    /** �Ƿ�� Key **/
    this.containsKey = function (key)
    {
        return (key in entry);
    }

    /** �Ƿ�� Value **/
    this.containsValue = function (value)
    {
        for (var prop in entry)
        {
            if (entry[prop] == value)
            {
                return true;
            }
        }
        return false;
    }

    /** ���� Value **/
    this.values = function ()
    {
        var values = new Array();
        for (var prop in entry)
        {
            values.push(entry[prop]);
        }
        return values;
    }

    /** ���� Key **/
    this.keys = function ()
    {
        var keys = new Array();
        for (var prop in entry)
        {
            keys.push(prop);
        }
        return keys;
    }

    /** Map Size **/
    this.size = function ()
    {
        return size;
    }

    /* ��� */
    this.clear = function ()
    {
        size = 0;
        entry = new Object();
    }
}

/*
 * 方法:Array.removeAt(Index)
 * 功能:删除数组元素.
 * 参数:Index删除元素的下标.
 * 返回:在原数组上修改数组
 */

Array.prototype.removeAt=function(Index)
{
    if(isNaN(Index)||Index>this.length){return false;}
    for(var i=0,n=0;i<this.length;i++)
    {
        if(this[i]!=this[Index])
        {
            this[n++]=this[i]
        }
    }
    this.length-=1
}

/*
 * 方法:Array.remove(obj)
 * 功能:删除数组元素.
 * 参数:要删除的对象.
 * 返回:在原数组上修改数组
 */

Array.prototype.remove=function(obj)
{
    if(null==obj){return;}
    for(var i=0,n=0;i<this.length;i++)
    {
        if(this[i]!=obj)
        {
            this[n++]=this[i];
        }
    }
    this.length-=1
}

/*
 * 方法:Array.Contains(obj)
 * 功能:确定某个元素是否在数组中.
 * 参数:要查找的Object对象
 * 返回:找到返回true,否则返回false;
 */
Array.prototype.Contains=function(obj)
{
    if(null==obj){return;}
    for(var i=0,n=0;i<this.length;i++)
    {
        if(this[i]!=obj)
        {
            return true;
        }
    }

    return false;
}


/*
 * 方法:Array.IndexOf(obj)
 * 功能:搜索指定的Object,并返回第一个匹配项从零开始的索引
 * 参数:要查找的Object对象
 * 返回:找到返回该元素在数组中的索引,否则返回-1
 */
Array.prototype.IndexOf=function(obj)
{
    if(null==obj){return;}
    {
        for(var i=0,n=0;i<this.length;i++)
        {
            if(this[i]==obj)
            {
                return i;
            }
        }
    }

    return -1;
}

/*
 * 方法:Array.Clear()
 * 功能:消空数组元素.
 * 参数:无.
 * 返回:空数组
 */
Array.prototype.Clear=function()
{
    this.length=0;
}

String.formatmodel = function(str,model){
    for(var k in model){
        var re = new RegExp("{"+k+"}","g");
        str = str.replace(re,model[k]);
    }
    return str;
};

if($.browser.msie&&($.browser.version=="6.0")&&!$.support.style){
    var DD_belatedPNG={ns:"DD_belatedPNG",imgSize:{},delay:10,nodesFixed:0,createVmlNameSpace:function(){if(document.namespaces&&!document.namespaces[this.ns]){document.namespaces.add(this.ns,"urn:schemas-microsoft-com:vml")}},createVmlStyleSheet:function(){var b,a;b=document.createElement("style");b.setAttribute("media","screen");document.documentElement.firstChild.insertBefore(b,document.documentElement.firstChild.firstChild);if(b.styleSheet){b=b.styleSheet;b.addRule(this.ns+"\\:*","{behavior:url(#default#VML)}");b.addRule(this.ns+"\\:shape","position:absolute;");b.addRule("img."+this.ns+"_sizeFinder","behavior:none; border:none; position:absolute; z-index:-1; top:-10000px; visibility:hidden;");this.screenStyleSheet=b;a=document.createElement("style");a.setAttribute("media","print");document.documentElement.firstChild.insertBefore(a,document.documentElement.firstChild.firstChild);a=a.styleSheet;a.addRule(this.ns+"\\:*","{display: none !important;}");a.addRule("img."+this.ns+"_sizeFinder","{display: none !important;}")}},readPropertyChange:function(){var b,c,a;b=event.srcElement;if(!b.vmlInitiated){return}if(event.propertyName.search("background")!=-1||event.propertyName.search("border")!=-1){DD_belatedPNG.applyVML(b)}if(event.propertyName=="style.display"){c=(b.currentStyle.display=="none")?"none":"block";for(a in b.vml){if(b.vml.hasOwnProperty(a)){b.vml[a].shape.style.display=c}}}if(event.propertyName.search("filter")!=-1){DD_belatedPNG.vmlOpacity(b)}},vmlOpacity:function(b){if(b.currentStyle.filter.search("lpha")!=-1){var a=b.currentStyle.filter;a=parseInt(a.substring(a.lastIndexOf("=")+1,a.lastIndexOf(")")),10)/100;b.vml.color.shape.style.filter=b.currentStyle.filter;b.vml.image.fill.opacity=a}},handlePseudoHover:function(a){setTimeout(function(){DD_belatedPNG.applyVML(a)},1)},fix:function(a){if(this.screenStyleSheet){var c,b;c=a.split(",");for(b=0;b<c.length;b++){this.screenStyleSheet.addRule(c[b],"behavior:expression(DD_belatedPNG.fixPng(this))")}}},applyVML:function(a){a.runtimeStyle.cssText="";this.vmlFill(a);this.vmlOffsets(a);this.vmlOpacity(a);if(a.isImg){this.copyImageBorders(a)}},attachHandlers:function(i){var d,c,g,e,b,f;d=this;c={resize:"vmlOffsets",move:"vmlOffsets"};if(i.nodeName=="A"){e={mouseleave:"handlePseudoHover",mouseenter:"handlePseudoHover",focus:"handlePseudoHover",blur:"handlePseudoHover"};for(b in e){if(e.hasOwnProperty(b)){c[b]=e[b]}}}for(f in c){if(c.hasOwnProperty(f)){g=function(){d[c[f]](i)};i.attachEvent("on"+f,g)}}i.attachEvent("onpropertychange",this.readPropertyChange)},giveLayout:function(a){a.style.zoom=1;if(a.currentStyle.position=="static"){a.style.position="relative"}},copyImageBorders:function(b){var c,a;c={borderStyle:true,borderWidth:true,borderColor:true};for(a in c){if(c.hasOwnProperty(a)){b.vml.color.shape.style[a]=b.currentStyle[a]}}},vmlFill:function(e){if(!e.currentStyle){return}else{var d,f,g,b,a,c;d=e.currentStyle}for(b in e.vml){if(e.vml.hasOwnProperty(b)){e.vml[b].shape.style.zIndex=d.zIndex}}e.runtimeStyle.backgroundColor="";e.runtimeStyle.backgroundImage="";f=true;if(d.backgroundImage!="none"||e.isImg){if(!e.isImg){e.vmlBg=d.backgroundImage;e.vmlBg=e.vmlBg.substr(5,e.vmlBg.lastIndexOf('")')-5)}else{e.vmlBg=e.src}g=this;if(!g.imgSize[e.vmlBg]){a=document.createElement("img");g.imgSize[e.vmlBg]=a;a.className=g.ns+"_sizeFinder";a.runtimeStyle.cssText="behavior:none; position:absolute; left:-10000px; top:-10000px; border:none; margin:0; padding:0;";c=function(){this.width=this.offsetWidth;this.height=this.offsetHeight;g.vmlOffsets(e)};a.attachEvent("onload",c);a.src=e.vmlBg;a.removeAttribute("width");a.removeAttribute("height");document.body.insertBefore(a,document.body.firstChild)}e.vml.image.fill.src=e.vmlBg;f=false}e.vml.image.fill.on=!f;e.vml.image.fill.color="none";e.vml.color.shape.style.backgroundColor=d.backgroundColor;e.runtimeStyle.backgroundImage="none";e.runtimeStyle.backgroundColor="transparent"},vmlOffsets:function(d){var h,n,a,e,g,m,f,l,j,i,k;h=d.currentStyle;n={W:d.clientWidth+1,H:d.clientHeight+1,w:this.imgSize[d.vmlBg].width,h:this.imgSize[d.vmlBg].height,L:d.offsetLeft,T:d.offsetTop,bLW:d.clientLeft,bTW:d.clientTop};a=(n.L+n.bLW==1)?1:0;e=function(b,p,q,c,s,u){b.coordsize=c+","+s;b.coordorigin=u+","+u;b.path="m0,0l"+c+",0l"+c+","+s+"l0,"+s+" xe";b.style.width=c+"px";b.style.height=s+"px";b.style.left=p+"px";b.style.top=q+"px"};e(d.vml.color.shape,(n.L+(d.isImg?0:n.bLW)),(n.T+(d.isImg?0:n.bTW)),(n.W-1),(n.H-1),0);e(d.vml.image.shape,(n.L+n.bLW),(n.T+n.bTW),(n.W),(n.H),1);g={X:0,Y:0};if(d.isImg){g.X=parseInt(h.paddingLeft,10)+1;g.Y=parseInt(h.paddingTop,10)+1}else{for(j in g){if(g.hasOwnProperty(j)){this.figurePercentage(g,n,j,h["backgroundPosition"+j])}}}d.vml.image.fill.position=(g.X/n.W)+","+(g.Y/n.H);m=h.backgroundRepeat;f={T:1,R:n.W+a,B:n.H,L:1+a};l={X:{b1:"L",b2:"R",d:"W"},Y:{b1:"T",b2:"B",d:"H"}};if(m!="repeat"||d.isImg){i={T:(g.Y),R:(g.X+n.w),B:(g.Y+n.h),L:(g.X)};if(m.search("repeat-")!=-1){k=m.split("repeat-")[1].toUpperCase();i[l[k].b1]=1;i[l[k].b2]=n[l[k].d]}if(i.B>n.H){i.B=n.H}d.vml.image.shape.style.clip="rect("+i.T+"px "+(i.R+a)+"px "+i.B+"px "+(i.L+a)+"px)"}else{d.vml.image.shape.style.clip="rect("+f.T+"px "+f.R+"px "+f.B+"px "+f.L+"px)"}},figurePercentage:function(d,c,f,a){var b,e;e=true;b=(f=="X");switch(a){case"left":case"top":d[f]=0;break;case"center":d[f]=0.5;break;case"right":case"bottom":d[f]=1;break;default:if(a.search("%")!=-1){d[f]=parseInt(a,10)/100}else{e=false}}d[f]=Math.ceil(e?((c[b?"W":"H"]*d[f])-(c[b?"w":"h"]*d[f])):parseInt(a,10));if(d[f]%2===0){d[f]++}return d[f]},fixPng:function(c){c.style.behavior="none";var g,b,f,a,d;if(c.nodeName=="BODY"||c.nodeName=="TD"||c.nodeName=="TR"){return}c.isImg=false;if(c.nodeName=="IMG"){if(c.src.toLowerCase().search(/\.png$/)!=-1){c.isImg=true;c.style.visibility="hidden"}else{return}}else{if(c.currentStyle.backgroundImage.toLowerCase().search(".png")==-1){return}}g=DD_belatedPNG;c.vml={color:{},image:{}};b={shape:{},fill:{}};for(a in c.vml){if(c.vml.hasOwnProperty(a)){for(d in b){if(b.hasOwnProperty(d)){f=g.ns+":"+d;c.vml[a][d]=document.createElement(f)}}c.vml[a].shape.stroked=false;c.vml[a].shape.appendChild(c.vml[a].fill);c.parentNode.insertBefore(c.vml[a].shape,c)}}c.vml.image.shape.fillcolor="none";c.vml.image.fill.type="tile";c.vml.color.fill.on=false;g.attachHandlers(c);g.giveLayout(c);g.giveLayout(c.offsetParent);c.vmlInitiated=true;g.applyVML(c)}};try{document.execCommand("BackgroundImageCache",false,true)}catch(r){}DD_belatedPNG.createVmlNameSpace();DD_belatedPNG.createVmlStyleSheet();
}

jQuery.cookie = function(name, value, options) {
    if (typeof value != 'undefined') {
        options = options || {};
        if (value === null) {
            value = '';
            options.expires = -1;
        }
        var expires = '';
        if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
            var date;
            if (typeof options.expires == 'number') {
                date = new Date();
                date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
            } else {
                date = options.expires;
            }
            expires = '; expires=' + date.toUTCString();
        }
        var path = options.path ? '; path=' + (options.path) : '';
        var domain = options.domain ? '; domain=' + (options.domain) : '';
        var secure = options.secure ? '; secure' : '';
        document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
    } else {
        var cookieValue = null;
        if (document.cookie && document.cookie != '') {
            var cookies = document.cookie.split(';');
            for (var i = 0; i < cookies.length; i++) {
                var cookie = jQuery.trim(cookies[i]);
                if (cookie.substring(0, name.length + 1) == (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    }
};

/*
 置顶插件scrolltotop
 scrolltotop.controlHTML='<a href="#top" id="scrolltotop">返回顶部</a>';
 scrolltotop.init();
 */
scrolltotop={
    setting: {startline:100, scrollto: 0, scrollduration:500, fadeduration:[500, 100]},
    controlHTML: '<a href="#top" id="scrolltotop"></a>',
    controlattrs: {offsetx:5, offsety:5},
    anchorkeyword: '#top',
    state: {isvisible:false, shouldvisible:false},
    scrollup:function(){
        if (!this.cssfixedsupport) {
            if(this.$control!=undefined) this.$control.css({opacity:0})
        };
        var dest=isNaN(this.setting.scrollto)? this.setting.scrollto : parseInt(this.setting.scrollto);
        if (typeof dest=="string" && jQuery('#'+dest).length==1) {
            dest=jQuery('#'+dest).offset().top;
        } else {
            dest=this.setting.scrollto;
        };
        if(this.$body!=undefined) this.$body.animate({scrollTop: dest}, this.setting.scrollduration);
    },
    keepfixed:function(){
        var $window=jQuery(window);
        var controlx=$window.scrollLeft() + $window.width() - this.$control.width() - this.controlattrs.offsetx;
        var controly=$window.scrollTop() + $window.height() - this.$control.height() - this.controlattrs.offsety;
        this.$control.css({left:controlx+'px', top:controly+'px'});
    },
    togglecontrol:function(){
        var scrolltop=jQuery(window).scrollTop();
        if (!this.cssfixedsupport) {
            this.keepfixed();
        };
        this.state.shouldvisible=(scrolltop>=this.setting.startline)? true : false;
        if (this.state.shouldvisible && !this.state.isvisible){
            this.$control.stop().animate({opacity:1}, this.setting.fadeduration[0]);
            this.state.isvisible=true;
        }
        else if (this.state.shouldvisible==false && this.state.isvisible){
            this.$control.stop().animate({opacity:0}, this.setting.fadeduration[1]);
            this.state.isvisible=false;
        }
    },
    init:function(){
        jQuery(document).ready(function($){
            if($("body").attr('scrolltotop')!='no'){
                scrolltotop.cssfixedsupport=!document.all || document.all && document.compatMode=="CSS1Compat" && window.XMLHttpRequest;
                scrolltotop.$body=(window.opera)? (document.compatMode=="CSS1Compat"? $('html') : $('body')) : $('html,body');
                scrolltotop.$control=$('<div id="topcontrol">'+scrolltotop.controlHTML+'</div>')
                    .css({position:scrolltotop.cssfixedsupport? 'fixed' : 'absolute', bottom:scrolltotop.controlattrs.offsety, right:scrolltotop.controlattrs.offsetx, opacity:0, cursor:'pointer'})
                    .click(function(){scrolltotop.scrollup(); return false;})
                    .appendTo('body');
                if (document.all && !window.XMLHttpRequest && scrolltotop.$control.text()!='') {
                    scrolltotop.$control.css({width:scrolltotop.$control.width()});
                };
                scrolltotop.togglecontrol();
                $('a[href="' + scrolltotop.anchorkeyword +'"]').click(function(){
                    scrolltotop.scrollup();
                    return false;
                });
                $(window).bind('scroll resize', function(e){
                    scrolltotop.togglecontrol();
                });
            }
        });
    }
};

/*
 doTimeOut
 demo：http://www.css88.com/demo/dotimeout
 */
(function($){var a={},c="doTimeout",d=Array.prototype.slice;$[c]=function(){return b.apply(window,[0].concat(d.call(arguments)))};$.fn[c]=function(){var f=d.call(arguments),e=b.apply(this,[c+f[0]].concat(f));return typeof f[0]==="number"||typeof f[1]==="number"?this:e};function b(l){var m=this,h,k={},g=l?$.fn:$,n=arguments,i=4,f=n[1],j=n[2],p=n[3];if(typeof f!=="string"){i--;f=l=0;j=n[1];p=n[2]}if(l){h=m.eq(0);h.data(l,k=h.data(l)||{})}else{if(f){k=a[f]||(a[f]={})}}k.id&&clearTimeout(k.id);delete k.id;function e(){if(l){h.removeData(l)}else{if(f){delete a[f]}}}function o(){k.id=setTimeout(function(){k.fn()},j)}if(p){k.fn=function(q){if(typeof p==="string"){p=g[p]}p.apply(m,d.call(n,i))===true&&!q?o():e()};o()}else{if(k.fn){j===undefined?e():k.fn(j===false);return true}else{e()}}}})(jQuery);

/*
 加载遮罩框插件loadmask
 基于jquery-loadmask-0.3
 $("body").mask("数据加载中...");
 $("body").unmask();
 */
$.fn.mask = function(label,pos,showmask){
    showmask=(showmask=='') ? true : showmask;
//alert(this.css("display"));
    this.find(".loadmask-msg,.loadmask").remove();
    if(this.css("position") == "static") {
        this.addClass("masked-relative");
    }
    if(showmask){
        this.addClass("masked");
        var maskDiv = $('<div class="loadmask"></div>');
        if(navigator.userAgent.toLowerCase().indexOf("msie") > -1){
            maskDiv.height(this.height() + parseInt(this.css("padding-top")) + parseInt(this.css("padding-bottom")));
            maskDiv.width(this.width() + parseInt(this.css("padding-left")) + parseInt(this.css("padding-right")));
        }
        if(navigator.userAgent.toLowerCase().indexOf("msie 6") > -1){
            this.find("select").addClass("masked-hidden");
        }
        this.append(maskDiv);
    }
    if(typeof label == "string") {
        var maskMsgDiv = $('<div class="loadmask-msg" style="display:none;"></div>');
        maskMsgDiv.append('<div>' + label + '</div>');
        this.append(maskMsgDiv);
        switch(pos){
            case '':
                maskMsgDiv.css("top",Math.round(this.height() / 2 - (maskMsgDiv.height() - parseInt(maskMsgDiv.css("padding-top")) - parseInt(maskMsgDiv.css("padding-bottom"))) / 2)+"px");
                break;
            case 'T':
                maskMsgDiv.css("top",$(document).scrollTop()+10+"px");
                break;
            case 'C':
                maskMsgDiv.css("top",$(document).scrollTop()+Math.round(this.height() / 2 - (maskMsgDiv.height() - parseInt(maskMsgDiv.css("padding-top")) - parseInt(maskMsgDiv.css("padding-bottom"))) / 2)+"px");
                break;
            default:
                maskMsgDiv.css("top",$(document).scrollTop()+pos+"px");
                break;
        }
        maskMsgDiv.css("left", Math.round(this.width() / 2 - (maskMsgDiv.width() - parseInt(maskMsgDiv.css("padding-left")) - parseInt(maskMsgDiv.css("padding-right"))) / 2)+"px");
        maskMsgDiv.show();
    }
};
$.fn.unmask = function(){
    this.find(".loadmask-msg,.loadmask").remove();
    this.removeClass("masked");
    this.removeClass("masked-relative");
    this.find("select").removeClass("masked-hidden");
};


var WEBUTILS = {

};
var TEMP = {};
var base64_keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

String.prototype.ToCharArray = function() {
    return this.split("");
}
// 倒序
String.prototype.Reverse = function() {
    return this.split("").reverse().join("");
}
// 是否包含指定字符
String.prototype.IsContains = function(str) {
    return (this.indexOf(str) > -1);
}
// 判断是否为空
String.prototype.IsEmpty = function() {
    return this == "";
}
// 判断是否是数字
String.prototype.IsNumeric = function() {
    var tmpFloat = parseFloat(this);
    if (isNaN(tmpFloat))
        return false;
    return true;
}
// 判断是否是整数
String.prototype.IsInt = function() {
    if (this == "NaN")
        return false;
    return this == parseInt(this).toString();
}
// 合并多个空白为一个空白
String.prototype.resetBlank = function() {
    return this.replace(/s+/g, "");
}
// 除去左边空白
String.prototype.LTrim = function() {
    return this.replace(/(^\s*)/g, "");
}
// 除去右边空白
String.prototype.RTrim = function() {
    return this.replace(/(\s*$)/g, "");
}
// 除去两边空白
String.prototype.trim = function() {
    return this.replace(/(^\s*)|(\s*$)/g, "");
}
// 保留数字
String.prototype.getNum = function() {
    return this.replace(/[^d]/g, "");
}
// 保留字母
String.prototype.getEn = function() {
    return this.replace(/[^A-Za-z]/g, "");
}
// 保留中文
String.prototype.getCn = function() {
    return this.replace(/[^u4e00-u9fa5uf900-ufa2d]/g, "");
}
// 获取字节长度
String.prototype.ByteLength = function() {
    return this.replace(/[^\x00-\xff]/g, "aa").length;
}
// 从左截取指定长度的字串
String.prototype.left = function(n) {
    return this.slice(0, n);
}
// 从右截取指定长度的字串
String.prototype.right = function(n) {
    return this.slice(this.length - n);
}
// HTML编码
String.prototype.HTMLEncode = function() {
    var re = this;
    var q1 = [/x26/g, /x3C/g, /x3E/g, /x20/g];
    var q2 = ["&", "<", ">", " "];
    for (var i = 0; i < q1.length; i++)
        re = re.replace(q1[i], q2[i]);
    return re;
}
// 获取Unicode
String.prototype.Unicode = function() {
    var tmpArr = [];
    for (var i = 0; i < this.length; i++)
        tmpArr.push("&#" + this.charCodeAt(i) + ";");
    return tmpArr.join("");
}
// 指定位置插入字符串
String.prototype.Insert = function(index, str) {
    return this.substring(0, index) + str + this.substr(index);
}
/**
 * 判断字符串是否以指定的字符串开始
 */
String.prototype.startsWith = function(str) {
    return this.substr(0, str.length) == str;
}
/**
 * 判断字符串是否以指定的字符串开始，忽略大小写
 */
String.prototype.iStartsWith = function(str) {
    return this.substr(0, str.length).iEquals(str);
}
/**
 * 判断字符串是否以指定的字符串结束
 */
String.prototype.endsWith = function(str) {
    return this.substr(this.length - str.length) == str;
}
/**
 * 判断字符串是否以指定的字符串结束，忽略大小写
 */
String.prototype.iEndsWith = function(str) {
    return this.substr(this.length - str.length).iEquals(str);
}
/**
 * 忽略大小写比较字符串 注：不忽略大小写比较用 == 号
 */
String.prototype.iEquals = function(str) {
    return this.toLowerCase() == str.toLowerCase();
}
/**
 * 比较字符串，根据结果返回 -1, 0, 1
 */
String.prototype.compareTo = function(str) {
    if (this == str) {
        return 0;
    } else if (this < str) {
        return -1;
    } else {
        return 1;
    }
}
/**
 * 忽略大小写比较字符串，根据结果返回 -1, 0, 1
 */
String.prototype.iCompareTo = function(str) {
    return this.toLowerCase().compareTo(str.toLowerCase());
}

String.prototype.encode64 = function() {
    var output = "";
    var chr1, chr2, chr3 = "";
    var enc1, enc2, enc3, enc4 = "";
    var i = 0;
    var input = this.toString();
    do {
        chr1 = input.charCodeAt(i++);
        chr2 = input.charCodeAt(i++);
        chr3 = input.charCodeAt(i++);

        enc1 = chr1 >> 2;
        enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
        enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
        enc4 = chr3 & 63;

        if (isNaN(chr2)) {
            enc3 = enc4 = 64;
        } else if (isNaN(chr3)) {
            enc4 = 64;
        }

        output = output + base64_keyStr.charAt(enc1)
            + base64_keyStr.charAt(enc2) + base64_keyStr.charAt(enc3)
            + base64_keyStr.charAt(enc4);
        chr1 = chr2 = chr3 = "";
        enc1 = enc2 = enc3 = enc4 = "";
    } while (i < input.length);

    return output;
}
String.prototype.StringFormat = function() {
    if (arguments.length == 0)
        return null;
    var str = arguments[0];
    for (var i = 1; i < arguments.length; i++) {
        var re = new RegExp('\\{' + (i - 1) + '\\}');
        str = str.replace(re, arguments[i]);
    }
    return str;
}
String.prototype.decode64 = function(input) {
    var output = "";
    var chr1, chr2, chr3 = "";
    var enc1, enc2, enc3, enc4 = "";
    var i = 0;

    if (input.length % 4 != 0) {
        return "";
    }
    var base64test = /[^A-Za-z0-9\+\/\=]/g;
    if (base64test.exec(input)) {
        return "";
    }

    do {
        enc1 = base64_keyStr.indexOf(input.charAt(i++));
        enc2 = base64_keyStr.indexOf(input.charAt(i++));
        enc3 = base64_keyStr.indexOf(input.charAt(i++));
        enc4 = base64_keyStr.indexOf(input.charAt(i++));

        chr1 = (enc1 << 2) | (enc2 >> 4);
        chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
        chr3 = ((enc3 & 3) << 6) | enc4;

        output = output + String.fromCharCode(chr1);

        if (enc3 != 64) {
            output += String.fromCharCode(chr2);
        }
        if (enc4 != 64) {
            output += String.fromCharCode(chr3);
        }

        chr1 = chr2 = chr3 = "";
        enc1 = enc2 = enc3 = enc4 = "";

    } while (i < input.length);

    return output;
}
function dateLe(startDate,endDate){
    if(startDate&&endDate){
        startDate=startDate.replace(/-/g,"/");
        endDate=endDate.replace(/-/g,"/");
        var dt1=new Date(Date.parse(startDate));
        var dt2=new Date(Date.parse(endDate));
        return (dt1<=dt2);
    }else{
        return false;
    }
}
function dateLt(startDate,endDate){
    if(startDate&&endDate){
        startDate=startDate.replace(/-/g,"/");
        endDate=endDate.replace(/-/g,"/");
        var dt1=new Date(Date.parse(startDate));
        var dt2=new Date(Date.parse(endDate));
        return (dt1<dt2);
    }else{
        return false;
    }
}
function dateGt(startDate,endDate){
    if(startDate&&endDate){
        startDate=startDate.replace(/-/g,"/");
        endDate=endDate.replace(/-/g,"/");
        var dt1=new Date(Date.parse(startDate));
        var dt2=new Date(Date.parse(endDate));
        return (dt1>dt2);
    }else{
        return false;
    }
}
function dateGe(startDate,endDate){
    if(startDate&&endDate){
        startDate=startDate.replace(/-/g,"/");
        endDate=endDate.replace(/-/g,"/");
        var dt1=new Date(Date.parse(startDate));
        var dt2=new Date(Date.parse(endDate));
        return (dt1>=dt2);
    }else{
        return false;
    }
}
/*
 圆角插件corner
 基于jquery.corner.js
 修正了按钮圆角的问题，调用了corner插件
 $('.qmenu').corner("5px");
 */
if(!document.createElement('canvas').getContext){(function(){var m=Math;var y=m.round;var z=m.sin;var A=m.cos;var Z=10;var B=Z/2;function getContext(){if(this.context_){return this.context_}return this.context_=new CanvasRenderingContext2D_(this)}var C=Array.prototype.slice;function bind(f,b,c){var a=C.call(arguments,2);return function(){return f.apply(b,a.concat(C.call(arguments)))}}var D={init:function(a){if(/MSIE/.test(navigator.userAgent)&&!window.opera){var b=a||document;b.createElement('canvas');b.attachEvent('onreadystatechange',bind(this.init_,this,b))}},init_:function(a){if(!a.namespaces['g_vml_']){a.namespaces.add('g_vml_','urn:schemas-microsoft-com:vml')}if(!a.styleSheets['ex_canvas_']){var b=a.createStyleSheet();b.owningElement.id='ex_canvas_';b.cssText='canvas{display:inline-block;overflow:hidden;'+'text-align:left;width:300px;height:150px}'+'g_vml_\\:*{behavior:url(#default#VML)}'}},i:function(a){if(!a.getContext){a.getContext=getContext;a.attachEvent('onpropertychange',onPropertyChange);a.attachEvent('onresize',onResize);var b=a.attributes;if(b.width&&b.width.specified){a.style.width=b.width.nodeValue+'px'}else{a.width=a.clientWidth}if(b.height&&b.height.specified){a.style.height=b.height.nodeValue+'px'}else{a.height=a.clientHeight}}return a}};function onPropertyChange(e){var a=e.srcElement;switch(e.propertyName){case'width':a.style.width=a.attributes.width.nodeValue+'px';a.getContext().clearRect();break;case'height':a.style.height=a.attributes.height.nodeValue+'px';a.getContext().clearRect();break}}function onResize(e){var a=e.srcElement;if(a.firstChild){a.firstChild.style.width=a.clientWidth+'px';a.firstChild.style.height=a.clientHeight+'px'}}D.init();var E=[];for(var i=0;i<16;i++){for(var j=0;j<16;j++){E[i*16+j]=i.toString(16)+j.toString(16)}}function createMatrixIdentity(){return[[1,0,0],[0,1,0],[0,0,1]]}function processStyle(a){var b,alpha=1;a=String(a);if(a.substring(0,3)=='rgb'){var c=a.indexOf('(',3);var d=a.indexOf(')',c+1);var e=a.substring(c+1,d).split(',');b='#';for(var i=0;i<3;i++){b+=E[Number(e[i])]}if(e.length==4&&a.substr(3,1)=='a'){alpha=e[3]}}else{b=a}return[b,alpha]}function processLineCap(a){switch(a){case'butt':return'flat';case'round':return'round';case'square':default:return'square'}}function CanvasRenderingContext2D_(a){this.m_=createMatrixIdentity();this.mStack_=[];this.aStack_=[];this.currentPath_=[];this.strokeStyle='#000';this.fillStyle='#000';this.lineWidth=1;this.lineJoin='miter';this.lineCap='butt';this.miterLimit=Z*1;this.globalAlpha=1;this.canvas=a;var b=a.ownerDocument.createElement('div');b.style.width=a.clientWidth+'px';b.style.height=a.clientHeight+'px';b.style.overflow='hidden';b.style.position='absolute';a.appendChild(b);this.element_=b;this.arcScaleX_=1;this.arcScaleY_=1}var F=CanvasRenderingContext2D_.prototype;F.clearRect=function(){this.element_.innerHTML='';this.currentPath_=[]};F.beginPath=function(){this.currentPath_=[]};F.moveTo=function(a,b){var p=this.getCoords_(a,b);this.currentPath_.push({type:'moveTo',x:p.x,y:p.y});this.currentX_=p.x;this.currentY_=p.y};F.lineTo=function(a,b){var p=this.getCoords_(a,b);this.currentPath_.push({type:'lineTo',x:p.x,y:p.y});this.currentX_=p.x;this.currentY_=p.y};F.bezierCurveTo=function(a,b,c,d,e,f){var p=this.getCoords_(e,f);var g=this.getCoords_(a,b);var h=this.getCoords_(c,d);this.currentPath_.push({type:'bezierCurveTo',cp1x:g.x,cp1y:g.y,cp2x:h.x,cp2y:h.y,x:p.x,y:p.y});this.currentX_=p.x;this.currentY_=p.y};F.fillRect=function(a,b,c,d){this.beginPath();this.moveTo(a,b);this.lineTo(a+c,b);this.lineTo(a+c,b+d);this.lineTo(a,b+d);this.closePath();this.fill();this.currentPath_=[]};F.createLinearGradient=function(a,b,c,d){return new CanvasGradient_('gradient')};F.createRadialGradient=function(a,b,c,d,e,f){var g=new CanvasGradient_('gradientradial');g.radius1_=c;g.radius2_=f;g.focus_.x=a;g.focus_.y=b;return g};F.stroke=function(d){var e=[];var f=false;var a=processStyle(d?this.fillStyle:this.strokeStyle);var g=a[0];var h=a[1]*this.globalAlpha;var W=10;var H=10;e.push('<g_vml_:shape',' fillcolor="',g,'"',' filled="',Boolean(d),'"',' style="position:absolute;width:',W,';height:',H,';"',' coordorigin="0 0" coordsize="',Z*W,' ',Z*H,'"',' stroked="',!d,'"',' strokeweight="',this.lineWidth,'"',' strokecolor="',g,'"',' path="');var j=false;var k={x:null,y:null};var l={x:null,y:null};for(var i=0;i<this.currentPath_.length;i++){var p=this.currentPath_[i];var c;switch(p.type){case'moveTo':e.push(' m ');c=p;e.push(y(p.x),',',y(p.y));break;case'lineTo':e.push(' l ');e.push(y(p.x),',',y(p.y));break;case'close':e.push(' x ');p=null;break;case'bezierCurveTo':e.push(' c ');e.push(y(p.cp1x),',',y(p.cp1y),',',y(p.cp2x),',',y(p.cp2y),',',y(p.x),',',y(p.y));break;case'at':case'wa':e.push(' ',p.type,' ');e.push(y(p.x-this.arcScaleX_*p.radius),',',y(p.y-this.arcScaleY_*p.radius),' ',y(p.x+this.arcScaleX_*p.radius),',',y(p.y+this.arcScaleY_*p.radius),' ',y(p.xStart),',',y(p.yStart),' ',y(p.xEnd),',',y(p.yEnd));break}if(p){if(k.x==null||p.x<k.x){k.x=p.x}if(l.x==null||p.x>l.x){l.x=p.x}if(k.y==null||p.y<k.y){k.y=p.y}if(l.y==null||p.y>l.y){l.y=p.y}}}e.push(' ">');if(typeof this.fillStyle=='object'){var m={x:'50%',y:'50%'};var n=l.x-k.x;var o=l.y-k.y;var q=n>o?n:o;m.x=y(this.fillStyle.focus_.x/n*100+50)+'%';m.y=y(this.fillStyle.focus_.y/o*100+50)+'%';var r=[];if(this.fillStyle.type_=='gradientradial'){var s=this.fillStyle.radius1_/q*100;var t=this.fillStyle.radius2_/q*100-s}else{var s=0;var t=100}var u={offset:null,color:null};var v={offset:null,color:null};this.fillStyle.colors_.sort(function(a,b){return a.offset-b.offset});for(var i=0;i<this.fillStyle.colors_.length;i++){var w=this.fillStyle.colors_[i];r.push(w.offset*t+s,'% ',w.color,',');if(w.offset>u.offset||u.offset==null){u.offset=w.offset;u.color=w.color}if(w.offset<v.offset||v.offset==null){v.offset=w.offset;v.color=w.color}}r.pop();e.push('<g_vml_:fill',' color="',v.color,'"',' color2="',u.color,'"',' type="',this.fillStyle.type_,'"',' focusposition="',m.x,', ',m.y,'"',' colors="',r.join(''),'"',' opacity="',h,'" />')}else if(d){e.push('<g_vml_:fill color="',g,'" opacity="',h,'" />')}else{var x=Math.max(this.arcScaleX_,this.arcScaleY_)*this.lineWidth;e.push('<g_vml_:stroke',' opacity="',h,'"',' joinstyle="',this.lineJoin,'"',' miterlimit="',this.miterLimit,'"',' endcap="',processLineCap(this.lineCap),'"',' weight="',x,'px"',' color="',g,'" />')}e.push('</g_vml_:shape>');this.element_.insertAdjacentHTML('beforeEnd',e.join(''))};F.fill=function(){this.stroke(true)};F.closePath=function(){this.currentPath_.push({type:'close'})};F.getCoords_=function(a,b){return{x:Z*(a*this.m_[0][0]+b*this.m_[1][0]+this.m_[2][0])-B,y:Z*(a*this.m_[0][1]+b*this.m_[1][1]+this.m_[2][1])-B}};function CanvasPattern_(){}G_vmlCMjrc=D})()}if(jQuery.browser.msie){document.execCommand("BackgroundImageCache",false,true)}(function($){var N=$.browser.msie;var O=N&&!window.XMLHttpRequest;var P=$.browser.opera;var Q=typeof document.createElement('canvas').getContext=="function";var R=function(i){return parseInt(i,10)||0};var S=function(a,b,c){var x=a,y;if(x.currentStyle){y=x.currentStyle[b]}else if(window.getComputedStyle){if(typeof arguments[2]=="string")b=c;y=document.defaultView.getComputedStyle(x,null).getPropertyValue(b)}return y};var T=function(a,p){return S(a,'border'+p+'Color','border-'+p.toLowerCase()+'-color')};var U=function(a,p){if(a.currentStyle&&!P){w=a.currentStyle['border'+p+'Width'];if(w=='thin')w=2;if(w=='medium'&&!(a.currentStyle['border'+p+'Style']=='none'))w=4;if(w=='thick')w=6}else{p=p.toLowerCase();w=document.defaultView.getComputedStyle(a,null).getPropertyValue('border-'+p+'-width')}return R(w)};var V=function(a,i){return a.tagName.toLowerCase()==i};var W=function(e,a,b,c,d){if(e=='tl')return a;if(e=='tr')return b;if(e=='bl')return c;if(e=='br')return d};var X=function(a,b,c,d,e,f,g){var h,curve_to;if(d.indexOf('rgba')!=-1){var i=/^rgba\((\d{1,3}),\s*(\d{1,3}),\s*(\d{1,3}),\s*(\d{1,3})\)$/;var j=i.exec(d);if(j){var k=[R(j[1]),R(j[2]),R(j[3])];d='rgb('+k[0]+', '+k[1]+', '+k[2]+')'}}var l=a.getContext('2d');if(b==1||g=='notch'){if(e>0&&b>1){l.fillStyle=f;l.fillRect(0,0,b,b);l.fillStyle=d;h=W(c,[0-e,0-e],[e,0-e],[0-e,e],[e,e]);l.fillRect(h[0],h[1],b,b)}else{l.fillStyle=d;l.fillRect(0,0,b,b)}return a}else if(g=='bevel'){h=W(c,[0,0,0,b,b,0,0,0],[0,0,b,b,b,0,0,0],[0,0,b,b,0,b,0,0],[b,b,b,0,0,b,b,b]);l.fillStyle=d;l.beginPath();l.moveTo(h[0],h[1]);l.lineTo(h[2],h[3]);l.lineTo(h[4],h[5]);l.lineTo(h[6],h[7]);l.fill();if(e>0&&e<b){l.strokeStyle=f;l.lineWidth=e;l.beginPath();h=W(c,[0,b,b,0],[0,0,b,b],[b,b,0,0],[0,b,b,0]);l.moveTo(h[0],h[1]);l.lineTo(h[2],h[3]);l.stroke()}return a}h=W(c,[0,0,b,0,b,0,0,b,0,0],[b,0,b,b,b,0,0,0,0,0],[0,b,b,b,0,b,0,0,0,b],[b,b,b,0,b,0,0,b,b,b]);l.fillStyle=d;l.beginPath();l.moveTo(h[0],h[1]);l.lineTo(h[2],h[3]);if(c=='br')l.bezierCurveTo(h[4],h[5],b,b,h[6],h[7]);else l.bezierCurveTo(h[4],h[5],0,0,h[6],h[7]);l.lineTo(h[8],h[9]);l.fill();if(e>0&&e<b){var m=e/2;var n=b-m;h=W(c,[n,m,n,m,m,n],[n,n,n,m,m,m],[n,n,m,n,m,m,m,n],[n,m,n,m,m,n,n,n]);curve_to=W(c,[0,0],[0,0],[0,0],[b,b]);l.strokeStyle=f;l.lineWidth=e;l.beginPath();l.moveTo(h[0],h[1]);l.bezierCurveTo(h[2],h[3],curve_to[0],curve_to[1],h[4],h[5]);l.stroke()}return a};var Y=function(p,a){var b=document.createElement('canvas');b.setAttribute("height",a);b.setAttribute("width",a);b.style.display="block";b.style.position="absolute";b.className="jrCorner";Z(p,b);if(!Q&&N){if(typeof G_vmlCanvasManager=="object"){b=G_vmlCanvasManager.initElement(b)}else if(typeof G_vmlCMjrc=="object"){b=G_vmlCMjrc.i(b)}else{throw Error('Could not find excanvas');}}return b};var Z=function(p,a){if(p.is("table")){p.children("tbody").children("tr:first").children("td:first").append(a);p.css('display','block')}else if(p.is("td")){if(p.children(".JrcTdContainer").length===0){p.html('<div class="JrcTdContainer" style="padding:0px;position:relative;margin:-1px;zoom:1;">'+p.html()+'</div>');p.css('zoom','1');if(O){p.children(".JrcTdContainer").get(0).style.setExpression("height","this.parentNode.offsetHeight")}}p.children(".JrcTdContainer").append(a)}else{p.append(a)}};if(N){var ba=document.createStyleSheet();ba.media='print';ba.cssText='.jrcIECanvasDiv { display:none !important; }'}var bb=function(D){if(this.length==0||!(Q||N)){return this}if(D=="destroy"){return this.each(function(){var p,elm=$(this);if(elm.is(".jrcRounded")){if(typeof elm.data("ie6tmr.jrc")=='number')window.clearInterval(elm.data("ie6tmr.jrc"));if(elm.is("table"))p=elm.children("tbody").children("tr:first").children("td:first");else if(elm.is("td"))p=elm.children(".JrcTdContainer");else p=elm;p.children(".jrCorner").remove();elm.unbind('mouseleave.jrc').unbind('mouseenter.jrc').removeClass('jrcRounded').removeData('ie6tmr.jrc');if(elm.is("td"))elm.html(elm.children(".JrcTdContainer").html())}})}var o=(D||"").toLowerCase();var E=R((o.match(/(\d+)px/)||[])[1])||"auto";var F=((o.match(/(#[0-9a-f]+)/)||[])[1])||"auto";var G=/round|bevel|notch/;var H=((o.match(G)||['round'])[0]);var I=/hover/.test(o);var J=/oversized/.test(o);var K=o.match("hiddenparent");if(N){var G=/ie6nofix|ie6fixinit|ie6fixexpr|ie6fixonload|ie6fixwidthint|ie6fixheightint|ie6fixbothint/;var L=((o.match(G)||['ie6fixinit'])[0])}var M={tl:/top|left|tl/.test(o),tr:/top|right|tr/.test(o),bl:/bottom|left|bl/.test(o),br:/bottom|right|br/.test(o)};if(!M.tl&&!M.tr&&!M.bl&&!M.br)M={tl:1,tr:1,bl:1,br:1};this.each(function(){var d=$(this),rbg=null,bg,s,b,pr;var a=this;var e=S(this,'display');var f=S(this,'position');var g=S(this,'lineHeight','line-height');if(F=="auto"){s=d.siblings(".jrcRounded:eq(0)");if(s.length>0){b=s.data("rbg.jrc");if(typeof b=="string"){rbg=b}}}if(K||rbg===null){var h=this.parentNode,hidden_parents=new Array(),a=0;while((typeof h=='object')&&!V(h,'html')){if(K&&S(h,'display')=='none'){hidden_parents.push({originalvisibility:S(h,'visibility'),elm:h});h.style.display='block';h.style.visibility='hidden'}var j=S(h,'backgroundColor','background-color');if(rbg===null&&j!="transparent"&&j!="rgba(0, 0, 0, 0)"){rbg=j}h=h.parentNode}if(rbg===null)rbg="#ffffff"}if(F=="auto"){bg=rbg;d.data("rbg.jrc",rbg)}else{bg=F}if(e=='none'){var k=S(this,'visibility');this.style.display='block';this.style.visibility='hidden';var l=true}else{var m=false}var n=d.height();var p=d.width();if(I){var q=o.replace(/hover|ie6nofix|ie6fixinit|ie6fixexpr|ie6fixonload|ie6fixwidthint|ie6fixheightint|ie6fixbothint/g,"");if(L!='ie6nofix')q="ie6fixinit "+q;d.bind("mouseenter.jrc",function(){d.addClass(d.attr("hoverclass"))});d.bind("mouseleave.jrc",function(){d.removeClass(d.attr("hoverclass"))})}if(O&&L!='ie6nofix'){this.style.zoom=1;if(L!='ie6fixexpr'){if(d.width()%2!=0)d.width(d.width()+1);if(d.height()%2!=0)d.height(d.height()+1)}$(window).load(function(){if(L=='ie6fixonload'){if(d.css('height')=='auto')d.height(d.css('height'));if(d.width()%2!=0)d.width(d.width()+1);if(d.height()%2!=0)d.height(d.height()+1)}else if(L=='ie6fixwidthint'||L=='ie6fixheightint'||L=='ie6fixbothint'){var c,ie6FixFunction;if(L=='ie6fixheightint'){ie6FixFunction=function(){d.height('auto');var a=d.height();if(a%2!=0)a=a+1;d.css({height:a})}}else if(L=='ie6fixwidthint'){ie6FixFunction=function(){d.width('auto');var a=d.width();if(a%2!=0)a=a+1;d.css({width:a});d.data('lastWidth.jrc',d.get(0).offsetWidth)}}else if(L=='ie6fixbothint'){ie6FixFunction=function(){d.width('auto');d.height('auto');var a=d.width();var b=d.height();if(b%2!=0)b=b+1;if(a%2!=0)a=a+1;d.css({width:a,height:b})}}c=window.setInterval(ie6FixFunction,100);d.data("ie6tmr.jrc",c)}})}var r=n<p?this.offsetHeight:this.offsetWidth;if(E=="auto"){E=r/2;if(E>10)E=r/4}if(E>r/2&&!J){E=r/2}E=Math.floor(E);var t=U(this,'Top');var u=U(this,'Right');var v=U(this,'Bottom');var w=U(this,'Left');if(f=='static'&&!V(this,'td')){this.style.position='relative'}else if(f=='fixed'&&N&&!(document.compatMode=='CSS1Compat'&&!O)){this.style.position='absolute'}if(t+u+v+w>0){this.style.overflow='visible'}if(l)d.css({display:'none',visibility:k});if(typeof hidden_parents!="undefined"){for(var i=0;i<hidden_parents.length;i++){hidden_parents[i].elm.style.display='none';hidden_parents[i].elm.style.visibility=hidden_parents[i].originalvisibility}}var x=0-t,p_right=0-u,p_bottom=0-v,p_left=0-w;var y=(d.find("canvas").length>0);if(y){if(V(this,'table'))pr=d.children("tbody").children("tr:first").children("td:first");else if(V(this,'td'))pr=d.children(".JrcTdContainer");else pr=d}if(M.tl){bordersWidth=t<w?t:w;if(y)pr.children("canvas.jrcTL").remove();var z=X(Y(d,E),E,'tl',bg,bordersWidth,T(this,'Top'),H);$(z).css({left:p_left,top:x}).addClass('jrcTL')}if(M.tr){bordersWidth=t<u?t:u;if(y)pr.children("canvas.jrcTR").remove();var A=X(Y(d,E),E,'tr',bg,bordersWidth,T(this,'Top'),H);$(A).css({right:p_right,top:x}).addClass('jrcTR')}if(M.bl){bordersWidth=v<w?v:w;if(y)pr.children("canvas.jrcBL").remove();var B=X(Y(d,E),E,'bl',bg,bordersWidth,T(this,'Bottom'),H);$(B).css({left:p_left,bottom:p_bottom}).addClass('jrcBL')}if(M.br){bordersWidth=v<u?v:u;if(y)pr.children("canvas.jrcBR").remove();var C=X(Y(d,E),E,'br',bg,bordersWidth,T(this,'Bottom'),H);$(C).css({right:p_right,bottom:p_bottom}).addClass('jrcBR')}if(N)d.children('canvas.jrCorner').children('div').addClass('jrcIECanvasDiv');if(O&&L=='ie6fixexpr'){if(M.bl){B.style.setExpression("bottom","this.parentNode.offsetHeight % 2 == 0 || this.parentNode.offsetWidth % 2 == 0 ? 0-(parseInt(this.parentNode.currentStyle['borderBottomWidth'])) : 0-(parseInt(this.parentNode.currentStyle['borderBottomWidth'])+1)")}if(M.br){C.style.setExpression("right","this.parentNode.offsetWidth % 2 == 0 || this.parentNode.offsetWidth % 2 == 0 ? 0-(parseInt(this.parentNode.currentStyle['borderRightWidth'])) : 0-(parseInt(this.parentNode.currentStyle['borderRightWidth'])+1)");C.style.setExpression("bottom","this.parentNode.offsetHeight % 2 == 0 || this.parentNode.offsetWidth % 2 == 0 ? 0-(parseInt(this.parentNode.currentStyle['borderBottomWidth'])) : 0-(parseInt(this.parentNode.currentStyle['borderBottomWidth'])+1)")}if(M.tr){A.style.setExpression("right","this.parentNode.offsetWidth % 2 == 0 || this.parentNode.offsetWidth % 2 == 0 ? 0-(parseInt(this.parentNode.currentStyle['borderRightWidth'])) : 0-(parseInt(this.parentNode.currentStyle['borderRightWidth'])+1)")}}d.addClass('jrcRounded')});if(typeof arguments[1]=="function")arguments[1](this);return this};$.fn.corner=bb})(jQuery);



function SetCookie(name, value) {
    var Days = 60;   //cookie 将被保存两个月
    var exp = new Date();  //获得当前时间
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);  //换成毫秒
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
}

function getCookie(name) {
    var arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
    if (arr != null)
        return unescape(arr[2]);
    return '';

}
function getLocationHref(){
    return document.location.href;
}