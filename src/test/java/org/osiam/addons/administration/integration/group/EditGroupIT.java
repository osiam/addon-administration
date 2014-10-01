package org.osiam.addons.administration.integration.group;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.osiam.addons.administration.elements.EditList;
import org.osiam.addons.administration.elements.group.GroupEdit;
import org.osiam.addons.administration.elements.group.GroupList;
import org.osiam.addons.administration.elements.group.GroupMembership;
import org.osiam.addons.administration.integration.Integrationtest;
import org.osiam.addons.administration.selenium.Field;

public class EditGroupIT extends Integrationtest {
	private static final String TEST_GROUP_NAME = "test_group03";

	private static final String NEW_GROUP_NAME = "TestGroup";
	private static final String NEW_EXTERNAL_ID ="205";

	@Override
	public void setup() {
		super.setup();

		browser.doOauthLogin(ADMIN_USERNAME, ADMIN_PASSWORD);

		browser.click(GroupList.GROUP_LIST);
	}

	@Test
	public void edit_group() {
		gotoEditGroupView(TEST_GROUP_NAME);

		checkEmptyValidation();
		checkSuccessfulEdit();
	}

	@Test
	public void add_and_delete_group() {
		createNewGroup();
		deleteNewGroup();
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
		assertTrue(browser.isTextPresent("Das ausgew\u00e4hlte Objekt wurde erfolgreich gel\u00f6scht") ||
				browser.isTextPresent("The selected option was deleted"));
		assertFalse(browser.isTextPresent(NEW_GROUP_NAME));
	}

	@Test
	public void add_and_remove_group_member() {
		final String testGroup = "test_group08";
		//dcooper
		final String userId = "d6f323e2-c717-4ab6-af9c-e639b50a948c";

		gotoMembershipGroupView(testGroup);

		assignUser(userId);

		assertTrue(isUserAssigned(userId));

		unassignUser(userId);

		assertFalse(isUserAssigned(userId));
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

	private void gotoMembershipGroupView(String groupName) {
		String actionLabelXpath = "//td[. = '" + groupName + "']/..//div[contains(@id, 'action-label')]";
		String editMembershipButtonXpath = "//td[. = '" + groupName + "']/..//button[contains(@id, 'action-button-edit-user-membership')]";

		browser.findElement(By.xpath(actionLabelXpath)).click();
		browser.findElement(By.xpath(editMembershipButtonXpath)).click();
	}

	private void clickDeleteGroup(String groupName) {
		String actionLabelXpath = "//td[. = '" + groupName + "']/..//div[contains(@id, 'action-label')]";
		String deleteButtonXpath = "//td[. = '" + groupName + "']/..//button[contains(@id, 'action-button-delete')]";

		browser.findElement(By.xpath(actionLabelXpath)).click();
		browser.findElement(By.xpath(deleteButtonXpath)).click();
	}

	private void unassignUser(String userValue) {
		String unassignUserIconXpath = "//div[contains(@id, 'form-group-insider')]//a[contains(@href, '" + userValue + "')]";
		browser.findElement(By.xpath(unassignUserIconXpath)).click();
	}

	private void assignUser(String userValue) {
		String assignUserIconXpath = "//div[contains(@id, 'form-group-outsider')]//a[contains(@href, '" + userValue + "')]";
		browser.findElement(By.xpath(assignUserIconXpath)).click();
	}

	@Test
	public void assing_multiple_user() {
		final String testGroup = "test_group06";
		final String allUnassignedUserCheckbox = "//table[contains(@id, 'outsider')]//input[contains(@id, 'group-checkbox')]";
		final String submitUnassigendButton = "//table[contains(@id, 'outsider')]//input[contains(@type, 'submit')]";

		gotoMembershipGroupView(testGroup);

		browser.fill(new Field(GroupMembership.LIMIT_UNASSIGNED, "0")); // set limit to
																		// "unlimited"
		int allUser = getAssignedUser("outsider");

		browser.findElement(By.xpath(allUnassignedUserCheckbox)).click();

		browser.findElement(By.xpath(submitUnassigendButton)).click();

		browser.fill(new Field(GroupMembership.LIMIT_ASSIGNED, "0"));

		assertEquals(allUser, getAssignedUser("insider"));
	}

	@Test
	public void unassign_multiple_user() {
		final String testGroup = "test_group06";
		final String allUnassignedUserCheckbox = "//table[contains(@id, 'outsider')]//input[contains(@id, 'group-checkbox')]";
		final String allAssignedUserCheckbox = "//table[contains(@id, 'insider')]//input[contains(@id, 'group-checkbox')]";
		final String submitAssignedButton = "//table[contains(@id, 'insider')]//input[contains(@type, 'submit')]";
		final String submitUnassignedButton = "//table[contains(@id, 'outsider')]//input[contains(@type, 'submit')]";

		gotoMembershipGroupView(testGroup);

		//Add user to group
		browser.fill(new Field(GroupMembership.LIMIT_UNASSIGNED, "0"));

		browser.findElement(By.xpath(allUnassignedUserCheckbox)).click();

		browser.findElement(By.xpath(submitUnassignedButton)).click();

		//Remove user to group
		browser.fill(new Field(GroupMembership.LIMIT_ASSIGNED, "0"));

		browser.findElement(By.xpath(allAssignedUserCheckbox)).click();

		browser.findElement(By.xpath(submitAssignedButton)).click();

		assertEquals(0, getAssignedUser("insider"));
	}

	@Test
	public void paging_assigned_user() {
		final String testGroup = "test_group05";

		gotoMembershipGroupView(testGroup);

		browser.fill(new Field(GroupMembership.LIMIT_ASSIGNED, "5")); // set limit to 5
		int userCount = pageForwardAssigned();
		pageBackwardAssigned();

		browser.click(GroupMembership.PAGING_ASSIGNED_LAST);
		assertFalse(browser.isErrorPage());
		browser.click(GroupMembership.PAGING_ASSIGNED_FIRST);
		assertFalse(browser.isErrorPage());

		clickFirstPagingNumberAssigned();
		assertFalse(browser.isErrorPage());
		browser.fill(new Field(GroupMembership.LIMIT_ASSIGNED, "0")); // set limit to
																	// "unlimited"
		assertEquals(getAssignedUser("insider"), userCount);
	}

	private void pageBackwardAssigned() {
		while (true) {
			try {
				browser.click(GroupMembership.PAGING_ASSIGNED_PREVIOUS);

				assertTrue(firstPageUnassigned());
				//adavies
				assertTrue(isUserUnassigned("03dc8f50-acaa-44d6-9401-bdfc5e10e821"));
			} catch (NoSuchElementException e) {
				break;
			}
			assertFalse(browser.isErrorPage());
		}
	}

	private int pageForwardAssigned() {
		int userCount = 0;

		while (true) {
			try {
				userCount += getAssignedUser("insider");
				browser.click(GroupMembership.PAGING_ASSIGNED_NEXT);

				assertTrue(firstPageUnassigned());
				//adavies
				assertTrue(isUserUnassigned("03dc8f50-acaa-44d6-9401-bdfc5e10e821"));
			} catch (NoSuchElementException e) {
				break;
			}
			assertFalse(browser.isErrorPage());
		}
		return userCount;
	}

	private void clickFirstPagingNumberAssigned() {
		String pagingNumberXpath = "//div[contains(@id, 'form-group-insider')]//a[contains(@id, 'paging-') "
				+ "and not(@id = 'paging-first') "
				+ "and not(@id = 'paging-prev') "
				+ "and not(@id = 'paging-next') "
				+ "and not(@id = 'paging-last') " + "and not(@href = '#')]";
		browser.findElements(By.xpath(pagingNumberXpath)).get(0).click();

		assertTrue(firstPageUnassigned());
		//adavies
		assertTrue(isUserUnassigned("03dc8f50-acaa-44d6-9401-bdfc5e10e821"));
	}


	private boolean firstPageUnassigned() {
		String firstPageActive = "//div[contains(@id, 'form-group-outsider')]//li[contains(@class, 'active')]//a[contains(@id, 'paging-0')]";

		try {
			browser.findElement(By.xpath(firstPageActive));
		}
		catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}

	private boolean isUserUnassigned(String userValue) {
		try {
			browser.findElement(By.xpath("//table[contains(@id, 'outsider')]//a[contains(@href, '" + userValue + "')]"));
		} catch (NoSuchElementException e) {
			return false;
		}

		return true;
	}

	@Test
	public void paging_unassigned_user() {
		final String testGroup = "test_group04";

		gotoMembershipGroupView(testGroup);

		browser.fill(new Field(GroupMembership.LIMIT_UNASSIGNED, "5")); // set limit to 5
		int userCount = pageForwardUnassigned();
		pageBackwardUnassigned();

		browser.click(GroupMembership.PAGING_UNASSIGNED_LAST);
		assertFalse(browser.isErrorPage());
		browser.click(GroupMembership.PAGING_UNASSIGNED_FIRST);
		assertFalse(browser.isErrorPage());

		clickFirstPagingNumberUnassigned();
		assertFalse(browser.isErrorPage());
		browser.fill(new Field(GroupMembership.LIMIT_UNASSIGNED, "0")); // set limit to
																		// "unlimited"
		assertEquals(getAssignedUser("outsider"), userCount);
	}

	private void pageBackwardUnassigned() {
		while (true) {
			try {
				browser.click(GroupMembership.PAGING_UNASSIGNED_PREVIOUS);

				assertTrue(firstPageAssigned());
				//adavies
				assertTrue(isUserAssigned("03dc8f50-acaa-44d6-9401-bdfc5e10e821"));
			} catch (NoSuchElementException e) {
				break;
			}
			assertFalse(browser.isErrorPage());
		}
	}

	private int pageForwardUnassigned() {
		int userCount = 0;

		while (true) {
			try {
				userCount += getAssignedUser("outsider");
				browser.click(GroupMembership.PAGING_UNASSIGNED_NEXT);

				assertTrue(firstPageAssigned());
				//adavies
				assertTrue(isUserAssigned("03dc8f50-acaa-44d6-9401-bdfc5e10e821"));
			} catch (NoSuchElementException e) {
				break;
			}
			assertFalse(browser.isErrorPage());
		}
		return userCount;
	}

	private void clickFirstPagingNumberUnassigned() {
		String pagingNumberXpath = "//div[contains(@id, 'form-group-outsider')]//a[contains(@id, 'paging-') "
				+ "and not(@id = 'paging-first') "
				+ "and not(@id = 'paging-prev') "
				+ "and not(@id = 'paging-next') "
				+ "and not(@id = 'paging-last') " + "and not(@href = '#')]";
		browser.findElements(By.xpath(pagingNumberXpath)).get(0).click();

		assertTrue(firstPageAssigned());
		//adavies
		assertTrue(isUserAssigned("03dc8f50-acaa-44d6-9401-bdfc5e10e821"));
	}

	private boolean firstPageAssigned() {
		String firstPageActive = "//div[contains(@id, 'form-group-insider')]//li[contains(@class, 'active')]//a[contains(@id, 'paging-0')]";

		try {
			browser.findElement(By.xpath(firstPageActive));
		}
		catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}

	private int getAssignedUser(String visibility) {
		String userRowsXpath = "//table[contains(@id,'" + visibility + "')]//tr//td//input[contains(@class, 'checkbox')]";
		return browser.findElements(By.xpath(userRowsXpath)).size();
	}

	private boolean isUserAssigned(String userValue) {
		try {
			browser.findElement(By.xpath("//table[contains(@id, 'insider')]//a[contains(@href, '" + userValue + "')]"));
		} catch (NoSuchElementException e) {
			return false;
		}

		return true;
	}
}
