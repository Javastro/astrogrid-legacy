/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.util.List;

public class ApplicationDescription {

	private String id;
	private String name;
	private List messages;
	private String version;
	private String iconUrl;
	private String ivorn;
	boolean responding;
	private String description;

	public ApplicationDescription(String id, String name, String description, List messages, String version, String url, String ivorn, boolean responding) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.messages = messages;
		this.version = version;
		this.iconUrl = url;
		this.ivorn = ivorn;
		this.responding = responding;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List getUnderstoodMessages() {
		return messages;
	}

	public String getVersion() {
		return version;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public String getIvorn() {
		return ivorn;
	}

	public boolean isResponding() {
		return responding;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	
}