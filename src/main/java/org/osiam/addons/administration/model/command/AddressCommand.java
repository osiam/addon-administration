package org.osiam.addons.administration.model.command;

import org.osiam.resources.scim.Address;
import org.osiam.resources.scim.Address.Type;

import com.google.common.base.Strings;

public class AddressCommand implements Emptiable {

    private Boolean primary;

    private String country;

    private String formatted;

    private String locality;

    private String postalcode;

    private String region;

    private String streetaddress;

    private String type;

    public AddressCommand() {
    }

    public AddressCommand(Address address) {
        setPrimary(address.isPrimary());
        setCountry(address.getCountry());
        setFormatted(address.getFormatted());
        setLocality(address.getLocality());
        setPostalcode(address.getPostalCode());
        setRegion(address.getRegion());
        setStreetaddress(address.getStreetAddress());

        if (address.getType() != null) {
            setType(address.getType().getValue());
        }
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFormatted() {
        return formatted;
    }

    public void setFormatted(String formatted) {
        this.formatted = formatted;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStreetaddress() {
        return streetaddress;
    }

    public void setStreetaddress(String streetaddress) {
        this.streetaddress = streetaddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Address getAsAddress() {
        return new Address.Builder()
                .setPrimary(getPrimary())
                .setCountry(getCountry())
                .setFormatted(getFormatted())
                .setLocality(getLocality())
                .setPostalCode(getPostalcode())
                .setRegion(getRegion())
                .setStreetAddress(getStreetaddress())
                .setType(new Type(getType()))
                .build();
    }

    @Override
    public boolean isEmpty() {
        return getPrimary() == null &&
                Strings.isNullOrEmpty(getCountry()) &&
                Strings.isNullOrEmpty(getFormatted()) &&
                Strings.isNullOrEmpty(getLocality()) &&
                Strings.isNullOrEmpty(getPostalcode()) &&
                Strings.isNullOrEmpty(getRegion()) &&
                Strings.isNullOrEmpty(getStreetaddress()) &&
                Strings.isNullOrEmpty(getType());
    }
}
