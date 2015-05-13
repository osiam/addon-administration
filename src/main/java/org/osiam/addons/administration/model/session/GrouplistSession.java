package org.osiam.addons.administration.model.session;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GrouplistSession {
    private PagingInformation pagingInformation = new PagingInformation();

    public PagingInformation getPagingInformation() {
        return pagingInformation;
    }

    public void setPagingInformation(PagingInformation pagingInformation) {
        this.pagingInformation = pagingInformation;
    }
}
