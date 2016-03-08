package org.osiam.addons.administration

import org.osiam.addons.administration.pages.EditUser
import org.osiam.addons.administration.pages.Login
import org.osiam.addons.administration.pages.Start
import org.osiam.addons.administration.pages.UserList

class CreateUserIT extends IntegrationTest {

    def 'create a user'() {
        when:
        to Start

        $('#login-button').click()

        at Login

        username.value('marissa')
        password.value('koala')
        loginButton.click()

        at Start

        $('#userListLink').click()

        at UserList

        $('a', href: 'create').click()

        $('#username').value('testuser')
        $('#password').value('testuser')
        $('#btnCreate').click()

        then:
        at EditUser
        assert $('#userName').value() == 'testuser'
    }
}
