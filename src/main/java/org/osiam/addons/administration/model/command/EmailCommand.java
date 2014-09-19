package org.osiam.addons.administration.model.command;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.osiam.resources.scim.Email;
import org.osiam.resources.scim.Email.Type;

import com.google.common.base.Strings;

/**
 * Command object for the user update view.
 */
public class EmailCommand implements Emptiable {

	@NotNull
	@NotEmpty
	@NotBlank
	private String type;

	@NotNull
	@org.hibernate.validator.constraints.Email
	private String value;

	private String display;

	@NotNull
	private Boolean primary;

	public EmailCommand() {
	}

	public EmailCommand(Email email) {
		setDisplay(email.getDisplay());
		setPrimary(email.isPrimary());
		setValue(email.getValue());

		if(email.getType() != null) {
			setType(email.getType().getValue());
		}
	}

	@Override
	public boolean isEmpty() {
		return getPrimary() == null &&
				Strings.isNullOrEmpty(getDisplay()) &&
				Strings.isNullOrEmpty(getType()) &&
				Strings.isNullOrEmpty(getValue());
	}

	public Email getAsEmail(){
		return new Email.Builder()
			.setDisplay(getDisplay())
			.setPrimary(getPrimary())
			.setType(new Type(getType()))
			.setValue(getValue())
			.build();
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public Boolean getPrimary() {
		return primary;
	}
	public void setPrimary(Boolean primary) {
		this.primary = primary;
	}
}
