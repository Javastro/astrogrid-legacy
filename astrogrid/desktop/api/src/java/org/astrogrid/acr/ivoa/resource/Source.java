/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;

/** 
                A bibliographic reference.
@see Content                
 * @author Noel Winstanley
 */
public class Source implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8990456730308358863L;
	private  String format;
	private String value;
	/**     The reference format.  Recognized values include "bibcode", 
                 referring to a standard astronomical bibcode 
                 @see <a href='http://cdsweb.u-strasbg.fr/simbad/refcode.html'>Bibcode Format Description</a>   */
	public String getFormat() {
		return this.format;
	}
	/** the bibliographic reference */
	public String getValue() {
		return this.value;
	}
	/** @exclude */
	public void setFormat(final String format) {
		this.format = format;
	}
    /** @exclude */	
	public void setValue(final String reference) {
		this.value = reference;
	}
    /** @exclude */	
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.format == null) ? 0 : this.format.hashCode());
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
		if (! (obj instanceof Source)) {
            return false;
        }
		final Source other = (Source) obj;
		if (this.format == null) {
			if (other.format != null) {
                return false;
            }
		} else if (!this.format.equals(other.format)) {
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
			buffer.append("Source[");
			buffer.append(", format = ").append(format);
			buffer.append(", value = ").append(value);
			buffer.append("]");
			return buffer.toString();
		}
	
	

}
