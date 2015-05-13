package org.osiam.addons.administration.model.session;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GroupMembershipSession {
    private PagingInformation assignedGroupsPagingInformation = new PagingInformation();
    private PagingInformation unassignedGroupsPagingInformation = new PagingInformation();

    public PagingInformation getAssignedGroupsPagingInformation() {
        return assignedGroupsPagingInformation;
    }

    public void setAssignedGroupsPagingInformation(
            PagingInformation assignedGroupsPagingInformation) {
        this.assignedGroupsPagingInformation = assignedGroupsPagingInformation;
    }

    public PagingInformation getUnassignedGroupsPagingInformation() {
        return unassignedGroupsPagingInformation;
    }

    public void setUnassignedGroupsPagingInformation(
            PagingInformation unassignedGroupsPagingInformation) {
        this.unassignedGroupsPagingInformation = unassignedGroupsPagingInformation;
    }
}
