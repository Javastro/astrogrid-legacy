/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.util.Arrays;

/** registry search capability..
 * @author Noel Winstanley
 * @since Aug 5, 200610:20:50 PM
 */
public class SearchCapability extends Capability {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6972269072498293663L;
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
	private int maxRecords;
	private String extensionSearchSupport;
	private String[] optionalProtocol;
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((this.extensionSearchSupport == null) ? 0 : this.extensionSearchSupport.hashCode());
		result = PRIME * result + this.maxRecords;
		result = PRIME * result + SearchCapability.hashCode(this.optionalProtocol);
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SearchCapability other = (SearchCapability) obj;
		if (this.extensionSearchSupport == null) {
			if (other.extensionSearchSupport != null)
				return false;
		} else if (!this.extensionSearchSupport.equals(other.extensionSearchSupport))
			return false;
		if (this.maxRecords != other.maxRecords)
			return false;
		if (!Arrays.equals(this.optionalProtocol, other.optionalProtocol))
			return false;
		return true;
	}
	public String getExtensionSearchSupport() {
		return this.extensionSearchSupport;
	}
	public void setExtensionSearchSupport(String extensionSearchSupport) {
		this.extensionSearchSupport = extensionSearchSupport;
	}
	public int getMaxRecords() {
		return this.maxRecords;
	}
	public void setMaxRecords(int maxRecords) {
		this.maxRecords = maxRecords;
	}
	public String[] getOptionalProtocol() {
		return this.optionalProtocol;
	}
	public void setOptionalProtocol(String[] optionalProtocol) {
		this.optionalProtocol = optionalProtocol;
	}
	/**
		 * toString methode: creates a String representation of the object
		 * @return the String representation
		 * @author info.vancauwenberge.tostring plugin
	
		 */
		public String toString() {
			StringBuffer buffer = new StringBuffer();
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
