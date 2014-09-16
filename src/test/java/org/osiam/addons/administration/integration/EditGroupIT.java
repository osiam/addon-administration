package org.osiam.addons.administration.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.osiam.addons.administration.Element.EditList;
import org.osiam.addons.administration.Element.GroupEdit;
import org.osiam.addons.administration.Element.GroupList;
import org.osiam.addons.administration.selenium.Field;

public class EditGroupIT extends Integrationtest {
    String editGroup = "\u00c4pfel";
    String editExternalId = "300";
    String newGroup = "TestGroup";
    String newExternalId ="205";
    String testGroup = "test_group03";
    //Action-Menu-Button
    String actionLabelXpath = "//td['" + newGroup + "']/..//div[contains(@id, 'action-label')]";

    @Override
    public void setup() {
        super.setup();

        browser.doOauthLogin(ADMIN_USERNAME, ADMIN_PASSWORD);

        browser.click(GroupList.GROUP_LIST);
    }

    @Test
    public void GroupEdit() {
        editTestGroup(testGroup);

        browser.fill(new Field(EditList.DISPLAYNAME, ""));

        browser.click(EditList.SUBMIT_BUTTON);
        browser.click(EditList.DIALOG_SUCCESS);

        assertTrue(browser.isTextPresent("Die Eingabe darf nicht leer sein!") || browser.isTextPresent("The value can\u02c8t be empty!"));

        browser.fill(new Field(EditList.DISPLAYNAME, editGroup));
        browser.fill(new Field(GroupEdit.EXTERNAL_ID_GROUP, editExternalId));

        browser.click(EditList.SUBMIT_BUTTON);
        browser.click(EditList.DIALOG_SUCCESS);

        editTestGroup(editGroup);

        assertEquals(editGroup, browser.getValue(EditList.DISPLAYNAME));
        assertEquals(editExternalId, browser.getValue(GroupEdit.EXTERNAL_ID_GROUP));

        browser.click(EditList.CANCEL_BUTTON);
        browser.click(EditList.DIALOG_SUCCESS);
    }

    @Test
    public void GroupAddDelete() {
        //new Group
        String deleteGroup = "0";

        browser.click(GroupList.ADD_GROUP);

        browser.fill(new Field(EditList.DISPLAYNAME, newGroup));
        browser.fill(new Field(GroupEdit.EXTERNAL_ID_GROUP, newExternalId));

        browser.click(EditList.SUBMIT_BUTTON);
        browser.click(EditList.DIALOG_SUCCESS);

        editTestGroup(newGroup);

        assertEquals(newGroup, browser.getValue(EditList.DISPLAYNAME));
        assertEquals(newExternalId, browser.getValue(GroupEdit.EXTERNAL_ID_GROUP));

        browser.click(EditList.CANCEL_BUTTON);
        browser.click(EditList.DIALOG_SUCCESS);

        browser.findElement(By.xpath(actionLabelXpath)).click();

        deleteGroup(deleteGroup);

        browser.click(EditList.DIALOG_ABORT);

        assertTrue(browser.isTextPresent(newGroup));

        deleteGroup(deleteGroup);

        browser.click(EditList.DIALOG_SUCCESS);
        assertFalse(browser.isTextPresent(newGroup));
    }

    @Test
    public void GroupUserAddRemove() {
        final String testGroup = "test_group05";
        //dcooper
        final String userValue = "d6f323e2-c717-4ab6-af9c-e639b50a948c";

        editTestGroup(testGroup);

        selectExternUser(userValue);

        browser.click(GroupEdit.ADD_USER_GROUP);

        browser.click(EditList.SUBMIT_BUTTON);
        browser.click(EditList.DIALOG_SUCCESS);

        editTestGroup(testGroup);

        assertTrue(isUserGroup(userValue));

        selectGroupUser(userValue);

        browser.click(GroupEdit.REMOVE_USER_GROUP);

        browser.click(EditList.SUBMIT_BUTTON);
        browser.click(EditList.DIALOG_SUCCESS);

        editTestGroup(testGroup);

        assertFalse(isUserGroup(userValue));
    }

    private void editTestGroup(String testGroup) {
        String actionLabelXpath = "//td[. = '" + testGroup + "']/..//div[contains(@id, 'action-label')]";
        String editButtonXpath = "//td[. = '" + testGroup + "']/..//button[contains(@id, 'action-button-edit')]";

        browser.findElement(By.xpath(actionLabelXpath)).click();
        browser.findElement(By.xpath(editButtonXpath)).click();
    }

    private void deleteGroup(String id) {
        browser.findElement(By.id("action-label-" + id)).click();

        browser.findElement(By.id("action-button-delete-group-" + id)).click();
    }

    private void selectGroupUser(String userValue) {
        browser.findElement(By.xpath("//select[contains(@id, 'member')]/..//option[contains(@value, '" + userValue + "')]")).click();
    }
    private void selectExternUser(String userValue) {
        browser.findElement(By.xpath("//select[contains(@id, 'outsider')]/..//option[contains(@value, '" + userValue + "')]")).click();
    }

    private boolean isUserGroup(String userValue) {
        try {
            browser.findElement(By.xpath("//select[contains(@id, 'member')]/..//option[contains(@value, '" + userValue + "')]"));
        } catch (NoSuchElementException e) {
            return false;
        }

        return true;
    }
}
