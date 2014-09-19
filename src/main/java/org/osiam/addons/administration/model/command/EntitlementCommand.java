package org.osiam.addons.administration.model.command;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.osiam.resources.scim.Entitlement;
import org.osiam.resources.scim.Entitlement.Type;

import com.google.common.base.Strings;

/**
 * Command object for the user update view.
 */
public class EntitlementCommand implements Emptiable {

	@NotNull
	@NotEmpty
	@NotBlank
	private String type;

	@NotNull
	@NotEmpty
	@NotBlank
	private String value;

	private String display;

	@NotNull
	private Boolean primary;

	public EntitlementCommand() {
	}

	public EntitlementCommand(Entitlement entitlement) {
		setDisplay(entitlement.getDisplay());
		setPrimary(entitlement.isPrimary());
		setValue(entitlement.getValue());

		if (entitlement.getType() != null) {
			setType(entitlement.getType().getValue());
		}
	}

	@Override
	public boolean isEmpty(){
		return getPrimary() == null &&
				Strings.isNullOrEmpty(getDisplay()) &&
				Strings.isNullOrEmpty(getType()) &&
				Strings.isNullOrEmpty(getValue());
	}

	public Entitlement getAsEntitlement() {
		return new Entitlement.Builder()
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
