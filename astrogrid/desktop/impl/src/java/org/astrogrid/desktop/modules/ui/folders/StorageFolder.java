/**
 * 
 */
package org.astrogrid.desktop.modules.ui.folders;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;

/**A bookmark for FileExplorer.
 * @todo merge with resourceFolder later.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 26, 200710:53:04 PM
 * @TEST can this be serialized better by xstream now?
 * @TEST get description
 */
public class StorageFolder extends Folder {
	
	//@todo is this needed now?
	static  {
		try { // set up the bean behaviour. - need to do this to prevent XMLSerializer persisting the FileObject. blergh.
			final BeanInfo info = Introspector.getBeanInfo(StorageFolder.class);
			final PropertyDescriptor[] propertyDescriptors =
				info.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; ++i) {
				final PropertyDescriptor pd = propertyDescriptors[i];
				if (pd.getName().equals("file") || pd.getName().equals("uri")) {
					pd.setValue("transient", Boolean.TRUE);
				}
			}
		} catch (final IntrospectionException x) {
			logger.error("IntrospectionException",x);
		}
	}			
	
	private URI uri;
	private String description;
	// constructor for persistence.
	public StorageFolder()  {
		super();		
		setIconName("folder16.png");
	}
	
	public StorageFolder(final String name, final String iconName,final URI uri)  {
		super(name,iconName);
		setUri(uri);
		setIconName(iconName); // used to ignore any icon set by setURI.
	}	
	/** access the URI for this folder */
	public URI getUri() {
		return this.uri;
	}
	// necessary for persistence.
	public String getUriString() {
		return this.uri == null ? null :  this.uri.toString();
	} 
	// necessary for persistence.
	public void setUriString(final String uri) throws URISyntaxException {
	    if (StringUtils.isEmpty(uri)) {
	        throw new URISyntaxException(uri,"Empty URI provided");
	    }
		setUri( new URI(StringUtils.replace(uri.trim()," ","%20"))); //escape any spaces that have leaked in.	
	}
	/** set the URI for this storage folder .
	 * setting URI nullifies the file attribute, and recomputes the icon name*/
	public void setUri(final URI uri) {
	    this.uri = uri;
	    this.file = null;
	    // compute a URI.
	    if ("file".equals(uri.getScheme())) {
	        setIconName("folder16.png");
	    } else { // asssume it's remote then
	        setIconName("myspace16.png");
	    }
	}
	
	private transient FileObjectView file;
	/** acceess the transient file object for this storage folder - may be null*/
	public FileObjectView getFile() {
		return this.file;
	}
	/** set the file object - transient, and not used in the definition of the storage folder */
	public void setFile(final FileObjectView file) {
		this.file = file;
	}

	@Override
    public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((this.uri == null) ? 0 : this.uri.hashCode());
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
		final StorageFolder other = (StorageFolder) obj;
		if (this.uri == null) {
			if (other.uri != null) {
                return false;
            }
		} else if (!this.uri.equals(other.uri)) {
            return false;
        }
		return true;
	}

    public final String getDescription() {
        return this.description;
    }

    public final void setDescription(final String description) {
        this.description = description;
    }



	
}
