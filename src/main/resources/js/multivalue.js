$(function () {
    $("#button-add-email").click(function () {
        var allEmailBlocks = $("[id^='email-block-']");
        var displayedEmailCount = allEmailBlocks.size();
        var lastEmailBlock = allEmailBlocks.last();

        var myIndex = displayedEmailCount;
        var newBlock = cloneBlock('#email-template', 'email-block-' + myIndex, myIndex);

        if (lastEmailBlock.length > 0) {
            lastEmailBlock.after(newBlock);
        } else {
            $('#email-container').prepend(newBlock);
        }

        newBlock.show();
        applyRemoveActions();
    });

    $("#button-add-phoneNumber").click(function () {
        var allPhoneNumberBlocks = $("[id^='phoneNumber-block-']");
        var displayedPhoneNumberCount = allPhoneNumberBlocks.size();
        var lastPhoneNumberBlock = allPhoneNumberBlocks.last();

        var myIndex = displayedPhoneNumberCount;
        var newBlock = cloneBlock('#phoneNumber-template', 'phoneNumber-block-' + myIndex, myIndex);

        if (lastPhoneNumberBlock.length > 0) {
            lastPhoneNumberBlock.after(newBlock);
        } else {
            $('#phoneNumber-container').prepend(newBlock);
        }

        newBlock.show();
        applyRemoveActions();
    });

    $("#button-add-im").click(function () {
        var allImBlocks = $("[id^='im-block-']");
        var displayedImCount = allImBlocks.size();
        var lastImBlock = allImBlocks.last();

        var myIndex = displayedImCount;
        var newBlock = cloneBlock('#im-template', 'im-block-' + myIndex, myIndex);

        if (lastImBlock.length > 0) {
            lastImBlock.after(newBlock);
        } else {
            $('#im-container').prepend(newBlock);
        }

        newBlock.show();
        applyRemoveActions();
    });

    $("#button-add-certificate").click(function () {
        var allCertificatesBlocks = $("[id^='certificate-block-']");
        var displayedCertificatesCount = allCertificatesBlocks.size();
        var lastCertificatesBlock = allCertificatesBlocks.last();

        var myIndex = displayedCertificatesCount;
        var newBlock = cloneBlock('#certificate-template', 'certificate-block-' + myIndex, myIndex);

        if (lastCertificatesBlock.length > 0) {
            lastCertificatesBlock.after(newBlock);
        } else {
            $('#certificates-container').prepend(newBlock);
        }

        newBlock.show();
        applyRemoveActions();
    });

    $("#button-add-entitlement").click(function () {
        var allEntitlementBlocks = $("[id^='entitlement-block-']");
        var displayedEntitlementCount = allEntitlementBlocks.size();
        var lastEntitlementBlock = allEntitlementBlocks.last();

        var myIndex = displayedEntitlementCount;
        var newBlock = cloneBlock('#entitlement-template', 'entitlement-block-' + myIndex, myIndex);

        if (lastEntitlementBlock.length > 0) {
            lastEntitlementBlock.after(newBlock);
        } else {
            $('#entitlements-container').prepend(newBlock);
        }

        newBlock.show();
        applyRemoveActions();
    });

    $("#button-add-address").click(function () {
        var allAddressBlocks = $("[id^='address-block-']");
        var displayedAddressCount = allAddressBlocks.size();
        var lastAddressBlock = allAddressBlocks.last();

        var myIndex = displayedAddressCount;
        var newBlock = cloneBlock('#address-template', 'address-block-' + myIndex, myIndex);

        if (lastAddressBlock.length > 0) {
            lastAddressBlock.after(newBlock);
        } else {
            $('#addresses-container').prepend(newBlock);
        }

        newBlock.show();
        applyRemoveActions();
    });

    function applyRemoveActions() {
        $("button[id^='button-remove-email-']").click(function () {
            var myBlock = $(this).parent().parent().parent();
            myBlock.remove();
        });

        $("button[id^='button-remove-phoneNumber-']").click(function () {
            var myBlock = $(this).parent().parent().parent();
            myBlock.remove();
        });

        $("button[id^='button-remove-im-']").click(function () {
            var myBlock = $(this).parent().parent().parent();
            myBlock.remove();
        });

        $("button[id^='button-remove-certificates-']").click(function () {
            var myBlock = $(this).parent().parent().parent();
            myBlock.remove();
        });

        $("button[id^='button-remove-entitlements-']").click(function () {
            var myBlock = $(this).parent().parent().parent();
            myBlock.remove();
        });

        $("button[id^='button-remove-addresses-']").click(function () {
            var myBlock = $(this).parent().parent().parent();
            myBlock.remove();
        });
    }

    function cloneBlock(searchString, newId, newIndex) {
        var templateBlock = $(searchString);
        var newBlock = templateBlock.clone();

        newBlock.attr('id', newId);
        newBlock.find('*').each(function (e, element) {
            $.each(this.attributes, function (a, attr) {
                var name = attr.name;
                var value = attr.value;

                if (value.indexOf("ID") > -1) {
                    var newValue = value.replace(/ID/g, newIndex);
                    $(element).attr(name, newValue);
                }

            });
        });
        return newBlock;
    };

    applyRemoveActions();
});
