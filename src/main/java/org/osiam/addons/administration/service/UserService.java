package org.osiam.addons.administration.service;

import javax.inject.Inject;

import org.osiam.addons.administration.model.SessionData;
import org.osiam.client.OsiamConnector;
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
    protected SessionData sessionData;

    @Inject
    protected OsiamConnector connector;

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
     * 
     * @param query Containing the query to execute.
     * @param limit The maximum number of returned resources.
     * @param offset The (1-based) index of the first resource.
     * @param orderBy The attribute to sort the resulting resources by.
     * @param ascending The sort direction of the resulting resources.
     * @return A SCIMSearchResult Containing a list of all found Users.
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
