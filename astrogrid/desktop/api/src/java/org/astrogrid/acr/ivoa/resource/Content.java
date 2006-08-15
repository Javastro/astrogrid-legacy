/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.net.URL;
import java.util.Arrays;

/**    Information regarding the general content of a resource
 * @author Noel Winstanley
 * @since Jul 31, 20064:04:51 PM
 */
public class Content implements Serializable {
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
	private static final long serialVersionUID = 4108094414678499781L;
	/**
	 * 
	 */
	private  Source source;
	private  String[] subject;
	private  String description;
	private  URL referenceURL;
	private  String[] type;
	private  String[] contentLevel;
	private  Relationship[] relationships;
	
	/**     Description of the content level or intended audience */
	public String[] getContentLevel() {
		return this.contentLevel;
	}
	/**  An account of the nature of the resource */
	public String getDescription() {
		return this.description;
	}
	/**               URL pointing to a human-readable document describing this 
                resource.    */
	public URL getReferenceURL() {
		return this.referenceURL;
	}
	/**    a description of a relationship to another resource.  
              */
	public Relationship[] getRelationships() {
		return this.relationships;
	}
	/**                a topic, object type, or other descriptive keywords 
               about the resource.   */
	public String[] getSubject() {
		return this.subject;
	}
	/**     Nature or genre of the content of the resource 
	 * 
	 * @return one of an enumeration of 
	 */
	public String[] getType() {
		return this.type;
	}
/**      a bibliographic reference from which the present resource is 
                derived or extracted.   */
	public Source getSource() {
		return this.source;
	}
public void setContentLevel(String[] contentLevel) {
	this.contentLevel = contentLevel;
}
public void setDescription(String description) {
	this.description = description;
}
public void setReferenceURL(URL referenceURL) {
	this.referenceURL = referenceURL;
}
public void setRelationships(Relationship[] relationships) {
	this.relationships = relationships;
}
public void setSource(Source source) {
	this.source = source;
}
public void setSubject(String[] subject) {
	this.subject = subject;
}
public void setType(String[] type) {
	this.type = type;
}
public int hashCode() {
	final int PRIME = 31;
	int result = 1;
	result = PRIME * result + Content.hashCode(this.contentLevel);
	result = PRIME * result + ((this.description == null) ? 0 : this.description.hashCode());
	result = PRIME * result + ((this.referenceURL == null) ? 0 : this.referenceURL.hashCode());
	result = PRIME * result + Content.hashCode(this.relationships);
	result = PRIME * result + ((this.source == null) ? 0 : this.source.hashCode());
	result = PRIME * result + Content.hashCode(this.subject);
	result = PRIME * result + Content.hashCode(this.type);
	return result;
}
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	final Content other = (Content) obj;
	if (!Arrays.equals(this.contentLevel, other.contentLevel))
		return false;
	if (this.description == null) {
		if (other.description != null)
			return false;
	} else if (!this.description.equals(other.description))
		return false;
	if (this.referenceURL == null) {
		if (other.referenceURL != null)
			return false;
	} else if (!this.referenceURL.equals(other.referenceURL))
		return false;
	if (!Arrays.equals(this.relationships, other.relationships))
		return false;
	if (this.source == null) {
		if (other.source != null)
			return false;
	} else if (!this.source.equals(other.source))
		return false;
	if (!Arrays.equals(this.subject, other.subject))
		return false;
	if (!Arrays.equals(this.type, other.type))
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
		buffer.append("Content[");
		buffer.append(", source = ").append(source);
		if (subject == null) {
			buffer.append(", subject = ").append("null");
		} else {
			buffer.append(", subject = ").append(
				Arrays.asList(subject).toString());
		}
		buffer.append(", description = ").append(description);
		buffer.append(", referenceURL = ").append(referenceURL);
		if (type == null) {
			buffer.append(", type = ").append("null");
		} else {
			buffer.append(", type = ").append(Arrays.asList(type).toString());
		}
		if (contentLevel == null) {
			buffer.append(", contentLevel = ").append("null");
		} else {
			buffer.append(", contentLevel = ").append(
				Arrays.asList(contentLevel).toString());
		}
		if (relationships == null) {
			buffer.append(", relationships = ").append("null");
		} else {
			buffer.append(", relationships = ").append(
				Arrays.asList(relationships).toString());
		}
		buffer.append("]");
		return buffer.toString();
	}
	
}
