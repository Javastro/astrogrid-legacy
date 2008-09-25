/**
 * 
 */
package org.astrogrid.acr.builtin;

import java.net.URL;

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.SecurityException;

/** AR Service: manage user sessions.
 * 
 * There's one default user session - this is the one who's connection 
 * properties are described in {@code ~/.astrogrid-properties}  (for XMLRPC & HTTP) 
 * and to which RMI clients connect to automatically using the Finder.
 * 
 * This SessionManager service allows a client connected to the default user session to 
 * create a new session - which will not share state such as authentication,
 * login information, etc with the default session. This is useful in a multi-user 
 * or server-side settings.
 * 
 * Once a session has been created, call the {@link #findHttpSession(String)}, {@link #findXmlRpcSession(String)}
 * or {@link Finder#findSession(String)} to access the information required to connect to that session.
 * 
 * {@stickyWarning This session manager does not make any strong guarantees about
 * security - the purpose of this feature is to enable multiple user identities within the same AR -
 * not to enforce strict isolation between users of the same AR. In particular, all 
 * clients of the AR must first use the default user session to create a new session, 
 * and it may also be possible to determine how to connect to other sessions - hence
 * it is recommended that all clients connecting to the AR are mutually trusting.
 * }
 * @service builtin.sessionManager
 * @author Noel.Winstanley@manchester.ac.uk
 */
public interface SessionManager {

	/** create a new session.
	 * 
	 * Create a new session that will be available for at least the specified 
	 * number of minutes, after which it will be disposed and inaccessible.
	 * @param leaseMinutes number of minutes that this session will be available for. 
	 * @return a sessionId - an identifier for the newly created session.
	 * @throws NotApplicableException if this AR does not support creating new sessions.
	 * @throws SecurityException if the current user or client is not permitted to create new sessions.
	 */
	String createNewSession(long leaseMinutes) throws NotApplicableException, SecurityException;
	
	/** check whether a session exists and is accessible */
	boolean exists(String sessionId);
	
	/** dispose of a session.
	 * 
	 * @note Once disposed of, a session cannot be connected to.
	 * @param sessionId a sessionId that currently exists.
	 */
	void dispose(String sessionId);

	/** connect to an session using XML-RPC
	 * 
	 * @param sessionId an existing sessionId
	 * @return a full URL that an XML-RPC client should connect to to work with this session. No further manipulation
	 * of this URL is required.
	 * @throws InvalidArgumentException if the sessionId is invalid.
	 */
	URL findXmlRpcSession(String sessionId) throws InvalidArgumentException;
	
	/** connect to an session using Http
	 * 
	 * @param sessionId an existing sessionId
	 * @return a URL that should be used as  the <b>base</b> for HTTP invocations of the services in this session. 
	 * @throws InvalidArgumentException if the sessionId is invalid.
	 */
	URL findHttpSession(String sessionId) throws InvalidArgumentException;
}
