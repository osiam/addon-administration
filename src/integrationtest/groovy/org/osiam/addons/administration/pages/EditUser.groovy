package org.osiam.addons.administration.pages

import geb.Page

class EditUser extends Page {

    static url = '/admin/user/edit'

    static at = {
        title == 'Edit user'
    }
}
