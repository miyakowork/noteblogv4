/** Created By Wuwenbin https://wuwenbin.me
 * mail to wuwenbinwork@163.com
 * 欢迎加入我们，QQ群：697053454
 * if you use the code,  please do not delete the comment
 * 如果您使用了此代码，请勿删除此头部注释
 * */
layui.use(['element', 'layer', 'form', 'carousel'], function () {
    var element = layui.element;
    var form = layui.form;
    var layer = layui.layer;

    element.render();
    form.render();


    var dom = document.getElementById("sum-container");
    var myChart = echarts.init(dom, 'walden');
    var x = [];
    var y = [];
    for (var i = 0; i < tableData.length; i++) {
        x.unshift(tableData[i][0]);
        y.unshift(tableData[i][1])
    }
    var option = {
        color: ['#3398DB'],
        tooltip: {
            trigger: 'axis',
            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: [
            {
                type: 'category',
                data: x,
                axisTick: {
                    alignWithLabel: true
                }
            }
        ],
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: [
            {
                name: '当日访问量',
                type: 'bar',
                barWidth: '20%',
                data: y
            }
        ]
    };


    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }

});






