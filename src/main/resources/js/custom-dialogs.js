$(function(){
    $("button[id^='action-button-deactivate-']").click(function(){
        var button = $(this);

        bootbox.dialog({
            message : $('#dialog-deactivate-message').html(),
            title : $('#dialog-deactivate-title').val(),
            buttons : {
                success : {
                    label : $('#dialog-deactivate-success').val(),
                    className : "btn-success",
                    callback : function() {
                        var sendMail = $('div[role="dialog"] #send-mail').is(':checked');

                        button.parent().find('input[name="sendMail"]').val(sendMail);
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
