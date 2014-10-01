package org.osiam.addons.administration.elements.group;

import org.openqa.selenium.By;
import org.osiam.addons.administration.elements.Element;

public enum GroupEdit implements Element {
	EDIT_GROUP(By.id("action-button-edit-0")),
	MEMBERSHIP_GROUP(By.id("action-button-member-0")),
	EXTERNAL_ID_GROUP(By.id("externalId")),
	ADD_USER_GROUP(By.id("move-to-member")),
	REMOVE_USER_GROUP(By.id("remove-from-member")),

	//adavies
	EXTERN_USER(By.xpath("//select[contains(@id, 'outsider')]/..//option[contains(@value, '03dc8f50-acaa-44d6-9401-bdfc5e10e821')]")),
	GROUP_USER(By.xpath("//select[contains(@id, 'member')]/..//option[contains(@value, '03dc8f50-acaa-44d6-9401-bdfc5e10e821')]"));


	private By by;

	private GroupEdit(By by) {
		this.by = by;
	}

	@Override
	public By by() {
		return this.by;
	}
}