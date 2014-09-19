package org.osiam.addons.administration.model.command;

import java.util.Date;

import org.osiam.resources.scim.Meta;

public class MetaCommand {

	private Date created;
	private Date lastModified;
	private String location;
	private String resourceType;
	private String version;

	public MetaCommand(Meta meta) {
		this.setCreated(meta.getCreated());
		this.setLastModified(meta.getLastModified());
		this.setLocation(meta.getLocation());
		this.setResourceType(meta.getResourceType());
		this.setVersion(meta.getVersion());
	}

	public MetaCommand() {
	}

	public Meta getAsMeta() {
		return new Meta.Builder()
				.setLocation(this.getLocation())
				.setResourceType(this.getResourceType())
				.setVersion(this.getVersion())
				.build();
	}

	public Date getCreated() {
		return created;
	}

	private void setCreated(Date created) {
		this.created = created;
	}

	public Date getLastModified() {
		return lastModified;
	}

	private void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getLocation() {
		return location;
	}

	private void setLocation(String location) {
		this.location = location;
	}

	public String getResourceType() {
		return resourceType;
	}

	private void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getVersion() {
		return version;
	}

	private void setVersion(String version) {
		this.version = version;
	}
}
