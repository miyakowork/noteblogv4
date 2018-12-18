layui.use(['table', 'element'], function () {
    var table = layui.table
        , element = layui.element;

    element.render();
    var noteTable = table.render({
        elem: '#profile-table'
        , height: 'full'
        , url: BMY.url.prefix + "/profile/list"
        , cellMinWidth: 90
        , limit: 10
        , size: 'lg'
        , cols: [[
            {type: 'numbers'}
            , {field: 'tab', title: 'tab名称', sort: true}
            , {field: 'name', title: '中文名称', sort: true}
            , {title: '操作', width: 200, align: 'center', toolbar: '#profileTableBar'}
        ]]
        , page: true
    });


//监听工具条
    table.on('tool(profile)', function (obj) {
        debugger
        var data = obj.data;
        if (obj.event === 'detail') {
            location.hash = vipspa.stringify("/profile/edit", {id: data.id});
        } else if (obj.event === 'del') {
            layer.confirm('确认删除吗？', function (index) {
                obj.del();
                BMY.ajax(BMY.url.prefix + "/profile/delete/" + data.id, {}, function (json) {
                    BMY.okMsgHandle(json);
                });
                layer.close(index);
            });
        }
    });

    table.on('sort(profile)', function (obj) {
        noteTable.reload({
            initSort: obj
            , where: {
                sort: obj.field
                , order: obj.type
            }
        });
    });

});







