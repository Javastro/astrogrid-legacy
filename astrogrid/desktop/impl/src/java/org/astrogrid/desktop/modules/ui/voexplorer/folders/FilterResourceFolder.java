package org.astrogrid.desktop.modules.ui.voexplorer.folders;

import org.astrogrid.desktop.modules.dialogs.registry.RegistryGooglePanel;

/** represents a smart / dynamic folder of resouces 
 *  defined by a filter expression
 *  @todo find a way to estimate / size the result set, and set size accordingly.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 2, 20075:29:27 PM
 */
public class FilterResourceFolder extends ResourceFolder {
	public FilterResourceFolder(String name, String icon, String filter) {
		this(name,icon,filter,-1);
	}
	
	public FilterResourceFolder(String name, String icon, String filter,int sz) {
		super(name, icon);
		this.setFilter(filter);
		this.size = sz;
	}	
	
	public FilterResourceFolder() {
		super(null,defaultIcon);
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
	public String getFilter() {
		return filter;
	}

	public static final String defaultIcon = "smartfolder16.png";
	private String filter;
		
	/** threshold below which all items matched by a filter are displayed. */
	public static final int DISPLAY_ALL_THRESHOLD = 500;
	
	public void display(RegistryGooglePanel p) {
		if (getSize() > -1 && getSize() < DISPLAY_ALL_THRESHOLD) {
			p.displayFilter(filter);
		} else {
			p.applyFilter(filter);
		}
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((this.filter == null) ? 0 : this.filter.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final FilterResourceFolder other = (FilterResourceFolder) obj;
		if (this.filter == null) {
			if (other.filter != null)
				return false;
		} else if (!this.filter.equals(other.filter))
			return false;
		return true;
	} 
}