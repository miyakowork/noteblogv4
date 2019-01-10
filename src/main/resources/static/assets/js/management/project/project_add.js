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
    var upload = layui.upload;
    element.render();
    form.render();


    var post = function (data) {
        data.field.cover = $("#projectCover").find("img").attr("src") || "";
        data.field.projectCate = data.field.cateId;
        $.ajax({
            type: "post"
            , url: BMY.url.prefix + "/project/create"
            , dataType: "json"
            , data: data.field
            , success: function (json) {
                BMY.msgHandle(json, function () {
                    if (json.code === 200) {
                        location.hash = vipspa.stringifyDefault("/project");
                    }
                })
            }
        });
    };


    upload.render({
        elem: '#projectCover' //绑定元素
        , url: BMY.url.prefix + '/upload?reqType=lay' //上传接口
        , done: function (res) {
            if (res.code === 0) {
                $("#projectCover").html('<p><img style="width: 144px;height: 90px;" src="' + res.data.src + '"></p>');
            }
            layer.msg(res.msg || res.message);
        }
        , error: function () {
            layer.msg("上传失败！");
        }
    });


    //监听提交
    form.on('submit(projectSubmit)', function (data) {
        post(data);
        return false;
    });

});





