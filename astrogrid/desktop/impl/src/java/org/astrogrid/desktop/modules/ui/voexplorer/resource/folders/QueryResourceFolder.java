/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.resource.folders;

import javax.swing.Icon;

import org.astrogrid.desktop.modules.dialogs.registry.RegistryGooglePanel;
/** folder populated by a full xquery */
public class QueryResourceFolder extends ResourceFolder {

	public QueryResourceFolder() {
		super(null,FilterResourceFolder.defaultIcon);
	}
	public QueryResourceFolder(String name, String icon, String query) {
		super(name, icon);
		this.query = query;
	}
	private String query;
	public String getQuery() {
		return this.query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public void display(RegistryGooglePanel p) {
		p.displayQuery(query);
	}
}