/**
 * 
 */
package org.astrogrid.desktop.modules.ui.folders;

import org.astrogrid.desktop.modules.ui.voexplorer.RegistryGooglePanel;
import org.astrogrid.desktop.modules.ui.voexplorer.VOExplorerImpl;
/** representation of a resource folder
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 2, 20075:26:08 PM
 */
public abstract class ResourceFolder extends Folder{

	/** whether this resource is 'fixed' - cannot be removed by user.*/
	private boolean fixed = false;

	public ResourceFolder(String name, String iconName) {
		super(name,iconName);
	}

	/* display this folder's contents in a google panel.*/
	public abstract void display(RegistryGooglePanel p) ;

	/** edit this folder's definition */
	public abstract void edit(VOExplorerImpl voe);
	
	/** edit this folder's definition from new */
	public abstract void editAsNew(VOExplorerImpl voe);

	public String getName() {
		return name;
	}

	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	public boolean isFixed() {
		return fixed;
	}

    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (this.fixed ? 1231 : 1237);
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
        return true;
    }

}