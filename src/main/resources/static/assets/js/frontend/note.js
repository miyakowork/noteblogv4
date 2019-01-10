/** Created By Wuwenbin https://wuwenbin.me
 * mail to wuwenbinwork@163.com
 * 欢迎加入我们，QQ群：697053454
 * if you use the code,  please do not delete the comment
 * 如果您使用了此代码，请勿删除此头部注释
 * */
layui.define(function (exports) {
    exports('note', function (cover, page, next, laytpl) {
        return nextShare(cover, page, next, laytpl)
    });
});

var shares =
    "{{# layui.each(d.content, function(index, item){ }}" +
    "<li class=\"layui-timeline-item animated slideInUp\">" +
    "   <i class=\"layui-icon layui-timeline-axis\">&#xe63f;</i>" +
    "   <div class=\"layui-timeline-content layui-text\">" +
    "       <h3 class=\"layui-timeline-title\">{{ noteTitle(item) }}</h3>" +
    "       <div class=\"timeline-body\"'>" +
    "           {{item.content}}" +
    "       </div>" +
    "   </div>" +
    "</li>" +
    "{{# }); }}";

var shareEnds =
    "<li class=\"layui-timeline-item layui-note-cover\">" +
    "   <i class=\"layui-icon layui-timeline-axis\">&#xe63f;</i>" +
    "   <div class=\"layui-timeline-content layui-text\">" +
    "       <h3 class=\"layui-timeline-title\">笔记封面</h3>" +
    "   </div>" +
    "</li>";

function nextShare(cover, page, next, tpl) { //执行下一页的回调
    var s = BMY.getParam("t");
    $.post("/note/next", {
        pageNo: page,
        t: s,
        cc: s,
    }, function (json) {
        if (json.code === BMY.status.ok) {
            tpl(shares).render(json.data, function (html) {
                cover.slideUp();
                next(html + shareEnds, !json.data.last)
            });
            if (s !== "" && s !== undefined && s !== null) {
                BMY.noteVm.quote.showSearch = true;
                BMY.noteVm.quote.searchCount = json.data.totalElements;
            }
            $(window).resize();
        }
    });
}

function noteTitle(note) {
    return "<i class=\"fa fa-hand-grab-o \"></i> "
        + note.title
        + "<small style='margin-left: 10px;font-style: italic;color: #cccccc;'>"
        + BMY.dateFormatter(note.post)
        + "</small>";
}