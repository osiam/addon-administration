package org.osiam.addons.administration.model.command;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.osiam.resources.scim.X509Certificate;
import org.osiam.resources.scim.X509Certificate.Type;

import com.google.common.base.Strings;

public class X509CertificateCommand implements Emptiable {
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

    public X509CertificateCommand() {
    }

    public X509CertificateCommand(X509Certificate x509Certificate) {
        setDisplay(x509Certificate.getDisplay());
        setPrimary(x509Certificate.isPrimary());
        setValue(x509Certificate.getValue());

        if (x509Certificate.getType() != null) {
            setType(x509Certificate.getType().getValue());
        }
    }

    public X509Certificate getAsCertificate() {
        return new X509Certificate.Builder()
                .setDisplay(getDisplay())
                .setPrimary(getPrimary())
                .setValue(getValue())
                .setType(new Type(getType()))
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

    @Override
    public boolean isEmpty() {
        return getPrimary() == null &&
                Strings.isNullOrEmpty(getDisplay()) &&
                Strings.isNullOrEmpty(getType()) &&
                Strings.isNullOrEmpty(getValue());
    }
}
