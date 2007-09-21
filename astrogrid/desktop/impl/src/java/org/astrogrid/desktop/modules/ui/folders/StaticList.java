package org.astrogrid.desktop.modules.ui.folders;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.astrogrid.desktop.modules.ui.voexplorer.RegistryGooglePanel;
import org.astrogrid.desktop.modules.ui.voexplorer.VOExplorerImpl;

/** represents a classical / static folder of resources */
public class StaticList extends ResourceFolder  {

	public StaticList() {
		super("New list",defaultIcon);
	}
	public StaticList(String name, String icon) {
		super(name, icon);
	}
	public StaticList(String name) {
		super(name, defaultIcon);
	}
	
	public StaticList(String name, Collection coll) {
	    this(name);
	    setResourceSet(coll);
	}
	
	   public StaticList(String name,  String[] arr) {
	        this(name);
	        Collection c = Arrays.asList(arr);
	        setResourceSet(c);
	    }
	// just for persistence.
	/** @deprecated - only used for obsolete persistence method */
	public String[] getResourceArray() {
		String[] result = new String[resouceUris.size()];
		Iterator i = resouceUris.iterator();
		for (int ix = 0; i.hasNext();ix++) {
			URI u = (URI) i.next();
			 result[ix] = u.toString();
		}
		return result;
	}
	/** @deprecated - only used for obsolete persistence method */
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
	private Set resouceUris = new LinkedHashSet(); // maintains order of insertion.
	
	public int getSize() {
		return resouceUris.size();
	}
	public void display(RegistryGooglePanel p) {
		logger.info("Displaying " + getName());
		p.displayIdSet("Contents of " + getName(),resouceUris);
	}
	public void edit(VOExplorerImpl voe) {
		voe.editExistingStaticList(this);
	}
	public void editAsNew(VOExplorerImpl voe) {
		voe.editNewStaticList(this);
	}

}