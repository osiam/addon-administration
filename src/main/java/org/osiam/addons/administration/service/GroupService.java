package org.osiam.addons.administration.service;

import javax.inject.Inject;

import org.osiam.addons.administration.model.session.GeneralSessionData;
import org.osiam.client.OsiamConnector;
import org.osiam.client.oauth.AccessToken;
import org.osiam.client.query.Query;
import org.osiam.client.query.QueryBuilder;
import org.osiam.resources.scim.Group;
import org.osiam.resources.scim.SCIMSearchResult;
import org.osiam.resources.scim.UpdateGroup;
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

        return connector.searchGroups(qb.build(), getAccesstoken());
    }

    /**
     * Delete the given group.
     *
     * @param id The id of the {@link Group}.
     */
    public void deleteGroup(String id) {
        connector.deleteGroup(id, getAccesstoken());
    }

    /**
     * Returns the groupt with the given ID.
     *
     * @param id the group Id
     * @return the requested group
     * @throws NoSuchGroupException if no group was found for id.
     */
    public Group getGroup(String id) {
        return connector.getGroup(id, getAccesstoken());
    }

    /**
     * Updates a group based on the given {@link UpdateGroup}.
     *
     * @param id
     *        the group ID
     * @param updateGroup
     *        the {@link UpdateGroup}
     */
    public Group updateGroup(String id, UpdateGroup updateGroup) {
        return connector.updateGroup(id, updateGroup, getAccesstoken());
    }

    public Group createGroup(Group group) {
        return connector.createGroup(group, getAccesstoken());
    }

    private AccessToken getAccesstoken() {
        return sessionData.getAccesstoken();
    }

}
