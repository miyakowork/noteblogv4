layui.use(['element', 'layer', 'form', 'carousel'], function () {
    var element = layui.element;
    var form = layui.form;
    var carousel = layui.carousel;

    element.render();
    form.render();

    form.on('submit(simpleArticlePost)', function (data) {
        BMY.ajax(BMY.url.prefix + "/simple/post/article", data.field, function (json) {
            BMY.okMsgHandle(json, "保存草稿成功！");
        });
        return false;
    });

    form.on('submit(simpleNotePost)', function (data) {
        BMY.ajax(BMY.url.prefix + "/simple/post/note", data.field, function (json) {
            BMY.okMsgHandle(json, "发布随笔成功！");
        });
        return false;
    });

    //建造实例
    carousel.render({
        elem: '#sum'
        , width: '100%' //设置容器宽度
        , arrow: 'none'
    });

    var dom = document.getElementById("sum-container");
    var myChart = echarts.init(dom, 'walden');
    var option = {
        xAxis: {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                data: [820, 932, 901, 934, 1290, 1330, 1320],
                type: 'line',
                smooth: false
            },
            {
                data: [420, 732, 501, 393, 290, 330, 320],
                type: 'line',
                smooth: false
            }
        ]
    };


    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }

});






