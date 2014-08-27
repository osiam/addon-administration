package org.osiam.addons.administration.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.osiam.addons.administration.Element.UserEdit;
import org.osiam.addons.administration.selenium.Field;

public class UserEditIT extends Integrationtest {

    @Override
    public void setup() {
        super.setup();

        browser.doOauthLogin(ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    private static String TEST_USER_NAME = "adavies";

    @Test
    public void save_changes_dialog_should_return_to_user_list() {
        editTestUser();

        // closing the dialog should return to the edit view
        browser.findElement(UserEdit.SUBMIT_BUTTON).click();
        browser.findElement(UserEdit.DIALOG_CLOSE).click();

        assertTrue(browser.getCurrentUrl().contains("/user/edit?id="));

        // aborting the dialog should return to the edit view
        browser.findElement(UserEdit.SUBMIT_BUTTON).click();
        browser.findElement(UserEdit.DIALOG_ABORT).click();

        assertTrue(browser.getCurrentUrl().contains("/user/edit?id="));

        // submitting the dialog should return to the user list
        browser.findElement(UserEdit.SUBMIT_BUTTON).click();
        browser.findElement(UserEdit.DIALOG_SUCCESS).click();

        assertTrue(browser.getCurrentUrl().contains("/user/list"));
    }

    @Test
    public void cancel_edit_dialog_should_return_to_user_list() {
        editTestUser();

        // aborting the dialog should return to the edit view
        browser.findElement(UserEdit.CANCEL_BUTTON).click();
        browser.findElement(UserEdit.DIALOG_ABORT).click();

        assertTrue(browser.getCurrentUrl().contains("/user/edit?id="));

        // canceling the dialog should return to the user list view5
        browser.findElement(UserEdit.CANCEL_BUTTON).click();
        browser.findElement(UserEdit.DIALOG_SUCCESS).click();

        assertTrue(browser.getCurrentUrl().contains("/user/list"));
    }

    @Test
    public void edit_user_view_displays_previously_saved_changes() {
        editTestUser();

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

        browser.findElement(UserEdit.ACTIVE).click();
        browser.fill(new Field(UserEdit.LASTNAME, newLastName));
        browser.fill(new Field(UserEdit.FORMATTEDNAME, newFormattedName));
        browser.fill(new Field(UserEdit.GIVENNAME, newGivenName));
        browser.fill(new Field(UserEdit.HONORIFICPREFIX, newHonorificPrefix));
        browser.fill(new Field(UserEdit.HONORIFICSUFFIX, newHonorificSuffix));
        browser.fill(new Field(UserEdit.MIDDLENAME, newMiddleName));
        browser.fill(new Field(UserEdit.DISPLAYNAME, newDisplayName));
        browser.fill(new Field(UserEdit.NICKNAME, newNickName));
        browser.fill(new Field(UserEdit.USERTITLE, newUserTitle));

        browser.fill(new Field(UserEdit.PREFERREDLANGUAGE, newPreferredLanguage));
        browser.fill(new Field(UserEdit.LOCALE, newLocale));
        browser.fill(new Field(UserEdit.PROFILEURL, newProfileURL));
        browser.fill(new Field(UserEdit.TIMEZONE, newTimezone));
        browser.fill(new Field(UserEdit.USERNAME, newUserName));

        TEST_USER_NAME = newUserName;

        browser.click(UserEdit.SUBMIT_BUTTON);
        browser.click(UserEdit.DIALOG_SUCCESS);

        editTestUser();

        assertEquals(newActive, browser.findElement(UserEdit.ACTIVE).isSelected());
        assertEquals(newLastName, browser.getValue(UserEdit.LASTNAME));
        assertEquals(newFormattedName, browser.getValue(UserEdit.FORMATTEDNAME));
        assertEquals(newGivenName, browser.getValue(UserEdit.GIVENNAME));
        assertEquals(newHonorificPrefix, browser.getValue(UserEdit.HONORIFICPREFIX));
        assertEquals(newHonorificSuffix, browser.getValue(UserEdit.HONORIFICSUFFIX));
        assertEquals(newMiddleName, browser.getValue(UserEdit.MIDDLENAME));
        assertEquals(newDisplayName, browser.getValue(UserEdit.DISPLAYNAME));
        assertEquals(newNickName, browser.getValue(UserEdit.NICKNAME));
        assertEquals(newUserTitle, browser.getValue(UserEdit.USERTITLE));

        assertEquals(newPreferredLanguage, browser.getValue(UserEdit.PREFERREDLANGUAGE));
        assertEquals(newLocale, browser.getValue(UserEdit.LOCALE));
        assertEquals(newProfileURL, browser.getValue(UserEdit.PROFILEURL));
        assertEquals(newTimezone, browser.getValue(UserEdit.TIMEZONE));
        assertEquals(newUserName, browser.getValue(UserEdit.USERNAME));
    }

    private void editTestUser() {
        String actionLabelXpath = "//td[. = '" + TEST_USER_NAME + "']/..//div[contains(@id, 'action-label')]";
        String editButtonXpath = "//td[. = '" + TEST_USER_NAME + "']/..//button[contains(@id, 'action-button-edit')]";

        browser.findElement(By.xpath(actionLabelXpath)).click();
        browser.findElement(By.xpath(editButtonXpath)).click();
    }
}