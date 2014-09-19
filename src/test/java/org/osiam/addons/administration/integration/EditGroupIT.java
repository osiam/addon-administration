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
	private static final String TEST_GROUP_NAME = "test_group03";

	private static final String NEW_GROUP_NAME = "TestGroup";
	private static final String NEW_EXTERNAL_ID ="205";

	@Override
	public void setup() {
		super.setup();

		browser.doOauthLogin(ADMIN_USERNAME, ADMIN_PASSWORD);

		browser.click(EditList.GROUP_LIST);
	}

	@Test
	public void edit_group() {
		gotoEditGroupView(TEST_GROUP_NAME);

		checkEmptyValidation();
		checkSuccessfulEdit();
	}

	private void checkSuccessfulEdit() {
		final String newGroupName = "\u00c4pfel";
		final String newExternalId = "300";

		browser.fill(new Field(EditList.DISPLAYNAME, newGroupName));
		browser.fill(new Field(GroupEdit.EXTERNAL_ID_GROUP, newExternalId));

		saveEditChanges();

		gotoEditGroupView(newGroupName);

		assertEquals(newGroupName, browser.getValue(EditList.DISPLAYNAME));
		assertEquals(newExternalId, browser.getValue(GroupEdit.EXTERNAL_ID_GROUP));

		browser.click(EditList.CANCEL_BUTTON);
		browser.click(EditList.DIALOG_SUCCESS);
	}

	private void checkEmptyValidation() {
		browser.fill(new Field(EditList.DISPLAYNAME, ""));

		saveEditChanges();

		assertTrue(browser.isTextPresent("Die Eingabe darf nicht leer sein!") || browser.isTextPresent("The value can\u02c8t be empty!"));
	}

	@Test
	public void add_and_delete_group() {
		createNewGroup();
		deleteNewGroup();
	}

	private void createNewGroup() {
		browser.click(GroupList.ADD_GROUP);

		browser.fill(new Field(EditList.DISPLAYNAME, NEW_GROUP_NAME));
		browser.fill(new Field(GroupEdit.EXTERNAL_ID_GROUP, NEW_EXTERNAL_ID));

		saveEditChanges();

		gotoEditGroupView(NEW_GROUP_NAME);

		assertEquals(NEW_GROUP_NAME, browser.getValue(EditList.DISPLAYNAME));
		assertEquals(NEW_EXTERNAL_ID, browser.getValue(GroupEdit.EXTERNAL_ID_GROUP));

		browser.click(EditList.CANCEL_BUTTON);
		browser.click(EditList.DIALOG_SUCCESS);
	}

	private void deleteNewGroup() {
		clickDeleteGroup(NEW_GROUP_NAME);

		browser.click(EditList.DIALOG_ABORT);
		assertTrue(browser.isTextPresent(NEW_GROUP_NAME));

		clickDeleteGroup(NEW_GROUP_NAME);

		browser.click(EditList.DIALOG_SUCCESS);
		assertTrue(browser.isTextPresent("Das ausgew\u00e4hlte Objekt wurde erfolgreich gel\u00f6scht") || browser.isTextPresent("The selected option was deleted"));
		assertFalse(browser.isTextPresent(NEW_GROUP_NAME));
	}

	@Test
	public void add_and_remove_group_member() {
		final String testGroup = "test_group05";
		//dcooper
		final String userId = "d6f323e2-c717-4ab6-af9c-e639b50a948c";

		gotoEditGroupView(testGroup);
		addUserToMemberlist(userId);
		saveEditChanges();

		gotoEditGroupView(testGroup);
		assertTrue(isUserGroupMember(userId));

		removeUserFromMembers(userId);
		saveEditChanges();
		gotoEditGroupView(testGroup);

		assertFalse(isUserGroupMember(userId));
	}

	private void saveEditChanges() {
		browser.click(EditList.SUBMIT_BUTTON);
		browser.click(EditList.DIALOG_SUCCESS);
	}

	private void gotoEditGroupView(String groupName) {
		String actionLabelXpath = "//td[. = '" + groupName + "']/..//div[contains(@id, 'action-label')]";
		String editButtonXpath = "//td[. = '" + groupName + "']/..//button[contains(@id, 'action-button-edit')]";

		browser.findElement(By.xpath(actionLabelXpath)).click();
		browser.findElement(By.xpath(editButtonXpath)).click();
	}

	private void clickDeleteGroup(String groupName) {
		String actionLabelXpath = "//td[. = '" + groupName + "']/..//div[contains(@id, 'action-label')]";
		String editButtonXpath = "//td[. = '" + groupName + "']/..//button[contains(@id, 'action-button-delete')]";

		browser.findElement(By.xpath(actionLabelXpath)).click();
		browser.findElement(By.xpath(editButtonXpath)).click();
	}

	private void removeUserFromMembers(String userValue) {
		browser.findElement(By.xpath("//select[contains(@id, 'member')]/..//option[contains(@value, '" + userValue + "')]")).click();
		browser.click(GroupEdit.REMOVE_USER_GROUP);
	}
	private void addUserToMemberlist(String userValue) {
		browser.findElement(By.xpath("//select[contains(@id, 'outsider')]/..//option[contains(@value, '" + userValue + "')]")).click();
		browser.click(GroupEdit.ADD_USER_GROUP);
	}

	private boolean isUserGroupMember(String userValue) {
		try {
			browser.findElement(By.xpath("//select[contains(@id, 'member')]/..//option[contains(@value, '" + userValue + "')]"));
		} catch (NoSuchElementException e) {
			return false;
		}

		return true;
	}
}
