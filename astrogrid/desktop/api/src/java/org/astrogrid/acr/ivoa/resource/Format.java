/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;

/** Describes a data format that a service can return
 * @author Noel Winstanley
 * @since Aug 5, 20069:42:45 PM
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
	public void setMimeType(boolean mimeType) {
		this.mimeType = mimeType;
	}
	/** the name of this data format */
	public String getValue() {
		return this.value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + (this.mimeType ? 1231 : 1237);
		result = PRIME * result + ((this.value == null) ? 0 : this.value.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (! (obj instanceof Format))
			return false;
		final Format other = (Format) obj;
		if (this.mimeType != other.mimeType)
			return false;
		if (this.value == null) {
			if (other.value != null)
				return false;
		} else if (!this.value.equals(other.value))
			return false;
		return true;
	}

		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("Format[");
			buffer.append("value = ").append(value);
			buffer.append(", mimeType = ").append(mimeType);
			buffer.append("]");
			return buffer.toString();
		}
}
