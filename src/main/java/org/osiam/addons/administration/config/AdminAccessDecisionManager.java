package org.osiam.addons.administration.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.osiam.addons.administration.model.session.GeneralSessionData;
import org.osiam.client.OsiamConnector;
import org.osiam.client.query.QueryBuilder;
import org.osiam.resources.scim.GroupRef;
import org.osiam.resources.scim.SCIMSearchResult;
import org.osiam.resources.scim.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for decide admin-access.
 */
@Component
public class AdminAccessDecisionManager implements AccessDecisionManager {

    @Inject
    private GeneralSessionData session;

    @Inject
    private OsiamConnector connector;

    @Value("${org.osiam.administration.adminGroups}")
    private String[] adminGroups;

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) {
        // if there are no access-token in session
        if (session.getAccesstoken() == null) {
            throw new AccessDeniedException("There is no accesstoken in session!");
        }

        //groups.display eq "vr_bewerber" OR groups.display eq "vr_anwaerterStipendiat" OR groups.display eq "vr_stipendiat" OR groups.display eq "vr_anwaerterAltstipendiat"
//        List<GroupRef> userGroups = connector.getCurrentUser(session.getAccesstoken()).getGroups();

        StringBuilder queryString = new StringBuilder();
        queryString.append("userName eq ");
        queryString.append("\"");
        queryString.append(session.getAccesstoken().getUserName());
        queryString.append("\"");
        if (adminGroups.length > 0) {
            queryString.append(" AND ");
        }

        Iterator<String> groupIterator = Arrays.asList(adminGroups).iterator();
        while (groupIterator.hasNext()) {
            queryString.append("groups.display eq \"");
            queryString.append(groupIterator.next());
            queryString.append("\"");

            if(groupIterator.hasNext()) {
                queryString.append(" OR ");
            }
        }

        SCIMSearchResult<User> a = connector.searchUsers(new QueryBuilder().filter(queryString.toString()).build(), session.getAccesstoken());

        if(a.getTotalResults() != 1) {
            throw new AccessDeniedException("No permissions to user admin gui");
        }
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
