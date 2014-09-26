package org.osiam.addons.administration.integration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.osiam.addons.administration.EditList;
import org.osiam.addons.administration.GroupList;

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

		clickDeleteButton(username);
		browser.click(EditList.DIALOG_ABORT);

		assertTrue(isUserVisible(username));

		clickDeleteButton(username);
		browser.click(EditList.DIALOG_SUCCESS);

		assertFalse(isUserVisible(username));

		gotoEditGroup(testGroup);
		assertFalse(isUserVisible(username));
	}

	private void clickDeleteButton(String username) {
		String actionLabelXpath =
			"//table//td[contains(., '" + username + "')]/..//span";

		String deleteButtonXpath =
				"//table//td[contains(., '" + username
				+ "')]/..//button[starts-with(@id, 'action-button-delete-')]";

		browser.findElement(By.xpath(actionLabelXpath)).click();
		browser.findElement(By.xpath(deleteButtonXpath)).click();
	}

	private void gotoEditGroup(String groupName) {
		browser.click(GroupList.GROUP_LIST);

		String actionLabelXpath = "//td[. = '" + groupName + "']/..//div[contains(@id, 'action-label')]";
		String editButtonXpath = "//td[. = '" + groupName + "']/..//button[contains(@id, 'action-button-edit')]";

		browser.findElement(By.xpath(actionLabelXpath)).click();
		browser.findElement(By.xpath(editButtonXpath)).click();
	}

	private boolean isUserVisible(String username) {
		return browser.isTextPresent(username);
	}
}
