/**
 * 
 */
package org.astrogrid.desktop.modules.auth;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.hivelock.SecurityService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.desktop.alternatives.SingleSessionManager;
import org.astrogrid.desktop.modules.system.SchedulerInternal;
import org.astrogrid.desktop.modules.system.WebServerInternal;
import org.astrogrid.desktop.modules.system.SchedulerInternal.DelayedContinuation;
import org.joda.time.DateTime;
import org.joda.time.Duration;

/** Manages sessions and interfaces between {@code Community} and hivelock library.
 * <p/>
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

	public SessionManagerImpl(final SecurityService s, final WebServerInternal ws,final CommunityInternal comm, final SchedulerInternal scheduler) {
		super(s,ws);
		this.scheduler = scheduler;
		this.comm = comm;
		
	}
	private final SchedulerInternal scheduler;
	private final CommunityInternal comm;
	
	@Override
    public void attemptUpgrade(){	
	    logger.debug("Attempting upgrade of " + ss.getCurrentUser());
		comm.guiLogin();		
		logger.debug("Called comm.guiLogin(), returning");
		// and now wait for the callback, if any.
	}

	// listener interface 
	// callback from community - either triggered by attemptUpgrade(), or oone of the methods in community.
	public void userLogin(final UserLoginEvent arg0) {
                // This principal is non-null if the community object has signed on
                // at a community service. It is of the form ivo://user@authority/resource-key.
                final Principal p = comm.getSecurityGuard().getAccountIvorn();
		if (p != null) {
			final MutablePrincipal mp = (MutablePrincipal) ss.getCurrentUser();
			mp.setActualPrincipal(p);
		}
	}

	public void userLogout(final UserLoginEvent arg0) {
		// on user log out, totally discard the current session and replace with another
		// uninitialized one, and change the _identity_ of this session.
		// this will flush all the session-based services too - so we start afresh.
		final MutablePrincipal mp = (MutablePrincipal)ss.getCurrentUser();
		mp.setActualPrincipal(new UnauthenticatedPrincipal());
	}

// sessionManager interface

	@Override
    public String createNewSession(final long inactivityMinutes)  {
		final String sid = generateSessionId();
		final MutablePrincipal newSession = new MutablePrincipal();
		sessionMap.put(sid,newSession);
		// now attach ourselves to this session's instance of community too - so we receive notifications there.
		final Principal previousSession = currentSession();
		try {
			adoptSession(newSession);
			comm.addUserLoginListener(this); // we now have access to the other sessions's community service.
		} finally {
			adoptSession(previousSession);
		}
		
		final SessionExpirer expirer = new SessionExpirer(sid,inactivityMinutes);
		expirerMap.put(newSession,expirer);
		scheduler.schedule(expirer);
		return sid;
	}
	
	protected final Map<Principal,SessionExpirer> expirerMap = new HashMap<Principal,SessionExpirer>();
	
	
	/** overridden to reset inactivity timer associated with the session */
	@Override
	public void adoptSession(final Principal p) {
	    final SessionExpirer expirer = expirerMap.get(p);
	    if (expirer != null) {
	        expirer.reset();
	    }
	    super.adoptSession(p);
	}
	/** scheduled task that expires a session, if no activity is seen */
	private class SessionExpirer implements SchedulerInternal.DelayedContinuation {

	    private final String sid;
	    private final Duration threshold;
	    private volatile DateTime latestActivity; // volatile, as may be reset by a different thread.
	    private Duration delay;
	    
	    /** called to reset the inactivity timer */
	    public void reset() {
	        latestActivity = new DateTime();
	    }
        public SessionExpirer(final String sid, final long inactivityMinutes) {
            super();
            this.latestActivity = new DateTime();
            this.sid = sid;
            this.threshold = new Duration(inactivityMinutes * 60 * 1000);
            this.delay = this.threshold;
        }

        public DelayedContinuation execute() {
            final DateTime now = new DateTime();
            final Duration inactivity = new Duration(latestActivity,now);
            if (! inactivity.isShorterThan(threshold)) { // i.e. inactivity >= threshold
                dispose(sid);
                return null;
            } else {
                // some activity happended, need to wait again.
                delay = threshold.minus(inactivity); // wait for the remaining amount of the threshold
                return this;
            }
        }

        public Duration getDelay() {
            return delay;
        }

        public Principal getPrincipal() {
            return null; // doesn't matter which principal this runs as.
        }

        public String getTitle() {
            return "Expirer for Session " + sid;
        }
	}
	
	@Override
    public void dispose(final String arg0)  {
		if (defaultSessionId.equals(arg0)) {
			return; // ignore requests to dispose default session.
		}
		// remove ourselves as a listener to this session (necessary?)
		final Principal p = sessionMap.remove(arg0); 
		if (p == null) {
			return;
		}
		final Principal previousSession = currentSession();
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
	@Override
    public URL findHttpSession(final String arg0) throws InvalidArgumentException {
		if (! exists(arg0)) {
			throw new InvalidArgumentException(arg0);
		}
		URL base = ws.getContextBase(arg0);
		if (base == null) {
			base = ws.createContext(arg0);
		}
		return base;
	}


	@Override
    public URL findXmlRpcSession(final String arg0) throws InvalidArgumentException {
		if (! exists(arg0)) {
			throw new InvalidArgumentException(arg0);
		}
		URL base = ws.getContextBase(arg0);
		if (base == null) {
			base = ws.createContext(arg0);
		}
		try {
			return new URL(base,"xmlrpc");
		} catch (final MalformedURLException x) {
			// unlikely.
			throw new InvalidArgumentException(x);
		}					
	}
	



}
