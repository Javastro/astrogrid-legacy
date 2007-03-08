/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.resource.folders;

import javax.swing.Icon;

import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.registry.RegistryGooglePanel;
/** representation of a resource folder
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 2, 20075:26:08 PM
 */
public abstract class ResourceFolder {
	
	/** the name of the folder */
	private String name;

	private String iconName;
	/** whether this resource is 'fixed' - cannot be removed by user.*/
	private boolean fixed = false;
	/** the size / estimated size of this resource. */
	public int size = -1; //@todo make this transient later, when it's all computed.
	
	public ResourceFolder(String name, String iconName) {
		super();
		this.name = name;
		this.iconName = iconName;
	}

	/* display this folder's contents in a google panel.*/
	public abstract void display(RegistryGooglePanel p) ;
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		if (fixed) {
			return "<html><b>" + name;
		} else {
			return  name;
		}
	}

	public void setIconName(String icon) {
		this.iconName = icon;
	}
	public String getIconName() {
		return this.iconName;
	}

	public Icon getIcon() {
		return IconHelper.loadIcon(iconName); // iconHelper takes care of caching.
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int i) {
		this.size = i;
	}

	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	public boolean isFixed() {
		return fixed;
	}

}