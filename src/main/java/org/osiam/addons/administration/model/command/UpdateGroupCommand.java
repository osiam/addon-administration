package org.osiam.addons.administration.model.command;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.osiam.resources.scim.Group;
import org.osiam.resources.scim.MemberRef;
import org.osiam.resources.scim.UpdateGroup;

/**
 * Command object for the group update view.
 */
public class UpdateGroupCommand {
    private String id;

    @NotEmpty
    private String displayName;
    private String externalId;

    private List<String> memberIds = new ArrayList<String>();

    /**
     * Creates a new UpdateGroupCommand based on the given {@link Group}.
     *
     * @param group
     *        the user
     */
    public UpdateGroupCommand(Group group) {
        setId(group.getId());

        setExternalId(group.getExternalId());
        setDisplayName(group.getDisplayName());

        if(group.getMembers() != null){
            for(MemberRef ref : group.getMembers()){
                memberIds.add(ref.getValue());
            }
        }
    }

    /**
     * Creates a new UpdateGroupCommand.
     */
    public UpdateGroupCommand() {
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
     * @param displayName
     *        the displayname to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
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
        if(getExternalId() != null && getExternalId().equals("")) {
            builder.deleteExternalId();
        } else {
            builder.updateExternalId(getExternalId());
        }

        builder.deleteMembers();
        for(String member : memberIds){
            builder.addMember(member);
        }

        return builder.build();
    }
}
