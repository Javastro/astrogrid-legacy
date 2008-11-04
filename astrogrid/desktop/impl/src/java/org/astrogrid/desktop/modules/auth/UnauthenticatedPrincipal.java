package org.astrogrid.desktop.modules.auth;

import java.security.Principal;

/** 
 * an unauthenticated session. 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 20, 20072:57:35 PM
 */
public  class UnauthenticatedPrincipal implements Principal {
	private static int sessionId;
	private static final String[] UNAUTHENTICATED_ROLES = new String[]{"unauthenticated"};	
	/**
	 * 
	 */
	public  UnauthenticatedPrincipal() {
			// ensures each session id is uniqiue.
			synchronized(UnauthenticatedPrincipal.class) {
				this.name = "Unauthenticated-session-" + sessionId++;
			}
	}
	private final String name;
	public String getName() {
		return name; 
	}
	
	// unsure when this one is called.
	public String getLogin() {
		return name;
	}
	
	public String[] getRoles() {
		return UNAUTHENTICATED_ROLES;
	}
	
	public String toString() {
	    return getName();
	}
}