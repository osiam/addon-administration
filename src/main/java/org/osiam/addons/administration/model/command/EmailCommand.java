package org.osiam.addons.administration.model.command;

import javax.validation.constraints.NotNull;

import org.osiam.resources.scim.Email;

import com.google.common.base.Strings;

/**
 * Command object for the user update view.
 */
public class EmailCommand implements Emptiable {

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
        display = email.getDisplay();
        primary = email.isPrimary();
        value = email.getValue();

        if (email.getType() != null) {
            type = email.getType().getValue();
        }
    }

    @Override
    public boolean isEmpty() {
        return getPrimary() == null &&
                Strings.isNullOrEmpty(display) &&
                Strings.isNullOrEmpty(type) &&
                Strings.isNullOrEmpty(value);
    }

    public Email getAsEmail() {
        Email.Builder email = new Email.Builder()
                .setDisplay(display)
                .setPrimary(primary)
                .setValue(value);

        if (!Strings.isNullOrEmpty(type)) {
            email.setType(new Email.Type(type));
        }

        return email.build();
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
