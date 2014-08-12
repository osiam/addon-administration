package org.osiam.addons.administration.model.command;

import org.junit.Before;
import org.junit.Test;
import org.osiam.resources.scim.Name;
import static org.junit.Assert.assertEquals;

public class NameCommandTest {

    private Name name;
    private String familyname = "Molonate";
    private String formatted = "Halona Molonate";
    private String givenname = "Halonate";
    private String honorificprefix = "Dr.";
    private String honorificsuffix = "Dr. Dr.";
    private String middlename = "Disalone";

    @Before
    public void setUp() {
        this.name = new Name.Builder()
                .setFamilyName(this.familyname)
                .setFormatted(this.formatted)
                .setGivenName(this.givenname)
                .setHonorificPrefix(this.honorificprefix)
                .setHonorificSuffix(this.honorificsuffix)
                .setMiddleName(this.middlename)
                .build();
    }

    @Test
    public void constructor() {
        NameCommand command = new NameCommand(this.name);

        assertEquals(this.familyname, command.getFamilyname());
        assertEquals(this.formatted, command.getFormatted());
        assertEquals(this.givenname, command.getGivenname());
        assertEquals(this.honorificprefix, command.getHonorificprefix());
        assertEquals(this.honorificsuffix, command.getHonorificsuffix());
        assertEquals(this.middlename, command.getMiddlename());
    }

    @Test
    public void asUpdateUser() {
        NameCommand command = new NameCommand();

        String familyname = "Silamina";
        String formatted = "tarentius Silamina";
        String givenname = "tarentius";
        String honorificprefix = "Prof.";
        String honorificsuffix = "Prof. Dr.";
        String middlename = "osiamone";

        command.setFamilyname(familyname);
        command.setFormatted(formatted);
        command.setGivenname(givenname);
        command.setHonorificprefix(honorificprefix);
        command.setHonorificsuffix(honorificsuffix);
        command.setMiddlename(middlename);

        assertEquals(familyname, command.getFamilyname());
        assertEquals(formatted, command.getFormatted());
        assertEquals(givenname, command.getGivenname());
        assertEquals(honorificprefix, command.getHonorificprefix());
        assertEquals(honorificsuffix, command.getHonorificsuffix());
        assertEquals(middlename, command.getMiddlename());
    }

}
