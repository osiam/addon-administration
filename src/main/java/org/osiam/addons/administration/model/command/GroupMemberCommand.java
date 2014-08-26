package org.osiam.addons.administration.model.command;

import java.util.ArrayList;
import java.util.List;

import org.osiam.resources.scim.Group;
import org.osiam.resources.scim.MemberRef;

public class GroupMemberCommand {

    private List<String> memberIds = new ArrayList<String>();

    public GroupMemberCommand(Group group){
        if(group.getMembers() != null){
            for(MemberRef ref : group.getMembers()){
                memberIds.add(ref.getValue());
            }
        }
    }

    public List<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
    }
}
