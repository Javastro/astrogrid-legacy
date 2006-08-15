/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.net.URI;

/**
         the name of a potentially registered resource.  That is, the entity
         referred to may have an associated identifier.
 * @author Noel Winstanley
 * @since Jul 31, 20063:59:02 PM
 */
public class ResourceName implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4766263637652231454L;
	public String value;
	public URI id;
	/**          The URI form of the IVOA identifier for the resource refered to */
	public URI getId() {
		return this.id;
	}
	/** the name of the resource */
	public String getValue() {
		return this.value;
	}
	public void setId(URI id) {
		this.id = id;
	}
	public void setValue(String name) {
		this.value = name;
	}
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = PRIME * result + ((this.value == null) ? 0 : this.value.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ResourceName other = (ResourceName) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		if (this.value == null) {
			if (other.value != null)
				return false;
		} else if (!this.value.equals(other.value))
			return false;
		return true;
	}
	/**
		 * toString methode: creates a String representation of the object
		 * @return the String representation
		 * @author info.vancauwenberge.tostring plugin
	
		 */
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("ResourceName[");
			buffer.append(", value = ").append(value);
			buffer.append(", id = ").append(id);
			buffer.append("]");
			return buffer.toString();
		}

}
