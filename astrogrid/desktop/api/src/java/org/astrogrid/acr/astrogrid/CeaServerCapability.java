/**
 * 
 */
package org.astrogrid.acr.astrogrid;

import java.net.URI;
import java.util.Arrays;

import org.astrogrid.acr.ivoa.resource.Capability;

/**Registry description of a CEA server.
 * @bean
 * 
 * @author Noel Winstanley
 * @since Aug 5, 200610:01:00 PM
 */
public class CeaServerCapability extends Capability {
    /**
     * 
     */
    private static final long serialVersionUID = -1690230691899100320L;
    public CeaServerCapability() {
        setStandardID(CAPABILITY_ID);
    }
    /** standard identifier for this capability */
    private static final URI CAPABILITY_ID = URI.create("ivo://org.astrogrid/std/CEA/v1.0");
	/**
	 * 
	 */

	private static int hashCode(Object[] array) {
		final int PRIME = 31;
		if (array == null)
			return 0;
		int result = 1;
		for (int index = 0; index < array.length; index++) {
			result = PRIME * result + (array[index] == null ? 0 : array[index].hashCode());
		}
		return result;
	}

	private URI[] managedApplications = new URI[0];

	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + CeaServerCapability.hashCode(this.managedApplications);
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final CeaServerCapability other = (CeaServerCapability) obj;
		if (!Arrays.equals(this.managedApplications, other.managedApplications))
			return false;
		return true;
	}

	/** access a list of the identifiers of the applications this server provides */
	public URI[] getManagedApplications() {
		return this.managedApplications;
	}

	public void setManagedApplications(URI[] managedApplications) {
		this.managedApplications = managedApplications;
	}

	/**
		 * toString methode: creates a String representation of the object
		 * @return the String representation
		 * @author info.vancauwenberge.tostring plugin
	
		 */
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("CeaServerCapability[");
			if (managedApplications == null) {
				buffer.append("managedApplications = ").append("null");
			} else {
				buffer.append("managedApplications = ").append(
					Arrays.asList(managedApplications).toString());
			}
			buffer.append("]");
			return buffer.toString();
		}

}
