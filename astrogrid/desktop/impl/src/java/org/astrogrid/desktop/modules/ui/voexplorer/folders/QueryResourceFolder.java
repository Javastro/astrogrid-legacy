/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.folders;

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
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((this.query == null) ? 0 : this.query.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final QueryResourceFolder other = (QueryResourceFolder) obj;
		if (this.query == null) {
			if (other.query != null)
				return false;
		} else if (!this.query.equals(other.query))
			return false;
		return true;
	}
}