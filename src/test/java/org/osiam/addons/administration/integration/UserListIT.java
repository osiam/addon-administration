package org.osiam.addons.administration.integration;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.By;
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
        browser.click(UserList.FilterButton);

        assertTrue(isUserVisible(ADMIN_USERNAME));
    }

    @Test
    public void apply_single_filter() {
        browser.fill(new Field(UserList.FilterLogin, ADMIN_USERNAME));
        browser.click(UserList.FilterButton);

        assertTrue(isUserVisible(ADMIN_USERNAME));
        assertFalse(isUserVisible("adavies")); // an another user
    }

    @Test
    public void apply_multi_filter() {
        browser.fill(
                new Field(UserList.FilterGivenName, "Adeline"),
                new Field(UserList.FilterFamilyName, "Davies"));
        browser.click(UserList.FilterButton);

        assertFalse(isUserVisible(ADMIN_USERNAME));
        assertTrue(isUserVisible("adavies")); // an another user
    }

    @Test
    public void order() {
        browser.click(UserList.SortLoginAsc);
        assertTrue(isUserAtPosition("adavies", 0));

        browser.click(UserList.SortGivenNameAsc);
        assertTrue(isUserAtPosition("adavies", 0));     //  Adeline

        browser.click(UserList.SortFamilyNameAsc);
        assertTrue(isUserAtPosition("gparker", 0));     //  Barker

        browser.click(UserList.SortLoginDesc);
        assertTrue(isUserAtPosition("marissa", 0));

        browser.click(UserList.SortGivenNameDesc);
        assertTrue(isUserAtPosition("ewilley", 0));     //  Willey

        browser.click(UserList.SortFamilyNameDesc);
        assertTrue(isUserAtPosition("jcambell", 0));    //  Thompson
    }

    private boolean isUserVisible(String username) {
        return browser.isTextPresent(username);
    }

    private boolean isUserAtPosition(String username, Integer position) {
        //the row #2 is the first user-row!
        String rowXpath = "//table//tr[" + (2 - position) + "]//td[contains(., '" + username + "')]";
        
        return browser.findElements(By.xpath(rowXpath)).size() == 1;
    }
}
