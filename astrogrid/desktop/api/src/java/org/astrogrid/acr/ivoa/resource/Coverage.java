/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.util.Arrays;

import org.w3c.dom.Document;

/**
 * @author Noel Winstanley
 * @since Aug 5, 20069:38:43 PM
 *@todo parse more of the coverage thing, if I can understand it.
 */
public class Coverage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8701318183058506916L;
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
	private String[] wavebands;
	private ResourceName[] footprint;
	private Document stcResourceProfile;
	public ResourceName[] getFootprint() {
		return this.footprint;
	}
	public void setFootprint(ResourceName[] footprint) {
		this.footprint = footprint;
	}
	public Document getStcResourceProfile() {
		return this.stcResourceProfile;
	}
	public void setStcResourceProfile(Document stcResourceProfile) {
		this.stcResourceProfile = stcResourceProfile;
	}
	public String[] getWavebands() {
		return this.wavebands;
	}
	public void setWavebands(String[] wavebands) {
		this.wavebands = wavebands;
	}
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + Coverage.hashCode(this.footprint);
		result = PRIME * result + Coverage.hashCode(this.wavebands);
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Coverage other = (Coverage) obj;
		if (!Arrays.equals(this.footprint, other.footprint))
			return false;
		if (!Arrays.equals(this.wavebands, other.wavebands))
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
			buffer.append("Coverage[");
			if (wavebands == null) {
				buffer.append("wavebands = ").append("null");
			} else {
				buffer.append("wavebands = ").append(
					Arrays.asList(wavebands).toString());
			}
			if (footprint == null) {
				buffer.append(", footprint = ").append("null");
			} else {
				buffer.append(", footprint = ").append(
					Arrays.asList(footprint).toString());
			}
			buffer.append(", stcResourceProfile = ").append(stcResourceProfile);
			buffer.append("]");
			return buffer.toString();
		}
}
