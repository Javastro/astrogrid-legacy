/**
 * 
 */
package org.astrogrid.desktop.modules.ui.folders;

import org.astrogrid.desktop.modules.ui.voexplorer.RegistryGooglePanel;
import org.astrogrid.desktop.modules.ui.voexplorer.VOExplorerImpl;
/** A folder containing a list of registry resources.
 * @TEST mock out voexp.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 2, 20075:26:08 PM
 */
public abstract class ResourceFolder extends Folder{

	/** whether this resource is 'fixed' - cannot be removed by user.*/
	private boolean fixed = false;

	
	/** a description of this folder - can be displayed as a tooltip */
	private String description;
    /**
     * URL of XML specification of this item.  If non-null the item
     * may/should be regenerated from the XML at this location if possible.
     * @see  {@link org.astrogrid.desktop.impl.modules.system.XStreamXmlPersist}
     */
    private String subscription;

	public ResourceFolder(final String name, final String iconName) {
		super(name,iconName);
	}

	/* display this folder's contents in a google panel.*/
	public abstract void display(RegistryGooglePanel p) ;

	/** edit this folder's definition */
	public abstract void edit(VOExplorerImpl voe);
	
	/** edit this folder's definition from new */
	public abstract void editAsNew(VOExplorerImpl voe);

    /** edit the subscription attribute for this folder */
    public void editSubscription(final VOExplorerImpl voe) {
        voe.editExistingSubscription(this);
    }

    /** edit the subscriptino attribute for this folder from new */
    public void editSubscriptionAsNew(final VOExplorerImpl voe) {
        voe.editNewSubscription(this);
    }

	@Override
    public String getName() {
		return name;
	}

	public void setFixed(final boolean fixed) {
		this.fixed = fixed;
	}

	public boolean isFixed() {
		return fixed;
	}
	
	public void setDescription(final String s) {
	    this.description = s;
	}
	
	public String getDescription() {
	     return this.description;
	}

    /**
     * Sets the URL of a location for an XML file containing a description
     * (XStream) of the content of this folder.
     *
     * @param  subscription  XML file URL as string
     */
    public void setSubscription(final String subscription) {
        this.subscription = subscription;
    }

    /**
     * Returns the URL of a location for an XML file containing a description
     * (XStream) of the content of this folder.
     *
     * @return  XML file URL as string
     */
    public String getSubscription() {
        return subscription;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (this.fixed ? 1231 : 1237);
        result = prime * result + this.subscription == null ? 23 : subscription.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ResourceFolder other = (ResourceFolder) obj;
        if (this.fixed != other.fixed) {
            return false;
        }
        if (!(""+this.subscription).equals(""+other.subscription)) {
            return false;
        }
        return true;
    }

}
