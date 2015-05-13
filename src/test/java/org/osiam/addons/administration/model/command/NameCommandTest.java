package org.osiam.addons.administration.model.command;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.osiam.resources.scim.Name;

public class NameCommandTest {

    private static final String FAMILY_NAME = "Molonate";
    private static final String FORMATTED = "Halona Molonate";
    private static final String GIVEN_NAME = "Halonate";
    private static final String HONORIFIC_PREFIX = "Dr.";
    private static final String HONORIFIC_SUFFIX = "Dr. Dr.";
    private static final String MIDDLENAME = "Disalone";
    private Name name;

    @Before
    public void setUp() {
        this.name = new Name.Builder()
                .setFamilyName(FAMILY_NAME)
                .setFormatted(FORMATTED)
                .setGivenName(GIVEN_NAME)
                .setHonorificPrefix(HONORIFIC_PREFIX)
                .setHonorificSuffix(HONORIFIC_SUFFIX)
                .setMiddleName(MIDDLENAME)
                .build();
    }

    @Test
    public void constructor() {
        NameCommand command = new NameCommand(this.name);

        assertEquals(FAMILY_NAME, command.getFamilyname());
        assertEquals(FORMATTED, command.getFormatted());
        assertEquals(GIVEN_NAME, command.getGivenname());
        assertEquals(HONORIFIC_PREFIX, command.getHonorificprefix());
        assertEquals(HONORIFIC_SUFFIX, command.getHonorificsuffix());
        assertEquals(MIDDLENAME, command.getMiddlename());
    }

    @Test
    public void asUpdateUser() {
        NameCommand command = new NameCommand();

        String familyName = "Silamina";
        String formatted = "tarentius Silamina";
        String givenname = "tarentius";
        String honorificprefix = "Prof.";
        String honorificsuffix = "Prof. Dr.";
        String middlename = "osiamone";

        command.setFamilyname(familyName);
        command.setFormatted(formatted);
        command.setGivenname(givenname);
        command.setHonorificprefix(honorificprefix);
        command.setHonorificsuffix(honorificsuffix);
        command.setMiddlename(middlename);

        assertEquals(familyName, command.getFamilyname());
        assertEquals(formatted, command.getFormatted());
        assertEquals(givenname, command.getGivenname());
        assertEquals(honorificprefix, command.getHonorificprefix());
        assertEquals(honorificsuffix, command.getHonorificsuffix());
        assertEquals(middlename, command.getMiddlename());
    }

}
