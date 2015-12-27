--
-- OAuth Client: please sync to the attribute values in the
-- addon-administration.properties. Has to be imported in the database of the
-- auth-server, before you deploy the addon-administration!
--

INSERT INTO osiam_client VALUES (
  20, -- internal_id
  28800, -- access_token_validity_seconds
  'super-secret', -- client_secret
  'addon-administration-client', -- id
  TRUE, -- implicit_approval
  'http://localhost:8080/addon-administration', -- redirect_uri
  0, -- refresh_token_validity_seconds
  0 -- validity_in_seconds
);

INSERT INTO osiam_client_scopes VALUES (20, 'ADMIN');
INSERT INTO osiam_client_grants VALUES (20, 'authorization_code');
