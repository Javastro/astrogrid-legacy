package org.astrogrid.desktop.modules.ui.voexplorer.resource.folders;

import javax.swing.Icon;

import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.registry.RegistryGooglePanel;

/** represents a smart / dynamic folder of resouces 
 *  defined by a filter expression
 *  @todo find a way to estimate / size the result set, and set size accordingly.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 2, 20075:29:27 PM
 */
public class FilterResourceFolder extends ResourceFolder {
	public FilterResourceFolder(String name, String icon, String filter) {
		super(name, icon);
		this.setFilter(filter);
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
}