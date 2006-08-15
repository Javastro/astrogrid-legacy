/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/**
 * @author Noel Winstanley
 * @since Aug 5, 20069:57:07 PM
 */
public class SiapCapability extends Capability {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7374155736625037914L;
	protected String imageServiceType ;
    protected float maxQueryRegionSizeRa;
    protected float maxQueryRegionSizeDec;
    protected float maxImageExtentRa;
    protected float maxImageExtentDec;
    protected int maxImageSizeRa;
    protected int maxImageSizeDec;
    protected int maxFileSize;
    protected int maxRecords;
	public String getImageServiceType() {
		return this.imageServiceType;
	}
	public void setImageServiceType(String imageServiceType) {
		this.imageServiceType = imageServiceType;
	}
	public int getMaxFileSize() {
		return this.maxFileSize;
	}
	public void setMaxFileSize(int maxFileSize) {
		this.maxFileSize = maxFileSize;
	}
	public float getMaxImageExtentDec() {
		return this.maxImageExtentDec;
	}
	public void setMaxImageExtentDec(float maxImageExtentDec) {
		this.maxImageExtentDec = maxImageExtentDec;
	}
	public float getMaxImageExtentRa() {
		return this.maxImageExtentRa;
	}
	public void setMaxImageExtentRa(float maxImageExtentRa) {
		this.maxImageExtentRa = maxImageExtentRa;
	}
	public int getMaxImageSizeDec() {
		return this.maxImageSizeDec;
	}
	public void setMaxImageSizeDec(int maxImageSizeDec) {
		this.maxImageSizeDec = maxImageSizeDec;
	}
	public int getMaxImageSizeRa() {
		return this.maxImageSizeRa;
	}
	public void setMaxImageSizeRa(int maxImageSizeRa) {
		this.maxImageSizeRa = maxImageSizeRa;
	}
	public float getMaxQueryRegionSizeDec() {
		return this.maxQueryRegionSizeDec;
	}
	public void setMaxQueryRegionSizeDec(float maxQueryRegionSizeDec) {
		this.maxQueryRegionSizeDec = maxQueryRegionSizeDec;
	}
	public float getMaxQueryRegionSizeRa() {
		return this.maxQueryRegionSizeRa;
	}
	public void setMaxQueryRegionSizeRa(float maxQueryRegionSizeRa) {
		this.maxQueryRegionSizeRa = maxQueryRegionSizeRa;
	}
	public int getMaxRecords() {
		return this.maxRecords;
	}
	public void setMaxRecords(int maxRecords) {
		this.maxRecords = maxRecords;
	}
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((this.imageServiceType == null) ? 0 : this.imageServiceType.hashCode());
		result = PRIME * result + this.maxFileSize;
		result = PRIME * result + Float.floatToIntBits(this.maxImageExtentDec);
		result = PRIME * result + Float.floatToIntBits(this.maxImageExtentRa);
		result = PRIME * result + this.maxImageSizeDec;
		result = PRIME * result + this.maxImageSizeRa;
		result = PRIME * result + Float.floatToIntBits(this.maxQueryRegionSizeDec);
		result = PRIME * result + Float.floatToIntBits(this.maxQueryRegionSizeRa);
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
		final SiapCapability other = (SiapCapability) obj;
		if (this.imageServiceType == null) {
			if (other.imageServiceType != null)
				return false;
		} else if (!this.imageServiceType.equals(other.imageServiceType))
			return false;
		if (this.maxFileSize != other.maxFileSize)
			return false;
		if (Float.floatToIntBits(this.maxImageExtentDec) != Float.floatToIntBits(other.maxImageExtentDec))
			return false;
		if (Float.floatToIntBits(this.maxImageExtentRa) != Float.floatToIntBits(other.maxImageExtentRa))
			return false;
		if (this.maxImageSizeDec != other.maxImageSizeDec)
			return false;
		if (this.maxImageSizeRa != other.maxImageSizeRa)
			return false;
		if (Float.floatToIntBits(this.maxQueryRegionSizeDec) != Float.floatToIntBits(other.maxQueryRegionSizeDec))
			return false;
		if (Float.floatToIntBits(this.maxQueryRegionSizeRa) != Float.floatToIntBits(other.maxQueryRegionSizeRa))
			return false;
		if (this.maxRecords != other.maxRecords)
			return false;
		return true;
	}
	/**
		 * toString methode: creates a String representation of the object
		 * @return the String representation
		 * @author info.vancauwenberge.tostring plugin
	
		 */
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("SiapCapability[");
			buffer.append("imageServiceType = ").append(imageServiceType);
			buffer.append(", maxQueryRegionSizeRa = ").append(maxQueryRegionSizeRa);
			buffer.append(", maxQueryRegionSizeDec = ").append(
				maxQueryRegionSizeDec);
			buffer.append(", maxImageExtentRa = ").append(maxImageExtentRa);
			buffer.append(", maxImageExtentDec = ").append(maxImageExtentDec);
			buffer.append(", maxImageSizeRa = ").append(maxImageSizeRa);
			buffer.append(", maxImageSizeDec = ").append(maxImageSizeDec);
			buffer.append(", maxFileSize = ").append(maxFileSize);
			buffer.append(", maxRecords = ").append(maxRecords);
			buffer.append("]");
			return buffer.toString();
		}
}
