package org.osiam.addons.administration

import com.github.springtestdbunit.DbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseOperation
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DatabaseTearDown
import geb.driver.CachingDriverFactory
import geb.spock.GebSpec
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener

@ContextConfiguration('classpath:/test-context.xml')
@TestExecutionListeners([DependencyInjectionTestExecutionListener, DbUnitTestExecutionListener])
@DatabaseSetup('/database_seed.xml')
@DatabaseTearDown(value = '/database_tear_down.xml', type = DatabaseOperation.DELETE_ALL)
class IntegrationTest extends GebSpec {

    public static def ADMINISTRATION_HOST = "http://localhost:8380/addon-administration"

    def setup() {
        baseUrl = ADMINISTRATION_HOST
    }

    def cleanup() {
        CachingDriverFactory.clearCache()
    }
}
