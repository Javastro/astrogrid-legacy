/**
 * 
 */
package org.astrogrid.desktop.framework;

import java.security.Principal;

import org.astrogrid.acr.builtin.SessionManager;

/** Keeps track of multiple sessions in the AR.
 * each session may start either authenticated or unauthenticated.
 * Unauthenticated sessions may upgrade by authenticating later.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 20, 20076:40:12 PM
 */
public interface SessionManagerInternal extends SessionManager{
	/** attempt to login and authenticate the principal currently associated with
	 * this thread.
	 * if successful, will upgrade the principal
	 * Else will leave it unchanged.
	 */
	void attemptUpgrade() ;
	
	
	/** locate the session associated with a particular key */
	Principal findSessionForKey(String sessionId);
	
	/** adopt the current thread onto the specified  session */
	void adoptSession(Principal p);
	
	/** clear the current thread from the specified session */
	void clearSession();
	
	/** reutrn the session associated with the current thread - may be null */
	Principal currentSession();
	
	/** id of the default session */
	String getDefaultSessionId();
}
