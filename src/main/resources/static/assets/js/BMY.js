var BMY = {
    $: layui.$
    , layui: {}
    , status: {
        ok: 200
    }
    //此处标识formSelect v4的标签值为标签名字
    , valIsName: true

    , url: {
        prefix: "/management"
        , manage_index: "/management/index"
        , login: "/login"
    }

    , ajax: function (url, data, success) {
        layui.$.ajax({
            type: 'post'
            , dataType: 'json'
            , url: url
            , data: data
            , success: success
            , error: function () {
                layer.msg("发生未知错误！");
            }
        })
    }

    , ajaxManagement: function (url, data, success) {
        layui.$.ajax({
            type: 'post'
            , dataType: 'json'
            , url: BMY.url.prefix + url
            , data: data
            , success: success
            , error: function (err) {
                var msg = err.responseJSON.message || "发生未知错误！";
                layer.msg(msg);
            }
        })
    }

    , okHandle: function (json, index, tableId, ok) {
        if (json.code === BMY.status.ok) {
            var okMsg = ok !== undefined ? ok : json.message;
            layer.msg(okMsg);
            if (index)
                layer.close(index);
            if (tableId)
                layui.table.reload(tableId);
        } else {
            layer.msg(json.message);
        }
    }

    , msgHandle: function (json, callback) {
        layer.msg(json.message || json.msg || json.errorMsg || json.errMsg);
        callback();
    }
    , okMsgHandle: function (json, ok) {
        if (json.code === BMY.status.ok) {
            var okMsg = ok !== undefined ? ok : json.message;
            layer.msg(okMsg);
        } else {
            layer.msg(json.message);
        }
    }

    , dateFormatter: function (date) {
        if (date.indexOf("T") > -1) {
            var temp = date.split("T");
            var t = temp[0] + " " + temp[1];
            if (t.indexOf(".") > -1) {
                return t.substring(0, t.indexOf("."));
            } else {
                return t;
            }
        }
        return date;
    }

    , coffee4Me: function () {
        layer.open({
            type: 1,
            title: false,
            closeBtn: false,
            area: ['550px', '319px'],
            shade: 0.8,
            id: 'BMY_PAY',
            resize: false,
            shadeClose: true,
            btnAlign: 'c',
            moveType: 1,
            content: '<div style="padding: 20px; background-color: #5b6275; color: #fff; font-weight: 300;" class="layui-row">' +
                '<div class="layui-col-md6">' +
                '   <img src="/static/assets/img/alipay.png" style="width: 250px;height: 250px;">' +
                '   <p style="text-align: center;" class="layui-admin-mt10">支付宝</p>' +
                '</div> ' +
                '<div class="layui-col-md6">' +
                '   <img src="/static/assets/img/wechat.png" style="width: 250px;height: 250px;">' +
                '   <p style="text-align: center;" class="layui-admin-mt10">微信</p>' +
                '</div>' +
                '</div>'
        });
    }

    , stickyIt: function () {
        var agent = navigator.userAgent;
        var isNotWebkit = (agent.indexOf("Edge/") > -1) || (agent.indexOf("Firefox/") > -1);
        if (isNotWebkit) {
            if ($("#main-body")) {
                $("#main-body").find("div.layui-row.layui-col-space10").removeClass("animated fadeInUp");
            }
            if ($("#note-body")) {
                $("#note-body").removeClass("animated fadeInUp");
            }
        }
    }

    , sticky: function () {
        var listHeight = $(".layui-container>.layui-row>.layui-col-md9").outerHeight(true);
        var stickHeight = $(".layui-container #affix-side").parents(".layui-col-md3").outerHeight(true);
        return stickHeight < listHeight;
    }

    , getParam: function (paramName) {
        var name, value;
        var str = location.href;
        var num = str.indexOf("?");
        str = str.substr(num + 1);
        var arr = str.split("&");
        for (var i = 0; i < arr.length; i++) {
            num = arr[i].indexOf("=");
            if (num > 0) {
                name = arr[i].substring(0, num);
                if (name === paramName) {
                    value = arr[i].substr(num + 1);
                    return value;
                }
            }
        }
    }
    , setCookie: function (name, value) {
        var Days = 30;
        var exp = new Date();
        exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
        document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
    }
    , getCookie: function (name) {
        var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
        if (arr = document.cookie.match(reg))
            return unescape(arr[2]);
        else
            return null;
    }
    , delCookie: function (name) {
        var exp = new Date();
        exp.setTime(exp.getTime() - 1);
        var cval = getCookie(name);
        if (cval != null)
            document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
    }

    , toAdminIndex: function () {
        location.href = BMY.url.manage_index;
    }

    , transUrl: function (url) {
        return BMY.startsWith(url, '/management/') ? '#/' + url.substring(12) : url;
    }

    , startsWith: function (str, prefix) {
        return str.slice(0, prefix.length) === prefix;
    }

    , refreshLayUIComponent: function () {
        //layui中的form和element的刷新
        if (window._layform) {
            window._layform.render();
        }
        if (window._layelem) {
            window._layelem.render();
        }
        if (window._laySelect) {
            window._laySelect.render();
        }
    }

    , animateNav: function ($body) {
        var result = $body.offset().top - $(window).scrollTop();
        if (result >= -50) {
            if (result >= window._justResult) {
                $("body.simple .header").removeClass("header-small");
                $("body.simple .nav-header").removeClass("nav-header-small");
            } else {
                $("body.simple .nav-header").addClass("nav-header-small");
                $("body.simple .header").addClass("header-small");
            }
        } else {
            if (result <= window._justResult) {
                $("body.simple .nav-header").addClass("nav-header-small");
                $("body.simple .header").addClass("header-small");
            } else {
                $("body.simple .header").removeClass("header-small");
                $("body.simple .nav-header").removeClass("nav-header-small");
            }
        }
        return result;
    }

    //设备检测
    , detectmob: function () {
        return !!(navigator.userAgent.match(/Android/i)
            || navigator.userAgent.match(/webOS/i)
            || navigator.userAgent.match(/iPhone/i)
            || navigator.userAgent.match(/iPad/i)
            || navigator.userAgent.match(/iPod/i)
            || navigator.userAgent.match(/BlackBerry/i)
            || navigator.userAgent.match(/Windows Phone/i));

    }
};
/**
 * 全局登录超时判断
 */
$(document).ajaxComplete(function (event, xhr, options) {
    if (xhr && xhr.responseJSON && xhr.responseJSON.code) {
        if (xhr.responseJSON.code === -1) {
            location.href = xhr.responseJSON.data;
        }
    }
});