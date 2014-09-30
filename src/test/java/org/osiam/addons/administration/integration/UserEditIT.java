package org.osiam.addons.administration.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.osiam.addons.administration.EditList;
import org.osiam.addons.administration.UserEdit;
import org.osiam.addons.administration.selenium.Field;

public class UserEditIT extends Integrationtest {

	@Override
	public void setup() {
		super.setup();

		browser.doOauthLogin(ADMIN_USERNAME, ADMIN_PASSWORD);
		edit_Test_User();
	}

	private static String TEST_USER_NAME = "adavies";

	@Test
	public void save_changes_dialog_should_return_to_user_list() {
		// closing the dialog should return to the edit view
		browser.findElement(UserEdit.SUBMIT_BUTTON).click();
		browser.findElement(EditList.DIALOG_CLOSE).click();

		assertTrue(browser.getCurrentUrl().contains("/user/edit?id="));

		// aborting the dialog should return to the edit view
		browser.findElement(UserEdit.SUBMIT_BUTTON).click();
		browser.findElement(EditList.DIALOG_ABORT).click();

		assertTrue(browser.getCurrentUrl().contains("/user/edit?id="));

		// submitting the dialog should return to the user list
		browser.findElement(UserEdit.SUBMIT_BUTTON).click();
		browser.findElement(EditList.DIALOG_SUCCESS).click();

		assertTrue(browser.getCurrentUrl().contains("/user/list"));
	}

	@Test
	public void cancel_edit_dialog_should_return_to_user_list() {
		// aborting the dialog should return to the edit view
		browser.findElement(UserEdit.CANCEL_BUTTON).click();
		browser.findElement(EditList.DIALOG_ABORT).click();

		assertTrue(browser.getCurrentUrl().contains("/user/edit?id="));

		// canceling the dialog should return to the user list view5
		browser.findElement(UserEdit.CANCEL_BUTTON).click();
		browser.findElement(EditList.DIALOG_SUCCESS).click();

		assertTrue(browser.getCurrentUrl().contains("/user/list"));
	}

	@Test
	public void testExtensionName() {
		assertTrue(browser.isTextPresent("Authentication"));
		assertTrue(browser.isTextPresent("Vehicle"));
		assertFalse(browser.isTextPresent("origin"));
		assertFalse(browser.isTextPresent("car"));
	}

	//TODO
	//Fix me "displayname" (Bug in server)
	@Test
	public void edit_user_view_displays_previously_saved_changes() {
		final boolean newActive = true;

		final String newLastName = "Test456";
		final String newFormattedName = "Test 23 336";
		final String newGivenName = "Test123\u00e4";
		final String newHonorificPrefix = "Dr.";
		final String newHonorificSuffix = "moon travel";
		final String newMiddleName = "Hans";
		final String newDisplayName = "TestHansKlaus17";
		final String newNickName = "MeterPeterMolaCola";
		final String newUserTitle = "Dr. Dr. Prof.";

		final String newPreferredLanguage = "RU";
		final String newLocale = "ru_RU";
		final String newProfileURL = "http://www.gibtsnicht.ehnicht111222.de";
		final String newTimezone = "ru/RussischeStadt";
		final String newUserName = "MeterPeterMolaCola";

		final String newEmailDisplay = "meine email";
		final String newEmailValue = "hans_peter@gibtsnicht.org";
		final String newEmailType = "StandartE";

		final String newPhoneDisplay = "zu hause";
		final String newPhoneValue = "0987654321";
		final String newPhoneType = "StandartP";

		final String newIMDisplay = "XMPP";
		final String newIMValue = "444555666";
		final String newIMType = "StandartI";

		final String newCertificatesDisplay = "Positiv";
		final String newCertificatesValue = "Vitamin C Professor";
		final String newCertificatesType = "StandartC";

		final String newEntitlementsDisplay = "Alles";
		final String newEntitlementsValue = "Admin";
		final String newEntitlementsType = "StandartE";

		final String newAddressCountry = "Deutschland";
		final String newAddressFormatted = "Deutschlandstraße 20 12345 Ländisch";
		final String newAddressLocality = "de_DE";
		final String newAddressPostalcode = "12345";
		final String newAddressRegion = "Ländisch";
		final String newAddressStreetaddress = "Deutschlandstraße 20";
		final String newAddressType = "StandartA";

		browser.findElement(UserEdit.ACTIVE).click();
		browser.fill(new Field(UserEdit.LASTNAME, newLastName));
		browser.fill(new Field(UserEdit.FORMATTEDNAME, newFormattedName));
		browser.fill(new Field(UserEdit.GIVENNAME, newGivenName));
		browser.fill(new Field(UserEdit.HONORIFICPREFIX, newHonorificPrefix));
		browser.fill(new Field(UserEdit.HONORIFICSUFFIX, newHonorificSuffix));
		browser.fill(new Field(UserEdit.MIDDLENAME, newMiddleName));
		browser.fill(new Field(EditList.DISPLAYNAME, newDisplayName));
		browser.fill(new Field(UserEdit.NICKNAME, newNickName));
		browser.fill(new Field(UserEdit.USERTITLE, newUserTitle));

		browser.fill(new Field(UserEdit.PREFERREDLANGUAGE, newPreferredLanguage));
		browser.fill(new Field(UserEdit.LOCALE, newLocale));
		browser.fill(new Field(UserEdit.PROFILEURL, newProfileURL));
		browser.fill(new Field(UserEdit.TIMEZONE, newTimezone));
		browser.fill(new Field(UserEdit.USERNAME, newUserName));

		browser.fill(new Field(UserEdit.FIRST_EMAIL_DISPLAY, newEmailDisplay));
		browser.fill(new Field(UserEdit.FIRST_EMAIL_VALUE, newEmailValue));
		browser.fill(new Field(UserEdit.FIRST_MAIL_PRIMARY, newActive));
		browser.fill(new Field(UserEdit.FIRST_MAIL_TYPE, newEmailType));

		browser.fill(new Field(UserEdit.PHONENUMBER_DISPLAY, newPhoneDisplay));
		browser.fill(new Field(UserEdit.PHONENUMBER_VALUE, newPhoneValue));
		browser.fill(new Field(UserEdit.PHONENUMBER_PRIMARY, newActive));
		browser.fill(new Field(UserEdit.PHONENUMBER_TYPE, newPhoneType));

		browser.fill(new Field(UserEdit.INSTANT_MESSENGER_DISPLAY, newIMDisplay));
		browser.fill(new Field(UserEdit.INSTANT_MESSENGER_VALUE, newIMValue));
		browser.fill(new Field(UserEdit.INSTANT_MESSENGER_PRIMARY, newActive ));
		browser.fill(new Field(UserEdit.INSTANT_MESSENGER_TYPE, newIMType));

		browser.fill(new Field(UserEdit.CERTIFICATES_DISPLAY, newCertificatesDisplay));
		browser.fill(new Field(UserEdit.CERTIFICATES_VALUE, newCertificatesValue));
		browser.fill(new Field(UserEdit.CERTIFICATES_PRIMARY, newActive));
		browser.fill(new Field(UserEdit.CERTIFICATES_TYPE, newCertificatesType));

		browser.fill(new Field(UserEdit.ENTITLEMENTS_DISPLAY, newEntitlementsDisplay));
		browser.fill(new Field(UserEdit.ENTITLEMENTS_VALUE, newEntitlementsValue));
		browser.fill(new Field(UserEdit.ENTITLEMENTS_PRIMARY, newActive));
		browser.fill(new Field(UserEdit.ENTITLEMENTS_TYPE, newEntitlementsType));

		browser.fill(new Field(UserEdit.ADDRESS_COUNTRY, newAddressCountry));
		browser.fill(new Field(UserEdit.ADDRESS_FORMATTED, newAddressFormatted));
		browser.fill(new Field(UserEdit.ADDRESS_LOCALITY, newAddressLocality));
		browser.fill(new Field(UserEdit.ADDRESS_POSTALCODE, newAddressPostalcode));
		browser.fill(new Field(UserEdit.ADDRESS_REGION, newAddressRegion));
		browser.fill(new Field(UserEdit.ADDRESS_STREETADDRESS, newAddressStreetaddress));
		browser.fill(new Field(UserEdit.ADDRESS_PRIMARY, newActive));
		browser.fill(new Field(UserEdit.ADDRESS_TYPE, newAddressType));

		TEST_USER_NAME = newUserName;

		browser.click(EditList.SUBMIT_BUTTON);
		browser.click(EditList.DIALOG_SUCCESS);

		edit_Test_User();

		assertEquals(newActive, browser.findElement(UserEdit.ACTIVE).isSelected());
		assertEquals(newLastName, browser.getValue(UserEdit.LASTNAME));
		assertEquals(newFormattedName, browser.getValue(UserEdit.FORMATTEDNAME));
		assertEquals(newGivenName, browser.getValue(UserEdit.GIVENNAME));
		assertEquals(newHonorificPrefix, browser.getValue(UserEdit.HONORIFICPREFIX));
		assertEquals(newHonorificSuffix, browser.getValue(UserEdit.HONORIFICSUFFIX));
		assertEquals(newMiddleName, browser.getValue(UserEdit.MIDDLENAME));
		assertEquals(newDisplayName, browser.getValue(EditList.DISPLAYNAME));
		assertEquals(newNickName, browser.getValue(UserEdit.NICKNAME));
		assertEquals(newUserTitle, browser.getValue(UserEdit.USERTITLE));

		assertEquals(newPreferredLanguage, browser.getValue(UserEdit.PREFERREDLANGUAGE));
		assertEquals(newLocale, browser.getValue(UserEdit.LOCALE));
		assertEquals(newProfileURL, browser.getValue(UserEdit.PROFILEURL));
		assertEquals(newTimezone, browser.getValue(UserEdit.TIMEZONE));
		assertEquals(newUserName, browser.getValue(UserEdit.USERNAME));

		assertEquals(newEmailDisplay, browser.getValue(UserEdit.FIRST_EMAIL_DISPLAY));
		assertEquals(newEmailValue, browser.getValue(UserEdit.FIRST_EMAIL_VALUE));
		assertEquals(newActive, browser.getValue(UserEdit.FIRST_MAIL_PRIMARY));
		assertEquals(newEmailType, browser.getValue(UserEdit.FIRST_MAIL_TYPE));

		assertEquals(newPhoneDisplay, browser.getValue(UserEdit.PHONENUMBER_DISPLAY));
		assertEquals(newPhoneValue, browser.getValue(UserEdit.PHONENUMBER_VALUE));
		assertEquals(newActive, browser.getValue(UserEdit.PHONENUMBER_PRIMARY));
		assertEquals(newPhoneType, browser.getValue(UserEdit.PHONENUMBER_TYPE));

		assertEquals(newIMDisplay, browser.getValue(UserEdit.INSTANT_MESSENGER_DISPLAY));
		assertEquals(newIMValue, browser.getValue(UserEdit.INSTANT_MESSENGER_VALUE));
		assertEquals(newActive, browser.getValue(UserEdit.INSTANT_MESSENGER_PRIMARY));
		assertEquals(newIMType, browser.getValue(UserEdit.INSTANT_MESSENGER_TYPE));

		assertEquals(newCertificatesDisplay, browser.getValue(UserEdit.CERTIFICATES_DISPLAY));
		assertEquals(newCertificatesValue, browser.getValue(UserEdit.CERTIFICATES_VALUE));
		assertEquals(newActive, browser.getValue(UserEdit.CERTIFICATES_PRIMARY));
		assertEquals(newCertificatesType, browser.getValue(UserEdit.CERTIFICATES_TYPE));

		assertEquals(newEntitlementsDisplay, browser.getValue(UserEdit.ENTITLEMENTS_DISPLAY));
		assertEquals(newEntitlementsValue, browser.getValue(UserEdit.ENTITLEMENTS_VALUE));
		assertEquals(newActive, browser.getValue(UserEdit.ENTITLEMENTS_PRIMARY));
		assertEquals(newEntitlementsType, browser.getValue(UserEdit.ENTITLEMENTS_TYPE));

		assertEquals(newAddressCountry, browser.getValue(UserEdit.ADDRESS_COUNTRY));
		assertEquals(newAddressFormatted, browser.getValue(UserEdit.ADDRESS_FORMATTED));
		assertEquals(newAddressLocality, browser.getValue(UserEdit.ADDRESS_LOCALITY));
		assertEquals(newAddressPostalcode, browser.getValue(UserEdit.ADDRESS_POSTALCODE));
		assertEquals(newAddressRegion, browser.getValue(UserEdit.ADDRESS_REGION));
		assertEquals(newAddressStreetaddress, browser.getValue(UserEdit.ADDRESS_STREETADDRESS));
		assertEquals(newActive, browser.getValue(UserEdit.ADDRESS_PRIMARY));
		assertEquals(newAddressType, browser.getValue(UserEdit.ADDRESS_TYPE));
	}

	@Test
	public void false_Multi_Value_Attributes() {
		final String falseEmailValue = "hanspeters_email";
		final String falsePhoneValue = "meintelefon";
		final String falseAddressPostalcode = "einszweidreivierfünf";

		browser.fill(new Field(UserEdit.FIRST_EMAIL_VALUE, falseEmailValue));
		browser.fill(new Field(UserEdit.PHONENUMBER_VALUE, falsePhoneValue));
		browser.fill(new Field(UserEdit.ADDRESS_POSTALCODE, falseAddressPostalcode));

		browser.click(UserEdit.SUBMIT_BUTTON);
		browser.click(EditList.DIALOG_SUCCESS);

		assertTrue(browser.isTextPresent("keine gültige E-Mail-Adresse"));
	}

	@Test
	public void remove_And_Add_All_Multi_Value_Attributes() {
		test_Remove_And_Add_Multi_Value_Attribute("email", 1);
		test_Remove_And_Add_Multi_Value_Attribute("phoneNumber", 1);
		test_Remove_And_Add_Multi_Value_Attribute("im", 0);
		test_Remove_And_Add_Multi_Value_Attribute("certificate", 0);
		test_Remove_And_Add_Multi_Value_Attribute("entitlement", 0);
		test_Remove_And_Add_Multi_Value_Attribute("address", 1);
	}

	private void test_Remove_And_Add_Multi_Value_Attribute(String containerId, int existingFields) {
		add_Multi_Value_Attribute(containerId);
		assertEquals(existingFields + 1, count_Child_Elements(containerId));
		add_Multi_Value_Attribute(containerId);
		assertEquals(existingFields + 2, count_Child_Elements(containerId));
		drop_Multi_Value_Attribute(containerId, existingFields + 2);
		drop_Multi_Value_Attribute(containerId, existingFields + 1);
		assertEquals(existingFields + 0, count_Child_Elements(containerId));
	}

	private void edit_Test_User() {
		String actionLabelXpath = "//td[. = '" + TEST_USER_NAME + "']/..//div[contains(@id, 'action-label')]";
		String editButtonXpath = "//td[. = '" + TEST_USER_NAME + "']/..//button[contains(@id, 'action-button-edit')]";

		browser.findElement(By.xpath(actionLabelXpath)).click();
		browser.findElement(By.xpath(editButtonXpath)).click();
	}

	private void add_Multi_Value_Attribute(String multivalueName) {
		browser.findElement(By.xpath("//button[contains(@id, 'button-add-" + multivalueName + "')]")).click();
	}

	private void drop_Multi_Value_Attribute(String containerId, int nth) {
		browser.findElement(
				By.xpath("//div[contains(@id, 'container')]//div[contains(@id, '" + containerId + "-block')][" + nth
						+ "]//button[contains(@id, 'button-remove')]"))
				.click();
	}

	private int count_Child_Elements(String containerId) {
		List<WebElement> foundElements = browser.findElements(By.xpath("//div[contains(@id, 'container')]//div[contains(@id, '" + containerId + "-block')]"));
		return foundElements.size();
	}

	@Test
	public void trimInputUser() {
		final String spaceDisplayName = " Trimmer ";
		final String spaceHonorificPrefix ="Dr. ";
		final String spaceHonorificSuffix = " Dr.";
		final String trimDisplayName = "Trimmer";
		final String trimHonorificPrefix ="Dr.";
		final String trimHonorificSuffix = "Dr.";

		browser.fill(new Field(EditList.DISPLAYNAME, spaceDisplayName));
		browser.fill(new Field(UserEdit.HONORIFICPREFIX, spaceHonorificPrefix));
		browser.fill(new Field(UserEdit.HONORIFICSUFFIX, spaceHonorificSuffix));

		browser.click(EditList.SUBMIT_BUTTON);
		browser.click(EditList.DIALOG_SUCCESS);

		edit_Test_User();

		assertEquals(trimDisplayName, browser.getValue(EditList.DISPLAYNAME));
		assertEquals(trimHonorificPrefix, browser.getValue(UserEdit.HONORIFICPREFIX));
		assertEquals(trimHonorificSuffix, browser.getValue(UserEdit.HONORIFICSUFFIX));
	}
}
