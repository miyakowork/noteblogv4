layui.use(['form', 'table', 'element'], function () {
    var table = layui.table
        , form = layui.form
        , element = layui.element;

    element.render();
    var articleTable = table.render({
        elem: '#article-table'
        , height: 'full'
        , url: BMY.url.prefix + "/article/list"
        , cellMinWidth: 90
        , limit: 10
        , size: 'lg'
        , where: {
            order: 'desc'
            , sort: 'post'
        }
        , initSort: {
            field: 'post'
            , type: 'desc'
        }
        , cols: [[
            {type: 'numbers'}
            , {
                field: 'title', title: '标题', sort: true, minWidth: 250, templet: function (d) {
                    return '<a href="/article/' + d.id + '" class="layui-blue" target="_blank">' + d.title + '</a>';
                }
            }
            , {
                field: 'cate', title: '分类', templet: function (d) {
                    return d.cate.cnName;
                }
            }
            , {
                field: 'post', title: '发布时间', minWidth: 200, sort: true, templet: function (d) {
                    return BMY.dateFormatter(d.post);
                }
            }
            , {
                field: 'draft', title: '状态', templet: function (d) {
                    return d.draft ? '<span class="layui-badge layui-bg-orange">未发布</span>' : '<span class="layui-badge layui-bg-blue">已发布</span>';
                }
            }
            , {field: 'view', title: '浏览数', width: 90}
            , {title: '评论', width: 90, align: 'center', toolbar: '#commentedTpl'}
            , {title: '打赏', width: 90, align: 'center', toolbar: '#appreciableTpl'}
            , {field: 'top', title: '置顶', width: 110, templet: '#topTpl'}
            , {title: '操作', width: 200, align: 'center', toolbar: '#articleBar'}
        ]]
        , page: true
    });
    var $ = layui.$, active = {
        reload: function () {
            var titleSearch = $('#article-title-search');
            //执行重载
            table.reload('article-table', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                , where: {
                    title: titleSearch.val()
                }
            });
        }
    };

    $('button[data-type]').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

//监听工具条
    table.on('tool(article)', function (obj) {
        var data = obj.data;
        if (obj.event === 'detail') {
            location.hash = vipspa.stringify("/article/edit", {id: data.id});
        } else if (obj.event === 'del') {
            layer.confirm('确认删除吗？', function (index) {
                obj.del();
                BMY.ajax(BMY.url.prefix + "/article/delete/" + data.id, {}, function (json) {
                    BMY.okMsgHandle(json);
                });
                layer.close(index);
            });
        }
    });

    table.on('sort(article)', function (obj) {
        articleTable.reload({
            initSort: obj
            , where: {
                sort: obj.field
                , order: obj.type
            }
        });
    });

    form.on('switch(appreciable)', function (obj) {
        BMY.ajax(BMY.url.prefix + "/article/update/appreciable/" + this.value, {appreciable: obj.elem.checked}, function (json) {
            BMY.okMsgHandle(json);
            layer.tips('打赏：' + ((obj.elem.checked) ? "开启" : "关闭"), obj.othis);
        });
    });

    form.on('switch(commented)', function (obj) {
        BMY.ajax(BMY.url.prefix + "/article/update/commented/" + this.value, {commented: obj.elem.checked}, function (json) {
            BMY.okMsgHandle(json);
            layer.tips('评论：' + ((obj.elem.checked) ? "开启" : "关闭"), obj.othis);
        });
    });

    form.on('checkbox(top)', function (obj) {
        BMY.ajax(BMY.url.prefix + "/article/update/top/" + this.value, {top: obj.elem.checked}, function (json) {
            BMY.okMsgHandle(json);
            layer.tips(((obj.elem.checked) ? "已置顶" : "取消置顶"), obj.othis);
        });
    });

});








