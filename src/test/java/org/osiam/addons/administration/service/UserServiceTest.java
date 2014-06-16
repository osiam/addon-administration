package org.osiam.addons.administration.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.osiam.addons.administration.model.SessionData;
import org.osiam.client.OsiamConnector;
import org.osiam.client.oauth.AccessToken;
import org.osiam.client.query.Query;
import org.osiam.resources.scim.SCIMSearchResult;
import org.osiam.resources.scim.User;

public class UserServiceTest {

    UserService toTest;
    UserService toTestSpy;
    
    @Before
    public void setup(){
        toTest = new UserService();
        toTest.sessionData = new SessionData();
        toTest.sessionData.setAccesstoken(mock(AccessToken.class));
        toTest.connector = mock(OsiamConnector.class);
        
        toTestSpy = spy(toTest);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void searchUser_onlyQuery(){
        final SCIMSearchResult<User> toReturn = mock(SCIMSearchResult.class);
        doReturn(toReturn).when(toTestSpy).searchUser(anyString(), anyInt(), anyLong(), anyString(), anyBoolean());
        
        final String query = "TestQuery";
        final SCIMSearchResult<User> result = toTestSpy.searchUser(query);
        
        assertSame(result, toReturn);
        verify(toTestSpy).searchUser(query, null, null, null, null);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void searchUser_withLimit(){
        final SCIMSearchResult<User> toReturn = mock(SCIMSearchResult.class);
        doReturn(toReturn).when(toTestSpy).searchUser(anyString(), anyInt(), anyLong(), anyString(), anyBoolean());
        
        final String query = "TestQuery";
        final Integer limit = 13;
        final Long offset = 12L;
        final SCIMSearchResult<User> result = toTestSpy.searchUser(query, limit, offset);
        
        assertSame(result, toReturn);
        verify(toTestSpy).searchUser(query, limit, offset, null, null);
    }
    
    @Test
    public void searchUser_advanced(){
        final String query = "testQuery";
        final Integer limit = 13;
        final Long offset = 12L;
        final String orderBy = "orderby";
        final Boolean asc = false;  //desc
        
        toTestSpy.searchUser(query, limit, offset, orderBy, asc);
        
        ArgumentCaptor<Query> cap = ArgumentCaptor.forClass(Query.class);
        
        verify(toTest.connector).searchUsers(cap.capture(), same(toTest.sessionData.getAccesstoken()));
        
        Query usedQuery = cap.getValue();
        assertEquals(query, usedQuery.getFilter());
        assertTrue(limit == usedQuery.getCount());
        assertTrue(offset == usedQuery.getStartIndex());
        assertEquals(orderBy, usedQuery.getSortBy());
        assertEquals("descending", usedQuery.getSortOrder());
    }
}
