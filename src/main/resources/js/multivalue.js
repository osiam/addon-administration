$(function(){
    $(".only-on-js").css({"visibility":"visible"});

    $("#button-add-email").click(function(){
        var allEmailBlocks = $("[id^='email-block-']");
        var displayedEmailCount = allEmailBlocks.size();
        var lastEmailBlock = allEmailBlocks.last();

        var myIndex = displayedEmailCount;
        var newBlock = cloneBlock('#email-template', 'email-block-' + myIndex, myIndex);

        lastEmailBlock.after(newBlock);
        newBlock.show();
    });

    $("button[id^='button-remove-email-']").click(function(){
        var myBlock = $(this).parent();
        myBlock.remove();
    });

    $("#button-add-phoneNumber").click(function(){
        var allPhoneNumberBlocks = $("[id^='phoneNumber-block-']");
        var displayedPhoneNumberCount = allPhoneNumberBlocks.size();
        var lastPhoneNumberBlock = allPhoneNumberBlocks.last();

        var myIndex = displayedPhoneNumberCount;
        var newBlock = cloneBlock('#phoneNumber-template', 'phoneNumber-block-' + myIndex, myIndex);

        lastPhoneNumberBlock.after(newBlock);
        newBlock.show();
    });

    $("button[id^='button-remove-phoneNumber-']").click(function(){
        var myBlock = $(this).parent();
        myBlock.remove();
    });

    $("#button-add-im").click(function(){
        var allImBlocks = $("[id^='im-block-']");
        var displayedImCount = allImBlocks.size();
        var lastImBlock = allImBlocks.last();

        var myIndex = displayedImCount;
        var newBlock = cloneBlock('#im-template', 'im-block-' + myIndex, myIndex);

        lastImBlock.after(newBlock);
        newBlock.show();
    });

    $("button[id^='button-remove-im-']").click(function(){
        var myBlock = $(this).parent();
        myBlock.remove();
    });

    $("#button-add-certificates").click(function(){
        var allCertificatesBlocks = $("[id^='certificates-block-']");
        var displayedCertificatesCount = allCertificatesBlocks.size();
        var lastCertificatesBlock = allCertificatesBlocks.last();

        var myIndex = displayedCertificatesCount;
        var newBlock = cloneBlock('#certificates-template', 'certificates-block-' + myIndex, myIndex);

        lastCertificatesBlock.after(newBlock);
        newBlock.show();
    });

    $("button[id^='button-remove-certificates-']").click(function(){
        var myBlock = $(this).parent();
        myBlock.remove();
    });

    $("#button-add-entitlement").click(function(){
        var allentitlementBlocks = $("[id^='entitlement-block-']");
        var displayedentitlementCount = allentitlementBlocks.size();
        var lastentitlementBlock = allentitlementBlocks.last();

        var myIndex = displayedentitlementCount;
        var newBlock = cloneBlock('#entitlement-template', 'entitlement-block-' + myIndex, myIndex);

        lastentitlementBlock.after(newBlock);
        newBlock.show();
    });

    $("button[id^='button-remove-entitlement-']").click(function(){
        var myBlock = $(this).parent();
        myBlock.remove();
    });

    $("#button-add-address").click(function(){
        var allentitlementBlocks = $("[id^='address-block-']");
        var displayedentitlementCount = allentitlementBlocks.size();
        var lastentitlementBlock = allentitlementBlocks.last();

        var myIndex = displayedentitlementCount;
        var newBlock = cloneBlock('#address-template', 'address-block-' + myIndex, myIndex);

        lastentitlementBlock.after(newBlock);
        newBlock.show();
    });

    $("button[id^='button-remove-address-']").click(function(){
        var myBlock = $(this).parent();
        myBlock.remove();
    });

    function cloneBlock(searchString, newId, newIndex){
        var templateBlock = $(searchString);
        var newBlock = templateBlock.clone();

        newBlock.attr('id', newId);
        newBlock.find('*').each(function(e, element) {
            $.each(this.attributes, function(a, attr){
                var name = attr.name;
                var value = attr.value;

                if(value.indexOf("ID") > -1){
                    var newValue = value.replace(/ID/g, newIndex);
                    $(element).attr(name, newValue);
                }

            });
        });
        return newBlock;
    };

});