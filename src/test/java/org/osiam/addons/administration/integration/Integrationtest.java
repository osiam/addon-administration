package org.osiam.addons.administration.integration;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.osiam.addons.administration.Administration;
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

/**
 * This class is responsible for booting up the integration-tests. This: - starts a tomcat including the
 * addon-administration-servlet. - fill the osiam-database with test data. - after the test clear the osiam-database. -
 * initialize a {@link Browser} instance.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Administration.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
@WebAppConfiguration
@IntegrationTest("server.port:8280")
@DirtiesContext
@DatabaseSetup("/database_seed.xml")
@DatabaseTearDown(value = "/database_tear_down.xml", type = DatabaseOperation.DELETE_ALL)
@Ignore
public class Integrationtest {

    public static final String ADMIN_USERNAME = "marissa";
    public static final String ADMIN_PASSWORD = "koala";

    @Value("http://localhost:${local.server.port}/")
    protected String baseURL;

    @Inject
    protected Browser browser;

    @Before
    public void setup() {
        browser.setBaseURL(baseURL);
        browser.manage().deleteAllCookies();
    }

}
