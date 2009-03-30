/**
 * 
 */
package org.astrogrid.acr.astrogrid;

import java.net.URI;
import java.util.Arrays;

import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Capability;

/**Registry description of a CEA server. 
 * 
 * Accessed from a {@link CeaService}.
 * @bean
 * @see Applications Executing remote applications
 * @see Registry Querying for registry resources
 * @author Noel Winstanley
 */
public class CeaServerCapability extends Capability {
    /**
     * 
     */
    private static final long serialVersionUID = -1690230691899100320L;
    /** @exclude */
    public CeaServerCapability() {
        setStandardID(CAPABILITY_ID);
    }
    /** standard identifier for this capability */
    private static final URI CAPABILITY_ID = URI.create("ivo://org.astrogrid/std/CEA/v1.0");
	/**
	 * 
	 */

	private static int hashCode(final Object[] array) {
		final int PRIME = 31;
		if (array == null) {
            return 0;
        }
		int result = 1;
		for (int index = 0; index < array.length; index++) {
			result = PRIME * result + (array[index] == null ? 0 : array[index].hashCode());
		}
		return result;
	}

	private URI[] managedApplications = new URI[0];

	/** @exclude */
	@Override
    public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + CeaServerCapability.hashCode(this.managedApplications);
		return result;
	}
	/** @exclude */
	@Override
    public boolean equals(final Object obj) {
		if (this == obj) {
            return true;
        }
		if (!super.equals(obj)) {
            return false;
        }
		if (getClass() != obj.getClass()) {
            return false;
        }
		final CeaServerCapability other = (CeaServerCapability) obj;
		if (!Arrays.equals(this.managedApplications, other.managedApplications)) {
            return false;
        }
		return true;
	}

	/** list the registry resource identifiers of the applications that this server provides.
	 * 
	 * Each of these identifiers can be retrieved using {@link Registry} to return a corresponding {@link CeaApplication}
	 */
	public URI[] getManagedApplications() {
		return this.managedApplications;
	}

	/** @exclude */
	public void setManagedApplications(final URI[] managedApplications) {
		this.managedApplications = managedApplications;
	}

	/**@exclude
		 * toString methode: creates a String representation of the object
		 * @return the String representation
		 * @author info.vancauwenberge.tostring plugin
	
		 */
		@Override
        public String toString() {
			final StringBuffer buffer = new StringBuffer();
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
