package org.osiam.addons.administration.model.command;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.osiam.resources.scim.Im;
import org.osiam.resources.scim.Im.Type;

import com.google.common.base.Strings;

public class ImCommand implements Emptiable {
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

    public ImCommand() {
    }

    public ImCommand(Im im) {
        setDisplay(im.getDisplay());
        setPrimary(im.isPrimary());
        setValue(im.getValue());

        if (im.getType() != null) {
            setType(im.getType().getValue());
        }
    }

    @Override
    public boolean isEmpty(){
        return getPrimary() == null &&
                Strings.isNullOrEmpty(getDisplay()) &&
                Strings.isNullOrEmpty(getType()) &&
                Strings.isNullOrEmpty(getValue());
    }

    public Im getAsIm() {
        return new Im.Builder()
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
