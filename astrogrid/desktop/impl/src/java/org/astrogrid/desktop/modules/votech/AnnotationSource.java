package org.astrogrid.desktop.modules.votech;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

/** A pointer to a static file containing a list of annotations to ingest into the 
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
	public AnnotationSource(final URI source, final String name) {
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
	public final void setSortOrder(final int sortOrder) {
		this.sortOrder = sortOrder;
	}

	public AnnotationSource(final String source, final String name) throws URISyntaxException {
		super();
		this.source = new URI(source);
		this.name = name;
	}

	/**
     * 
     */
    protected AnnotationSource() {
        // called by subclasses.
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
	public final void setName(final String name) {
		this.name = name;
	}

	/**
	 * @param source the source to set
	 */
	public final void setSource(final URI url) {
		this.source = url;
	}

	@Override
    public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.source == null) ? 0 : this.source.hashCode());
		return result;
	}

	@Override
    public boolean equals(final Object obj) {
		if (this == obj) {
            return true;
        }
		if (obj == null) {
            return false;
        }
		if (getClass() != obj.getClass()) {
            return false;
        }
		final AnnotationSource other = (AnnotationSource) obj;
		if (this.source == null) {
			if (other.source != null) {
                return false;
            }
		} else if (!this.source.equals(other.source)) {
            return false;
        }
		return true;
	}

	/**
		 * toString methode: creates a String representation of the object
		 * @return the String representation
		 * @author info.vancauwenberge.tostring plugin
	
		 */
		@Override
        public String toString() {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("AnnotationSource[");
			buffer.append("source = ").append(source);
			buffer.append(", name = ").append(name);
			buffer.append("]");
			return buffer.toString();
		}

	public int compareTo(final Object arg0) {
		return sortOrder - ((AnnotationSource)arg0).sortOrder;
	}

}