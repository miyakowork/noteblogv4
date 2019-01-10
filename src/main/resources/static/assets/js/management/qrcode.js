/** Created By Wuwenbin https://wuwenbin.me
 * mail to wuwenbinwork@163.com
 * 欢迎加入我们，QQ群：697053454
 * if you use the code,  please do not delete the comment
 * 如果您使用了此代码，请勿删除此头部注释
 * */
layui.use(['upload', 'element'], function () {
    var element = layui.element
        , upload = layui.upload;
    element.render();

    //执行实例
    var alipay = upload.render({
        elem: '#alipay' //绑定元素
        , url: BMY.url.prefix + '/upload' //上传接口
        , data: {
            reqType: 'lay',
            payType: "alipay"
        }
        , done: function (res) {
            if (res.code === 0) {
                $("#a").find("img").attr("src", res.data.src);
                BMY.ajaxManagement("/settings/pay/update", {
                    name: 'alipay',
                    value: res.data.src,
                    msg:"支付宝二维码"
                }, function (json) {
                    BMY.msgHandle(json)
                });
            }
        }
        , error: function () {
            layer.msg("上传失败！");
        }
    });

    var wechat = upload.render({
        elem: '#wechat' //绑定元素
        , url: BMY.url.prefix + '/upload' //上传接口
        , data: {
            reqType: 'lay',
            payType: "wechat_pay"
        }
        , done: function (res) {
            if (res.code === 0) {
                $("#w").find("img").attr("src", res.data.src);
                BMY.ajaxManagement("/settings/pay/update", {
                    name: 'wechat_pay',
                    value: res.data.src,
                    msg:"微信支付二维码"
                }, function (json) {
                    BMY.msgHandle(json)
                });
            }
        }
        , error: function () {
            layer.msg("上传失败！");
        }
    });


});







