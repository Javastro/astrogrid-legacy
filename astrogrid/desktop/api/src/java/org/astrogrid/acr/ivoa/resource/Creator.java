/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**       The person or organisation responsible 
            for creating a resource.
         
 * @author Noel Winstanley
 * @see Curation
 */
public class Creator implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4484705219406811935L;
	private  ResourceName name;
	private  URI logo;
	
	/** url pointing to a logo for this entity. 
	 * 
	 * @warning will return null for an url with an unknown scheme, or
	 * which is invalid. To access the value then, use {@link #getLogoURI}
	 * @deprecated prefer {@link #getLogoURI}
	 * */
	@Deprecated
    public URL getLogo() {
		try {
			
			return this.logo == null ? null :this.logo.toURL();
		} catch (final MalformedURLException x) {
			return null;
		}
	}
	
	/** URI of a logo image for this entity */
	public URI getLogoURI() {
		return this.logo;
	}
	/** name of the creator */
	public ResourceName getName() {
		return this.name;
	}
    /** @exclude */	
	public void setLogoURI(final URI logo) {
		this.logo = logo;
	}
    /** @exclude */	
	public void setName(final ResourceName name) {
		this.name = name;
	}
    /** @exclude */	
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.logo == null) ? 0 : this.logo.hashCode());
		result = PRIME * result + ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}
    /** @exclude */	
	public boolean equals(final Object obj) {
		if (this == obj) {
            return true;
        }
		if (obj == null) {
            return false;
        }
		if (! (obj instanceof Creator)) {
            return false;
        }
		final Creator other = (Creator) obj;
		if (this.logo == null) {
			if (other.logo != null) {
                return false;
            }
		} else if (!this.logo.equals(other.logo)) {
            return false;
        }
		if (this.name == null) {
			if (other.name != null) {
                return false;
            }
		} else if (!this.name.equals(other.name)) {
            return false;
        }
		return true;
	}
    /** @exclude */
		public String toString() {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("Creator[");
			buffer.append(", name = ").append(name);
			buffer.append(", logo = ").append(logo);
			buffer.append("]");
			return buffer.toString();
		}

}
