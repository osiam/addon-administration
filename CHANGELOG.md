# OSIAM addon-administration

## 1.5 - Unreleased

### Changes

- Remove field `expiry` from OAuth client in SQL scripts

    The field `expiry` has been removed from the auth-server (osiam/auth-server#9)
    and must be removed from the SQL scripts too.

- Adjust some attributes of the 'admin' group

    - Set `external_id` to NULL
    - Set `created` and `lastmodified` timestamps to `LOCALTIMESTAMP`

- Adjust some attributes of the OAuth client

    - Enable implicit approval
    - Remove unnecessary grants: resource owner credentials grant, client
      credentials grant, refresh token grant
    - Increase access token validity to 8 hours
    - Decrease refresh token validity to 0 seconds
    - Decrease approval validity to 0 seconds

- Rename SQL scripts for installing client and group

    Use `client.sql` to create the OAuth client in the `auth-server`'s database
    and `admin_group.sql` to create the SCIM group `admin` in th
    `resource-server`'s database and also associate it with the user with id 1.
    The old file is still in place for compatibility reasons, but will receive
    no further updates and be eventually removed in a future version. All users
    are encouraged to update to the new files.

### Fixes

- Client database id may lead to problems with other clients

    Changed from `1` to `10`

## 1.4 - 2015-04-29
- [feature] Bulk operations for user: activate all / deactivate all / delete all
- [fix] the email type is not required
- [change] bump dependency versions
- [docs] move documentation from Wiki to repo
- [docs] rename file RELEASE.NOTES to CHANGELOG.md

## 1.3.2 - 2014-11-24
- [enhancement] Compatibility for Internet Explorer 8
- [fix] Not Bug (https://github.com/osiam/server/issues/247)

## 1.3.1 - 2014-10-27
- release because of fixes in addon-self-administration

## 1.3 - 2014-10-17
- [fix] Styling issues
- [enhancement] Groupname is shown in Edit-User-Membership view
- [enhancement] Username is shown in Edit-Group-Membership view
- [fix] Display of paging meta information
- [enhancement] Check the access token validity foreach request
- [fix] Saving primary flag for multi-value-attributes
  Before: Primary flag will not be saved, if you enter a display value or a new attribute.
  After: Now the primary flag will be saved correctly.
- [refactor] Sorting arrows
  One arrow with a link for ascending or descending depends on the current sort direction.
- [refactor] Unify all success and error messages for all pages

## 1.2 - 2014-09-30
- new page for add group to users
- new page for add user to groups
- revive the logout button

## 1.1 - 2014-09-22
- first release of the addon-administration!
