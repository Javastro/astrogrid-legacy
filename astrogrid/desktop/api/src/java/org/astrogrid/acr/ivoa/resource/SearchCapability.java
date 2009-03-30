/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.util.Arrays;

import org.astrogrid.acr.ivoa.Registry;

/** Capability to query  a registry
 * @see RegistryService
 * @see Registry
 * @author Noel Winstanley
 */
public class SearchCapability extends RegistryCapability {
	
	/**
     * 
     */
    private static final long serialVersionUID = -6972269072498293663L;
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
	private int maxRecords;
	private String extensionSearchSupport;
	private String[] optionalProtocol = new String[0];
    /** @exclude */	
	@Override
    public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((this.extensionSearchSupport == null) ? 0 : this.extensionSearchSupport.hashCode());
		result = PRIME * result + this.maxRecords;
		result = PRIME * result + SearchCapability.hashCode(this.optionalProtocol);
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
		if (! (obj instanceof SearchCapability)) {
            return false;
        }
		final SearchCapability other = (SearchCapability) obj;
		if (this.extensionSearchSupport == null) {
			if (other.extensionSearchSupport != null) {
                return false;
            }
		} else if (!this.extensionSearchSupport.equals(other.extensionSearchSupport)) {
            return false;
        }
		if (this.maxRecords != other.maxRecords) {
            return false;
        }
		if (!Arrays.equals(this.optionalProtocol, other.optionalProtocol)) {
            return false;
        }
		return true;
	}
	/** the level of support provided for searching against
                       metadata defined in a legal VOResource extension schema.*/
	public String getExtensionSearchSupport() {
		return this.extensionSearchSupport;
	}
    /** @exclude */	
	public void setExtensionSearchSupport(final String extensionSearchSupport) {
		this.extensionSearchSupport = extensionSearchSupport;
	}
	/** returns the maximum number of records this service will return in response to a query */
	public int getMaxRecords() {
		return this.maxRecords;
	}
    /** @exclude */	
	public void setMaxRecords(final int maxRecords) {
		this.maxRecords = maxRecords;
	}
	/** A list of additional optional search protocols this registry provides 
	 * @note this method will return {@code xquery} if the registry supports the optional xquery-search method*/
	public String[] getOptionalProtocol() {
		return this.optionalProtocol;
	}
    /** @exclude */	
	public void setOptionalProtocol(final String[] optionalProtocol) {
		this.optionalProtocol = optionalProtocol;
	}
    /** @exclude */
		@Override
        public String toString() {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("SearchCapability[");
			buffer.append("maxRecords = ").append(maxRecords);
			buffer.append(", extensionSearchSupport = ").append(
				extensionSearchSupport);
			if (optionalProtocol == null) {
				buffer.append(", optionalProtocol = ").append("null");
			} else {
				buffer.append(", optionalProtocol = ").append(
					Arrays.asList(optionalProtocol).toString());
			}
			buffer.append("]");
			return buffer.toString();
		}
	

}
