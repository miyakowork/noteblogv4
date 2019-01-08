layui.use(['element', 'layer', 'form', 'carousel'], function () {
    var element = layui.element;
    var form = layui.form;
    var layer = layui.layer;

    element.render();
    form.render();



    var dom = document.getElementById("sum-container");
    var myChart = echarts.init(dom, 'walden');
    var posList = [
        'left', 'right', 'top', 'bottom',
        'inside',
        'insideTop', 'insideLeft', 'insideRight', 'insideBottom',
        'insideTopLeft', 'insideTopRight', 'insideBottomLeft', 'insideBottomRight'
    ];
    var app = {};
    app.configParameters = {
        rotate: {
            min: -90,
            max: 90
        },
        align: {
            options: {
                left: 'left',
                center: 'center',
                right: 'right'
            }
        },
        verticalAlign: {
            options: {
                top: 'top',
                middle: 'middle',
                bottom: 'bottom'
            }
        },
        position: {
            options: echarts.util.reduce(posList, function (map, pos) {
                map[pos] = pos;
                return map;
            }, {})
        },
        distance: {
            min: 0,
            max: 100
        }
    };

    app.config = {
        rotate: 90,
        align: 'left',
        verticalAlign: 'middle',
        position: 'insideBottom',
        distance: 15,
        onChange: function () {
            var labelOption = {
                normal: {
                    rotate: app.config.rotate,
                    align: app.config.align,
                    verticalAlign: app.config.verticalAlign,
                    position: app.config.position,
                    distance: app.config.distance
                }
            };
            myChart.setOption({
                series: [{
                    label: labelOption
                }, {
                    label: labelOption
                }, {
                    label: labelOption
                }, {
                    label: labelOption
                }]
            });
        }
    };


    var labelOption = {
        normal: {
            show: true,
            position: app.config.position,
            distance: app.config.distance,
            align: app.config.align,
            verticalAlign: app.config.verticalAlign,
            rotate: app.config.rotate,
            formatter: '{c}  {name|{a}}',
            fontSize: 16,
            rich: {
                name: {
                    textBorderColor: '#fff'
                }
            }
        }
    };

    var option = {
        color: ['#003366', '#006699', '#4cabce', '#e5323e'],
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        legend: {
            data: ['http://127.0.0.1:8000/management/dashboard1', 'http://127.0.0.1:8000/management/dashboard2', 'http://127.0.0.1:8000/management/dashboard', '77777']
        },
        calculable: true,
        xAxis: [
            {
                type: 'category',
                axisTick: {show: false},
                data: ['2012', '2013', '2014', '2015', '2016']
            }
        ],
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: [
            {
                name: 'http://127.0.0.1:8000/management/dashboard1',
                type: 'bar',
                barGap: 0,
                label: labelOption,
                data: [320, 332, 301, 334, 390]
            },
            {
                name: 'http://127.0.0.1:8000/management/dashboard2',
                type: 'bar',
                label: labelOption,
                data: [220, 182, 191, 234, 290]
            },
            {
                name: 'http://127.0.0.1:8000/management/dashboard',
                type: 'bar',
                label: labelOption,
                data: [150, 232, 201, 154, 190]
            },
            {
                name: '77777',
                type: 'bar',
                label: labelOption,
                data: [98, 77, 101, 99, 40]
            }
        ]
    };


    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }

});






