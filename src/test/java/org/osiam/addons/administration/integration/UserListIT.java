package org.osiam.addons.administration.integration;

import static org.junit.Assert.*;

import org.junit.Test;
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
    public void apply_empty_filter(){
        browser.click(UserList.FilterButton);

        assertTrue(isUserVisible(ADMIN_USERNAME));
    }
    
    @Test
    public void apply_single_filter(){
        browser.fill(new Field(UserList.FilterLogin, ADMIN_USERNAME));
        browser.click(UserList.FilterButton);
        
        assertTrue(isUserVisible(ADMIN_USERNAME));
        assertFalse(isUserVisible("adavies"));  //an another user
    }

    @Test
    public void apply_multi_filter(){
        browser.fill(
                new Field(UserList.FilterGivenName, "Adeline"),
                new Field(UserList.FilterFamilyName, "Davies"));
        browser.click(UserList.FilterButton);
        
        assertFalse(isUserVisible(ADMIN_USERNAME));
        assertTrue(isUserVisible("adavies"));  //an another user
    }
    
    private boolean isUserVisible(String username) {
        return browser.isTextPresent(username);
    }
}
