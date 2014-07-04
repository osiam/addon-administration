package org.osiam.addons.administration.integration;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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
        browser.click(UserList.FILTER_BUTTON);

        assertTrue(isUserVisible(ADMIN_USERNAME));
    }

    @Test
    public void apply_single_filter() {
        browser.fill(new Field(UserList.FILTER_LOGIN, ADMIN_USERNAME));
        browser.click(UserList.FILTER_BUTTON);

        assertTrue(isUserVisible(ADMIN_USERNAME));
        assertFalse(isUserVisible("adavies")); // an another user
    }

    @Test
    public void apply_multi_filter() {
        browser.fill(
                new Field(UserList.FILTER_GIVEN_NAME, "Adeline"),
                new Field(UserList.FILTER_FAMILY_NAME, "Davies"));
        browser.click(UserList.FILTER_BUTTON);

        assertFalse(isUserVisible(ADMIN_USERNAME));
        assertTrue(isUserVisible("adavies")); // an another user
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
        assertEquals("20", browser.findSelectedOption(UserList.LIMIT).getText());

        // change limit seticfied
        assertTrue("Precondition is not satisfied! For this testcase the dataset must be contains more than 5 users!",
                getDisplayedUser() > 5);

        browser.fill(new Field(UserList.LIMIT, "5"));
        assertTrue(getDisplayedUser() <= 5);
        assertEquals("5", browser.findSelectedOption(UserList.LIMIT).getText());
    }

    @Test
    public void paging() {
        browser.fill(new Field(UserList.LIMIT, "5")); // set limit to 5
        int userCount = 0;

        while (true) {
            try {
                userCount += getDisplayedUser();
                browser.click(UserList.PAGING_NEXT);
            } catch (NoSuchElementException e) {
                break;
            }

            assertFalse(browser.isErrorPage());
        }

        while (true) {
            try {
                browser.click(UserList.PAGING_PREVIOUS);
            } catch (NoSuchElementException e) {
                break;
            }

            assertFalse(browser.isErrorPage());
        }

        browser.click(UserList.PAGING_LAST);
        assertFalse(browser.isErrorPage());

        browser.click(UserList.PAGING_FIRST);
        assertFalse(browser.isErrorPage());

        clickFirstPagingNumber();
        assertFalse(browser.isErrorPage());

        browser.fill(new Field(UserList.LIMIT, "0")); // set limit to "unlimited"
        assertEquals(getDisplayedUser(), userCount);
    }

    private void clickFirstPagingNumber() {
        String pagingNumberXpath = "//a[contains(@id, 'paging-') " +
                "and not(@id = 'paging-first') " +
                "and not(@id = 'paging-prev') " +
                "and not(@id = 'paging-next') " +
                "and not(@id = 'paging-last') " +
                "and not(@href = '#')]";

        browser.findElements(By.xpath(pagingNumberXpath)).get(0).click();
    }

    private int getDisplayedUser() {
        String userRowsXpath = "//table//tr//button[contains(@id, 'action-button')]";

        return browser.findElements(By.xpath(userRowsXpath)).size();
    }

    private boolean isUserVisible(String username) {
        return browser.isTextPresent(username);
    }

    private boolean isUserAtPosition(String username, Integer position) {
        // the row #2 is the first user-row!
        String rowXpath = "//table//tr[" + (2 - position) + "]//td[contains(., '" + username + "')]";

        return browser.findElements(By.xpath(rowXpath)).size() == 1;
    }

}
