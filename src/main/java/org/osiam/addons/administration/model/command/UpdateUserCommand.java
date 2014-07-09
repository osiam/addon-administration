package org.osiam.addons.administration.model.command;

import org.osiam.resources.helper.SCIMHelper;
import org.osiam.resources.scim.Email;
import org.osiam.resources.scim.Name;
import org.osiam.resources.scim.UpdateUser;
import org.osiam.resources.scim.User;

import com.google.common.base.Optional;

/**
 * Command object for the user update view.
 * 
 * @author Timo Kanera, tarent solutions GmbH
 */
public class UpdateUserCommand {

    private User user;

    private String id;
    private String firstName;
    private String lastName;
    private String email;

    /**
     * Creates a new UpdateUserCommand based on the given {@link User}.
     * 
     * @param user
     *        the user
     */
    public UpdateUserCommand(User user) {
        this.user = user;
        setId(user.getId());
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
