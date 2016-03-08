package org.osiam.addons.administration.pages

import geb.Page

class UserList extends Page {

    static url = '/admin/user/list'

    static at = {
        title == 'User-List'
    }
}
