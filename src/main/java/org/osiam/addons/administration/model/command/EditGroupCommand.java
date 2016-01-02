package org.osiam.addons.administration.model.command;

import org.hibernate.validator.constraints.NotEmpty;
import org.osiam.resources.scim.Group;
import org.osiam.resources.scim.UpdateGroup;

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
     * Returns a SCIM {@link UpdateGroup} based on this command.
     *
     * @return the requested {@link UpdateGroup}
     */
    public UpdateGroup getAsUpdateGroup() {
        UpdateGroup.Builder builder = new UpdateGroup.Builder();

        builder.updateDisplayName(getDisplayName());
        if (getExternalId() != null && getExternalId().equals("")) {
            builder.deleteExternalId();
        } else {
            builder.updateExternalId(getExternalId());
        }
        return builder.build();
    }
}
