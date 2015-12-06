package org.osiam.addons.administration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.osiam.addons.administration.model.session.GeneralSessionData;
import org.osiam.client.OsiamConnector;
import org.osiam.client.oauth.AccessToken;
import org.osiam.client.query.Query;
import org.osiam.resources.scim.Group;
import org.osiam.resources.scim.MemberRef;
import org.osiam.resources.scim.UpdateGroup;

@RunWith(MockitoJUnitRunner.class)
public class GroupServiceTest {

    final String OPERATION_DELETE = "delete";
    final String OPERATION_ADD = null;

    @Mock
    OsiamConnector connector;

    @Mock
    AccessToken accessToken;

    @Spy
    @InjectMocks
    GeneralSessionData sessionData = new GeneralSessionData();

    @Spy
    @InjectMocks
    GroupService groupService = new GroupService();

    @Test
    public void searchGroup_advanced() {
        final String query = "testQuery";
        final Integer limit = 13;
        final Long offset = 12L;
        final String orderBy = "orderby";
        final Boolean asc = false; // desc

        groupService.searchGroup(query, limit, offset, orderBy, asc);

        ArgumentCaptor<Query> cap = ArgumentCaptor.forClass(Query.class);

        verify(connector).searchGroups(cap.capture(), same(accessToken));

        Query usedQuery = cap.getValue();
        assertEquals(query, usedQuery.getFilter());
        assertTrue(limit == usedQuery.getCount());
        assertTrue(offset == usedQuery.getStartIndex());
        assertEquals(orderBy, usedQuery.getSortBy());
        assertEquals("descending", usedQuery.getSortOrder());
    }

    @Test
    public void deleteGroup() {
        final String id = "groupID";

        groupService.deleteGroup(id);

        verify(connector).deleteGroup(eq(id), same(accessToken));
    }

    @Test
    public void getGroup() {
        String id = "groupID";

        Group group = new Group.Builder("testGroup").build();
        doReturn(group).when(connector).getGroup(eq(id), same(accessToken));

        Group result = groupService.getGroup(id);
        assertEquals(group, result);
    }

    @Test
    public void updateGroup() {
        UpdateGroup updateGroup = new UpdateGroup.Builder().build();
        String id = "user ID";

        groupService.updateGroup(id, updateGroup);
        verify(connector, times(1)).updateGroup(eq(id), eq(updateGroup), same(accessToken));
    }

    @Test
    public void addUserToGroups_emptyGroupIds() {
        final String userId = "userId";

        groupService.addUserToGroups(userId);

        verify(connector, never()).updateGroup(anyString(), any(UpdateGroup.class), any(AccessToken.class));
    }

    @Test
    public void addUserToGroups_oneGroupId() {
        final String userId = "userId";
        final String groupId = "groupId";

        groupService.addUserToGroups(userId, groupId);

        ArgumentCaptor<UpdateGroup> updateCap = ArgumentCaptor.forClass(UpdateGroup.class);

        verify(connector, times(1)).updateGroup(
                eq(groupId), updateCap.capture(), same(accessToken));

        assertContainsMember(updateCap.getValue(), userId, OPERATION_ADD);
    }

    @Test
    public void addUserToGroups_multiGroupIds() {
        final String userId = "userId";
        final String[] groupIds = new String[] { "groupId#1", "groupId#2" };

        groupService.addUserToGroups(userId, groupIds);

        for (int i = 0; i < groupIds.length; i++) {
            ArgumentCaptor<UpdateGroup> updateCap = ArgumentCaptor.forClass(UpdateGroup.class);

            verify(connector, times(1)).updateGroup(
                    eq(groupIds[i]), updateCap.capture(), same(accessToken));

            assertContainsMember(updateCap.getValue(), userId, OPERATION_ADD);
        }
    }

    @Test
    public void addUsersToGroup_emptyUserIds() {
        final String userId = "userId";

        groupService.addUsersToGroup(userId);

        verify(connector, never()).updateGroup(anyString(), any(UpdateGroup.class), any(AccessToken.class));
    }

    @Test
    public void addUsersToGroup_oneUserId() {
        final String groupId = "groupId";
        final String userId = "userId";

        groupService.addUsersToGroup(groupId, userId);

        ArgumentCaptor<UpdateGroup> updateCap = ArgumentCaptor.forClass(UpdateGroup.class);

        verify(connector, times(1)).updateGroup(
                eq(groupId), updateCap.capture(), same(accessToken));

        assertContainsMember(updateCap.getValue(), userId, OPERATION_ADD);
    }

    @Test
    public void addUsersToGroup_multiUserIds() {
        final String groupId = "groupId";
        final String[] userIds = new String[] { "userId#1", "userId#2" };

        groupService.addUsersToGroup(groupId, userIds);

        ArgumentCaptor<UpdateGroup> updateCap = ArgumentCaptor.forClass(UpdateGroup.class);

        verify(connector, times(1)).updateGroup(
                eq(groupId), updateCap.capture(), same(accessToken));

        for (int i = 0; i < userIds.length; i++) {
            assertContainsMember(updateCap.getValue(), userIds[i], OPERATION_ADD);
        }
    }

    @Test
    public void removeUsersFromGroup_emptyUserIds() {
        final String groupId = "groupId";

        groupService.removeUsersFromGroup(groupId);

        verify(connector, never()).updateGroup(anyString(), any(UpdateGroup.class), any(AccessToken.class));
    }

    @Test
    public void removeUsersFromGroup_oneGroupId() {
        final String groupId = "groupId";
        final String userId = "userId";

        groupService.removeUsersFromGroup(groupId, userId);

        ArgumentCaptor<UpdateGroup> updateCap = ArgumentCaptor.forClass(UpdateGroup.class);

        verify(connector, times(1)).updateGroup(
                eq(groupId), updateCap.capture(), same(accessToken));

        assertContainsMember(updateCap.getValue(), userId, OPERATION_DELETE);
    }

    @Test
    public void removeUsersFromGroup_multiUserIds() {
        final String groupId = "groupId";
        final String[] userIds = new String[] { "userId#1", "userId#2" };

        groupService.removeUsersFromGroup(groupId, userIds);

        ArgumentCaptor<UpdateGroup> updateCap = ArgumentCaptor.forClass(UpdateGroup.class);

        verify(connector, times(1)).updateGroup(
                eq(groupId), updateCap.capture(), same(accessToken));

        for (int i = 0; i < userIds.length; i++) {
            assertContainsMember(updateCap.getValue(), userIds[i], OPERATION_DELETE);
        }
    }

    @Test
    public void removeUserFromGroups_emptyGroupIds() {
        final String userId = "userId";

        groupService.removeUserFromGroups(userId);

        verify(connector, never()).updateGroup(anyString(), any(UpdateGroup.class), any(AccessToken.class));
    }

    @Test
    public void removeUserFromGroups_oneGroupId() {
        final String userId = "userId";
        final String groupId = "groupId";

        groupService.removeUserFromGroups(userId, groupId);

        ArgumentCaptor<UpdateGroup> updateCap = ArgumentCaptor.forClass(UpdateGroup.class);

        verify(connector, times(1)).updateGroup(
                eq(groupId), updateCap.capture(), same(accessToken));

        assertContainsMember(updateCap.getValue(), userId, OPERATION_DELETE);
    }

    @Test
    public void removeUserFromGroups_multiGroupIds() {
        final String userId = "userId";
        final String[] groupIds = new String[] { "groupId#1", "groupId#2" };

        groupService.removeUserFromGroups(userId, groupIds);

        for (int i = 0; i < groupIds.length; i++) {
            ArgumentCaptor<UpdateGroup> updateCap = ArgumentCaptor.forClass(UpdateGroup.class);

            verify(connector, times(1)).updateGroup(
                    eq(groupIds[i]), updateCap.capture(), same(accessToken));

            assertContainsMember(updateCap.getValue(), userId, OPERATION_DELETE);
        }
    }

    private void assertContainsMember(UpdateGroup update, String userId, String operation) {
        Group group = update.getScimConformUpdateGroup();

        boolean found = false;
        for (MemberRef member : group.getMembers()) {
            if (member.getValue().equals(userId)) {
                if ((operation == null && member.getOperation() == null) ||
                        (operation != null && operation.equals(member.getOperation()))) {

                    found = true;
                }
            }
        }

        assertTrue(found);
    }
}
