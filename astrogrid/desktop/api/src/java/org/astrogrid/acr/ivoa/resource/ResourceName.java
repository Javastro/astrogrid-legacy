/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.net.URI;

/** The name of a potentially registered resource.
 * @author Noel Winstanley
 */
public class ResourceName implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4766263637652231454L;
	private String value;
	private URI id;
	/**          The IVOA identifier of the resource
	 * @note may be null, if no resource ID is available */
	public URI getId() {
		return this.id;
	}
	/** the name of the resource */
	public String getValue() {
		return this.value;
	}
	   /** @exclude */
	public void setId(final URI id) {
		this.id = id;
	}
	   /** @exclude */
	public void setValue(final String name) {
		this.value = name;
	}
	   /** @exclude */
	@Override
    public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = PRIME * result + ((this.value == null) ? 0 : this.value.hashCode());
		return result;
	}
	   /** @exclude */
	@Override
    public boolean equals(final Object obj) {
		if (this == obj) {
            return true;
        }
		if (obj == null) {
            return false;
        }
		if (! (obj instanceof ResourceName)) {
            return false;
        }
		final ResourceName other = (ResourceName) obj;
		if (this.id == null) {
			if (other.id != null) {
                return false;
            }
		} else if (!this.id.equals(other.id)) {
            return false;
        }
		if (this.value == null) {
			if (other.value != null) {
                return false;
            }
		} else if (!this.value.equals(other.value)) {
            return false;
        }
		return true;
	}
	   /** @exclude */
		@Override
        public String toString() {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("ResourceName[");
			buffer.append(", value = ").append(value);
			buffer.append(", id = ").append(id);
			buffer.append("]");
			return buffer.toString();
		}

}
