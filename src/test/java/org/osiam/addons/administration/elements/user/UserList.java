package org.osiam.addons.administration.elements.user;

import org.openqa.selenium.By;
import org.osiam.addons.administration.elements.Element;

public enum UserList implements Element {
	FILTER_LOGIN(By.id("filter-login")),
	FILTER_GIVEN_NAME(By.id("filter-givenname")),
	FILTER_FAMILY_NAME(By.id("filter-familyname")),
	FILTER_GROUP_NAME(By.id("filter-group")),

	SORT_LOGIN_ASC(By.id("order-by-login-asc")),
	SORT_LOGIN_DESC(By.id("order-by-login-desc")),
	SORT_GIVEN_NAME_ASC(By.id("order-by-givenname-asc")),
	SORT_GIVEN_NAME_DESC(By.id("order-by-givenname-desc")),
	SORT_FAMILY_NAME_ASC(By.id("order-by-familyname-asc")),
	SORT_FAMILY_NAME_DESC(By.id("order-by-familyname-desc"));

	private By by;

	private UserList(By by) {
		this.by = by;
	}

	@Override
	public By by() {
		return this.by;
	}
}