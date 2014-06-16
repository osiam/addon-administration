package org.osiam.addons.administration.service;

import javax.inject.Inject;

import org.osiam.addons.administration.model.SessionData;
import org.osiam.client.OsiamConnector;
import org.osiam.client.oauth.AccessToken;
import org.osiam.client.query.Query;
import org.osiam.client.query.QueryBuilder;
import org.osiam.resources.scim.SCIMSearchResult;
import org.osiam.resources.scim.User;
import org.springframework.stereotype.Component;

/**
 * This class contains all logic about handling users.
 */
@Component
public class UserService {

    @Inject
    SessionData sessionData;

    @Inject
    OsiamConnector connector;

    /**
     * See {@link UserService#searchUser(String, Integer, Long, String, Boolean)}.
     */
    public SCIMSearchResult<User> searchUser(String query) {
        return searchUser(query, null, null, null, null);
    }

    /**
     * See {@link UserService#searchUser(String, Integer, Long, String, Boolean)}.
     */
    public SCIMSearchResult<User> searchUser(String query, Integer limit, Long offset) {
        return searchUser(query, limit, offset, null, null);
    }

    /**
     * Search for existing Users.
     * 
     * @see OsiamConnector#searchUsers(Query, AccessToken)
     * @param query
     * @param limit
     * @param offset
     * @param orderBy
     * @param ascending
     * @return
     */
    public SCIMSearchResult<User> searchUser(String query, Integer limit, Long offset, String orderBy, Boolean ascending) {
        QueryBuilder qb = new QueryBuilder();
        qb.filter(query);

        if (limit != null) {
            qb.count(limit);
        }

        if (offset != null) {
            qb.startIndex(offset);
        }

        if (orderBy != null && ascending != null) {
            if (ascending) {
                qb.ascending(orderBy);
            } else {
                qb.descending(orderBy);
            }
        }

        return connector.searchUsers(qb.build(), sessionData.getAccesstoken());
    }
}
