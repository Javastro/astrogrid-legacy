/**
 * 
 */
package org.astrogrid.desktop.modules.auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;

import net.sourceforge.hivelock.SecurityService;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.desktop.alternatives.SingleSessionManager;
import org.astrogrid.desktop.modules.system.SchedulerInternal;
import org.astrogrid.desktop.modules.system.WebServerInternal;
import org.joda.time.Duration;

/** manages sessions and plugs ag community into the hivelock system.
 * 
 * reason that the sessionManager and community are 2 classes is that there's only 
 * ever one of these created, while the community instance is created per session.
 * 
 * however, login attempts may start from either sessionManager (on demand), 
 * or from community (instigated through ui, or AR). So the code takes the pattern
 * of co-routines.
 * 
 * starts with a single session.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 20, 20072:39:22 PM
 */
public class SessionManagerImpl  extends SingleSessionManager implements UserLoginListener {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(SessionManagerImpl.class);

	public SessionManagerImpl(SecurityService s, WebServerInternal ws,CommunityInternal comm, SchedulerInternal scheduler) {
		super(s,ws);
		this.scheduler = scheduler;
		this.comm = comm;
		
	}
	private final SchedulerInternal scheduler;
	private final CommunityInternal comm;
	
	public void attemptUpgrade(){	
	    logger.debug("Attempting upgrade of " + ss.getCurrentUser());
		comm.guiLogin();		
		logger.debug("Called comm.guiLogin(), returning");
		// and now wait for the callback, if any.
	}

	// listener interface 
	// callback from community - either triggered by attemptUpgrade(), or oone of the methods in community.
	public void userLogin(UserLoginEvent arg0) {
                // This principal is non-null if the community object has signed on
                // at a community service. It is of the form ivo://user@authority/resource-key.
                Principal p = comm.getSecurityGuard().getAccountIvorn();
		if (p != null) {
			MutablePrincipal mp = (MutablePrincipal) ss.getCurrentUser();
			mp.setActualPrincipal(p);
		}
	}

	public void userLogout(UserLoginEvent arg0) {
		// on user log out, totally discard the current session and replace with another
		// uninitialized one, and change the _identity_ of this session.
		// this will flush all the session-based services too - so we start afresh.
		MutablePrincipal mp = (MutablePrincipal)ss.getCurrentUser();
		mp.setActualPrincipal(new UnauthenticatedPrincipal());
	}

// sessionManager interface

	public String createNewSession(long minutes)  {
		final String sid = generateSessionId();
		Principal newSession = new MutablePrincipal();
		sessionMap.put(sid,newSession);
		// now attach ourselves to this session's instance of community too - so we receive notifications there.
		Principal previousSession = currentSession();
		try {
			adoptSession(newSession);
			comm.addUserLoginListener(this); // we now have access to the other sessions's community service.
		} finally {
			adoptSession(previousSession);
		}
		
		Duration millis = new Duration(minutes * 60 * 1000);
		scheduler.executeAfterDelay(millis,new Runnable() {
			public void run() {
				dispose(sid);
			}
		});
		return sid;
	}
	
	public void dispose(String arg0)  {
		if (defaultSessionId.equals(arg0)) {
			return; // ignore requests to dispose default session.
		}
		// remove ourselves as a listener to this session (necessary?)
		Principal p = (Principal)sessionMap.remove(arg0); 
		if (p == null) {
			return;
		}
		Principal previousSession = currentSession();
		try {
			adoptSession(p);
			comm.removeUserLoginListener(this);
		} finally {
			adoptSession(previousSession);
		}
		// remove from webserver
		if (ws.getContextBase(arg0) != null) {
			ws.dropContext(arg0);
		}
		//rmi - no additional machinery to clean up.
	}	

	
	//overridden to create new contexts on webserver if not already present.
	public URL findHttpSession(String arg0) throws InvalidArgumentException {
		if (! exists(arg0)) {
			throw new InvalidArgumentException(arg0);
		}
		URL base = ws.getContextBase(arg0);
		if (base == null) {
			base = ws.createContext(arg0);
		}
		return base;
	}


	public URL findXmlRpcSession(String arg0) throws InvalidArgumentException {
		if (! exists(arg0)) {
			throw new InvalidArgumentException(arg0);
		}
		URL base = ws.getContextBase(arg0);
		if (base == null) {
			base = ws.createContext(arg0);
		}
		try {
			return new URL(base,"xmlrpc");
		} catch (MalformedURLException x) {
			// unlikely.
			throw new InvalidArgumentException(x);
		}					
	}
	



}
