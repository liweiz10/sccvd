function format(d) {
    // 安全地分割字符串，处理 null 或 undefined 的情况
    const safeSplit = (str, separator = "|") => {
        return str ? str.split(separator) : [];
    };

    let start = '<table class="table table-hover"><thead><tr><th>GSMID</th><th>AGE</th><th>SEX</th><th>TIMES</th><th>TISSUE</th><th>TREATMENT</th><th>SampleName</th></tr></thead><tbody>';
    const end = '</tbody></table>';

    const gsmarr = safeSplit(d.gsmId);
    const agearr = safeSplit(d.age);
    const sexarr = safeSplit(d.sex);
    const timearr = safeSplit(d.times);
    const tissuearr = safeSplit(d.tissue);
    const treatmentarr = safeSplit(d.treatment);
    const samplearr = safeSplit(d.sampleName);

    // 找到最大的数组长度，确保循环能覆盖所有数据
    const maxLength = Math.max(
        gsmarr.length,
        agearr.length,
        sexarr.length,
        timearr.length,
        tissuearr.length,
        treatmentarr.length,
        samplearr.length
    );

    for (let i = 0; i < maxLength; i++) {
        const tr = '<tr>' +
            '<td>' + (gsmarr[i] || '') + '</td>' +
            '<td>' + (samplearr[i] || '') + '</td>' +
            '<td>' + (agearr[i] || '') + '</td>' +
            '<td>' + (sexarr[i] || '') + '</td>' +
            '<td>' + (timearr[i] || '') + '</td>' +
            '<td>' + (tissuearr[i] || '') + '</td>' +
            '<td>' + (treatmentarr[i] || '') + '</td>' +
            '</tr>';
        start = start + tr;
    }

    const article = '<p><strong>Article:  </strong>' + (d.article || '') + '</p>';
    const descript = '<p><strong>Description:  </strong>' + (d.descript || '') + '</p>';

    return (start + end + article + descript);
}

function getSampleAna(container) { //获取两个表的函数
    var $containerid = $('#' + container);
    const tab = $containerid.DataTable({
        ajax: {
            url: "getSampleAna",
            type: "GET",
            async: true
        },
        rowId: 'id',
        stateSave: true,
        bProcessing : true,
        serverSide: true,
        searching: true,
        paging: true,
        ordering: true,
        scrollX: true,
        lengthMenu: [[10, 20, 50, 100], [10, 20, 50, 100]],
        destroy: true,
        scrollCollapse: true,
        scrollY: '400px',
        columns: [
            {
                className: 'dt-control',
                orderable: false,
                data: null,
                defaultContent: ''
            },
            {
                "data": "sampleId",
                "render": function (data, type, row, meta) {
                    return "<a target='_blank' href='getDetailViewGL?id=" + row.sampleId + "'>" + row.sampleId + "</a>";
                },
                "title":"SampleID"
            },
            {"data": "gse_sample","title":"Sample"},
            {"data": "species","title":"species"},
            {"data": "gse_age","title":"age"},
            {"data": "gse_treatment","title":"Treatment"},
            {"data": "gse_sex","title":"sex"},
            {"data": "technology","title":"Technology"},
            {"data": "journal","title":"journal"}
        ],
        oLanguage: {
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
    });
    return tab;
}
function createTableHandlers(table, storageObj, sampleKey) { // 构建工厂函数，设置表的单行选择和行折叠功能。
    table.on('click', '> tbody > tr', (e) => { // 单行选择
        const hasDataDtRow = e.currentTarget.hasAttribute('data-dt-row');
        let classList = e.currentTarget.classList;
        if (classList.contains('selected')) {
            classList.remove('selected');
            storageObj[sampleKey] = "";
        } else {
            if (!hasDataDtRow) {
                table.rows('.selected').nodes().each((row) => row.classList.remove('selected'));
                classList.add('selected');
                storageObj[sampleKey] = e.currentTarget.querySelector('td:nth-child(2)').textContent; // 存储当前选择的样本。
            }
        }
    });
    table.on('requestChild.dt', function(e, row) { // 行折叠
        row.child(format(row.data())).show();
    });
    table.on('click', 'tbody td.dt-control', function(e) {
        let tr = e.target.closest('tr');
        let row = table.row(tr);

        if (row.child.isShown()) {
            row.child.hide();
        } else {
            row.child(format(row.data())).show();
        }
    });
}

function colorMapFun(categorys) {
    const colors = [[31,120,180],[51,160,44],[227,26,28],[255,127,0],[106,61,154],[177,89,40],[166,206,227],[178,223,138],[251,154,153],[253,191,111],[202,178,214],[255,255,153],[141,211,199],[255,255,179],[190,186,218],[251,128,114],[128,177,211],[253,180,98],[179,222,105],[252,205,229],[217,217,217],[188,128,189],[204,235,197],[255,237,111]]
    const colorMap = new Map();//为每个细胞类型映射颜色
    categorys.forEach((type, index) => {
        colorMap.set(type, colors[index % colors.length]);
    });
    return colorMap;
}
