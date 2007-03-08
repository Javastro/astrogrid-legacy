/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.util.Arrays;

/**      A description of the relationship between one resource and one or
           more other resources.
 * @author Noel Winstanley
 * @since Jul 31, 20064:11:03 PM
 */
public class Relationship implements Serializable {
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
	private static final long serialVersionUID = 5440894019888381310L;
	private String relationshipType;
	private ResourceName[] relatedResources = new ResourceName[0];
	/** the name of resource that this resource is related to.*/
	public ResourceName[] getRelatedResources() {
		return this.relatedResources;
	}
	/**   the named type of relationship */
	public String getRelationshipType() {
		return this.relationshipType;
	}
	public void setRelatedResources(ResourceName[] relatedResources) {
		this.relatedResources = relatedResources;
	}
	public void setRelationshipType(String relationshipType) {
		this.relationshipType = relationshipType;
	}
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + Relationship.hashCode(this.relatedResources);
		result = PRIME * result + ((this.relationshipType == null) ? 0 : this.relationshipType.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (! (obj instanceof Relationship))
			return false;
		final Relationship other = (Relationship) obj;
		if (!Arrays.equals(this.relatedResources, other.relatedResources))
			return false;
		if (this.relationshipType == null) {
			if (other.relationshipType != null)
				return false;
		} else if (!this.relationshipType.equals(other.relationshipType))
			return false;
		return true;
	}

		public String toString() {
			StringBuffer buffer = new StringBuffer();
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
