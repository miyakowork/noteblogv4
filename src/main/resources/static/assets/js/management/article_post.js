var editor, editorMd;
layui.use(['element', 'form', 'layer', 'upload', 'formSelects'], function () {
    var form = layui.form;
    var element = layui.element;
    var $ = layui.$;
    var upload = layui.upload;
    var formSelects = layui.formSelects;
    element.render();
    form.render();

    formSelects.config('tags', {
        keyName: 'name'
        , keyVal: 'name'
    });
    formSelects.data('tags', 'server', {
        url: '/tag/all'
    });
    formSelects.maxTips('tags', function () {
        layer.msg("最多只能选择4个");
    });

    var post = function (data, draft, msg, editor, mdContent) {
        data.field.draft = draft;
        data.field.editor = editor;
        data.field.mdContent = mdContent;
        data.field.content = editor.html();
        data.field.cover = $("#coverImg").find("img").attr("src");
        $.ajax({
            type: "post"
            , url: BMY.url.prefix + "/blog/post"
            , dataType: "json"
            , data: data.field
            , success: function (json) {
                BMY.okMsgHandle(json, msg);
                if (json.code === BMY.status.ok) {
                    location.hash = vipspa.stringifyParam("blogs", {});
                }
            }
        });
    };
    //监听提交
    form.on('submit(postSubmit)', function (data) {
        post(data, false, "发布博文成功！");
        return false;
    });

    form.on('submit(draftSubmit)', function (data) {
        post(data, true, "保存草稿成功！");
        // window.location.href = BMY.url.prefix + "/index#/blogs";
        return false;
    });

    form.on("switch(summary)", function (data) {
        if (data.elem.checked) {
            $("#article-summary").show();
        } else {
            $("#article-summary").hide();
        }
    });

    form.on('radio(editor)', function (data) {
        if (data.value === "markdown") {
            editor.remove();
            $("#content-editor").append("<div id='editormd'></div>");
            $.getScript("/static/plugins/editormd/editormd.min.js", function () {
                editorMd = editormd("editormd", {
                    height: 640,
                    watch: true,
                    codeFold: true,
                    toolbarIcons: function () {
                        return [
                            "undo", "redo", "|",
                            "bold", "del", "italic", "quote", "ucwords", "uppercase", "lowercase", "|",
                            "h1", "h2", "h3", "h4", "h5", "h6", "|",
                            "list-ul", "list-ol", "hr", "|",
                            "link", "reference-link", "image", "code", "preformatted-text", "code-block", "table", "datetime", "html-entities", "pagebreak", "|",
                            "goto-line", "watch", "preview", "fullscreen", "clear", "search", "|",
                            "help", "info"
                        ]
                    },
                    pluginPath: '/static/plugins/editormd/plugins/',
                    markdown: "### 书写内容至此",
                    path: '/static/plugins/editormd/lib/',
                    placeholder: '请在此书写你的内容',
                    saveHTMLToTextarea: true,
                    imageUpload: true,
                    imageFormats: ["jpg", "jpeg", "gif", "png", "bmp"],
                    imageUploadURL: BMY.url.prefix + "/upload/editorMD?reqType=lay",
                    onfullscreen: function () {
                        $(".layui-header").css("z-index", "-1");
                        $("#left-menu").css("z-index", "-1");
                        $(".layui-form-item>label,.layui-form-item>div:not(#content-editor)").css("z-index", -1);
                        $(".layui-card").css("z-index", "-1");
                    },
                    onfullscreenExit: function () {
                        $(".layui-header").css("z-index", "999");
                        $("#left-menu").css("z-index", "999");
                        $(".layui-form-item>label,.layui-form-item>div:not(#content-editor)").css("z-index", "");
                        $(".layui-card").css("z-index", "999");
                    }
                });
            });
        }
        else if (data.value === "html") {
            editorMd.editor.remove();
            editor = KindEditor.create('#editor', {
                cssData: 'body {font-family: "Helvetica Neue", Helvetica, "PingFang SC", 微软雅黑, Tahoma, Arial, sans-serif; font-size: 14px}',
                width: "auto",
                height: "600px",
                items: [
                    'source', 'preview', 'undo', 'redo', 'code', 'cut', 'copy', 'paste',
                    'plainpaste', 'wordpaste', 'justifyleft', 'justifycenter', 'justifyright',
                    'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
                    'superscript', 'clearhtml', 'quickformat', 'selectall', 'fullscreen', '/',
                    'formatblock', 'fontname', 'fontsize', 'forecolor', 'hilitecolor', 'bold',
                    'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', 'image', 'graft',
                    'insertfile', 'table', 'hr', 'emoticons', 'pagebreak',
                    'link', 'unlink', 'about'
                ],
                uploadJson: BMY.url.prefix + '/upload?reqType=nk',
                dialogOffset: 0, //对话框距离页面顶部的位置，默认为0居中，
                allowImageUpload: true,
                allowMediaUpload: true,
                themeType: 'black',
                fixToolBar: true,
                autoHeightMode: true,
                filePostName: 'file',//指定上传文件form名称，默认imgFile
                resizeType: 1,//可以改变高度
                afterCreate: function () {
                    var self = this;
                    KindEditor.ctrl(document, 13, function () {
                        self.sync();
                        K('form[name=example]')[0].submit();
                    });
                    KindEditor.ctrl(self.edit.doc, 13, function () {
                        self.sync();
                        KindEditor('form[name=example]')[0].submit();
                    });
                },
                //错误处理 handler
                errorMsgHandler: function (message, type) {
                    try {
                        JDialog.msg({type: type, content: message, timer: 2000});
                    } catch (Error) {
                        alert(message);
                    }
                }
            });
        }
    });

    upload.render({
        elem: '#coverImg' //绑定元素
        , url: BMY.url.prefix + '/upload?reqType=lay' //上传接口
        , done: function (res) {
            if (res.code === 0) {
                $("#coverImg").html('<p><img style="width: 144px;height: 90px;" src="' + res.data.src + '"></p>');
            }
            layer.msg(res.msg || res.message);
        }
        , error: function () {
            layer.msg("上传失败！");
        }
    });

});

$(function () {
    $("#content-editor").append("<div id='editormd'></div>");
    $.getScript("/static/plugins/editormd/editormd.min.js", function () {
        editorMd = editormd("editormd", {
            height: 640,
            watch: true,
            codeFold: true,
            toolbarIcons: function () {
                return [
                    "undo", "redo", "|",
                    "bold", "del", "italic", "quote", "ucwords", "uppercase", "lowercase", "|",
                    "h1", "h2", "h3", "h4", "h5", "h6", "|",
                    "list-ul", "list-ol", "hr", "|",
                    "link", "reference-link", "image", "code", "preformatted-text", "code-block", "table", "datetime", "html-entities", "pagebreak", "|",
                    "goto-line", "watch", "preview", "fullscreen", "clear", "search", "|",
                    "help", "info"
                ]
            },
            pluginPath: '/static/plugins/editormd/plugins/',
            markdown: "### 书写内容至此",
            path: '/static/plugins/editormd/lib/',
            placeholder: '请在此书写你的内容',
            saveHTMLToTextarea: true,
            imageUpload: true,
            imageFormats: ["jpg", "jpeg", "gif", "png", "bmp"],
            imageUploadURL: BMY.url.prefix + "/upload/editorMD?reqType=lay",
            onfullscreen: function () {
                $(".layui-header").css("z-index", "-1");
                $("#left-menu").css("z-index", "-1");
                $(".layui-form-item>label,.layui-form-item>div:not(#content-editor)").css("z-index", -1);
                $(".layui-card").css("z-index", "-1");
            },
            onfullscreenExit: function () {
                $(".layui-header").css("z-index", "999");
                $("#left-menu").css("z-index", "999");
                $(".layui-form-item>label,.layui-form-item>div:not(#content-editor)").css("z-index", "");
                $(".layui-card").css("z-index", "999");
            }
        });
    });
});






