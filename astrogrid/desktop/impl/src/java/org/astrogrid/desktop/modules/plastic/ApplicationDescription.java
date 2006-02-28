/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.util.List;

public class ApplicationDescription {

	String id;
	String name;
	private List messages;
	String version;
	String iconUrl;
	String ivorn;
	boolean responding;

	public ApplicationDescription(String id, String name, List messages, String version, String url, String ivorn, boolean responding) {
		this.id = id;
		this.name = name;
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
	
}