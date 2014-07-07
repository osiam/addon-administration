package org.osiam.addons.administration.service;

import javax.inject.Inject;

import org.osiam.addons.administration.model.session.GeneralSessionData;
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
    private GeneralSessionData sessionData;

    @Inject
    private OsiamConnector connector;

    /**
     * Search for existing Users.
     * 
     * @see OsiamConnector#searchUsers(Query, AccessToken)
     * 
     * @param query
     *        Containing the query to execute.
     * @param limit
     *        The maximum number of returned resources.
     * @param offset
     *        The (1-based) index of the first resource.
     * @param orderBy
     *        The attribute to sort the resulting resources by.
     * @param ascending
     *        The sort direction of the resulting resources.
     * @param attributes
     *        List of attributes to return.
     * @return A SCIMSearchResult Containing a list of all found Users.
     */
    public SCIMSearchResult<User> searchUser(String query, Integer limit, Long offset, String orderBy,
            Boolean ascending, String attributes) {
        QueryBuilder qb = new QueryBuilder();
        qb.filter(query);
        qb.attributes(attributes);
        qb.count(limit == null ? 0 : limit);
        qb.startIndex(offset == null ? 0 : offset);

        if (ascending) {
            qb.ascending(orderBy);
        } else {
            qb.descending(orderBy);
        }

        return connector.searchUsers(qb.build(), sessionData.getAccesstoken());
    }
}
