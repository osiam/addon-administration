package org.osiam.addons.administration.service;

import javax.inject.Inject;

import org.osiam.addons.administration.model.session.GeneralSessionData;
import org.osiam.client.OsiamConnector;
import org.osiam.client.query.QueryBuilder;
import org.osiam.resources.scim.Group;
import org.osiam.resources.scim.SCIMSearchResult;
import org.springframework.stereotype.Component;

@Component
public class GroupService {

    @Inject
    private GeneralSessionData sessionData;

    @Inject
    private OsiamConnector connector;

    /**
     * Search for existing groups by a given Query.
     *
     * @see OsiamConnector#searchGroups(Query, AccessToken)
     *
     * @param query Containing the query to execute.
     * @param limit The maximum number of returned resources.
     * @param offset The (1-based) index of the first resource.
     * @param orderBy The attribute to sort the resulting resources by.
     * @param ascending The sort direction of the resulting resources.
     * @return A SCIMSearchResult Containing a list of all found groups.
     */
    public SCIMSearchResult<Group> searchGroup(String query, Integer limit, Long offset, String orderBy,
            Boolean ascending) {

        QueryBuilder qb = new QueryBuilder();
        qb.filter(query);
        qb.count(limit == null ? 0 : limit);
        qb.startIndex(offset == null ? 0 : offset);

        if (ascending) {
            qb.ascending(orderBy);
        } else {
            qb.descending(orderBy);
        }

        return connector.searchGroups(qb.build(), sessionData.getAccesstoken());
    }

    /**
     * Delete the given group.
     *
     * @param id The id of the {@link Group}.
     */
    public void deleteGroup(String id) {
        connector.deleteGroup(id, sessionData.getAccesstoken());
    }

}
