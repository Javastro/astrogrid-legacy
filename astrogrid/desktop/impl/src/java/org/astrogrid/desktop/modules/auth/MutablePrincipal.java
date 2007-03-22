package org.astrogrid.desktop.modules.auth;

import java.security.Principal;

/** a principal that starts off unauthenticated, but
 * who later can be replaced by logged in credientials
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 20, 20072:53:37 PM
 */
public class MutablePrincipal implements Principal {
	/**
	 * 
	 */
	public MutablePrincipal() {
		this.p = new UnauthenticatedPrincipal();
	}
	private Principal p;
	
	public void setActualPrincipal(Principal p) {
		this.p = p;
	}
	
	public Principal getActualPrincipal() {
		return p;
	}
	
	public String getName() {
		return p.getName(); 
	}

// important - do not delegate hashCode and equals to p
// as then the identity of this principal will change when it's upgraded,
// with the concequence that it's treated as an entirely new session.
// instead, we use this field to define identity - which lets us change the identity of the session
// if needed
	private Object identity = new Object();


	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final MutablePrincipal other = (MutablePrincipal) obj;
		if (this.identity == null) {
			if (other.identity != null)
				return false;
		} else if (!this.identity.equals(other.identity))
			return false;
		return true;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((this.identity == null) ? 0 : this.identity.hashCode());
		return result;
	}
/** changes the identity of this object - hashCode give different results 
 * 
 * works, in a hacky way, because hivelock seesm to use hashcode to reference 
 *  session-specific components. So if hashcode of the object finds, previous session's stuff
 *  is discarded.
 * 
 * and not vital if this doesn't work  - there's a very small chance that we'll get an object with the 
 * same hashcode - but does give an extra layer of protection when
 * a user logs out within a session that no old stuff is left lying around.
 * */
	public void scrubIdentity() {
		identity = new Object();
	}
	
	public String toString() {
		return this.p.toString();
	}
}