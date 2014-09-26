package org.osiam.addons.administration.model.session;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserMembershipSession {
	private PagingInformation addPanelPagingInformation = new PagingInformation();
	private PagingInformation removePanelPagingInformation = new PagingInformation();

	public PagingInformation getAddPanelPagingInformation() {
		return addPanelPagingInformation;
	}
	public void setAddPanelPagingInformation(PagingInformation addPanelPagingInformation) {
		this.addPanelPagingInformation = addPanelPagingInformation;
	}
	public PagingInformation getRemovePanelPagingInformation() {
		return removePanelPagingInformation;
	}
	public void setRemovePanelPagingInformation(PagingInformation removePanelPagingInformation) {
		this.removePanelPagingInformation = removePanelPagingInformation;
	}


}
