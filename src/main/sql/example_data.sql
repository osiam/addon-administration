--
-- DEPRECATED: replaced by extension.sql and admin_group.sql
--

INSERT INTO osiam_client (internal_id, accesstokenvalidityseconds, client_secret, id,
                          implicit_approval, redirect_uri, refreshtokenvalidityseconds, validityinseconds)
VALUES (20, 28800, 'super-secret', 'addon-administration-client',
        TRUE, 'http://localhost:8080/addon-administration', 86400, 0);

INSERT INTO osiam_client_scopes (id, scope) VALUES (20, 'GET');
INSERT INTO osiam_client_scopes (id, scope) VALUES (20, 'POST');
INSERT INTO osiam_client_scopes (id, scope) VALUES (20, 'PUT');
INSERT INTO osiam_client_scopes (id, scope) VALUES (20, 'PATCH');
INSERT INTO osiam_client_scopes (id, scope) VALUES (20, 'DELETE');
INSERT INTO osiam_client_grants (id, grants) VALUES (20, 'authorization_code');

INSERT INTO scim_meta (id, created, lastmodified, location, resourcetype, version)
VALUES (2, LOCALTIMESTAMP, LOCALTIMESTAMP, NULL, 'Group', NULL);

INSERT INTO scim_id (internal_id, external_id, id, meta_id)
VALUES (2, NULL, 'cef9452e-00a9-4cec-a086-d171374aabef', 2);

INSERT INTO scim_group (displayname, internal_id)
VALUES ('admin', 2);

INSERT INTO scim_group_scim_id (groups_internal_id, members_internal_id)
VALUES (2, 1);
