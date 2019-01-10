/** Created By Wuwenbin https://wuwenbin.me
 * mail to wuwenbinwork@163.com
 * 欢迎加入我们，QQ群：697053454
 * if you use the code,  please do not delete the comment
 * 如果您使用了此代码，请勿删除此头部注释
 * */
layui.use(['element', 'layer'], function () {
    var element = layui.element;
    var layer = layui.layer;

    element.render();

    var E1 = window.wangEditor;
    var editor1 = new E1('#website-editor');
    editor1.create();
    var editor2 = new E1('#blog-editor');
    editor2.create();
    var editor3 = new E1('#me-editor');
    editor3.create();

    $("#website-submit").click(function () {
        BMY.ajax(BMY.url.prefix + "/about/post", {tab: "about_website", content: editor1.txt.html()}, function (json) {
            BMY.okMsgHandle(json, "编辑成功！");
        })
    })

    $("#blog-submit").click(function () {
        BMY.ajax(BMY.url.prefix + "/about/post", {tab: "about_blog", content: editor2.txt.html()}, function (json) {
            BMY.okMsgHandle(json, "编辑成功！");
        })
    })

    $("#me-submit").click(function () {
        BMY.ajax(BMY.url.prefix + "/about/post", {tab: "about_me", content: editor3.txt.html()}, function (json) {
            BMY.okMsgHandle(json, "编辑成功！");
        })
    })

    $(".layui-tab-title>li:eq(0)").addClass("layui-this")

});






