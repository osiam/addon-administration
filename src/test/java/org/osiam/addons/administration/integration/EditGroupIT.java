package org.osiam.addons.administration.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.osiam.addons.administration.Element.EditList;
import org.osiam.addons.administration.Element.GroupEdit;
import org.osiam.addons.administration.Element.GroupList;
import org.osiam.addons.administration.selenium.Field;

public class EditGroupIT extends Integrationtest {
    @Override
    public void setup() {
        super.setup();

        browser.doOauthLogin(ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    @Test
    public void Group() {
        String editGroup = "Äpfel";
        String editExternalId = "300";
        String newGroup = "TestGroup";
        String newExternalId ="205";
        String testGroup = "test_group03";
        //Action-Menu-Button
        String actionLabelXpath = "//td[. = '" + newGroup + "']/..//div[contains(@id, 'action-label')]";
        
        browser.click(GroupList.GROUP_LIST);
        
        editTestGroup(testGroup);
        
        browser.fill(new Field(EditList.DISPLAYNAME, ""));
        
        browser.click(EditList.SUBMIT_BUTTON);
        browser.click(EditList.DIALOG_SUCCESS);
        
        assertTrue(browser.isTextPresent("darf nicht leer sein"));
        
        browser.fill(new Field(EditList.DISPLAYNAME, editGroup));
        browser.fill(new Field(GroupEdit.EXTERNAL_ID_GROUP, editExternalId));
        
        browser.click(EditList.SUBMIT_BUTTON);
        browser.click(EditList.DIALOG_SUCCESS);
        
        editTestGroup(editGroup);
        
        assertEquals(editGroup, browser.getValue(EditList.DISPLAYNAME));
        assertEquals(editExternalId, browser.getValue(GroupEdit.EXTERNAL_ID_GROUP));
        
        browser.click(EditList.CANCEL_BUTTON);
        browser.click(EditList.DIALOG_SUCCESS);
        
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
        browser.click(GroupList.DELETE_GROUP);
        browser.click(EditList.DIALOG_ABORT);
        
        assertTrue(browser.isTextPresent(newGroup));
        
        browser.findElement(By.xpath(actionLabelXpath)).click();
        browser.click(GroupList.DELETE_GROUP);
        browser.click(EditList.DIALOG_SUCCESS);
        assertFalse(browser.isTextPresent(newGroup));
    }
    
    private void editTestGroup(String testGroup) {
        String actionLabelXpath = "//td[. = '" + testGroup + "']/..//div[contains(@id, 'action-label')]";
        String editButtonXpath = "//td[. = '" + testGroup + "']/..//button[contains(@id, 'action-button-edit')]";

        browser.findElement(By.xpath(actionLabelXpath)).click();
        browser.findElement(By.xpath(editButtonXpath)).click();
    }
}
