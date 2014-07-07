package org.osiam.addons.administration.integration;

import static org.junit.Assert.*;

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
    
    private static final String TEST_USER_NAME = "adavies";
    private static final String TEST_USER_FIRST_NAME = "Adeline";
    private static final String TEST_USER_LAST_NAME = "Davies";
    private static final String TEST_USER_MAIL = "";
    
    @Test
    public void edit_user() {
        editTestUser();

        final String newFirstName = "Test123";
        final String newLastName = "Test456";
        final String newMail = "Test123@abc.de";

        browser.fill(new Field(UserEdit.FIRSTNAME, newFirstName));
        browser.fill(new Field(UserEdit.LASTNAME, newLastName));
        browser.fill(new Field(UserEdit.MAIL, newMail));

        browser.click(UserEdit.CANCEL_BUTTON);

        editTestUser();

        // Nothing should be saved
        assertEquals(TEST_USER_FIRST_NAME, browser.getValue(UserEdit.FIRSTNAME));
        assertEquals(TEST_USER_LAST_NAME, browser.getValue(UserEdit.LASTNAME));
        assertEquals(TEST_USER_MAIL, browser.getValue(UserEdit.MAIL));

        // ///////////////

        browser.fill(new Field(UserEdit.FIRSTNAME, newFirstName));
        browser.fill(new Field(UserEdit.LASTNAME, newLastName));
        browser.fill(new Field(UserEdit.MAIL, newMail));

        browser.click(UserEdit.SUBMIT_BUTTON);
        assertFalse(browser.isErrorPage());
        browser.click(UserEdit.CANCEL_BUTTON);

        editTestUser();
        
        // New Data should be saved
        assertEquals(newFirstName, browser.getValue(UserEdit.FIRSTNAME)); 
        assertEquals(newLastName, browser.getValue(UserEdit.LASTNAME)); 
        assertEquals(newMail, browser.getValue(UserEdit.MAIL)); 

        // ////////////////

        browser.fill(new Field(UserEdit.MAIL, "notValid"));
        browser.click(UserEdit.SUBMIT_BUTTON);
        
        // An error should be occurred
        assertTrue(browser.isErrorPage());
    }

    private void editTestUser() {
        String editButtonXpath = "//td[. = '" + TEST_USER_NAME + "']/..//button[contains(@id, 'action-button-edit')]";
        browser.findElement(By.xpath(editButtonXpath)).click();
    }
}