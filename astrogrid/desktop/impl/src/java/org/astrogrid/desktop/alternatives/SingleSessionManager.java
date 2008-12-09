/**
 * 
 */
package org.astrogrid.desktop.alternatives;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.SwingUtilities;

import net.sourceforge.hivelock.SecurityService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.desktop.framework.SessionManagerInternal;
import org.astrogrid.desktop.modules.auth.MutablePrincipal;
import org.astrogrid.desktop.modules.auth.SessionManagerImpl;
import org.astrogrid.desktop.modules.system.WebServerInternal;

/** Simple session manager that doesn't allow more than a single default session.
 * Used as a baseclass for the full {@link SessionManagerImpl}
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 21, 200710:05:40 AM
 */
public class SingleSessionManager implements SessionManagerInternal {

	protected static final Log logger = LogFactory
			.getLog(SessionManagerInternal.class);

	public SingleSessionManager(final SecurityService ss, final WebServerInternal ws) {
		super();
		this.ss = ss;
		this.ws = ws;
		// now create a default session, and attach it to the current thread, 
		// and the edt thread, if it's different to this thread and available.
		
		// providing an unauthenticatedPrincipal object to the security manager
		//means that we get consulted when a permissions check is performed
		// and at that point can intercept and login, upgrading that session's permissions.
		
		// if the current user in the security manager is left as null, then it considers this
		// an unauthenticated user, and the attempt fails before it gets to us.
		
		defaultSessionId = createNewSession();
		final Principal defaultSession = findSessionForKey(defaultSessionId);
		ss.login(defaultSession);
		if (! SwingUtilities.isEventDispatchThread()) {
			// am waiting here - to make sure that the session is set.
		try {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					ss.setCurrentUser(defaultSession);
				}
			});
		} catch(final Throwable t) { // too bad.
			logger.warn("Failed to set default session on EDT",t);
		}
		}		
	}	
	protected final String defaultSessionId;
	protected final SecurityService ss;
	protected final WebServerInternal ws;
	protected final Map<String, MutablePrincipal> sessionMap = new HashMap<String, MutablePrincipal>(); // barely used in htis class, but useful for subclasses.
	
	// create a new unlimited session
	protected String createNewSession()  {
		final String sid = generateSessionId();
		sessionMap.put(sid,new MutablePrincipal());
		return sid;
	}	
	
	/* generates a random string - check it's not a session that's already present. */
	protected String generateSessionId() {
		String sid;
		do {
			final Random r = new Random();
			sid =  Long.toString(Math.abs(r.nextLong()),16);
		} while (sessionMap.containsKey(sid));
		return sid;
	}	
	
//	 session Internal interface.

	public void attemptUpgrade(){
		// does nothing.
	}

	public void adoptSession(final Principal p) {
		ss.setCurrentUser(p);
	}
	
	public void clearSession() {
		ss.clearCurrentUser();
	}

	public Principal currentSession() {
		return ss.getCurrentUser();
	}

	public String getDefaultSessionId() {
		return defaultSessionId;
	}

	public Principal findSessionForKey(final String key) {
		return sessionMap.get(key);
	}

	public String createNewSession(final long arg0) throws NotApplicableException,
			SecurityException {
		throw new NotApplicableException("Multiple sessions not supported in this AR");
	}

	public void dispose(final String arg0)  {
		// does nothing.
	}	
	
	public boolean exists(final String arg0) {
		return sessionMap.containsKey(arg0);
	}

	public URL findHttpSession(final String arg0) throws InvalidArgumentException {
		if (! defaultSessionId.equals(arg0)) { // no other sessions.
			throw new InvalidArgumentException(arg0);
		}
		return ws.getRoot();
	}


	public URL findXmlRpcSession(final String arg0) throws InvalidArgumentException {
		if (! defaultSessionId.equals(arg0)) { // no other sessions.
			throw new InvalidArgumentException(arg0);
		}
		try {
			return new URL(ws.getRoot(),"xmlrpc");
		} catch (final MalformedURLException x) {
			// unlikely.
			throw new InvalidArgumentException(x);
		}		
	}



}
