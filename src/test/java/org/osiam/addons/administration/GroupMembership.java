package org.osiam.addons.administration;

import org.openqa.selenium.By;

public enum GroupMembership implements Element {
	MEMBERSHIP_GROUP(By.id("action-button-edit-user-membership-1")),
	ADD_USER(By.id("move-to-member")),
	REMOVE_USER(By.id("remove-from-member")),

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
