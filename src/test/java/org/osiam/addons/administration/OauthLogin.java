package org.osiam.addons.administration;

import org.openqa.selenium.By;

public enum OauthLogin implements Element {
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