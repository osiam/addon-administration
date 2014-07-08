package org.osiam.addons.administration.model.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.osiam.resources.scim.Email;
import org.osiam.resources.scim.Name;
import org.osiam.resources.scim.UpdateUser;
import org.osiam.resources.scim.User;

public class UpdateUserCommandTest {

    private User user;
    private Name name;
    private String primaryMail = "primary@osiam.org";
    private String secondaryMail = "secondary@osiam.org";

    @Before
    public void setUp() {
        name = new Name.Builder().setGivenName("Joe").setFamilyName("Random").build();
        user = new User.Builder().setName(name)
                .addEmail(new Email.Builder().setValue(primaryMail).setPrimary(true).build())
                .addEmail(new Email.Builder().setValue(secondaryMail).setPrimary(false).build())
                .build();
    }

    @Test
    public void constructor() {
        UpdateUserCommand command = new UpdateUserCommand(user);
        assertEquals(primaryMail, command.getEmail());
        assertEquals(name.getFamilyName(), command.getLastName());
        assertEquals(name.getGivenName(), command.getFirstName());
    }

    @Test
    public void asUpdateUser() {
        UpdateUserCommand command = new UpdateUserCommand(user);

        String newEmail = "primary_new@osiam.org";
        String newFirstName = "Marissa";
        String newLastName = "Müller";
        command.setEmail(newEmail);
        command.setFirstName(newFirstName);
        command.setLastName(newLastName);

        UpdateUser updateUser = command.getAsUpdateUser();
        User resultingUser = updateUser.getScimConformUpdateUser();

        assertEquals(newFirstName, resultingUser.getName().getGivenName());
        assertEquals(newLastName, resultingUser.getName().getFamilyName());
        assertEquals(2, resultingUser.getEmails().size());
        Email resultingEmail = null;
        for (Email mail : resultingUser.getEmails()) {
            if (!"delete".equals(mail.getOperation())) {
                resultingEmail = mail;
            }
        }
        assertTrue(resultingEmail.isPrimary());
        assertEquals(newEmail, resultingEmail.getValue());
    }

    @Test
    public void asUpdateUser_noPreviousEmail() {
        user = new User.Builder().build();

        UpdateUserCommand command = new UpdateUserCommand(user);

        String newEmail = "primary_new@osiam.org";
        command.setEmail(newEmail);

        UpdateUser updateUser = command.getAsUpdateUser();
        User resultingUser = updateUser.getScimConformUpdateUser();

        assertEquals(1, resultingUser.getEmails().size());
        assertTrue(resultingUser.getEmails().get(0).isPrimary());
        assertEquals(newEmail, resultingUser.getEmails().get(0).getValue());
    }
}
