/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;

/**          Date associated with an event in the life cycle of the
               resource.  
             
 * @author Noel Winstanley
 * @since Aug 2, 200612:20:23 PM
 */
public class Date implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2955749554614804679L;
	private String value;
	private String role;
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.role == null) ? 0 : this.role.hashCode());
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
		final Date other = (Date) obj;
		if (this.role == null) {
			if (other.role != null)
				return false;
		} else if (!this.role.equals(other.role))
			return false;
		if (this.value == null) {
			if (other.value != null)
				return false;
		} else if (!this.value.equals(other.value))
			return false;
		return true;
	}
	/**     A string indicating what the date refers to.   */
	public String getRole() {
		return this.role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	/** the date value */
	public String getValue() {
		return this.value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	/**
		 * toString methode: creates a String representation of the object
		 * @return the String representation
		 * @author info.vancauwenberge.tostring plugin
	
		 */
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("Date[");
			buffer.append("value = ").append(value);
			buffer.append(", role = ").append(role);
			buffer.append("]");
			return buffer.toString();
		}

}
