package org.osiam.addons.administration;

import org.openqa.selenium.By;

/**
 * This contains all elements that we can access via selenium.
 */
public interface Element {
    By by();
    String name();
    
    public static enum OauthLogin implements Element {
        Username(By.id("username")),
        Password(By.id("password")),
        LoginButton(By.xpath("//button[@type = 'submit']")),
        AuthorizeButton(By.xpath("//form[@id = 'confirmation-form']/button"));

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
        FilterButton(By.id("filter-button")),
        FilterLogin(By.id("filter-login")),
        FilterGivenName(By.id("filter-givenname")),
        FilterFamilyName(By.id("filter-familyname")), 
        
        SortLoginAsc(By.id("order-by-login-asc")),
        SortLoginDesc(By.id("order-by-login-desc")),
        SortGivenNameAsc(By.id("order-by-givenname-asc")),
        SortGivenNameDesc(By.id("order-by-givenname-desc")),
        SortFamilyNameAsc(By.id("order-by-familyname-asc")),
        SortFamilyNameDesc(By.id("order-by-familyname-desc")), 

        Limit(By.id("paging-limit")),

        PagingNext(By.xpath("//a[@id = 'paging-next' and not(@href = '#')]")),
        PagingPrevious(By.xpath("//a[@id = 'paging-prev' and not(@href = '#')]")),
        PagingLast(By.xpath("//a[@id = 'paging-last' and not(@href = '#')]")),
        PagingFirst(By.xpath("//a[@id = 'paging-first' and not(@href = '#')]"));

        private By by;
        
        private UserList(By by) {
            this.by = by;
        }
        
        @Override
        public By by() {
            return this.by;
        }
        
    }
}
