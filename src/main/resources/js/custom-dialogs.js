$(function(){
	$("button[id^='action-button-deactivate-']").click(function(){
		var button = $(this);
		
		bootbox.dialog({
			message : $('#dialog-deactivate-message').val(),
			title : $('#dialog-deactivate-title').val(),
			buttons : {
				success : {
					label : $('#dialog-deactivate-success').val(),
					className : "btn-success",
					callback : function() {
						button.parent().submit();
					}
				},
				danger : {
					label : $('#dialog-deactivate-abort').val(),
					className : "btn-danger"
				}
			}
		});
		
		return false;
	});
});
