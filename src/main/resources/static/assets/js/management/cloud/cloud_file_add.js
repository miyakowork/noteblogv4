/** Created By Wuwenbin https://wuwenbin.me
 * mail to wuwenbinwork@163.com
 * 欢迎加入我们，QQ群：697053454
 * if you use the code,  please do not delete the comment
 * 如果您使用了此代码，请勿删除此头部注释
 * */
layui.use(['element', 'form', 'layer', 'upload'], function () {
    var form = layui.form;
    var element = layui.element;
    var $ = layui.$;
    element.render();
    form.render();


    var post = function (data) {
        data.field.cloudFileCate = data.field.cateId;
        $.ajax({
            type: "post"
            , url: BMY.url.prefix + "/cloudFile/create"
            , dataType: "json"
            , data: data.field
            , success: function (json) {
                BMY.msgHandle(json, function () {
                    if (json.code === 200) {
                        location.hash = vipspa.stringifyDefault("/cloudFile");
                    }
                })
            }
        });
    };


    //监听提交
    form.on('submit(cloudFileSubmit)', function (data) {
        if (data.field.description.length > 500) {
            layer.msg("项目描述长度不能超过500字符");
        } else {
            post(data);
        }
        return false;
    });

});





