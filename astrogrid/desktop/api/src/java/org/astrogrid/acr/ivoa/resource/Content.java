/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;

/**   Metadata about the general content of a resource.
 * @see <a href="http://www.ivoa.net/Documents/PR/ResMetadata/RM-20061212.html">Registry Metadata document</a>
 * @author Noel Winstanley
 * @see Resource
 */
public class Content implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8759669056738166514L;
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
	private  Source source;
	private  String[] subject = new String[0];
	private  String description;
	private  URI referenceURI;
	private  String[] type = new String[0];
	private  String[] contentLevel = new String[0];
	private  Relationship[] relationships = new Relationship[0];
	
	/**     Description of the content level (intended audience) for this rsource */
	public String[] getContentLevel() {
		return this.contentLevel;
	}
	/**  An account of the nature of the resource */
	public String getDescription() {
		return this.description;
	}
	/**               URL pointing to a human-readable document describing this 
                resource.  
     @warning will return null for an url with an unknown scheme, or
	 * which is invalid. To access the value then, use {@link #getReferenceURI}
	 * @deprecated use {@link #getReferenceURI}
                  *
                  */
	@Deprecated
    public URL getReferenceURL() {
		try {
			return this.referenceURI == null ? null : this.referenceURI.toURL();
		} catch (final MalformedURLException x) {
			return null;
		}
	}
	/** URI pointing to a human-readable document describing this resource */
	public URI getReferenceURI() {
			return this.referenceURI;
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
	 */
	public String[] getType() {
		return this.type;
	}
/**      a bibliographic reference from which the present resource is 
                derived or extracted.   */
	public Source getSource() {
		return this.source;
	}
	/** @exclude */ 	
public void setContentLevel(final String[] contentLevel) {
	this.contentLevel = contentLevel;
}
/** @exclude */ 
public void setDescription(final String description) {
	this.description = description;
}
/** @exclude */ 
public void setReferenceURI(final URI referenceURL) {
	this.referenceURI = referenceURL;
}
/** @exclude */ 
public void setRelationships(final Relationship[] relationships) {
	this.relationships = relationships;
}
/** @exclude */ 
public void setSource(final Source source) {
	this.source = source;
}
/** @exclude */ 
public void setSubject(final String[] subject) {
	this.subject = subject;
}
/** @exclude */ 
public void setType(final String[] type) {
	this.type = type;
}
/** @exclude */ 
@Override
public int hashCode() {
	final int PRIME = 31;
	int result = 1;
	result = PRIME * result + Content.hashCode(this.contentLevel);
	result = PRIME * result + ((this.description == null) ? 0 : this.description.hashCode());
	result = PRIME * result + ((this.referenceURI == null) ? 0 : this.referenceURI.hashCode());
	result = PRIME * result + Content.hashCode(this.relationships);
	result = PRIME * result + ((this.source == null) ? 0 : this.source.hashCode());
	result = PRIME * result + Content.hashCode(this.subject);
	result = PRIME * result + Content.hashCode(this.type);
	return result;
}
/** @exclude */ 
@Override
public boolean equals(final Object obj) {
	if (this == obj) {
        return true;
    }
	if (obj == null) {
        return false;
    }
	if (! (obj instanceof Content)) {
        return false;
    }
	final Content other = (Content) obj;
	if (!Arrays.equals(this.contentLevel, other.contentLevel)) {
        return false;
    }
	if (this.description == null) {
		if (other.description != null) {
            return false;
        }
	} else if (!this.description.equals(other.description)) {
        return false;
    }
	if (this.referenceURI == null) {
		if (other.referenceURI != null) {
            return false;
        }
	} else if (!this.referenceURI.equals(other.referenceURI)) {
        return false;
    }
	if (!Arrays.equals(this.relationships, other.relationships)) {
        return false;
    }
	if (this.source == null) {
		if (other.source != null) {
            return false;
        }
	} else if (!this.source.equals(other.source)) {
        return false;
    }
	if (!Arrays.equals(this.subject, other.subject)) {
        return false;
    }
	if (!Arrays.equals(this.type, other.type)) {
        return false;
    }
	return true;
}
/** @exclude */ 
	@Override
    public String toString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("Content[");
		buffer.append(", source = ").append(source);
		if (subject == null) {
			buffer.append(", subject = ").append("null");
		} else {
			buffer.append(", subject = ").append(
				Arrays.asList(subject).toString());
		}
		buffer.append(", description = ").append(description);
		buffer.append(", referenceURL = ").append(referenceURI);
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
