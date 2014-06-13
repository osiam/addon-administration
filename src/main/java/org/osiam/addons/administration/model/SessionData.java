package org.osiam.addons.administration.model;

import org.osiam.client.oauth.AccessToken;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * This class contains all basic session data.
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionData {

	private AccessToken accesstoken;

	public AccessToken getAccesstoken() {
		return accesstoken;
	}

	public void setAccesstoken(AccessToken at) {
		this.accesstoken = at;
	}
}
