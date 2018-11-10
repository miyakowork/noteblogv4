layui.define(function (exports) {
    exports('article_simple', function (page, next, tpl) {
        return nextPage(page, next, tpl)
    });
});

var ani_ =
    '{{# layui.each(d.pageArticle.content, function(index, item){ }}' +
    '<blockquote class="layui-elem-quote simple-article layui-anim layui-anim-upbit layui-row layui-col-space15"><div class="layui-col-xs12 layui-col-sm10">' +
    '   <div class="article-title center-to-head">' +
    '       {{# if(item.top){ }}' +
    '       <span class="layui-badge  layui-bg-cyan">置顶</span>' +
    '       {{# } }}' +
    '       <span class="layui-badge" style="background: #F44336;">{{ item.cate.cnName }}</span>' +
    '       {{# if(item.urlSequence != null && item.urlSequence !=""){ }}' +
    '       <a href="/article/u/{{ item.urlSequence }}">{{ item.title }}</a>' +
    '       {{# }else{ }}' +
    '       <a href="/article/{{ item.id }}">{{ item.title }}</a>' +
    '       {{# } }}' +
    '   </div>' +
    '   <div class="article-title sm">' +
    '       {{# if(item.top){ }}' +
    '       <span class="layui-badge layui-bg-cyan">置顶</span>' +
    '       {{# } }}' +
    '       <span class="layui-badge" style="background: #F44336;">{{ item.cate.cnName }}</span>' +
    '       {{# if(item.urlSequence != null && item.urlSequence !=""){ }}' +
    '       <a href="/article/u/{{ item.urlSequence }}">{{ item.title }}</a>' +
    '       {{# }else{ }}' +
    '       <a href="/article/{{ item.id }}">{{ item.title }}</a>' +
    '       {{# } }}' +
    '   </div>' +
    '       {{# if(item.urlSequence != null && item.urlSequence !=""){ }}' +
    '   <div class="article-body normal">{{ item.summary }}<a href="/article/u/{{ item.urlSequence }}">...</a></div>' +
    '       {{# }else{ }}' +
    '   <div class="article-body normal">{{ item.summary }}<a href="/article/{{ item.id }}">...</a></div>' +
    '       {{# } }}' +
    '       {{# if(item.urlSequence != null && item.urlSequence !=""){ }}' +
    '   <div class="article-body sm">{{  item.summary.substring(0,Math.ceil(lenStat(item.summary)/2))  }}<a href="/article/u/{{ item.urlSequence }}">...</a></div>' +
    '       {{# }else{ }}' +
    '   <div class="article-body sm">{{ item.summary.substring(0,Math.ceil(lenStat(item.summary)/2)) }}<a href="/article/{{ item.id }}">...</a></div>' +
    '       {{# } }}' +
    '   <div class="article-footer">' +
    '       <p>' +
    '           <span><i class="fa fa-thermometer-1"></i> 热度：<span>{{ item.view }}℃</span></span>' +
    '           {{# if(d.articleComments[item.id] > 0){ }}' +
    '           <span><i class="fa fa-commenting-o"></i> <span>{{ d.articleComments[item.id] }}</span>条评论</span>' +
    '           {{# } }}' +
    '           {{# if(d.articleComments[item.id] == 0){ }}' +
    '           <span><i class="fa fa-user-o"></i> 用户：<span>{{ d.articleAuthors[item.id] }}</span></span>' +
    '           {{# } }}' +
    '           <span><i class="fa fa-clock-o"></i> 时间：<span class="detail-date">{{ BMY.wholeCnDate(item.post) }}</span><span class="simple-date">{{ BMY.simpleDate(item.post) }}</span></span>' +
    '       </p>' +
    '   </div>' +
    '</div><div class="layui-hide-xs layui-col-sm2"><img class="simple-article-cover" src="{{item.cover}}"></div> </blockquote>' +
    '{{# });  }}';

function isChinese(str) {  //判断是不是中文
    var reCh = /[u00-uff]/;
    return !reCh.test(str);
}

function lenStat(target) {
    var strlen = 0; //初始定义长度为0
    var txtval = $.trim(target);
    for (var i = 0; i < txtval.length; i++) {
        if (isChinese(txtval.charAt(i)) === true) {
            strlen = strlen + 0.5;//中文为2个字符
        } else {
            strlen = strlen + 1;//英文一个字符
        }
    }
    // strlen = Math.ceil(strlen / 2);//中英文相加除2取整数
    return strlen;
}

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
            tpl(ani_).render(json.data, function (html) {
                next(html, json.data.pageArticle.last)
            });
            if (BMY.affix !== undefined && BMY.affix !== null) {
                BMY.affix.reinit();
            }
            if (s !== "" && s !== undefined && s !== null
                || c !== "" && c !== undefined && c !== null) {
                BMY.indexVM.quote.showSearch = true;
                BMY.indexVM.quote.searchCount = json.data.pageArticle.totalPages;
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