let dataArr = [];
let geneArr = [];
let celltypeArr = [];
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
// 获取DOM元素
const genesContainer = document.getElementById('genesContainer');
const geneInput = document.getElementById('geneInput');
const notification = document.getElementById('notification');
// 初始化页面
function createOption(data,colorIndex,maxPctExp,maxAvgExp) {

    const schema = [
        { name: 'Gene Symbol', index: 0, text: 'Gene Symbol' },
        { name: 'Cell Type', index: 1, text: 'Cell Type' },
        { name: 'Gene Expression', index: 2, text: 'Gene Expression Avg' },
        { name: 'Expressed in cells', index: 3, text: 'Expressed in Cells' },
        { name: 'Cell Count', index: 4, text: 'Cell Count Number' },
        { name: 'Gene Expression, Scaled', index: 5, text: 'Gene Expression, Scaled' }
    ];

    const itemStyle = {
        opacity: 1,
        shadowBlur: 10,
        shadowOffsetX: 0,
        shadowOffsetY: 0,
        shadowColor: 'rgba(0,0,0,0.3)'
    };

    const option = {
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
            left: '0%',
            right: '0%',
            top: '0%',
            bottom: '0%'
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
                    '<span style="color: #0a0a0a;">' + schema[3].text + ':&nbsp;&nbsp;&nbsp;&nbsp; </span>' +
                    '<span style="color: #a3a3a3;">' + value[3] + '% (' +  Math.round(parseFloat(value[3])/100 * value[4])+ ' of ' + value[4] + ' cells)</span>' +
                    '</div>' +

                    '<div style="display: flex; justify-content: space-between; margin: 4px 0;">' +
                    '<span style="color: #0a0a0a;">' + schema[2].text + ': </span>' +
                    '<span style="color: #a3a3a3;">' + value[2] + '</span>' +
                    '</div>' +

                    '<div style="display: flex; justify-content: space-between; margin: 4px 0;">' +
                    '<span style="color: #0a0a0a;">' + schema[5].text + ': </span>' +
                    '<span style="color: #a3a3a3;">' + value[5] + '</span>' +
                    '</div>' +

                    '<div style="height: 1px; background: #eee; margin: 8px 0;"></div>' +

                    '<div style="display: flex; justify-content: space-between; margin: 4px 0;">' +
                    '<span style="color: #0a0a0a;">' + schema[1].text + ': </span>' +
                    '<span style="color: #a3a3a3;">' + value[1] + '</span>' +
                    '</div>' +

                    '<div style="display: flex; justify-content: space-between; margin: 4px 0;">' +
                    '<span style="color: #0a0a0a;">Tisue Composition: </span>' +
                    '<span style="color: #a3a3a3;">' + value[3] + '%</span>' +
                    '</div>' +
                    '</div>';
            },
            axisPointer: {
                type: 'cross'
            }
        },
        xAxis: {
            show: false,
            type: 'category',
            name: 'Gene',
            nameGap: 16,
            nameTextStyle: {
                fontSize: 16
            },
            axisPointer: {
                label: {
                    show: false // 不显示x轴标签
                }
            },
            splitLine: {
                show: false
            }
        },
        yAxis: {
            show: false,
            type: 'category',
            name: 'Expressed',
            nameLocation: 'end',
            nameGap: 20,
            nameTextStyle: {
                fontSize: 16
            },
            axisPointer: {
                label: {
                    show: false // 不显示y轴标签
                }
            },
            splitLine: {
                show: false
            }
        },
        visualMap: [
            {
                show: false,
                left: 'right',
                top: '10%',
                dimension: 3,
                min: 0,
                max: maxPctExp,
                itemWidth: 30,
                itemHeight: 120,
                calculable: true,
                precision: 0.1,
                text: ['Expressed in Cells'],
                textGap: 10,
                inRange: {
                    symbolSize: [1, 10]
                },
                outOfRange: {
                    symbolSize: 0,  // 将outOfRange的点大小设为0
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
                show: false,
                left: 'right',
                bottom: '5%',
                dimension: colorIndex,//2
                min: 0,
                max: maxAvgExp,
                itemHeight: 120,
                text: ['Gene Expression'],
                textGap: 10,
                inRange: {
                    color: ['#2F93C8', '#AEC48F', '#FFDB5C', '#F98862']
                },
            }
        ],
        series: [
            {
                name: 'Gene Expression',
                type: 'scatter',
                itemStyle: itemStyle,
                data: data
            }
        ]
    };
    return option;
}

// 获取DOM元素
const celltypeContainer = document.getElementById('celltypeContainer');

// 显示通知
function showNotification(message, isSuccess = true) {
    notification.textContent = message;
    notification.style.background = isSuccess ? '#38c172' : '#e74c3c';
    notification.classList.add('show');

    setTimeout(() => {
        notification.classList.remove('show');
    }, 2000);
}

// 添加基因符号函数
function addGeneSymbol(symbol, genesContainer) {
    const geneItem = document.createElement('div');
    geneItem.className = 'gene-item';

    const geneElement = document.createElement('div');
    geneElement.className = 'gene-symbol';
    geneElement.textContent = symbol;

    // 添加基因点击事件
    geneElement.addEventListener('click', function() {
        showNotification(`select gene: ${symbol}`);
    });

    // 创建信息图标
    const infoIcon = document.createElement('div');
    infoIcon.className = 'gene-icon';
    infoIcon.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" data-bs-toggle="toast" data-bs-target="#toast-geneDescription" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M3 12a9 9 0 1 0 18 0a9 9 0 0 0 -18 0" /><path d="M12 8v4" /><path d="M12 16h.01" /></svg>';

    // 添加信息图标点击事件
    infoIcon.addEventListener('click', function(e) {
        e.stopPropagation(); // 防止触发基因的点击事件
        showNotification(`show gene ${symbol} information`);
        getGeneDescription($("#id").text(), `${symbol}`);
        $('#toast-geneDescription').addClass('show');
    });

    // 创建删除图标
    const deleteIcon = document.createElement('div');
    deleteIcon.className = 'gene-icon delete-icon';
    deleteIcon.innerHTML = '<svg  xmlns="http://www.w3.org/2000/svg"  width="24"  height="24"  viewBox="0 0 24 24"  fill="none"  stroke="currentColor"  stroke-width="2"  stroke-linecap="round"  stroke-linejoin="round"  class="icon icon-tabler icons-tabler-outline icon-tabler-trash-x"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M4 7h16" /><path d="M5 7l1 12a2 2 0 0 0 2 2h8a2 2 0 0 0 2 -2l1 -12" /><path d="M9 7v-3a1 1 0 0 1 1 -1h4a1 1 0 0 1 1 1v3" /><path d="M10 12l4 4m0 -4l-4 4" /></svg>';

    // 添加删除图标点击事件
    deleteIcon.addEventListener('click', function(e) {
        e.stopPropagation(); // 防止触发基因的点击事件
        geneItem.remove();
        showNotification(`alertly gene: ${symbol}`);
        const singleSelect = $('#select-feature').selectize();
        const singleSelectize = singleSelect[0].selectize;
        singleSelectize.removeItem(`${symbol}`);
    });

    geneItem.appendChild(geneElement);
    geneItem.appendChild(infoIcon);
    geneItem.appendChild(deleteIcon);

    genesContainer.appendChild(geneItem);
}

// 添加细胞类型符号函数
function addCelltype(name, celltypeContainer) {
    const celltypeItem = document.createElement('div');
    celltypeItem.className = 'celltype-item';

    const celltypeElement = document.createElement('label');
    celltypeElement.className = 'celltype-name';
    celltypeElement.textContent = name[0];

    const celltypeCountElement = document.createElement('div');
    celltypeCountElement.className = 'celltype-count';
    celltypeCountElement.className = 'float-end';
    celltypeCountElement.textContent = name[1];

    // 添加细胞类型点击事件
    celltypeItem.addEventListener('click', function() {
        showNotification(`select celltype: ${name}`);
    });

    // 创建信息图标
    celltypeElement.innerHTML = name[0] + '<svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path stroke="none" d="M0 0h24v24H0z" fill="none"/><path d="M3 12a9 9 0 1 0 18 0a9 9 0 0 0 -18 0" /><path d="M12 8v4" /><path d="M12 16h.01" /></svg>';

    // 添加信息图标点击事件
    celltypeItem.addEventListener('click', function(e) {
        e.stopPropagation(); // 防止触发基因的点击事件
        showNotification(`show ${name} information`);
        getDegTable("degTable",$("#id").text(), `${name[0]}`);
        $('#toast-degCelltype').addClass('show');
    });

    celltypeItem.appendChild(celltypeElement);
    celltypeItem.appendChild(celltypeCountElement);
    celltypeContainer.appendChild(celltypeItem);
}

// 初始化函数
function initGenes(initialGenes, genesContainer) {
    genesContainer.innerHTML = '';
    initialGenes.forEach(gene => addGeneSymbol(gene, genesContainer));
}
function initCelltype(initialCelltype, celltypeContainer) {
    celltypeContainer.innerHTML = '';
    // 注意这里为了和echarts生成的图顺序一致，采用倒叙的插入细胞类型方式。
    initialCelltype.slice().reverse().forEach(celltype => addCelltype(celltype, celltypeContainer));
}

function getGeneExpressData(id,genes) {
    $.ajax({
        url: "geneExpressData",
        type: "GET",
        dataType: "json",
        traditional: true,
        async: true,
        data: {id: id, genes: genes},
        success: function(data) {
            geneArr = data.gene;
            celltypeArr = data.celltype;
            dataArr = data.data;
            var maxAvgExp = data.maxAvgExp;
            var maxPctExp = data.maxPctExp;
            var colorIndex = 2; // 颜色值avg_exp所在索引.
            // 使用数组中的第五个索引值(avg exp scaled)作为颜色值。其实不这么设置图的颜色也是相同的，这么做只是为了让图例标记为0，1和鼠标悬停选项卡时展示scaled值。（可有可无的）
            colorIndex = 5;
            maxAvgExp = 1;

            initGenes(geneArr, genesContainer);
            initCelltype(celltypeArr, celltypeContainer);
            var width = geneArr.length*20
            var heidht = celltypeArr.length*20
            const chartDom = document.getElementById('main-chart');
            const myChart = echarts.init(chartDom);
            myChart.resize({width: width+"px", height: heidht+"px"});
            var option = createOption(dataArr, colorIndex, maxPctExp, maxAvgExp)
            option && myChart.setOption(option);
        }
    });
}

// 清空所有基因
$('#button-clear').on('click', function() {
    const singleSelect = $('#select-feature').selectize();
    const singleSelectize = singleSelect[0].selectize;
    singleSelectize.clear();
});
$('#button-setvalue').on('click', function() {
    const singleSelect = $('#select-feature').selectize();
    const singleSelectize = singleSelect[0].selectize;
    singleSelectize.setValue(["Aqp4","Cd4"]);
});



function getGeneDescription(id,gene) {
    $.ajax({
        url: "getGeneDescription",
        type: "GET",
        dataType: "json",
        async: true,
        data: {id: id, gene: gene},
        success: function(res) {
            if (res.data==null){
                $("#geneDescription").text("")
                $("#geneSummary").text("")
                $("#geneSynonyms").text("")
                $("#geneSymbol").text("")
                $("#genedb").text("")
                $('#geneCard').attr('href', "https://www.genecards.org")
                $('#geneId').attr('href', "https://www.ncbi.nlm.nih.gov/gene/")
            }else {
                $("#geneDescription").text(res.data.description)
                $("#geneSummary").text(res.data.summary)
                $("#geneSynonyms").text(res.data.synonyms)
                $("#geneSymbol").text(res.data.symbol)
                $("#genedb").text(res.data.dbxrefs)
                $('#geneCard').attr('href', "https://www.genecards.org/cgi-bin/carddisp.pl?gene=" + res.data.symbol)
                $('#geneId').attr('href', "https://www.ncbi.nlm.nih.gov/gene/" + res.data.geneid)
            }
        }
    });
}

function getDegTable(container,id,celltype) {
    var $containerid = $('#' + container);
    $containerid.DataTable({
        ajax: {
            url: "getDegTableFilter",
            type: "GET",
            async: true,
            data: {"id":id, "celltype":celltype}
        },
        bProcessing : true,
        serverSide: true,
        searching: true,
        paging: true,
        order: false,
        ordering: false,
        scrollX: true,
        lengthMenu: [[10, 20], [10, 20]],
        destroy: true,
        columns: [
            {"data": "gene"},
            // {"data": "celltype"},
            {"data": "p_value"},
            {"data": "avg_log2fc"},
            {"data": "p_value_adj"},
            {"data": "pct1"},
            {"data": "pct2"}
        ],
        oLanguage: olanguage
    });
}
