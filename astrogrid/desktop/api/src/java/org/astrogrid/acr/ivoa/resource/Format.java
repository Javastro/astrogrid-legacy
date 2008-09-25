/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;

/** Description of a data format that a service can return.
 * @author Noel Winstanley
 * @see DataCollection
 */
public class Format implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1809580501692644534L;
	private String value;
	private boolean mimeType;
	/** true if this format is described as a mime type */
	public boolean isMimeType() {
		return this.mimeType;
	}
    /** @exclude */	
	public void setMimeType(final boolean mimeType) {
		this.mimeType = mimeType;
	}
	/** the data format */
	public String getValue() {
		return this.value;
	}
    /** @exclude */	
	public void setValue(final String value) {
		this.value = value;
	}
	   /** @exclude */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + (this.mimeType ? 1231 : 1237);
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
		if (! (obj instanceof Format)) {
            return false;
        }
		final Format other = (Format) obj;
		if (this.mimeType != other.mimeType) {
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
    /** @exclude */
		public String toString() {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("Format[");
			buffer.append("value = ").append(value);
			buffer.append(", mimeType = ").append(mimeType);
			buffer.append("]");
			return buffer.toString();
		}
}
