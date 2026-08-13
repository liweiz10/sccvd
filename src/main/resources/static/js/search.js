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

function getCelltype() {
    $.ajax({
        url: "getCelltype",
        type: "GET",
        dataType: "json",
        async: true,
        success: function(data) {
            const $select = $('#select-celltype').selectize({
                valueField: 'name',
                labelField: 'name',
                searchField: 'name',
                options: data.data,
                create: false
            });
            const control = $select[0].selectize;
            control.setValue(data.data[0].name);
        }
    });
}
function getTissuetype() {
    $.ajax({
        url: "getTissuetype",
        type: "GET",
        dataType: "json",
        async: true,
        success: function(data) {
            const $select = $('#select-tissuetype').selectize({
                valueField: 'name',
                labelField: 'name',
                searchField: 'name',
                options: data.data,
                create: false
            });
            const control = $select[0].selectize;
            control.setValue(data.data[0].name);
        }
    });
}
function getDisease() {
    $.ajax({
        url: "getDisease",
        type: "GET",
        dataType: "json",
        async: true,
        success: function(data) {
            const $select = $('#select-disease').selectize({
                valueField: 'name',
                labelField: 'name',
                searchField: 'name',
                options: data.data,
                create: false
            });
            const control = $select[0].selectize;
            control.setValue(data.data[0].name);
        }
    });
}

function searchTab(param,type){
    var $containerid = $('#searchTab');
    $containerid.DataTable({
        ajax: {
            url: "getSearchTab",
            type: "GET",
            async: true,
            data: {"param":param,"type":type}
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
                "title": "sampleId",
                "data": "sampleId",
                "render": function (data, type, row, meta) {
                    return "<a target='_blank' href='getDetailViewGL?id=" + row.sampleId + "'>" + row.sampleId + "</a>";
                }
            },
            {
                "title": "gseId",
                "data": "gseId",
                "render": function (data, type, row, meta) {
                    return "<a target='_blank' href='https://www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc=" + row.gseId + "'>" + row.gseId + "</a>";
                }
            },
            {
                "title": "gsmId",
                "data": "gsmId",
                "render": function (data, type, row, meta) {
                    return "<a target='_blank' href='https://www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc=" + row.gsmId + "'>" + row.gsmId + "</a>";
                }
            },
            {"title": "sampleName","data": "sampleName"},
            {"title": "species","data": "species"},
            {"title": "tissue","data": "tissue"},
            {"title": "age","data": "age"},
            {"title": "sex","data": "sex"},
            {"title": "treatment","data": "treatment"},
            {"title": "disease","data": "disease"},
            {"title": "times","data": "times"},
            {"title": "sample","data": "sample"},
            {"title": "genomic","data": "genomic"},
            {"title": "technology","data": "technology"},
            {"title": "strain","data": "strain"},
            {
                "title": "pmid",
                "data": "pmid",
                "render": function (data, type, row, meta) {
                    return "<a target='_blank' href='https://pubmed.ncbi.nlm.nih.gov/" + row.pmid + "'>" + row.pmid + "</a>";
                }
            },
            {"title": "journal","data": "journal"},
            {"title": "year","data": "year"},
            {"title": "celltype","data": "celltype"}
        ],
        oLanguage: olanguage
    });
}