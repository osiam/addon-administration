$(function(){
    $("button[id^='button-remove-extensions-']").click(function(){
        var myBlock = $(this).parent().parent();
        myBlock.remove();
    });
});