var classcodes = [];
window.Import = {LoadFileList:function (_files, succes, flag) {
    var FileArray = [];
    if (typeof _files === "object") {
        FileArray = _files;
    } else {
        if (typeof _files === "string") {
            FileArray = _files.split(",");
        }
    }
    if (FileArray != null && FileArray.length > 0) {
        var LoadedCount = 0;
        for (var i = 0; i < FileArray.length; i++) {
            loadFile(FileArray[i], function () {
                LoadedCount++;
                if (flag) {
                    if (LoadedCount == FileArray.length) {
                        succes(FileArray.length);
                    }
                } else {
                    succes(LoadedCount);
                }
            })
        }
    }
    function loadFile(url, success) {
        if (!FileIsExt(classcodes, url)) {
            var ThisType = GetFileTypeIndexOf(url);
            var fileObj = null;
            if (ThisType == ".js") {
                fileObj = document.createElement('script');
                fileObj.src = url;
            } else if (ThisType == ".css") {
                fileObj = document.createElement('link');
                fileObj.href = url;
                fileObj.type = "text/css";
                fileObj.rel = "stylesheet";
            } else if (ThisType == ".less") {
                fileObj = document.createElement('link');
                fileObj.href = url;
                fileObj.type = "text/css";
                fileObj.rel = "stylesheet/less";
            } else {
                fileObj = document.createElement('script');
                fileObj.src = url;
            }
            success = success || function () {
            };
            fileObj.onload = fileObj.onreadystatechange = function () {
                if (!this.readyState || 'loaded' == this.readyState || 'complete' == this.readyState) {
                    success();
                    classcodes.push(url)
                }
            }
            document.getElementsByTagName('head')[0].appendChild(fileObj);
        } else {
            success();
        }
    }

    function GetFileType(url) {
        if (url != null && url.length > 0) {
            return url.substr(url.lastIndexOf(".")).toLowerCase();
        }
        return"";
    }

    function GetFileTypeIndexOf(url) {
        if (url != null && url.length > 0) {
            if (url.indexOf('css') != -1) {
                return'.css';
            } else if (url.indexOf('js') != -1) {
                return'.js'
            }
        }
        return"";
    }

    function FileIsExt(FileArray, _url) {
        if (FileArray != null && FileArray.length > 0) {
            var len = FileArray.length;
            for (var i = 0; i < len; i++) {
                if (FileArray[i] == _url) {
                    return true;
                }
            }
        }
        return false;
    }
}}