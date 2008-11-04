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
import org.apache.commons.vfs.FileObject;

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
				if (pd.getName().equals("file")) {
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
		//@todo work out when it's a root, or remote, or someting
		// and use networkdisk16.png instead.
		setIconName("folder16.png");
	}
	
	public StorageFolder(final String name, final String iconName,final URI uri)  {
		super(name,iconName);
		this.uri =uri;
	}	
	public URI getUri() {
		return this.uri;
	}
	// necessary for persistence.
	public String getUriString() {
		return this.uri == null ? null :  this.uri.toString();
	} 
	public void setUriString(final String uri) throws URISyntaxException {
	    if (StringUtils.isEmpty(uri)) {
	        throw new URISyntaxException(uri,"Empty URI provided");
	    }
		this.uri = new URI(StringUtils.replace(uri.trim()," ","%20")); //escape any spaces that have leaked in.
		file = null;
	}
	
	private transient FileObject file;
	public FileObject getFile() {
		return this.file;
	}

	public void setFile(final FileObject file) {
		this.file = file;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((this.uri == null) ? 0 : this.uri.hashCode());
		return result;
	}

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
