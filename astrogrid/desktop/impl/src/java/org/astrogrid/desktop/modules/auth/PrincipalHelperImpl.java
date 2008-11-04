/**
 * 
 */
package org.astrogrid.desktop.modules.auth;

import java.security.Principal;

import net.sourceforge.hivelock.PrincipalHelperService;

import org.astrogrid.desktop.framework.SessionManagerInternal;

/** Plugin to hivemind user management system.
 * Bridges the gap between hivemind and AR.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 20, 20076:35:42 PM
 */
public class PrincipalHelperImpl implements PrincipalHelperService {
/**
 * 
 */
public PrincipalHelperImpl(final SessionManagerInternal sess) {
	this.sess = sess;
}
private final SessionManagerInternal sess;

	// naieve treatment of authenticated principles for now.
	// later, might be necessary to have different roles (and so different abilities, and access rights) for different classes of principal.
	
	public String getLogin(final Principal arg0) {
		final Principal p = getTruePrincipal(arg0);
		if (p instanceof UnauthenticatedPrincipal) {
			return ((UnauthenticatedPrincipal)p).getLogin();
		} else {
			return p.toString();
		}
	}

	public String getName(final Principal arg0) {
		final Principal p = getTruePrincipal(arg0);
		if (p instanceof UnauthenticatedPrincipal) {
			return ((UnauthenticatedPrincipal)p).getName();
		} else {
			return p.toString();
		}
	}

	public String[] getRoles(final Principal arg0) {
		final Principal p = getTruePrincipal(arg0);
		if (p instanceof UnauthenticatedPrincipal) {
			return ((UnauthenticatedPrincipal)p).getRoles();
		} else {
			return AUTHENTICATED_ROLES; 
		}
	}
	
	private static final String[] AUTHENTICATED_ROLES = new String[] {"loggedin"};
	
	/** assumes the parameter principal is a mutable principal.
	 * if the wrapped principal is an unauthenticatedPrincipal, it calls doLogin() to try to upgrade, and 
	 * if successful stores the result back in mutable principal
	 * @param p a mutablePrincipal
	 * @return mutablePrincipal.getActualPrincipal()
	 */
	private Principal getTruePrincipal(final Principal p) {
		if (p instanceof MutablePrincipal) {
			final MutablePrincipal mutablePrincipal = ((MutablePrincipal)p);
			if (mutablePrincipal.getActualPrincipal() instanceof UnauthenticatedPrincipal) {
				sess.attemptUpgrade(); // will mutate mutablePrincipal in place.
			}
			return mutablePrincipal.getActualPrincipal();
		}
		throw new IllegalArgumentException("Expected a mutablePrincipal, got a " + p.getClass().getName());
	}


}
