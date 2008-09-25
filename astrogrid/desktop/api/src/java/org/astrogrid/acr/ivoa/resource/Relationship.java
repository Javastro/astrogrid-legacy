/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.util.Arrays;

/**      A relationship between one resource and one or
           more other resources.
 * @author Noel Winstanley
 * @see Content
 */
public class Relationship implements Serializable {
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
	private static final long serialVersionUID = 5440894019888381310L;
	private String relationshipType;
	private ResourceName[] relatedResources = new ResourceName[0];
	/** Resources that this resource is related to.*/
	public ResourceName[] getRelatedResources() {
		return this.relatedResources;
	}
	/**   the type of relationship */
	public String getRelationshipType() {
		return this.relationshipType;
	}
	/** @exclude */
	public void setRelatedResources(final ResourceName[] relatedResources) {
		this.relatedResources = relatedResources;
	}
    /** @exclude */	
	public void setRelationshipType(final String relationshipType) {
		this.relationshipType = relationshipType;
	}
    /** @exclude */	
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + Relationship.hashCode(this.relatedResources);
		result = PRIME * result + ((this.relationshipType == null) ? 0 : this.relationshipType.hashCode());
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
		if (! (obj instanceof Relationship)) {
            return false;
        }
		final Relationship other = (Relationship) obj;
		if (!Arrays.equals(this.relatedResources, other.relatedResources)) {
            return false;
        }
		if (this.relationshipType == null) {
			if (other.relationshipType != null) {
                return false;
            }
		} else if (!this.relationshipType.equals(other.relationshipType)) {
            return false;
        }
		return true;
	}
    /** @exclude */
		public String toString() {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("Relationship[");
			buffer.append(", relationshipType = ").append(relationshipType);
			if (relatedResources == null) {
				buffer.append(", relatedResources = ").append("null");
			} else {
				buffer.append(", relatedResources = ").append(
					Arrays.asList(relatedResources).toString());
			}
			buffer.append("]");
			return buffer.toString();
		}
	
	

}
