/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/** Access URL for a service.
 * @author Noel Winstanley
 * @since Jul 31, 20064:46:22 PM
 */
public class AccessURL implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6840826388724043304L;
	private  URI value;
	private  String use;
	/** the service endpoint 
	 * warning: will return null for an url with an unknown scheme, or
	 * which is invalid. To access the value then, use {@link #getValueURI}
	 * @deprecated prefer {@link #getValueURI}
	 * */
	public URL getValue() {
		try {
			return this.value==null ? null : this.value.toURL();
		} catch (MalformedURLException x) {
		return null;
		}
	}
	
	/** access the literal value of the access url - useful in cases where getValue()
	 * returns null
	 * @return
	 */
	public URI getValueURI() {
			return this.value;
	}
	
	/**     A flag indicating whether this should be interpreted as a base
               URL, a full URL, or a URL to a directory that will produce a 
               listing of files. */
	public String getUse() {
		return this.use;
	}
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.value == null) ? 0 : this.value.hashCode());
		result = PRIME * result + ((this.use == null) ? 0 : this.use.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AccessURL other = (AccessURL) obj;
		if (this.value == null) {
			if (other.value != null)
				return false;
		} else if (!this.value.equals(other.value))
			return false;
		if (this.use == null) {
			if (other.use != null)
				return false;
		} else if (!this.use.equals(other.use))
			return false;
		return true;
	}
	public void setValueURI(URI url) {
		this.value = url;
	}
	public void setUse(String use) {
		this.use = use;
	}


		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("AccessURL[");
			buffer.append(", value = ").append(value);
			buffer.append(", use = ").append(use);
			buffer.append("]");
			return buffer.toString();
		}

}
