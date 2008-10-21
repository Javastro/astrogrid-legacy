/*$Id: Finder.java,v 1.19 2008/10/21 13:09:08 nw Exp $
 * Created on 26-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr;

import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.SessionManager;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.acr.system.ApiHelp;

/** Connect to an AstroRuntime Instance using RMI.
 * 
 * The Astro Runtime instance is found by 
 * <ol>
      <li> First attempting to connect to a running instance using RMI, on a port defined in the file <tt>~/.acr-rmi-port</tt> 
      (this file is written by a running AR instance)</li>
     <li> failing that, tries to create an instance internally (which will only work if implementation classes are on classpath),</li>
     <li> failing that, will prompt the user to start an instance of the AR</li>
 * </ol>
     The interface returned will either be a  RMI stub or direct instance, depending on how the AR was found.
     * No matter how the AR is found, the ACR interface returned is a singleton - it is stored in this class for simple access the next time
 * {@example "Connecting to AstroRuntime"
 * import org.astrogrid.acr.builtin.ACR;
 * import org.astrogrid.acr.Finder;
 * Finder f = new Finder();
 * ACR acr = f.find(); // find the AR 
 * }
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 26-Jul-2005
 *@see org.astrogrid.acr.builtin.ACR How to retrieve services from the ACR interface
 *@note This class can also be used for direct-java connection to an AstroRuntime, if the AR is running in the same JVM as the calling code.
 *@xmlrpc This class is not used for XML-RPC connections to AstroRuntime. For instructions for Python, Perl, C, etc 
 *see the <a href="{@docRoot}/overview-summary.html">overview documentation</a>.
 */
public class Finder {
    /** Part of the internal implementation
     * @exclude
     * <p/>
     *  rmi stub that connects to a remote acr instance.
	 * @author Noel.Winstanley@manchester.ac.uk

	 */
	public static final class RmiAcr implements ACR {
		// used to connect to the remote acr
		final SessionAwareClient client;
		// used to map names to classes. - lazily initialized.
		private  ApiHelp api;
		
		private synchronized ApiHelp getApiHelp() throws RemoteException, NotBoundException {
			if (api == null) {
				api = (ApiHelp)client.lookup(ApiHelp.class);
			} 
			return api;
		}

		private RmiAcr(final SessionAwareClient client) {
			this.client = client;
		}
		
		public Object getService(final Class interfaceClass) throws ACRException, NotFoundException {
			if (interfaceClass.equals(ACR.class)) {
				return this;
			}
			try {
				registerListeners(interfaceClass);
				return this.client.lookup(interfaceClass);
			} catch (final RemoteException e) {
				throw new ACRException(e);
			} catch (final NotBoundException e) {
				throw new NotFoundException(e);
			}
		}

		private void registerListeners(final Class c) {
			final Method[] arr = c.getMethods();
			for (int i = 0; i < arr.length; i++) {
				final Method m = arr[i];
				final Class[] ps = m.getParameterTypes();
				for (int j = 0; j < ps.length; j++) {
					maybeRegister(ps[j]);
				}
				final Class ret = m.getReturnType();
				maybeRegister(ret);
			}
		}

		private void maybeRegister(final Class c) {
			if (c.isInterface() &&   c.getName().endsWith("Listener")) {
				logger.debug("Exporting interface " + c.getName());
				this.client.exportInterface(c);
			}
		}

		public Object getService(final String componentName) throws ACRException, NotFoundException {
			Class clazz = null;
			try {
				clazz = Class.forName(componentName); // makes the interface more user friendly - can take either a short name of fully-qualified name.
			} catch (final ClassNotFoundException e) {
				// try to resolve o
				try {
					final String className = getApiHelp().interfaceClassName(componentName);
					clazz = Class.forName(className);
				} catch (final ClassNotFoundException e1) {
					throw new NotFoundException(e1);
				} catch (final RemoteException x) {
					throw new ACRException(x);
				} catch (final NotBoundException x) {
					throw new ACRException(x);
				}
			}
			return getService(clazz);
		}
	}

	/**Part of the internal implementation 
	 * @exclude
     * <p />
     * Refactored as an static public class - previously was an anonymous class, and RmiLite seemed to be unable to call it
     *  - producing a nice stack trace on shutdown. Same code, but as a named public static class works fine.
	 * @author Noel Winstanley

	 */
	public static final class FinderCleanupShutdownListener implements ShutdownListener {
		public FinderCleanupShutdownListener(final Finder f) {
			this.f = f;
		}
		private final Finder f;
		public void halting() {
		    logger.info("Host ACR shutting down");
		    f.acr = null;
		}

		public String lastChance() {
		    return null; // won't ever object.
		}
	}

	/** Webstart URL for the ACR
	 * @exclude */
    public static final String ACR_JNLP_URL = "http://software.astrogrid.org/jnlp/astro-runtime/astro-runtime.jnlp";
    /**
     * Commons Logger for this class
     * @exclude
     */
    protected static final Log logger = LogFactory.getLog(Finder.class);

    /** Construct a new Finder
     * 
     */
    public Finder() {
        super();
    }
    
    /** Find or create an Astro Runtime (AR) instance.
     * @return an interface to the Astro Runtime - depending on how connected will either 
     * be a direct instance or a remote stub - although this makes no difference to the 
     * consumer.
     * @note The instance returned is a singleton - i.e. all subsequent calls to  this method
     * will return the same object.
     * @throws ACRException if all options fail
     * @equivalence findSession(true,false)
     * */
    public synchronized ACR find()  throws ACRException{
    	final boolean tryToStartIfNotRunning = true;
    	final boolean warnUserBeforeStarting = false;
         
        return find(tryToStartIfNotRunning, warnUserBeforeStarting);

    }
    
    /** Find an Astro Runtime instance for a specific session. 
     * 
     * Unlike {@link #find()} this method will not start the Astro Runtime service if it is 
     * not already running - the Astro Runtime must be running first.
     * @warning Still Experimental.
     * @param sessionId the identifier of a current session
     * @return an AR instance that is connected to the specified session
	 * @throws InvalidArgumentException if the sessionId is invalid.
	 * @throws NotApplicableException if a connection has not already been made to an AR instance, or AR is an older version without session support
	 * @throws ServiceException if there is an error connecting to this session.
	 * @see org.astrogrid.acr.builtin.SessionManager
     */
    public ACR findSession(final String sessionId) throws InvalidArgumentException, NotApplicableException, ServiceException{
    	if (this.acr == null) {
    		throw new NotApplicableException("You must previously connect to an ACR before retrieving a session");
    	}
    	SessionManager sess;
		try {
			sess = (SessionManager)acr.getService(SessionManager.class);
		} catch (final NotFoundException x) {
			throw new NotApplicableException("This ACR does not support sessions");
		} catch (final ACRException x) {
			throw new ServiceException(x);
		}
    	if (! sess.exists(sessionId)) {
    		throw new InvalidArgumentException("Not a current session " + sessionId);
    	}
    	if (acr instanceof RmiAcr) { // we're in remote mode.
    		
    		try {
				return connectExternalSession(sessionId);
			} catch (final Exception x) {
				throw new ServiceException(x);
			}
    	} else { // we've got a direct connection - dunno what to do here.
    		//@fixme work out how to sessionify a direct connection
    		throw new ServiceException("Not implemented for direct method connections");
    	}
    }

    /**
     * Find or create an Astro Runtime (AR) instance.
     * @see #find()
     * @param tryToStartIfNotRunning if false, will not attempt to start an AR, but instead will return NULL if there isn't an instance already running
     * @param warnUserBeforeStarting if true, will warn the user before attempting to start an AR, giving them the chance to start one manually
     * @return an instance of the Astro Runtime
     * @throws ACRException
     */
	public ACR find(final boolean tryToStartIfNotRunning, final boolean warnUserBeforeStarting) throws ACRException {
		if (acr == null) {
            acr= createACR(tryToStartIfNotRunning, warnUserBeforeStarting);
            try { // attempt to register a listener, it it'll let me: use it to remove singleton when host vanishes.
                final Shutdown sd = (Shutdown)acr.getService(Shutdown.class);               
                sd.addShutdownListener(new FinderCleanupShutdownListener(this));
              
            } catch (final ACRException e) {
                logger.warn("Failed to register shutdown listener - no matter",e);
            }
                
        }
		return acr;
	}
    
    /**
     * @param userWarning If not null, prompt the user <b>before</b> attempting to start an external ACR.
     * @param tryToStartIfNotRunning if false, don't start an external ACR if there isn't one. 
     * @throws NoAvailableACRException
     */
    private ACR createACR(final boolean tryToStartIfNotRunning, final boolean warnUser) throws ACRException {
    	logger.info("Searching for acr");
    	ACR result = null;
    	try {
    		result = connectExternal();
    		if (result != null) {
    			return result;
    		}
    	} catch (final Exception e) {
    		logger.warn("Failed to connect to existing external acr",e);
    	}                    
    	
    	try {
    		result = createInternal();
    		if (result != null) {
    			return result;
    		}    
    	} catch (final Exception e) {
    		logger.warn("Failed to create internal acr",e);
    	}
    	// hmm, try starting external service
    	if (tryToStartIfNotRunning) {
    		try {
    			if (warnUser) {
    				try {
    					//warn before attempting to start, it's only polite
    					JOptionPane.showMessageDialog(null,"<html>This application requires the ACR.<br>Either start it yourself and press 'OK', or simply press 'OK' to auto-start it.</html>",
    							"ACR Required",JOptionPane.INFORMATION_MESSAGE);
    					//Try again, maybe they started it now
    					result = connectExternal();
    					if (result != null) {
    						return result;
    					}
    				} catch (final HeadlessException he) {
    					logger.warn("Not running in a ui environment - can't ask permission, so starting ACR anyway");
    				} catch (final Exception e) {
    					logger.warn("Failed to connect to external acr.",e);
    				}  
    			}
    			logger.info("Still no ACR, so attempt to create one");
    			createExternal();
    			// need to wait some time to allow external to bootup (and maybe download).
    			
    			final long now = System.currentTimeMillis();
    			final int WAIT_TIME = (2 * 60  * 1000); // 2 minutes
				long tooLong = now + WAIT_TIME ; 
    			while (connectExternal()==null) {
    				if (System.currentTimeMillis()>tooLong) {
    					//prompt and see if we want to carry on waiting
    		    		int continueToWait;
    		    		try {
    		    			continueToWait = JOptionPane.showConfirmDialog(null,"<html>The ACR hasn't started yet.  Press OK to continue waiting, Cancel to abort.</html>","ACR not started",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
    		    		} catch (final HeadlessException e) {
    		    			logger.warn("Not running in a ui environment - can't prompt");
    		    			continueToWait = JOptionPane.CANCEL_OPTION;
    		    		}
    		    		if (continueToWait == JOptionPane.CANCEL_OPTION) {
    		    			break;
    		    		} else {
    		    			tooLong += WAIT_TIME;
    		    		}
    				}
    				Thread.sleep(5000); // 5 seconds.
    			}
    			result = connectExternal();
    			if (result != null) {
    				return result;
    			}
    		} catch (final Exception e) {
    			logger.warn("Failed to create external acr",e);
    		}            
    		// finally - try prompting the user.
    		int dialogueResult;
    		try {
    			dialogueResult = JOptionPane.showConfirmDialog(null,"<html><b>Please start the ACR by hand</b><br>When started press 'Ok'. To halt press 'Cancel'","Unable to automatically start ACR",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
    		} catch (final HeadlessException e) {
    			logger.warn("Not running in a ui environment - can't prompt");
    			dialogueResult = JOptionPane.NO_OPTION;
    		}
    		
    		if (dialogueResult == JOptionPane.YES_OPTION) { // try to connect again.
    			try {
    				result = connectExternal();
    				if (result != null) {
    					return result;
    				}
    			} catch (final Exception e) {
    				logger.warn("Failed to connect to external acr, after user claimed to start one.",e);
    				// could loop here?
    			}                  
    		}
    	}
    	// fallen through everything.
    	throw new ACRException("Failed to find or create an ACR to connect to");
    }

    /** connect to an external AR.

     * @throws FileNotFoundException
     * @throws NumberFormatException
     * @throws IOException
     * @throws RemoteException
     * @throws NotBoundException
     */
    protected ACR connectExternal() throws FileNotFoundException, NumberFormatException, IOException, RemoteException, NotBoundException {
    	final int port = parseConfigFile();
    	final SessionAwareClient client = new SessionAwareClient("localhost",port);

    	final ACR newAcr = new RmiAcr(client);
    	//TODO check that the ACR is booted?  Sometimes the config file is present before the ACR is ready.
    	return newAcr;           
    }
    
    protected ACR connectExternalSession(final String sessionId) throws FileNotFoundException, NumberFormatException, IOException, RemoteException, NotBoundException {
    	final int port = parseConfigFile();
    	final SessionAwareClient client = new SessionAwareClient("localhost",port,sessionId);

    	final ACR newAcr = new RmiAcr(client);
    	//TODO check that the ACR is booted?  Sometimes the config file is present before the ACR is ready.
    	return newAcr;           
    }

	/**

	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected int parseConfigFile() throws IOException {
		final File conf = configurationFile();  
    	if (!conf.exists()) {
    		logger.info("No configuration file - suggests an acr instance is not running at the moment");
    		throw new FileNotFoundException(conf.getAbsolutePath());
    	}
    
    	logger.info("configuration file indicates an acr is already running");                
    	BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(conf));
			final int port = Integer.parseInt(br.readLine());
			logger.info("Port determined to be " + port);
			return port;
		} finally {
			if (br != null) {
				try {
					br.close(); //Otherwise the file can be locked and left undeleted when the ACR shuts down.
				} catch (final IOException e) {
				}
			}
		}
	}
   
    
    private ACR acr;
    
    /** The AR RMI connection file.
     * <p/>
     * This is <tt>~/.acr-rmi-port</tt> */
    public static final File configurationFile() {
        final File homeDir = new File(System.getProperty("user.home"));
        return new File(homeDir,".acr-rmi-port");
    }
    
    
    
    /** create an external instance of the acr 
     * would like to be able to do this by fetching the jnlp file - however, don't know that this process is 
     * running under javaws - and so need to find another library to control the system browser.
     * blechh.
     * for now, only create an external acr if running under javaws.
     * @todo add browser control lib to do this in other circumstances.

 
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws MalformedURLException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws ClassNotFoundException
     * @throws ClassNotFoundException
     */
    private void createExternal() throws SecurityException, NoSuchMethodException, MalformedURLException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        Method showMethod = null;
        Object methodTarget = null;        
        try {
             final Class managerClass = Class.forName("javax.jnlp.ServiceManager");
             final Method lookupMethod= managerClass.getMethod("lookup",new Class[]{String.class});
             methodTarget = lookupMethod.invoke(null,new Object[]{"javax.jnlp.BasicService"});
             showMethod = methodTarget.getClass().getMethod("showDocument",new Class[]{URL.class});
        } catch (final ClassNotFoundException e) {
            logger.info("Not running under java web start");
        }
        if (showMethod == null) { // try something else.
            try {
            final Class jdicClass = Class.forName("org.jdesktop.jdic.desktop.Desktop");
            showMethod= jdicClass.getMethod("browse",new Class[]{URL.class});    
            } catch (final ClassNotFoundException e1) {
                logger.info("Not running with jdic libs");
            }
        }
        // @todo add in code for other browser control class here.
        
        if (showMethod == null) {
            throw new ClassNotFoundException("Can't find any class that can control the system browser");
        }
        final URL url = new URL(ACR_JNLP_URL);
        showMethod.invoke(methodTarget,new Object[]{url});        
    }
    
    private ACR createInternal() throws InstantiationException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        // all done without referencing the class by name - as may not be available on the classpath.
        try {
            final Class buildClass = Class.forName("org.astrogrid.desktop.BuildInprocessACR");
            final Object o = buildClass.newInstance();
            // start the acr.
            Method m = buildClass.getMethod("start",(Class[])null);
            m.invoke(o,(Object[])null);
            // return the acr instance
            m = buildClass.getMethod("getACR",(Class[])null);
            return (ACR)m.invoke(o,(Object[])null);
        } catch (final ClassNotFoundException e) {            
            logger.info("ACR implementation classes not available - must connect to a remote acr",e);
        } 
        return null;
    }
}


/* 
$Log: Finder.java,v $
Revision 1.19  2008/10/21 13:09:08  nw
Incomplete - taskapi documentation

Incomplete - taskUseful scripts / Examples.

Complete - taskwrite up examples.

Revision 1.18  2008/09/25 16:02:09  nw
documentation overhaul

Revision 1.17  2008/08/21 11:34:03  nw
doc tweaks.

Revision 1.16  2007/11/12 13:36:27  pah
change parameter name to structure

Revision 1.15  2007/03/22 18:54:10  nw
added support for sessions.

Revision 1.14  2007/01/24 14:04:45  nw
updated my email address

Revision 1.13  2007/01/23 19:58:29  nw
made finder more overridable, and improved cleanup code. (added a finally block)

Revision 1.12  2006/10/30 12:12:36  nw
documentation improvements.

Revision 1.11  2006/10/12 02:22:33  nw
fixed up documentaiton

Revision 1.10  2006/08/31 20:20:42  nw
minor tweak

Revision 1.9  2006/06/12 10:10:54  nw
fixed shutdown bug.

Revision 1.8  2006/05/22 20:26:52  jdt
Updated the auto-start URL.  Will need to think how we now handle
the multiple AR flavours

Revision 1.7  2006/04/14 19:41:46  jdt
Added some new features to the Finder and made it more robust.  It's now more of a rats' nest though and so could do with some refactoring.

Revision 1.6  2006/02/02 14:19:47  nw
fixed up documentation.

Revision 1.5  2005/08/25 16:59:44  nw
1.1-beta-3

Revision 1.4  2005/08/16 13:16:23  nw
doc fix

Revision 1.3  2005/08/12 12:42:05  nw
finished documentation effort.

Revision 1.2  2005/08/12 08:45:15  nw
souped up the javadocs

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/