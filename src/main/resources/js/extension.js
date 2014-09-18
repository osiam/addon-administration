$(function() {
	$("button[id^='button-clear-extensions-']").click(function() {
		$(this).parent().parent().find("input").each(function() {
			if($(this).attr("type") == "checkbox") {
				$(this).prop("checked", false);
			} else {
				$(this).val("");
			}
		});
	});
});