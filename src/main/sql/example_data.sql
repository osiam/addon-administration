--
-- Example Client, please sync to the attribute values in the addon-administration.properties.
-- Please update all queries in this file to your needs!
-- Have to be imported in the database of the auth-server, before you deploy the addon-administration!
--
INSERT INTO osiam_client (internal_id, accesstokenvalidityseconds, client_secret, expiry,
id, implicit_approval, redirect_uri, refreshtokenvalidityseconds,
validityinseconds)
VALUES (20, 2342, 'super-secret', null, 'addon-administration-client', FALSE, 'http://localhost:8080/addon-administration', 4684, 1337);

INSERT INTO osiam_client_scopes (id, scope) VALUES (20, 'GET');
INSERT INTO osiam_client_scopes (id, scope) VALUES (20, 'POST');
INSERT INTO osiam_client_scopes (id, scope) VALUES (20, 'PUT');
INSERT INTO osiam_client_scopes (id, scope) VALUES (20, 'PATCH');
INSERT INTO osiam_client_scopes (id, scope) VALUES (20, 'DELETE');
INSERT INTO osiam_client_grants (id, grants) VALUES (20, 'authorization_code');
INSERT INTO osiam_client_grants (id, grants) VALUES (20, 'refresh_token');
INSERT INTO osiam_client_grants (id, grants) VALUES (20, 'password');
INSERT INTO osiam_client_grants (id, grants) VALUES (20, 'client_credentials');
