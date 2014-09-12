package org.osiam.addons.administration.integration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.osiam.addons.administration.Element.EditList;
import org.osiam.addons.administration.Element.GroupList;

public class UserDeleteIT extends Integrationtest{

    @Override
    public void setup() {
        super.setup();

        browser.doOauthLogin(ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    @Test
    public void deleteUser() {
        String username = "kmorris";
        String testGroup = "test_group08";

        deleteButton(username);

        browser.click(EditList.DIALOG_ABORT);

        assertTrue(isUserVisible(username));

        deleteButton(username);

        browser.click(EditList.DIALOG_SUCCESS);

        assertFalse(isUserVisible(username));

        browser.click(GroupList.GROUP_LIST);

        editTestGroup(testGroup);

        assertFalse(isUserVisible(username));
    }

    private void deleteButton(String username) {
        String actionLabelXpath =
            "//table//td[contains(., '" + username + "')]/..//span";

        String deleteButtonXpath =
                "//table//td[contains(., '" + username
                + "')]/..//button[starts-with(@id, 'action-button-delete-')]";

        browser.findElement(By.xpath(actionLabelXpath)).click();
        browser.findElement(By.xpath(deleteButtonXpath)).click();
    }

    private boolean isUserVisible(String username) {
        return browser.isTextPresent(username);
    }

    private void editTestGroup(String testGroup) {
        String actionLabelXpath = "//td[. = '" + testGroup + "']/..//div[contains(@id, 'action-label')]";
        String editButtonXpath = "//td[. = '" + testGroup + "']/..//button[contains(@id, 'action-button-edit')]";

        browser.findElement(By.xpath(actionLabelXpath)).click();
        browser.findElement(By.xpath(editButtonXpath)).click();
    }
}
