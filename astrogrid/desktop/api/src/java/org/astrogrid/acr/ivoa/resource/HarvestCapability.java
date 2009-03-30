/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;


/** Capability of harvesting a registry.
 * @author Noel Winstanley
 * @see RegistryService
 */
public class HarvestCapability extends RegistryCapability {

	/**
     * 
     */
    private static final long serialVersionUID = 5596947048291053986L;
    /**
	 * 
	 */
	private int maxRecords;

	/** maximum number of records that this service will supply to a harvest request */
	public int getMaxRecords() {
		return this.maxRecords;
	}
    /** @exclude */
	public void setMaxRecords(final int maxRecords) {
		this.maxRecords = maxRecords;
	}
    /** @exclude */
	@Override
    public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + this.maxRecords;
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
		if (! (obj instanceof HarvestCapability)) {
            return false;
        }
		final HarvestCapability other = (HarvestCapability) obj;
		if (this.maxRecords != other.maxRecords) {
            return false;
        }
		return true;
	}
    /** @exclude */
		@Override
        public String toString() {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("HarvestCapability[");
			buffer.append("maxRecords = ").append(maxRecords);
			buffer.append("]");
			return buffer.toString();
		}


}
