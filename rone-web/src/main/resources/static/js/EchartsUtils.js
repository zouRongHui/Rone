/*
* 具体的配置可查阅Echarts的官方文档：https://www.echartsjs.com/option.html
* 若有特殊的配置需求，可在返回的option基础上做变动或要求相关开发人员提供该配置方法
*/

/**
 * 简单的饼图
 * @param titleText     标题，String
 * @param legendData    图例数据，String数组
 * @param seriesName    系列名(提示框浮层中展示)，String
 * @param seriesData    分析数据，json数据[{name:'', value:''}...{name:'', value:''}],其余配置可查看Echarts的官方文档
 * @return {{color: string[], legend: {orient: string, data: *, x: string, selected: {}}, series: {data: *, center: string[], name: *, type: string, radius: string[]}[], tooltip: {formatter: string, trigger: string}, title: {subtext: string, show: boolean, x: string, text: *}}}
 * Demo:
    var legendData = ['直接访问','邮件营销','联盟广告','视频广告','搜索引擎'];
    var seriesData = [];
    seriesData.push({value:335, name:'直接访问'});
    seriesData.push({value:310, name:'邮件营销'});
    seriesData.push({value:234, name:'联盟广告'});
    seriesData.push({value:135, name:'视频广告'});
    seriesData.push({value:1548, name:'搜索引擎'});
    var option = getSimplePieOption('某站点用户访问来源', legendData, '访问来源', seriesData);
 */
function getSimplePieOption(titleText, legendData, seriesName, seriesData) {
    var option = {
        title : {//标题
            show: true,//是否显示
            text: titleText,//主标题
            subtext: '',//副标题
            x: 'center'//标题位置，center、left、right、bottom
        },
        legend: {//图例
            orient: 'vertical',//图例布局朝向，'horizontal'水平(默认值),'vertical'垂直
            x: 'left',//标题位置，center、left、right、bottom
            top: '5%',//距上边框的距离
            left: '2%',//距左边框距离
            selected: {},//图例中选中状态，默认都选中，不要选择的将其设置为false，例如：{'直接访问': false}
            data: legendData
        },
        toolbox: {//工具栏
            right: '15px',//距右边距
            feature: {//具体哪些功能
                saveAsImage: {show: true}//保存图片
            }
        },
        tooltip : {//提示框
            trigger: 'item',//触发类型，饼图默认
            formatter: "{a} <br/>{b} : {c} ({d}%)"//提示框浮层内容格式，{a}（系列名称），{b}（数据项名称），{c}（数值）, {d}（百分比）
        },
        series : [
            {
                name: seriesName,//系列名，主要是在提示框浮层中展示
                type: 'pie',//图形类型
                radius: ['0', '65%'] ,//饼图圆半径，第一个参数为内半径，第二个参数为外半径
                center: ['50%', '50%'],//饼图圆心位置
                data: seriesData
            }
        ],
        //配色，循环
        color: ['rgb(46,199,201)','rgb(255,185,128)','rgb(74,179,255)','rgb(7,210,226)','rgb(27,192,160)','rgb(15,147,189)']
    };
    return option;
}

/**
 * 双饼图
 * @param titleText     标题，String
 * @param legendData    图例数据，String数组
 * @param seriesName1   左侧系列名(提示框浮层中展示)，String
 * @param seriesData1   左侧分析数据，json数据[{name:'', value:''}...{name:'', value:''}],其余配置可查看Echarts的官方文档
 * @param seriesName2   右侧系列名(提示框浮层中展示)，String
 * @param seriesData2   右侧分析数据，json数据[{name:'', value:''}...{name:'', value:''}],其余配置可查看Echarts的官方文档
 * @return {{color: string[], legend: {orient: string, top: string, data: *, x: string, selected: {}}, series: {data: *, center: string[], name: *, label: {formatter: string, show: boolean}, type: string, radius: string[]}[], tooltip: {formatter: string, trigger: string}, title: {subtext: string, show: boolean, x: string, text: *}}}
 * Demo:
    var legendData = ['rose1','rose2','rose3'];
    var seriesData1 = [{value:10, name:'rose1'},{value:5, name:'rose2'},{value:15, name:'rose3'}];
    var seriesData2 = [{value:10, name:'rose1'},{value:5, name:'rose2'},{value:15, name:'rose3'}];
    var option = getDoublePieOption('双饼图', legendData, '左侧图', seriesData1, '右侧图', seriesData2);
 */
function getDoublePieOption(titleText, legendData, seriesName1, seriesData1, seriesName2, seriesData2) {
    var option = {
        title : {//标题
            show: true,//是否显示
            text: titleText,//主标题
            subtext: '',//副标题
            x: 'left'//标题位置，center、left、right、bottom
        },
        legend: {//图例
            orient: 'horizontal',//图例布局朝向，'horizontal'水平(默认值),'vertical'垂直
            x: 'center',//标题位置，center、left、right、bottom
            top: '5%',//距上边框的距离
            // left: '2%',//距左边框距离
            selected: {},//图例中选中状态，默认都选中，不要选择的将其设置为false，例如：{'直接访问': false}
            data: legendData
        },
        toolbox: {//工具栏
            right: '15px',//距右边距
            feature: {//具体哪些功能
                saveAsImage: {show: true}//保存图片
            }
        },
        tooltip : {//提示框
            trigger: 'item',//触发类型，饼图默认
            formatter: "{a} <br/>{b} : {c} ({d}%)"//提示框浮层内容格式，{a}（系列名称），{b}（数据项名称），{c}（数值）, {d}（百分比）
        },
        series : [
            {
                name: seriesName1,//系列名，主要是在提示框浮层中展示
                type: 'pie',//图形类型
                radius: ['0', '65%'] ,//饼图圆半径，第一个参数为内半径，第二个参数为外半径
                center: ['25%', '50%'],//饼图圆心位置
                label: {//图形上的文本标签
                    show: true,//是否展示
                    //自定义显示文本
                    // formatter: function(obj) {
                    //     var totalCount = 0;
                    //     for (var i = 0; i < option.series[0].data.length; i++) {
                    //         totalCount += option.series[0].data[i].value;
                    //     }
                    //     return obj.name + " "
                    //         + ((Number)(obj.value/totalCount*100)).toFixed(0) + "%";
                    // }
                    //也可使用其自带的字符串模板, {a}（系列名称），{b}（数据项名称），{c}（数值）, {d}（百分比）
                    formatter: '{b} : {c} ({d}%)'
                },
                data: seriesData1
            },
            {
                name: seriesName2,//系列名，主要是在提示框浮层中展示
                type: 'pie',//图形类型
                radius: ['0', '65%'] ,//饼图圆半径，第一个参数为内半径，第二个参数为外半径
                center: ['75%', '50%'],//饼图圆心位置
                label: {//图形上的文本标签
                    show: true,//是否展示
                    formatter: '{b} : {c} ({d}%)'//展示的格式，{a}（系列名称），{b}（数据项名称），{c}（数值）, {d}（百分比）
                },
                data: seriesData2
            }
        ],
        //配色，循环
        color: ['rgb(46,199,201)','rgb(255,185,128)','rgb(74,179,255)','rgb(7,210,226)','rgb(27,192,160)','rgb(15,147,189)']
    };
    return option;
}

/**
 * 嵌套环形图
 * @param titleText     标题，String
 * @param legendData    图例数据，String数组
 * @param seriesName1   内圈系列名(提示框浮层中展示)，String
 * @param seriesData1   内圈分析数据，json数据[{name:'', value:''}...{name:'', value:''}],其余配置可查看Echarts的官方文档
 * @param seriesName2   外环系列名(提示框浮层中展示)，String
 * @param seriesData2   外环分析数据，json数据[{name:'', value:''}...{name:'', value:''}],其余配置可查看Echarts的官方文档
 * @return {{color: string[], legend: {orient: string, top: string, data: *, left: string, x: string, selected: {}}, series: *[], tooltip: {formatter: string, trigger: string}, title: {subtext: string, show: boolean, x: string, text: *}}}
 * Demo：
    var legendData = ['直达','营销广告','搜索引擎','邮件营销','联盟广告','视频广告','百度','谷歌','必应','其他'];
    var seriesData1 = [{value:335, name:'直达'},{value:679, name:'营销广告'},{value:1548, name:'搜索引擎'}];
    var seriesData2 = [{value:335, name:'直达'},{value:310, name:'邮件营销'},{value:234, name:'联盟广告'},{value:135, name:'视频广告'},
        {value:1048, name:'百度'},{value:251, name:'谷歌'},{value:147, name:'必应'},{value:102, name:'其他'}];
    var option = getNestedRingPieOption('嵌套环形图', legendData, '内圈', seriesData1, '外环', seriesData2);
 */
function getNestedRingPieOption(titleText, legendData, seriesName1, seriesData1, seriesName2, seriesData2) {
    var option = {
        title : {//标题
            show: true,//是否显示
            text: titleText,//主标题
            subtext: '',//副标题
            x: 'center'//标题位置，center、left、right、bottom
        },
        legend: {//图例
            orient: 'vertical',//图例布局朝向，'horizontal'水平(默认值),'vertical'垂直
            x: 'left',//标题位置，center、left、right、bottom
            top: '5%',//距上边框的距离
            left: '2%',//距左边框距离
            selected: {},//图例中选中状态，默认都选中，不要选择的将其设置为false，例如：{'直接访问': false}
            data: legendData
        },
        toolbox: {//工具栏
            right: '15px',//距右边距
            feature: {//具体哪些功能
                saveAsImage: {show: true}//保存图片
            }
        },
        tooltip : {//提示框
            trigger: 'item',//触发类型，饼图默认
            formatter: "{a} <br/>{b} : {c} ({d}%)"//提示框浮层内容格式，{a}（系列名称），{b}（数据项名称），{c}（数值）, {d}（百分比）
        },
        series : [
            {
                name: seriesName1,//系列名，主要是在提示框浮层中展示
                type: 'pie',//图形类型
                radius: ['0', '35%'] ,//饼图圆半径，第一个参数为内半径，第二个参数为外半径
                center: ['50%', '50%'],//饼图圆心位置
                label: {//图形上的文本标签
                    position: 'inner'//位置，饼图扇区内部
                },
                data: seriesData1
            },
            {
                name: seriesName2,//系列名，主要是在提示框浮层中展示
                type: 'pie',//图形类型
                radius: ['45%', '65%'] ,//饼图圆半径，第一个参数为内半径，第二个参数为外半径
                center: ['50%', '50%'],//饼图圆心位置
                data: seriesData2
            }
        ],
        //配色，循环
        color: ['rgb(46,199,201)','rgb(255,185,128)','rgb(74,179,255)','rgb(7,210,226)','rgb(27,192,160)','rgb(15,147,189)']
    };
    return option;
}

/**
 * 简单的柱状/折线图
 * @param titleText     标题，String
 * @param axisData      x轴数据，String数组
 * @param seriesName   系列名(提示框浮层中展示)，String
 * @param seriesType    展现形式，柱状图：bar, 折线图：line
 * @param seriesData    展示的数据，String数组
 * @return {{yAxis: {type: string}[], xAxis: {data: *, type: string}[], color: string[], series: {data: *, name: *, label: {show: boolean, position: string}, type: string}[], toolbox: {feature: {saveAsImage: {show: boolean}, magicType: {show: boolean, type: string[]}, dataView: {show: boolean, readOnly: boolean, optionToContent: (function(): string)}}, right: string}, tooltip: {axisPointer: {type: string}, trigger: string}, title: {subtext: string, show: boolean, x: string, text: *}}}
 * Demo：
    var axisData = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
    var seriesData = [120, 200, 150, 80, 70, 110, 130];
    var option = getSimpleBarOption('简单的柱状图', axisData, '柱状图', 'bar', seriesData);
 */
function getSimpleBarOption(titleText, axisData, seriesName, seriesType, seriesData) {
    var option = {
        title : {//标题
            show: true,//是否显示
            text: titleText,//主标题
            subtext: '',//副标题
            x: 'center'//标题位置，center、left、right、bottom
        },
        toolbox: {//工具栏位
            right: '15px',//右边距
            feature: {//具体功能
                dataView: {//数据视图
                    show: true,//是否展示
                    readOnly: true,//是否只读
                    optionToContent: function () {//自定义展示格式
                        var table = "<table style='width: 100%;user-select: text;'><thead><tr>" +
                            "<th style='width:100%;text-align: right;padding-right: 5px;'>" + title + "</th>" +
                            "<th style='width: 100%;text-align: left;padding-left: 5px;'>人数</th>" +
                            "</tr></thead><tbody>";
                        for (var i = 0; i < axisData.length; i++) {
                            table += "<tr>" +
                                "<td style='width: 100%;text-align: right;padding-right: 5px;'>" + axisData[i] + "</td>" +
                                "<td style='width: 100%;text-align: left;padding-left: 5px;'>" + seriesData[i] + "</td>" +
                                "</tr>";
                        }
                        table += "</tbody></table>";
                        return table;
                    }
                },
                magicType: {
                    show: true,//是否展示
                    type: ['line', 'bar']//柱、线切换
                },
                saveAsImage: {show: true}//保存为图片
            }
        },
        tooltip: {//提示框
            trigger: 'axis',//触发类型
            axisPointer: {//指示器
                type: 'shadow'//阴影
            }
        },
        xAxis: [//x轴
            {
                type: 'category',//类目轴
                boundaryGap:'true',//default:true此时x轴的刻度作为分隔符，数据在两刻度之间展示，false时数据在刻度线出展示
                data: axisData//类目轴数据
                // axisLabel: {
                //     //设置文本旋转角度 -90~90
                //     rotate: -30
                //     //自定义文本格式
                //     formatter: function(value) {
                //         return value.split("").join("\n");
                //     }
                // }
            }
        ],
        yAxis: [//y轴
            {
                type: 'value'//数值轴
            }
        ],
        series: [
            {
                name: seriesName,//系列名
                yAxisIndex: 0,//使用的 y 轴的 index
                type: seriesType,//柱状图：bar, 折线图：line
                smooth: true,//图形是否光滑，仅在折线图中生效
                stack: null,//数据堆叠，同个类目轴上系列配置相同的stack值可以堆叠放置
                label: {//图形上的文本标签
                    show: true,//是否展示
                    position: 'top'//展示位置
                },
                data: seriesData//展示数据
            }
        ],
        //配色，循环
        color: ['rgb(46,199,201)','rgb(255,185,128)','rgb(74,179,255)','rgb(7,210,226)','rgb(27,192,160)','rgb(15,147,189)']
    };
    return option;
}

/**
 * 拼装柱状图的series
 * @param name          系列名，String
 * @param type          图形类型，柱状图：bar, 折线图：line
 * @param yAxisIndex    使用的 y 轴的 index, 从0开始计数
 * @param stack         堆叠配置，为null时不进行堆叠
 * @param data          展示数据
 * @return {{data: *, name: *, type: *, yAxisIndex: number, smooth: boolean}}
 * Demo:
    var data = [2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2];
    var series = getBarSeries("平均温度", "bar", 0, null, data);
 */
function getBarSeries(name, type, yAxisIndex, stack, data) {
    var series = {
        name: name,//系列名
        type: type,//柱状图：bar, 折线图：line
        yAxisIndex: yAxisIndex,//使用的 y 轴的 index
        smooth: true,//图形是否光滑，仅在折线图中生效
        stack: stack,//数据堆叠，同个类目轴上系列配置相同的stack值可以堆叠放置
        data: data//展示数据
    };
    return series;
}

/**
 * 复杂的柱状图
 * @param titleText     标题，String
 * @param legendData    图例数据，String数组
 * @param axisData      x轴数据，String数组
 * @param yAxisNumber   y轴数量，Number整数
 * @param series        展示数据，柱状图series数组，来自getBarSeries()方法
 * @return {{yAxis: Array, xAxis: {data: *, type: string, boundaryGap: string}[], color: string[], legend: {data: *}, series: *, toolbox: {feature: {saveAsImage: {show: boolean}, magicType: {show: boolean, type: string[]}, dataView: {show: boolean, readOnly: boolean}}, right: string}, tooltip: {axisPointer: {type: string}, trigger: string}, title: {subtext: string, show: boolean, x: string, text: *}}}
 * Demo:
    var legendData = ['蒸发量','降水量','平均温度'];
    var axisData = ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'];
    var series = [];
    series.push(getBarSeries("蒸发量", "bar", 0, null, [2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3]));
    series.push(getBarSeries("降水量", "bar", 0, null, [2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3]));
    series.push(getBarSeries("平均温度", "line", 1, null, [2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2]));
    var option = getComplexBarOption("", legendData, axisData, 2, series);
 */
function getComplexBarOption(titleText, legendData, axisData, yAxisNumber, series) {
    var option = {
        title : {//标题
            show: true,//是否显示
            text: titleText,//主标题
            subtext: '',//副标题
            x: 'left'//标题位置，center、left、right、bottom
        },
        legend:{//图例
            data: legendData
        },
        toolbox: {//工具栏位
            right: '15px',//右边距
            feature: {//具体功能
                dataView: {//数据视图
                    show: true,//是否展示
                    readOnly: true//是否只读
                },
                magicType: {
                    show: true,//是否展示
                    type: ['line', 'bar']//柱、线切换
                },
                saveAsImage: {show: true}//保存为图片
            }
        },
        tooltip: {//提示框
            trigger: 'axis',//触发类型
            axisPointer: {//指示器
                type: 'shadow'//阴影
            }
        },
        xAxis: [//x轴
            {
                type: 'category',//类目轴
                boundaryGap:'true',//default:true此时x轴的刻度作为分隔符，数据在两刻度之间展示，false时数据在刻度线出展示
                data: axisData//类目轴数据
            }
        ],
        yAxis: [],//y轴，{type: 'value'//数值轴}
        series: series,
        //配色，循环
        color: ['rgb(46,199,201)','rgb(255,185,128)','rgb(74,179,255)','rgb(7,210,226)','rgb(27,192,160)','rgb(15,147,189)']
    };
    //动态配置y轴数量
    if (yAxisNumber >= 1) {
        for (var i = 0; i < yAxisNumber; i++) {
            option.yAxis.push({type: 'value'});
        }
    }
    return option;
}

/**
 * 为图形加上区域缩放组件
 * 支持柱状图、散点图、K线图等
 * @param option                柱状图、散点图、K线图等等坐标系图已配置完的option，要求option.toolbox.feature必须已定义
 * @param dataZoomStartValue    数据窗口的初始开始点，String
 * @param dataZoomEndValue      数据窗口的初始结束点，String
 * @param dataZoomInsideEnable  内置缩放组件是否启用，Boolean
 * @param dataZoomSliderEnable  滑动条型数据区域缩放组件是否启用，Boolean
 * @return {*}
 * Demo:
    var option = {};//从其他地方已配置完的option
    //例如x轴数据是 ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    option = addDataZoom(option, 'Tue', 'Fri', false, true);
 */
function addDataZoom(option, dataZoomStartValue, dataZoomEndValue, dataZoomInsideEnable, dataZoomSliderEnable) {
    option.toolbox.feature.dataZoom = {yAxisIndex: false};//数据区域缩放工具,指定不缩放y轴
    option.dataZoom = [//数据区域缩放
        {
            type: 'inside',//内置型数据区域缩放组件
            disabled: !dataZoomInsideEnable,//是否禁用
            startValue: dataZoomStartValue,//数据窗口的初始开始点
            endValue: dataZoomEndValue//数据窗口的初始结束点
        },
        {
            type: 'slider',//滑动条型数据区域缩放组件
            show: dataZoomSliderEnable,//是否显示(启用)
            startValue: dataZoomStartValue,//数据窗口的初始开始点
            endValue: dataZoomEndValue//数据窗口的初始结束点
        }
    ];
    return option;
}

/**
 * 简单的雷达图(维度图)
 * @param titleText         标题，String
 * @param indicator         维度指示器，json数组[{ name: 维度名称, max: 维度最大值},{name: '', max: ''} ...{name: '', max: ''}],其余配置可查看Echarts的官方文档
 * @param seriesName        系列名(提示框浮层中展示)，String
 * @param seriesDataValue   展示数据，String数组
 * @return {{radar: {indicator: *, shape: string, center: string[], name: {padding: number[], backgroundColor: string, color: string, borderRadius: number}, radius: string}, color: string[], series: {areaStyle: {}, data: {name: *, value: *}[], name: *, type: string}[], title: {subtext: string, show: boolean, x: string, text: *}}}
 * Demo:
    var indicator = [{ name: '销售', max: 6500},{ name: '管理', max: 16000},{ name: '信息技术', max: 30000},
        { name: '客服', max: 38000},{ name: '研发', max: 52000}];
    var seriesDataValue = [4300, 10000, 28000, 35000, 50000];
    var option = getSimpleRadarOption('五维度图', indicator, '开销', seriesDataValue);
 */
function getSimpleRadarOption(titleText, indicator, seriesName, seriesDataValue) {
    var option = {
        title : {//标题
            show: true,//是否显示
            text: titleText,//主标题
            subtext: '',//副标题
            x: 'center'//标题位置，center、left、right、bottom
        },
        tooltip : {},//提示框
        radar: {
            shape: 'polygon',//边框形状，default：polygon(多边形), circle(原型)
            center: ['50%', '50%'],//设置中心位置
            radius: '55%',//半径
            name: {//指示器名称的配置
                color: '#020202',//文本颜色
                backgroundColor: '#dee0e0',//北京颜色
                borderRadius: 3,//背景框圆角
                padding: [3, 5]//文字块的内边距，用法同css的padding
            },
            indicator: indicator//维度指示器，{ name: 维度名称, max: 维度最大值}
        },
        series: [//展示数据配置
            {
                name: seriesName,//系列名称
                type: 'radar',//雷达图
                areaStyle: {//区域填充
                    opacity: 0.5,//透明度,支持从 0 到 1 的数字，为 0 时不绘制该图形
                    color: {// 线性渐变，前四个参数分别是 x0, y0, x2, y2, 范围从 0 - 1，相当于在图形包围盒中的百分比，如果 globalCoord 为 `true`，则该四个值是绝对的像素位置
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 0,
                        y2: 1,
                        colorStops: [{
                            offset: 0, color: '#67f1d2' // 0% 处的颜色
                        }, {
                            offset: 1, color: '#d47afa' // 100% 处的颜色
                        }],
                        global: false // 缺省为 false
                    }
                },
                data : [//数据
                    {
                        value : seriesDataValue,
                        name : seriesName
                    }
                ]
            }
        ],
        //配色，循环
        color: ['rgb(46,199,201)','rgb(255,185,128)','rgb(74,179,255)','rgb(7,210,226)','rgb(27,192,160)','rgb(15,147,189)']
    };
    return option;
}

/**
 * 简单的漏斗图
 * @param titleText     标题，String
 * @param legendData    图例数据，String数组
 * @param seriesTitle   系列名(提示框浮层中展示)，String
 * @param seriesMax     展示数据的最大值
 * @param seriesData    分析数据，json数据[{name:'', value:''}...{name:'', value:''}],其余配置可查看Echarts的官方文档
 * @return {{legend: {orient: string, top: string, data: *, left: string, x: string, selected: {}}, series: {min: number, data: *, max: *, gap: number, name: *, emphasis: {label: {fontSize: number}}, itemStyle: {borderColor: string}, minSize: string, maxSize: string, sort: string, label: {show: boolean, position: string}, type: string}[], toolbox: {feature: {saveAsImage: {show: boolean}}, right: string}, tooltip: {formatter: string, trigger: string}, title: {subtext: string, show: boolean, x: string, text: *}}}
 * Demo:
    var legendData = ['展现','点击','访问','咨询','订单'];
    var seriesMax = 100;
    var seriesData = [{value: 60, name: '访问'},{value: 40, name: '咨询'},{value: 20, name: '订单'},
        {value: 80, name: '点击'},{value: 100, name: '展现'}];
    var option = getSimpleFunnelOption('漏斗图', legendData, '漏斗图', seriesMax, seriesData);
 */
function getSimpleFunnelOption(titleText, legendData, seriesTitle, seriesMax, seriesData) {
    var option = {
        title : {//标题
            show: true,//是否显示
            text: titleText,//主标题
            subtext: '',//副标题
            x: 'center'//标题位置，center、left、right、bottom
        },
        legend: {//图例
            orient: 'vertical',//图例布局朝向，'horizontal'水平(默认值),'vertical'垂直
            x: 'left',//标题位置，center、left、right、bottom
            top: '5%',//距上边框的距离
            left: '2%',//距左边框距离
            selected: {},//图例中选中状态，默认都选中，不要选择的将其设置为false，例如：{'直接访问': false}
            data: legendData
        },
        toolbox: {//工具栏
            right: '15px',//距右边距
            feature: {//具体哪些功能
                saveAsImage: {show: true}//保存图片
            }
        },
        tooltip : {//提示框
            trigger: 'item',//触发类型
            formatter: "{a} <br/>{b} : {c}"//提示框浮层内容格式，{a}（系列名称），{b}（数据项名称），{c}（数值）, {d}（百分比）
        },
        series: [
            {
                name: seriesTitle,//item展示内容
                type: 'funnel',//漏斗图
                min: 0,//最小值
                max: seriesMax,//最大值
                minSize: '0%',//default
                maxSize: '100%',//default
                sort: 'descending',//default:'descending'(降序), 'ascending'(升序),'none'(按照data顺序)
                gap: 0,//间距
                label: {
                    show: true,
                    position: 'inside'//默认文本的展示位置
                },
                itemStyle: {
                    borderColor: '#fff'//边框
                },
                emphasis: {
                    label: {
                        fontSize: 30//高亮时文本大小
                    }
                },
                data: seriesData
            }
        ],
        //配色，循环
        color: ['rgb(46,199,201)','rgb(255,185,128)','rgb(74,179,255)','rgb(7,210,226)','rgb(27,192,160)','rgb(15,147,189)']
    };
    return option;
}
