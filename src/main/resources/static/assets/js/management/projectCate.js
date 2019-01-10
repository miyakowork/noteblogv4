/** Created By Wuwenbin https://wuwenbin.me
 * mail to wuwenbinwork@163.com
 * 欢迎加入我们，QQ群：697053454
 * if you use the code,  please do not delete the comment
 * 如果您使用了此代码，请勿删除此头部注释
 * */
layui.use(['table', 'element'], function () {
    var table = layui.table
        , element = layui.element;
    element.render();

    table.render({
        elem: '#project-cate-table'
        , url: BMY.url.prefix + '/dictionary/projectCate/list'
        , page: true
        , limit: 10
        , height: 'full'
        , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
        , cols: [[
            {field: 'id', width: 80, title: 'ID', sort: true}
            , {field: 'name', title: '分类名称', edit: 'text', sort: true}
            , {field: 'cnName', title: '分类中文名', edit: 'text', sort: true}
            , {fixed: 'right', title: '操作', width: 178, align: 'center', toolbar: '#projectCateTableBar'}
        ]]
    });

    //监听单元格编辑
    table.on('edit(cate)', function (obj) {
        BMY.ajax(BMY.url.prefix + "/dictionary/projectCate/update", obj.data, function (json) {
            if (json.code === BMY.status.ok) {
                layer.msg('修改成功！')
            } else {
                layer.msg("修改出错，错误信息：" + json.message);
            }
            setTimeout(function () {
                location.hash = vipspa.stringifyDefault("/cate");
            }, 500)
        })
    });

    //监听工具条
    table.on('tool(cate)', function (obj) {
        var data = obj.data;
        if (obj.event === 'del') {
            layer.confirm('真的删除么?', function (index) {
                BMY.ajax(BMY.url.prefix + "/dictionary/projectCate/delete", {cateId: data.id}, function (json) {
                    BMY.okMsgHandle(json, "删除成功");
                    if (json.code === BMY.status.ok) obj.del();
                    layer.close(index);
                })
            });
        }
    });

    $(".layui-btn[data-type=addProjectCate]").on("click", function () {
        var index = layer.confirm($("#addProjectCatePage").html(), {
            title: '新增分类'
            , type: 1
            , area: '480px'
        }, function () {
            BMY.ajax(BMY.url.prefix + "/dictionary/projectCate/create", {
                name: $("input.layui-input[name=name]").val()
                , cnName: $("input.layui-input[name=cnName]").val()
            }, function (json) {
                BMY.okHandle(json, index, "project-cate-table");
            })
        });
    });

});







