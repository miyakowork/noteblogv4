layui.define(function (exports) {
    exports('article', function (page, next, tpl) {
        return nextPage(page, next, tpl)
    });
});

var ani =
    '{{# layui.each(d.page.result, function(index, item){ }}' +
    '<div class="layui-collapse layui-panel layui-article animated fadeInUp">' +
    '   <div class="layui-colla-item">' +
    '       <div class="layui-colla-content layui-show layui-article">' +
    '           <fieldset class="layui-elem-field layui-field-title">' +
    '               <legend class="center-to-head">' +
    '                   <span class="layui-badge layui-bg-green"> {{ item.cateName }}</span>' +
    '                   {{# if(item.top){ }}' +
    '                   <span class="layui-badge layui-bg-cyan"> 置顶</span>' +
    '                   {{# } }}' +
    '                   <a href="/article/{{ item.id }}">{{ item.title }}</a>' +
    '               </legend>' +
    '               {{# if(item.cover === null || item.cover === ""){ }}' +
    '               <div class="layui-field-box">' +
    '                   &nbsp;&nbsp;&nbsp;&nbsp;{{ item.summary }}...&nbsp;<a class="loading" href="/article/{{ item.id }}">更多<i class="fa fa-angle-double-right"></i> </a>' +
    '               </div>' +
    '               {{# } }}' +
    '               {{# if(item.cover !== null && item.cover !== ""){ }}' +
    '               <div class="layui-field-box has-pic">' +
    '                   <div class="layui-row layui-col-space10">' +
    '                       <div class="layui-col-lg10 layui-col-md10 layui-col-sm10 layui-col-xs12">' +
    '                           &nbsp;&nbsp;&nbsp;&nbsp;{{ item.summary }}...&nbsp;<a class="loading" href="/article/{{ item.id }}">更多<i class="fa fa-angle-double-right"></i> </a>' +
    '                       </div>' +
    '                       <div class="layui-col-lg2 layui-col-md2 layui-col-sm2">' +
    '                           <img src="{{ item.cover }}" class="panel-pic">' +
    '                       </div>' +
    '                   </div>' +
    '               </div>' +
    '               {{# } }}' +
    '               <div class="operation">' +
    '                   <div class="tags">' +
    '                       {{# layui.each(d.tags[item.id], function(index1, item1){ }}' +
    '                       <span class="layui-badge-rim" onclick="searchTag(this)"><i class="fa fa-tag"></i> {{ item1.name }}</span>' +
    '                       {{# }); }}' +
    '                   </div>' +
    '                   <div class="info">' +
    '                       <span class="views"><i class="fa fa-thermometer-half"></i> {{ item.views }}<code style="font-family: cursive;">℃</code></span>' +
    '                       <span class="datetime"><i class="fa fa-bullhorn"></i> {{ BMY.dateFormatter(item.post,"/", false) }}</span>' +
    '                   </div>' +
    '               </div>' +
    '           </fieldset>' +
    '       </div>' +
    '   </div> ' +
    '</div>' +
    '{{# });  }}';

var ani_ =
    '{{# layui.each(d.pageArticle.content, function(index, item){ }}' +
    '<blockquote class="layui-elem-quote simple-article layui-anim layui-anim-upbit">' +
    '   <div class="article-title center-to-head">' +
    '       {{# if(item.top){ }}' +
    '       <span class="layui-badge" style="background: #F44336;">置顶</span>' +
    '       {{# } }}' +
    '       <span class="layui-badge layui-bg-cyan">{{ item.cate.name }}</span>' +
    '       <a href="/article/{{ item.id }}">{{ item.title }}</a>' +
    '   </div>' +
    '   <div class="article-title sm">' +
    '       {{# if(item.top){ }}' +
    '       <span class="layui-badge" style="background: #F44336;">置顶</span>' +
    '       {{# } }}' +
    '       <span class="layui-badge layui-bg-cyan">{{ item.cate.name }}</span>' +
    '       <a href="/article/{{ item.id }}">{{ item.title }}</a>' +
    '   </div>' +
    '   <div class="article-body normal">{{ item.summary }}<a href="/article/{{ item.id }}">...</a></div>' +
    '   <div class="article-body sm">{{ item.summary }}<a href="/article/{{ item.id }}">...</a></div>' +
    '   <div class="article-footer">' +
    '       <p>' +
    '           <span><i class="fa fa-thermometer-1"></i> 热度：<span>{{ item.views }}℃</span></span>' +
    '           {{# if(item.articleComments[item.id] > 0){ }}' +
    '           <span><i class="fa fa-commenting-o"></i> <span>{{ item.articleComments[item.id] }}</span>条评论</span>' +
    '           {{# } }}' +
    '           {{# if(item.articleComments[item.id] == 0){ }}' +
    '           <span><i class="fa fa-user-circle-o"></i> <span>{{ item.articleAuthors[item.id] }}</span></span>' +
    '           {{# } }}' +
    '           <span><i class="fa fa-clock-o"></i> 时间：<span class="detail-date">2018年8月27日 22:00:00</span><span class="simple-date">18/08/27 22:00</span></span>' +
    '       </p>' +
    '   </div>' +
    '</blockquote>' +
    '{{# });  }}';

function nextPage(page, next, tpl) {
    var s = BMY.getParam("s");
    var c = BMY.getParam("c");
    var t = BMY.getParam("t");
    $.post("/next", {
        limit: 10,
        page: page,
        title: s,
        textContent: s,
        cateId: c,
        tagSearch: t
    }, function (json) {
        if (json.code === BMY.status.ok) {
            tpl(ani).render(json.data, function (html) {
                next(html, json.data.page.hasNext)
            });
            if (BMY.affix !== undefined && BMY.affix !== null) {
                BMY.affix.reinit();
            }
            if (s !== "" && s !== undefined && s !== null
                || c !== "" && c !== undefined && c !== null) {
                BMY.indexVM.quote.showSearch = true;
                BMY.indexVM.quote.searchCount = json.data.page.totalCount;
            }
            $(window).resize();
        }

    });
}

function searchTag(span) {
    var s = $(span).text();
    s = s.substring(1);
    location.href = "/index?s=" + s + "&t=" + s
}