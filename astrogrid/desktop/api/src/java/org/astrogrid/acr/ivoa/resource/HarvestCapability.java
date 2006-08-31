/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/** Capability of registries that can be harvested.
 * @author Noel Winstanley
 * @since Aug 5, 200610:22:56 PM
 */
public class HarvestCapability extends Capability {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5596947048291053986L;
	private int maxRecords;

	/** maximum number of records that this service will supply to a harvest request */
	public int getMaxRecords() {
		return this.maxRecords;
	}

	public void setMaxRecords(int maxRecords) {
		this.maxRecords = maxRecords;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + this.maxRecords;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final HarvestCapability other = (HarvestCapability) obj;
		if (this.maxRecords != other.maxRecords)
			return false;
		return true;
	}

		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("HarvestCapability[");
			buffer.append("maxRecords = ").append(maxRecords);
			buffer.append("]");
			return buffer.toString();
		}


}
