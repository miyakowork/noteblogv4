(function () {
    function Vipspa() {

    }

    Vipspa.prototype.start = function (config) {
        var self = this;
        self.routerMap = config.router;
        self.mainView = config.view;
        self.baseUrl = config.basePath;
        self.baseStatic = config.baseStatic;
        self.errorTemplateId = config.errorTemplateId;
        startRouter();
        window.onhashchange = function () {
            startRouter();
        };
    };
    var messageStack = [];
    // {
    //     'id': 'home_bindcard',
    //     'content': {
    //     }
    // }
    Vipspa.prototype.getMessage = function (id) {
        var msg = {};
        $.each(messageStack, function (i, e) {
            if (e.id === id) {
                msg = e;
            }
        });
        return msg;
    };
    Vipspa.prototype.setMessage = function (obj) {
        var _obj = JSON.parse(JSON.stringify(obj));
        $.each(messageStack, function (i, e) {
            if (e.id === _obj.id) {
                e = _obj;
                return false;
            }
        });
        messageStack.push(_obj);
    };
    Vipspa.prototype.delMessage = function (id) {
        if (typeof id === 'undefined') {
            return false;
        }
        var index = 0;
        $.each(messageStack, function (i, e) {
            if (e.id === id) {
                index = i;
            }
        });
        $.each(messageStack, function (i, e) {
            if (i > index) {
                messageStack[i - 1] = e;
            }
        });
    };
    Vipspa.prototype.clearMessage = function () {
        messageStack = [];
    };

    Vipspa.prototype.stringifyParam = function (routerUrl, paramObj) {
        var paramStr = '', hash;
        for (var i in  paramObj) {
            paramStr += i + '=' + encodeURIComponent(paramObj[i]) + '&';
        }
        if (paramStr === '') {
            hash = routerUrl;
        }
        else {
            paramStr = paramStr.substring(0, paramStr.length - 1);
            hash = routerUrl + '?' + paramStr;
        }
        return hash;
    };

    Vipspa.prototype.stringifyPattern = function (routerUrl, paramObj) {
        var paramStr = '', hash;
        for (var i = 0; i < paramObj.length; i++) {
            paramStr += paramObj[i] + '/';
        }
        if (paramStr === '') {
            hash = routerUrl;
        } else {
            paramStr = paramStr.substring(0, paramStr.length - 1);
            hash = routerUrl + '!!' + paramStr;
        }
        return hash;
    };

    Vipspa.prototype.stringifyDefault = function (routerUrl) {
        var hash = routerUrl;
        return hash + "?_=" + new Date().getTime();
    };

    Vipspa.prototype.stringify = function (routerUrl, paramObj) {
        var paramStr = '', hash;
        for (var i in  paramObj) {
            paramStr += i + '=' + encodeURIComponent(paramObj[i]) + '&';
        }
        paramStr += '_=' + new Date().getTime() + '&';
        if (paramStr === '') {
            hash = routerUrl;
        }
        else {
            paramStr = paramStr.substring(0, paramStr.length - 1);
            hash = routerUrl + '?' + paramStr;
        }
        return hash;
    };


    Vipspa.prototype.parse = function (routerHash) {
        var hash = typeof routerHash === 'undefined' ? location.hash : routerHash;
        var obj = {
            url: '',
            param: {}
        };
        var param = {}, url = '';
        var pIndex = hash.indexOf('?');
        if (hash === '') {
            return obj;
        }

        if (pIndex > -1) {
            url = hash.substring(1, pIndex);
            var paramStr = hash.substring(pIndex + 1);
            var paramArr = paramStr.split('&');

            $.each(paramArr, function (i, e) {
                var item = e.split('='),
                    key,
                    val;
                key = item[0];
                val = item[1];
                if (key !== '') {
                    param[key] = decodeURIComponent(val);
                }


            });
        }
        else {
            url = hash.substring(1);
            param = {};
        }
        return {
            url: url,
            param: param
        };
    };

    function routerAction(routeObj) {
        var key = routeObj.url.indexOf("?");
        var routerItem;
        if (key > -1) {
            var url = routeObj.url.substr(0, key);
            routerItem = vipspa.routerMap[url];
        } else {
            routerItem = vipspa.routerMap[routeObj.url];
        }
        if (typeof routerItem === 'undefined') {
            var defaultsRoute = vipspa.routerMap.defaults;
            routerItem = vipspa.routerMap[defaultsRoute];
            location.hash = defaultsRoute;
            return false;
        }
        var ajaxUrl = vipspa.baseUrl + routerItem.path;
        if (typeof routeObj.rest !== 'undefined' && routeObj.rest !== '') {
            ajaxUrl = vipspa.baseUrl + routerItem.path + '/' + routeObj.rest;
        }
        $.ajax({
            type: 'GET',
            url: ajaxUrl,
            data: routeObj.param,
            cache: false,
            dataType: 'html',
            success: function (data, status, xhr) {
                try {
                    var resp = eval('(' + data + ')');
                    if (resp.code && resp.code === -1) {
                        window.location.href = resp.data;
                    }
                } catch (e) {
                    var container = $(vipspa.mainView);
                    container.html(data);
                    //刷新layui的动态组件
                    BMY.refreshLayUIComponent();
                    if (routerItem.action !== undefined) {
                        loadScript(vipspa.baseStatic + routerItem.action);
                    }
                    // clear data var
                    data = null;
                    container = null;
                }
            },
            error: function (xhr, errorType, error) {
                if ($(vipspa.errorTemplateId).length === 0) {
                    return false;
                }
                var errHtml = $(vipspa.errorTemplateId).html();
                errHtml = errHtml.replace(/{{errStatus}}/, xhr.status);
                errHtml = errHtml.replace(/{{errContent}}/, xhr.responseText);
                $(vipspa.mainView).html(errHtml);
            }
        });
    }

    function startRouter() {
        var hash = location.hash;
        var routeObj = vipspa.parse(hash);
        routerAction(routeObj);
    }

    function loadScript(src, callback) {
        var script = document.createElement('script'),
            loaded;
        script.charset = "UTF-8";
        script.setAttribute('src', src);
        script.onreadystatechange = script.onload = function () {
            script.onreadystatechange = null;
            document.documentElement.removeChild(script);
            script = null;
            if (!loaded) {
                if (typeof callback === 'function')
                    callback();
            }
            loaded = true;
        };
        document.documentElement.appendChild(script);
    }

    window.vipspa = new Vipspa();
})();