package org.osiam.addons.administration.model.command;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.osiam.resources.scim.Meta;

import static org.junit.Assert.assertEquals;

public class MetaCommandTest {

    private Meta meta;
    private Date created;
    private Date lastModified;
    private String location;
    private String resourceType;
    private String version;

    @Before
    public void setUp() {
        this.meta = new Meta.Builder()
                .setLocation(this.location)
                .setResourceType(this.resourceType)
                .setVersion(this.version)
                .build();
        this.created = this.meta.getCreated();
        this.lastModified = this.meta.getLastModified();
    }

    @Test
    public void constructor() {
        MetaCommand command = new MetaCommand(this.meta);

        assertEquals(this.location, command.getLocation());
        assertEquals(this.resourceType, command.getResourceType());
        assertEquals(this.version, command.getVersion());
        assertEquals(this.lastModified, command.getLastModified());
        assertEquals(this.created, command.getCreated());
    }
}
