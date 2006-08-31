/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;

/**          Information that can be used for contacting someone
 * @author Noel Winstanley
 * @since Jul 31, 20064:02:04 PM
 */
public class Contact implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5344411623724464318L;
	private ResourceName name;
	private String address;
	private String email;
	private String telephone;
	/** the contact mailing address */
	public String getAddress() {
		return this.address;
	}
	/** the contact email address */
	public String getEmail() {
		return this.email;
	}
	/**              the name or title of the contact person. */
	public ResourceName getName() {
		return this.name;
	}
	/** the contact telephone number */
	public String getTelephone() {
		return this.telephone;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setName(ResourceName name) {
		this.name = name;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.address == null) ? 0 : this.address.hashCode());
		result = PRIME * result + ((this.email == null) ? 0 : this.email.hashCode());
		result = PRIME * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = PRIME * result + ((this.telephone == null) ? 0 : this.telephone.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Contact other = (Contact) obj;
		if (this.address == null) {
			if (other.address != null)
				return false;
		} else if (!this.address.equals(other.address))
			return false;
		if (this.email == null) {
			if (other.email != null)
				return false;
		} else if (!this.email.equals(other.email))
			return false;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		if (this.telephone == null) {
			if (other.telephone != null)
				return false;
		} else if (!this.telephone.equals(other.telephone))
			return false;
		return true;
	}

		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("Contact[");
			buffer.append(", name = ").append(name);
			buffer.append(", address = ").append(address);
			buffer.append(", email = ").append(email);
			buffer.append(", telephone = ").append(telephone);
			buffer.append("]");
			return buffer.toString();
		}

	
}
