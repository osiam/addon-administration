package org.osiam.addons.administration.model.command;

import org.osiam.resources.scim.Email;
import org.osiam.resources.scim.User;

/**
 * Command object for the user update view.
 * 
 * @author Timo Kanera, tarent solutions GmbH
 */
public class UpdateUserCommand {

    private String firstName;
    
    private String lastName;
    
    private String eMail;
    
    /**
     * Creates a new UpdateUserCommand based on the given {@link User}.
     * 
     * @param user the user
     */
    public UpdateUserCommand(User user) {
        if (user.getName() != null) {
            setFirstName(user.getName().getGivenName());
            setLastName(user.getName().getFamilyName());
        }
        seteMail(getPrimaryEMail(user));
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
     * @param firstName the first name to set
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
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the e-mail address.
     * 
     * @return the the e-mail address
     */
    public String geteMail() {
        return eMail;
    }

    /**
     * Sets the e-mail address.
     * 
     * @param eMail the e-mail address to set
     */
    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
    
    private String getPrimaryEMail(User user) {
        if (user.getEmails() != null && !user.getEmails().isEmpty()) {
            for (Email eMail : user.getEmails()) {
                if (eMail.isPrimary()) {
                    return eMail.getValue();
                }
            }
        }
        return null;
    }
}
