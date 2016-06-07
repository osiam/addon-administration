package org.osiam.addons.administration.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.osiam.addons.administration.model.session.GeneralSessionData;
import org.osiam.addons.administration.model.session.PagingInformation;
import org.osiam.client.OsiamConnector;
import org.osiam.client.oauth.AccessToken;
import org.osiam.client.query.Query;
import org.osiam.resources.scim.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    OsiamConnector connector;

    @Mock
    AccessToken accessToken;

    @Spy
    @InjectMocks
    GeneralSessionData sessionData = new GeneralSessionData();

    @Spy
    @InjectMocks
    UserService userService = new UserService();

    @Test
    public void searchUser_advanced() {
        final String query = "testQuery";
        final Integer limit = 13;
        final Long offset = 12L;
        final String orderBy = "orderby";
        final Boolean asc = false; // desc
        final String attributes = "attributes";

        userService.searchUser(query, limit, offset, orderBy, asc, attributes);

        ArgumentCaptor<Query> cap = ArgumentCaptor.forClass(Query.class);

        verify(connector).searchUsers(cap.capture(), same(accessToken));

        Query usedQuery = cap.getValue();
        assertEquals(query, usedQuery.getFilter());
        assertTrue(limit == usedQuery.getCount());
        assertTrue(offset == usedQuery.getStartIndex());
        assertEquals(orderBy, usedQuery.getSortBy());
        assertEquals("descending", usedQuery.getSortOrder());
        assertEquals(attributes, usedQuery.getAttributes());
    }

    @Test
    public void getUser() {
        String id = "userID";

        User user = new User.Builder("testUser").build();
        doReturn(user).when(connector).getUser(eq(id), same(accessToken));

        User result = userService.getUser(id);
        assertEquals(user, result);
    }

    @Test
    public void logoutCurrentUser() {
        userService.logoutCurrentUser();

        verify(connector, times(1)).revokeAccessToken(same(accessToken));
    }

    @Test
    public void createUser() {
        User newUser = new User.Builder("username").build();

        userService.createUser(newUser);
        verify(connector, times(1)).createUser(same(newUser), same(accessToken));
    }

    @Test
    public void deactivateUser() {
        String id = "userID";
        User user = new User.Builder("testUser").build();
        doReturn(user).when(connector).getUser(eq(id), same(accessToken));

        userService.deactivateUser(id);
        ArgumentCaptor<User> cap = ArgumentCaptor.forClass(User.class);

        verify(connector, times(1)).replaceUser(eq(id), cap.capture(), same(accessToken));
        assertFalse(cap.getValue().isActive());
    }

    @Test
    public void activateUser() {
        String id = "userID";
        User user = new User.Builder("testUser").build();
        doReturn(user).when(connector).getUser(eq(id), same(accessToken));

        userService.activateUser(id);
        ArgumentCaptor<User> cap = ArgumentCaptor.forClass(User.class);

        verify(connector, times(1)).replaceUser(eq(id), cap.capture(), same(accessToken));
        assertTrue(cap.getValue().isActive());
    }

    @Test
    public void deleteUser() {
        String id = "userId";

        userService.deleteUser(id);

        verify(connector, times(1)).deleteUser(eq(id), same(accessToken));
    }

    @Test
    public void replaceUser() {
        User replaceUser = new User.Builder().build();
        String id = "user ID";

        userService.replaceUser(id, replaceUser);
        verify(connector, times(1)).replaceUser(eq(id), eq(replaceUser), same(accessToken));
    }

    @Test
    public void getAssignedUsers_withoutQuery() {
        ArgumentCaptor<Query> cap = ArgumentCaptor.forClass(Query.class);
        String groupId = "groupId";
        String attributes = "aaa, bbb, ccc";
        String filter = "groups eq \"" + groupId + "\"";
        String sortOrder = "descending";
        String sortBy = null;
        boolean ascending = false;
        int count = 654654;
        int startIndex = 1995;

        PagingInformation pagingInformation = new PagingInformation();
        pagingInformation.setAscending(ascending);
        pagingInformation.setLimit(count);
        pagingInformation.setOffset((long) startIndex);

        userService.getAssignedUsers(groupId, pagingInformation, attributes);
        verify(connector, times(1)).searchUsers(cap.capture(), same(accessToken));

        assertEquals(cap.getValue().getAttributes(), attributes);
        assertEquals(cap.getValue().getFilter(), filter);
        assertEquals(cap.getValue().getSortBy(), sortBy);
        assertEquals(cap.getValue().getCount(), count);
        assertEquals(cap.getValue().getSortOrder(), sortOrder);
        assertEquals(cap.getValue().getStartIndex(), startIndex);
    }

    @Test
    public void getAssignedUsers_withQuery() {
        ArgumentCaptor<Query> cap = ArgumentCaptor.forClass(Query.class);
        String groupId = "groupId";
        String query = "the same = eq d-";
        String attributes = "aaa, bbb, ccc";
        String filter = "groups eq \"" + groupId + "\" and " + query;
        String sortOrder = "descending";
        String sortBy = null;
        boolean ascending = false;
        int count = 654654;
        int startIndex = 1995;

        PagingInformation pagingInformation = new PagingInformation();
        pagingInformation.setAscending(ascending);
        pagingInformation.setLimit(count);
        pagingInformation.setOffset((long) startIndex);
        pagingInformation.setQuery(query);

        userService.getAssignedUsers(groupId, pagingInformation, attributes);
        verify(connector, times(1)).searchUsers(cap.capture(), same(accessToken));

        assertEquals(cap.getValue().getAttributes(), attributes);
        assertEquals(cap.getValue().getFilter(), filter);
        assertEquals(cap.getValue().getSortBy(), sortBy);
        assertEquals(cap.getValue().getCount(), count);
        assertEquals(cap.getValue().getSortOrder(), sortOrder);
        assertEquals(cap.getValue().getStartIndex(), startIndex);
    }
}
