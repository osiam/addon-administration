package org.osiam.addons.administration.service;

import javax.inject.Inject;

import org.osiam.addons.administration.model.session.GeneralSessionData;
import org.osiam.addons.administration.model.session.PagingInformation;
import org.osiam.client.OsiamConnector;
import org.osiam.client.oauth.AccessToken;
import org.osiam.client.query.Query;
import org.osiam.client.query.QueryBuilder;
import org.osiam.resources.scim.Group;
import org.osiam.resources.scim.MemberRef;
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
     * @param query
     *            Containing the query to execute.
     * @param limit
     *            The maximum number of returned resources.
     * @param offset
     *            The (1-based) index of the first resource.
     * @param orderBy
     *            The attribute to sort the resulting resources by.
     * @param ascending
     *            The sort direction of the resulting resources.
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
     * @param id
     *            The id of the {@link Group}.
     */
    public void deleteGroup(String id) {
        connector.deleteGroup(id, getAccesstoken());
    }

    /**
     * Returns the group with the given ID.
     *
     * @param id
     *            the group Id
     * @return the requested group
     */
    public Group getGroup(String id) {
        return connector.getGroup(id, getAccesstoken());
    }

    public void replaceGroup(String groupId, Group group) {
        connector.replaceGroup(groupId, group, getAccesstoken());
    }

    /**
     * Add the given users to the given groups.
     *
     * @param groupId
     *            The user id.
     * @param userIds
     *            The group id(s)
     */
    public void addUsersToGroup(String groupId, String... userIds) {
        if (userIds == null || userIds.length == 0) {
            return;
        }
        Group group = connector.getGroup(groupId, getAccesstoken());
        Group.Builder updatedGroup = new Group.Builder(group);

        for (String userId : userIds) {
            updatedGroup.addMember(new MemberRef.Builder()
                    .setValue(userId)
                    .build());
        }

        connector.replaceGroup(groupId, updatedGroup.build(), getAccesstoken());
    }

    /**
     * Remove the given users from the given groups.
     *
     * @param groupId
     *            The user id.
     * @param userIds
     *            The group id(s)
     */
    public void removeUsersFromGroup(String groupId, String... userIds) {
        if (userIds == null || userIds.length == 0) {
            return;
        }
        Group group = connector.getGroup(groupId, getAccesstoken());
        Group.Builder updatedGroup = new Group.Builder(group);

        for (String userId : userIds) {
            updatedGroup.removeMember(new MemberRef.Builder()
                    .setValue(userId)
                    .build());
        }

        connector.replaceGroup(groupId, updatedGroup.build(), getAccesstoken());
    }

    /**
     * Create a new group.
     */
    public Group createGroup(Group group) {
        return connector.createGroup(group, getAccesstoken());
    }

    /**
     * Return all the other groups where the user is not assigned to.
     *
     * @param userId
     *            The user id.
     * @param pagingInformation
     *            Contains all informations about the paging.
     * @return A SCIMSearchResult containing a list of all found groups.
     */
    public SCIMSearchResult<Group> getUnassignedGroups(String userId,
            PagingInformation pagingInformation) {

        String query = "not(members eq \"" + userId + "\") or not (members pr)";

        if (pagingInformation.getQuery() != null && !pagingInformation.getQuery().trim().isEmpty()) {
            query += " and " + pagingInformation.getQuery();
        }

        return searchGroup(query,
                pagingInformation.getLimit(),
                pagingInformation.getOffset(),
                pagingInformation.getOrderBy(),
                pagingInformation.getAscending());
    }

    /**
     * Return all the groups where the user is assigned to.
     *
     * @param userId
     *            The user id.
     * @param pagingInformation
     *            Contains all informations about the paging.
     * @return A SCIMSearchResult containing a list of all found groups.
     */
    public SCIMSearchResult<Group> getAssignedGroups(String userId,
            PagingInformation pagingInformation) {

        String query = "members eq \"" + userId + "\"";

        if (pagingInformation.getQuery() != null && !pagingInformation.getQuery().trim().isEmpty()) {
            query += " and " + pagingInformation.getQuery();
        }

        return searchGroup(query,
                pagingInformation.getLimit(),
                pagingInformation.getOffset(),
                pagingInformation.getOrderBy(),
                pagingInformation.getAscending());
    }

    /**
     * Add the given user to the given groups.
     *
     * @param userId
     *            The user id.
     * @param groupIds
     *            The group id(s)
     */
    public void addUserToGroups(String userId, String... groupIds) {
        if (groupIds == null || groupIds.length == 0) {
            return;
        }

        for (String groupId : groupIds) {
            Group group = connector.getGroup(groupId, getAccesstoken());
            Group.Builder updatedGroup = new Group.Builder(group);

            updatedGroup.addMember(new MemberRef.Builder()
                    .setValue(userId)
                    .build());

            connector.replaceGroup(groupId, updatedGroup.build(), getAccesstoken());
        }
    }

    /**
     * Remove the given user from the given groups.
     *
     * @param userId
     *            The user id.
     * @param groupIds
     *            The group id(s)
     */
    public void removeUserFromGroups(String userId, String... groupIds) {
        if (groupIds == null || groupIds.length == 0) {
            return;
        }

        for (String groupId : groupIds) {
            Group group = connector.getGroup(groupId, getAccesstoken());
            Group.Builder updatedGroup = new Group.Builder(group);

            updatedGroup.removeMember(new MemberRef.Builder()
                    .setValue(userId)
                    .build());

            connector.replaceGroup(groupId, updatedGroup.build(), getAccesstoken());
        }
    }

    private AccessToken getAccesstoken() {
        return sessionData.getAccessToken();
    }
}
