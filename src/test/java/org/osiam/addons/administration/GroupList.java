package org.osiam.addons.administration;

import org.openqa.selenium.By;

public enum GroupList implements Element {
	GROUP_LIST(By.xpath("//a[@href = '/admin/group/list']")),
	ADD_GROUP(By.xpath("//a[@href = 'create']")),
	FILTER_GROUP(By.id("filter-display-name")),
	SORT_GROUP_ASC(By.id("order-by-displayname-asc")),
	SORT_GROUP_DESC(By.id("order-by-displayname-desc")),
	DELETE_GROUP(By.id("action-button-delete-group-0"));

	private By by;

	private GroupList(By by) {
		this.by = by;
	}

	@Override
	public By by() {
		return this.by;
	}

}