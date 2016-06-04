package org.osiam.addons.administration.model.command;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.osiam.resources.scim.Extension;
import org.osiam.resources.scim.User;

public class UpdateUserCommandTest {
    private User user;
    private String userName = "maglino";
    private Boolean isActive = false;
    private String title = "Weltraumpr\u00e4sident";
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
        UpdateUserCommand command = new UpdateUserCommand(user, new ArrayList<Extension>(0));

        assertEquals(this.userName, command.getUserName());
        assertEquals(this.isActive, command.getActive());
        assertEquals(this.title, command.getTitle());
        assertEquals(this.displayName, command.getDisplayName());
        assertEquals(this.nickName, command.getNickName());
        assertEquals(this.preferredLanguage, command.getPreferredLanguage());
        assertEquals(this.locale, command.getLocale());
        assertEquals(this.profileUrl, command.getProfileURL());
        assertEquals(this.timezone, command.getTimezone());
    }
}
