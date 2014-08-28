package org.osiam.addons.administration.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.osiam.addons.administration.Element.EditList;
import org.osiam.addons.administration.Element.GroupList;
import org.osiam.addons.administration.selenium.Field;

public class GroupListIT extends Integrationtest {
    String firstGroup = "test_group01";
    
    @Override
    public void setup() {
        super.setup();

        browser.doOauthLogin(ADMIN_USERNAME, ADMIN_PASSWORD);
    }
    
    @Test
    public void apply_empty_filter() {
        browser.click(GroupList.GROUP_LIST);
        
        browser.click(EditList.FILTER_BUTTON);

        assertTrue(isGroupVisible(firstGroup));
    }

    @Test
    public void apply_filter() {
        browser.click(GroupList.GROUP_LIST);
        
        browser.fill(new Field(GroupList.FILTER_GROUP, firstGroup));
        browser.click(EditList.FILTER_BUTTON);

        assertTrue(isGroupVisible(firstGroup));
        assertFalse(isGroupVisible("test_group02")); // an another user
    }
    
    @Test
    public void apply_no_result_filter() {
        browser.click(GroupList.GROUP_LIST);
        
        browser.fill(
                new Field(GroupList.FILTER_GROUP, "DoesNotExist"));
        browser.click(EditList.FILTER_BUTTON);

        assertEquals(0, getDisplayedGroup());
    }

    @Test
    public void order() {
        browser.click(GroupList.GROUP_LIST);
        
        browser.click(GroupList.SORT_GROUP_ASC);
        assertTrue(isGroupAtPosition(firstGroup, 0));

        browser.click(GroupList.SORT_GROUP_DESC);
        assertTrue(isGroupAtPosition("test_group10", 0));
    }
    
    @Test
    public void limit() {
        browser.click(GroupList.GROUP_LIST);
        
        // default limit: 20
        assertEquals("20", browser.findSelectedOption(EditList.LIMIT).getText());

        // change limit seticfied
        assertTrue("Precondition is not satisfied! For this testcase the dataset must be contains more than 5 users!",
                getDisplayedGroup() > 5);

        browser.fill(new Field(EditList.LIMIT, "5"));
        assertTrue(getDisplayedGroup() <= 5);
        assertEquals("5", browser.findSelectedOption(EditList.LIMIT).getText());
    }
    
    @Test
    public void paging() {
        browser.click(GroupList.GROUP_LIST);
        
        browser.fill(new Field(EditList.LIMIT, "5")); // set limit to 5
        int groupCount = 0;

        while (true) {
            try {
                groupCount += getDisplayedGroup();
                browser.click(EditList.PAGING_NEXT);
            } catch (NoSuchElementException e) {
                break;
            }

            assertFalse(browser.isErrorPage());
        }

        while (true) {
            try {
                browser.click(EditList.PAGING_PREVIOUS);
            } catch (NoSuchElementException e) {
                break;
            }

            assertFalse(browser.isErrorPage());
        }

        browser.click(EditList.PAGING_LAST);
        assertFalse(browser.isErrorPage());

        browser.click(EditList.PAGING_FIRST);
        assertFalse(browser.isErrorPage());

        browser.fill(new Field(EditList.LIMIT, "0")); // set limit to "unlimited"
        assertEquals(getDisplayedGroup(), groupCount);
    }
    
    private int getDisplayedGroup() {
        String userRowsXpath = "//table//tr//li[contains(@class, 'action')]";

        return browser.findElements(By.xpath(userRowsXpath)).size();
    }

    private boolean isGroupVisible(String group) {
        return browser.isTextPresent(group);
    }
    
    private boolean isGroupAtPosition(String group, Integer position) {
        // the row #2 is the first user-row!
        String rowXpath = "//table//tr[" + (2 - position) + "]//td[contains(., '" + group + "')]";

        return browser.findElements(By.xpath(rowXpath)).size() == 1;
    }
}
