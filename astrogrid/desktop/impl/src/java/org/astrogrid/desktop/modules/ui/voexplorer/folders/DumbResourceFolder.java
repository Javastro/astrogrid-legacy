package org.astrogrid.desktop.modules.ui.voexplorer.folders;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.collections.set.ListOrderedSet;
import org.astrogrid.desktop.modules.dialogs.registry.RegistryGooglePanel;

/** represents a classical / static folder of resources */
public class DumbResourceFolder extends ResourceFolder  {

	public DumbResourceFolder() {
		super(null,defaultIcon);
	}
	public DumbResourceFolder(String name, String icon) {
		super(name, icon);
	}
	public DumbResourceFolder(String name) {
		super(name, defaultIcon);
	}
	// just for persistence.
	public String[] getResourceArray() {
		String[] result = new String[resouceUris.size()];
		Iterator i = resouceUris.iterator();
		for (int ix = 0; i.hasNext();ix++) {
			URI u = (URI) i.next();
			 result[ix] = u.toString();
		}
		return result;
	}
	public void setResourceArray(String[] arr) {
		Collection c = Arrays.asList(arr);
		setResourceSet(c);
	}
	
	/** reutrn a set of URI objects */
	public Set getResourceSet() {
		return this.resouceUris;
	}
	/**
	 * 
	 * @param s a collection of URI objects.
	 */
	public void setResourceSet(Collection s) {
		this.resouceUris.clear();
		for (Iterator i = s.iterator(); i.hasNext();) {
			Object element =  i.next();
			if (element instanceof URI) {
				this.resouceUris.add(element);
			} else {
				try {
					this.resouceUris.add(new URI(element.toString()));
				} catch (URISyntaxException x) {
					//logger.error("URISyntaxException",x);
					//@todo report this.
				}
			}
		}
	}

	private static final String defaultIcon ="folder16.png";
	private final Set resouceUris = new ListOrderedSet(); // maintains order of insertion.
	
	public int getSize() {
		return resouceUris.size();
	}
	public void display(RegistryGooglePanel p) {
		p.displayIdSet(resouceUris);
	}
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((this.resouceUris == null) ? 0 : this.resouceUris.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DumbResourceFolder other = (DumbResourceFolder) obj;
		if (this.resouceUris == null) {
			if (other.resouceUris != null)
				return false;
		} else if (!this.resouceUris.equals(other.resouceUris))
			return false;
		return true;
	}

}