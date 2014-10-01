package org.osiam.addons.administration.elements;

import org.openqa.selenium.By;

public enum General implements Element {
	LOGOUT_BUTTON(By.id("logout-button"));

	private By by;

	private General(By by) {
		this.by = by;
	}

	@Override
	public By by() {
		return this.by;
	}
}