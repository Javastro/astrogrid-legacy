package org.astrogrid.client;

import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import org.astrogrid.acr.*;
import org.astrogrid.acr.SecurityException;

public class Finder {
    
    // configure the logger earliest.
    static {
        if (System.getProperty("log4j.configuration") == null && isArOnClasspath() ) {
            System.setProperty("log4j.configuration","default-log4j.properties");
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
            logger.info("Host AR shutting down");
            f.acr = null;
        }

        public String lastChance() {
            return null; // won't ever object.
        }
    }

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
        logger.info("Searching for AR");
        ACR result = null;    

        // try starting internal service
        if (tryToStartIfNotRunning) {
            if (isArOnClasspath()) {
                try {

                    result = createInternal();
                    if (result != null) {
                        return result;
                    }     
                } catch (final Exception e) {
                    logger.warn("Failed to create internal AR",e);
                    e.printStackTrace();
                }
            }            
        }
        // fallen through everything.
        throw new ACRException("Failed to find or create an AR to connect to");
    }


    /**

     * @throws FileNotFoundException
     * @throws IOException
     */
    protected int parseConfigFile() throws IOException {
        final File conf = configurationFile();  
        if (!conf.exists()) {
            logger.info("No configuration file - suggests an AR instance is not running at the moment");
            throw new FileNotFoundException(conf.getAbsolutePath());
        }

        logger.info("configuration file indicates an AR is already running");                
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


    /** attempt to start an in-process ar
     * pre-req - isArOnClasspath == true
     * @return
     * @throws InstantiationException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private ACR createInternal() throws InstantiationException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        // all done without referencing the class by name - as may not be available on the classpath.
        final Class buildClass = findInProcessARClass(); 
        final Object o = buildClass.newInstance();
        // start the acr.
        Method m = buildClass.getMethod("start",(Class[])null);
        m.invoke(o,(Object[])null);
        // return the acr instance
        m = buildClass.getMethod("getACR",(Class[])null);
        final ACR acr =  (ACR)m.invoke(o,(Object[])null);
        return acr;
    }

    /**
     * @return
     */
    private static Class findInProcessARClass() {
        Class buildClass;
        try {
            buildClass = Class.forName("org.astrogrid.desktop.BuildInprocessACR");
        } catch (final ClassNotFoundException e) {            
            //can't log at this point.
            //logger.info("AstroRuntime implementation classes not on classpath - must connect to a remote AR",e);
            return null;
        }
        return buildClass;
    }

    private static boolean isArOnClasspath() {
        return findInProcessARClass() != null;
    }



}
