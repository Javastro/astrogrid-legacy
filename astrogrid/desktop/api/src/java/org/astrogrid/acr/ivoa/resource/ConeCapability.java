/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/**
 * @author Noel Winstanley
 * @since Aug 5, 20069:52:40 PM
 */
public class ConeCapability extends Capability {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6428664326439921845L;
	protected float maxSR;
    protected int maxRecords;
    protected boolean verbosity;
    protected float testRA;
    protected float testDec;
    protected float testSR;
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + this.maxRecords;
		result = PRIME * result + Float.floatToIntBits(this.maxSR);
		result = PRIME * result + Float.floatToIntBits(this.testDec);
		result = PRIME * result + Float.floatToIntBits(this.testRA);
		result = PRIME * result + Float.floatToIntBits(this.testSR);
		result = PRIME * result + (this.verbosity ? 1231 : 1237);
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ConeCapability other = (ConeCapability) obj;
		if (this.maxRecords != other.maxRecords)
			return false;
		if (Float.floatToIntBits(this.maxSR) != Float.floatToIntBits(other.maxSR))
			return false;
		if (Float.floatToIntBits(this.testDec) != Float.floatToIntBits(other.testDec))
			return false;
		if (Float.floatToIntBits(this.testRA) != Float.floatToIntBits(other.testRA))
			return false;
		if (Float.floatToIntBits(this.testSR) != Float.floatToIntBits(other.testSR))
			return false;
		if (this.verbosity != other.verbosity)
			return false;
		return true;
	}
	public int getMaxRecords() {
		return this.maxRecords;
	}
	public void setMaxRecords(int maxRecords) {
		this.maxRecords = maxRecords;
	}
	public float getMaxSR() {
		return this.maxSR;
	}
	public void setMaxSR(float maxSR) {
		this.maxSR = maxSR;
	}
	public float getTestDec() {
		return this.testDec;
	}
	public void setTestDec(float testDec) {
		this.testDec = testDec;
	}
	public float getTestRA() {
		return this.testRA;
	}
	public void setTestRA(float testRA) {
		this.testRA = testRA;
	}
	public float getTestSR() {
		return this.testSR;
	}
	public void setTestSR(float testSR) {
		this.testSR = testSR;
	}
	public boolean isVerbosity() {
		return this.verbosity;
	}
	public void setVerbosity(boolean verbosity) {
		this.verbosity = verbosity;
	}
	/**
		 * toString methode: creates a String representation of the object
		 * @return the String representation
		 * @author info.vancauwenberge.tostring plugin
	
		 */
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("ConeCapability[");
			buffer.append("maxSR = ").append(maxSR);
			buffer.append(", maxRecords = ").append(maxRecords);
			buffer.append(", verbosity = ").append(verbosity);
			buffer.append(", testRA = ").append(testRA);
			buffer.append(", testDec = ").append(testDec);
			buffer.append(", testSR = ").append(testSR);
			buffer.append(", Capability").append(super.toString());
			buffer.append("]");
			return buffer.toString();
		}
}
