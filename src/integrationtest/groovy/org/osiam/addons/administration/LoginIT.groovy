package org.osiam.addons.administration

import org.osiam.addons.administration.pages.Login
import org.osiam.addons.administration.pages.Start

class LoginIT extends IntegrationTest {

    def 'when not logged in redirect to osiam login'() {
        when:
        to Start

        $('#login-button').click()

        then:
        at Login
    }

    def 'after successful osiam login redirect to start'() {
        when:
        to Start

        $('#login-button').click()

        at Login

        username.value('marissa')
        password.value('koala')
        loginButton.click()

        then:
        at Start
    }

    def 'start page with wrong auth code shows error'() {
        when:
        go ADMINISTRATION_HOST + '?code=123456'

        then:
        $('p', 'class': 'text-danger').displayed
    }
}
