package org.osiam.addons.administration;

import org.openqa.selenium.By;

public enum UserEdit implements Element {
	ACTIVE(By.id("active")),
	LASTNAME(By.id("familyname")),
	FORMATTEDNAME(By.id("formatted")),
	GIVENNAME(By.id("givenname")),
	HONORIFICPREFIX(By.id("honorificprefix")),
	HONORIFICSUFFIX(By.id("honorificsuffix")),
	MIDDLENAME(By.id("middlename")),
	NICKNAME(By.id("nickName")),
	USERTITLE(By.id("userTitle")),

	PREFERREDLANGUAGE(By.id("preferredLanguage")),
	LOCALE(By.id("locale")),
	PROFILEURL(By.id("profileURL")),
	TIMEZONE(By.id("timezone")),
	USERNAME(By.id("userName")),

	SUBMIT_BUTTON(By.id("btnSaveChanges")),
	CANCEL_BUTTON(By.id("btnCancelChanges")),

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