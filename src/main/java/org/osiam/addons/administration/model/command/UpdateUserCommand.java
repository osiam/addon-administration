package org.osiam.addons.administration.model.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.osiam.resources.helper.SCIMHelper;
import org.osiam.resources.scim.Email;
import org.osiam.resources.scim.Name;
import org.osiam.resources.scim.UpdateUser;
import org.osiam.resources.scim.User;

import com.google.common.base.Optional;

/**
 * Command object for the user update view.
 */
public class UpdateUserCommand {

    private User user;

    private String id;
    private Boolean active;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String title;
    @NotNull
    private String displayname;
    @NotNull
    private String nickname;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]{2}$")
    private String preferredlanguage;
    @NotNull
    @Pattern(regexp = ".\\@.*\\..*")
    private String email;
    @NotNull
    @Pattern(regexp = "^[a-z]{2}_[A-Z]{2}&")
    private String locale;
    @NotNull
    @Pattern(regexp = "www\\..*\\..*")
    private String profileurl;
    @NotNull
    @Pattern(regexp = ".*\\/.*")
    private String timezone;
    @NotNull
    @Size(min = 1)
    private String username;

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
        setDisplayname(user.getDisplayName());
        setNickname(user.getNickName());
        setPreferredlanguage(user.getPreferredLanguage());
        setLocale(user.getLocale());
        setProfileurl(user.getProfileUrl());
        setTimezone(user.getTimezone());
        setUsername(user.getUserName());

        if (user.getName() != null) {
            setFirstName(user.getName().getGivenName());
            setLastName(user.getName().getFamilyName());
        }
        Optional<Email> primaryEmail = SCIMHelper.getPrimaryOrFirstEmail(user);
        if (primaryEmail.isPresent()) {
            setEmail(primaryEmail.get().getValue());
        }
    }

    /**
     * Creates a new UpdateUserCommand.
     */
    public UpdateUserCommand() {
    }

    /**
     * Returns the first name.
     * 
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     * 
     * @param firstName
     *        the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name.
     * 
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     * 
     * @param lastName
     *        the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the e-mail address.
     * 
     * @return the the e-mail address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the e-mail address.
     * 
     * @param email
     *        the e-mail address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the displayname.
     * 
     * @return the the displayname
     */
    public String getDisplayname() {
        return displayname;
    }

    /**
     * Sets the displayname.
     * 
     * @param displayname
     *        the displayname to set
     */
    public void setDisplayname(String displayname) {
        this.displayname = displayname;
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
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the nickname.
     * 
     * @param nickname
     *        the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Returns the preferredlanguage.
     * 
     * @return the the preferredlanguage
     */
    public String getPreferredlanguage() {
        return preferredlanguage;
    }

    /**
     * Sets the preferredlanguage.
     * 
     * @param preferredlanguage
     *        the preferredlanguage to set
     */
    public void setPreferredlanguage(String preferredlanguage) {
        this.preferredlanguage = preferredlanguage;
    }

    /**
     * Returns the profileurl.
     * 
     * @return the the profileurl
     */
    public String getProfileurl() {
        return profileurl;
    }

    /**
     * Sets the profileurl.
     * 
     * @param profileurl
     *        the profileurl to set
     */
    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
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
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     * 
     * @param username
     *        the username to set
     */
    public void setUsername(String username) {
        this.username = username;
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

    /**
     * Returns a SCIM {@link UpdateUser} based on this command.
     * 
     * @return the requested {@link UpdateUser}
     */
    public UpdateUser getAsUpdateUser() {
        Name name = new Name.Builder().setGivenName(getFirstName()).setFamilyName(getLastName()).build();
        UpdateUser.Builder builder = new UpdateUser.Builder().updateName(name);

        builder.updateActive(getActive());
        builder.updateTitle(getTitle());
        builder.updateDisplayName(getDisplayname());
        builder.updateNickName(getNickname());
        builder.updatePreferredLanguage(getPreferredlanguage());
        builder.updateLocale(getLocale());
        builder.updateProfileUrl(getProfileurl());
        builder.updateTimezone(getTimezone());
        builder.updateUserName(getUsername());

        Optional<Email> previousEmail = SCIMHelper.getPrimaryOrFirstEmail(user);
        if (previousEmail.isPresent()) {
            builder.updateEmail(previousEmail.get(), new Email.Builder(previousEmail.get()).setValue(getEmail())
                    .build());
        } else {
            builder.addEmail(new Email.Builder().setPrimary(true).setValue(getEmail()).build());
        }

        return builder.build();
    }
}
