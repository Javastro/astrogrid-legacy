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

/** A static, enumerated list of resources.
 * 
 *  @TEST mock out calls to voexp, etc.*/
public class StaticList extends ResourceFolder  {

	public StaticList() {
		super("New list",defaultIcon);
	}
	public StaticList(final String name, final String icon) {
		super(name, icon);
	}
	public StaticList(final String name) {
		super(name, defaultIcon);
	}
	
	public StaticList(final String name, final Collection<URI> coll) {
	    this(name);
	    setResourceSet(coll);
	}
	
	   public StaticList(final String name,  final String[] arr) {
	        this(name);
	        final Collection c = Arrays.asList(arr);
	        setResourceSet(c);
	    }
	// just for persistence.
	/** @deprecated - only used for obsolete persistence method */
	@Deprecated
    public String[] getResourceArray() {
		final String[] result = new String[resouceUris.size()];
		final Iterator i = resouceUris.iterator();
		for (int ix = 0; i.hasNext();ix++) {
			final URI u = (URI) i.next();
			 result[ix] = u.toString();
		}
		return result;
	}
	/** @deprecated - only used for obsolete persistence method */
	@Deprecated
    public void setResourceArray(final String[] arr) {
		final Collection c = Arrays.asList(arr);
		setResourceSet(c);
	}
	
	/** reutrn a set of URI objects */
	public Set<URI> getResourceSet() {
		return this.resouceUris;
	}
	/**
	 * 
	 * @param s a collection of URI objects.
	 */
	public void setResourceSet(final Collection s) {
		this.resouceUris.clear();
		for (final Iterator i = s.iterator(); i.hasNext();) {
			final Object element =  i.next();
			if (element instanceof URI) {
				this.resouceUris.add((URI)element);
			} else {
				try {
					this.resouceUris.add(new URI(element.toString()));
				} catch (final URISyntaxException x) {
					//logger.error("URISyntaxException",x);
					//@todo report this.
				}
			}
		}
	}
    private static final String defaultIcon ="doc16.png";
	//private static final String defaultIcon ="folder16.png";
	private final Set<URI> resouceUris = new LinkedHashSet<URI>(); // maintains order of insertion.
	
	public int getSize() {
		return resouceUris.size();
	}
	@Override
    public void display(final RegistryGooglePanel p) {
		logger.info("Displaying " + getName());
		p.displayIdSet("Contents of " + getName(),resouceUris);
	}
	@Override
    public void edit(final VOExplorerImpl voe) {
		voe.editExistingStaticList(this);
	}
	@Override
    public void editAsNew(final VOExplorerImpl voe) {
		voe.editNewStaticList(this);
	}

}