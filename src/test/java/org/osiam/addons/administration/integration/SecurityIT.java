package org.osiam.addons.administration.integration;

import static org.junit.Assert.*;

import org.junit.Test;
import org.osiam.addons.administration.controller.AdminController;

public class SecurityIT extends Integrationtest {

    @Test
    public void no_access_without_valid_access_token() {
        browser.gotoPage(AdminController.CONTROLLER_PATH);

        assertTrue(browser.isLoginPage());
    }

    @Test
    public void access_with_valid_access_token() {
        browser
                .doOauthLogin(ADMIN_USERNAME, ADMIN_PASSWORD)
                .gotoPage(AdminController.CONTROLLER_PATH);
        
        assertFalse(browser.isAccessDenied());
    }
}
