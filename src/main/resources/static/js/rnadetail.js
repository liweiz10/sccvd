function formatFourDecimal(data, type, row, meta) {
    if (data === null || data === undefined || data === "") {
        return "";
    }
    return Number(data).toFixed(4);
}
function _getRnaDegTab_(container,id) {
    var $containerid = $('#' + container);
    $containerid.DataTable({
        ajax: {
            url: "getRnaDegTab",
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
                "title": "gene",
                "data": "gene",
                "render": function (data, type, row, meta) {
                    return "<a target='_blank' href='https://www.ncbi.nlm.nih.gov/geo/query/acc.cgi?acc=" + row.gene + "'>" + row.gene + "</a>";
                }
            },
            {"title": "baseMean", "data": "baseMean", "render": formatFourDecimal},
            {"title": "log2fc", "data": "log2fc", "render": formatFourDecimal},
            {"title": "lfcSE", "data": "lfcSE", "render": formatFourDecimal},
            {"title": "pvalue", "data": "pvalue", "render": formatFourDecimal},
            {"title": "padj", "data": "padj", "render": formatFourDecimal},
            {"title": "baseMean_case", "data": "baseMean_case", "render": formatFourDecimal},
            {"title": "baseMean_ctrl", "data": "baseMean_ctrl", "render": formatFourDecimal}
        ],
        oLanguage: olanguage
    });
}