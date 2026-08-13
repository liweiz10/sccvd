/*两种绘制cell type umap图的函数，
* umapCellTypePlot：以散点图的方式绘制，当点多的时候推荐这一种方式，他的细胞类型滑动时会快一点。
* umapCellTypePlot_graph：以力导向图的方式绘制
* */
const olanguage = {
    "sProcessing": 'processing......',
    "sLengthMenu": "_MENU_ entries per page",
    "sZeroRecords": "No matching data found",
    "sInfo": "Showing _START_ to _END_ of _TOTAL_ entries",
    "sInfoEmpty": "Showing _START_ to _END_ of _TOTAL_ entries",
    "sInfoFiltered": "( Filter from _MAX_ records )",
    "sSearch": "Search: ",
    "oPaginate": {
        "sFirst": "home",
        "sPrevious": "‹",
        "sNext": "›",
        "sLast": "end"
    }
}
const toolbox = {
        feature: {
            brush: {
                type: ['rect', 'polygon', 'clear']
            },
            saveAsImage: {title: "save"}
        }
    };

const div = document.getElementById("qc1");
const width = div.offsetWidth;
const height = div.offsetHeight;
var layout = {
    height: height,
    width: width,
    showlegend: false,
    legend: {
        y: 0.5,
        font: {
            family: 'Arial, sans-serif',
            size: 10
        },
        itemclick: 'toggleothers',
        itemsizing: 'constant'
    },
    margin: {
        l: 40,
        r: 40,
        b: 60,
        t: 5,
        pad: 2
    },
    size:20
};
var config = {
    displaylogo: false,
    doubleClickDelay: 500,
    responsive: true,
    toImageButtonOptions: {
        format: 'svg', // one of png, svg, jpeg, webp
        filename: 'qc'
    }
};

/*
* 根据基因名，样本id，获取基因表达信息。
* */
function getGeneDescription(id) {
    var singleSelect = $('#select-feature').selectize();
    var singleSelectize = singleSelect[0].selectize;
    var singleValue = singleSelectize.getValue();
    $.ajax({
        url: "getGeneDescription",
        type: "GET",
        dataType: "json",
        async: true,
        data: {id: id, gene: singleValue},
        success: function(res) {
            $("#geneDescription").text(res.data.description)
            $("#geneSummary").text(res.data.summary)
            $("#geneSynonyms").text(res.data.synonyms)
            $("#geneSymbol").text(res.data.symbol)
            $("#genedb").text(res.data.dbxrefs)
            $('#geneCard').attr('href', "https://www.genecards.org/cgi-bin/carddisp.pl?gene=" + res.data.symbol)
            $('#geneId').attr('href', "https://www.ncbi.nlm.nih.gov/gene/" + res.data.geneid)
        }
    });
}

/*
* 样本信息描述
* */
function getSampleDescript(id) {
    $.ajax({
        url: "getSampleDescript",
        type: "GET",
        dataType: "json",
        async: true,
        data: {id: id},
        success: function(res) {
            if (res.data && res.data.length > 0) {
                const sampleDescript = '<tbody>\n' +
                    '<tr><td>DatasetID</td>' + '<td class="text-secondary"><a href="https://www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc='+ res.data[0].gseId +'">'+ res.data[0].gseId +'</div></td></tr>\n' +
                    '<tr><td>Species</td>' + '<td class="text-secondary">'+ res.data[0].species +'</td></tr>\n' +
                    '<tr><td>Genomic</td>' + '<td class="text-secondary">'+ res.data[0].genomic +'</td></tr>\n' +
                    '<tr><td>Technology</td>' + '<td class="text-secondary">'+ res.data[0].technology +'</td></tr>\n' +
                    '<tr><td>Strain</td>' + '<td class="text-secondary">'+ res.data[0].strain +'</td></tr>\n' +
                    '<tr><td>Article</td>' + '<td class="text-secondary">'+ res.data[0].article +'<a href="https://pubmed.ncbi.nlm.nih.gov/'+ res.data[0].pmid +'"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" className="icon icon-tabler icons-tabler-outline icon-tabler-link"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M9 15l6 -6"/><path d="M11 6l.463 -.536a5 5 0 0 1 7.071 7.072l-.534 .464"/><path d="M13 18l-.397 .534a5.068 5.068 0 0 1 -7.127 0a4.972 4.972 0 0 1 0 -7.071l.524 -.463"/></svg></a></td></tr>\n' +
                    '<tr><td>Journal</td>' + '<td class="text-secondary">'+ res.data[0].journal +'</td></tr>\n' +
                    '<tr><td>Year</td>' + '<td class="text-secondary">'+ res.data[0].year +'</td></tr>\n' +
                    '<tr><td>Description</td>' + '<td class="text-secondary text-pull">'+ res.data[0].descript +'</td></tr>\n' +
                    '</tbody>'
                $('#sampleDescript').append(sampleDescript);
            }else {
                const table = document.getElementById('sampleDescript');
                table.innerHTML = '<tr><td colspan="2">There is no sample information for the current ID, please check.</td></tr>';
            }
        }
    });
}
function getSampleInfo(container,id) {
    var $containerid = $('#' + container);
    $containerid.DataTable({
        ajax: {
            url: "getSampleInfo",
            type: "GET",
            async: true,
            data: {"id":id}
        },
        bProcessing : true,
        serverSide: true,
        searching: true,
        paging: true,
        ordering: true,
        scrollX: true,
        lengthMenu: [[5, 10, 20, 50], [5, 10, 20, 50]],
        destroy: true,
        columns: [
            {
                "title": "gsmId",
                "data": "gsmId",
                "render": function (data, type, row, meta) {
                    return "<a target='_blank' href='https://www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc=" + row.gsmId + "'>" + row.gsmId + "</a>";
                }
            },
            {"title": "sampleName","data": "sampleName"},
            {"title": "tissue","data": "tissue"},
            {"title": "age","data": "age"},
            {"title": "sex","data": "sex"},
            {"title": "treatment","data": "treatment"},
            {"title": "times","data": "times"},
        ],
        oLanguage: olanguage
    });
}


/*
* 绘制每个样本的细胞类型数量信息
* */
function toCellNumber(id, type) {
    const containerId = "cellNumber";
    const chartDom = document.getElementById(containerId);
    const myChart = echarts.init(chartDom, null, {renderer: 'svg'});
    myChart.showLoading();
    $.ajax({
        url: "getCellCount",
        type: "GET",
        dataType: "json",
        async: true,
        data: {id: id, type: type},
        success: function(res) {
            if (res.data && res.data.length > 0) {
                originalArray = res.data;
                const transformData = (originalArray) => {
                    return originalArray.map(item => {
                        const dataArray = item.value2.split(',').map(num => parseInt(num, 10));
                        return {
                            name: item.name,
                            type: 'bar',
                            stack: 'total',
                            label: {
                                show: false
                            },
                            emphasis: {
                                focus: 'series'
                            },
                            data: dataArray
                        };
                    });
                };
                const resultArray = transformData(originalArray);
                option = {
                    tooltip: {
                        trigger: 'item',
                        axisPointer: {
                            type: 'shadow' // 'shadow' as default; can also be 'line' or 'shadow'
                        }
                    },
                    toolbox: toolbox,
                    legend: {
                        type: 'scroll',
                        orient: 'vertical',
                        right: 2,
                        top: 2,
                        bottom: 2
                    },
                    grid: {
                        top: '3%',
                        left: '3%',
                        right: '40%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'value'
                    },
                    yAxis: {
                        type: 'category',
                        axisLabel: {
                            interval: 0, // 强制显示所有标签
                            rotate: 0 // 当标签太多重叠时，可以设置旋转角度
                        },
                        data: res.data[0].value1.split(',')
                    },
                    series: resultArray
                };
                myChart.hideLoading();
                myChart.setOption(option);
            }else {
                myChart.hideLoading();
                myChart.dispose();
                chartDom.innerHTML = '<label>The current data is empty.</label>';
            }
        }
    });
}
function toCellNumberCluster(id, type) {
    const containerId = "cellNumberCluster";
    const chartDom = document.getElementById(containerId);
    const myChart = echarts.init(chartDom, null, {renderer: 'svg'});
    myChart.showLoading();
    $.ajax({
        url: "getCellCountCluster",
        type: "GET",
        dataType: "json",
        async: true,
        data: {id: id, type: type},
        success: function(res) {
            if (res.data && res.data.length > 0) {
                originalArray = res.data;
                const transformData = (originalArray) => {
                    return originalArray.map(item => {
                        const dataArray = item.value2.split(',').map(num => parseInt(num, 10));
                        return {
                            name: item.name,
                            type: 'bar',
                            stack: 'total',
                            label: {
                                show: false
                            },
                            emphasis: {
                                focus: 'series'
                            },
                            data: dataArray
                        };
                    });
                };
                const resultArray = transformData(originalArray);
                option = {
                    tooltip: {
                        trigger: 'item',
                        axisPointer: {
                            type: 'shadow' // 'shadow' as default; can also be 'line' or 'shadow'
                        }
                    },
                    toolbox: toolbox,
                    legend: {
                        type: 'scroll',
                        orient: 'vertical',
                        right: 2,
                        top: 2,
                        bottom: 2
                    },
                    grid: {
                        top: '3%',
                        left: '3%',
                        right: '20%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'value'
                    },
                    yAxis: {
                        type: 'category',
                        axisLabel: {
                            interval: 0, // 强制显示所有标签
                            rotate: 0 // 当标签太多重叠时，可以设置旋转角度
                        },
                        data: res.data[0].value1.split(',')
                    },
                    series: resultArray
                };
                myChart.hideLoading();
                myChart.setOption(option);
            }else {
                myChart.hideLoading();
                myChart.dispose();
                chartDom.innerHTML = '<label>The current data is empty.</label>';
            }
        }
    });
}

/*
* 质量控制信息
* */
function toQc(id, type, divid) {
    $.ajax({
        url: "getQcInfo",
        type: "GET",
        dataType: "json",
        async: true,
        data: {id: id, type: type},
        success: function(res) {
            const styles = [];
            for (const i in res.groups){
                const target = res.groups[i];
                const color = '';
                styles.push({
                    target,
                    value: {
                        line: { color }
                    }
                });
            }
            var x1 = res.name;
            var y1 = res.value;
            var data = [{
                type: 'violin',
                x: x1,
                y: y1,
                points: 'none',
                showlegend: false,
                box: {
                    visible: true
                },
                line: {
                    color: 'green',
                },
                meanline: {
                    visible: true
                },
                transforms: [{
                    type: 'groupby',
                    groups: x1,
                    styles: styles
                }]
            }]
            Plotly.newPlot(divid, data, layout, config);
        }
    });
}

function showCelltype(containerId, sampleId, group){/*细胞类型聚类图*/
    const chartDom = document.getElementById(containerId);
    const myChart = echarts.init(chartDom, null, {renderer: 'svg'});
    myChart.showLoading();
    $.ajax({
        url: "getNodeData",
        type: "GET",
        dataType: "json",
        async: true,
        data: {id: sampleId,group: group},
        success: function(data) {
            let celltypes = new Array();
            let seriesdata = new Array();
            for (let key in data){
                celltypes.push(key)
                let track = {
                    name: key,
                    type: 'scatter',
                    symbolSize: 2,
                    large: true,
                    emphasis: {
                        focus: 'series'
                    },
                    data: data[key]
                }
                seriesdata.push(track);
            }
            umapCellTypePlot(celltypes, seriesdata, myChart);
        }
    });
}

/*
* pySCENIC TF表达可视化
* */
async function toTfExpression2(id) { //使用异步函数，这个方法仅获取一次位置信息
    // 初始化变量
    let positionData = null; // 存储位置数据（二维数组）
    let valueData = null;    // 存储值数据（一维数组）
    let currentDataType = ''; // 当前基因
    // 固定配色方案
    const colorArray = [[237, 248, 251], [102, 194, 164], [0, 109, 44]];
    // 初始化DeckGL
    const featureDeckgl = new deck.DeckGL({
        container: document.getElementById('tfPlotGL'),
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
                url: "getTfUmapPosition",
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
    async function generateValueData(tfName) {
        try {
            const response = await $.ajax({
                url: "getTFExpressionValue",
                type: "GET",
                dataType: "json",
                data: {"id": id, "tfName": tfName}
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
        $("#tfPlotGLlodding").hide();
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
            const tfData = await $.ajax({
                url: "getTFNameBinary",
                type: "GET",
                dataType: "json",
                data: {id: id}
            });
            const handleTfChange = async function(tfName) {
                if (tfName !== "") {
                    currentDataType = tfName;
                    // 生成新的值数据
                    valueData = await generateValueData(tfName);
                    // 重新渲染
                    redrawFeaturePlot();
                }
            };
            const $selectTf = $('#select-tf').selectize({
                valueField: 'name',
                labelField: 'name',
                searchField: 'name',
                options: tfData.data,
                create: false,
                items: [tfData.data[0].name],
                onChange: handleTfChange
            });
            await handleTfChange(tfData.data[0].name);
        } catch (error) {
            console.error('Initialization error:', error);
        }
    }
    // 启动初始化
    init();
}
function toTfExpression(id) {
    let tfData = null;
    const color1 = [237, 248, 251];
    const color2 = [102, 194, 164];
    const color3 = [0, 109, 44];
    const colorArray = [color1, color2, color3];
    const tfDeckgl = new DeckGL({
        container: document.getElementById('tfPlotGL'),
        initialViewState: {
            longitude: 2,
            latitude: 3,
            zoom: 3.5,
            minZoom: 3,
            maxZoom: 6
        },
        controller: true
    });
    function redrawTFPlot() {
        const domainArray = generateArray(tfData.max);
        const colorScale = d3.scaleLinear()
            .domain(domainArray)
            .range(colorArray);
        const tfLayer = new ScatterplotLayer({
            id: 'scatter-plot',
            data: tfData.data,
            radiusMaxPixels: 100,
            radiusMinPixels: 1,
            radiusScale: 20,
            getRadius: 1,
            getPosition: d => [d[0], d[1]],
            getFillColor: d => colorScale(d[2])
        });
        tfDeckgl.setProps({
            layers: [tfLayer]
        });
    }

    /*基因名用于下拉菜单切换*/
    const eventHandler = function(name) {
        return function() {
            tfName = arguments[0];
            if (name=="onChange"&&tfName!=""){
                // showGeneExpression("geneExpression", id, geneName)/*使用echart绘制基因表达图*/
                $.ajax({
                    url: "getTFExpression",
                    type: "GET",
                    dataType: "json",
                    async: true,
                    data: {"id":id, "tfName":tfName},
                    success: function(data) {
                        tfData = data;
                        redrawTFPlot();
                    }
                });
            }
        };
    };
    $.ajax({
        url: "getTFName",
        type: "GET",
        dataType: "json",
        async: true,
        data: {id: id},
        success: function(data) {
            const $selectTF = $('#select-tf').selectize({
                valueField: 'name',
                labelField: 'name',
                searchField: 'name',
                options: data.data,
                create: false,
                onChange: eventHandler('onChange')
            });
            const control = $selectTF[0].selectize;
            control.setValue(data.data[0].name);
        }
    });
}



function umapCellTypePlot(celltypes, seriesdata, myChart) {
    let option = {
        grid: {
            left: '3%',
            right: '7%',
            top: '10%',
            containLabel: true
        },
        tooltip: {
            showDelay: 0,
            formatter: function (params) {
                if (params.value.length >= 1) {
                    return (
                        'CellType: ' + params.seriesName + '<br/>' +
                        'UMAPX: ' + params.value[0] + '<br/>' +
                        'UMAPY: ' + params.value[1] + '<br/>' +
                        'Barcode: ' + params.value[3]
                    );
                } else {
                    return (
                        'CellType: ' + params.seriesName + '<br/>' +
                        'X: ' + params.name +
                        'Y: ' + params.value
                    );
                }
            }
        },
        toolbox: toolbox,
        legend: {
            data: celltypes,
            left: 'center',
            bottom: 10,
            type: 'scroll',
            orient: 'horizontal',//vertical
            right: 10,
        },
        xAxis: [
            {
                type: 'value',
                scale: true,
                axisLabel: {
                    formatter: '{value}'
                },
                splitLine: {
                    show: true
                }
            }
        ],
        yAxis: [
            {
                type: 'value',
                scale: true,
                axisLabel: {
                    formatter: '{value}'
                },
                splitLine: {
                    show: false
                }
            }
        ],
        series: seriesdata
    };
    myChart.hideLoading();
    myChart.setOption(option);
}

function umapCellTypePlot_graph(data, id) {
    let myChart = echarts.init(document.getElementById(id));
    let graph= {
        "nodes": data.node,
        "categories": data.categories
    }
    let option = {
        tooltip: {},
        legend: [
            {
                data: graph.categories.map(function (a) {
                    return a.name;
                }),
                left: 'center',
                bottom: 10,
                type: 'scroll',
                orient: 'horizontal',//vertical
                right: 10,
            }
        ],
        series: [
            {
                name: 'Les Miserables',
                type: 'graph',
                layout: 'none',
                symbolSize: 2,
                data: graph.nodes,
                categories: graph.categories,
                roam: false,
                label: {
                    show: false,
                    position: 'right',
                    formatter: '{b}'
                },
                labelLayout: {
                    hideOverlap: true
                }
            }
        ]
    };
    myChart.setOption(option);
}

/*
* 细胞类型差异表达基因表
* */
function toCelltypeLogFC(id) {
    const url = id + "_logfc_expression.txt"
    new DataTable('#deg-celltype-logfc', {
        ajax: {
            url: url,  // 数据文件路径
            dataSrc: 'data', // 从JSON的data属性中获取数据
            error: function(xhr, status, error) {
                // 请求失败处理
                $('#deg-celltype-logfc').html('<label>Failed to load data.</label>');
            }
        },
        initComplete: function(settings) {
            const api = this.api();
            const json = api.ajax.json();  // 获取完整JSON数据
            const headers = json.data[0];  // 获取表头数据（第一个数组）
            const columnDefs = headers.map(function(title,index) {  // 动态生成列配置
                const param = {
                    title: title,
                    data: index,
                    createdCell: function(nTd, sData, oData, iRow, iCol) {
                        if (iCol!=0 && parseFloat(sData)>=10){
                            // 可以自定义设置背景色，也可以用<span>标签设置框架定义颜色。
                            // $(nTd).css( "background-color", "#d73027");
                            // $(nTd).css( "color", "#fff");
                            $(nTd).empty()
                            $(nTd).append('<span class="status status-pink">'+sData+'</span>')
                        }else if (iCol!=0 && sData<10 && sData>=7){
                            /*$(nTd).css( "background-color", "#f46d43");
                            $(nTd).css( "color", "#fff");*/
                            $(nTd).empty()
                            $(nTd).append('<span class="status status-red">'+sData+'</span>')
                        }else if (iCol!=0 && sData<7 && sData>=4){
                            /*$(nTd).css( "background-color", "#fdae61");
                            $(nTd).css( "color", "#fff");*/
                            $(nTd).empty()
                            $(nTd).append('<span class="status status-orange">'+sData+'</span>')
                        }else if (iCol!=0 && sData<4 && sData>=1){
                            /*$(nTd).css( "background-color", "#fee08b");
                            $(nTd).css( "color", "#fff");*/
                            $(nTd).empty()
                            $(nTd).append('<span class="status status-yellow">'+sData+'</span>')
                        }else if (iCol!=0 && sData<=-10){
                            /*$(nTd).css( "background-color", "#1a9850");
                            $(nTd).css( "color", "#fff");*/
                            $(nTd).empty()
                            $(nTd).append('<span class="status status-cyan">'+sData+'</span>')
                        }else if (iCol!=0 && sData<=-7 && sData>-10){
                            /*$(nTd).css( "background-color", "#66bd63");
                            $(nTd).css( "color", "#fff");*/
                            $(nTd).empty()
                            $(nTd).append('<span class="status status-teal">'+sData+'</span>')
                        }else if (iCol!=0 && sData<=-4 && sData>-7){
                            /*$(nTd).css( "background-color", "#a6d96a");
                            $(nTd).css( "color", "#fff");*/
                            $(nTd).empty()
                            $(nTd).append('<span class="status status-green">'+sData+'</span>')
                        }else if (iCol!=0 && sData<=-1 && sData>-4){
                            /*$(nTd).css( "background-color", "#a6d96a");
                            $(nTd).css( "color", "#fff");*/
                            $(nTd).empty()
                            $(nTd).append('<span class="status status-lime">'+sData+'</span>')
                        }
                    }
                };
                return param;
            });
            // 重新初始化DataTable并设置列配置
            api.destroy();
            $('#deg-celltype-logfc').DataTable({
                data: json.data.slice(1),  // 跳过表头行
                columns: columnDefs,
                order: [],
                scrollX: true,
                lengthMenu: [[10, 20, 30, 100], [10, 20, 30, 100]],
                paging: true,             // 启用分页
                searching: true,          // 启用搜索
                info: true                // 显示表格信息
            });
        }
    });
}
function getDegTable(container,id) {
    var $containerid = $('#' + container);
    $containerid.DataTable({
        ajax: {
            url: "getDegTable",
            type: "GET",
            async: true,
            data: {"id":id}
        },
        bProcessing : true,
        serverSide: true,
        searching: true,
        paging: true,
        order: [],
        ordering: true,
        scrollX: true,
        lengthMenu: [[10, 20, 30, 100], [10, 20, 30, 100]],
        destroy: true,
        columns: [
            {"data": "gene"},
            {"data": "celltype"},
            {"data": "p_value"},
            {"data": "avg_log2fc"},
            {"data": "p_value_adj"},
            {"data": "pct1"},
            {"data": "pct2"}
        ],
        oLanguage: olanguage
    });
}
function getPeakDegTable(container,id) {
    var $containerid = $('#' + container);
    $containerid.DataTable({
        ajax: {
            url: "getPeakDegTable",
            type: "GET",
            async: true,
            data: {"id":id}
        },
        bProcessing : true,
        serverSide: true,
        searching: true,
        paging: true,
        order: [],
        ordering: true,
        scrollX: true,
        lengthMenu: [[10, 20, 30, 100], [10, 20, 30, 100]],
        destroy: true,
        columns: [
            /*{"data": "chr"},
            {"data": "starts"},
            {"data": "ends"},*/
            {"data": "peaks"},
            {"data": "gene"},
            {"data": "distance"},
            {"data": "pval"},
            {"data": "avglogfc"},
            {"data": "pct1"},
            {"data": "pct2"},
            {"data": "p_val_adj"},
            {"data": "celltype"}
        ],
        oLanguage: olanguage
    });
}
function getDegClusterTable(container,id) {
    var $containerid = $('#' + container);
    $containerid.DataTable({
        ajax: {
            url: "getDegClusterTable",
            type: "GET",
            async: true,
            data: {"id":id}
        },
        bProcessing : true,
        serverSide: true,
        searching: true,
        paging: true,
        order: [],
        ordering: true,
        scrollX: true,
        lengthMenu: [[10, 20, 30, 100], [10, 20, 30, 100]],
        destroy: true,
        columns: [
            {"data": "gene"},
            {"data": "celltype"},
            {"data": "p_value"},
            {"data": "avg_log2fc"},
            {"data": "p_value_adj"},
            {"data": "pct1"},
            {"data": "pct2"}
        ],
        oLanguage: olanguage
    });
}

/*富集分析*/
function pathway_go_option(go_data){
    var option = {
        dataset: {
            source: go_data
        },
        grid: {
            containLabel: false,
            left: '25%',
            // right: 100,
            top: '10%',
            bottom: '18%'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            },
            backgroundColor: 'rgba(255,255,255,0.7)',
            formatter: function (params) {
                return 'Description: ' + params[0].data[2] + '<br/>-log10(P.Adjust): ' + params[0].marker + params[0].data[0] + '<br/>Count: ' + params[0].data[1];
            }
        },
        xAxis: {
            type: 'value',
            name: 'Count',
            nameGap: 20,
            position: 'bottom',
            nameLocation: 'middle',
            nameTextStyle: {
                fontSize: 16
            },
            splitLine: {
                show: false
            }
        },
        yAxis: {
            name: 'Description',
            type: 'category',
            axisLabel: {
                show: true,
                interval: 'auto',
                formatter: function (param) {
                    return param.substring(0,50)+"...";
                }
            },
            nameLocation: 'end',
            nameGap: 20,
            nameTextStyle: {
                fontSize: 16
            },
            splitLine: {
                show: false
            }
        },
        toolbox: toolbox,
        visualMap: {
            orient: 'horizontal',
            left: '20%',
            bottom: 0,
            min: 1,
            max: 30,
            text: ['High Score', 'Low Score'],
            dimension: 0,
            inRange: {
                color: ['#65B581', '#FFCE34', '#FD665F']
            }
        },
        dataZoom: [
            {type: 'slider', yAxisIndex: 0},
        ],
        series: [
            {
                type: 'bar',
                encode: {
                    x: 'amount',
                    y: 'product'
                }
            }
        ]
    };
    return option;
}
function pathway_go(id,param1,param2,param3,type) {
    var chartDom = document.getElementById('go'+type+'Img');
    var myChart = echarts.init(chartDom, null, {renderer: 'svg'});
    myChart.showLoading();
    $.ajax({
        url: "getPathwayGo",
        type: "get",
        data: {"id":id, "param1":param1, "param2": param2, "param3": param3, "type": type},
        async: true,
        success: function (res) {
            if (res.data.length<1){
                myChart.hideLoading();
                myChart.clear();
            }else {
                var option = pathway_go_option(res.data);
                myChart.hideLoading();
                option && myChart.setOption(option);
            }
        },
        dataType: "json"
    });
}
function pathway_kegg_option(data){
    var option = {
        color: ['#fec42c'],
        grid: {
            containLabel: false,
            left: '25%',
            top: '10%',
            bottom: '18%'
        },
        toolbox: toolbox,
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            },
            backgroundColor: 'rgba(255,255,255,0.7)',
            formatter: function (params) {
                return 'Description: ' + params[0].data[1] + '<br/>-log10(P.Adjust): ' + params[0].marker + params[0].data[2] + '<br/>Gene ratio: ' + params[0].data[0] + '<br/>Count: ' + params[0].data[3];
            }
        },
        dataZoom: [
            {type: 'slider', yAxisIndex: 0},
        ],
        xAxis: {
            type: 'value',
            name: 'Gene ratio',
            nameGap: 20,
            position: 'bottom',
            nameLocation: 'middle',
            nameTextStyle: {
                fontSize: 16
            },
            splitLine: {
                show: false
            }
        },
        yAxis: {
            type: 'category',
            axisLabel: {
                show: true,
                interval: 'auto',
                formatter: function (param) {
                    return param.substring(0,50)+"...";
                }
            },
            name: 'Description',
            nameLocation: 'end',
            nameGap: 20,
            nameTextStyle: {
                fontSize: 16
            },
            splitLine: {
                show: false
            }
        },
        visualMap: [
            {
                orient: 'horizontal',
                left: '20%',
                bottom: 0,
                dimension: 3,
                min: data.countMin,
                max: data.countMax,
                itemWidth: 20,
                itemHeight: 100,
                calculable: true,
                precision: 0.1,
                text: ['Count'],
                textGap: 30,
                inRange: {
                    symbolSize: [5, 30]
                },
                outOfRange: {
                    symbolSize: [5, 30],
                    color: ['rgba(255,255,255,0.4)']
                },
                controller: {
                    inRange: {
                        color: ['#c23531']
                    },
                    outOfRange: {
                        color: ['#999']
                    }
                }
            },
            {
                orient: 'horizontal',
                left: '55%',
                bottom: 0,
                dimension: 2,
                min: data.padjMin,
                max: data.padjMax,
                itemWidth: 20,
                itemHeight: 100,
                calculable: true,
                precision: 0.1,
                text: ['-log10(P.Adjust)'],
                textGap: 30,
                inRange: {
                    colorLightness: [0.9, 0.5]
                },
                outOfRange: {
                    color: ['rgba(255,255,255,0.4)']
                },
                controller: {
                    inRange: {
                        color: ['#fec42c']
                    },
                    outOfRange: {
                        color: ['#999']
                    }
                }
            }
        ],
        series: [
            {
                type: 'scatter',
                itemStyle: {
                    opacity: 0.8,
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowOffsetY: 0,
                    shadowColor: 'rgba(0,0,0,0.3)'
                },
                data: data.data
            }
        ]
    };
    return option;
}
function pathway_kegg(id,param1,param2,type) {
    var chartDom = document.getElementById('kegg'+type+'Img');
    var myChart = echarts.init(chartDom, null, {renderer: 'svg'});
    myChart.showLoading();
    $.ajax({
        url: "getPathwayKEGG",
        type: "get",
        data: {"id":id, "param1":param1, "param2": param2, "type": type},
        async: true,
        success: function (res) {
            if (res.data==null){
                myChart.hideLoading();
                myChart.clear();
            }else {
                var option = pathway_kegg_option(res);
                myChart.hideLoading();
                option && myChart.setOption(option);
            }
        },
        dataType: "json"
    });
}
function pathway_kegg_atac(id,param1,param2,type) {
    var chartDom = document.getElementById('kegg'+type+'Img');
    var myChart = echarts.init(chartDom, null, {renderer: 'svg'});
    myChart.showLoading();
    $.ajax({
        url: "getPathwayKEGGAtac",
        type: "get",
        data: {"id":id, "param1":param1, "param2": param2, "type": type},
        async: true,
        success: function (res) {
            if (res.data==null){
                myChart.hideLoading();
                myChart.clear();
            }else {
                var option = pathway_kegg_option(res);
                myChart.hideLoading();
                option && myChart.setOption(option);
            }
        },
        dataType: "json"
    });
}

function toCelltypeEnrich(id) {
    const eventHandlerGo = function(name) {
        return function() {
            var param1 = $("#select-celltype-go option:selected").val();
            var param2 = $("#select-celltype-ontology option:selected").val();
            var param3 = $("#select-celltypeAdj-go option:selected").val();
            if (param1!=null&&param2!=null&&param3!=null){
                pathway_go(id,param1,param2,param3,"celltype");
            }
        };
    };
    const eventHandlerKegg = function(name) {
        return function() {
            var param1 = $("#select-celltype-kegg option:selected").val();
            var param2 = $("#select-celltypeAdj-kegg option:selected").val();
            if (param1!=null&&param2!=null){
                pathway_kegg(id,param1,param2,"celltype");
            }
        };
    };
    $.ajax({
        url: "getCelltypeBySample",
        type: "GET",
        dataType: "json",
        async: false,
        data: {id: id, type: "celltype"},
        success: function(data) {
            /*-------------GO--------------*/
            const $selectGo = $('#select-celltype-go').selectize({
                valueField: 'name',
                labelField: 'name',
                searchField: 'name',
                options: data.data,
                create: false,
                onChange: eventHandlerGo('onChange')
            });
            const controlGo = $selectGo[0].selectize;
            controlGo.setValue(data.data[0].name);

            const celltypeGoOntData = [{name:"BP"},{name: "CC"},{name: "MF"}];
            const $selectGoOnt = $('#select-celltype-ontology').selectize({
                valueField: 'name',
                labelField: 'name',
                searchField: 'name',
                options: celltypeGoOntData,
                create: false,
                onChange: eventHandlerGo('onChange')
            });
            const controlGoOnt = $selectGoOnt[0].selectize;
            controlGoOnt.setValue(celltypeGoOntData[0].name);

            const celltypeGoAdjData = [{name:"0.005"},{name: "0.01"},{name: "0.05"},{name: "1"}];
            const $selectGoAdj = $('#select-celltypeAdj-go').selectize({
                valueField: 'name',
                labelField: 'name',
                searchField: 'name',
                options: celltypeGoAdjData,
                create: false,
                onChange: eventHandlerGo('onChange')
            });
            const controlGoAdj = $selectGoAdj[0].selectize;
            controlGoAdj.setValue(celltypeGoAdjData[0].name);

            /*-------------KEGG--------------*/
            const $selectKegg = $('#select-celltype-kegg').selectize({
                valueField: 'name',
                labelField: 'name',
                searchField: 'name',
                options: data.data,
                create: false,
                onChange: eventHandlerKegg('onChange')
            });
            const controlKegg = $selectKegg[0].selectize;
            controlKegg.setValue(data.data[0].name);

            const celltypeKeggAdjData = [{name:"0.005"},{name: "0.01"},{name: "0.05"},{name: "1"}];
            const $selectKeggAdj = $('#select-celltypeAdj-kegg').selectize({
                valueField: 'name',
                labelField: 'name',
                searchField: 'name',
                options: celltypeKeggAdjData,
                create: false,
                onChange: eventHandlerKegg('onChange')
            });
            const controlKeggAdj = $selectKeggAdj[0].selectize;
            controlKeggAdj.setValue(celltypeKeggAdjData[0].name);
        }
    });
}
function pathway_go_atac(id,param1,param2,param3,type) {
    var chartDom = document.getElementById('go'+type+'Img');
    var myChart = echarts.init(chartDom, null, {renderer: 'svg'});
    myChart.showLoading();
    $.ajax({
        url: "getPathwayGoAtac",
        type: "get",
        data: {"id":id, "param1":param1, "param2": param2, "param3": param3, "type": type},
        async: true,
        success: function (res) {
            if (res.data.length<1){
                myChart.hideLoading();
                myChart.clear();
            }else {
                var option = pathway_go_option(res.data);
                myChart.hideLoading();
                option && myChart.setOption(option);
            }
        },
        dataType: "json"
    });
}
function toCelltypeEnrichAtac(id) {
    const eventHandlerGo = function(name) {
        return function() {
            var param1 = $("#select-celltype-go option:selected").val();
            var param2 = $("#select-celltype-ontology option:selected").val();
            var param3 = $("#select-celltypeAdj-go option:selected").val();
            if (param1!=null&&param2!=null&&param3!=null){
                pathway_go_atac(id,param1,param2,param3,"celltype");
            }
        };
    };
    const eventHandlerKegg = function(name) {
        return function() {
            var param1 = $("#select-celltype-kegg option:selected").val();
            var param2 = $("#select-celltypeAdj-kegg option:selected").val();
            if (param1!=null&&param2!=null){
                pathway_kegg_atac(id,param1,param2,"celltype");
            }
        };
    };
    $.ajax({
        url: "getCelltypeBySampleAtac",
        type: "GET",
        dataType: "json",
        async: false,
        data: {id: id, type: "celltype"},
        success: function(data) {
            /*-------------GO--------------*/
            const $selectGo = $('#select-celltype-go').selectize({
                valueField: 'name',
                labelField: 'name',
                searchField: 'name',
                options: data.data,
                create: false,
                onChange: eventHandlerGo('onChange')
            });
            const controlGo = $selectGo[0].selectize;
            controlGo.setValue(data.data[0].name);

            const celltypeGoOntData = [{name:"BP"},{name: "CC"},{name: "MF"}];
            const $selectGoOnt = $('#select-celltype-ontology').selectize({
                valueField: 'name',
                labelField: 'name',
                searchField: 'name',
                options: celltypeGoOntData,
                create: false,
                onChange: eventHandlerGo('onChange')
            });
            const controlGoOnt = $selectGoOnt[0].selectize;
            controlGoOnt.setValue(celltypeGoOntData[0].name);

            const celltypeGoAdjData = [{name:"0.005"},{name: "0.01"},{name: "0.05"},{name: "1"}];
            const $selectGoAdj = $('#select-celltypeAdj-go').selectize({
                valueField: 'name',
                labelField: 'name',
                searchField: 'name',
                options: celltypeGoAdjData,
                create: false,
                onChange: eventHandlerGo('onChange')
            });
            const controlGoAdj = $selectGoAdj[0].selectize;
            controlGoAdj.setValue(celltypeGoAdjData[0].name);

            /*-------------KEGG--------------*/
            const $selectKegg = $('#select-celltype-kegg').selectize({
                valueField: 'name',
                labelField: 'name',
                searchField: 'name',
                options: data.data,
                create: false,
                onChange: eventHandlerKegg('onChange')
            });
            const controlKegg = $selectKegg[0].selectize;
            controlKegg.setValue(data.data[0].name);

            const celltypeKeggAdjData = [{name:"0.005"},{name: "0.01"},{name: "0.05"},{name: "1"}];
            const $selectKeggAdj = $('#select-celltypeAdj-kegg').selectize({
                valueField: 'name',
                labelField: 'name',
                searchField: 'name',
                options: celltypeKeggAdjData,
                create: false,
                onChange: eventHandlerKegg('onChange')
            });
            const controlKeggAdj = $selectKeggAdj[0].selectize;
            controlKeggAdj.setValue(celltypeKeggAdjData[0].name);
        }
    });
}

function toClusterEnrich(id) {
    const eventHandlerGo = function(name) {
        return function() {
            var param1 = $("#select-cluster-go option:selected").val();
            var param2 = $("#select-cluster-ontology option:selected").val();
            var param3 = $("#select-clusterAdj-go option:selected").val();
            if (param1!=null&&param2!=null&&param3!=null){
                pathway_go(id,param1,param2,param3,"cluster");
            }
        };
    };
    const eventHandlerKegg = function(name) {
        return function() {
            var param1 = $("#select-cluster-kegg option:selected").val();
            var param2 = $("#select-clusterAdj-kegg option:selected").val();
            if (param1!=null&&param2!=null){
                pathway_kegg(id,param1,param2,"cluster");
            }
        };
    };
    $.ajax({
        url: "getCelltypeBySample",
        type: "GET",
        dataType: "json",
        async: true,
        data: {id: id, type: "cluster"},
        success: function(data) {
            /*-------------GO------------*/
            const $selectGo = $('#select-cluster-go').selectize({
                valueField: 'name',
                labelField: 'name',
                searchField: 'name',
                options: data.data,
                create: false,
                onChange: eventHandlerGo('onChange')
            });
            const controlGo = $selectGo[0].selectize;
            controlGo.setValue(data.data[0].name);

            const clusterGoOntData = [{name:"BP"},{name: "CC"},{name: "MF"}];
            const $selectGoOnt = $('#select-cluster-ontology').selectize({
                valueField: 'name',
                labelField: 'name',
                searchField: 'name',
                options: clusterGoOntData,
                create: false,
                onChange: eventHandlerGo('onChange')
            });
            const controlGoOnt = $selectGoOnt[0].selectize;
            controlGoOnt.setValue(clusterGoOntData[0].name);

            const clusterGoAdjData = [{name:"0.005"},{name: "0.01"},{name: "0.05"},{name: "1"}];
            const $selectGoAdj = $('#select-clusterAdj-go').selectize({
                valueField: 'name',
                labelField: 'name',
                searchField: 'name',
                options: clusterGoAdjData,
                create: false,
                onChange: eventHandlerGo('onChange')
            });
            const controlGoAdj = $selectGoAdj[0].selectize;
            controlGoAdj.setValue(clusterGoAdjData[0].name);

            /*-------------KEGG------------*/
            const $selectKegg = $('#select-cluster-kegg').selectize({
                valueField: 'name',
                labelField: 'name',
                searchField: 'name',
                options: data.data,
                create: false,
                onChange: eventHandlerKegg('onChange')
            });
            const controlKegg = $selectKegg[0].selectize;
            controlKegg.setValue(data.data[0].name);

            const clusterKeggAdjData = [{name:"0.005"},{name: "0.01"},{name: "0.05"},{name: "1"}];
            const $selectKeggAdj = $('#select-clusterAdj-kegg').selectize({
                valueField: 'name',
                labelField: 'name',
                searchField: 'name',
                options: clusterKeggAdjData,
                create: false,
                onChange: eventHandlerKegg('onChange')
            });
            const controlKeggAdj = $selectKeggAdj[0].selectize;
            controlKeggAdj.setValue(clusterKeggAdjData[0].name);
        }
    });
}

/*
* 绘制细胞类型互作图
* */
function cellInteraction(myChart, graph) {
    graph.nodes.forEach(function (node) {
        node.label = {
            show: node.symbolSize > 2
        };
    });
    const option = {
        legend: [
            {
                left: 'center',
                bottom: 10,
                data: graph.categories.map(function (a) {
                    return a.name;
                })
            }
        ],
        toolbox: toolbox,
        animationDurationUpdate: 1500,
        animationEasingUpdate: 'quinticInOut',
        series: [
            {
                name: 'cell-cell interaction',
                type: 'graph',
                layout: 'circular',
                top: '25%',
                left: '25%',
                right: '25%',
                bottom: '30%',
                circular: {
                    rotateLabel: true
                },
                data: graph.nodes,
                links: graph.links,
                categories: graph.categories,
                roam: true,
                label: {
                    position: 'right',
                    formatter: '{b}'
                },
                lineStyle: {
                    color: 'source',
                    curveness: 0.2,
                    opacity: 0.5
                }
            }
        ]
    };
    myChart.hideLoading();
    myChart.setOption(option);
}
function toCellchat(id) {
    const containerId = "cellChat";
    const chartDom = document.getElementById(containerId);
    const myChart = echarts.init(chartDom, null, {renderer: 'svg'});
    myChart.showLoading();
    function showCellInteraction(param) {
        $.ajax({
            url: "getNodeCellInteraction",
            type: "GET",
            dataType: "json",
            async: true,
            data: {id: id, type: param},
            success: function(data) {
                cellInteraction(myChart, data)
            }
        });
    }
    const eventHandlerCellchat = function() {  /!*细胞互作分组下拉列表*!/
        return function() {
            param = arguments[0];
            showCellInteraction(param)
        };
    };
    const cellchatData = [{name:"count"},{name: "weight"}];
    const $selectCellchat = $('#select-cellchat').selectize({
        valueField: 'name',
        labelField: 'name',
        searchField: 'name',
        options: cellchatData,
        create: false,
        onChange: eventHandlerCellchat('onChange')
    });
    const controlCellchat = $selectCellchat[0].selectize;
    controlCellchat.setValue(cellchatData[0].name);
}
function toCellchatTab(id){
    var $containerid = $('#toCellchatTab');
    $containerid.DataTable({
        ajax: {
            url: "toCellchatTab",
            type: "GET",
            async: true,
            data: {"id":id}
        },
        bProcessing : true,
        serverSide: true,
        searching: true,
        paging: true,
        order: [],
        ordering: true,
        scrollX: true,
        lengthMenu: [[10, 20, 30, 100], [10, 20, 30, 100]],
        destroy: true,
        columns: [
            {
                "data": "source",
                className: "wordBreak"
            },
            {
                "data": "target",
                className: "wordBreak"
            },
            {"data": "ligand"},
            {"data": "receptor"},
            {
                "data": "prob",
                render: function (data, type, row, meta) {
                    return Math.round(data * 10000) / 10000;
                }
            },
            {"data": "pval"},
            {"data": "interaction_name"},
            {"data": "interaction_name_2"},
            {"data": "pathway_name"},
            {
                "data": "annotation",
                className: "wordBreak"
            },
            {
                "data": "evidence",
                className: "wordBreak"
            }
        ],
        oLanguage: olanguage
    });
}
function toCiceroTab(id){
    var $containerid = $('#toCiceroTab');
    $containerid.DataTable({
        ajax: {
            url: "toCiceroTab",
            type: "GET",
            async: true,
            data: {"id":id}
        },
        bProcessing : true,
        serverSide: true,
        searching: true,
        paging: true,
        order: [],
        ordering: true,
        scrollX: true,
        lengthMenu: [[10, 20, 30, 100], [10, 20, 30, 100]],
        destroy: true,
        columns: [
            {
                "data": "peak1",
                className: "wordBreak"
            },
            {
                "data": "peak1_gene",
                className: "wordBreak"
            },
            {"data": "peak2"},
            {"data": "peak2_gene"},
            {"data": "coaccess"}
        ],
        oLanguage: olanguage
    });
}
function toDiffTfMotifTab(id){
    var $containerid = $('#toDiffTfMotifTab');
    $containerid.DataTable({
        ajax: {
            url: "toDiffTfMotifTab",
            type: "GET",
            async: true,
            data: {"id":id}
        },
        bProcessing : true,
        serverSide: true,
        searching: true,
        paging: true,
        order: [],
        ordering: true,
        scrollX: true,
        lengthMenu: [[10, 20, 30, 100], [10, 20, 30, 100]],
        destroy: true,
        columns: [
            {
                "data": "feature",
                className: "wordBreak"
            },
            {
                "data": "cluster",
                className: "wordBreak"
            },
            {"data": "mean1"},
            {"data": "mean2"},
            {"data": "pvalue"},
            {"data": "padj"},
            {"data": "tfname"}
        ],
        oLanguage: olanguage
    });
}
function getWgcnaTab(container,id) {
    var $containerid = $('#' + container);
    $containerid.DataTable({
        ajax: {
            url: "getWgcnaTab",
            type: "GET",
            async: true,
            data: {"id":id}
        },
        bProcessing : true,
        serverSide: true,
        searching: true,
        paging: true,
        order: [],
        ordering: true,
        scrollX: true,
        lengthMenu: [[10, 20, 30, 100], [10, 20, 30, 100]],
        destroy: true,
        columns: [
            {"data": "umap1"},
            {"data": "umap1"},
            {"data": "gene"},
            {"data": "module"},
            {"data": "color"},
            {"data": "hub"},
            {"data": "kme"}
        ],
        oLanguage: olanguage
    });
}

function getWgcnaScoreTab(container,id) {
    var $containerid = $('#' + container);
    $containerid.DataTable({
        ajax: {
            url: "getWgcnaScoreTab",
            type: "GET",
            async: true,
            data: {"id":id}
        },
        bProcessing : true,
        serverSide: true,
        searching: true,
        paging: true,
        order: [],
        ordering: true,
        scrollX: true,
        lengthMenu: [[10, 20, 30, 100], [10, 20, 30, 100]],
        destroy: true,
        columns: [
            {"data": "source"},
            {"data": "target"},
            {"data": "weight"}
        ],
        oLanguage: olanguage
    });
}

function toDotMarker(id) {
    const containerId = "dotmarker";
    const chartDom = document.getElementById(containerId);
    const myChart = echarts.init(chartDom, null, {renderer: 'svg'});
    myChart.showLoading();
    $.ajax({
        url: "getDotMarker",
        type: "GET",
        dataType: "json",
        async: true,
        data: {id: id},
        success: function(res) {
            let data = res.data
            const maxPctExp = res.maxPctExp;
            const maxAvgExp = res.maxAvgExp;
            const schema = [
                { name: 'Cell Type', index: 0, text: 'Cell Type' },
                { name: 'Gene Symbol', index: 1, text: 'Gene Symbol' },
                { name: 'Cell Count', index: 2, text: 'Cell Count Number' },
                { name: 'Expressed in cells', index: 3, text: 'Expressed in Cells' },
                { name: 'Gene Expression', index: 4, text: 'Gene Expression Avg' }
            ];

            const itemStyle = {
                opacity: 1,
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowOffsetY: 0,
                shadowColor: 'rgba(0,0,0,0.3)'
            };

            let option = {
                toolbox : {
                    feature: {
                        brush: {
                            type: ['rect', 'polygon', 'clear']
                        },
                        saveAsImage: {title: "save"}
                    }
                },
                color: ['#fec42c'],
                legend: {
                    show: false,
                    top: 10,
                    data: ['Gene Expression'],
                    textStyle: {
                        fontSize: 16
                    }
                },
                grid: {
                    left: '10%',
                    right: '2%',
                    top: '5%',
                    bottom: '30%'
                },
                tooltip: {
                    trigger: 'item',
                    padding: [10, 15, 10, 15],
                    backgroundColor: 'rgba(255,255,255,0.95)',
                    borderWidth: 1,
                    borderColor: '#ddd',
                    textStyle: {
                        color: '#333'
                    },
                    extraCssText: 'min-width: 280px; max-width: 500px; box-shadow: 0 2px 8px rgba(0,0,0,0.15);',
                    formatter: function (params) {
                        var value = params.value;
                        // 使用表格布局，避免浮动问题
                        return '<div style="width: 100%;">' +
                            '<div style="display: flex; justify-content: space-between; border-bottom: 1px solid #eee; padding-bottom: 8px; margin-bottom: 8px;">' +
                            '<span style="color: #0a0a0a;">' + schema[0].text + ': </span>' +
                            '<span style="color: #a3a3a3;">' + value[0] + '</span>' +
                            '</div>' +

                            '<div style="display: flex; justify-content: space-between; margin: 4px 0;">' +
                            '<span style="color: #0a0a0a;">' + schema[1].text + ': </span>' +
                            '<span style="color: #a3a3a3;">' + value[1] + '</span>' +
                            '</div>' +

                            '<div style="display: flex; justify-content: space-between; margin: 4px 0;">' +
                            '<span style="color: #0a0a0a;">' + schema[3].text + ':&nbsp;&nbsp;&nbsp;&nbsp; </span>' +
                            '<span style="color: #a3a3a3;">' + value[5] + '% (' +  value[3] + ' of ' + value[2] + ' cells)</span>' +
                            '</div>' +

                            '<div style="display: flex; justify-content: space-between; margin: 4px 0;">' +
                            '<span style="color: #0a0a0a;">' + schema[2].text + ': </span>' +
                            '<span style="color: #a3a3a3;">' + value[2] + '</span>' +
                            '</div>' +

                            '<div style="height: 1px; background: #eee; margin: 8px 0;"></div>' +

                            '<div style="display: flex; justify-content: space-between; margin: 4px 0;">' +
                            '<span style="color: #0a0a0a;">Tisue Composition: </span>' +
                            '<span style="color: #a3a3a3;">' + value[5] + '%</span>' +
                            '</div>' +
                            '</div>';
                    },
                    axisPointer: {
                        type: 'cross'
                    }
                },
                xAxis: {
                    show: true,
                    type: 'category',
                    name: '',
                    nameGap: 16,
                    nameTextStyle: {
                        fontSize: 16
                    },
                    axisPointer: {
                        label: {
                            show: false // 不显示x轴标签
                        }
                    },
                    axisLabel: {
                        rotate: 45
                    },
                    splitLine: {
                        show: false
                    }
                },
                yAxis: {
                    show: true,
                    type: 'category',
                    name: '',
                    nameLocation: 'end',
                    nameGap: 20,
                    nameTextStyle: {
                        fontSize: 16
                    },
                    axisPointer: {
                        label: {
                            show: true // 不显示y轴标签
                        }
                    },
                    splitLine: {
                        show: true
                    }
                },
                visualMap: [
                    {
                        orient: 'horizontal',
                        show: true,
                        left: '50%',
                        bottom: -10,  // 改为底部
                        dimension: 5,
                        min: 0,
                        max: maxPctExp,
                        itemWidth: 30,
                        itemHeight: 120,
                        calculable: true,
                        precision: 0.1,
                        text: ['Exp in Cells'],
                        textGap: 10,
                        inRange: {
                            symbolSize: [0, 10]
                        },
                        outOfRange: {
                            symbolSize: [0, 10],  // 将outOfRange的点大小设为0
                            color: ['rgba(255,255,255,0)']  // 颜色设为完全透明
                        },
                        controller: {
                            inRange: {
                                color: ['#fec42c']
                            },
                            outOfRange: {
                                color: ['#999']
                            }
                        }
                    },
                    {
                        orient: 'horizontal',
                        show: true,
                        left: '10%',
                        bottom: 5,
                        dimension: 4,
                        min: 0,
                        max: maxAvgExp,
                        itemHeight: 120,
                        text: ['Gene Exp'],
                        textGap: 10,
                        inRange: {
                            color: ['#2F93C8', '#AEC48F', '#FFDB5C', '#F98862']
                        },
                    }
                ],
                series: [
                    {
                        name: '',
                        type: 'scatter',
                        itemStyle: itemStyle,
                        data: data
                    }
                ]
            };
            myChart.hideLoading();
            myChart.setOption(option);
        }
    });
}

/*
* echarts 热图
* */
function heatmap(myChart,data) {
    const option = {
        title: {
            text: 'heatmap graph to show gene expression',
            top: 'top',
            left: 'left'
        },
        tooltip: {
            position: 'top'
        },
        grid: {
            height: '50%',
            top: '10%'
        },
        toolbox: toolbox,
        xAxis: {
            type: 'category',
            data: data.hours,
            splitArea: {
                show: true
            }
        },
        yAxis: {
            type: 'category',
            data: data.days,
            splitArea: {
                show: true
            }
        },
        visualMap: {
            min: 0,
            max: 10,
            calculable: true,
            orient: 'horizontal',
            left: 'center',
            bottom: '5%'
        },
        series: [
            {
                name: 'Punch Card',
                type: 'heatmap',
                data: data.data,
                label: {
                    show: true
                },
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    myChart.hideLoading();
    myChart.setOption(option);
}


/*
* box plot
* */
function boxplot(myChart,data) {
    const option = {
        title: [
            {
                text: 'Michelson-Morley Experiment',
                left: 'left'
            },
            {
                text: 'upper: Q3 + 1.5 * IQR \nlower: Q1 - 1.5 * IQR',
                borderColor: '#999',
                borderWidth: 1,
                textStyle: {
                    fontWeight: 'normal',
                    fontSize: 14,
                    lineHeight: 20
                },
                left: '10%',
                top: '90%'
            }
        ],
        dataset: [
            {
                // prettier-ignore
                source: data
            },
            {
                transform: {
                    type: 'boxplot',
                    config: { itemNameFormatter: 'expr {value}' }
                }
            },
            {
                fromDatasetIndex: 1,
                fromTransformResult: 1
            }
        ],
        tooltip: {
            trigger: 'item',
            axisPointer: {
                type: 'shadow'
            }
        },
        grid: {
            left: '10%',
            right: '10%',
            bottom: '15%'
        },
        toolbox: toolbox,
        xAxis: {
            type: 'category',
            boundaryGap: true,
            nameGap: 30,
            splitArea: {
                show: false
            },
            splitLine: {
                show: false
            }
        },
        yAxis: {
            type: 'value',
            name: 'km/s minus 299,000',
            splitArea: {
                show: true
            }
        },
        series: [
            {
                name: 'boxplot',
                type: 'boxplot',
                datasetIndex: 1
            },
            {
                name: 'outlier',
                type: 'scatter',
                datasetIndex: 2
            }
        ]
    };
    myChart.hideLoading();
    myChart.setOption(option);
}
function datatablesShow() {
    $('a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
        // 当切换tab时，强制重新计算列宽
        $.fn.dataTable.tables({
            visible: true,
            api: true
        }).columns.adjust();
    });
    /* datatables配置结束 */
}

function lengedOpen() {
    $("#eye-open").hide()
    $("#eye-close").show()
    $("#legend").show()
}
function lengedClose() {
    $("#eye-open").show()
    $("#eye-close").hide()
    $("#legend").hide()
}



async function toRnaBoxPlot(id, container, selectContainer) { //使用异步函数，这个方法仅获取一次位置信息
    let valueData = null;
    let currentDataType = ''; // 当前基因
    const chartDom = document.getElementById("toRnaBox");
    const myChart = echarts.init(chartDom, null, {renderer: "svg"});
    // 根据选择的基因生成值数据（一维数组）
    async function getRnaGroup(id,geneName) {
        try {
            const response = await $.ajax({
                url: "getRnaGroup",
                type: "GET",
                dataType: "json",
                data: {"id": id, "gene": geneName}
            });
            return {
                data: response.data
            };
        } catch (error) {
            console.error('Error fetching value data:', error);
            throw error;
        }
    }
    // 初始化函数
    async function init() {
        try {
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
                    resData = await getRnaGroup(id, geneName);

                    // 修改：保存每个 GSE 对应的完整数据（包含 gsm_id 和 expr_value）
                    const groupMap = {};
                    resData.data.forEach(item => {
                        const gse = item.gse;
                        if (!groupMap[gse]) {
                            groupMap[gse] = [];
                        }
                        // 保存完整数据对象，而不仅仅是值
                        groupMap[gse].push({
                            gsm_id: item.gsm_id,
                            expr_value: parseFloat(item.expr_value)
                        });
                    });

                    const gseNames = Object.keys(groupMap);
                    // 提取数值用于绘制箱线图
                    const boxplotSource = gseNames.map(gse => {
                        return groupMap[gse].map(item => item.expr_value);
                    });

                    // 保存完整数据供 tooltip 使用
                    const fullData = groupMap;

                    // 已经获取数据，现在开始绘制 myChart 插件 Box 图。
                    const option = {
                        dataset: [
                            {source: boxplotSource},
                            {
                                transform: {
                                    type: 'boxplot',
                                    config: {
                                        itemNameFormatter: function (params) {
                                            return gseNames[params.value];
                                        }
                                    }
                                }
                            },
                            {fromDatasetIndex: 1, fromTransformResult: 1}
                        ],
                        dataZoom: [
                            {type: 'inside', start: 0, end: 100},
                            {show: true, type: 'slider', top: '90%', xAxisIndex: [0], start: 0, end: 100}
                        ],
                        tooltip: {
                            trigger: 'item',
                            axisPointer: {type: 'shadow'},
                            formatter: function(params) {
                                if (params.componentType === 'series') {
                                    const seriesType = params.seriesType;
                                    const dataIndex = params.dataIndex;
                                    const gseName = gseNames[dataIndex];

                                    if (seriesType === 'boxplot') {
                                        const stats = params.data;
                                        const samples = fullData[gseName] || [];

                                        // 将样本按每3个一组排列
                                        let sampleInfo = '';
                                        for (let i = 0; i < samples.length; i += 3) {
                                            const group = samples.slice(i, i + 3);
                                            const groupHtml = group.map(s =>
                                                `${s.gsm_id}: ${s.expr_value.toFixed(4)}`
                                            ).join('&nbsp;&nbsp;|&nbsp;&nbsp;');
                                            sampleInfo += groupHtml + '<br/>';
                                        }

                                        return `<strong>${gseName}</strong><br/><br/>
                                                Max: ${stats[5].toFixed(4)}<br/>
                                                Q3: ${stats[4].toFixed(4)}<br/>
                                                Median: ${stats[3].toFixed(4)}<br/>
                                                Q1: ${stats[2].toFixed(4)}<br/>
                                                Min: ${stats[1].toFixed(4)}<br/>
                                                Count: ${stats[0]}<br/><br/>
                                                <strong>Sample Details (GSM ID → Value):</strong><br/>
                                                ${sampleInfo}`;
                                    } else if (seriesType === 'scatter') {
                                        const outlierData = params.data;
                                        if (Array.isArray(outlierData) && outlierData.length >= 2) {
                                            const value = outlierData[0];
                                            const index = outlierData[1];
                                            const gseName = gseNames[index];
                                            const samples = fullData[gseName] || [];
                                            let matchedSample = null;
                                            let minDiff = Infinity;
                                            samples.forEach(s => {
                                                const diff = Math.abs(s.expr_value - value);
                                                if (diff < minDiff) {
                                                    minDiff = diff;
                                                    matchedSample = s;
                                                }
                                            });

                                            if (matchedSample) {
                                                return `<strong>${gseName}</strong><br/>
                                                        GSM ID: ${matchedSample.gsm_id}<br/>
                                                        Outlier Value: ${typeof value === 'number' ? value.toFixed(4) : value}`;
                                            }
                                        }
                                        // 修复：检查 outlierData[0] 是否为数字
                                        const value = outlierData[0];
                                        return `Outlier: ${typeof value === 'number' ? value.toFixed(4) : value || 'N/A'}`;
                                    }
                                }
                                return params.value;
                            }
                        },
                        toolbox: toolbox,
                        grid: {top: '10%', left: '5%', right: '5%', bottom: '30%'},
                        xAxis: {
                            type: 'category',
                            boundaryGap: true,
                            name: '',
                            nameGap: 0,
                            axisLabel: {rotate: 45},
                            splitArea: {show: false},
                            splitLine: {show: false}
                        },
                        yAxis: {
                            type: 'value',
                            name: 'log2(normalized count + 1)',
                            nameGap: 15,
                            nameTextStyle: {
                                align: 'left'
                            },
                            splitArea: {show: true}
                        },
                        series: [
                            {
                                name: 'Expression',
                                type: 'boxplot',
                                datasetIndex: 1
                            },
                            {
                                name: 'Outlier',
                                type: 'scatter',
                                datasetIndex: 2
                            }
                        ]
                    };
                    myChart.setOption(option, true);
                    // 绘制 myChart 插件 Box 图结束。
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
    init();
}

function getRnaSampleTable(id) {
    $('#rnaTab').DataTable({
        ajax: {
            url: "getRnaSampleTable",
            type: "GET",
            async: true,
            data: {"id": id}
        },
        bProcessing : true,
        serverSide: true,
        searching: true,
        paging: true,
        ordering: true,
        scrollX: true,
        lengthMenu: [[10, 20, 30, 100], [10, 20, 30, 100]],
        destroy: true,
        columns: [
            {
                "data": "gseId",
                "render": function (data, type, row, meta) {
                    return "<a target='_blank' href='https://www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc=" + row.gseId + "'>" + row.gseId + "</a>";
                }
            },
            {
                "data": "gsmId",
                "render": function (data, type, row, meta) {
                    return "<a target='_blank' href='https://www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc=" + row.gsmId + "'>" + row.gsmId + "</a>";
                }
            },
            {"data": "sampleName"},
            {"data": "species"},
            {"data": "tissue"},
            {"data": "age"},
            {"data": "sex"},
            {"data": "treatment"},
            {"data": "disease"},
            {"data": "times"},
            {"data": "genomic"},
            {"data": "strain"},
            {
                "data": "pmid",
                "render": function (data, type, row, meta) {
                    return "<a target='_blank' href='https://pubmed.ncbi.nlm.nih.gov/" + row.pmid + "'>" + row.pmid + "</a>";
                }
            },
            {"data": "journal"},
            {"data": "year"}
        ],
        oLanguage: olanguage
    });
}

function toRnaBubble(id,container) {
    $.ajax({
        url: "getRnaBubbleData",
        type: "GET",
        dataType: "json",
        async: true,
        data: {id: id,gene:"GLOD4"},
        success: function(res) {
            let data = res.data
            console.log(data)
        }
    });
}
async function toRnaBubblePlot(id, container, selectContainer) {
    let currentDataType = ''; // 当前基因
    const chartDom = document.getElementById(container);
    const myChart = echarts.init(chartDom, null, {renderer: "svg"});
    // 根据选择的基因生成值数据（一维数组）
    async function getRnaBubbleData(id,geneName) {
        try {
            const response = await $.ajax({
                url: "getRnaBubbleData",
                type: "GET",
                dataType: "json",
                data: {"id": id, "gene": geneName}
            });
            return {
                data: response.data
            };
        } catch (error) {
            console.error('Error fetching value data:', error);
            throw error;
        }
    }
    // 初始化函数
    async function init() {
        try {
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
                    resData = await getRnaBubbleData(id,geneName);
                    console.log("----------处理数据为绘图格式-----------")
                    rawData = resData.data
                    function isValidNumber(x) {
                        return x !== null && x !== undefined && x !== "" && !isNaN(Number(x)) && isFinite(Number(x));
                    }
                    function safeNegLog10(padj) {
                        let x = Number(padj);
                        if (x <= 0) x = 1e-300;
                        return -Math.log10(x);
                    }
                    const validData = rawData.filter(d => isValidNumber(d.log2fc) && isValidNumber(d.padj));
                    const contrasts = [...new Set(validData.map(d => `${d.gse_id}: ${d.case_group} vs ${d.ctrl_group}`))];
                    const genes = [...new Set(validData.map(d => d.gene))];
                    const plotData = validData.map(d => {
                        const contrastLabel = `${d.gse_id}: ${d.case_group} vs ${d.ctrl_group}`;
                        const xIndex = contrasts.indexOf(contrastLabel);
                        const yIndex = genes.indexOf(d.gene);
                        const log2fc = Number(d.log2fc);
                        const padj = Number(d.padj);
                        const negLog10Padj = safeNegLog10(padj);
                        return {
                            name: d.gene,
                            value: [xIndex, yIndex, log2fc, negLog10Padj],
                            gene: d.gene,
                            gse_id: d.gse_id,
                            case_group: d.case_group,
                            ctrl_group: d.ctrl_group,
                            log2fc: log2fc,
                            pvalue: isValidNumber(d.pvalue) ? Number(d.pvalue) : null,
                            padj: padj,
                            negLog10Padj: negLog10Padj
                        };
                    });
                    const option = {
                        tooltip: {
                            trigger: "item",
                            confine: true,
                            formatter: function (params) {
                                const d = params.data;
                                return `<b>${d.gene}</b><br/>
                                        ${d.gse_id}<br/>
                                        ${d.case_group} vs ${d.ctrl_group}<br/>
                                        log2FC: ${d.log2fc.toFixed(4)}<br/>
                                        pvalue: ${d.pvalue === null ? "NA" : d.pvalue}<br/>
                                        padj: ${d.padj.toFixed(4)}<br/>
                                        -log10(padj): ${d.negLog10Padj.toFixed(4)}`;
                            }
                        },
                        toolbox: toolbox,
                        grid: {left: 60, right: 100, top: 50, bottom: 320},
                        xAxis: {
                            type: "category",
                            name: "Comparison",
                            data: contrasts,
                            axisLabel: {interval: 0, rotate: 90},
                            splitLine: {show: true}
                        },
                        yAxis: {
                            type: "category",
                            name: "Bubble color represents log2FC, and bubble size represents -log10(Padj).",
                            data: genes,
                            nameTextStyle: {
                                align: 'left'
                            },
                            splitLine: {show: true}
                        },
                        visualMap: {
                            type: "continuous",
                            min: -3,
                            max: 3,
                            dimension: 2,
                            right: 20,
                            top: "middle",
                            calculable: true,
                            inRange: {color: ["#2166ac", "#67a9cf", "#f7f7f7", "#ef8a62", "#b2182b"]}
                        },
                        dataZoom: [
                            {type: "slider", xAxisIndex: 0, bottom: 10},
                            {type: "inside", xAxisIndex: 0}
                        ],
                        series: [
                            {
                                type: "scatter",
                                data: plotData,
                                symbolSize: function (val) {
                                    return Math.max(5, Math.min(val[3] * 3, 25));
                                },
                                itemStyle: {borderColor: "#333", borderWidth: 0.5, opacity: 0.85}
                            }
                        ]
                    };
                    myChart.setOption(option, true);
                    console.log("---------绘图结束------------")
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
    init();
}
function getRnaSampleGroupTable(id) {
    $('#rnaTabGroup').DataTable({
        ajax: {
            url: "getRnaSampleGroupTable",
            type: "GET",
            async: true,
            data: {"id": id}
        },
        bProcessing : true,
        serverSide: true,
        searching: true,
        paging: true,
        ordering: true,
        scrollX: true,
        lengthMenu: [[10, 20, 30, 100], [10, 20, 30, 100]],
        destroy: true,
        columns: [
            {
                "data": "gse_id",
                "render": function (data, type, row, meta) {
                    return "<a target='_blank' href='https://www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc=" + row.gse_id + "'>" + row.gse_id + "</a>";
                }
            },
            {"data": "case_group"},
            {"data": "ctrl_group"},
            {"data": "sample_n"},
            {"data": "sample_name"},
            {"data": "times"}
        ],
        oLanguage: olanguage
    });
}