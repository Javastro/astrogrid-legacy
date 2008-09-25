/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;

/**          Date associated with an event in the life cycle of the
               resource.  
             
 * @author Noel Winstanley
 * @see Curation
 * 
 */
public class Date implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2955749554614804679L;
	private String value;
	private String role;
	   /** @exclude */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.role == null) ? 0 : this.role.hashCode());
		result = PRIME * result + ((this.value == null) ? 0 : this.value.hashCode());
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
		if (! (obj instanceof Date)) {
            return false;
        }
		final Date other = (Date) obj;
		if (this.role == null) {
			if (other.role != null) {
                return false;
            }
		} else if (!this.role.equals(other.role)) {
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
	/**     An indication of what this date refers to.   */
	public String getRole() {
		return this.role;
	}
	   /** @exclude */
	public void setRole(final String role) {
		this.role = role;
	}
	/** the date value */
	public String getValue() {
		return this.value;
	}
	   /** @exclude */
	public void setValue(final String value) {
		this.value = value;
	}
    /** @exclude */
		public String toString() {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("Date[");
			buffer.append("value = ").append(value);
			buffer.append(", role = ").append(role);
			buffer.append("]");
			return buffer.toString();
		}

}
