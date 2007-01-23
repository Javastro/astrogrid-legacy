/*$Id: Finder.java,v 1.13 2007/01/23 19:58:29 nw Exp $
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

import net.ladypleaser.rmilite.Client;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.acr.system.ApiHelp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


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

/** Find or create an AR daemon, and return an interface to it.
 * 
 * <p/>
      * First attempts to connect to a running instance using RMI, on a port defined in the file <tt>~/.acr-rmi-port</tt> (which is written by a running AR instance)<p />
     * failing that, tries to create an external instance (will only work if running under java web start), and then connect to that using RMI<p />
     * failing that, trie to create an instance internally (will only work if implementation classes are on classpath),<p />     
     * the interface returned will either be a  RMI stub or direct instance, depending on how the AR was found.
     * <p />
     * No matter how the AR is found, the ACR interface returned is a singleton - it is stored in this class for simple access the next time
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Jul-2005
 * @example
 * <pre>
 * import org.astrogrid.acr.builtin.ACR;
 * import org.astrogrid.acr.Finder;
 * Finder f = new Finder();
 * ACR acr = f.find(); // find the AR 
 * </pre>
 *@see org.astrogrid.acr.builtin.ACR How to retrieve services from the ACR interface
 */
public class Finder {
    /**Internal Class. 
     * 
     * Refactored as an static public class - previously was an anonymous class, and RmiLite seemed to be unable to call it
     *  - producing a nice stack trace on shutdown. Same code, but as a named public static class works fine.
	 * @author Noel Winstanley
	 * @since Jun 12, 200611:03:25 AM
	 */
	public static final class FinderCleanupShutdownListener implements ShutdownListener {
		public FinderCleanupShutdownListener(Finder f) {
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

	/** Webstart URL for the ACR */
    public static final String ACR_JNLP_URL = "http://software.astrogrid.org/jnlp/astro-runtime/astro-runtime.jnlp";
    /**
     * Commons Logger for this class
     */
    protected static final Log logger = LogFactory.getLog(Finder.class);

    /** Construct a new Finder
     * 
     */
    public Finder() {
        super();
    }
    
    /** Find or create an AR daemon.
     * @return an interface to the running CR - depending on how connected will either 
     * be a direct instance or a remote stub - although this makes no difference to the 
     * consumer.
     * The instance returned is a singleton - i.e. all subsequent calls to {@link #find} 
     * will return the same object.
     * @throws ACRException if all options fail
     * 
     * */
    public synchronized ACR find()  throws ACRException{
    	boolean tryToStartIfNotRunning = true;
    	boolean warnUserBeforeStarting = false;
         
        return find(tryToStartIfNotRunning, warnUserBeforeStarting);

    }

    /**
     * Find or create an AR daemon.
     * @see #find()
     * @param tryToStartIfNotRunning if false, will not attempt to start an ACR, but merely return NULL if there isn't one running
     * @param warnUserBeforeStarting if true, will warn the user before attempting start an ACR, giving him the chance to start one manually
     * @return
     * @throws ACRException
     */
	public ACR find(boolean tryToStartIfNotRunning, boolean warnUserBeforeStarting) throws ACRException {
		if (acr == null) {
            acr= createACR(tryToStartIfNotRunning, warnUserBeforeStarting);
            try { // attempt to register a listener, it it'll let me: use it to remove singleton when host vanishes.
                Shutdown sd = (Shutdown)acr.getService(Shutdown.class);               
                sd.addShutdownListener(new FinderCleanupShutdownListener(this));
              
            } catch (ACRException e) {
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
    private ACR createACR(boolean tryToStartIfNotRunning, boolean warnUser) throws ACRException {
    	logger.info("Searching for acr");
    	ACR result = null;
    	try {
    		result = connectExternal();
    		if (result != null) {
    			return result;
    		}
    	} catch (Exception e) {
    		logger.warn("Failed to connect to existing external acr",e);
    	}                    
    	
    	try {
    		result = createInternal();
    		if (result != null) {
    			return result;
    		}    
    	} catch (Exception e) {
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
    				} catch (HeadlessException he) {
    					logger.warn("Not running in a ui environment - can't ask permission, so starting ACR anyway");
    				} catch (Exception e) {
    					logger.warn("Failed to connect to external acr.",e);
    				}  
    			}
    			logger.info("Still no ACR, so attempt to create one");
    			createExternal();
    			// need to wait some time to allow external to bootup (and maybe download).
    			
    			long now = System.currentTimeMillis();
    			final int WAIT_TIME = (2 * 60  * 1000); // 2 minutes
				long tooLong = now + WAIT_TIME ; 
    			while (connectExternal()==null) {
    				if (System.currentTimeMillis()>tooLong) {
    					//prompt and see if we want to carry on waiting
    		    		int continueToWait;
    		    		try {
    		    			continueToWait = JOptionPane.showConfirmDialog(null,"<html>The ACR hasn't started yet.  Press OK to continue waiting, Cancel to abort.</html>","ACR not started",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
    		    		} catch (HeadlessException e) {
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
    		} catch (Exception e) {
    			logger.warn("Failed to create external acr",e);
    		}            
    		// finally - try prompting the user.
    		int dialogueResult;
    		try {
    			dialogueResult = JOptionPane.showConfirmDialog(null,"<html><b>Please start the ACR by hand</b><br>When started press 'Ok'. To halt press 'Cancel'","Unable to automatically start ACR",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
    		} catch (HeadlessException e) {
    			logger.warn("Not running in a ui environment - can't prompt");
    			dialogueResult = JOptionPane.NO_OPTION;
    		}
    		
    		if (dialogueResult == JOptionPane.YES_OPTION) { // try to connect again.
    			try {
    				result = connectExternal();
    				if (result != null) {
    					return result;
    				}
    			} catch (Exception e) {
    				logger.warn("Failed to connect to external acr, after user claimed to start one.",e);
    				// could loop here?
    			}                  
    		}
    	}
    	// fallen through everything.
    	throw new ACRException("Failed to find or create an ACR to connect to");
    }

    /** connect to an external acr.
     * @return
     * @throws FileNotFoundException
     * @throws NumberFormatException
     * @throws IOException
     * @throws RemoteException
     * @throws NotBoundException
     */
    protected ACR connectExternal() throws FileNotFoundException, NumberFormatException, IOException, RemoteException, NotBoundException {
    	int port = parseConfigFile();
    	final Client client = new Client("localhost",port);
    	final ApiHelp api = (ApiHelp)client.lookup(ApiHelp.class);
    	ACR newAcr = new ACR() {
    		
    		public Object getService(Class interfaceClass) throws ACRException, NotFoundException {
    			if (interfaceClass.equals(ACR.class)) {
    				return this;
    			}
    			try {
    				registerListeners(interfaceClass);
    				return client.lookup(interfaceClass);
    			} catch (RemoteException e) {
    				throw new ACRException(e);
    			} catch (NotBoundException e) {
    				throw new NotFoundException(e);
    			}
    		}
    		
    		private void registerListeners(Class c) {
    			Method[] arr = c.getMethods();
    			for (int i = 0; i < arr.length; i++) {
    				Method m = arr[i];
    				Class[] ps = m.getParameterTypes();
    				for (int j = 0; j < ps.length; j++) {
    					maybeRegister(ps[j]);
    				}
    				Class ret = m.getReturnType();
    				maybeRegister(ret);
    			}
    		}
    		private void maybeRegister(Class c) {
    			if (c.isInterface() &&   c.getName().endsWith("Listener")) {
    				logger.debug("Exporting interface " + c.getName());
    				client.exportInterface(c);
    			}
    		}
    		
    		
    		public Object getService(String componentName) throws ACRException, NotFoundException {
    			Class clazz = null;
    			try {
    				clazz = Class.forName(componentName); // makes the interface more user friendly - can take either a short name of fully-qualified name.
    			} catch (ClassNotFoundException e) {
    				// try to resolve o
    				try {
    					String className = api.interfaceClassName(componentName);
    					clazz = Class.forName(className);
    				} catch (ClassNotFoundException e1) {
    					throw new NotFoundException(e1);
    				}
    			}
    			return getService(clazz);
    		}
    	};
    	//TODO check that the ACR is booted?  Sometimes the config file is present before the ACR is ready.
    	
    	return newAcr;           
    	
    }

	/**
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected int parseConfigFile() throws IOException {
		File conf = configurationFile();  
    	if (!conf.exists()) {
    		logger.info("No configuration file - suggests an acr instance is not running at the moment");
    		throw new FileNotFoundException(conf.getAbsolutePath());
    	}
    
    	logger.info("configuration file indicates an acr is already running");                
    	BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(conf));
			int port = Integer.parseInt(br.readLine());
			logger.info("Port determined to be " + port);
			return port;
		} finally {
			if (br != null) {
				try {
					br.close(); //Otherwise the file can be locked and left undeleted when the ACR shuts down.
				} catch (IOException e) {
				}
			}
		}
	}
   
    
    private ACR acr;
    
    public static final File configurationFile() {
        File homeDir = new File(System.getProperty("user.home"));
        return new File(homeDir,".acr-rmi-port");
    }
    
    
    
    /** create an external instance of the acr 
     * would like to be able to do this by fetching the jnlp file - however, don't know that this process is 
     * running under javaws - and so need to find another library to control the system browser.
     * blechh.
     * for now, only create an external acr if running under javaws.
     * @todo add browser control lib to do this in other circumstances.

     * @return
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
             Class managerClass = Class.forName("javax.jnlp.ServiceManager");
             Method lookupMethod= managerClass.getMethod("lookup",new Class[]{String.class});
             methodTarget = lookupMethod.invoke(null,new Object[]{"javax.jnlp.BasicService"});
             showMethod = methodTarget.getClass().getMethod("showDocument",new Class[]{URL.class});
        } catch (ClassNotFoundException e) {
            logger.info("Not running under java web start");
        }
        if (showMethod == null) { // try something else.
            try {
            Class jdicClass = Class.forName("org.jdesktop.jdic.desktop.Desktop");
            showMethod= jdicClass.getMethod("browse",new Class[]{URL.class});    
            } catch (ClassNotFoundException e1) {
                logger.info("Not running with jdic libs");
            }
        }
        // @todo add in code for other browser control class here.
        
        if (showMethod == null) {
            throw new ClassNotFoundException("Can't find any class that can control the system browser");
        }
        URL url = new URL(ACR_JNLP_URL);
        showMethod.invoke(methodTarget,new Object[]{url});        
    }
    
    private ACR createInternal() throws InstantiationException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        // all done without referencing the class by name - as may not be available on the classpath.
        try {
            Class buildClass = Class.forName("org.astrogrid.desktop.BuildInprocessACR");
            Object o = buildClass.newInstance();
            // start the acr.
            Method m = buildClass.getMethod("start",null);
            m.invoke(o,null);
            // return the acr instance
            m = buildClass.getMethod("getACR",null);
            return (ACR)m.invoke(o,null);
        } catch (ClassNotFoundException e) {            
            logger.info("ACR implementation classes not available - must connect to a remote acr",e);
        } 
        return null;
    }

}


/* 
$Log: Finder.java,v $
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