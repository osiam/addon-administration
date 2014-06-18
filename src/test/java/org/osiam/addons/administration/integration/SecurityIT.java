package org.osiam.addons.administration.integration;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osiam.addons.administration.config.Application;
import org.osiam.addons.administration.controller.AdminController;
import org.osiam.addons.administration.controller.LoginController;
import org.osiam.addons.administration.selenium.Browser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
@WebAppConfiguration
@IntegrationTest("server.port:8280")
@DirtiesContext
@DatabaseSetup("/database_seed.xml")
@DatabaseTearDown(value = "/database_tear_down.xml", type = DatabaseOperation.DELETE_ALL)
public class SecurityIT {
    
    @Value("http://localhost:${local.server.port}/")
    private String baseURL;
    
    @Inject
    private Browser browser;
    
    @Before
    public void setup(){
        browser.setBaseURL(baseURL);
        browser.manage().deleteAllCookies();
    }
    
    @Test
    public void no_access_without_valid_access_token(){
        browser.gotoPage(AdminController.CONTROLLER_PATH);
        
        assertTrue(browser.isAccessDenied());
    }
    
    @Test
    public void access_with_valid_access_token(){
        browser
            .gotoPage(LoginController.CONTROLLER_PATH)
            .doOauthLogin("marissa", "koala")
            .gotoPage(AdminController.CONTROLLER_PATH);
        
        assertFalse(browser.isAccessDenied());
    }
}
