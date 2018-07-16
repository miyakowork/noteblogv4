layui.use('form', function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.$;

    form.verify({
        username: function (value, item) { //value：表单的值、item：表单的DOM对象
            if (!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)) {
                return '用户名不能有特殊字符';
            }
            if (/(^\_)|(\__)|(\_+$)/.test(value)) {
                return '用户名首尾不能出现下划线\'_\'';
            }
            if (/^\d+\d+\d$/.test(value)) {
                return '用户名不能全为数字';
            }
        }
        , pass: [
            /^[\S]{6,12}$/
            , '密码必须6到12位，且不能出现空格'
        ]
        , repeatPass: function (value) {
            var pass = $("input[name=password]").val();
            if (pass !== value) {
                return '两次输入的密码不一致';
            }
        }
        , mail: [
            /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/
            , '请输入正确的邮箱！'
        ]
    });
    //监听提交
    form.on('submit(nbInit)', function (data) {
        var html = '<p style="color: #FF5722;">请确认您所填的信息</p>';
        var obj = data.field;
        for (var o in obj) {
            if (obj.hasOwnProperty(o) && o !== 'repeatPass') {
                var labelName = $("input[name=" + o + "], select[name=" + o + "]").parents("div.layui-form-item").find("label").text();
                if (o === 'uploadMethod') {
                    obj[o] = $("option[value=" + o + "]").text();
                }
                html += '<p>' + labelName + '：' + obj[o] + '</p>';
            }
        }
        var index = layer.confirm(html, {
            btn: ['确定', '重填']
            , title: '确认信息'
        }, function () {
            $.post('http://127.0.0.1:8088/initApp'
                , obj
                , function (json) {
                    console.log(json);
                    layer.close(index);
                });
        });

        return false;
    });

});



