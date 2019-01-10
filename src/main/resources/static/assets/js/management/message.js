/** Created By Wuwenbin https://wuwenbin.me
 * mail to wuwenbinwork@163.com
 * 欢迎加入我们，QQ群：697053454
 * if you use the code,  please do not delete the comment
 * 如果您使用了此代码，请勿删除此头部注释
 * */
layui.use(['form', 'layer', 'table', 'element'], function () {
    var table = layui.table
        , element = layui.element
        , layer = layui.layer
        , form = layui.form;
    element.render();


    var commentTable = table.render({
        elem: '#message-table'
        , url: BMY.url.prefix + '/message/list'
        , page: true
        , limit: 10
        , height: 'full'
        , method: 'post'
        , cols: [[
            {
                field: 'user', title: '用户昵称', templet: function (d) {
                    return d.user.nickname;
                }
            }
            , {
                field: 'clearComment', title: '评论内容', event: 'detail'
            }
            , {
                field: 'post', title: '发布时间', sort: true, templet: function (d) {
                    return BMY.dateFormatter(d.post);
                }
            }
            , {field: 'ipAddr', title: 'IP地址'}
            , {field: 'ipCnAddr', title: 'IP具体地址'}
            , {field: 'userAgent', title: '用户代理'}
            , {title: '状态', width: 90, align: 'center', toolbar: '#enableTpl'}
        ]]
    });


    form.on('switch(enable)', function (obj) {
        BMY.ajax(BMY.url.prefix + "/message/update", {id: this.value, enable: obj.elem.checked}, function (json) {
            BMY.okMsgHandle(json);
            layer.tips('状态：' + ((obj.elem.checked) ? "正常" : "隐藏"), obj.othis);
        });
    });

    //监听单元格事件
    table.on('tool(message)', function (obj) {
        var data = obj.data;
        if (obj.event === 'detail') {
            layer.open({
                type: 1
                , offset: 'auto'
                , id: 'layerDemo' + data.id //防止重复弹出
                , content: '<div style="padding: 20px;">' + data.comment.replace(/<[^<>]+?>/g, '') + '</div>'
                , btnAlign: 'c' //按钮居中
                , shade: 0 //不显示遮罩
            });
        }
    });

    table.on('sort(message)', function (obj) {
        commentTable.reload({
            initSort: obj
            , where: {
                sort: obj.field
                , order: obj.type
            }
        });
    });

    var $ = layui.$, active = {
        reload: function () {
            var message = $("#message");
            //执行重载
            table.reload('message-table', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                , where: {
                    clearComment: message.val()
                }
            });
        }
    };

    $('#message-table-search').find('.layui-btn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
});







