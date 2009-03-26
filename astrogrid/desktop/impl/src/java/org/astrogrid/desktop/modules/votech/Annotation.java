/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import java.io.Serializable;
import java.net.URI;
import java.util.Set;

/** An annotation from an internal source.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 18, 20077:13:12 PM
 */
public class Annotation implements Serializable {
	/** the resourceID this annotation belongs to */
	protected URI resourceId;
	
	/** the owner / source of the annotation */
	protected transient AnnotationSource source;
	
	/** an alternative title for the resource */
	protected String alternativeTitle;
	
	/** a set of tags applied to this resource */
	protected Set tags;
	
	/** a set of arbitrary-length notes applied to this resource */
	protected String note;

	/**
	 * @return the alternativeTitle
	 */
	public final String getAlternativeTitle() {
		return this.alternativeTitle;
	}

	/**
	 * @param alternativeTitle the alternativeTitle to set
	 */
	public final void setAlternativeTitle(final String altTitle) {
		this.alternativeTitle = altTitle;
	}

	/**
	 * @return the notes
	 */
	public final String getNote() {
		return this.note;
	}

	/**
	 * @param notes the notes to set
	 */
	public final void setNote(final String notes) {
		this.note = notes;
	}

	/**
	 * @return the source
	 * @see AnnotationSource#getName()
	 */
	public final AnnotationSource getSource() {
		return this.source;
	}

	/**
	 * @param source the source to set
	 */
	public final void setSource(final AnnotationSource source) {
		this.source = source;
	}

	/**
	 * @return the tags - a list of strings
	 */
	public final Set getTags() {
		return this.tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public final void setTags(final Set tags) {
		this.tags = tags;
	}

	/**
	 * @return the resourceId
	 */
	public final URI getResourceId() {
		return this.resourceId;
	}

	/**
	 * @param resourceId the resourceId to set
	 */
	public final void setResourceId(final URI annotatedResourceId) {
		this.resourceId = annotatedResourceId;
	}

	/**
		 * toString methode: creates a String representation of the object
		 * @return the String representation
	
		 */
		@Override
        public String toString() {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("Annotation[");
			buffer.append("resourceId = ").append(resourceId);
			buffer.append(", source = ").append(source);
			buffer.append(", alternativeTitle = ").append(alternativeTitle);
			if (tags == null) {
				buffer.append(", tags = ").append("null");
			} else {
				buffer.append(", tags = ").append(tags);
			}
			buffer.append(", note = ").append(note);
			buffer.append("]");
			return buffer.toString();
		}
	
	
}
