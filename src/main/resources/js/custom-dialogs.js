$(function () {
    $("#btnSaveChanges").click(function () {
        var button = $(this);

        bootbox.dialog({
            message: $('#dialog-update-message').val(),
            title: $('#dialog-title').val(),
            buttons: {
                success: {
                    label: $('#dialog-success').val(),
                    className: "btn-primary",
                    callback: function () {
                        button.closest("form").submit();
                    }
                },
                danger: {
                    label: $('#dialog-abort').val(),
                    className: "btn-default"
                }
            }
        });

        return false;
    });

    $("#btnCancelChanges").click(function () {
        var button = $(this);
        var continueLocation = $(button).parent().attr('href');
        $(button).parent().attr('');

        bootbox.dialog({
            closeButton: false,
            message: $('#dialog-cancelUpdate-message').val(),
            title: $('#dialog-title').val(),
            buttons: {
                success: {
                    label: $('#dialog-success').val(),
                    className: "btn-primary",
                    callback: function () {
                        window.location = continueLocation;
                    }
                },
                danger: {
                    label: $('#dialog-abort').val(),
                    className: "btn-default",
                    callback: function () {
                        $(button).parent().attr('href', continueLocation);
                    }
                }
            }
        });

        return false;
    });

    $("a[id^='action-button-deactivate-']").click(function (event) {
        event.preventDefault();
        var $actionLink = $(this);

        bootbox.dialog({
            message: $('#dialog-deactivate-message').html(),
            title: $('#dialog-title').val(),
            buttons: {
                success: {
                    label: $('#dialog-success').val(),
                    className: "btn-primary",
                    callback: function () {
                        var $form = $actionLink.next();

                        $form.find('input[name="sendMail"]').val($('div[role="dialog"] #send-mail').is(':checked'));
                        $form.submit();
                    }
                },
                danger: {
                    label: $('#dialog-abort').val(),
                    className: "btn-default"
                }
            }
        });
    });

    $("a[id^='action-button-activate-']").click(function (event) {
        event.preventDefault();
        var $actionLink = $(this);

        bootbox.dialog({
            message: $('#dialog-activate-message').html(),
            title: $('#dialog-title').val(),
            buttons: {
                success: {
                    label: $('#dialog-success').val(),
                    className: "btn-primary",
                    callback: function () {
                        var $form = $actionLink.next();

                        $form.find('input[name="sendMail"]').val($('div[role="dialog"] #send-mail').is(':checked'));
                        $form.submit();
                    }
                },
                danger: {
                    label: $('#dialog-abort').val(),
                    className: "btn-default"
                }
            }
        });
    });

    $("button[id^='action-button-selected-deactivate']").click(function () {
        var button = $(this);

        bootbox.dialog({
            message: $('#dialog-deactivate-selected-message').html(),
            title: $('#dialog-title').val(),
            buttons: {
                success: {
                    label: $('#dialog-success').val(),
                    className: "btn-primary",
                    callback: function () {
                        var sendMail = $('div[role="dialog"] #send-mail').is(':checked');

                        button.parent().find('input[name="sendMail"]').val(sendMail);
                        button.parent().submit();
                    }
                },
                danger: {
                    label: $('#dialog-abort').val(),
                    className: "btn-default"
                }
            }
        });

        return false;
    });

    $("button[id^='action-button-selected-activate']").click(function () {
        var button = $(this);

        bootbox.dialog({
            message: $('#dialog-activate-selected-message').html(),
            title: $('#dialog-title').val(),
            buttons: {
                success: {
                    label: $('#dialog-success').val(),
                    className: "btn-primary",
                    callback: function () {
                        var sendMail = $('div[role="dialog"] #send-mail').is(':checked');

                        button.parent().find('input[name="sendMail"]').val(sendMail);
                        button.parent().submit();
                    }
                },
                danger: {
                    label: $('#dialog-abort').val(),
                    className: "btn-default"
                }
            }
        });

        return false;
    });

    $("a[id^='action-button-delete-user-']").click(function (event) {
        event.preventDefault();
        var $actionLink = $(this);

        bootbox.dialog({
            message: $('#dialog-delete-message').val(),
            title: $('#dialog-title').val(),
            buttons: {
                success: {
                    label: $('#dialog-success').val(),
                    className: "btn-primary",
                    callback: function () {
                        $actionLink.next().submit();
                    }
                },
                danger: {
                    label: $('#dialog-abort').val(),
                    className: "btn-default"
                }
            }
        });
    });

    $("button[id^='action-button-selected-delete']").click(function () {
        var button = $(this);

        bootbox.dialog({
            message: $('#dialog-delete-selected-message').val(),
            title: $('#dialog-title').val(),
            buttons: {
                success: {
                    label: $('#dialog-success').val(),
                    className: "btn-primary",
                    callback: function () {
                        button.parent().submit();
                    }
                },
                danger: {
                    label: $('#dialog-abort').val(),
                    className: "btn-default"
                }
            }
        });

        return false;
    });

    $("a[id^='action-button-delete-group-']").click(function (event) {
        event.preventDefault();
        var $actionLink = $(this);

        bootbox.dialog({
            message: $('#dialog-delete-message').val(),
            title: $('#dialog-title').val(),
            buttons: {
                success: {
                    label: $('#dialog-success').val(),
                    className: "btn-primary",
                    callback: function () {
                        $actionLink.next().submit();
                    }
                },
                danger: {
                    label: $('#dialog-abort').val(),
                    className: "btn-default"
                }
            }
        });
    });
});
