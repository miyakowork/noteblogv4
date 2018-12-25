layui.use(['table', 'element'], function () {
    var table = layui.table
        , element = layui.element;

    element.render();
    var noteTable = table.render({
        elem: '#project-table'
        , height: 'full'
        , url: BMY.url.prefix + "/project/list"
        , cellMinWidth: 90
        , limit: 10
        , size: 'lg'
        , cols: [[
            {type: 'numbers'}
            , {field: 'name', title: '项目名称', sort: true}
            , {
                field: "projectCate", title: '分类', sort: true, templet: function (d) {
                    return d.projectCate.cnName;
                }
            }
            , {field: "description", sort: true, title: "描述"}
            , {field: "url", sort: true, title: "项目主页"}
            , {
                field: "cover", sort: false, title: "封面", templet: function (d) {
                    return "<img style='width: 50px;height: 50px;' src='" + d.cover + "'>";
                }
            }
            , {title: '操作', width: 300, align: 'center', toolbar: '#projectTableBar'}
        ]]
        , page: true
    });


//监听工具条
    table.on('tool(project)', function (obj) {
        var data = obj.data;
        if (obj.event === 'detail') {
            location.hash = vipspa.stringify("/project/edit", {id: data.id});
        } else if (obj.event === 'del') {
            layer.confirm('确认删除吗？', function (index) {
                obj.del();
                BMY.ajax(BMY.url.prefix + "/project/delete/" + data.id, {}, function (json) {
                    BMY.okMsgHandle(json);
                });
                layer.close(index);
            });
        }
    });

    table.on('sort(project)', function (obj) {
        noteTable.reload({
            initSort: obj
            , where: {
                sort: obj.field
                , order: obj.type
            }
        });
    });

});







