package org.osiam.addons.administration;

import org.openqa.selenium.By;

/**
 * This contains all elements that we can access via selenium.
 */
public interface Element {
    By by();
    String name();
    
    public static enum OauthLogin implements Element {
        USERNAME(By.id("username")),
        PASSWORD(By.id("password")),
        LOGIN_BUTTON(By.xpath("//button[@type = 'submit']")),
        AUTHORIZE_BUTTON(By.xpath("//form[@id = 'confirmation-form']/button"));

        private By by;
        
        private OauthLogin(By by) {
            this.by = by;
        }
        
        @Override
        public By by() {
            return this.by;
        }
    }
    
    public static enum UserList implements Element {
        FILTER_BUTTON(By.id("filter-button")),
        FILTER_LOGIN(By.id("filter-login")),
        FILTER_GIVEN_NAME(By.id("filter-givenname")),
        FILTER_FAMILY_NAME(By.id("filter-familyname")), 
        
        SORT_LOGIN_ASC(By.id("order-by-login-asc")),
        SORT_LOGIN_DESC(By.id("order-by-login-desc")),
        SORT_GIVEN_NAME_ASC(By.id("order-by-givenname-asc")),
        SORT_GIVEN_NAME_DESC(By.id("order-by-givenname-desc")),
        SORT_FAMILY_NAME_ASC(By.id("order-by-familyname-asc")),
        SORT_FAMILY_NAME_DESC(By.id("order-by-familyname-desc")), 

        LIMIT(By.id("paging-limit")),

        PAGING_NEXT(By.xpath("//a[@id = 'paging-next' and not(@href = '#')]")),
        PAGING_PREVIOUS(By.xpath("//a[@id = 'paging-prev' and not(@href = '#')]")),
        PAGING_LAST(By.xpath("//a[@id = 'paging-last' and not(@href = '#')]")),
        PAGING_FIRST(By.xpath("//a[@id = 'paging-first' and not(@href = '#')]"));

        private By by;
        
        private UserList(By by) {
            this.by = by;
        }
        
        @Override
        public By by() {
            return this.by;
        }
    }

    public static enum UserEdit implements Element {
        FIRSTNAME(By.id("firstname")),
        LASTNAME(By.id("lastname")),
        MAIL(By.id("email")),
        SUBMIT_BUTTON(By.xpath("//button[@type = 'submit']")),
        CANCEL_BUTTON(By.xpath("//button[@type = 'button']"));
        
        private By by;
        
        private UserEdit(By by) {
            this.by = by;
        }
        
        @Override
        public By by() {
            return this.by;
        }
    }
}