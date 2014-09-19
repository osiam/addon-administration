package org.osiam.addons.administration.model.command;

import org.hibernate.validator.constraints.NotEmpty;
import org.osiam.resources.scim.Group;

/**
 * Command object for the group create view.
 */
public class CreateGroupCommand {
	@NotEmpty
	private String displayName;
	private String externalId;

	/**
	 * Creates a new CreateGroupCommand.
	 */
	public CreateGroupCommand() {
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

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	/**
	 * Returns a SCIM {@link Group} based on this command.
	 *
	 * @return the requested {@link Group}
	 */
	public Group getAsGroup() {
		Group.Builder builder = new Group.Builder(getDisplayName());

		builder.setExternalId(getExternalId());

		return builder.build();
	}
}
