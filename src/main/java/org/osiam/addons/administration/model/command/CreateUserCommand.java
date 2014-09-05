package org.osiam.addons.administration.model.command;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.osiam.resources.scim.User;

public class CreateUserCommand {

    @NotNull
    @NotBlank
    @NotEmpty
    private String userName;

    private String password;

    private boolean active;

    public CreateUserCommand() {
    }

    public CreateUserCommand(User user) {
        this.setUserName(user.getUserName());
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public User getAsUser() {
        return new User.Builder(this.getUserName())
                .setPassword(password)
                .setActive(active)
                .build();
    }
}
