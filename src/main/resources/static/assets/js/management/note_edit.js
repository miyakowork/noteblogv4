$(function () {
    var editorMd;
    layui.use(['element', 'form', 'layer', 'formSelects'], function () {
        var form = layui.form;
        var element = layui.element;
        var $ = layui.$;
        var formSelects = layui.formSelects;
        element.render();
        form.render();
        window._laySelect = formSelects;

        formSelects.config('tags', {
            keyName: 'name'
            , keyVal: 'name'
        });
        formSelects.data('tags', 'server', {
            url: BMY.url.prefix + '/note/edit/tags?id=' + noteId
        });
        formSelects.maxTips('tags', function () {
            layer.msg("最多只能选择4个");
        });

        var post = function (data) {
            //markdown文本
            data.field.mdContent = editorMd.getMarkdown();
            //html文本
            data.field.content = editorMd.getHTML();
            $.ajax({
                type: "post"
                , url: BMY.url.prefix + "/note/update"
                , dataType: "json"
                , data: data.field
                , success: function (json) {
                    BMY.msgHandle(json, function () {
                        if (json.code === 200) {
                            location.hash = vipspa.stringifyDefault("/note");
                        }
                    })
                }
            });
        };

        //监听提交
        form.on('submit(noteSubmit)', function (data) {
            post(data);
            return false;
        });

    });


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
            markdown: mdNoteContent,
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





