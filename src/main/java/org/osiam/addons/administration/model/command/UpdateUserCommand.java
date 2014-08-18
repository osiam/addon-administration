package org.osiam.addons.administration.model.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.osiam.resources.scim.Address;
import org.osiam.resources.scim.Email;
import org.osiam.resources.scim.Entitlement;
import org.osiam.resources.scim.Im;
import org.osiam.resources.scim.PhoneNumber;
import org.osiam.resources.scim.UpdateUser;
import org.osiam.resources.scim.User;
import org.osiam.resources.scim.X509Certificate;

/**
 * Command object for the user update view.
 */
public class UpdateUserCommand {
    private User user;

    private String id;
    private Boolean active;
    @NotNull
    private String title;
    @NotNull
    private String displayName;
    @NotNull
    private String nickName;
    @NotNull
    @Pattern(regexp = "^$|^[a-zA-Z]{2}$")
    private String preferredLanguage;
    @NotNull
    @Pattern(regexp = "^$|^[a-z]{2}_[A-Z]{2}$")
    private String locale;
    @NotNull
    @URL
    private String profileURL;
    @NotNull
    @Pattern(regexp = "^$|.*\\/.*")
    private String timezone;
    @NotNull
    @NotBlank
    private String userName;
    @Valid
    private NameCommand name = new NameCommand();
    @Valid
    private MetaCommand meta = new MetaCommand();

    @Valid
    private List<EmailCommand> emails = new ArrayList<EmailCommand>();
    @Valid
    private List<PhoneNumberCommand> phoneNumbers = new ArrayList<PhoneNumberCommand>();
    @Valid
    private List<ImCommand> ims = new ArrayList<ImCommand>();
    @Valid
    private List<X509CertificateCommand> certificates = new ArrayList<X509CertificateCommand>();
    @Valid
    private List<AddressCommand> addresses = new ArrayList<AddressCommand>();
    @Valid
    private List<EntitlementCommand> entitlements = new ArrayList<EntitlementCommand>();

    private Map<String, Map<String, String>> extensions;

    /**
     * Creates a new UpdateUserCommand based on the given {@link User}.
     *
     * @param user
     *        the user
     */
    public UpdateUserCommand(User user) {
        this.user = user;
        setId(user.getId());

        setActive(user.isActive());
        setTitle(user.getTitle());
        setDisplayName(user.getDisplayName());
        setNickName(user.getNickName());
        setPreferredLanguage(user.getPreferredLanguage());
        setLocale(user.getLocale());
        setProfileURL(user.getProfileUrl());
        setTimezone(user.getTimezone());
        setUserName(user.getUserName());

        if(user.getName() != null)
            setName(new NameCommand(user.getName()));

        if(user.getMeta() != null)
            setMeta(new MetaCommand(user.getMeta()));

        if (user.getName() != null) {
            setName(new NameCommand(user.getName()));
        }

        if (user.getEmails() != null) {
            for (Email email : user.getEmails()) {
                this.emails.add(new EmailCommand(email));
            }
        }
        if (user.getPhoneNumbers() != null) {
            for (PhoneNumber number : user.getPhoneNumbers()) {
                this.phoneNumbers.add(new PhoneNumberCommand(number));
            }
        }
        if (user.getIms() != null) {
            for (Im im : user.getIms()) {
                this.ims.add(new ImCommand(im));
            }
        }
        if (user.getX509Certificates() != null) {
            for (X509Certificate certificate: user.getX509Certificates()) {
                this.certificates.add(new X509CertificateCommand(certificate));
            }
        }
        if (user.getAddresses() != null) {
            for (Address address: user.getAddresses()) {
                this.addresses.add(new AddressCommand(address));
            }
        }
        if(user.getEntitlements() != null) {
            for (Entitlement entitlement : user.getEntitlements()) {
                this.entitlements.add(new EntitlementCommand(entitlement));
            }
        }
    }

    /**
     * Creates a new UpdateUserCommand.
     */
    public UpdateUserCommand() {
    }

    /**
     * Returns the displayname.
     *
     * @return the the displayname
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the displayname.
     *
     * @param displayName
     *        the displayname to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the locale.
     *
     * @return the the locale
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Sets the locale.
     *
     * @param locale
     *        the locale to set
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * Returns the nickname.
     *
     * @return the the nickname
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Sets the nickname.
     *
     * @param nickName
     *        the nickname to set
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * Returns the preferredlanguage.
     *
     * @return the the preferredlanguage
     */
    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    /**
     * Sets the preferredlanguage.
     *
     * @param preferredLanguage
     *        the preferredlanguage to set
     */
    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    /**
     * Returns the profileurl.
     *
     * @return the the profileurl
     */
    public String getProfileURL() {
        return profileURL;
    }

    /**
     * Sets the profileurl.
     *
     * @param profileURL
     *        the profileurl to set
     */
    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    /**
     * Returns the timezone.
     *
     * @return the the timezone
     */
    public String getTimezone() {
        return timezone;
    }

    /**
     * Sets the timezone.
     *
     * @param timezone
     *        the timezone to set
     */
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    /**
     * Returns the title.
     *
     * @return the the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title
     *        the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the username.
     *
     * @return the the username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the username.
     *
     * @param userName
     *        the username to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Returns the active.
     *
     * @return the the active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * Sets the active.
     *
     * @param active
     *        the active to set
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * Returns the user.
     *
     * @return the the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns the user ID.
     *
     * @return the user ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the user ID.
     *
     * @param id
     *        the user ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the user.
     *
     * @param user
     *        the {@link User} to set.
     */
    public void setUser(User user) {
        this.user = user;
    }

    public List<EmailCommand> getEmails() {
        return emails;
    }

    public void setEmails(List<EmailCommand> emails) {
        this.emails = emails;
    }

    public List<PhonenumberCommand> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhonenumberCommand> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public List<EmailCommand> getEmails() {
        return emails;
    }

    public void setEmails(List<EmailCommand> emails) {
        this.emails = emails;
    }

    public List<PhoneNumberCommand> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumberCommand> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public void setIms(List<ImCommand> ims) {
        this.ims = ims;
    }

    public List<ImCommand> getIms() {
        return ims;
    }

    public List<X509CertificateCommand> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<X509CertificateCommand> certificates) {
        this.certificates = certificates;
    }

    public List<AddressCommand> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressCommand> addresses) {
        this.addresses = addresses;
    }

    public List<EntitlementCommand> getEntitlements() {
        return entitlements;
    }

    public void setEntitlements(List<EntitlementCommand> entitlements) {
        this.entitlements = entitlements;
    }

    /**
     * Returns the name object.
     *
     * @return the name object
     */

    public NameCommand getName() {
        return name;
    }

    /**
     * Sets the name object.
     *
     * @param name
     *        the name object to set
     */
    public void setName(NameCommand name) {
        this.name = name;
    }

    public MetaCommand getMeta() {
        return meta;
    }

    public void setMeta(MetaCommand meta) {
        this.meta = meta;
    }

    public Map<String, Map<String, String>> getExtensions() {
        return extensions;
    }

    public void setExtensions(Map<String, Map<String, String>> extensions) {
        this.extensions = extensions;
    }

    /**
     * Returns a SCIM {@link UpdateUser} based on this command.
     *
     * @return the requested {@link UpdateUser}
     */
    public UpdateUser getAsUpdateUser() {
        UpdateUser.Builder builder = new UpdateUser.Builder();

        if(getActive() != null) {
            builder.updateActive(getActive());
        }
        builder.updateName(getName().getAsName());
        builder.updateTitle(getTitle());
        builder.updateDisplayName(getDisplayName());
        builder.updateNickName(getNickName());
        builder.updatePreferredLanguage(getPreferredLanguage());
        builder.updateLocale(getLocale());
        builder.updateProfileUrl(getProfileURL());
        builder.updateTimezone(getTimezone());
        builder.updateUserName(getUserName());

        return builder.build();
    }

    public User getAsUser(){
        User.Builder builder = new User.Builder(getUserName());

        builder.setActive(getActive());
        builder.setName(getName().getAsName());
        builder.setTitle(getTitle());
        builder.setDisplayName(getDisplayName());
        builder.setActive(getActive());
        builder.setNickName(getNickName());
        builder.setPreferredLanguage(getPreferredLanguage());
        builder.setLocale(getLocale());
        builder.setProfileUrl(getProfileURL());
        builder.setTimezone(getTimezone());

        for (EmailCommand email : getEmails()) {
            if (!email.isEmpty()) {
                builder.addEmail(email.getAsEmail());
            }
        }
        for (PhoneNumberCommand number : getPhoneNumbers()) {
            if (!number.isEmpty()) {
                builder.addPhoneNumber(number.getAsPhoneNumber());
            }
        }
        for (ImCommand im : getIms()) {
            if (!im.isEmpty()) {
                builder.addIm(im.getAsIm());
            }
        }
        for (X509CertificateCommand certificates : getCertificates()) {
            if (!certificates.isEmpty()) {
                builder.addX509Certificate(certificates.getAsCertificate());
            }
        }
        for (AddressCommand address : getAddresses()) {
            if (!address.isEmpty()) {
                builder.addAddress(address.getAsAddress());
            }
        }
        for (EntitlementCommand entitlement : getEntitlements()) {
            if (!entitlement.isEmpty()) {
                builder.addEntitlement(entitlement.getAsEntitlement());
            }
        }

        return builder.build();
    }

    public void purge() {
        removeEmptyElements(getEmails().iterator());
        removeEmptyElements(getPhoneNumbers().iterator());
        removeEmptyElements(getIms().iterator());
        removeEmptyElements(getCertificates().iterator());
        removeEmptyElements(getAddresses().iterator());
        removeEmptyElements(getEntitlements().iterator());
    }

    private void removeEmptyElements(Iterator<? extends Emptiable> elements) {
        while(elements.hasNext()){
            if(elements.next().isEmpty()){
                elements.remove();
            }
        }
    }
}
