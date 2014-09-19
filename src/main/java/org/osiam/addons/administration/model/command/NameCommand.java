package org.osiam.addons.administration.model.command;

import javax.validation.constraints.NotNull;

import org.osiam.resources.scim.Name;

public class NameCommand {

	@NotNull
	private String familyname;

	@NotNull
	private String formatted;

	@NotNull
	private String givenname;

	@NotNull
	private String honorificprefix;

	@NotNull
	private String honorificsuffix;

	@NotNull
	private String middlename;

	public NameCommand() {

	}

	public NameCommand(Name name) {
		this.setFamilyname(name.getFamilyName());
		this.setFormatted(name.getFormatted());
		this.setGivenname(name.getGivenName());
		this.setHonorificprefix(name.getHonorificPrefix());
		this.setHonorificsuffix(name.getHonorificSuffix());
		this.setMiddlename(name.getMiddleName());
	}

	public Name getAsName() {
		return new Name.Builder()
				.setFamilyName(this.getFamilyname())
				.setFormatted(this.getFormatted())
				.setGivenName(this.getGivenname())
				.setHonorificPrefix(this.getHonorificprefix())
				.setHonorificSuffix(this.getHonorificsuffix())
				.setMiddleName(this.getMiddlename())
				.build();
	}

	public String getFamilyname() {
		return familyname;
	}

	public void setFamilyname(String familyname) {
		this.familyname = familyname;
	}

	public String getFormatted() {
		return formatted;
	}

	public void setFormatted(String formatted) {
		this.formatted = formatted;
	}

	public String getGivenname() {
		return givenname;
	}

	public void setGivenname(String givenname) {
		this.givenname = givenname;
	}

	public String getHonorificprefix() {
		return honorificprefix;
	}

	public void setHonorificprefix(String honorificprefix) {
		this.honorificprefix = honorificprefix;
	}

	public String getHonorificsuffix() {
		return honorificsuffix;
	}

	public void setHonorificsuffix(String honorificsuffix) {
		this.honorificsuffix = honorificsuffix;
	}

	public String getMiddlename() {
		return middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}
}
