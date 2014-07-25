package org.osiam.addons.administration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
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
import org.osiam.resources.scim.UpdateUser;
import org.osiam.resources.scim.User;

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
    UserService toTestSpy = new UserService();

    @Test
    public void searchUser_advanced() {
        final String query = "testQuery";
        final Integer limit = 13;
        final Long offset = 12L;
        final String orderBy = "orderby";
        final Boolean asc = false; // desc
        final String attributes = "attributes";

        toTestSpy.searchUser(query, limit, offset, orderBy, asc, attributes);

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

        User user = mock(User.class);
        doReturn(user).when(connector).getUser(eq(id), same(accessToken));

        User result = toTestSpy.getUser(id);
        assertEquals(user, result);
    }

    @Test
    public void updateUser() {
        UpdateUser updateUser = new UpdateUser.Builder().build();
        String id = "user ID";

        toTestSpy.updateUser(id, updateUser);
        verify(connector, times(1)).updateUser(eq(id), eq(updateUser), same(accessToken));
    }
    
    @Test
    public void deactivateUser() {
        String id = "userID";
        
        toTestSpy.deactivateUser(id);
        ArgumentCaptor<UpdateUser> cap = ArgumentCaptor.forClass(UpdateUser.class);
        
        verify(connector, times(1)).updateUser(eq(id), cap.capture(), same(accessToken));
        assertFalse(cap.getValue().getScimConformUpdateUser().isActive());
    }
}
