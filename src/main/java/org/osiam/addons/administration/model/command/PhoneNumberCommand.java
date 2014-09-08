package org.osiam.addons.administration.model.command;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.osiam.resources.scim.PhoneNumber;
import org.osiam.resources.scim.PhoneNumber.Type;

/**
 * Command object for the user update view.
 */
public class PhoneNumberCommand implements Emptiable {

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

    public PhoneNumberCommand() {
    }

    public PhoneNumberCommand(PhoneNumber phoneNumber) {
        setDisplay(phoneNumber.getDisplay());
        setPrimary(phoneNumber.isPrimary());
        setValue(phoneNumber.getValue());

        if (phoneNumber.getType() != null) {
            setType(phoneNumber.getType().getValue());
        }
    }

    @Override
    public boolean isEmpty(){
        return (getDisplay() == null || getDisplay().isEmpty()) &&
                getPrimary() == null &&
                getType() == null &&
                getValue() == null;
    }

    public PhoneNumber getAsPhoneNumber() {
        return new PhoneNumber.Builder()
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
