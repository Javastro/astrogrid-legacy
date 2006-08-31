/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.net.URL;

/**       The entity (e.g. person or organisation) primarily responsible 
            for creating something
         
 * @author Noel Winstanley
 * @since Jul 31, 20065:03:49 PM
 */
public class Creator implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4484705219406811935L;
	private  ResourceName name;
	private  URL logo;
	
	/** url pointing to a logo for this entity */
	public URL getLogo() {
		return this.logo;
	}
	/** name of the creator */
	public ResourceName getName() {
		return this.name;
	}
	public void setLogo(URL logo) {
		this.logo = logo;
	}
	public void setName(ResourceName name) {
		this.name = name;
	}
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.logo == null) ? 0 : this.logo.hashCode());
		result = PRIME * result + ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Creator other = (Creator) obj;
		if (this.logo == null) {
			if (other.logo != null)
				return false;
		} else if (!this.logo.equals(other.logo))
			return false;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		return true;
	}

		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("Creator[");
			buffer.append(", name = ").append(name);
			buffer.append(", logo = ").append(logo);
			buffer.append("]");
			return buffer.toString();
		}

}
