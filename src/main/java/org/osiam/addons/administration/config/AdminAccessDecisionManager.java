package org.osiam.addons.administration.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.inject.Inject;

import org.osiam.addons.administration.model.session.GeneralSessionData;
import org.osiam.client.OsiamConnector;
import org.osiam.client.exception.UnauthorizedException;
import org.osiam.client.query.Query;
import org.osiam.client.query.QueryBuilder;
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
        if (session.getAccessToken() == null) {
            throw new AccessDeniedException("There is no access token in session!");
        }

        try {
            connector.validateAccessToken(session.getAccessToken());
        } catch (UnauthorizedException e) {
            throw new AccessDeniedException("The current access token is not valid!", e);
        }

        checkForAdminGroup();
    }

    private void checkForAdminGroup() {
        if (adminGroups == null || adminGroups.length == 0) {
            return;
        }

        String queryFilter = buildQueryFilter();
        Query query = new QueryBuilder()
                .count(1)
                .filter(queryFilter)
                .build();

        SCIMSearchResult<User> result = connector.searchUsers(query, session.getAccessToken());

        if (result.getTotalResults() != 1) {
            throw new AccessDeniedException("The user not a member of an admin group.");
        }
    }

    private String buildQueryFilter() {
        StringBuilder queryString = new StringBuilder();
        queryString.append("userName eq ");
        queryString.append("\"");
        queryString.append(session.getAccessToken().getUserName());
        queryString.append("\" AND (");

        Iterator<String> groupIterator = Arrays.asList(adminGroups).iterator();
        while (groupIterator.hasNext()) {
            queryString.append("groups.display eq \"");
            queryString.append(groupIterator.next());
            queryString.append("\"");

            if (groupIterator.hasNext()) {
                queryString.append(" OR ");
            }
        }
        queryString.append(")");

        return queryString.toString();
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
