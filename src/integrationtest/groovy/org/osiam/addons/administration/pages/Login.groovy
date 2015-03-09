package org.osiam.addons.administration.pages

import geb.Page

class Login extends Page {

    static url = 'http://localhost:8380/osiam/login'

    static at = {
        title == 'Please login'
    }

    static content = {
        loginButton { $('button', type: 'submit') }
        username { $('input', name: 'username') }
        password { $('input', name: 'password') }
    }
}
