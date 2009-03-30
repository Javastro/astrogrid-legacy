/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.util.Arrays;

/**     Information regarding the general curation of a resource.
 * @see <a href="http://www.ivoa.net/Documents/PR/ResMetadata/RM-20061212.html">IVOA Registry Metadata Document</a>
 * @author Noel Winstanley
 * @see Resource
 */
public class Curation implements Serializable {

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
	private static final long serialVersionUID = -2940273155884927702L;
	private  ResourceName publisher;
	private  Creator[] creators = new Creator[0];
	private  ResourceName[] contributors= new ResourceName[0];
	private  String version;
	private  Contact[] contacts = new Contact[0];
	private Date[] dates = new Date[0];
	
	/**  
              People who can be contacted about this resource.
              */
	public Contact[] getContacts() {
		return this.contacts;
	}
	/**                Entity responsible for contributions to the content of
               the resource */
	public ResourceName[] getContributors() {
		return this.contributors;
	}
	/** 
                The entity ( person or organisation) primarily responsible 
                for creating the content or constitution of the resource. */
	public Creator[] getCreators() {
		return this.creators;
	}
	/**                The Entity (person or organisation) responsible for making the 
               resource available */
	public ResourceName getPublisher() {
		return this.publisher;
	}
	/**         Label associated with creation or availablilty of a version of 
               a resource.
              */
	public String getVersion() {
		return this.version;
	}
	/** @exclude */
	public void setContacts(final Contact[] contacts) {
		this.contacts = contacts;
	}
    /** @exclude */	
	public void setContributors(final ResourceName[] contributors) {
		this.contributors = contributors;
	}
    /** @exclude */	
	public void setCreators(final Creator[] creators) {
		this.creators = creators;
	}
    /** @exclude */	
	public void setPublisher(final ResourceName publisher) {
		this.publisher = publisher;
	}
    /** @exclude */	
	public void setVersion(final String version) {
		this.version = version;
	}

	/**           Datse associated with an event in the life cycle of the
               resource.  
             */
	public Date[] getDates() {
		return this.dates;
	}
    /** @exclude */	
	public void setDates(final Date[] dates) {
		this.dates = dates;
	}
    /** @exclude */	
	@Override
    public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + Curation.hashCode(this.contacts);
		result = PRIME * result + Curation.hashCode(this.contributors);
		result = PRIME * result + Curation.hashCode(this.creators);
		result = PRIME * result + Curation.hashCode(this.dates);
		result = PRIME * result + ((this.publisher == null) ? 0 : this.publisher.hashCode());
		result = PRIME * result + ((this.version == null) ? 0 : this.version.hashCode());
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
		if (! (obj instanceof Curation)) {
            return false;
        }
		final Curation other = (Curation) obj;
		if (!Arrays.equals(this.contacts, other.contacts)) {
            return false;
        }
		if (!Arrays.equals(this.contributors, other.contributors)) {
            return false;
        }
		if (!Arrays.equals(this.creators, other.creators)) {
            return false;
        }
		if (!Arrays.equals(this.dates, other.dates)) {
            return false;
        }
		if (this.publisher == null) {
			if (other.publisher != null) {
                return false;
            }
		} else if (!this.publisher.equals(other.publisher)) {
            return false;
        }
		if (this.version == null) {
			if (other.version != null) {
                return false;
            }
		} else if (!this.version.equals(other.version)) {
            return false;
        }
		return true;
	}
    /** @exclude */
		@Override
        public String toString() {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("Curation[");
			buffer.append(", publisher = ").append(publisher);
			if (creators == null) {
				buffer.append(", creators = ").append("null");
			} else {
				buffer.append(", creators = ").append(
					Arrays.asList(creators).toString());
			}
			if (contributors == null) {
				buffer.append(", contributors = ").append("null");
			} else {
				buffer.append(", contributors = ").append(
					Arrays.asList(contributors).toString());
			}
			buffer.append(", version = ").append(version);
			if (contacts == null) {
				buffer.append(", contacts = ").append("null");
			} else {
				buffer.append(", contacts = ").append(
					Arrays.asList(contacts).toString());
			}
			if (dates == null) {
				buffer.append(", dates = ").append("null");
			} else {
				buffer.append(", dates = ").append(Arrays.asList(dates).toString());
			}
			buffer.append("]");
			return buffer.toString();
		}

	
	}