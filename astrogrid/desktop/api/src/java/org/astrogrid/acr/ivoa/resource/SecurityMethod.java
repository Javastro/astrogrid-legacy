/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.net.URI;

/**     a description of a security mechanism.
 * @author Noel Winstanley
 * @since Aug 3, 200612:25:20 AM
 */
public class SecurityMethod implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1122799707246452847L;
	private URI standardID;

	/**      A URI identifier for a standard security mechanism. 
             */
	public URI getStandardID() {
		return this.standardID;
	}

	public void setStandardID(URI standardID) {
		this.standardID = standardID;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.standardID == null) ? 0 : this.standardID.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (! (obj instanceof SecurityMethod))
			return false;
		final SecurityMethod other = (SecurityMethod) obj;
		if (this.standardID == null) {
			if (other.standardID != null)
				return false;
		} else if (!this.standardID.equals(other.standardID))
			return false;
		return true;
	}

		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("SecurityMethod[");
			buffer.append("standardID = ").append(standardID);
			buffer.append("]");
			return buffer.toString();
		}
}
