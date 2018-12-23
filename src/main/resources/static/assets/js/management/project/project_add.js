layui.use(['element', 'form', 'layer'], function () {
    var form = layui.form;
    var element = layui.element;
    var $ = layui.$;
    element.render();
    form.render();


    var post = function (data) {
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


    //监听提交
    form.on('submit(projectSubmit)', function (data) {
        post(data);
        return false;
    });

});





