/**
 * 
 */
package org.astrogrid.desktop.modules.ui.folders;

import org.astrogrid.desktop.modules.ui.voexplorer.RegistryGooglePanel;
import org.astrogrid.desktop.modules.ui.voexplorer.VOExplorerImpl;
/** folder populated by a full xquery */
public class XQueryList extends ResourceFolder {

	public XQueryList() {
		super("New XQuerylist",SmartList.defaultIcon);
	}
	public XQueryList(String name, String query) {
		super(name, SmartList.defaultIcon);
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
		p.displayQuery(getName(),query);
	}

	public void edit(VOExplorerImpl voe) {
		voe.editExistingQueryList(this);
	}
	public void editAsNew(VOExplorerImpl voe) {
		voe.editNewQueryList(this);
	}
}