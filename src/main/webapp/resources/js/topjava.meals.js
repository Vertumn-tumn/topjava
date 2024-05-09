const mealAjaxUrl = "profile/meals/";

const ctx = {
    ajaxUrl: mealAjaxUrl,
};

$(function () {
    makeEditable(
        $("datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "",
                    "orderable": false
                },
                {
                    "defaultContent": "",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        }));
});

function updateTableWithData(data) {
    ctx.datatableApi.clear().rows.add(data).draw();
}

function filter() {
    form = $('#filter');
    $.ajax({
        url: mealAjaxUrl + 'filter',
        method: 'GET',
        data: form.serialize()
    }).done(function (data) {
        updateTableWithData(data);
        successNoty("Filtered");
    });
}

function clearFilter() {
    $('#filter')[0].reset();
}



