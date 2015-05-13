package org.osiam.addons.administration.integration.group;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.osiam.addons.administration.elements.group.GroupList;
import org.osiam.addons.administration.elements.group.GroupMembership;
import org.osiam.addons.administration.integration.Integrationtest;
import org.osiam.addons.administration.selenium.Field;

/**
 * Test for View: Group List > Action > Edit Group Membership
 */
public class GroupMembershipIT extends Integrationtest {

    @Override
    public void setup() {
        super.setup();

        browser.doOauthLogin(ADMIN_USERNAME, ADMIN_PASSWORD);
        browser.click(GroupList.GROUP_LIST);
    }

    @Test
    public void assign_and_unassign_multiple_user() {
        final String testGroup = "test_group06";

        gotoMembershipGroupView(testGroup);

        assertEquals("Precondition is not given!", 0, getAssignedUserCount());

        // set both limit to "unlimited"
        browser.fill(new Field(GroupMembership.LIMIT_UNASSIGNED, "0"));
        browser.fill(new Field(GroupMembership.LIMIT_ASSIGNED, "0"));

        int allUserCount = getUnassignedUserCount();

        browser.click(GroupMembership.UNASSIGNED_MASTER_CHECKBOX);
        browser.click(GroupMembership.SUBMIT_BUTTON_UNASSIGNED_SELECTION);

        assertEquals(allUserCount, getAssignedUserCount());
        assertEquals(0, getUnassignedUserCount()); // FIXME: There is a bug in the server! See
                                                   // https://github.com/osiam/server/issues/247

        browser.click(GroupMembership.ASSIGNED_MASTER_CHECKBOX);
        browser.click(GroupMembership.SUBMIT_BUTTON_ASSIGNED_SELECTION);

        assertEquals(allUserCount, getUnassignedUserCount());
        assertEquals(0, getAssignedUserCount());
    }

    @Test
    public void paging_assigned_user() {
        final String testGroup = "test_group05";

        gotoMembershipGroupView(testGroup);

        browser.fill(new Field(GroupMembership.LIMIT_ASSIGNED, "5")); // set limit to 5
        int userCount = pageForwardAssigned();
        pageBackwardAssigned();

        browser.click(GroupMembership.PAGING_ASSIGNED_LAST);
        assertTrue(isUnassignedAtFirstPage());
        browser.click(GroupMembership.PAGING_ASSIGNED_FIRST);
        assertTrue(isUnassignedAtFirstPage());

        clickFirstPagingNumberAssigned();
        assertTrue(isUnassignedAtFirstPage());

        assertTrue(isUserUnassigned("03dc8f50-acaa-44d6-9401-bdfc5e10e821")); // adavies

        browser.fill(new Field(GroupMembership.LIMIT_ASSIGNED, "0")); // set limit to "unlimited"
        assertEquals(getAssignedUserCount(), userCount);
    }

    @Test
    public void paging_unassigned_user() {
        final String testGroup = "test_group04";

        gotoMembershipGroupView(testGroup);

        browser.fill(new Field(GroupMembership.LIMIT_UNASSIGNED, "5")); // set limit to 5
        int userCount = pageForwardUnassigned();
        pageBackwardUnassigned();

        browser.click(GroupMembership.PAGING_UNASSIGNED_LAST);
        assertTrue(isAssignedAtFirstPage());
        browser.click(GroupMembership.PAGING_UNASSIGNED_FIRST);
        assertTrue(isAssignedAtFirstPage());

        clickFirstPagingNumberUnassigned();
        assertTrue(isAssignedAtFirstPage());

        assertTrue(isUserAssigned("03dc8f50-acaa-44d6-9401-bdfc5e10e821")); // adavies

        browser.fill(new Field(GroupMembership.LIMIT_UNASSIGNED, "0")); // set limit to "unlimited"
        assertEquals(getUnassignedUserCount(), userCount);
    }

    @Test
    public void add_and_remove_group_member_with_single_button() {
        final String testGroup = "test_group08";

        final String userId = "d6f323e2-c717-4ab6-af9c-e639b50a948c"; // dcooper

        gotoMembershipGroupView(testGroup);

        assignUser(userId);

        assertTrue(isUserAssigned(userId));

        unassignUser(userId);

        assertFalse(isUserAssigned(userId));
    }

    @Test
    public void order() {
        final String testGroup = "test_group10";

        gotoMembershipGroupView(testGroup);

        browser.fill(new Field(GroupMembership.LIMIT_UNASSIGNED, "0")); // set limit to "unlimited"
        browser.click(GroupMembership.UNASSIGNED_MASTER_CHECKBOX);
        browser.click(GroupMembership.SUBMIT_BUTTON_UNASSIGNED_SELECTION);

        browser.fill(new Field(GroupMembership.LIMIT_ASSIGNED, "5")); // set limit to "5"
        browser.fill(new Field(GroupMembership.LIMIT_UNASSIGNED, "5")); // set limit to "5"

        browser.click(GroupMembership.SORT_ASSIGNED_LOGIN_DESC);
        assertTrue(isAssignedUserAtPosition("marissa", 0));
        browser.click(GroupMembership.SORT_ASSIGNED_LOGIN_ASC);
        assertTrue(isAssignedUserAtPosition("adavies", 0));

        browser.click(GroupMembership.SORT_UNASSIGNED_LOGIN_DESC);
        assertFalse(isUnassignedUserAtPosition("adavies", 0));
        browser.click(GroupMembership.SORT_UNASSIGNED_LOGIN_ASC);
        assertTrue(isUnassignedUserAtPosition("adavies", 0));

        browser.click(GroupMembership.SORT_ASSIGNED_GIVEN_NAME_DESC);
        assertTrue(isAssignedUserAtPosition("ewilley", 0)); // Willey
        browser.click(GroupMembership.SORT_ASSIGNED_GIVEN_NAME_ASC);
        assertTrue(isAssignedUserAtPosition("marissa", 0)); // marissa

        browser.click(GroupMembership.SORT_UNASSIGNED_GIVEN_NAME_DESC);
        assertFalse(isUnassignedUserAtPosition("adavies", 0));
        browser.click(GroupMembership.SORT_UNASSIGNED_GIVEN_NAME_ASC);
        assertFalse(isUnassignedUserAtPosition("ewilley", 0));

        browser.click(GroupMembership.SORT_ASSIGNED_FAMILY_NAME_DESC);
        assertTrue(isAssignedUserAtPosition("jcambell", 0)); // Thompson
        browser.click(GroupMembership.SORT_ASSIGNED_FAMILY_NAME_ASC);
        assertTrue(isAssignedUserAtPosition("marissa", 0)); // Barker

        browser.click(GroupMembership.SORT_UNASSIGNED_FAMILY_NAME_DESC);
        assertFalse(isUnassignedUserAtPosition("gparker", 0));
        browser.click(GroupMembership.SORT_UNASSIGNED_FAMILY_NAME_ASC);
        assertFalse(isUnassignedUserAtPosition("jcambell", 0));

        // page and sort
        browser.click(GroupMembership.PAGING_UNASSIGNED_NEXT);
        browser.click(GroupMembership.SORT_ASSIGNED_LOGIN_DESC);
        browser.click(GroupMembership.SORT_ASSIGNED_LOGIN_ASC);
        browser.click(GroupMembership.SORT_UNASSIGNED_LOGIN_DESC);
        assertTrue(isUnassignedUserAtPosition("ewilley", 0));
        assertTrue(isAssignedUserAtPosition("adavies", 0));

        browser.click(GroupMembership.PAGING_ASSIGNED_NEXT);
        browser.click(GroupMembership.SORT_ASSIGNED_LOGIN_DESC);
        assertTrue(isAssignedUserAtPosition("ewilley", 0));
        assertTrue(isUnassignedUserAtPosition("ewilley", 0));
    }

    @Test
    public void apply_empty_filter() {
        final String testGroup = "test_group01";

        gotoMembershipGroupView(testGroup);

        browser.click(GroupMembership.FILTER_BUTTON_ASSIGNED);

        assertTrue(isUserAssigned("03dc8f50-acaa-44d6-9401-bdfc5e10e821")); // adavies
        assertTrue(isUserUnassigned("ac3bacc9-915d-4bab-9145-9eb600d5e5bf")); // cmiller

        browser.click(GroupMembership.FILTER_BUTTON_UNASSIGNED);

        assertTrue(isUserAssigned("03dc8f50-acaa-44d6-9401-bdfc5e10e821")); // cmiller
        assertTrue(isUserUnassigned("ac3bacc9-915d-4bab-9145-9eb600d5e5bf")); // adavies
    }

    @Test
    public void apply_single_filter_assigned_user() {
        final String testGroup = "test_group01";

        final String assignedUser = "adavies";

        gotoMembershipGroupView(testGroup);

        browser.fill(new Field(GroupMembership.FILTER_ASSIGNED_LOGIN, assignedUser));
        browser.click(GroupMembership.FILTER_BUTTON_ASSIGNED);

        assertTrue(isUserAssigned("03dc8f50-acaa-44d6-9401-bdfc5e10e821")); // adavies
        assertFalse(isUserAssigned("7d33bcbe-a54c-43d8-867e-f6146164941e")); // hsimpson
        assertFalse(getUnassignedUserCount() <= 1);
    }

    @Test
    public void apply_single_filter_unassigned_user() {
        final String testGroup = "test_group01";

        final String unassignedUser = "cmiller";

        gotoMembershipGroupView(testGroup);

        browser.fill(new Field(GroupMembership.FILTER_UNASSIGNED_LOGIN, unassignedUser));
        browser.click(GroupMembership.FILTER_BUTTON_UNASSIGNED);

        assertTrue(isUserUnassigned("ac3bacc9-915d-4bab-9145-9eb600d5e5bf")); // cmiller
        assertFalse(isUserUnassigned("7d33bcbe-a54c-43d8-867e-f6146164941e")); // hsimpson
        assertFalse(getAssignedUserCount() <= 1);
    }

    @Test
    public void apply_no_result_filter_assigned_user() {
        final String testGroup = "test_group01";

        gotoMembershipGroupView(testGroup);

        browser.fill(new Field(GroupMembership.FILTER_ASSIGNED_LOGIN, "DoesNotExist"));
        browser.click(GroupMembership.FILTER_BUTTON_ASSIGNED);

        assertEquals(0, getAssignedUserCount());
        assertFalse(getUnassignedUserCount() == 0);
    }

    @Test
    public void apply_no_result_filter_unassigned_user() {
        final String testGroup = "test_group01";

        gotoMembershipGroupView(testGroup);

        browser.fill(new Field(GroupMembership.FILTER_UNASSIGNED_LOGIN, "DoesNotExist"));
        browser.click(GroupMembership.FILTER_BUTTON_UNASSIGNED);

        assertEquals(0, getUnassignedUserCount());
        assertFalse(getAssignedUserCount() == 0);
    }

    @Test
    public void apply_multi_filter_assigned_user() {
        final String testGroup = "test_group01";

        gotoMembershipGroupView(testGroup);

        // Also tests trimming in filter fields
        browser.fill(new Field(GroupMembership.FILTER_ASSIGNED_LOGIN, "adavies"),
                new Field(GroupMembership.FILTER_ASSIGNED_GIVEN_NAME, "  Adeline  "),
                new Field(GroupMembership.FILTER_ASSIGNED_FAMILY_NAME, "  Davies  "));
        browser.click(GroupMembership.FILTER_BUTTON_ASSIGNED);

        assertTrue(isUserAssigned("03dc8f50-acaa-44d6-9401-bdfc5e10e821")); // adavies
        assertEquals(getAssignedUserCount(), 1);
        assertFalse(getUnassignedUserCount() <= 1);
    }

    @Test
    public void apply_multi_filter_unassigned_user() {
        final String testGroup = "test_group01";

        gotoMembershipGroupView(testGroup);

        // Also tests trimming in filter fields
        browser.fill(new Field(GroupMembership.FILTER_UNASSIGNED_LOGIN, "bjensen"),
                new Field(GroupMembership.FILTER_UNASSIGNED_GIVEN_NAME, "  Barbara  "),
                new Field(GroupMembership.FILTER_UNASSIGNED_FAMILY_NAME, "  Jensen  "));
        browser.click(GroupMembership.FILTER_BUTTON_UNASSIGNED);

        assertTrue(isUserUnassigned("834b410a-943b-4c80-817a-4465aed037bc")); // bjensen
        assertEquals(getUnassignedUserCount(), 1);
        assertFalse(getAssignedUserCount() <= 1);
    }

    private void pageBackwardAssigned() {
        while (true) {
            try {
                browser.click(GroupMembership.PAGING_ASSIGNED_PREVIOUS);

                assertTrue(isUnassignedAtFirstPage());
                assertTrue(isUserUnassigned("03dc8f50-acaa-44d6-9401-bdfc5e10e821")); // adavies
            } catch (NoSuchElementException e) {
                break;
            }
            assertFalse(browser.isErrorPage());
        }
    }

    private int pageForwardAssigned() {
        int userCount = 0;

        while (true) {
            try {
                userCount += getAssignedUserCount();
                browser.click(GroupMembership.PAGING_ASSIGNED_NEXT);

                assertTrue(isUnassignedAtFirstPage());
                assertTrue(isUserUnassigned("03dc8f50-acaa-44d6-9401-bdfc5e10e821")); // adavies
            } catch (NoSuchElementException e) {
                break;
            }
            assertFalse(browser.isErrorPage());
        }
        return userCount;
    }

    private void clickFirstPagingNumberAssigned() {
        String pagingNumberXpath = "//div[contains(@id, 'form-group-insider')]//a[contains(@id, 'paging-') "
                + "and not(@id = 'paging-first') "
                + "and not(@id = 'paging-prev') "
                + "and not(@id = 'paging-next') "
                + "and not(@id = 'paging-last') " + "and not(@href = '#')]";
        browser.findElements(By.xpath(pagingNumberXpath)).get(0).click();
    }

    private boolean isUnassignedAtFirstPage() {
        String firstPageActive = "//div[contains(@id, 'form-group-outsider')]//li[contains(@class, 'active')]//a[contains(@id, 'paging-0')]";

        try {
            browser.findElement(By.xpath(firstPageActive));
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    private boolean isUserUnassigned(String userValue) {
        try {
            browser.findElement(By.xpath("//table[contains(@id, 'outsider')]//a[contains(@href, '" + userValue + "')]"));
        } catch (NoSuchElementException e) {
            return false;
        }

        return true;
    }

    private void gotoMembershipGroupView(String groupName) {
        String actionLabelXpath = "//td[. = '" + groupName + "']/..//div[contains(@id, 'action-label')]";
        String editMembershipButtonXpath = "//td[. = '" + groupName
                + "']/..//button[contains(@id, 'action-button-edit-user-membership')]";

        browser.findElement(By.xpath(actionLabelXpath)).click();
        browser.findElement(By.xpath(editMembershipButtonXpath)).click();
    }

    private void pageBackwardUnassigned() {
        while (true) {
            try {
                browser.click(GroupMembership.PAGING_UNASSIGNED_PREVIOUS);

                assertTrue(isAssignedAtFirstPage());
                assertTrue(isUserAssigned("03dc8f50-acaa-44d6-9401-bdfc5e10e821")); // adavies
            } catch (NoSuchElementException e) {
                break;
            }
            assertFalse(browser.isErrorPage());
        }
    }

    private int pageForwardUnassigned() {
        int userCount = 0;

        while (true) {
            try {
                userCount += getUnassignedUserCount();
                browser.click(GroupMembership.PAGING_UNASSIGNED_NEXT);

                assertTrue(isAssignedAtFirstPage());
                // adavies
                assertTrue(isUserAssigned("03dc8f50-acaa-44d6-9401-bdfc5e10e821"));
            } catch (NoSuchElementException e) {
                break;
            }
            assertFalse(browser.isErrorPage());
        }
        return userCount;
    }

    private void clickFirstPagingNumberUnassigned() {
        String pagingNumberXpath = "//div[contains(@id, 'form-group-outsider')]//a[contains(@id, 'paging-') "
                + "and not(@id = 'paging-first') "
                + "and not(@id = 'paging-prev') "
                + "and not(@id = 'paging-next') "
                + "and not(@id = 'paging-last') " + "and not(@href = '#')]";
        browser.findElements(By.xpath(pagingNumberXpath)).get(0).click();
    }

    private boolean isAssignedAtFirstPage() {
        String firstPageActive = "//div[contains(@id, 'form-group-insider')]//li[contains(@class, 'active')]//a[contains(@id, 'paging-0')]";

        try {
            browser.findElement(By.xpath(firstPageActive));
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    private int getAssignedUserCount() {
        String userRowsXpath = "//table[contains(@id,'insider')]//tr//td//input[contains(@class, 'checkbox')]";
        return browser.findElements(By.xpath(userRowsXpath)).size();
    }

    private int getUnassignedUserCount() {
        String userRowsXpath = "//table[contains(@id,'outsider')]//tr//td//input[contains(@class, 'checkbox')]";
        return browser.findElements(By.xpath(userRowsXpath)).size();
    }

    private boolean isUserAssigned(String userValue) {
        try {
            browser.findElement(By.xpath("//table[contains(@id, 'insider')]//a[contains(@href, '" + userValue + "')]"));
        } catch (NoSuchElementException e) {
            return false;
        }

        return true;
    }

    private boolean isAssignedUserAtPosition(String username, Integer position) {
        // the row #2 is the first user-row!
        String rowXpath = "//table[contains(@id, 'insider')]//tr[" + (position + 2)
                + "]//td[contains(., '" + username + "')]";

        return browser.findElements(By.xpath(rowXpath)).size() == 1;
    }

    private boolean isUnassignedUserAtPosition(String username, Integer position) {
        // the row #2 is the first user-row!
        String rowXpath = "//table[contains(@id, 'outsider')]//tr[" + (position + 2)
                + "]//td[contains(., '" + username + "')]";

        return browser.findElements(By.xpath(rowXpath)).size() == 1;
    }

    private void assignUser(String userValue) {
        String assignUserIconXpath = "//div[contains(@id, 'form-group-outsider')]//a[contains(@href, '" + userValue
                + "')]";
        browser.findElement(By.xpath(assignUserIconXpath)).click();
    }

    private void unassignUser(String userValue) {
        String unassignUserIconXpath = "//div[contains(@id, 'form-group-insider')]//a[contains(@href, '" + userValue
                + "')]";
        browser.findElement(By.xpath(unassignUserIconXpath)).click();
    }
}
