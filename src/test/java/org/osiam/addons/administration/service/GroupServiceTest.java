package org.osiam.addons.administration.service;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RunWith(MockitoJUnitRunner.class)
public class GroupServiceTest {

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

        groupService.searchGroup(query, limit, offset, orderBy, false);

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
    public void addUserToGroups_emptyGroupIds() {
        final String userId = "userId";

        groupService.addUserToGroups(userId);

        verify(connector, never()).replaceGroup(anyString(), any(Group.class), any(AccessToken.class));
    }

    @Test
    public void addUserToGroups_oneGroupId() {
        final String userId = "userId";
        final String groupId = "groupId";
        Group group = new Group.Builder("testGroup").build();
        doReturn(group).when(connector).getGroup(eq(groupId), same(accessToken));

        groupService.addUserToGroups(userId, groupId);

        ArgumentCaptor<Group> updateCap = ArgumentCaptor.forClass(Group.class);

        verify(connector, times(1)).replaceGroup(
                eq(groupId), updateCap.capture(), same(accessToken));

        assertContainsMember(updateCap.getValue(), userId);
    }

    @Test
    public void addUserToGroups_multiGroupIds() {
        final String userId = "userId";
        final String[] groupIds = new String[] { "groupId#1", "groupId#2" };
        Group group = new Group.Builder("testGroup").build();
        doReturn(group).when(connector).getGroup(anyString(), same(accessToken));

        groupService.addUserToGroups(userId, groupIds);

        for (String groupId : groupIds) {
            ArgumentCaptor<Group> updateCap = ArgumentCaptor.forClass(Group.class);

            verify(connector, times(1)).replaceGroup(eq(groupId), updateCap.capture(), same(accessToken));

            assertContainsMember(updateCap.getValue(), userId);
        }
    }

    @Test
    public void addUsersToGroup_emptyUserIds() {
        final String userId = "userId";

        groupService.addUsersToGroup(userId);

        verify(connector, never()).replaceGroup(anyString(), any(Group.class), any(AccessToken.class));
    }

    @Test
    public void addUsersToGroup_oneUserId() {
        final String groupId = "groupId";
        final String userId = "userId";
        Group group = new Group.Builder("testGroup").build();
        doReturn(group).when(connector).getGroup(eq(groupId), same(accessToken));

        groupService.addUsersToGroup(groupId, userId);

        ArgumentCaptor<Group> updateCap = ArgumentCaptor.forClass(Group.class);

        verify(connector, times(1)).replaceGroup(
                eq(groupId), updateCap.capture(), same(accessToken));

        assertContainsMember(updateCap.getValue(), userId);
    }

    @Test
    public void addUsersToGroup_multiUserIds() {
        final String groupId = "groupId";
        final String[] userIds = new String[] { "userId#1", "userId#2" };
        Group group = new Group.Builder("testGroup").build();
        doReturn(group).when(connector).getGroup(eq(groupId), same(accessToken));

        groupService.addUsersToGroup(groupId, userIds);

        ArgumentCaptor<Group> updateCap = ArgumentCaptor.forClass(Group.class);

        verify(connector, times(1)).replaceGroup(
                eq(groupId), updateCap.capture(), same(accessToken));

        for (String userId : userIds) {
            assertContainsMember(updateCap.getValue(), userId);
        }
    }

    @Test
    public void removeUsersFromGroup_emptyUserIds() {
        final String groupId = "groupId";

        groupService.removeUsersFromGroup(groupId);

        verify(connector, never()).replaceGroup(anyString(), any(Group.class), any(AccessToken.class));
    }

    @Test
    public void removeUsersFromGroup_oneGroupId() {
        final String groupId = "groupId";
        final String userId = "userId";
        Group group = new Group.Builder("testGroup").build();
        doReturn(group).when(connector).getGroup(eq(groupId), same(accessToken));

        groupService.removeUsersFromGroup(groupId, userId);

        ArgumentCaptor<Group> updateCap = ArgumentCaptor.forClass(Group.class);

        verify(connector, times(1)).replaceGroup(eq(groupId), updateCap.capture(), same(accessToken));

        assertNotContainsMember(updateCap.getValue(), userId);
    }

    @Test
    public void removeUsersFromGroup_multiUserIds() {
        final String groupId = "groupId";
        final String[] userIds = new String[] { "userId#1", "userId#2" };
        Group group = new Group.Builder("testGroup").build();
        doReturn(group).when(connector).getGroup(eq(groupId), same(accessToken));

        groupService.removeUsersFromGroup(groupId, userIds);

        ArgumentCaptor<Group> updateCap = ArgumentCaptor.forClass(Group.class);

        verify(connector, times(1)).replaceGroup(
                eq(groupId), updateCap.capture(), same(accessToken));

        for (String userId : userIds) {
            assertNotContainsMember(updateCap.getValue(), userId);
        }
    }

    @Test
    public void removeUserFromGroups_emptyGroupIds() {
        final String userId = "userId";

        groupService.removeUserFromGroups(userId);

        verify(connector, never()).replaceGroup(anyString(), any(Group.class), any(AccessToken.class));
    }

    @Test
    public void removeUserFromGroups_oneGroupId() {
        final String userId = "userId";
        final String groupId = "groupId";
        Group group = new Group.Builder("testGroup").build();
        doReturn(group).when(connector).getGroup(eq(groupId), same(accessToken));

        groupService.removeUserFromGroups(userId, groupId);

        ArgumentCaptor<Group> updateCap = ArgumentCaptor.forClass(Group.class);

        verify(connector, times(1)).replaceGroup(eq(groupId), updateCap.capture(), same(accessToken));

        assertNotContainsMember(updateCap.getValue(), userId);
    }

    @Test
    public void removeUserFromGroups_multiGroupIds() {
        final String userId = "userId";
        final String[] groupIds = new String[] { "groupId#1", "groupId#2" };
        Group group1 = new Group.Builder("testGroup1").build();
        doReturn(group1).when(connector).getGroup(eq("groupId#1"), same(accessToken));
        Group group2 = new Group.Builder("testGroup2").build();
        doReturn(group2).when(connector).getGroup(eq("groupId#2"), same(accessToken));

        groupService.removeUserFromGroups(userId, groupIds);

        for (String groupId : groupIds) {
            ArgumentCaptor<Group> updateCap = ArgumentCaptor.forClass(Group.class);

            verify(connector, times(1)).replaceGroup(eq(groupId), updateCap.capture(), same(accessToken));

            assertNotContainsMember(updateCap.getValue(), userId);
        }
    }

    private void assertNotContainsMember(Group group, String userId) {
        boolean found = false;
        for (MemberRef member : group.getMembers()) {
            if (member.getValue().equals(userId)) {
                found = true;
            }
        }

        assertFalse(found);
    }

    private void assertContainsMember(Group group, String userId) {
        boolean found = false;
        for (MemberRef member : group.getMembers()) {
            if (member.getValue().equals(userId)) {
                found = true;
            }
        }

        assertTrue(found);
    }
}
