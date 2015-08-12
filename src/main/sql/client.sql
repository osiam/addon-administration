--
-- OAuth Client: please sync to the attribute values in the
-- addon-administration.properties. Has to be imported in the database of the
-- auth-server, before you deploy the addon-administration!
--

INSERT INTO osiam_client (internal_id, access_token_validity_seconds, client_secret, id,
                          implicit_approval, redirect_uri, refresh_token_validity_seconds, validity_in_seconds)
VALUES (20, 28800, 'super-secret', 'addon-administration-client',
        TRUE, 'http://localhost:8080/addon-administration', 0, 0);

INSERT INTO osiam_client_scopes (id, scope) VALUES (20, 'ADMIN');
INSERT INTO osiam_client_grants (id, grants) VALUES (20, 'authorization_code');
