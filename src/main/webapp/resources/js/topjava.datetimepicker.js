$(document).ready(function () {
    $('#filter #startDate').datetimepicker({
        format: 'Y-m-d',
        onShow: function (ct) {
            this.setOptions({
                maxDate: jQuery('#filter #endDate').val() ? jQuery('#filter #endDate').val() : false
            })
        },
        timepicker: false
    });
    $('#filter #endDate').datetimepicker({
        format: 'Y-m-d',
        onShow: function (ct) {
            this.setOptions({
                minDate: jQuery('#filter #startDate').val() ? jQuery('#filter #startDate').val() : false
            })
        },
        timepicker: false
    });
    $('#filter #startTime').datetimepicker({
        format: 'H:i',
        onShow: function (ct) {
            this.setOptions({
                maxTime: jQuery('#filter #endTime').val() ? jQuery('#filter #endTime').val() : false
            })
        },
        datepicker: false
    });
    $('#filter #endTime').datetimepicker({
        format: 'H:i',
        onShow: function (ct) {
            this.setOptions({
                minTime: jQuery('#filter #startTime').val() ? jQuery('#filter #startTime').val() : false
            })
        },
        datepicker: false
    });
    $('#detailsForm #dateTime').datetimepicker({
        format: 'Y-m-d H:i',
    });
});