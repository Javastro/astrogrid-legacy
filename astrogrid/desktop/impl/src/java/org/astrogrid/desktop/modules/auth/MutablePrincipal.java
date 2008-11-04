package org.astrogrid.desktop.modules.auth;

import java.security.Principal;

/** a principal that can be mutated.
 * <p/>
 * It starts off unauthenticated, but
 * who later can be replaced by logged in credientials
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 20, 20072:53:37 PM
 * @TEST equals.
 */
public class MutablePrincipal implements Principal {
	/**
	 * 
	 */
	public MutablePrincipal() {
		this.p = new UnauthenticatedPrincipal();
	}
	private Principal p;
	
	public void setActualPrincipal(final Principal p) {
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
	private final Object identity = new Object();


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
		final MutablePrincipal other = (MutablePrincipal) obj;
		if (this.identity == null) {
			if (other.identity != null) {
                return false;
            }
		} else if (!this.identity.equals(other.identity)) {
            return false;
        }
		return true;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((this.identity == null) ? 0 : this.identity.hashCode());
		return result;
	}

	
	public String toString() {
		return this.p.toString();
	}
}