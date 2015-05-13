package org.osiam.addons.administration.model.session;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserMembershipSession {
    private PagingInformation unassignedUsersPagingInformation = new PagingInformation();
    private PagingInformation assignedUsersPagingInformation = new PagingInformation();

    public PagingInformation getUnassignedUsersPagingInformation() {
        return unassignedUsersPagingInformation;
    }

    public void setUnassignedUsersPagingInformation(PagingInformation unassignedUsersPagingInformation) {
        this.unassignedUsersPagingInformation = unassignedUsersPagingInformation;
    }

    public PagingInformation getAssignedUsersPagingInformation() {
        return assignedUsersPagingInformation;
    }

    public void setAssignedUsersPagingInformation(PagingInformation assignedUsersPagingInformation) {
        this.assignedUsersPagingInformation = assignedUsersPagingInformation;
    }

}
