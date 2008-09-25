/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/** URL from which a capability of a service can be accessed. 
 * @see Capability
 * @see Service
 * @see Interface
 * @author Noel Winstanley
 */
public class AccessURL implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6840826388724043304L;
	private  URI value;
	private  String use;
	/** The service endpoint. 
	 * @warning will return null for an url with an unknown scheme, or
	 * which is invalid. To access the value then, use {@link #getValueURI}
	 *
	 *  @deprecated prefer {@link #getValueURI}
	 * */
	@Deprecated
    public URL getValue() {
		try {
			return this.value==null ? null : this.value.toURL();
		} catch (final MalformedURLException x) {
		return null;
		}
	}
	
	/** Access the literal value of the access url.
	 * @note useful in cases where getValue()
	 * returns null
	 * @return the literal value of the access url.
	 */
	public URI getValueURI() {
			return this.value;
	}
	
	/**     Indicates whether this AccessURL should be interpreted as a base
               URL, a full URL, or a URL to a directory that will produce a 
               listing of files. */
	public String getUse() {
		return this.use;
	}
	/** @exclude */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.value == null) ? 0 : this.value.hashCode());
		result = PRIME * result + ((this.use == null) ? 0 : this.use.hashCode());
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
		if (! (obj instanceof AccessURL)) {
            return false;
        }
		final AccessURL other = (AccessURL) obj;
		if (this.value == null) {
			if (other.value != null) {
                return false;
            }
		} else if (!this.value.equals(other.value)) {
            return false;
        }
		if (this.use == null) {
			if (other.use != null) {
                return false;
            }
		} else if (!this.use.equals(other.use)) {
            return false;
        }
		return true;
	}
	/** @exclude */
	public void setValueURI(final URI url) {
		this.value = url;
	}
	/** @exclude */
	public void setUse(final String use) {
		this.use = use;
	}
/**@exclude */
		public String toString() {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("AccessURL[");
			buffer.append(", value = ").append(value);
			buffer.append(", use = ").append(use);
			buffer.append("]");
			return buffer.toString();
		}

}
