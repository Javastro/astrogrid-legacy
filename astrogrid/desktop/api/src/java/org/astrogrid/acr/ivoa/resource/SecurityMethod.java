/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.net.URI;

/**    An access-control mechanism
 * @author Noel Winstanley
 * @see Interface
 */
public class SecurityMethod implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1122799707246452847L;
	private URI standardID;

	/**     The ID of a standard security mechanism. 
             */
	public URI getStandardID() {
		return this.standardID;
	}
    /** @exclude */
	public void setStandardID(final URI standardID) {
		this.standardID = standardID;
	}
    /** @exclude */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.standardID == null) ? 0 : this.standardID.hashCode());
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
		if (! (obj instanceof SecurityMethod)) {
            return false;
        }
		final SecurityMethod other = (SecurityMethod) obj;
		if (this.standardID == null) {
			if (other.standardID != null) {
                return false;
            }
		} else if (!this.standardID.equals(other.standardID)) {
            return false;
        }
		return true;
	}
    /** @exclude */
		public String toString() {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("SecurityMethod[");
			buffer.append("standardID = ").append(standardID);
			buffer.append("]");
			return buffer.toString();
		}
}
