--
-- Example Client, please sync to the attribute values in the
-- addon-administration.properties. Have to be imported in the database of the
-- auth-server, before you deploy the addon-administration!
--

INSERT INTO osiam_client (internal_id, accesstokenvalidityseconds, client_secret, expiry,
                          id, implicit_approval, redirect_uri, refreshtokenvalidityseconds,
                          validityinseconds)
VALUES (20, 28800, 'super-secret', NULL,
        'addon-administration-client', TRUE, 'http://localhost:8080/addon-administration', 86400,
        0);

INSERT INTO osiam_client_scopes (id, scope) VALUES (20, 'GET');
INSERT INTO osiam_client_scopes (id, scope) VALUES (20, 'POST');
INSERT INTO osiam_client_scopes (id, scope) VALUES (20, 'PUT');
INSERT INTO osiam_client_scopes (id, scope) VALUES (20, 'PATCH');
INSERT INTO osiam_client_scopes (id, scope) VALUES (20, 'DELETE');
INSERT INTO osiam_client_grants (id, grants) VALUES (20, 'authorization_code');

-- resource server: example group 'admin' for the addon-administration

INSERT INTO scim_meta (id, created, lastmodified, location, resourcetype, version)
VALUES (2, '2011-10-10', '2011-10-10', NULL, 'Group', NULL);

INSERT INTO scim_id (internal_id, external_id, id, meta_id)
VALUES (2, 'group_external_id', 'cef9452e-00a9-4cec-a086-d171374aabef', 2);

INSERT INTO scim_group (displayname, internal_id)
VALUES ('admin', 2);

-- resource server: add user to group

INSERT INTO scim_group_scim_id (groups_internal_id, members_internal_id)
VALUES (2, 1);
