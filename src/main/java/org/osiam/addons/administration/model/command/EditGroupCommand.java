package org.osiam.addons.administration.model.command;

import org.hibernate.validator.constraints.NotEmpty;
import org.osiam.resources.scim.Group;

/**
 * Command object for the group update view.
 */
public class EditGroupCommand {
    private String id;

    @NotEmpty
    private String displayName;
    private String externalId;

    /**
     * Creates a new UpdateGroupCommand based on the given {@link Group}.
     *
     * @param group the group
     */
    public EditGroupCommand(Group group) {
        setId(group.getId());

        setExternalId(group.getExternalId());
        setDisplayName(group.getDisplayName());
    }

    EditGroupCommand() {
    }

    /**
     * Returns the displayname.
     *
     * @return the the displayname
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the displayname.
     *
     * @param displayName the displayname to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    /**
     * Update the given {@link Group} with the values from this command.
     *
     * @return The updated {@link Group}
     */
    public Group updateGroup(Group group) {
        return new Group.Builder(group)
                .setDisplayName(getDisplayName())
                .setExternalId(getExternalId())
                .build();
    }
}
