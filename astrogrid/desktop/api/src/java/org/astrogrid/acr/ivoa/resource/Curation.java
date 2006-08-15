/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.util.Arrays;

/**     Information regarding the general curation of a resource
 * @author Noel Winstanley
 * @since Jul 31, 20063:51:47 PM
 */
public class Curation implements Serializable {

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
	private static final long serialVersionUID = -2940273155884927702L;
	private  ResourceName publisher;
	private  Creator[] creators;
	private  ResourceName[] contributors;
	private  String version;
	private  Contact[] contacts;
	private Date[] dates;
	
	/**  
               Information that can be used for contacting someone with
               regard to this resource.
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
                The entity (e.g. person or organisation) primarily responsible 
                for creating the content or constitution of the resource. */
	public Creator[] getCreators() {
		return this.creators;
	}
	/**                Entity (e.g. person or organisation) responsible for making the 
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
	public void setContacts(Contact[] contacts) {
		this.contacts = contacts;
	}
	public void setContributors(ResourceName[] contributors) {
		this.contributors = contributors;
	}
	public void setCreators(Creator[] creators) {
		this.creators = creators;
	}
	public void setPublisher(ResourceName publisher) {
		this.publisher = publisher;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	/**           Date associated with an event in the life cycle of the
               resource.  
             */
	public Date[] getDates() {
		return this.dates;
	}
	public void setDates(Date[] dates) {
		this.dates = dates;
	}
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Curation other = (Curation) obj;
		if (!Arrays.equals(this.contacts, other.contacts))
			return false;
		if (!Arrays.equals(this.contributors, other.contributors))
			return false;
		if (!Arrays.equals(this.creators, other.creators))
			return false;
		if (!Arrays.equals(this.dates, other.dates))
			return false;
		if (this.publisher == null) {
			if (other.publisher != null)
				return false;
		} else if (!this.publisher.equals(other.publisher))
			return false;
		if (this.version == null) {
			if (other.version != null)
				return false;
		} else if (!this.version.equals(other.version))
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