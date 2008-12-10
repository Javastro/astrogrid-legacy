/**
 * 
 */
package org.astrogrid.acr.builtin;

import java.net.URL;

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.system.Configuration;

/** AR Service: Create and manage sessions.
 * <p/>
 * 
 * When using the Astro Runtime in a multi-user or server-side setting, it may be necessary to have more than one user 
 * simultaneously interacting with the AR. Without some additional mechanism, all users would share the same authentication and program state
 * - in particular, 2 users could not be simultaneously logged in as different identities.
 * <p/>
 * The {@code SessionManager} allows the programmer to create new <i>sessions</i>. Each user session contains the authentication
 * and program state of a single user. More than one session may exist simultaneously within the AR, and the state within sessions
 * are isolated from each other. By creating a new session for each user, multiple users with different identities can access a single
 * AR. 
 * </p>
 * By default, all AR api calls are made within the <i>default session</i>.
 * The  connection 
 * properties for this session are described in {@code ~/.astrogrid-properties}  (for XMLRPC & HTTP) 
 * and this is the session that RMI clients connect to automatically using the {@link Finder}.
 * <p/>
 * From the default session, a <i>new</i> user session can be created by calling {@link #createNewSession(long)}. A unique <i>session identifier</i>
 * is returned. A session can be checked for availability using {@link #exists(String)} and disposed of using {@link #dispose(String)}.
 * <p/>
 * Once a session has been created, it must be <i>connected</i> to. The connection method depends on how the AR is being accessed:
 * <dl>
 * <dt>XMLRPC<dt>
 * <dd>Pass the session identifier to {@link #findXmlRpcSession(String)}, which returns a URL that can then be connected to using an XMLRPC library</dd>
 * <dt>HTTP</dt>
 * <dd>Pass the session identifier to {@link #findHttpSession(String)}, which returns the base URL for performing HTTP function calls in this session</dd>
 * <dt>RMI</dt>
 * <dd>Pass the session identifier to {@link Finder#findSession(String)}, which returns an instance of {@link ACR} for this session</dd>
 * </dl>
 * 
 * No matter how the session is accessed, it presents an unaltered AR API - it's just that
 * user authentication and state is isolated from other sessions. Not all data is isolated
 * - in particular cached information (such as registry entries), and configuration settings
 * (accessed from {@link Configuration}) are shared between all sessions. 
 * 
 * {@stickyWarning This session manager does not make any strong guarantees about
 * security - the purpose of this feature is to enable multiple user identities within the same AR -
 * not to enforce strict isolation between users of the same AR. In particular, all 
 * clients of the AR must first use the default user session to create a new session, 
 * and it may also be possible to determine how to connect to other sessions - hence
 * it is recommended that all clients connecting to the AR are mutually trusting.
 * }
 * 
 * {@example "Python XMLRPC Example"
# connect to the AR (in the default session)
from xmlrpc import Server
from os.path import expanduser
ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
sm = ar.builtin.sessionManager

# create a new user session (with timeout of 10 mins)
sessID = sm.createNewSession(10)

#find xmlrpc endpoint for the new session
sessURL = sm.findXmlRpcSession(sessID)

#connect to the new session
sessAR = Server(sessURL)

#work within the neww session
sessAR.astrogrid.community.login("username","password","community")
...

}
 * @service builtin.sessionManager
 * @author Noel.Winstanley@manchester.ac.uk
 */
public interface SessionManager {

	/** create a new session.
	 * 
	 * @param inactivityTimeout number of minutes that this session can be inactive before it is disposed. 
	 * @return a sessionId - an identifier for the newly created session.
	 * @throws NotApplicableException if this AR does not support creating new sessions.
	 * @throws SecurityException if the current user or client is not permitted to create new sessions.
	 */
	String createNewSession(long inactivityTimeout) throws NotApplicableException, SecurityException;
	
	/** check whether a session exists and is accessible */
	boolean exists(String sessionId);
	
	/** dispose of a session.
	 * 
	 * @note Once disposed of, a session cannot be connected to.
	 * @param sessionId a sessionId that currently exists. If this is the id of the default session, nothing happens.
	 */
	void dispose(String sessionId);

	/** Find the endpoint to connect to a session using XML-RPC
	 * 
	 * @param sessionId an existing sessionId
	 * @return a full URL that an XML-RPC client should connect to to work with this session. No further manipulation
	 * of this URL is required.
	 * @throws InvalidArgumentException if the sessionId is invalid.
	 */
	URL findXmlRpcSession(String sessionId) throws InvalidArgumentException;
	
	/** Find the endpoint to connect to a session using HTTP
	 * 
	 * @param sessionId an existing sessionId
	 * @return a URL that should be used as  the <b>base</b> for HTTP invocations of the services in this session. 
	 * @throws InvalidArgumentException if the sessionId is invalid.
	 */
	URL findHttpSession(String sessionId) throws InvalidArgumentException;
}
