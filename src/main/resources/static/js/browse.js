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

/*define param*/
let tab1Param = ''
let tab2Param = ''
let tab3Param = ''
let tab4Param = ''
let tab5Param = ''

function getSampleTable() {
    $('#sampleTable').DataTable({
        ajax: {
            url: "getSampleTable",
            type: "GET",
            async: true,
            data: {"tab1Param": tab1Param, "tab2Param": tab2Param, "tab3Param": tab3Param, "tab4Param":tab4Param, "tab5Param":tab5Param}
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
                "data": "sampleId",
                "render": function (data, type, row, meta) {
                    return "<a target='_blank' href='getDetailViewGL?id=" + row.sampleId + "'>" + row.sampleId + "</a>";
                }
            },
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
            // {"data": "sample"},
            {"data": "genomic"},
            {"data": "technology"},
            {"data": "strain"},
            {
                "data": "pmid",
                "render": function (data, type, row, meta) {
                    return "<a target='_blank' href='https://pubmed.ncbi.nlm.nih.gov/" + row.pmid + "'>" + row.pmid + "</a>";
                }
            },
            // {"data": "article"},
            {"data": "journal"},
            {"data": "year"}
        ],
        oLanguage: olanguage
    });
}

/*browser DataSource*/
function tab1() {
    $.ajax({
        url: "tab1",
        type: "get",
        data: {"tab1Param": tab1Param, "tab2Param": tab2Param, "tab3Param": tab3Param, "tab4Param":tab4Param, "tab5Param":tab5Param},
        async: true,
        success: function (res) {
            itemSpan(res, 4, "tab1", "collapseTab1")
        },
        dataType: "json"
    });
}
function tab2() {
    $.ajax({
        url: "tab2",
        type: "get",
        data: {"tab1Param": tab1Param, "tab2Param": tab2Param, "tab3Param": tab3Param, "tab4Param":tab4Param, "tab5Param":tab5Param},
        async: true,
        success: function (res) {
            itemSpan(res, 4, "tab2", "collapseTab2")
        },
        dataType: "json"
    });
}
function tab3() {
    $.ajax({
        url: "tab3",
        type: "get",
        data: {"tab1Param": tab1Param, "tab2Param": tab2Param, "tab3Param": tab3Param, "tab4Param":tab4Param, "tab5Param":tab5Param},
        async: true,
        success: function (res) {
            itemSpan(res, 4, "tab3", "collapseTab3")
        },
        dataType: "json"
    });
}
function tab4() {
    $.ajax({
        url: "tab4",
        type: "get",
        data: {"tab1Param": tab1Param, "tab2Param": tab2Param, "tab3Param": tab3Param, "tab4Param":tab4Param, "tab5Param":tab5Param},
        async: true,
        success: function (res) {
            itemSpan(res, 4, "tab4", "collapseTab4")
        },
        dataType: "json"
    });
}
function tab5() {
    $.ajax({
        url: "tab5",
        type: "get",
        data: {"tab1Param": tab1Param, "tab2Param": tab2Param, "tab3Param": tab3Param, "tab4Param":tab4Param, "tab5Param":tab5Param},
        async: true,
        success: function (res) {
            itemSpan(res, 4, "tab5", "collapseTab5")
        },
        dataType: "json"
    });
}

/*循环遍历结果，插入HTML中*/
function itemSpan(res, size, id, id_Type) {
    $("#" + id).empty();

    if (res.active=="active"||res.data.length<5){
        $("#" + id).css("height","auto");
    }else $("#" + id).css("height","180px");

    for (var i = 0; i < res.data.length; i++) {
        var html
        if (res.data[i].name=="Starr"){
            html = '<li class="my_browse list-group-item justify-content-between align-items-center list-group-item-action '+res.active+'" onclick="' + id + 'Click(\'' +res.data[i].name+ '\')"><span>' + "STARR-seq" + '</span><span class="badge bg-primary rounded-pill">'+res.data[i].sum+'</span></li>'
        }else if (res.data[i].name=="Dnase"){
            html = '<li class="my_browse list-group-item justify-content-between align-items-center list-group-item-action '+res.active+'" onclick="' + id + 'Click(\'' +res.data[i].name+ '\')"><span>' + "DNase-seq" + '</span><span class="badge bg-primary rounded-pill">'+res.data[i].sum+'</span></li>'
        }else if (res.data[i].name=="Mnase"){
            html = '<li class="my_browse list-group-item justify-content-between align-items-center list-group-item-action '+res.active+'" onclick="' + id + 'Click(\'' +res.data[i].name+ '\')"><span>' + "MNase-seq" + '</span><span class="badge bg-primary rounded-pill">'+res.data[i].sum+'</span></li>'
        }else if (res.data[i].name=="FAIRE"){
            html = '<li class="my_browse list-group-item justify-content-between align-items-center list-group-item-action '+res.active+'" onclick="' + id + 'Click(\'' +res.data[i].name+ '\')"><span>' + "FAIRE-seq" + '</span><span class="badge bg-primary rounded-pill">'+res.data[i].sum+'</span></li>'
        }else if (res.data[i].name=="ATAC"){
            html = '<li class="my_browse list-group-item justify-content-between align-items-center list-group-item-action '+res.active+'" onclick="' + id + 'Click(\'' +res.data[i].name+ '\')"><span>' + "ATAC-seq" + '</span><span class="badge bg-primary rounded-pill">'+res.data[i].sum+'</span></li>'
        }else {
            html = '<li class="my_browse list-group-item justify-content-between align-items-center list-group-item-action '+res.active+'" onclick="' + id + 'Click(\'' +res.data[i].name+ '\')"><span>' + res.data[i].name + '</span><span class="badge bg-blue-lt">'+res.data[i].sum+'</span></li>'
        }
        $("#" + id).append(html);
    }
}

/*点击响应函数*/
function tab1Click(param) {
    if (param != '') {
        if (param == tab1Param) {
            tab1Param = ""
        } else tab1Param = param
        browserFourTable()
    }
}

function tab2Click(param) {
    if (param != '') {
        if (param == tab2Param) {
            tab2Param = ""
        } else tab2Param = param
        browserFourTable()
    }
}
function tab3Click(param) {
    if (param != '') {
        if (param == tab3Param) {
            tab3Param = ""
        } else tab3Param = param
        browserFourTable()
    }
}
function tab4Click(param) {
    if (param != '') {
        if (param == tab4Param) {
            tab4Param = ""
        } else tab4Param = param
        browserFourTable()
    }
}
function tab5Click(param) {
    if (param != '') {
        if (param == tab5Param) {
            tab5Param = ""
        } else tab5Param = param
        browserFourTable()
    }
}

function browserFourTable() {
    getSampleTable()
    tab1()
    tab2()
    tab3()
    tab4()
    tab5()
    datatablesShow()
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