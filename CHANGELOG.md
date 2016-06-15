# OSIAM addon-administration

## Unreleased

**NOTICE:** This version should be compatible with OSIAM 3.x and OSIAM 2.x.

### Features

- The Administration now uses a dedicated landing page, so users that are not logged in, will not be
  immediately redirected to the login page.

### Changes

- Use replace API instead of update API, because OSIAM 3.0 removes support for updates.
- Snapshot builds can now be downloaded from [Bintray]
  (https://dl.bintray.com/osiam/downloads/addon-administration/latest/addon-administration-latest.war)
  ([GPG Signature](https://dl.bintray.com/osiam/downloads/addon-administration/latest/addon-administration-latest.war.asc)).

### Fixes

- Changing the displayName removed all members from a group (#119).
- Umlauts in email were not correctly displayed (#82).
- Access tokens were revoked after editing a user, which rendered the Administration unusable, if
  one edited themselves.

### Updates

- OSIAM connector4java 1.9
- Spring Boot 1.3.5
- Jersey 2.22.2

## 1.8 - 2015-12-27

**NOTICE:** This version should be compatible with all versions of OSIAM >= 2.2.

### Features

- Support legacy SCIM schemas for connecting to OSIAM <= 2.3

    See [OSIAM Endpoints (OSIAM 2.x)](README.md#osiam-endpoints-osiam-2x), if you use OSIAM <= 2.3.

- Support OSIAM 3.x

    See [OSIAM Endpoints (OSIAM 3.x)](README.md#osiam-endpoints-osiam-3x), if you use OSIAM 3.x.

### Changes

- Make SQL scripts more independent of database schema

    Use `INSERT`s without field names.

- Change table name in SQL script `admin_group.sql`

    Due to the database schema changes in the resource-server, `scim_group_scim_id` becomes `scim_group_members`.
    A statement with the old table name is still contained as a comment for your convenience.

- Use the new scope `ADMIN` for connections to OSIAM

    Abandon the usage of the deprecated method-based scopes.
    SQL files have been changed to install the necessary client with scope `ADMIN`.
    Add the scope `ADMIN` to the `addon-administration-client`:

        INSERT INTO osiam_client_scopes (id, scope) VALUES (<id of addon-administration-client>, 'ADMIN');

    By default `<id of addon-administration-client>` is set to `20`.

- Remove deprecated SQL file `example_data.sql`

### Fixes

- Fix handling of boolean extension fields

    Fixes #83

- Create REST client only once to improve performance

### Updates

- OSIAM connector4java 1.8
- Spring Boot 1.2.7

## 1.7 - 2015-10-09

**NOTICE:** This version is only compatible with OSIAM 2.4 and later.

### Updates

- OSIAM connector4java 1.7
- Spring Boot 1.2.6

## 1.6 - 2015-06-18

### Features

- Allow setting of `external_id` for users
- Add button to copy user id into external id field

### Changes

- Bump Jackson version
- Bump connector to make use of more concurrent HTTP connections
- UI: Apply Bootstrap defaults

### Fixes

- Rename Style.css to currently used name in template
- Add templates for (de-)activation of users
- `sw =` is not a filter operator
- UI: Make dropdowns work again in IE8+
- UI: Make back/cancel buttons work again in IE8+
- UI: Fix visual glitches icons
- UI: Align checkboxes on group membership view
- UI: Fix ambiguous form submission in IE8 in group membership

## 1.5 - 2015-06-02

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
