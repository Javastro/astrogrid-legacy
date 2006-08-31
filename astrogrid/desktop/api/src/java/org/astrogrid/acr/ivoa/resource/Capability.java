/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.net.URI;
import java.util.Arrays;

/**    a description of what the service does (in terms of 
            context-specific behavior), and how to use it (in terms of
            an interface)
 * @author Noel Winstanley
 * @since Jul 31, 20064:25:09 PM
 */
public class Capability implements Serializable {

	
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
	/**
	 * 
	 */
	private static final long serialVersionUID = 8278238516274181324L;
	/**
	 * 
	 */
	protected Validation[] validationLevel;
	protected URI standardID;
	protected String description;
	protected Interface[] interfaces;
	protected String type; 
	/** 
                  A human-readable description of what this capability 
                  provides as part of the over-all service  */
	public String getDescription() {
		return this.description;
	}
	/** 
                  a description of how to call the service to access
                  this capability */
	public Interface[] getInterfaces() {
		return this.interfaces;
	}
	/**         A URI identifier for a standard service.  */
	public URI getStandardID() {
		return this.standardID;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	public void setInterfaces(Interface[] interfaces) {
		this.interfaces = interfaces;
	}
	public void setStandardID(URI standardID) {
		this.standardID = standardID;
	}
	public void setValidationLevel(Validation[] validationLevel) {
		this.validationLevel = validationLevel;
	}
	/**          A numeric grade describing the quality of the
                  capability description and interface, when applicable, 
                  to be used to indicate the confidence an end-user
                  can put in the resource as part of a VO application
                  or research study. 
                */
	public Validation[] getValidationLevel() {
		return this.validationLevel;
	}
	/** access the xsi:type of this element */
	public String getType() {
		return this.type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.description == null) ? 0 : this.description.hashCode());
		result = PRIME * result + Capability.hashCode(this.interfaces);
		result = PRIME * result + ((this.standardID == null) ? 0 : this.standardID.hashCode());
		result = PRIME * result + ((this.type == null) ? 0 : this.type.hashCode());
		result = PRIME * result + Capability.hashCode(this.validationLevel);
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Capability other = (Capability) obj;
		if (this.description == null) {
			if (other.description != null)
				return false;
		} else if (!this.description.equals(other.description))
			return false;
		if (!Arrays.equals(this.interfaces, other.interfaces))
			return false;
		if (this.standardID == null) {
			if (other.standardID != null)
				return false;
		} else if (!this.standardID.equals(other.standardID))
			return false;
		if (this.type == null) {
			if (other.type != null)
				return false;
		} else if (!this.type.equals(other.type))
			return false;
		if (!Arrays.equals(this.validationLevel, other.validationLevel))
			return false;
		return true;
	}

		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("Capability[");
			if (validationLevel == null) {
				buffer.append(", validationLevel = ").append("null");
			} else {
				buffer.append(", validationLevel = ").append(
					Arrays.asList(validationLevel).toString());
			}
			buffer.append(", standardID = ").append(standardID);
			buffer.append(", description = ").append(description);
			if (interfaces == null) {
				buffer.append(", interfaces = ").append("null");
			} else {
				buffer.append(", interfaces = ").append(
					Arrays.asList(interfaces).toString());
			}
			buffer.append(", type = ").append(type);
			buffer.append("]");
			return buffer.toString();
		}
}
