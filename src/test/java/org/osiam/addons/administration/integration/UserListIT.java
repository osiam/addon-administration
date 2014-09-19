package org.osiam.addons.administration.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.osiam.addons.administration.Element;
import org.osiam.addons.administration.Element.EditList;
import org.osiam.addons.administration.Element.UserList;
import org.osiam.addons.administration.selenium.Field;

public class UserListIT extends Integrationtest {
	@Override
	public void setup() {
		super.setup();
		browser.doOauthLogin(ADMIN_USERNAME, ADMIN_PASSWORD);
	}

	@Test
	public void show_list_of_user() {
		assertTrue(isUserVisible(ADMIN_USERNAME));
	}

	@Test
	public void apply_empty_filter() {
		browser.click(EditList.FILTER_BUTTON);
		assertTrue(isUserVisible(ADMIN_USERNAME));
	}

	@Test
	public void apply_single_filter() {
		browser.fill(new Field(UserList.FILTER_LOGIN, ADMIN_USERNAME));
		browser.click(EditList.FILTER_BUTTON);
		assertTrue(isUserVisible(ADMIN_USERNAME));
		assertFalse(isUserVisible("adavies")); // an another user
	}

	@Test
	public void apply_multi_filter() {
		//Also tests trimming in filter fields
		browser.fill(new Field(UserList.FILTER_GIVEN_NAME, "  Adeline  "),
				new Field(UserList.FILTER_FAMILY_NAME, "  Davies  "),
				new Field(UserList.FILTER_GROUP_NAME, " test_group08 "));
		browser.click(EditList.FILTER_BUTTON);
		assertFalse(isUserVisible(ADMIN_USERNAME));
		assertTrue(isUserVisible("adavies")); // an another user
	}

	@Test
	public void apply_no_result_filter() {
		browser.fill(new Field(UserList.FILTER_LOGIN, "DoesNotExist"));
		browser.click(EditList.FILTER_BUTTON);
		assertEquals(0, getDisplayedUser());
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
				getDisplayedUser() > 5);
		browser.fill(new Field(EditList.LIMIT, "5"));
		assertTrue(getDisplayedUser() <= 5);
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
		assertEquals(getDisplayedUser(), userCount);
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
				userCount += getDisplayedUser();
				browser.click(EditList.PAGING_NEXT);
			} catch (NoSuchElementException e) {
				break;
			}
			assertFalse(browser.isErrorPage());
		}
		return userCount;
	}

	@Test
	public void user_deactivation_dialog_should_deactivate_user() {
		String username = "hsimpson";
		// hsimpson
		String id = "7";
		// abort deactivation
		actionButton(id);
		handleDeactivationDialog(username, EditList.DIALOG_ABORT);
		assertTrue(isUserActive(username, id));
		// abort deactivation via closing dialog
		actionButton(id);
		handleDeactivationDialog(username, EditList.DIALOG_CLOSE);
		assertTrue(isUserActive(username, id));
		// deactivate user
		actionButton(id);
		handleDeactivationDialog(username, EditList.DIALOG_SUCCESS);
		assertTrue(isUserDeactive(username));
	}

	@Test
	public void user_activation_dialog_should_activate_user() {
		String username = "jcambell";
		// jcambell
		String id = "8";
		// abort activation
		actionButton(id);
		handleActivationDialog(username, EditList.DIALOG_ABORT);
		assertTrue(isUserDeactive(username));
		// abort activation via closing dialog
		actionButton(id);
		handleActivationDialog(username, EditList.DIALOG_CLOSE);
		assertTrue(isUserDeactive(username));
		// activate user
		actionButton(id);
		handleActivationDialog(username, EditList.DIALOG_SUCCESS);
		assertTrue(isUserActive(username, id));
	}

	private void clickFirstPagingNumber() {
		String pagingNumberXpath = "//a[contains(@id, 'paging-') "
				+ "and not(@id = 'paging-first') "
				+ "and not(@id = 'paging-prev') "
				+ "and not(@id = 'paging-next') "
				+ "and not(@id = 'paging-last') " + "and not(@href = '#')]";
		browser.findElements(By.xpath(pagingNumberXpath)).get(0).click();
	}

	private void actionButton(String id) {
		String actionLabelId = "action-label-";
		browser.findElement(By.id(actionLabelId + id)).click();
	}

	private void handleDeactivationDialog(String username,
			Element elementToClick) {
		String deactivateButtonXpath = "//table//td[contains(., "
				+ username
				+ ")]/..//button[starts-with(@id, 'action-button-deactivate-')]";
		browser.findElement(By.xpath(deactivateButtonXpath)).click();
		browser.findElement(elementToClick).click();
	}

	private void handleActivationDialog(String username, Element elementToClick) {
		String deactivateButtonXpath = "//table//td[contains(., '" + username
				+ "')]/..//button[starts-with(@id, 'action-button-activate-')]";
		browser.findElement(By.xpath(deactivateButtonXpath)).click();
		browser.findElement(elementToClick).click();
	}

	private int getDisplayedUser() {
		String userRowsXpath = "//table//tr//li[contains(@class, 'action')]";
		return browser.findElements(By.xpath(userRowsXpath)).size();
	}

	private boolean isUserVisible(String username) {
		return browser.isTextPresent(username);
	}

	private boolean isUserActive(String username, String id) {
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
}
