package org.osiam.addons.administration.model.session;

import java.io.Serializable;

import org.osiam.client.oauth.AccessToken;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * This class contains all basic session data.
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GeneralSessionData implements Serializable {

    private static final long serialVersionUID = 3937661507313495926L;

    private AccessToken accesstoken;

    public AccessToken getAccessToken() {
        return accesstoken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accesstoken = accessToken;
    }
}
