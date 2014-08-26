package org.osiam.addons.administration.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.osiam.addons.administration.Element.EditList;
import org.osiam.addons.administration.Element.UserList;
import org.osiam.addons.administration.selenium.Field;

public class UserGroupIT extends Integrationtest {
    
    @Override
    public void setup() {
        super.setup();

        browser.doOauthLogin(ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    @Test
    public void group() {
        //auf group list klicken
        //paging-tests von userlist
        //auch ergebnisse pro seite
        //filtern auch von da
        //sorting ebenfalls
        //
        //2. gruppe bearbeiten
        //anzeigename leer
        //anzeigename mit umlauten, zahlen, buchstaben
        //external id leer
        //speichern
        //2. gruppe bearbeiten
        //daten mit testdaten überprüfen
        //daten ändern
        //speichern
        //abbrechen
        //zurück
        //abbrechen
        //zurück
        //ja
        //2. gruppe bearbeiten
        //daten mit testdaten überprüfen
        //
        //2. gruppe löschen
        //abbrechen
        //überprüfen ob noch da ist
        //2. gruppe löschen
        //ja
        //überprüfen ob weg
        //fertig
        
        browser.click(EditList.GROUP_LIST);
        
        @Test
        public void apply_empty_filter() {
            browser.click(EditList.FILTER_BUTTON);

            assertTrue(isUserVisible(ADMIN_USERNAME));
        }

        @Test
        public void apply_single_filter() {
            browser.fill(new Field(UserList.FILTER_LOGIN, ADMIN_USERNAME));
            browser.click(EditList.FILTER_BUTTON);

            assertTrue(isUserVisible(ADMIN_USERNAME));
            assertFalse(isUserVisible("adavies")); // an another user
        }
        
        @Test
        public void apply_no_result_filter() {
            browser.fill(
                    new Field(UserList.FILTER_LOGIN, "DoesNotExist"));
            browser.click(EditList.FILTER_BUTTON);

            assertEquals(0, getDisplayedUser());
        }

        @Test
        public void order() {
            browser.click(UserList.SORT_LOGIN_ASC);
            assertTrue(isUserAtPosition("adavies", 0));

            browser.click(UserList.SORT_GIVEN_NAME_ASC);
            assertTrue(isUserAtPosition("adavies", 0)); // Adeline

            browser.click(UserList.SORT_FAMILY_NAME_ASC);
            assertTrue(isUserAtPosition("gparker", 0)); // Barker

            browser.click(UserList.SORT_LOGIN_DESC);
            assertTrue(isUserAtPosition("marissa", 0));

            browser.click(UserList.SORT_GIVEN_NAME_DESC);
            assertTrue(isUserAtPosition("ewilley", 0)); // Willey

            browser.click(UserList.SORT_FAMILY_NAME_DESC);
            assertTrue(isUserAtPosition("jcambell", 0)); // Thompson
        }
        
        @Test
        public void limit() {
            // default limit: 20
            assertEquals("20", browser.findSelectedOption(EditList.LIMIT).getText());

            // change limit seticfied
            assertTrue("Precondition is not satisfied! For this testcase the dataset must be contains more than 5 users!",
                    getDisplayedUser() > 5);

            browser.fill(new Field(EditList.LIMIT, "5"));
            assertTrue(getDisplayedUser() <= 5);
            assertEquals("5", browser.findSelectedOption(EditList.LIMIT).getText());
        }
        
        @Test
        public void paging() {
            browser.fill(new Field(EditList.LIMIT, "5")); // set limit to 5
            int userCount = 0;

            while (true) {
                try {
                    userCount += getDisplayedUser();
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

            clickFirstPagingNumber();
            assertFalse(browser.isErrorPage());

            browser.fill(new Field(EditList.LIMIT, "0")); // set limit to "unlimited"
            assertEquals(getDisplayedUser(), userCount);
        }
        
        private void clickFirstPagingNumber() {
            String pagingNumberXpath = "//a[contains(@id, 'paging-') " +
                    "and not(@id = 'paging-first') " +
                    "and not(@id = 'paging-prev') " +
                    "and not(@id = 'paging-next') " +
                    "and not(@id = 'paging-last') " +
                    "and not(@href = '#')]";

            browser.findElements(By.xpath(pagingNumberXpath)).get(0).click();
        }
    }
}
