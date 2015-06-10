package org.osiam.addons.administration.model.command;

import java.util.*;
import java.util.Map.Entry;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.osiam.addons.administration.model.validation.ExtensionValidator;
import org.osiam.resources.scim.*;
import org.osiam.resources.scim.Extension.Field;
import org.springframework.validation.BindingResult;

/**
 * Command object for the user update view.
 */
public class UpdateUserCommand {
    private User user;

    private String id;
    private Boolean active;
    private String externalId;
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

    private SortedMap<String, SortedMap<String, String>> extensions = new TreeMap<String, SortedMap<String, String>>();

    /**
     * Creates a new UpdateUserCommand based on the given {@link User}.
     *
     * @param user
     *        the user
     */
    public UpdateUserCommand(User user, Collection<Extension> allExtensions) {
        this.user = user;
        id = user.getId();

        active = user.isActive();
        externalId = user.getExternalId();
        title = user.getTitle();
        displayName = user.getDisplayName();
        nickName = user.getNickName();
        preferredLanguage = user.getPreferredLanguage();
        locale = user.getLocale();
        profileURL = user.getProfileUrl();
        timezone = user.getTimezone();
        userName = user.getUserName();

        if (user.getName() != null) {
            name = new NameCommand(user.getName());
        }

        if (user.getMeta() != null) {
            meta = new MetaCommand(user.getMeta());
        }

        if (user.getEmails() != null) {
            for (Email email : user.getEmails()) {
                emails.add(new EmailCommand(email));
            }
        }
        if (user.getPhoneNumbers() != null) {
            for (PhoneNumber number : user.getPhoneNumbers()) {
                phoneNumbers.add(new PhoneNumberCommand(number));
            }
        }
        if (user.getIms() != null) {
            for (Im im : user.getIms()) {
                ims.add(new ImCommand(im));
            }
        }
        if (user.getX509Certificates() != null) {
            for (X509Certificate certificate : user.getX509Certificates()) {
                certificates.add(new X509CertificateCommand(certificate));
            }
        }
        if (user.getAddresses() != null) {
            for (Address address : user.getAddresses()) {
                addresses.add(new AddressCommand(address));
            }
        }
        if (user.getEntitlements() != null) {
            for (Entitlement entitlement : user.getEntitlements()) {
                entitlements.add(new EntitlementCommand(entitlement));
            }
        }

        enrichExtensions(allExtensions);
        if (user.getExtensions() != null) {
            for (Extension extension : user.getExtensions().values()) {
                for (Entry<String, Field> field : extension.getFields().entrySet()) {
                    extensions.get(extension.getUrn()).put(field.getKey(), field.getValue().getValue());
                }
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

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
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
     * Sets the user.
     *
     * @param user
     *        the {@link User} to set.
     */
    public void setUser(User user) {
        this.user = user;
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

    public List<ImCommand> getIms() {
        return ims;
    }

    public void setIms(List<ImCommand> ims) {
        this.ims = ims;
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

    public SortedMap<String, SortedMap<String, String>> getExtensions() {
        return extensions;
    }

    public void setExtensions(SortedMap<String, SortedMap<String, String>> extensions) {
        this.extensions = extensions;
    }

    public void enrichExtensions(Collection<Extension> allExtensions) {
        for (Extension extension : allExtensions) {

            if (!extensions.containsKey(extension.getUrn())) {
                extensions.put(extension.getUrn(), new TreeMap<String, String>());
            }

            for (Entry<String, Field> field : extension.getFields().entrySet()) {
                SortedMap<String, String> localExtension = extensions.get(extension.getUrn());

                if (!localExtension.containsKey(field.getKey())) {
                    localExtension.put(field.getKey(), "");
                }
            }
        }
    }

    /**
     * Returns a SCIM {@link UpdateUser} based on this command.
     *
     * @return the requested {@link UpdateUser}
     */
    public UpdateUser getAsUpdateUser() {
        UpdateUser.Builder builder = new UpdateUser.Builder();

        if (active != null) {
            builder.updateActive(active);
        }
        builder.updateExternalId(externalId);
        builder.updateName(name.getAsName());
        builder.updateTitle(title);
        builder.updateDisplayName(displayName);
        builder.updateNickName(nickName);
        builder.updatePreferredLanguage(preferredLanguage);
        builder.updateLocale(locale);
        builder.updateProfileUrl(profileURL);
        builder.updateTimezone(timezone);
        builder.updateUserName(userName);

        return builder.build();
    }

    public User getAsUser() {
        User.Builder builder = new User.Builder(getUserName());

        builder.setActive(active);
        builder.setExternalId(externalId);
        builder.setName(name.getAsName());
        builder.setTitle(title);
        builder.setDisplayName(displayName);
        builder.setActive(active);
        builder.setNickName(nickName);
        builder.setPreferredLanguage(preferredLanguage);
        builder.setLocale(locale);
        builder.setProfileUrl(profileURL);
        builder.setTimezone(timezone);

        for (EmailCommand email : emails) {
            if (!email.isEmpty()) {
                builder.addEmail(email.getAsEmail());
            }
        }
        for (PhoneNumberCommand number : phoneNumbers) {
            if (!number.isEmpty()) {
                builder.addPhoneNumber(number.getAsPhoneNumber());
            }
        }
        for (ImCommand im : ims) {
            if (!im.isEmpty()) {
                builder.addIm(im.getAsIm());
            }
        }
        for (X509CertificateCommand certificate : certificates) {
            if (!certificate.isEmpty()) {
                builder.addX509Certificate(certificate.getAsCertificate());
            }
        }
        for (AddressCommand address : addresses) {
            if (!address.isEmpty()) {
                builder.addAddress(address.getAsAddress());
            }
        }
        for (EntitlementCommand entitlement : entitlements) {
            if (!entitlement.isEmpty()) {
                builder.addEntitlement(entitlement.getAsEntitlement());
            }
        }
        for (Entry<String, SortedMap<String, String>> extension : extensions.entrySet()) {
            final String urn = extension.getKey();
            Extension.Builder extensionBuilder = new Extension.Builder(urn);

            for (Entry<String, String> field : extension.getValue().entrySet()) {
                extensionBuilder.setField(field.getKey(), field.getValue());
            }

            builder.addExtension(extensionBuilder.build());
        }

        return builder.build();
    }

    public void purge() {
        removeEmptyElements(emails.iterator());
        removeEmptyElements(phoneNumbers.iterator());
        removeEmptyElements(ims.iterator());
        removeEmptyElements(certificates.iterator());
        removeEmptyElements(addresses.iterator());
        removeEmptyElements(entitlements.iterator());
        removeEmptyExtensions();
    }

    private void removeEmptyElements(Iterator<? extends Emptiable> elements) {
        while (elements.hasNext()) {
            if (elements.next().isEmpty()) {
                elements.remove();
            }
        }
    }

    private void removeEmptyExtensions() {
        // remove empty fields
        for (Map<String, String> extension : extensions.values()) {
            Iterator<Entry<String, String>> iter = extension.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, String> field = iter.next();

                if (field.getValue() == null || field.getValue().equals("")) {
                    iter.remove();
                }
            }
        }

        // remove empty extensions
        Iterator<Entry<String, SortedMap<String, String>>> iter = extensions.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, SortedMap<String, String>> extension = iter.next();
            if (extension.getValue().isEmpty()) {
                iter.remove();
            }
        }
    }

    public void validate(Map<String, Extension> allExtensions, BindingResult bindingResult) {
        validateExtensions(allExtensions, bindingResult);
    }

    private void validateExtensions(Map<String, Extension> allExtensions, BindingResult bindingResult) {
        ExtensionValidator validator = new ExtensionValidator("extensions", allExtensions, bindingResult);

        for (Entry<String, SortedMap<String, String>> extension : extensions.entrySet()) {
            final String urn = extension.getKey();
            for (Entry<String, String> field : extension.getValue().entrySet()) {
                final String key = field.getKey();
                final String value = field.getValue();

                validator.validate(urn, key, value);
            }
        }
    }
}
