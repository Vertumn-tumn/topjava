const mealAjaxUrl = "profile/meals/";

const ctx = {
    ajaxUrl: mealAjaxUrl,
};

$(function () {
    makeEditable(
        $("#datatable").DataTable({
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

function filter() {
    let filterForm = $('#filter');
    $.ajax({
        url: mealAjaxUrl + 'filter',
        method: 'GET',
        data: filterForm.serialize()
    }).done(function (data) {
        updateTableWithData(data);
        successNoty("Filtered");
    });
}

function clearFilter() {
    $('#filter')[0].reset();
    updateTable();
}

function saveMeal() {
    $.ajax({
        type: "POST",
        url: ctx.ajaxUrl,
        data: form.serialize()
    }).done(function () {
        $("#editRow").modal("hide");
        filter();
        successNoty("Saved");
    });
}



