--
-- Group 'admin' for the addon-administration. Has to be imported in the
-- database of the resource-server.
--

INSERT INTO scim_meta (id, created, last_modified, location, resource_type, version)
VALUES (2, LOCALTIMESTAMP, LOCALTIMESTAMP, NULL, 'Group', NULL);

INSERT INTO scim_id (internal_id, external_id, id, meta_id)
VALUES (2, NULL, 'cef9452e-00a9-4cec-a086-d171374aabef', 2);

INSERT INTO scim_group (displayname, internal_id)
VALUES ('admin', 2);

-- add default admin user to group

INSERT INTO scim_group_members (groups, members)
VALUES (2, 1);
