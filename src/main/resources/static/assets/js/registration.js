layui.use(['form', 'jquery'], function () {
    var form = layui.form
        , $ = layui.$;

    form.verify({
        username: function (value) {
            if (!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)) {
                return '用户名不能有特殊字符';
            }
            if (/(^\_)|(\__)|(\_+$)/.test(value)) {
                return '用户名首尾不能出现下划线\'_\'';
            }
            if (/^\d+\d+\d$/.test(value)) {
                return '用户名不能全为数字';
            }
            if (value.length < 4 || value.length > 12) {
                return "用户名必须4~12位，且不能包含空格";
            }
        }
        , pass: [
            /^[\S]{6,12}$/
            , '密码必须6到12位，且不能出现空格'
        ]
        , pass2: function (value) {
            var repeatPass = $("input[name=bmyPass]").val();
            if (repeatPass !== value) {
                return "两次输入的密码不一致";
            }
        }
    });

    form.on('submit(bmyReg)', function (data) {
        data.field.bmyPass = md5(data.field.bmyPass);
        BMY.ajax("/registration", data.field, function (resp) {
                if (resp.code === BMY.status.ok) {
                    layer.msg("注册成功！");
                    setTimeout(function () {
                        location.href = BMY.url.login || resp.data;
                    }, 1000);
                } else {
                    layer.msg("注册失败，" + resp.message);
                }
            }
        );
        return false;
    });

});