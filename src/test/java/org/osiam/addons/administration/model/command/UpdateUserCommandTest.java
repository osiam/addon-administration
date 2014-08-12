package org.osiam.addons.administration.model.command;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.osiam.resources.scim.UpdateUser;
import org.osiam.resources.scim.User;

public class UpdateUserCommandTest {

    private User user;
    private String userName = "maglino";
    private Boolean isActive = false;
    private String title = "Weltraumpräsident";
    private String displayName = "Peter";
    private String nickName = "Peat";
    private String preferredLanguage = "DE";
    private String locale = "de_DE";
    private String profileUrl = "myProfile.gibtsNielmals.com";
    private String timezone = "Mond/DieDunkleSeite";
    

    @Before
    public void setUp() {
        user = new User.Builder(this.userName)
            .setActive(this.isActive)
            .setTitle(this.title)
            .setDisplayName(this.displayName)
            .setNickName(this.nickName)
            .setPreferredLanguage(this.preferredLanguage)
            .setLocale(this.locale)
            .setProfileUrl(this.profileUrl)
            .setTimezone(this.timezone)
            .build();
    }

    @Test
    public void constructor() {
        UpdateUserCommand command = new UpdateUserCommand(user);

        assertEquals(this.userName, command.getUserName());
        assertEquals(this.isActive, command.isActive());
        assertEquals(this.title, command.getTitle());
        assertEquals(this.displayName, command.getDisplayName());
        assertEquals(this.nickName, command.getNickName());
        assertEquals(this.preferredLanguage, command.getPreferredLanguage());
        assertEquals(this.locale, command.getLocale());
        assertEquals(this.profileUrl, command.getProfileURL());
        assertEquals(this.timezone, command.getTimezone());
    }

    @Test
    public void asUpdateUser() {
        UpdateUserCommand command = new UpdateUserCommand(user);

        String userName = "updatali";
        Boolean isActive = true;
        String title = "Weltraumpräsidentin";
        String displayName = "Hans";
        String nickName = "MasterOfDisaster";
        String preferredLanguage = "EN";
        String locale = "df_GF";
        String profileUrl = "myProfile.gibtsImmerNochNicht.com";
        String timezone = "Mond/DieHelleSeite";

        command.setUserName(userName);
        command.setActive(isActive);
        command.setTitle(title);
        command.setDisplayName(displayName);
        command.setNickName(nickName);
        command.setPreferredLanguage(preferredLanguage);
        command.setLocale(locale);
        command.setProfileURL(profileUrl);
        command.setTimezone(timezone);

        UpdateUser updateUser = command.getAsUpdateUser();
        User resultingUser = updateUser.getScimConformUpdateUser();

        assertEquals(userName, resultingUser.getUserName());
        assertEquals(isActive, resultingUser.isActive());
        assertEquals(title, resultingUser.getTitle());
        assertEquals(displayName, resultingUser.getDisplayName());
        assertEquals(nickName, resultingUser.getNickName());
        assertEquals(preferredLanguage, resultingUser.getPreferredLanguage());
        assertEquals(locale, resultingUser.getLocale());
        assertEquals(profileUrl, resultingUser.getProfileUrl());
        assertEquals(timezone, resultingUser.getTimezone());
    }
}
