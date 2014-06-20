package org.osiam.addons.administration.integration;

import static org.junit.Assert.*;

import org.junit.Test;

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

    private boolean isUserVisible(String username) {
        return browser.isTextPresent(username);
    }
}
