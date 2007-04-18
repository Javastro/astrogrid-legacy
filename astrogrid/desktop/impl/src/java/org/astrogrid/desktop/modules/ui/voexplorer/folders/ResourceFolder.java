/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.folders;

import org.astrogrid.desktop.modules.dialogs.registry.RegistryGooglePanel;
/** representation of a resource folder
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 2, 20075:26:08 PM
 */
public abstract class ResourceFolder extends Folder{

	/** whether this resource is 'fixed' - cannot be removed by user.*/
	private boolean fixed = false;
	/** the size / estimated size of this resource. */
	public int size = -1; //@todo make this transient later, when it's all computed.
	
	public ResourceFolder(String name, String iconName) {
		super(name,iconName);
	}

	/* display this folder's contents in a google panel.*/
	public abstract void display(RegistryGooglePanel p) ;


	public String getName() {
		if (fixed) {
			return "<html><b>" + name;
		} else {
			return  name;
		}
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

	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + (this.fixed ? 1231 : 1237);
		result = PRIME * result + this.size;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ResourceFolder other = (ResourceFolder) obj;
		if (this.fixed != other.fixed)
			return false;
		if (this.size != other.size)
			return false;
		return true;
	}



}