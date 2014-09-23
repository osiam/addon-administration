package org.osiam.addons.administration.model.session;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * This class contains all userlist specific session data.
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserlistSession {
	private PagingInformation pagingInformation = new PagingInformation();

	public PagingInformation getPagingInformation() {
		return pagingInformation;
	}

	public void setPagingInformation(PagingInformation pagingInformation) {
		this.pagingInformation = pagingInformation;
	}
}
