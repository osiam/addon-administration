package org.osiam.addons.administration.model.session;

import org.osiam.client.oauth.AccessToken;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * This class contains all basic session data.
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GeneralSessionData {

    private AccessToken accesstoken;

    public AccessToken getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(AccessToken at) {
        this.accesstoken = at;
    }
}
