package org.osiam.addons.administration.elements.group;

import org.openqa.selenium.By;
import org.osiam.addons.administration.elements.Element;

public enum GroupMembership implements Element {
	MEMBERSHIP_GROUP(By.id("action-button-edit-user-membership-1")),
	ADD_USER(By.id("move-to-member")),
	REMOVE_USER(By.id("remove-from-member")),

	ASSIGNED_MASTER_CHECKBOX(By.id("assigned-master-checkbox")),
	SUBMIT_BUTTON_ASSIGNED_SELECTION(By.id("submit-button-assigned-selection")),

	UNASSIGNED_MASTER_CHECKBOX(By.id("unassigned-master-checkbox")),
	SUBMIT_BUTTON_UNASSIGNED_SELECTION(By.id("submit-button-unassigned-selection")),

	LIST_ROWS_INSIDER(By.xpath("//table[contains(@id, 'insider')]//div[contains(@id, 'content')]/table/tbody/tr")),
	LIST_ROWS_OUTSIDER(By.xpath("//table[contains(@id, 'outsider')]//div[contains(@id, 'content')]/table/tbody/tr")),

	SORT_ASSIGNED_LOGIN_ASC(By.xpath("//table[contains(@id, 'insider')]//a[contains(@id, 'order-by-assigned-login-asc')]")),
	SORT_ASSIGNED_LOGIN_DESC(By.xpath("//table[contains(@id, 'insider')]//a[contains(@id, 'order-by-assigned-login-desc')]")),
	SORT_ASSIGNED_GIVEN_NAME_ASC(By.xpath("//table[contains(@id, 'insider')]//a[contains(@id, 'order-by-assigned-givenname-asc')]")),
	SORT_ASSIGNED_GIVEN_NAME_DESC(By.xpath("//table[contains(@id, 'insider')]//a[contains(@id, 'order-by-assigned-givenname-desc')]")),
	SORT_ASSIGNED_FAMILY_NAME_ASC(By.xpath("//table[contains(@id, 'insider')]//a[contains(@id, 'order-by-assigned-familyname-asc')]")),
	SORT_ASSIGNED_FAMILY_NAME_DESC(By.xpath("//table[contains(@id, 'insider')]//a[contains(@id, 'order-by-assigned-familyname-desc')]")),

	SORT_UNASSIGNED_LOGIN_ASC(By.xpath("//table[contains(@id, 'outsider')]//a[contains(@id, 'order-by-unassigned-login-asc')]")),
	SORT_UNASSIGNED_LOGIN_DESC(By.xpath("//table[contains(@id, 'outsider')]//a[contains(@id, 'order-by-unassigned-login-desc')]")),
	SORT_UNASSIGNED_GIVEN_NAME_ASC(By.xpath("//table[contains(@id, 'outsider')]//a[contains(@id, 'order-by-unassigned-givenname-asc')]")),
	SORT_UNASSIGNED_GIVEN_NAME_DESC(By.xpath("//table[contains(@id, 'outsider')]//a[contains(@id, 'order-by-unassigned-givenname-desc')]")),
	SORT_UNASSIGNED_FAMILY_NAME_ASC(By.xpath("//table[contains(@id, 'outsider')]//a[contains(@id, 'order-by-unassigned-familyname-asc')]")),
	SORT_UNASSIGNED_FAMILY_NAME_DESC(By.xpath("//table[contains(@id, 'outsider')]//a[contains(@id, 'order-by-unassigned-familyname-desc')]")),

	FILTER_ASSIGNED_LOGIN(By.xpath("//table[contains(@id, 'insider')]//input[contains(@id, 'filter-login')]")),
	FILTER_ASSIGNED_GIVEN_NAME(By.xpath("//table[contains(@id, 'insider')]//input[contains(@id, 'filter-givenname')]")),
	FILTER_ASSIGNED_FAMILY_NAME(By.xpath("//table[contains(@id, 'insider')]//input[contains(@id, 'filter-familyname')]")),

	FILTER_UNASSIGNED_LOGIN(By.xpath("//table[contains(@id, 'outsider')]//input[contains(@id, 'filter-login')]")),
	FILTER_UNASSIGNED_GIVEN_NAME(By.xpath("//table[contains(@id, 'outsider')]//input[contains(@id, 'filter-givenname')]")),
	FILTER_UNASSIGNED_FAMILY_NAME(By.xpath("//table[contains(@id, 'outsider')]//input[contains(@id, 'filter-familyname')]")),

	FILTER_BUTTON_ASSIGNED(By.id("assigned-filter-button")),
	FILTER_BUTTON_UNASSIGNED(By.id("unassigned-filter-button")),

	LIMIT_ASSIGNED(By.id("paging-limit-intern")),
	LIMIT_UNASSIGNED(By.id("paging-limit-extern")),

	PAGING_ASSIGNED_NEXT(By.xpath("//div[contains(@id, 'form-group-insider')]//a[@id = 'paging-next' and not(@href = '#')]")),
	PAGING_ASSIGNED_PREVIOUS(By.xpath("//div[contains(@id, 'form-group-insider')]//a[@id = 'paging-prev' and not(@href = '#')]")),
	PAGING_ASSIGNED_LAST(By.xpath("//div[contains(@id, 'form-group-insider')]//a[@id = 'paging-last' and not(@href = '#')]")),
	PAGING_ASSIGNED_FIRST(By.xpath("//div[contains(@id, 'form-group-insider')]//a[@id = 'paging-first' and not(@href = '#')]")),

	PAGING_UNASSIGNED_NEXT(By.xpath("//div[contains(@id, 'form-group-outsider')]//a[@id = 'paging-next' and not(@href = '#')]")),
	PAGING_UNASSIGNED_PREVIOUS(By.xpath("//div[contains(@id, 'form-group-outsider')]//a[@id = 'paging-prev' and not(@href = '#')]")),
	PAGING_UNASSIGNED_LAST(By.xpath("//div[contains(@id, 'form-group-outsider')]//a[@id = 'paging-last' and not(@href = '#')]")),
	PAGING_UNASSIGNED_FIRST(By.xpath("//div[contains(@id, 'form-group-outsider')]//a[@id = 'paging-first' and not(@href = '#')]")),

	//adavies
	UNASSIGNED_USER(By.xpath("//select[contains(@id, 'outsider')]/..//option[contains(@value, '03dc8f50-acaa-44d6-9401-bdfc5e10e821')]")),
	ASSIGNED_USER(By.xpath("//select[contains(@id, 'insider')]/..//option[contains(@value, '03dc8f50-acaa-44d6-9401-bdfc5e10e821')]"));

	private By by;

	private GroupMembership(By by) {
		this.by = by;
	}

	@Override
	public By by() {
		return this.by;
	}
}
