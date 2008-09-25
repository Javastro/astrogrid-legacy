/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.net.URI;
import java.util.Arrays;

/**  Describes part of what a service does.
 * 
 *  A {@link Service} can contain one or more capabilities. Each capability
 *  describes something that the service does, and how to access that functionality.
 * @author Noel Winstanley
 * @see Service
 */
public class Capability implements Serializable {

	/** @exclude */
	private static int hashCode(final Object[] array) {
		final int PRIME = 31;
		if (array == null) {
            return 0;
        }
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
	protected Validation[] validationLevel = new Validation[0];
	protected URI standardID;
	protected String description;
	protected Interface[] interfaces = new Interface[0];
	protected String type;  
	/** 
                  A human-readable description of what this capability 
                  provides as part of the over-all service  */
	public String getDescription() {
		return this.description;
	}
	/**Get the interfaces to this capability.
	 * 
	 * Each interface describes a way of calling the service to access the functionality of this capability */
	public Interface[] getInterfaces() {
		return this.interfaces;
	}
	/**         An identifier that can be used to denote which standard this capability conforms to
	 *   */
	public URI getStandardID() {
		return this.standardID;
	}

	/** @exclude */
	public void setDescription(final String description) {
		this.description = description;
	}
    /** @exclude */	
	public void setInterfaces(final Interface[] interfaces) {
		this.interfaces = interfaces;
	}
    /** @exclude */	
	public void setStandardID(final URI standardID) {
		this.standardID = standardID;
	}
    /** @exclude */	
	public void setValidationLevel(final Validation[] validationLevel) {
		this.validationLevel = validationLevel;
	}
	/**          A numeric grade describing the quality of the
                  capability description and interface.
                  
                  It is  used to indicate the confidence an end-user
                  can put in the resource as part of a VO application
                  or research study. 
                */
	public Validation[] getValidationLevel() {
		return this.validationLevel;
	}
	/** Access the xsi:type of this capability.
	 * 
	 *  This is another method of determining what kind of capability is being described, but prefer {@link #getStandardID()},
	 *  or even just checking what subtype of Capability this instance is.*/
	public String getType() {
		return this.type;
	}
    /** @exclude */	
	public void setType(final String type) {
		this.type = type;
	}
    /** @exclude */	
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
    /** @exclude */	
	public boolean equals(final Object obj) {
		if (this == obj) {
            return true;
        }
		if (obj == null) {
            return false;
        }
		if (!(obj instanceof Capability)) {
            return false;
        }
		final Capability other = (Capability) obj;
		if (this.description == null) {
			if (other.description != null) {
                return false;
            }
		} else if (!this.description.equals(other.description)) {
            return false;
        }
		if (!Arrays.equals(this.interfaces, other.interfaces)) {
            return false;
        }
		if (this.standardID == null) {
			if (other.standardID != null) {
                return false;
            }
		} else if (!this.standardID.equals(other.standardID)) {
            return false;
        }
		if (this.type == null) {
			if (other.type != null) {
                return false;
            }
		} else if (!this.type.equals(other.type)) {
            return false;
        }
		if (!Arrays.equals(this.validationLevel, other.validationLevel)) {
            return false;
        }
		return true;
	}
    /** @exclude */
		public String toString() {
			final StringBuffer buffer = new StringBuffer();
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
