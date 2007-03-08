/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import java.io.Serializable;
import java.net.URI;
import java.util.Arrays;

/** 
            A description of a service interface.
 * @author Noel Winstanley
 * @since Jul 31, 20064:42:09 PM
 */
public class Interface implements Serializable {

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
	private static final long serialVersionUID = -1615855805795573431L;
	private  String version = "1.0";
	private  String role;
	private AccessURL[] accessUrls;
	private  SecurityMethod[] securityMethods;
	private String type;
	/**                  The URL (or base URL) that a client uses to access the
                 service.  How this URL is to be interpreted and used 
                 depends on the specific Interface subclass
               */
	public AccessURL[] getAccessUrls() {
		return this.accessUrls;
	}
	/**        A tag name the identifies the role the interface plays
               in the particular capability.  If the value is equal to
               "std" or begins with "std:", then the interface refers
               to a standard interface defined by the standard
               referred to by the capability's standardID attribute. */
	public String getRole() {
		return this.role;
	}
	/**          the mechanism the client must employ to gain secure
                  access to the service.  */
	public SecurityMethod[] getSecurityMethods() {
		return this.securityMethods;
	}
	/**   The version of a standard interface specification that this 
               interface complies with.  When the interface is
               provided in the context of a Capability element, then
               the standard being refered to is the one identified by
               the Capability's standardID element.  If the standardID
               is not provided, the meaning of this attribute is
               undefined.  
            */
	public String getVersion() {
		return this.version;
	}
	public void setAccessUrls(AccessURL[] accessUrls) {
		this.accessUrls = accessUrls;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public void setSecurityMethods(SecurityMethod[] securityMethods) {
		this.securityMethods = securityMethods;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	/** access the xsi:type of this element */
	public String getType() {
		return this.type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + Interface.hashCode(this.accessUrls);
		result = PRIME * result + ((this.role == null) ? 0 : this.role.hashCode());
		result = PRIME * result + Interface.hashCode(this.securityMethods);
		result = PRIME * result + ((this.type == null) ? 0 : this.type.hashCode());
		result = PRIME * result + ((this.version == null) ? 0 : this.version.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (! (obj instanceof Interface))
			return false;
		final Interface other = (Interface) obj;
		if (!Arrays.equals(this.accessUrls, other.accessUrls))
			return false;
		if (this.role == null) {
			if (other.role != null)
				return false;
		} else if (!this.role.equals(other.role))
			return false;
		if (!Arrays.equals(this.securityMethods, other.securityMethods))
			return false;
		if (this.type == null) {
			if (other.type != null)
				return false;
		} else if (!this.type.equals(other.type))
			return false;
		if (this.version == null) {
			if (other.version != null)
				return false;
		} else if (!this.version.equals(other.version))
			return false;
		return true;
	}
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("Interface[");
			buffer.append(", version = ").append(version);
			buffer.append(", role = ").append(role);
			if (accessUrls == null) {
				buffer.append(", accessUrls = ").append("null");
			} else {
				buffer.append(", accessUrls = ").append(
					Arrays.asList(accessUrls).toString());
			}
			if (securityMethods == null) {
				buffer.append(", securityMethods = ").append("null");
			} else {
				buffer.append(", securityMethods = ").append(
					Arrays.asList(securityMethods).toString());
			}
			buffer.append(", type = ").append(type);
			buffer.append("]");
			return buffer.toString();
		}
	
}
