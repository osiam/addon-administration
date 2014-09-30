package org.osiam.addons.administration;

import org.openqa.selenium.By;

public enum EditList implements Element {
	FILTER_BUTTON(By.id("filter-button")),

	LIMIT(By.id("paging-limit")),

	PAGING_NEXT(By.xpath("//a[@id = 'paging-next' and not(@href = '#')]")),
	PAGING_PREVIOUS(By.xpath("//a[@id = 'paging-prev' and not(@href = '#')]")),
	PAGING_LAST(By.xpath("//a[@id = 'paging-last' and not(@href = '#')]")),
	PAGING_FIRST(By.xpath("//a[@id = 'paging-first' and not(@href = '#')]")),

	DIALOG_SUCCESS(By.xpath("//div[contains(@role, 'dialog')]//button[contains(@data-bb-handler, 'success')]")),
	DIALOG_ABORT(By.xpath("//div[contains(@role, 'dialog')]//button[contains(@data-bb-handler, 'danger')]")),
	DIALOG_CLOSE(By.xpath("//div[contains(@role, 'dialog')]//button[starts-with(@class, 'bootbox-close-button')]")),

	LIST_ROWS(By.xpath("//div[contains(@id, 'content')]/table/tbody/tr")),

	DISPLAYNAME(By.id("displayName")),

	SUBMIT_BUTTON(By.id("btnSaveChanges")),
	CANCEL_BUTTON(By.id("btnCancelChanges"));

	private By by;

	private EditList(By by) {
		this.by = by;
	}

	@Override
	public By by() {
		return this.by;
	}
}