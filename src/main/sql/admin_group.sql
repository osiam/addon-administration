--
-- Group 'admin' for the addon-administration. Has to be imported in the
-- database of OSIAM.
--

INSERT INTO scim_meta VALUES (
  2, -- id
  LOCALTIMESTAMP, -- created
  LOCALTIMESTAMP, -- last_modified
  NULL, -- location
  'Group', -- resource_type
  NULL -- version
);

INSERT INTO scim_id VALUES (
  2, -- internal_id
  NULL, -- external_id
  'cef9452e-00a9-4cec-a086-d171374aabef', -- id
  2 -- meta
);

INSERT INTO scim_group VALUES (
  'admin', -- display_name
  2 -- internal_id
);

-- add default admin user to group

INSERT INTO scim_group_members VALUES (
  2, -- groups
  1 -- members
);
