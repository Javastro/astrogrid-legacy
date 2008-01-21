/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.util.Arrays;

import org.w3c.dom.Document;

/**  A description of how a resource's contents or behavior maps
           to the sky, to time, and to frequency space, including
           coverage and resolution. 
 * @author Noel Winstanley
 * @since Aug 5, 20069:38:43 PM
 *@todo parse more of the coverage thing, if I can understand it.
 */
public class Coverage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8489946252117775819L;
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
	private String[] wavebands = new String[0];
	private ResourceName footprint;
	private StcResourceProfile stcResourceProfile;
	/**  a reference to a footprint service for retrieving 
                  precise and up-to-date description of coverage. 
                    */
	public ResourceName getFootprint() {
		return this.footprint;
	}
	public void setFootprint(ResourceName footprint) {
		this.footprint = footprint;
	}
	/** The STC description of the location of the resource's 
                 data (or behavior on data) on the sky, in time, and in 
                 frequency space, including resolution.  
	 * 
	 * return this as a document, as it's so complex.
	 * it's up to the client to parse and consume this, if they can.
	 *
	 */
	public StcResourceProfile getStcResourceProfile() {
		return this.stcResourceProfile;
	}
	public void setStcResourceProfile(StcResourceProfile stcResourceProfile) {
		this.stcResourceProfile = stcResourceProfile;
	}
	/** list the named spectral regions of the electro-magnetic spectrum 
                  that the resource's spectral coverage overlaps with.
                  @return 'radio','millimeter','infrared','optical','uv','euv','x-ray','gamma-ray' */
	public String[] getWavebands() {
		return this.wavebands;
	}
	public void setWavebands(String[] wavebands) {
		this.wavebands = wavebands;
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
			buffer.append(", footprint = ").append(footprint);
			buffer.append(", stcResourceProfile = ").append(stcResourceProfile);
			buffer.append("]");
			return buffer.toString();
		}
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.footprint == null) ? 0 : this.footprint.hashCode());
		result = PRIME * result + Coverage.hashCode(this.wavebands);
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (! (obj instanceof Coverage))
			return false;
		final Coverage other = (Coverage) obj;
		if (this.footprint == null) {
			if (other.footprint != null)
				return false;
		} else if (!this.footprint.equals(other.footprint))
			return false;
		if (!Arrays.equals(this.wavebands, other.wavebands))
			return false;
		return true;
	}
	
}
