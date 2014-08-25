$(function(){
	$("#btnSaveChanges").click(function(){
		var button = $(this);

		bootbox.dialog({
			message : $('#dialog-updateUser-message').val(),
			title : $('#dialog-title').val(),
			buttons : {
				success : {
					label : $('#dialog-success').val(),
					className : "btn-success",
					callback : function() {
						button.closest("form").submit();
					}
				},
				danger : {
					label : $('#dialog-abort').val(),
					className : "btn-danger"
				}
			}
		});

		return false;
	});


	$("#btnCancelChanges").click(function(){
		var button = $(this);
		var continueLocation = $(button).parent().attr('href');
		$(button).parent().attr('');

		bootbox.dialog({
			closeButton: false,
			message : $('#dialog-cancelUpdate-message').val(),
			title : $('#dialog-title').val(),
			buttons : {
				success : {
					label : $('#dialog-success').val(),
					className : "btn-success",
					callback : function() {
						window.location = continueLocation;
					}
				},
				danger : {
					label : $('#dialog-abort').val(),
					className : "btn-danger",
					callback : function() {
						$(button).parent().attr('href', continueLocation);
					}
				}
			}
		});

		return false;
	});


	$("button[id^='action-button-deactivate-']").click(function(){
		var button = $(this);

		bootbox.dialog({
			message : $('#dialog-deactivate-message').html(),
			title : $('#dialog-title').val(),
			buttons : {
				success : {
					label : $('#dialog-success').val(),
					className : "btn-success",
					callback : function() {
						var sendMail = $('div[role="dialog"] #send-mail').is(':checked');

						button.parent().find('input[name="sendMail"]').val(sendMail);
						button.parent().submit();
					}
				},
				danger : {
					label : $('#dialog-abort').val(),
					className : "btn-danger"
				}
			}
		});

		return false;
	});

	$("button[id^='action-button-delete-group-']").click(function(){
		var button = $(this);

		bootbox.dialog({
			message : $('#dialog-delete-message').html(),
			title : $('#dialog-title').val(),
			buttons : {
				success : {
					label : $('#dialog-success').val(),
					className : "btn-success",
					callback : function() {
						button.parent().submit();
					}
				},
				danger : {
					label : $('#dialog-abort').val(),
					className : "btn-danger"
				}
			}
		});

		return false;
	});


	$("button[id^='action-button-activate-']").click(function(){
		var button = $(this);

		bootbox.dialog({
			message : $('#dialog-activate-message').html(),
			title : $('#dialog-title').val(),
			buttons : {
				success : {
					label : $('#dialog-success').val(),
					className : "btn-success",
					callback : function() {
						var sendMail = $('div[role="dialog"] #send-mail').is(':checked');

						button.parent().find('input[name="sendMail"]').val(sendMail);
						button.parent().submit();
					}
				},
				danger : {
					label : $('#dialog-abort').val(),
					className : "btn-danger"
				}
			}
		});

		return false;
	});
});
