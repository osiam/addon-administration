package org.osiam.addons.administration.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.osiam.addons.administration.GroupList;
import org.osiam.addons.administration.GroupMembership;
import org.osiam.addons.administration.selenium.Field;

public class GroupMembershipIT extends Integrationtest {

	@Override
	public void setup() {
		super.setup();

		browser.doOauthLogin(ADMIN_USERNAME, ADMIN_PASSWORD);
		browser.click(GroupList.GROUP_LIST);
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

	private void assignUser(String userValue) {
		String assignUserIconXpath = "//div[contains(@id, 'form-group-outsider')]//a[contains(@href, '" + userValue + "')]";
		browser.findElement(By.xpath(assignUserIconXpath)).click();
	}

	private void unassignUser(String userValue) {
		String unassignUserIconXpath = "//div[contains(@id, 'form-group-insider')]//a[contains(@href, '" + userValue + "')]";
		browser.findElement(By.xpath(unassignUserIconXpath)).click();
	}

	//TODO
	//Fix me "Get groups in list" (Bug in Server)
	@Test
	public void compare_user_amount() {
		final String allUnassignedUserCheckbox = "//table[contains(@id, 'outsider')]//input[contains(@id, 'group-checkbox')]";
		final String submitUnassigendButton = "//table[contains(@id, 'outsider')]//input[contains(@type, 'submit')]";

		final String testGroup = "test_group05";

		gotoMembershipGroupView(testGroup);

		browser.fill(new Field(GroupMembership.LIMIT_ASSIGNED, "0")); // set limit to
																	// "unlimited"
		browser.fill(new Field(GroupMembership.LIMIT_UNASSIGNED, "0"));

		browser.findElement(By.xpath(allUnassignedUserCheckbox)).click();

		browser.findElement(By.xpath(submitUnassigendButton)).click();

		assertEquals(getAssignedUser("insider"), getAssignedUser("outsider"));
	}

	private void gotoMembershipGroupView(String groupName) {
		String actionLabelXpath = "//td[. = '" + groupName + "']/..//div[contains(@id, 'action-label')]";
		String editMembershipButtonXpath = "//td[. = '" + groupName + "']/..//button[contains(@id, 'action-button-edit-user-membership')]";

		browser.findElement(By.xpath(actionLabelXpath)).click();
		browser.findElement(By.xpath(editMembershipButtonXpath)).click();
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

	private int getAssignedUser(String membership) {
		String userRowsXpath = "//table[contains(@id,'" + membership + "')]//tr//td//input[contains(@class, 'checkbox')]";
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
