package org.astrogrid.desktop.modules.votech;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

/** represents a static file containing a list of annotations to ingest into the 
 * annotation service.
 * built via hivemind contributions.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 18, 20077:23:44 PM
 */
public class AnnotationSource implements Serializable, Comparable{
	/** the source to load the annotations from */
	protected URI source;
	/** a name for the annotation source */
	protected String name;
	public AnnotationSource(URI source, String name) {
		super();
		this.source = source;
		this.name = name;
	}
	
	
	/** the sort oder for this source. lower the sort order, higher up the list.*/
	private int sortOrder = 2;
	
	
	/**
	 * @return the sortOrder
	 */
	public final int getSortOrder() {
		return this.sortOrder;
	}

	/**
	 * @param sortOrder the sortOrder to set
	 */
	public final void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	public AnnotationSource(String source, String name) throws URISyntaxException {
		super();
		this.source = new URI(source);
		this.name = name;
	}
	
	public AnnotationSource() {
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * @return the source
	 */
	public final URI getSource() {
		return this.source;
	}

	/**
	 * @param name the name to set
	 */
	public final void setName(String name) {
		this.name = name;
	}

	/**
	 * @param source the source to set
	 */
	public final void setSource(URI url) {
		this.source = url;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.source == null) ? 0 : this.source.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AnnotationSource other = (AnnotationSource) obj;
		if (this.source == null) {
			if (other.source != null)
				return false;
		} else if (!this.source.equals(other.source))
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
			buffer.append("AnnotationSource[");
			buffer.append("source = ").append(source);
			buffer.append(", name = ").append(name);
			buffer.append("]");
			return buffer.toString();
		}

	public int compareTo(Object arg0) {
		return sortOrder - ((AnnotationSource)arg0).sortOrder;
	}

}