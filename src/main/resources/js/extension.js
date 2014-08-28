$(function() {
	$("button[id^='button-clear-extensions-']").click(function() {
		$(this).parent().parent().find("input").val("");
	});
});