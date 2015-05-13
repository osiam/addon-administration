$(function () {
    $("button[id^='button-clear-extensions-']").click(function () {
        $(this).parent().parent().find("input").each(function () {
            if ($(this).attr("type") == "checkbox") {
                $(this).prop("checked", false);
            } else {
                $(this).val("");
            }
        });
    });

    $('input[type=datetime]').datetimepicker({
        format: "yyyy-mm-ddThh:ii:ss.000Z",
        autoclose: true,
        todayBtn: true,
        pickerPosition: "bottom-left"
    });
});
