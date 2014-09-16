package org.osiam.addons.administration;

import org.openqa.selenium.By;

/**
 * This contains all elements that we can access via selenium.
 */
public interface Element {
    By by();

    String name();

    public static enum OauthLogin implements Element {
        USERNAME(By.id("username")),
        PASSWORD(By.id("password")),
        LOGIN_BUTTON(By.xpath("//button[@type = 'submit']")),
        AUTHORIZE_BUTTON(By.xpath("//form[@id = 'confirmation-form']/button"));

        private By by;

        private OauthLogin(By by) {
            this.by = by;
        }

        @Override
        public By by() {
            return this.by;
        }
    }

    public static enum UserList implements Element {
        FILTER_BUTTON(By.id("filter-button")),
        FILTER_LOGIN(By.id("filter-login")),
        FILTER_GIVEN_NAME(By.id("filter-givenname")),
        FILTER_FAMILY_NAME(By.id("filter-familyname")),

        SORT_LOGIN_ASC(By.id("order-by-login-asc")),
        SORT_LOGIN_DESC(By.id("order-by-login-desc")),
        SORT_GIVEN_NAME_ASC(By.id("order-by-givenname-asc")),
        SORT_GIVEN_NAME_DESC(By.id("order-by-givenname-desc")),
        SORT_FAMILY_NAME_ASC(By.id("order-by-familyname-asc")),
        SORT_FAMILY_NAME_DESC(By.id("order-by-familyname-desc")),

        LIMIT(By.id("paging-limit")),

        PAGING_NEXT(By.xpath("//a[@id = 'paging-next' and not(@href = '#')]")),
        PAGING_PREVIOUS(By.xpath("//a[@id = 'paging-prev' and not(@href = '#')]")),
        PAGING_LAST(By.xpath("//a[@id = 'paging-last' and not(@href = '#')]")),
        PAGING_FIRST(By.xpath("//a[@id = 'paging-first' and not(@href = '#')]")),

        DIALOG_SUCCESS(By.xpath("//div[contains(@role, 'dialog')]//button[contains(@data-bb-handler, 'success')]")),
        DIALOG_ABORT(By.xpath("//div[contains(@role, 'dialog')]//button[contains(@data-bb-handler, 'danger')]")),
        DIALOG_CLOSE(By.xpath("//div[contains(@role, 'dialog')]//button[starts-with(@class, 'bootbox-close-button')]"));

        private By by;

        private UserList(By by) {
            this.by = by;
        }

        @Override
        public By by() {
            return this.by;
        }
    }

    public static enum UserEdit implements Element {
        ACTIVE(By.id("active")),
        LASTNAME(By.id("familyname")),
        FORMATTEDNAME(By.id("formatted")),
        GIVENNAME(By.id("givenname")),
        HONORIFICPREFIX(By.id("honorificprefix")),
        HONORIFICSUFFIX(By.id("honorificsuffix")),
        MIDDLENAME(By.id("middlename")),
        DISPLAYNAME(By.id("displayName")),
        NICKNAME(By.id("nickName")),
        USERTITLE(By.id("userTitle")),

        PREFERREDLANGUAGE(By.id("preferredLanguage")),
        LOCALE(By.id("locale")),
        PROFILEURL(By.id("profileURL")),
        TIMEZONE(By.id("timezone")),
        USERNAME(By.id("userName")),

        SUBMIT_BUTTON(By.id("btnSaveChanges")),
        CANCEL_BUTTON(By.id("btnCancelChanges")),

        DIALOG_SUCCESS(By.xpath("//div[contains(@class, 'modal-dialog')]//button[contains(@class, 'btn-success')]")),
        DIALOG_ABORT(By.xpath("//div[contains(@class, 'modal-dialog')]//button[contains(@class, 'btn-danger')]")),
        DIALOG_CLOSE(By.xpath("//div[contains(@class, 'modal-dialog')]//button[contains(@class, 'close')]")),

        FIRST_EMAIL_DISPLAY(By.id("email-0-display")),
        FIRST_EMAIL_VALUE(By.id("email-0-value")),
        FIRST_MAIL_PRIMARY(By.id("email-0-primary")),
        FIRST_MAIL_TYPE(By.id("email-0-type")),

        PHONENUMBER_DISPLAY(By.id("phoneNumber-0-display")),
        PHONENUMBER_VALUE(By.id("phoneNumber-0-value")),
        PHONENUMBER_PRIMARY(By.id("phoneNumber-0-primary")),
        PHONENUMBER_TYPE(By.id("phoneNumber-0-type")),

        INSTANT_MESSENGER_DISPLAY(By.id("im-0-display")),
        INSTANT_MESSENGER_VALUE(By.id("im-0-value")),
        INSTANT_MESSENGER_PRIMARY(By.id("im-0-primary")),
        INSTANT_MESSENGER_TYPE(By.id("im-0-type")),

        CERTIFICATES_DISPLAY(By.id("certificates-0-display")),
        CERTIFICATES_VALUE(By.id("certificates-0-value")),
        CERTIFICATES_PRIMARY(By.id("certificates-0-primary")),
        CERTIFICATES_TYPE(By.id("certificates-0-type")),

        ENTITLEMENTS_DISPLAY(By.id("entitlements-0-display")),
        ENTITLEMENTS_VALUE(By.id("entitlements-0-value")),
        ENTITLEMENTS_PRIMARY(By.id("entitlements-0-primary")),
        ENTITLEMENTS_TYPE(By.id("entitlements-0-type")),

        ADDRESS_COUNTRY(By.id("addresses-0-country")),
        ADDRESS_FORMATTED(By.id("addresses-0-formatted")),
        ADDRESS_LOCALITY(By.id("addresses-0-locality")),
        ADDRESS_POSTALCODE(By.id("addresses-0-postalcode")),
        ADDRESS_REGION(By.id("addresses-0-region-0-display")),
        ADDRESS_STREETADDRESS(By.id("addresses-0-streetaddress")),
        ADDRESS_PRIMARY(By.id("addresses-0-primary")),
        ADDRESS_TYPE(By.id("addresses-0-type"));


        private By by;

        private UserEdit(By by) {
            this.by = by;
        }

        @Override
        public By by() {
            return this.by;
        }
    }
}
