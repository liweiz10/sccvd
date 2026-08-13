/*
* 颜色映射函数
* */
function colorMapFun(categorys) {
    const colors = [[31,120,180],[51,160,44],[227,26,28],[255,127,0],[106,61,154],[177,89,40],[166,206,227],[178,223,138],[251,154,153],[253,191,111],[202,178,214],[255,255,153],[141,211,199],[255,255,179],[190,186,218],[251,128,114],[128,177,211],[253,180,98],[179,222,105],[252,205,229],[217,217,217],[188,128,189],[204,235,197],[255,237,111]]
    const colorMap = new Map();//为每个细胞类型映射颜色
    categorys.forEach((type, index) => {
        colorMap.set(type, colors[index % colors.length]);
    });
    return colorMap;
}
function generateArray(maxValue) {
    const step = maxValue / 2; // 将范围分成 2 个间隔
    const result = [];
    for (let i = 0; i <= 2; i++) {
        result.push(Math.round(i * step)); // 将每个值四舍五入为整数
    }
    return result;
};

/*
* 基因表达可视化
* */
function umapGeneExpressionPlot(data, max, myChart) {
    let option = {
        visualMap: {
            min: 0,
            max: max,
            dimension: 2,   //设置数组中第几个索引是值
            orient: 'vertical',
            right: 10,
            top: 'center',
            text: ['HIGH', 'LOW'],
            calculable: false,
            inRange: {
                color: ['#f2f2f2', '#922020']
                // color: ['#f2f2f2', '#204792']
            }
        },
        tooltip: {
            trigger: 'item',
            axisPointer: {
                type: 'cross'
            },
            showDelay: 1,
            formatter: function (params) {
                return("Gene expression: "+params.value[2])
            }
        },
        toolbox: toolbox,
        xAxis: [
            {type: 'value'}
        ],
        yAxis: [
            {type: 'value'}
        ],
        series: [
            {
                name: 'Gene expression',
                type: 'scatter',
                symbolSize: 1,
                data: data
            }
        ]
    };
    myChart.hideLoading();
    myChart.setOption(option);
}
function showGeneExpression(containerId, sampleId, geneName) {
    const chartDom = document.getElementById(containerId);
    const myChart = echarts.init(chartDom, null, {renderer: 'svg'});
    myChart.showLoading();
    $.ajax({
        url: "getGeneExpression",
        type: "GET",
        dataType: "json",
        async: true,
        data: {"id":sampleId, "geneName":geneName},
        success: function(data) {
            umapGeneExpressionPlot(data.data, data.max, myChart);
        }
    });
}

function toGeneExpression(id,container,selectContainer) { //使用异步函数不友好，且这个方法每次都需要重新获取位置信息
    let featureData = null;
    const color1 = [237, 248, 251];
    const color2 = [102, 194, 164];
    const color3 = [0, 109, 44];
    const colorArray = [color1, color2, color3];
    const featureDeckgl = new DeckGL({
        container: document.getElementById(container),
        initialViewState: {
            longitude: 2,
            latitude: 3,
            zoom: 3.5,
            minZoom: 3,
            maxZoom: 6
        },
        controller: true
    });
    function redrawFeaturePlot() {
        const domainArray = generateArray(featureData.max);
        const colorScale = d3.scaleLinear()
            .domain(domainArray)
            .range(colorArray);
        const featureLayer = new ScatterplotLayer({
            id: 'scatter-plot',
            data: featureData.data,
            radiusMaxPixels: 100,
            radiusMinPixels: 1,
            radiusScale: 20,
            getRadius: 1,
            getPosition: d => [d[0], d[1]],
            getFillColor: d => colorScale(d[2])
        });
        featureDeckgl.setProps({
            layers: [featureLayer]
        });
    }
    /*基因名用于下拉菜单切换*/
    const eventHandler = function(name) {
        return function() {
            geneName = arguments[0];
            if (name=="onChange"&&geneName!=""){
                // showGeneExpression("geneExpression", id, geneName)/*使用echart绘制基因表达图*/
                $.ajax({
                    url: "getGeneExpression",
                    type: "GET",
                    dataType: "json",
                    async: true,
                    data: {"id":id, "geneName":geneName},
                    success: function(data) {
                        featureData = data;
                        redrawFeaturePlot();
                    }
                });
            }
        };
    };
    $.ajax({
        url: "getGeneName",
        type: "GET",
        dataType: "json",
        async: true,
        data: {id: id},
        success: function(data) {
            const $selectGene = $('#'+selectContainer).selectize({
                valueField: 'name',
                labelField: 'name',
                searchField: 'name',
                options: data.data,
                create: false,
                onChange: eventHandler('onChange')
            });
            const control = $selectGene[0].selectize;
            control.setValue(data.data[0].name);
        }
    });
}


async function toSignacExpression2(id, container, selectContainer) { //使用异步函数，这个方法仅获取一次位置信息
    // 初始化变量
    let positionData = null; // 存储位置数据（二维数组）
    let valueData = null;    // 存储值数据（一维数组）
    let currentDataType = ''; // 当前基因
    // 固定配色方案
    const colorArray = [[237, 248, 251], [102, 194, 164], [0, 109, 44]];
    // 初始化DeckGL
    const featureDeckgl = new deck.DeckGL({
        container: document.getElementById(container),
        initialViewState: {
            longitude: 2,
            latitude: 3,
            zoom: 3.5,
            minZoom: 3,
            maxZoom: 6
        },
        controller: true
    });
    // 获取固定的位置(二维数组)
    async function generatePositionData() {
        try {
            const response = await $.ajax({
                url: "getUmapPosition",
                type: "GET",
                dataType: "json",
                data: {"id": id}
            });
            return response.data;
        } catch (error) {
            console.error('Error fetching position data:', error);
            throw error;
        }
    }
    // 根据选择的基因生成值数据（一维数组）
    async function generateValueData(geneName) {
        try {
            const response = await $.ajax({
                url: "getSignacExpressionValue",
                type: "GET",
                dataType: "json",
                data: {"id": id, "geneName": geneName}
            });
            return {
                data: response.data,
                max: response.max
            };
        } catch (error) {
            console.error('Error fetching value data:', error);
            throw error;
        }
    }
    // 生成颜色映射域
    function generateArray(maxValue) {
        return [0, maxValue / 2, maxValue];
    }
    // 重绘散点图
    function redrawFeaturePlot() {
        // 创建颜色比例尺
        $("#signacMax").text(valueData.max);
        const domainArray = generateArray(valueData.max);
        const colorScale = d3.scaleLinear()
            .domain(domainArray)
            .range(colorArray);
        // 创建一个索引数组，长度为数据点的数量作为图的数据（其实只是一个行索引），然后在获取数据时，根据索引提取真实数据（getPosition和getFillColor）
        const indices = Array.from({length: positionData.length}, (_, i) => i);
        // 创建散点图层
        const featureLayer = new deck.ScatterplotLayer({
            id: 'scatter-plot',
            data: indices,
            radiusMaxPixels: 100,
            radiusMinPixels: 1,
            radiusScale: 20,
            getPosition: d => [positionData[d].umap_x,positionData[d].umap_y], // 通过索引d获取位置
            getFillColor: d => colorScale(valueData.data[d]), // 通过索引d获取值并计算颜色
            getRadius: 1,
            pickable: true,
            autoHighlight: true,
            updateTriggers: {
                getFillColor: [currentDataType]
            }
        });
        $("#signacPlotGLlodding").hide();
        $("#signacshow").hide();
        if (valueData.data.length==0){
            $("#signacshow").show();
        }
        // 更新图层
        featureDeckgl.setProps({
            layers: [featureLayer]
        });
    }
    // 初始化函数
    async function init() {
        try {
            // 获取位置数据
            positionData = await generatePositionData();
            // 获取基因列表并初始化选择器
            const geneData = await $.ajax({
                url: "getSignacNameBinary",
                type: "GET",
                dataType: "json",
                data: {id: id}
            });
            const handleGeneChange = async function(geneName) {
                if (geneName !== "") {
                    currentDataType = geneName;
                    // 生成新的值数据
                    valueData = await generateValueData(geneName);
                    // 重新渲染
                    redrawFeaturePlot();
                }
            };
            const $selectGene = $('#'+selectContainer).selectize({
                valueField: 'name',
                labelField: 'name',
                searchField: 'name',
                options: geneData.data,
                create: false,
                items: [geneData.data[0].name],
                onChange: handleGeneChange
            });
            await handleGeneChange(geneData.data[0].name);
        } catch (error) {
            console.error('Initialization error:', error);
        }
    }
    // 启动初始化
    init();
}

async function toGeneExpression2(id, container, selectContainer) { //使用异步函数，这个方法仅获取一次位置信息
    // 初始化变量
    let positionData = null; // 存储位置数据（二维数组）
    let valueData = null;    // 存储值数据（一维数组）
    let currentDataType = ''; // 当前基因
    // 固定配色方案
    const colorArray = [[237, 248, 251], [102, 194, 164], [0, 109, 44]];
    // 初始化DeckGL
    const featureDeckgl = new deck.DeckGL({
        container: document.getElementById(container),
        initialViewState: {
            longitude: 2,
            latitude: 3,
            zoom: 3.5,
            minZoom: 3,
            maxZoom: 6
        },
        controller: true
    });
    // 获取固定的位置(二维数组)
    async function generatePositionData() {
        try {
            const response = await $.ajax({
                url: "getUmapPosition",
                type: "GET",
                dataType: "json",
                data: {"id": id}
            });
            return response.data;
        } catch (error) {
            console.error('Error fetching position data:', error);
            throw error;
        }
    }
    // 根据选择的基因生成值数据（一维数组）
    async function generateValueData(geneName) {
        try {
            const response = await $.ajax({
                url: "getGeneExpressionValue",
                type: "GET",
                dataType: "json",
                data: {"id": id, "geneName": geneName}
            });
            return {
                data: response.data,
                max: response.max
            };
        } catch (error) {
            console.error('Error fetching value data:', error);
            throw error;
        }
    }
    // 生成颜色映射域
    function generateArray(maxValue) {
        return [0, maxValue / 2, maxValue];
    }
    // 重绘散点图
    function redrawFeaturePlot() {
        // 创建颜色比例尺
        $("#featureMax").text(valueData.max);
        const domainArray = generateArray(valueData.max);
        const colorScale = d3.scaleLinear()
            .domain(domainArray)
            .range(colorArray);
        // 创建一个索引数组，长度为数据点的数量作为图的数据（其实只是一个行索引），然后在获取数据时，根据索引提取真实数据（getPosition和getFillColor）
        const indices = Array.from({length: positionData.length}, (_, i) => i);
        // 创建散点图层
        const featureLayer = new deck.ScatterplotLayer({
            id: 'scatter-plot',
            data: indices,
            radiusMaxPixels: 100,
            radiusMinPixels: 1,
            radiusScale: 20,
            getPosition: d => [positionData[d].umap_x,positionData[d].umap_y], // 通过索引d获取位置
            getFillColor: d => colorScale(valueData.data[d]), // 通过索引d获取值并计算颜色
            getRadius: 1,
            pickable: true,
            autoHighlight: true,
            updateTriggers: {
                getFillColor: [currentDataType]
            }
        });
        $("#genePlotGLlodding").hide();
        // 更新图层
        featureDeckgl.setProps({
            layers: [featureLayer]
        });
    }
    // 初始化函数
    async function init() {
        try {
            // 获取位置数据
            positionData = await generatePositionData();
            // 获取基因列表并初始化选择器
            const geneData = await $.ajax({
                url: "getGeneNameBinary",
                type: "GET",
                dataType: "json",
                data: {id: id}
            });
            const handleGeneChange = async function(geneName) {
                if (geneName !== "") {
                    currentDataType = geneName;
                    // 生成新的值数据
                    valueData = await generateValueData(geneName);
                    // 重新渲染
                    redrawFeaturePlot();
                }
            };
            const $selectGene = $('#'+selectContainer).selectize({
                valueField: 'name',
                labelField: 'name',
                searchField: 'name',
                options: geneData.data,
                create: false,
                items: [geneData.data[0].name],
                onChange: handleGeneChange
            });
            await handleGeneChange(geneData.data[0].name);
        } catch (error) {
            console.error('Initialization error:', error);
        }
    }
    // 启动初始化
    init();
}