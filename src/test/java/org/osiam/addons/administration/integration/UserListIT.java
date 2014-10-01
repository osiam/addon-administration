package org.osiam.addons.administration.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.osiam.addons.administration.EditList;
import org.osiam.addons.administration.Element;
import org.osiam.addons.administration.UserList;
import org.osiam.addons.administration.selenium.Field;

public class UserListIT extends Integrationtest {
	@Override
	public void setup() {
		super.setup();
		browser.doOauthLogin(ADMIN_USERNAME, ADMIN_PASSWORD);
	}

	@Test
	public void show_list_of_user() {
		assertTrue(isUserPresent(ADMIN_USERNAME));

		List<WebElement> availableUsers = browser.findElements(EditList.LIST_ROWS);
		assertTrue(availableUsers.size() > 3);
	}

	@Test
	public void apply_empty_filter() {
		int rowsBeforeFiltering = browser.findElements(EditList.LIST_ROWS).size();

		browser.click(EditList.FILTER_BUTTON);
		assertTrue(isUserPresent(ADMIN_USERNAME));

		int rowsAfterFiltering = browser.findElements(EditList.LIST_ROWS).size();

		assertEquals(rowsBeforeFiltering, rowsAfterFiltering);
	}

	@Test
	public void apply_single_filter() {
		testLoginFiltering();
		resetFilter();
		testFirstNameFiltering();
		resetFilter();
		testLastNameFiltering();
		resetFilter();
		testGroupNameFiltering();
	}

	@Test
	public void apply_multi_filter() {
		//Also tests trimming in filter fields
		browser.fill(new Field(UserList.FILTER_LOGIN, "adavies"),
				new Field(UserList.FILTER_GIVEN_NAME, "  Adeline  "),
				new Field(UserList.FILTER_FAMILY_NAME, "  Davies  "),
				new Field(UserList.FILTER_GROUP_NAME, " test_group08 "));
		browser.click(EditList.FILTER_BUTTON);

		assertTrue(isUserPresent("adavies"));
		assertEquals(getDisplayedUserCount(), 1);
	}

	@Test
	public void order() {
		browser.click(UserList.SORT_LOGIN_ASC);
		assertTrue(isUserAtPosition("adavies", 0));
		browser.click(UserList.SORT_GIVEN_NAME_ASC);
		assertTrue(isUserAtPosition("adavies", 0)); // Adeline
		browser.click(UserList.SORT_FAMILY_NAME_ASC);
		assertTrue(isUserAtPosition("gparker", 0)); // Barker

		browser.click(UserList.SORT_LOGIN_DESC);
		assertTrue(isUserAtPosition("marissa", 0));
		browser.click(UserList.SORT_GIVEN_NAME_DESC);
		assertTrue(isUserAtPosition("ewilley", 0)); // Willey
		browser.click(UserList.SORT_FAMILY_NAME_DESC);
		assertTrue(isUserAtPosition("jcambell", 0)); // Thompson
	}

	@Test
	public void limit() {
		// default limit: 20
		assertEquals("20", browser.findSelectedOption(EditList.LIMIT).getText());
		// change limit seticfied
		assertTrue(
				"Precondition is not satisfied! For this testcase the dataset must be contains more than 5 users!",
				getDisplayedUserCount() > 5);
		browser.fill(new Field(EditList.LIMIT, "5"));
		assertTrue(getDisplayedUserCount() <= 5);
		assertEquals("5", browser.findSelectedOption(EditList.LIMIT).getText());
	}

	@Test
	public void paging() {
		browser.fill(new Field(EditList.LIMIT, "5")); // set limit to 5
		int userCount = pageForward();
		pageBackward();

		browser.click(EditList.PAGING_LAST);
		assertFalse(browser.isErrorPage());
		browser.click(EditList.PAGING_FIRST);
		assertFalse(browser.isErrorPage());

		clickFirstPagingNumber();
		assertFalse(browser.isErrorPage());
		browser.fill(new Field(EditList.LIMIT, "0")); // set limit to
													// "unlimited"
		assertEquals(getDisplayedUserCount(), userCount);
	}

	@Test
	public void delete_user() {
		String username = "kmorris";
		String id = "9";

		clickActionButton(id);
		handleDeletionDialog(username, EditList.DIALOG_ABORT);
		assertTrue(isUserPresent(username));

		clickActionButton(id);
		handleDeletionDialog(username, EditList.DIALOG_CLOSE);
		assertTrue(isUserPresent(username));

		clickActionButton(id);
		handleDeletionDialog(username, EditList.DIALOG_SUCCESS);

		assertFalse(isUserPresent(username));
		assertFalse(getDisplayedUserCount() == 0);
	}

	@Test
	public void user_deactivation_dialog_should_deactivate_user() {
		String username = "hsimpson";
		// hsimpson
		String id = "7";
		// abort deactivation
		clickActionButton(id);
		handleDeactivationDialog(username, EditList.DIALOG_ABORT);
		assertTrue(isUserActive(id));
		// abort deactivation via closing dialog
		clickActionButton(id);
		handleDeactivationDialog(username, EditList.DIALOG_CLOSE);
		assertTrue(isUserActive(id));
		// deactivate user
		clickActionButton(id);
		handleDeactivationDialog(username, EditList.DIALOG_SUCCESS);
		assertTrue(isUserDeactive(username));
	}

	@Test
	public void user_activation_dialog_should_activate_user() {
		String username = "jcambell";
		// jcambell
		String id = "8";
		// abort activation
		clickActionButton(id);
		handleActivationDialog(username, EditList.DIALOG_ABORT);
		assertTrue(isUserDeactive(username));
		// abort activation via closing dialog
		clickActionButton(id);
		handleActivationDialog(username, EditList.DIALOG_CLOSE);
		assertTrue(isUserDeactive(username));
		// activate user
		clickActionButton(id);
		handleActivationDialog(username, EditList.DIALOG_SUCCESS);
		assertTrue(isUserActive(id));
	}

	private void pageBackward() {
		while (true) {
			try {
				browser.click(EditList.PAGING_PREVIOUS);
			} catch (NoSuchElementException e) {
				break;
			}
			assertFalse(browser.isErrorPage());
		}
	}

	private int pageForward() {
		int userCount = 0;

		while (true) {
			try {
				userCount += getDisplayedUserCount();
				browser.click(EditList.PAGING_NEXT);
			} catch (NoSuchElementException e) {
				break;
			}
			assertFalse(browser.isErrorPage());
		}
		return userCount;
	}

	private void clickFirstPagingNumber() {
		String pagingNumberXpath = "//a[contains(@id, 'paging-') "
				+ "and not(@id = 'paging-first') "
				+ "and not(@id = 'paging-prev') "
				+ "and not(@id = 'paging-next') "
				+ "and not(@id = 'paging-last') " + "and not(@href = '#')]";
		browser.findElements(By.xpath(pagingNumberXpath)).get(0).click();
	}

	private void clickActionButton(String id) {
		String actionLabelId = "action-label-";
		browser.findElement(By.id(actionLabelId + id)).click();
	}

	private void handleDeactivationDialog(String username,
			Element elementToClick) {
		String deactivateButtonXpath = "//table//td[contains(., "
				+ username
				+ ")]/..//button[starts-with(@id, 'action-button-deactivate-')]";
		handleDialog(deactivateButtonXpath, elementToClick);
	}

	private void handleActivationDialog(String username, Element elementToClick) {
		String deactivateButtonXpath = "//table//td[contains(., '" + username
				+ "')]/..//button[starts-with(@id, 'action-button-activate-')]";
		handleDialog(deactivateButtonXpath, elementToClick);
	}

	private void handleDeletionDialog(String username, Element elementToClick) {
			String deleteButtonXpath =
					"//table//td[contains(., '" + username
					+ "')]/..//button[starts-with(@id, 'action-button-delete-')]";

		handleDialog(deleteButtonXpath, elementToClick);
	}

	private int getDisplayedUserCount() {
		String userRowsXpath = "//table//tr//li[contains(@class, 'action')]";

		try {
			return browser.findElements(By.xpath(userRowsXpath)).size();
		}catch(NoSuchElementException e) {
			return 0;
		}

	}

	private boolean isUserPresent(String username) {
		return browser.isTextPresent(username);
	}

	private boolean isUserActive(String id) {
		try {
			browser.findElement(By.id("action-button-deactivate-" + id));
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}

	private boolean isUserDeactive(String username) {
		String buttonXpath = "//table//tr[contains(@class, 'danger')]//td[contains(., '"
				+ username + "')]";
		try {
			browser.findElement(By.xpath(buttonXpath));
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}

	private boolean isUserAtPosition(String username, Integer position) {
		// the row #2 is the first user-row!
		String rowXpath = "//table//tr[" + (2 - position)
				+ "]//td[contains(., '" + username + "')]";
		return browser.findElements(By.xpath(rowXpath)).size() == 1;
	}

	private void testLoginFiltering() {
		String testLogin = "bjensen";

		browser.fill(new Field(UserList.FILTER_LOGIN, testLogin));
		browser.click(EditList.FILTER_BUTTON);
		List<WebElement> availableUsers = browser.findElements(EditList.LIST_ROWS);

		assertTrue(isUserPresent(testLogin));
		assertEquals(availableUsers.size(), 2);
	}

	private void testFirstNameFiltering() {
		String testFirstName = "Barbara";

		browser.fill(new Field(UserList.FILTER_GIVEN_NAME, testFirstName));
		browser.click(EditList.FILTER_BUTTON);
		List<WebElement> availableUsers = browser.findElements(EditList.LIST_ROWS);

		assertTrue(browser.isTextPresent(testFirstName));
		assertEquals(availableUsers.size(), 2);
	}

	private void testLastNameFiltering() {
		String testLastName = "Jensen";

		browser.fill(new Field(UserList.FILTER_FAMILY_NAME, testLastName));
		browser.click(EditList.FILTER_BUTTON);
		List<WebElement> availableUsers = browser.findElements(EditList.LIST_ROWS);

		assertTrue(browser.isTextPresent(testLastName));
		assertEquals(availableUsers.size(), 2);
	}

	private void testGroupNameFiltering() {
		String testGroupName = "test_group01";

		browser.fill(new Field(UserList.FILTER_GROUP_NAME, testGroupName));
		browser.click(EditList.FILTER_BUTTON);
		List<WebElement> availableUsers = browser.findElements(EditList.LIST_ROWS);

		assertTrue(browser.isTextPresent(testGroupName));
		assertEquals(availableUsers.size(), 2);
	}

	private void resetFilter() {
		browser.clear(UserList.FILTER_LOGIN,
					UserList.FILTER_GIVEN_NAME,
					UserList.FILTER_FAMILY_NAME,
					UserList.FILTER_GROUP_NAME);
	}

	private void handleDialog(String activateDialogXpath, Element elementToClick) {
		browser.findElement(By.xpath(activateDialogXpath)).click();
		browser.findElement(elementToClick).click();
	}
}
