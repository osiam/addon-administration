--
-- OAuth Client: please sync to the attribute values in the
-- addon-administration.properties. Has to be imported in the database of the
-- auth-server, before you deploy the addon-administration!
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
